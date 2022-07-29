--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Mapping items in DynamoDB tables<a name="examples-dynamodb-enhanced"></a>

The Amazon DynamoDB enhanced client is a high\-level library that is part of the AWS SDK for Java version 2 \(v2\)\. It offers a straightforward way to map client\-side classes to DynamoDB tables\. You define the relationships between tables and their corresponding model classes in your code\. Then you can intuitively perform various create, read, update, or delete \(CRUD\) operations on tables or items in DynamoDB\.

The AWS SDK for Java v2 includes a set of annotations that you can use with a Java bean to quickly generate a [TableSchema](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/TableSchema.html) for mapping your classes to tables\. Alternatively, if you declare each [TableSchema](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/TableSchema.html) explicitly, you don’t need to include annotations in your classes\.

To work with items in a DynamoDB table using the enhanced client, first create a [DynamoDbEnhancedClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/DynamoDbEnhancedClient.html) from an existing [DynamoDbClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/dynamodb/DynamoDbClient.html) object\.

```
Region region = Region.US_EAST_1;
DynamoDbClient ddb = DynamoDbClient.builder()
        .region(region)
        .build();

DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
        .dynamoDbClient(ddb)
        .build();

createDynamoDBTable(enhancedClient);
```

## Create a table using the enhanced client<a name="dynamodb-enhanced-mapper-beantable"></a>

To easily create a [TableSchema](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/TableSchema.html) using the enhanced client, start by creating a Java data class that includes a default public constructor and standardized names of getters and setters for each property in the class\. Include a class\-level annotation to indicate it is a `DynamoDbBean` and, at a minimum, include a `DynamoDbPartitionKey` annotation on the getter or setter for the primary key of the table record\.

Once this data class has been defined, call [TableSchema](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/TableSchema.html)'s `fromBean()` with that data class to create the table schema\.

See the code snippet below for an example of how to do this\.

 **Imports** 

```
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
```

 **Code** 

```
    public static void putRecord(DynamoDbEnhancedClient enhancedClient) {

        try {
            DynamoDbTable<Customer> custTable = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));

            // Create an Instant
            LocalDate localDate = LocalDate.parse("2020-04-07");
            LocalDateTime localDateTime = localDate.atStartOfDay();
            Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

            // Populate the Table
            Customer custRecord = new Customer();
            custRecord.setCustName("Susan red");
            custRecord.setId("id146");
            custRecord.setEmail("sred@noserver.com");
            custRecord.setRegistrationDate(instant) ;

            // Put the customer data into a DynamoDB table
            custTable.putItem(custRecord);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("done");
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/EnhancedPutItem.java) on GitHub\.

## Retrieve \(get\) an item from a table<a name="dynamodb-enhanced-mapper-getitem"></a>

To get an item from a DynamoDB table, create a [DynamoDbTable](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html) object and call `getItem()` with a [GetItemEnhancedRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/model/GetItemEnhancedRequest.html) object to get the actual item\.

For example, the following code snippet demonstrates one way to use the enhanced client to get information from an item in a DynamoDB table\.

 **Imports** 

```
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import java.time.Instant;
```

 **Code** 

```
    public static String getItem(DynamoDbEnhancedClient enhancedClient) {
        try {
            //Create a DynamoDbTable object
            DynamoDbTable<Customer> mappedTable = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));

            //Create a KEY object
            Key key = Key.builder()
                    .partitionValue("id146")
                    .build();

            // Get the item by using the key
            Customer result = mappedTable.getItem(r->r.key(key));
            return "The email value is "+result.getEmail();

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return "";
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/EnhancedGetItem.java) on GitHub\.

## Batch create \(put\) and delete items<a name="dynamodb-enhanced-mapper-batchitems"></a>

You can batch a series of put requests \([PutItemEnhancedRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/model/PutItemEnhancedRequest.html)\) and delete requests \([DeleteItemEnhancedRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/model/DeleteItemEnhancedRequest.html)\) to one or more tables, and then send all of the changes in a single request\.

In the following code snippet, a [DynamoDbTable](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html) object is created, two items are queued up to be added to the table, and then the items are written to the table in a single call\. Include multiple entries of `addDeleteItem()` and `addPutItem()` \([part of WriteBatch\.Builder](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/model/WriteBatch.Builder.html)\) in each batch, as needed\. To queue up changes to a different table, add another instance of `WriteBatch.builder()` and provide a corresponding [DynamoDbTable](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html) object in `mappedTableResource()`\.

 **Imports** 

