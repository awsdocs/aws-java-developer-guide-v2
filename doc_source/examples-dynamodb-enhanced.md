# Mapping items in DynamoDB tables<a name="examples-dynamodb-enhanced"></a>

The DynamoDB enhanced client is a high\-level library that is part of the AWS SDK for Java 2\.x\. It offers a straightforward way to map client\-side classes to DynamoDB tables\. You define the relationships between tables and their corresponding model classes in your code\. After you define those relationships, you can intuitively perform various create, read, update, or delete \(CRUD\) operations on tables or items in DynamoDB\.

To begin working with the enhanced client in your project, add a dependency on the Maven artifact `dynamodb-enhanced` in addition to the `dynamodb` artifact to your build file, as shown in one of the following examples\. 

------
#### [ Maven ]

```
<project>
  <dependencyManagement>
   <dependencies>
      <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>bom</artifactId>
        <version>2.18.10</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
   </dependencies>
  </dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>dynamodb</artifactId>
    </dependency>
    <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>dynamodb-enhanced</artifactId>
    </dependency>
  </dependencies>
  ...
</project>
```

Perform a search of the Maven central repository for the [latest version](https://search.maven.org/search?q=g:software.amazon.awssdk%20AND%20a:bom)\.

------
#### [ Gradle ]

```
repositories {
    mavenCentral()
}
dependencies {
    implementation(platform("software.amazon.awssdk:bom:2.18.10"))
    implementation("software.amazon.awssdk:dynamodb")
    implementation("software.amazon.awssdk:dynamodb-enhanced")
    ...
}
```

Perform a search of the Maven central repository for the [latest version](https://search.maven.org/search?q=g:software.amazon.awssdk%20AND%20a:bom)\.

------

## Create a `DynamoDbEnhancedClient`<a name="dynamodb-enhanced-client-create"></a>

The `[DynamoDbEnhancedClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/DynamoDbEnhancedClient.html)` instance is used to work with DynamoDB tables and mapped classes\. You create a `DynamoDbEnhancedClient` from an existing `[DynamoDbClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/dynamodb/DynamoDbClient.html)` object, as shown in the following example\.

```
DynamoDbClient ddb = DynamoDbClient.builder()
        .region(Region.US_EAST_1)
        .build();

DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
        .dynamoDbClient(ddb)
        .build();

// Use the enhancedClient.
```

## Generate a `TableSchema`<a name="dynamodb-enhanced-mapper-tableschema"></a>

When you work with the mapping features of the enhanced client, the first step you take is to generate a `[TableSchema](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/TableSchema.html)`\. You can do this in a couple of ways\.

### Use an annotated Java bean<a name="dynamodb-enhanced-mapper-tableschema-bean"></a>

The SDK for Java 2\.x includes a [set of annotations](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/mapper/annotations/package-summary.html) that you can use with a Java bean to quickly generate a `TableSchema` for mapping your classes to tables\.

Start by creating a Java data class that includes a default public constructor and standardized names of getters and setters for each property in the class\. Include a class\-level annotation to indicate it is a `DynamoDbBean` and, at a minimum, include a `DynamoDbPartitionKey` annotation on the getter or setter for the primary key of the table record\. 

The following `Customer` class shows these annotations that will link the class definition to the DynamoDB table\.

```
/*
   Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
   SPDX-License-Identifier: Apache-2.0
*/

package com.example.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;

/**
 * This class is used by the Enhanced Client examples.
 */

 @DynamoDbBean
 public class Customer {

        private String id;
        private String name;
        private String email;
        private Instant regDate;

        @DynamoDbPartitionKey
        public String getId() {
            return this.id;
        }

        public void setId(String id) {

            this.id = id;
        }

        public String getCustName() {
            return this.name;
        }

        public void setCustName(String name) {
            this.name = name;
        }

        @DynamoDbSortKey
        public String getEmail() {
            return this.email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Instant getRegistrationDate() {
            return regDate;
        }
        public void setRegistrationDate(Instant registrationDate) {
            this.regDate = registrationDate;
        }

        @Override
        public String toString() {
            return "Customer [id=" + id + ", name=" + name + ", email=" + email
                    + ", regDate=" + regDate + "]";
        }
    }
```

Once you have created an annotated Java bean, use it to create the `TableSchema`, as shown in the following snippet\.

```
TableSchema<Customer> customerTableSchema = TableSchema.fromBean(Customer.class);
```

### Use a builder<a name="dynamodb-enhanced-mapper-tableschema-builder"></a>

If you would prefer to skip the slightly costly bean introspection for a faster solution, you can instead declare your schema directly and let the compiler do the heavy lifting\. If you do it this way, your class does not need to follow bean naming standards nor does it need to be annotated\. This following example uses a builder and is equivalent to the bean example\.

```
TableSchema<Customer> customerTableSchema =
  TableSchema.builder(Customer.class)
    .newItemSupplier(Customer::new)
    .addAttribute(String.class, a -> a.name("id")
                                      .getter(Customer::getId)
                                      .setter(Customer::setId)
                                      .tags(primaryPartitionKey()))
    .addAttribute(Integer.class, a -> a.name("email")
                                       .getter(Customer::getEmail)
                                       .setter(Customer::setEmail)
                                       .tags(primarySortKey()))
    .addAttribute(String.class, a -> a.name("name")
                                      .getter(Customer::getCustName)
                                      .setter(Customer::setCustName))
    .addAttribute(Instant.class, a -> a.name("registrationDate")
                                       .getter(Customer::getRegistrationDate)
                                       .setter(Customer::setRegistrationDate))
    .build();
```

## Create a table using the enhanced client<a name="dynamodb-enhanced-mapper-beantable"></a>

The following example shows you how to create a DynamoDB table from the `Customer` Java data bean class\. 

To create the [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html) object, pass the table name and the `TableSchema` to the `table()` method of the enhanced client\. This example creates a table with the name `Customer`—identical to the class name—but the table name can be something else\. Whatever you name the table, you must use this name in additional applications to work with the table\. Supply this name to the `table()` method anytime you create another `DynamoDbTable` object\.

The lambda parameter, `builder`, passed to the `createTable` method allows you to [customize the table](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/CreateTableEnhancedRequest.Builder.html)\. The example also uses a `[DynamoDbWaiter](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/waiters/DynamoDbWaiter.html)`\. Since the creation of a table takes a bit of time, using a waiter avoids the need for you to write logic that polls the DynamoDB service to see if the table exists before using the table\.

**Imports**

```
import com.example.dynamodb.Customer;
import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;
```

**Code**

```
public class EnhancedCreateTable {
    public static void createTable(DynamoDbEnhancedClient enhancedClient) {
        // Create a DynamoDbTable object
        DynamoDbTable<Customer> customerTable = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));
        // Create the table
        customerTable.createTable(builder -> builder
                .provisionedThroughput(b -> b
                        .readCapacityUnits(10L)
                        .writeCapacityUnits(10L)
                        .build())
        );

        System.out.println("Waiting for table creation...");

        try (DynamoDbWaiter waiter = DynamoDbWaiter.create()) { // DynamoDbWaiter is Autocloseable
            ResponseOrException<DescribeTableResponse> response = waiter
                    .waitUntilTableExists(builder -> builder.tableName("Customer").build())
                    .matched();
            DescribeTableResponse tableDescription = response.response().orElseThrow(
                    () -> new RuntimeException("Customer table was not created."));
            // The actual error can be inspected in response.exception()
            System.out.println(tableDescription.table().tableName() + " was created.");
        }
    }
}
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/enhanced/EnhancedCreateTable.java) on GitHub\.

## Retrieve \(get\) an item from a table<a name="dynamodb-enhanced-mapper-getitem"></a>

To get an item from a DynamoDB table, create a [DynamoDbTable](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html) object and call `getItem()` with a [GetItemEnhancedRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/model/GetItemEnhancedRequest.html) object\. As an alternative to passing in a `GetItemEnhancedRequest `object, you can take advantage of lambda expressions and the SDK's builder pattern\. In the example below, the `[getItem\(\)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html#getItem(java.util.function.Consumer))` method takes a `Consumer<GetItemEnhancedRequest.Builder>` that ultimately creates the` GetItemEnhancedRequest` object for you\.

For explanatory purposes, the example uses full lambda syntax, `(GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key)`, but a simpler expression such as `rb → rb.key(key)` works as well\.

The following code snippet demonstrates the use of the enhanced client to get information from an item in a DynamoDB table\.

 **Imports** 

```
import com.example.dynamodb.Customer;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
```

 **Code** 

```
    public static String getItem(DynamoDbEnhancedClient enhancedClient) {

        Customer result = null;

        try {
            DynamoDbTable<Customer> table = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));
            Key key = Key.builder()
                .partitionValue("id101").sortValue("tred@noserver.com")
                .build();

            // Get the item by using the key.
            result = table.getItem(
                    (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));
            System.out.println("******* The description value is " + result.getCustName());

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return result.getCustName();
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/enhanced/EnhancedGetItem.java) on GitHub\.

## Add a new item to a table<a name="enhanced-put-item"></a>

To insert a new item into a table using the enhanced client, you create an instance of the Java bean data class that is annotated with `@DynamoDbBean`\. Use the setters of the Java bean instance to add the data that you want to insert\. Use the `[putItem\(\)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html#putItem(T))` method of the `DynamoDbTable` to perform the insertion\.

 The following example shows one item added to the `Customer` table\.

**Imports** 

```
import com.example.dynamodb.Customer;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
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
    public static void putRecord(DynamoDbEnhancedClient enhancedClient) {
        try {
            DynamoDbTable<Customer> custTable = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));

            // Create an Instant value.
            LocalDate localDate = LocalDate.parse("2020-04-07");
            LocalDateTime localDateTime = localDate.atStartOfDay();
            Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

            // Populate the Table.
            Customer custRecord = new Customer();
            custRecord.setCustName("Tom red");
            custRecord.setId("id101");
            custRecord.setEmail("tred@noserver.com");
            custRecord.setRegistrationDate(instant) ;

            // Put the customer data into an Amazon DynamoDB table.
            custTable.putItem(custRecord);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Customer data added to the table with id id101");
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/enhanced/EnhancedPutItem.java) on GitHub\.

## Batch create \(put\) and delete items<a name="dynamodb-enhanced-mapper-batchitems"></a>

You can batch a series of put requests \([PutItemEnhancedRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/model/PutItemEnhancedRequest.html)\) and delete requests \([DeleteItemEnhancedRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/model/DeleteItemEnhancedRequest.html)\) to one or more tables, and then send all of the changes in a single request\.

The SDK offers the builder pattern to create the `PutItemEnhancedRequest` for you\. A lambda expression, such as `builder -> builder.item(record2)` is all you need to provide to the `[addPutItem\(\)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/WriteBatch.Builder.html#addPutItem(java.util.function.Consumer))` method as shown in the example below\.

A [DynamoDbTable](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html) object is created and three items are queued up to be added to the Customer table in the first call to `[WriteBatch\.builder](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/WriteBatch.html#builder(java.lang.Class))`\. You can call `addDeleteItem()` and `addPutItem()` \([part of WriteBatch\.Builder](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/model/WriteBatch.Builder.html)\) multiple times in each batch, as needed\. 

To queue up changes to a different table, make another call to `WriteBatch.builder()` and provide a corresponding [DynamoDbTable](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html) object in `mappedTableResource()`\. This is shown below in the second `WriteBatch.builder()` call using the Music table and deleting one item from the table\.

 **Imports** 

```
import com.example.dynamodb.Customer;
import com.example.dynamodb.Music;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
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
            DynamoDbTable<Customer> customerMappedTable = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));
            DynamoDbTable<Music> musicMappedTable = enhancedClient.table("Music", TableSchema.fromBean(Music.class));
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

            Customer record4 = new Customer();
            record4.setCustName("Jerry orange");
            record4.setId("id101");
            record4.setEmail("jorange@noserver.com");
            record4.setRegistrationDate(instant) ;


            BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                    .writeBatches(
                            WriteBatch.builder(Customer.class)          // add items to the Customer table
                                    .mappedTableResource(customerMappedTable)
                                    .addPutItem(builder -> builder.item(record2))
                                    .addPutItem(builder -> builder.item(record3))
                                    .addPutItem(builder -> builder.item(record4))
                                    .build(),
                            WriteBatch.builder(Music.class)             // delete an item from the Music table
                                    .mappedTableResource(musicMappedTable)
                                    .addDeleteItem(builder -> builder.key(
                                            Key.builder().partitionValue("Famous Band").build()))
                                    .build())
                    .build();


            // Add three items to the Customer table and delete one item from the Music table
            enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            System.out.println("done");

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/enhanced/EnhancedBatchWriteItems.java) on GitHub\.

## Use a filtered query to get items from a table<a name="dynamodb-enhanced-mapper-queryfilter"></a>

You can get items from a table based on filterable queries, and then perform operations on the query results\.

For the example below, assume the `Customer` table contains the following items:


| id | email | custName | registrationDate | 
| --- | --- | --- | --- | 
| id120 | spink@noserver\.com | Susan Pink | 2020\-04\-07T00:00:00Z | 
| id101 | jorange@noserver\.com | Jerry orange | 2020\-04\-07T00:00:00Z | 
| id101 | tred@noserver\.com | Tom red | 2020\-04\-07T00:00:00Z | 
| ed110 | fredp@noserver\.com | Fred Pink | 2020\-04\-07T00:00:00Z | 

The following steps describes what is happening in the `queryTableFilter` method below\.

1. Build an expression to filter the query\.

   1. You build a filter by first defining the value to match on as an [AttributeValue](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/dynamodb/model/AttributeValue.html) object \("Tom red" in this example\)\.

   1. You create a `HashMap` to hold a token as the map's key \(":value" in this example\) and the `AttributeValue` object as the map's value\.

   1. You then build an `[Expression](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/Expression.html)` using a filter expression \("custName = :value"\) and the expression values in the map\.

1. Build a `QueryConditional` object\.

   1. You build a [http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/model/QueryConditional.html](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/model/QueryConditional.html) object to select items based on the primary key value "id101"\.

1. Build the query request and execute the query\.

   1. Pass a lambda parameter to the DynamoDbTable's [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html#query(java.util.function.Consumer)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html#query(java.util.function.Consumer)) method\. The SDK uses the parameter's logic to build the final query request object to send to the DynamoDB service\.

1. Process the results\.

   1. This example processes the iterable results returned from the query in a *for*\-each loop by counting the number of items returned and printing out the `Customer` object\.

The following log output shows the request that is sent to the DynamoDB service\.

```
DEBUG org.apache.http.wire:87 - http-outgoing-0 >> "{"TableName":"Customer","FilterExpression":"custName = :value","KeyConditionExpression":"#AMZN_MAPPED_id = :AMZN_MAPPED_id","ExpressionAttributeNames":{"#AMZN_MAPPED_id":"id"},"ExpressionAttributeValues":{":AMZN_MAPPED_id":{"S":"id101"},":value":{"S":"Tom red"}}}"
```

**Note**  
The [QueryConditional](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/model/QueryConditional.html) interface has several methods you can use to build your queries, including common conditional statements like greater than, less than, and in between\.

 **Imports** 

```
import com.example.dynamodb.Customer;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.HashMap;
import java.util.Map;
```

 **Code** 

```
    public static Integer queryTableFilter(DynamoDbEnhancedClient enhancedClient) {

        Integer countOfCustomers = 0;

        try {
            DynamoDbTable<Customer> mappedTable = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));

            AttributeValue att = AttributeValue.builder()
                    .s("Tom red")
                    .build();

            Map<String, AttributeValue> expressionValues = new HashMap<>();
            expressionValues.put(":value", att);

            Expression expression = Expression.builder()
                    .expression("custName = :value")
                    .expressionValues(expressionValues)
                    .build();

            // Create a QueryConditional object to query by partitionValue.
            // Since the Customer table has a sort key attribute (email), we can use an expression
            // to filter the query results if multiple items have the same partition key value.
            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(Key.builder().partitionValue("id101")
                            .build());

            // Perform the query

            for (Customer customer : mappedTable.query(
                    r -> r.queryConditional(queryConditional)
                            .filterExpression(expression)
            ).items()) {
                countOfCustomers++;
                System.out.println(customer);
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Done");
        return countOfCustomers;
```

The code returns one item\. The `QueryConditional` object matches items that have an id of "id101" and the filter expression further restricts the items that are returned to items where `custName` is equal to "Tom red"\.

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/enhanced/EnhancedQueryRecordsWithFilter.java) on GitHub\.

## Retrieve \(get\) all items from a table<a name="dynamodb-enhanced-mapper-scan"></a>

When you want to get all of the records in a given DynamoDB table, use the `scan()` method of your [DynamoDbTable](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html) object and the `items()` method to get access to each item, against which you can execute various operations\. For example, the following code snippet prints out the id and customer name of each item in the `Customer` table\.

 **Imports** 

```
import com.example.dynamodb.Customer;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import java.util.Iterator;
```

 **Code** 

```
    public static void scan( DynamoDbEnhancedClient enhancedClient) {
        try{
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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/enhanced/EnhancedScanRecords.java) on GitHub\.

For more information, see [Working with items in DynamoDB ](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/WorkingWithItems.html) in the Amazon DynamoDB Developer Guide\.