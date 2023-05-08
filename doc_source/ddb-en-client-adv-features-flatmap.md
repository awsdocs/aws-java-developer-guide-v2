# Flatten attributes from other classes<a name="ddb-en-client-adv-features-flatmap"></a>

If the attributes for your table are spread across several different Java classes, either through inheritance or composition, the DynamoDB Enhanced Client API provides support to flatten the attributes into one class\.

## Use inheritance<a name="ddb-en-client-adv-features-flatmap-inheritance"></a>

If your classes use inheritance, use the following approaches to flatten the hierarchy\.

### Use annotated beans<a name="ddb-en-client-adv-features-flatmap-inheritance-anno"></a>

For the annotation approach, both classes must carry the `@DynamoDbBean` annotation and a class must carry one or more primary key annotations\.

The following shows examples of data classes that have an inheritance relationship\.

------
#### [ Standard data class ]

```
@DynamoDbBean
public class Customer extends GenericRecord {
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

@DynamoDbBean
public abstract class GenericRecord {
    private String id;
    private String createdDate;

    @DynamoDbPartitionKey
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}
```

------
#### [ Lombok ]

Lombok's [`onMethod` option](https://projectlombok.org/features/experimental/onX) copies attribute\-based DynamoDB annotations, such as `@DynamoDbPartitionKey`, onto the generated code\.

```
@DynamoDbBean
@Data
@ToString(callSuper = true)
public class Customer extends GenericRecord {
    private String name;
}

@Data
@DynamoDbBean
public abstract class GenericRecord {
    @Getter(onMethod_=@DynamoDbPartitionKey)
    private String id;
    private String createdDate;
}
```

------

### Use static schemas<a name="ddb-en-client-adv-features-flatmap-inheritance-static"></a>

For the static schema approach, use the `extend()` method of the builder to collapse the attributes of the parent class onto the child class\. This is shown after comment line 1 in the following example\.

```
        StaticTableSchema<org.example.tests.model.inheritance.stat.GenericRecord> GENERIC_RECORD_SCHEMA =
                StaticTableSchema.builder(org.example.tests.model.inheritance.stat.GenericRecord.class)
                        // The partition key will be inherited by the top level mapper.
                        .addAttribute(String.class, a -> a.name("id")
                                .getter(org.example.tests.model.inheritance.stat.GenericRecord::getId)
                                .setter(org.example.tests.model.inheritance.stat.GenericRecord::setId)
                                .tags(primaryPartitionKey()))
                        .addAttribute(String.class, a -> a.name("created_date")
                                .getter(org.example.tests.model.inheritance.stat.GenericRecord::getCreatedDate)
                                .setter(org.example.tests.model.inheritance.stat.GenericRecord::setCreatedDate))
                        .build();

        StaticTableSchema<org.example.tests.model.inheritance.stat.Customer> CUSTOMER_SCHEMA =
                StaticTableSchema.builder(org.example.tests.model.inheritance.stat.Customer.class)
                        .newItemSupplier(org.example.tests.model.inheritance.stat.Customer::new)
                        .addAttribute(String.class, a -> a.name("name")
                                .getter(org.example.tests.model.inheritance.stat.Customer::getName)
                                .setter(org.example.tests.model.inheritance.stat.Customer::setName))
                        // 1. Use the extend() method to collapse the parent attributes onto the child class.
                        .extend(GENERIC_RECORD_SCHEMA)     // All the attributes of the GenericRecord schema are added to Customer.
                        .build();
```

The previous static schema example uses the following data classes\. Because the mapping is defined when you build the static table schema, the data classes don't require annotations\.

#### Data classes<a name="gunk"></a>

------
#### [ Standard data class ]

```
public class Customer extends GenericRecord {
    private String name;
    private Address address;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}


public abstract class GenericRecord {
    private String id;
    private String createdDate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
```

------
#### [ Lombok ]

```
@Data
@ToString(callSuper = true)
public class Customer extends GenericRecord{
    private String name;
}

@Data
public abstract class GenericRecord {
    private String id;
    private String createdDate;
}
```

------

## Use composition<a name="ddb-en-client-adv-features-flatmap-comp"></a>

If your classes use composition, use the following approaches to flatten the hierarchy\.

### Use annotated beans<a name="ddb-en-client-adv-features-flatmap-comp-anno"></a>

The `@DynamoDbFlatten` annotation flattens the contained class\.

