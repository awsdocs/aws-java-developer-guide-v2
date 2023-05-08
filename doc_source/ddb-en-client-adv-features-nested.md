# Work with nested attributes<a name="ddb-en-client-adv-features-nested"></a>

A nested attribute in DynamoDB is embedded in another attribute\. Examples are list elements and map entries\. 

In Java, a DynamoDB nested attribute corresponds to a member of a class that is a `List` or `Map`\. It also corresponds to an instance of a complex type, such as `Address` or `PhoneNumber`, as used in the following `Person` class\.

## `Person` class<a name="ddb-en-client-adv-features-nested-person"></a>

```
@DynamoDbBean
public class Person {
    Integer id;
    String firstName;
    String lastName;
    Integer age;
    Map<String, Address> addresses;
    List<PhoneNumber> phoneNumbers;

    List<String> hobbies;

    @DynamoDbPartitionKey
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Map<String, Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Map<String, Address> addresses) {
        this.addresses = addresses;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", addresses=" + addresses +
                ", phoneNumbers=" + phoneNumbers +
                ", hobbies=" + hobbies +
                '}';
    }
}
```

## `Address` class<a name="ddb-en-client-adv-features-nested-address"></a>

```
@DynamoDbBean
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) && Objects.equals(city, address.city) && Objects.equals(state, address.state) && Objects.equals(zipCode, address.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, zipCode);
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
```

## `PhoneNumber` class<a name="ddb-en-client-adv-features-nested-phonenumber"></a>

```
@DynamoDbBean
public class PhoneNumber {
    String type;
    String number;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "PhoneNumber{" +
                "type='" + type + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
```

## Map nested attributes<a name="ddb-en-client-adv-features-nested-mapping"></a>

### Use annotated classes<a name="ddb-en-client-adv-features-nested-map-anno"></a>

You can save nested attributes for custom classes by annotating them\. The `Address` class and `PhoneNumber` class shown previously are annotated with only the `@DynamoDbBean` annotation\. When the DynamoDB Enhanced Client API builds the table schema for the `Person` class with the following snippet, the API discovers the use of the `Address` and `PhoneNumber` classes and builds the corresponding mappings to work with DynamoDB\.

```
TableSchema<Person> personTableSchema = TableSchema.fromBean(Person.class);
```

### Use nested schemas<a name="ddb-en-client-adv-features-nested-map-builder"></a>

The alternative approach is to use static table schema builders for each of the classes as shown in the following code\.

The table schemas for the `Address` and `PhoneNumber` classes are abstract in the sense that they cannot be used with a DynamoDB table\. This is because they lack definitions for the primary key\. They are used, however, as nested schemas in the table schema for the `Person` class\.

After comment lines 1 and 2 in the definition of `PERSON_TABLE_SCHEMA`, you see the code that uses the abstract table schemas\. The use of `documentOf` in the `EnhanceType.documentOf(...)` method does not indicate that the method returns an `EnhancedDocument` type of the Enhanced Document API\. The `documentOf(...)` method in this context returns an object that knows how to map its class argument to and from DynamoDB table attributes by using the table schema argument\.

#### Static schema code<a name="ddb-en-client-adv-features-nested-map-builder-code"></a>

