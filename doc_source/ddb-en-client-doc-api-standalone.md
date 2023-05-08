# Use an `EnhancedDocument` without DynamoDB<a name="ddb-en-client-doc-api-standalone"></a>

Although you usually use an instance of an `EnhancedDocument` to read and write document\-type DynamoDB items, it can also be used independently of DynamoDB\. 

You can use `EnhancedDocuments` for their ability to convert between JSON strings or custom objects to low\-level maps of `AttributeValues` as shown in the following example\.

```
    public static void conversionWithoutDynamoDbExample() {
        Address address = new Address();
        address.setCity("my city");
        address.setState("my state");
        address.setStreet("my street");
        address.setZipCode("00000");

        // Build an EnhancedDocument instance for its conversion functionality alone.
        EnhancedDocument addressEnhancedDoc = EnhancedDocument.builder()
                // Important: You must specify attribute converter providers when you build an EnhancedDocument instance not used with a DynamoDB table.
                .attributeConverterProviders(new CustomAttributeConverterProvider(), DefaultAttributeConverterProvider.create())
                .put("addressDoc", address, Address.class)
                .build();

        // Convert address to a low-level item representation.
        final Map<String, AttributeValue> addressAsAttributeMap = addressEnhancedDoc.getMapOfUnknownType("addressDoc");
        logger.info("addressAsAttributeMap: {}", addressAsAttributeMap.toString());

        // Convert address to a JSON string.
        String addressAsJsonString = addressEnhancedDoc.getJson("addressDoc");
        logger.info("addressAsJsonString: {}", addressAsJsonString);
        // Convert addressEnhancedDoc back to an Address instance.
        Address addressConverted =  addressEnhancedDoc.get("addressDoc", Address.class);
        logger.info("addressConverted: {}", addressConverted.toString());
    }

   /* Console output:
          addressAsAttributeMap: {zipCode=AttributeValue(S=00000), state=AttributeValue(S=my state), street=AttributeValue(S=my street), city=AttributeValue(S=my city)}
          addressAsJsonString: {"zipCode":"00000","state":"my state","street":"my street","city":"my city"}
          addressConverted: Address{street='my street', city='my city', state='my state', zipCode='00000'}
   */
```

**Note**  
When you use an enhanced document independent of a DynamoDB table, make sure you explicitly set attribute converter providers on the builder\.  
In contrast, the document table schema supplies the converter providers when an enhanced document is used with a DynamoDB table\.