# Access enhanced document attributes as custom objects<a name="ddb-en-client-doc-api-convert"></a>

In addition to providing an API to read and write attributes with schemaless structures, the Enhanced Document API lets you convert attributes to and from instances of custom classes\.

The Enhanced Document API uses `AttributeConverterProvider`s and `AttributeConverter`s that were shown in the [control attribute conversion](ddb-en-client-adv-features-conversion.md) section as part of the DynamoDB Enhanced Client API\.

In the following example, we use a `CustomAttributeConverterProvider` with its nested `AddressConverter` class to convert `Address` class instances\. 

This example shows that you can mix data from classes and also data from structures that are built as needed\. This example also shows that custom classes can be used at any level of a nested structure\. The `Address` objects in this example are values used in a map\.

```
    public static void attributeToAddressClassMappingExample(DynamoDbEnhancedClient enhancedClient, DynamoDbClient standardClient) {
        String tableName = "customer";

        // Define the DynamoDbTable for an enhanced document.
        // The schema builder provides methods for attribute converter providers and keys.
        DynamoDbTable<EnhancedDocument> documentDynamoDbTable = enhancedClient.table(tableName,
                DocumentTableSchema.builder()
                        // Add the CustomAttributeConverterProvider along with the default when you build the table schema.
                        .attributeConverterProviders(
                                List.of(
                                        new CustomAttributeConverterProvider(),
                                        AttributeConverterProvider.defaultProvider()))
                        .addIndexPartitionKey(TableMetadata.primaryIndexName(), "id", AttributeValueType.N)
                        .addIndexSortKey(TableMetadata.primaryIndexName(), "lastName", AttributeValueType.S)
                        .build());
        // Create the DynamoDB table if needed.
        documentDynamoDbTable.createTable();
        waitForTableCreation(tableName, standardClient);


        // The getAddressesForCustomMappingExample() helper method that provides 'addresses' shows the use of a custom Address class
        // rather than using a Map<String, Map<String, String> to hold the address data.
        Map<String, Address> addresses = getAddressesForCustomMappingExample();

        // Build an EnhancedDocument instance to save an item with a mix of structures defined as needed and static classes.
        EnhancedDocument personDocument = EnhancedDocument.builder()
                .putNumber("id", 50)
                .putString("firstName", "Shirley")
                .putString("lastName", "Rodriguez")
                .putNumber("age", 53)
                .putNull("nullAttribute")
                .putJson("phoneNumbers", phoneNumbersJSONString())
                // Note the use of 'EnhancedType.of(Address.class)' instead of the more generic
                // 'EnhancedType.mapOf(EnhancedType.of(String.class), EnhancedType.of(String.class))' that was used in a previous example.
                .putMap("addresses", addresses, EnhancedType.of(String.class), EnhancedType.of(Address.class))
                .putList("hobbies", List.of("Hobby 1", "Hobby 2"), EnhancedType.of(String.class))
                .build();
        // Save the item to DynamoDB.
        documentDynamoDbTable.putItem(personDocument);

        // Retrieve the item just saved.
        EnhancedDocument srPerson = documentDynamoDbTable.getItem(Key.builder().partitionValue(50).sortValue("Rodriguez").build());

        // Access the addresses attribute.
        Map<String, Address> srAddresses = srPerson.get("addresses",
                EnhancedType.mapOf(EnhancedType.of(String.class), EnhancedType.of(Address.class)));

        srAddresses.keySet().forEach(k -> logger.info(addresses.get(k).toString()));

        documentDynamoDbTable.deleteTable();

// The content logged to the console shows that the saved maps were converted to Address instances.
Address{street='123 Main Street', city='Any Town', state='NC', zipCode='00000'}
Address{street='100 Any Street', city='Any Town', state='NC', zipCode='00000'}
```

## `CustomAttributeConverterProvider` code<a name="ddb-en-client-doc-api-convert-provider"></a>

```
public class CustomAttributeConverterProvider implements AttributeConverterProvider {

    private final Map<EnhancedType<?>, AttributeConverter<?>> converterCache = ImmutableMap.of(
            // 1. Add AddressConverter to the internal cache.
            EnhancedType.of(Address.class), new AddressConverter());

    public static CustomAttributeConverterProvider create() {
        return new CustomAttributeConverterProvider();
    }

    // 2. The enhanced client queries the provider for attribute converters if it
    //    encounters a type that it does not know how to convert.
    @SuppressWarnings("unchecked")
    @Override
    public <T> AttributeConverter<T> converterFor(EnhancedType<T> enhancedType) {
        return (AttributeConverter<T>) converterCache.get(enhancedType);
    }

    // 3. Custom attribute converter
    private class AddressConverter implements AttributeConverter<Address> {
        // 4. Transform an Address object in into a DynamoDB map.
        @Override
        public AttributeValue transformFrom(Address address) {

            Map<String, AttributeValue> attributeValueMap = Map.of(
                    "street", AttributeValue.fromS(address.getStreet()),
                    "city", AttributeValue.fromS(address.getCity()),
                    "state", AttributeValue.fromS(address.getState()),
                    "zipCode", AttributeValue.fromS(address.getZipCode()));

            return AttributeValue.fromM(attributeValueMap);
        }

        // 5. Transform the DynamoDB map attribute to an Address oject.
        @Override
        public Address transformTo(AttributeValue attributeValue) {
            Map<String, AttributeValue> m = attributeValue.m();
            Address address = new Address();
            address.setStreet(m.get("street").s());
            address.setCity(m.get("city").s());
            address.setState(m.get("state").s());
            address.setZipCode(m.get("zipCode").s());

            return address;
        }

        @Override
        public EnhancedType<Address> type() {
            return EnhancedType.of(Address.class);
        }

        @Override
        public AttributeValueType attributeValueType() {
            return AttributeValueType.M;
        }
    }
}
```

## `Address` class<a name="ddb-en-client-doc-api-convert-address"></a>

```
public class Address {
                  private String street;
                  private String city;
                  private String state;
                  private String zipCode;

                  public Address() {
                  }

                  public String getStreet() {
                  return this.street;
                  }

                  public String getCity() {
                  return this.city;
                  }

                  public String getState() {
                  return this.state;
                  }

                  public String getZipCode() {
                  return this.zipCode;
                  }

                  public void setStreet(String street) {
                  this.street = street;
                  }

                  public void setCity(String city) {
                  this.city = city;
                  }

                  public void setState(String state) {
                  this.state = state;
                  }

                  public void setZipCode(String zipCode) {
                  this.zipCode = zipCode;
                  }
                  }
```

## Helper method that provides addresses<a name="ddb-en-client-doc-api-convert-helper"></a>

The following helper method provides the map that use custom `Address` instances for values rather than generic `Map<String, String>` instances for values\.

```
    private static Map<String, Address> getAddressesForCustomMappingExample() {
        Address homeAddress = new Address();
        homeAddress.setStreet("100 Any Street");
        homeAddress.setCity("Any Town");
        homeAddress.setState("NC");
        homeAddress.setZipCode("00000");

        Address workAddress = new Address();
        workAddress.setStreet("123 Main Street");
        workAddress.setCity("Any Town");
        workAddress.setState("NC");
        workAddress.setZipCode("00000");

        return Map.of("home", homeAddress,
                "work", workAddress);
    }
```