```
    public static final TableSchema<Address> TABLE_SCHEMA_ADDRESS = TableSchema.builder(Address.class)
            .newItemSupplier(Address::new)
            .addAttribute(String.class, a -> a.name("street")
                    .getter(Address::getStreet)
                    .setter(Address::setStreet))
            .addAttribute(String.class, a -> a.name("city")
                    .getter(Address::getCity)
                    .setter(Address::setCity))
            .addAttribute(String.class, a -> a.name("zipcode")
                    .getter(Address::getZipCode)
                    .setter(Address::setZipCode))
            .addAttribute(String.class, a -> a.name("state")
                    .getter(Address::getState)
                    .setter(Address::setState))
            .build();

    // Abstract table schema that cannot be used to work with a DynamoDB table,
    // but can be used as a nested schema.
    public static final TableSchema<PhoneNumber> TABLE_SCHEMA_PHONENUMBER = TableSchema.builder(PhoneNumber.class)
            .newItemSupplier(PhoneNumber::new)
            .addAttribute(String.class, a -> a.name("type")
                    .getter(PhoneNumber::getType)
                    .setter(PhoneNumber::setType))
            .addAttribute(String.class, a -> a.name("number")
                    .getter(PhoneNumber::getNumber)
                    .setter(PhoneNumber::setNumber))
            .build();

    // A static table schema that can be used with a DynamoDB table.
    // The table schema contains two nested schemas that are used to perform mapping to/from DynamoDB.
    public static final TableSchema<Person> PERSON_TABLE_SCHEMA =
            TableSchema.builder(Person.class)
                    .newItemSupplier(Person::new)
                    .addAttribute(Integer.class, a -> a.name("id")
                            .getter(Person::getId)
                            .setter(Person::setId)
                            .addTag(StaticAttributeTags.primaryPartitionKey()))
                    .addAttribute(String.class, a -> a.name("firstName")
                            .getter(Person::getFirstName)
                            .setter(Person::setFirstName))
                    .addAttribute(String.class, a -> a.name("lastName")
                            .getter(Person::getLastName)
                            .setter(Person::setLastName))
                    .addAttribute(Integer.class, a -> a.name("age")
                            .getter(Person::getAge)
                            .setter(Person::setAge))
                    .addAttribute(EnhancedType.listOf(String.class), a -> a.name("hobbies")
                            .getter(Person::getHobbies)
                            .setter(Person::setHobbies))
                    .addAttribute(EnhancedType.mapOf(
                                    EnhancedType.of(String.class),
                                    // 1. Use mapping functionality of the Address table schema.
                                    EnhancedType.documentOf(Address.class, TABLE_SCHEMA_ADDRESS)), a -> a.name("addresses")
                            .getter(Person::getAddresses)
                            .setter(Person::setAddresses))
                    .addAttribute(EnhancedType.listOf(
                                    // 2. Use mapping functionality of the PhoneNumber table schema.
                                    EnhancedType.documentOf(PhoneNumber.class, TABLE_SCHEMA_PHONENUMBER)), a -> a.name("phoneNumbers")
                            .getter(Person::getPhoneNumbers)
                            .setter(Person::setPhoneNumbers))
                    .build();
```

## Project nested attributes<a name="ddb-en-client-adv-features-nested-projection"></a>

For `query()` and `scan()` methods, you can specify which attributes you want to be returned in the results by using method calls such as `addNestedAttributeToProject()` and `attributesToProject()`\. The DynamoDB Enhanced Client API converts the Java method call parameters into[projection expressions](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.ProjectionExpressions.html) before the request is sent\.

The following example populates the `Person` table with two items, then performs three scan operations\. 

The first scan accesses all items in the table in order to compare the results to the other scan operations\. 