```
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
```

 **Code** 

```
    public static void putBatchRecords(DynamoDbEnhancedClient enhancedClient) {
        try {

           DynamoDbTable<Customer> mappedTable = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));

            LocalDate localDate = LocalDate.parse("2020-04-07");
            LocalDateTime localDateTime = localDate.atStartOfDay();
            Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

            Customer record2 = new Customer();
            record2.setCustName("Fred Pink");
            record2.setId("id110");
            record2.setEmail("fredp@noserver.com");
            record2.setRegistrationDate(instant) ;

            Customer record3 = new Customer();
            record3.setCustName("Susan Pink");
            record3.setId("id120");
            record3.setEmail("spink@noserver.com");
            record3.setRegistrationDate(instant) ;

            // Create a BatchWriteItemEnhancedRequest object
            BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest =
                    BatchWriteItemEnhancedRequest.builder()
                            .writeBatches(
                                    WriteBatch.builder(Customer.class)
                                            .mappedTableResource(mappedTable)
                                            .addPutItem(r -> r.item(record2))
                                            .addPutItem(r -> r.item(record3))
                                            .build())
                            .build();

            // Add these two items to the table
            enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);
            System.out.println("done");

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/EnhancedBatchWriteItems.java) on GitHub\.

## Use a filtered query to get items from a table<a name="dynamodb-enhanced-mapper-queryfilter"></a>

You can get items from a table based on filterable queries, and then perform operations \(for example, return item values\) on one or more of the items in the query results\.

In the following code snippet, you build a filter by first defining the value or values you’re searching for as an [AttributeValue](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/dynamodb/model/AttributeValue.html) object\. Then you put this into a `HashMap` and build an [Expression](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/Expression.html) from the `HashMap`\. Build a [QueryConditional](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/model/QueryConditional.html) object to specify the primary key to match against in the query, and then execute the query on your [DynamoDbTable](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html) object\.

**Note**  
The [QueryConditional](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/model/QueryConditional.html) interface has several methods you can use to build your queries, including common conditional statements like greater than, less than, and in between\.

 **Imports** 

```
import java.time.Instant;
import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
```

 **Code** 

```
    public static void queryTableFilter(DynamoDbEnhancedClient enhancedClient) {

        try{
            DynamoDbTable<Customer> mappedTable = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));

            AttributeValue att = AttributeValue.builder()
                    .s("sblue@noserver.com")
                    .build();

            Map<String, AttributeValue> expressionValues = new HashMap<>();
            expressionValues.put(":value", att);

            Expression expression = Expression.builder()
                    .expression("email = :value")
                    .expressionValues(expressionValues)
                    .build();

            // Create a QueryConditional object that is used in the query operation.
            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(Key.builder().partitionValue("id103")
                            .build());

            // Get items in the Customer table and write out the ID value.
            Iterator<Customer> results = mappedTable.query(r -> r.queryConditional(queryConditional).filterExpression(expression)).items().iterator();

            while (results.hasNext()) {

                Customer rec = results.next();
                System.out.println("The record id is "+rec.getId());
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Done");
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/EnhancedQueryRecordsWithFilter.java) on GitHub\.

## Retrieve \(get\) all items from a table<a name="dynamodb-enhanced-mapper-scan"></a>

When you want to get all of the records in a given DynamoDB table, use the `scan()` method of your [DynamoDbTable](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html) object and the `items()` method to create a set of results against which you can execute various item operations\. For example, the following code snippet prints out the ID value of each item in the **Record** table\.

 **Imports** 

```
import java.time.Instant;
import java.util.Iterator;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
```

 **Code** 

```
    public static void scan( DynamoDbEnhancedClient enhancedClient) {

        try{
            // Create a DynamoDbTable object
            DynamoDbTable<Customer> custTable = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));
            Iterator<Customer> results = custTable.scan().items().iterator();
            while (results.hasNext()) {

                Customer rec = results.next();
                System.out.println("The record id is "+rec.getId());
                System.out.println("The name is " +rec.getCustName());
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Done");
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/EnhancedScanRecords.java) on GitHub\.

For more information, see [Working with items in DynamoDB](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/WorkingWithItems.html) in the Amazon DynamoDB Developer Guide\.