The following data class examples use the `@DynamoDbFlatten` annotation to effectively add all attributes of the contained `GenericRecord` class to the `Customer` class\.

------
#### [ Standard data class ]

```
@DynamoDbBean
public class Customer {
    private String name;
    private GenericRecord record;

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    @DynamoDbFlatten
    public GenericRecord getRecord() { return this.record; }
    public void setRecord(GenericRecord record) { this.record = record; }

@DynamoDbBean
public class GenericRecord {
    private String id;
    private String createdDate;

    @DynamoDbPartitionKey
    public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }

    public String getCreatedDate() { return this.createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}
```

------
#### [ Lombok ]

```
@Data
@DynamoDbBean
public class Customer {
    private String name;
    @Getter(onMethod_=@DynamoDbFlatten)
    private GenericRecord record;
}

@Data
@DynamoDbBean
public class GenericRecord {
    @Getter(onMethod_=@DynamoDbPartitionKey)
    private String id;
    private String createdDate;
}
```

------

You can use the flatten annotation to flatten as many different eligible classes as you need to\. The following constraints apply:
+ All attribute names must be unique after they are flattened\.
+ There must never be more than one partition key, sort key, or table name\.

### Use static schemas<a name="ddb-en-client-adv-features-flatmap-comp-static"></a>

When you build a static table schema, use the `flatten()` method of the builder\. You also supply the getter and setter methods that identify the contained class\.

```
        StaticTableSchema<GenericRecord> GENERIC_RECORD_SCHEMA =
                StaticTableSchema.builder(GenericRecord.class)
                        .newItemSupplier(GenericRecord::new)
                        .addAttribute(String.class, a -> a.name("id")
                                .getter(GenericRecord::getId)
                                .setter(GenericRecord::setId)
                                .tags(primaryPartitionKey()))
                        .addAttribute(String.class, a -> a.name("created_date")
                                .getter(GenericRecord::getCreatedDate)
                                .setter(GenericRecord::setCreatedDate))
                        .build();

        StaticTableSchema<Customer> CUSTOMER_SCHEMA =
                StaticTableSchema.builder(Customer.class)
                        .newItemSupplier(Customer::new)
                        .addAttribute(String.class, a -> a.name("name")
                                .getter(Customer::getName)
                                .setter(Customer::setName))
                        // Because we are flattening a component object, we supply a getter and setter so the
                        // mapper knows how to access it.
                        .flatten(GENERIC_RECORD_SCHEMA, Customer::getRecord, Customer::setRecord)
                        .build();
```

The previous static schema example uses the following data classes\.

#### Data classes<a name="ddb-en-client-adv-features-flatmap-comp-static-supporting"></a>

------
#### [ Standard data class ]

```
public class Customer {
    private String name;
    private GenericRecord record;

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public GenericRecord getRecord() { return this.record; }
    public void setRecord(GenericRecord record) { this.record = record; }

public class GenericRecord {
    private String id;
    private String createdDate;

    public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }

    public String getCreatedDate() { return this.createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}
```

------
#### [ Lombok ]

```
@Data
public class Customer {
    private String name;
    private GenericRecord record;
}

@Data
public class GenericRecord {
    private String id;
    private String createdDate;
}
```

------

You can use the builder pattern to flatten as many different eligible classes as you need to\.

## Implications for other code<a name="ddb-en-client-adv-features-flatmap-compare"></a>

When you use the `@DynamoDbFlatten` attribute \(or `flatten()` builder method\), the item in DynamoDB contains an attribute for each attribute of the composed object\. It also includes the attributes of the composing object\. 

In contrast, if you annotate a data class with a composed class and don't use `@DynamoDbFlatten`, the item is saved with the composed object as a single attribute\.

For example, if you compare the `Customer` class shown in the [flattening with composition example](#ddb-en-client-adv-features-flatmap-comp-anno) with and without flattening of the `record` attribute, you can visualize the difference with JSON\. This is shown in the following table\.


****  

| With flattening | Without flattening | 
| --- | --- | 
| 3 attributes | 2 attributes | 
|  <pre>{<br />  "id": "1",<br />  "createdDate": "today",<br />  "name": "my name"<br />}</pre>  |  <pre>{<br />  "id": "1",<br />  "record": {<br />      "createdDate": "today",<br />      "name": "my name"<br />  }<br />}</pre>  | 

The difference becomes important if you have other code accessing the DynamoDB table that expects to find certain attributes\.