The second scan uses the [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/ScanEnhancedRequest.Builder.html#addNestedAttributeToProject(software.amazon.awssdk.enhanced.dynamodb.NestedAttributeName)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/ScanEnhancedRequest.Builder.html#addNestedAttributeToProject(software.amazon.awssdk.enhanced.dynamodb.NestedAttributeName)) builder method to return only the `street` attribute value\.

The third scan operation uses the [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/ScanEnhancedRequest.Builder.html#attributesToProject(java.lang.String...)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/ScanEnhancedRequest.Builder.html#attributesToProject(java.lang.String...)) builder method to return the data for the first\-level attribute, `hobbies`\. The attribute type of `hobbies` is a list\. To access individual list items, perform a `get()` operation on the list\.

```
        personDynamoDbTable = getDynamoDbEnhancedClient().table("Person", PERSON_TABLE_SCHEMA);
        PersonUtils.createPersonTable(personDynamoDbTable, getDynamoDbClient());
        // Use a utility class to add items to the Person table.
        List<Person> personList = PersonUtils.getItemsForCount(2);
        // This utility method performs a put against DynamoDB to save the instances in the list argument.
        PersonUtils.putCollection(getDynamoDbEnhancedClient(), personList, personDynamoDbTable);

        // The first scan logs all items in the table to compare to the results of the subsequent scans.
        final PageIterable<Person> allItems = personDynamoDbTable.scan();
        allItems.items().forEach(p ->
                // 1. Log what is in the table.
                logger.info(p.toString()));

        // Scan for nested attributes.
        PageIterable<Person> streetScanResult = personDynamoDbTable.scan(b -> b
                // Use the 'addNestedAttributeToProject()' or 'addNestedAttributesToProject()' to access data nested in maps in DynamoDB.
                .addNestedAttributeToProject(
                        NestedAttributeName.create("addresses", "work", "street")
                ));

        streetScanResult.items().forEach(p ->
                //2. Log the results of requesting nested attributes.
                logger.info(p.toString()));

        // Scan for a top-level list attribute.
        PageIterable<Person> phoneNumbersScanResult = personDynamoDbTable.scan(b -> b
                // Use the 'attributesToProject()' method to access first-level attributes.
                .attributesToProject("hobbies"));

        phoneNumbersScanResult.items().forEach((p) -> {
            // 3. Log the results of the request for the 'hobbies' attribute.
            logger.info(p.toString());
            // To access an item in a list, first get the parent attribute, 'hobbies', then access items in the list.
            String hobby = p.getHobbies().get(1);
            // 4. Log an item in the list.
            logger.info(hobby);
        });
```

```
// Logged results from comment line 1.
Person{id=2, firstName='first name 2', lastName='last name 2', age=11, addresses={work=Address{street='street 21', city='city 21', state='state 21', zipCode='33333'}, home=Address{street='street 2', city='city 2', state='state 2', zipCode='22222'}}, phoneNumbers=[PhoneNumber{type='home', number='222-222-2222'}, PhoneNumber{type='work', number='333-333-3333'}], hobbies=[hobby 2, hobby 21]}
Person{id=1, firstName='first name 1', lastName='last name 1', age=11, addresses={work=Address{street='street 11', city='city 11', state='state 11', zipCode='22222'}, home=Address{street='street 1', city='city 1', state='state 1', zipCode='11111'}}, phoneNumbers=[PhoneNumber{type='home', number='111-111-1111'}, PhoneNumber{type='work', number='222-222-2222'}], hobbies=[hobby 1, hobby 11]}

// Logged results from comment line 2.
Person{id=null, firstName='null', lastName='null', age=null, addresses={work=Address{street='street 21', city='null', state='null', zipCode='null'}}, phoneNumbers=null, hobbies=null}
Person{id=null, firstName='null', lastName='null', age=null, addresses={work=Address{street='street 11', city='null', state='null', zipCode='null'}}, phoneNumbers=null, hobbies=null}

// Logged results from comment lines 3 and 4.
Person{id=null, firstName='null', lastName='null', age=null, addresses=null, phoneNumbers=null, hobbies=[hobby 2, hobby 21]}
hobby 21
Person{id=null, firstName='null', lastName='null', age=null, addresses=null, phoneNumbers=null, hobbies=[hobby 1, hobby 11]}
hobby 11
```

**Note**  
If the `attributesToProject()` method follows any other builder method that adds attributes that you want to project, the list of attribute names supplied to the `attributesToProject()` replaces all other attribute names\.  
A scan performed with the `ScanEnhancedRequest` instance in the following snippet returns only hobby data\.  

```
ScanEnhancedRequest lastOverwrites = ScanEnhancedRequest.builder()
        .addNestedAttributeToProject(
                NestedAttributeName.create("addresses", "work", "street"))
        .addAttributeToProject("firstName")
        // If the 'attributesToProject()' method follows other builder methods that add attributes for projection,
        // its list of attributes replace all previous attributes.
        .attributesToProject("hobbies")
        .build();
PageIterable<Person> hobbiesOnlyResult = personDynamoDbTable.scan(lastOverwrites);
hobbiesOnlyResult.items().forEach(p ->
        logger.info(p.toString()));

// Logged results.
Person{id=null, firstName='null', lastName='null', age=null, addresses=null, phoneNumbers=null, hobbies=[hobby 2, hobby 21]}
Person{id=null, firstName='null', lastName='null', age=null, addresses=null, phoneNumbers=null, hobbies=[hobby 1, hobby 11]}
```
The following code snippet uses the `attributesToProject()` method first\. This ordering preserves all other requested attributes\.  

```
ScanEnhancedRequest attributesPreserved = ScanEnhancedRequest.builder()
        // Use 'attributesToProject()' first so that the method call does not replace all other attributes
        // that you want to project.
        .attributesToProject("firstName")
        .addNestedAttributeToProject(
                NestedAttributeName.create("addresses", "work", "street"))
        .addAttributeToProject("hobbies")
        .build();
PageIterable<Person> allAttributesResult = personDynamoDbTable.scan(attributesPreserved);
allAttributesResult.items().forEach(p ->
        logger.info(p.toString()));

// Logged results.
Person{id=null, firstName='first name 2', lastName='null', age=null, addresses={work=Address{street='street 21', city='null', state='null', zipCode='null'}}, phoneNumbers=null, hobbies=[hobby 2, hobby 21]}
Person{id=null, firstName='first name 1', lastName='null', age=null, addresses={work=Address{street='street 11', city='null', state='null', zipCode='null'}}, phoneNumbers=null, hobbies=[hobby 1, hobby 11]}
```