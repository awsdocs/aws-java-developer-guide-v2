--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# DynamoDB examples using SDK for Java 2\.x<a name="java_dynamodb_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with DynamoDB\.

*Actions* are code excerpts that show you how to call individual DynamoDB functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple DynamoDB functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w591aac15c14b9c27c13)
+ [Scenarios](#w591aac15c14b9c27c15)

## Actions<a name="w591aac15c14b9c27c13"></a>

### Create a table<a name="dynamodb_CreateTable_java_topic"></a>

The following code example shows how to create a DynamoDB table\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb#readme)\. 
  

```
    public static String createTable(DynamoDbClient ddb, String tableName, String key) {
        DynamoDbWaiter dbWaiter = ddb.waiter();
        CreateTableRequest request = CreateTableRequest.builder()
            .attributeDefinitions(AttributeDefinition.builder()
                .attributeName(key)
                .attributeType(ScalarAttributeType.S)
                .build())
            .keySchema(KeySchemaElement.builder()
                .attributeName(key)
                .keyType(KeyType.HASH)
                .build())
            .provisionedThroughput(ProvisionedThroughput.builder()
                .readCapacityUnits(new Long(10))
                .writeCapacityUnits(new Long(10))
                .build())
            .tableName(tableName)
            .build();

        String newTable ="";
        try {
            CreateTableResponse response = ddb.createTable(request);
            DescribeTableRequest tableRequest = DescribeTableRequest.builder()
                .tableName(tableName)
                .build();

            // Wait until the Amazon DynamoDB table is created.
            WaiterResponse<DescribeTableResponse> waiterResponse = dbWaiter.waitUntilTableExists(tableRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);
            newTable = response.tableDescription().tableName();
            return newTable;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
       return "";
    }
```
+  For API details, see [CreateTable](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/CreateTable) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a table<a name="dynamodb_DeleteTable_java_topic"></a>

The following code example shows how to delete a DynamoDB table\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb#readme)\. 
  

```
    public static void deleteDynamoDBTable(DynamoDbClient ddb, String tableName) {

        DeleteTableRequest request = DeleteTableRequest.builder()
            .tableName(tableName)
            .build();

        try {
            ddb.deleteTable(request);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println(tableName +" was successfully deleted!");
    }
```
+  For API details, see [DeleteTable](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/DeleteTable) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete an item from a table<a name="dynamodb_DeleteItem_java_topic"></a>

The following code example shows how to delete an item from a DynamoDB table\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb#readme)\. 
  

```
    public static void deleteDymamoDBItem(DynamoDbClient ddb, String tableName, String key, String keyVal) {
        HashMap<String,AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put(key, AttributeValue.builder()
            .s(keyVal)
            .build());

        DeleteItemRequest deleteReq = DeleteItemRequest.builder()
            .tableName(tableName)
            .key(keyToGet)
            .build();

        try {
            ddb.deleteItem(deleteReq);
        } catch (DynamoDbException e) {
           System.err.println(e.getMessage());
           System.exit(1);
        }
    }
```
+  For API details, see [DeleteItem](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/DeleteItem) in *AWS SDK for Java 2\.x API Reference*\. 

### Get an item from a table<a name="dynamodb_GetItem_java_topic"></a>

The following code example shows how to get an item from a DynamoDB table\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb#readme)\. 
Gets an item from a table by using the DynamoDbClient\.  

```
    public static int queryTable(DynamoDbClient ddb, String tableName, String partitionKeyName, String partitionKeyVal, String partitionAlias) {

        // Set up an alias for the partition key name in case it's a reserved word.
        HashMap<String,String> attrNameAlias = new HashMap<String,String>();
        attrNameAlias.put(partitionAlias, partitionKeyName);

        // Set up mapping of the partition name with the value.
        HashMap<String, AttributeValue> attrValues = new HashMap<>();

        attrValues.put(":"+partitionKeyName, AttributeValue.builder()
            .s(partitionKeyVal)
            .build());

        QueryRequest queryReq = QueryRequest.builder()
            .tableName(tableName)
            .keyConditionExpression(partitionAlias + " = :" + partitionKeyName)
            .expressionAttributeNames(attrNameAlias)
            .expressionAttributeValues(attrValues)
            .build();

        try {
            QueryResponse response = ddb.query(queryReq);
            return response.count();

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return -1;
    }
```
+  For API details, see [GetItem](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/GetItem) in *AWS SDK for Java 2\.x API Reference*\. 

### Get information about a table<a name="dynamodb_DescribeTable_java_topic"></a>

The following code example shows how to get information about a DynamoDB table\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb#readme)\. 
  

```
    public static void describeDymamoDBTable(DynamoDbClient ddb,String tableName ) {

        DescribeTableRequest request = DescribeTableRequest.builder()
            .tableName(tableName)
            .build();

        try {
            TableDescription tableInfo = ddb.describeTable(request).table();
            if (tableInfo != null) {
                System.out.format("Table name  : %s\n", tableInfo.tableName());
                System.out.format("Table ARN   : %s\n", tableInfo.tableArn());
                System.out.format("Status      : %s\n", tableInfo.tableStatus());
                System.out.format("Item count  : %d\n", tableInfo.itemCount().longValue());
                System.out.format("Size (bytes): %d\n", tableInfo.tableSizeBytes().longValue());

                ProvisionedThroughputDescription throughputInfo = tableInfo.provisionedThroughput();
                System.out.println("Throughput");
                System.out.format("  Read Capacity : %d\n", throughputInfo.readCapacityUnits().longValue());
                System.out.format("  Write Capacity: %d\n", throughputInfo.writeCapacityUnits().longValue());

                List<AttributeDefinition> attributes = tableInfo.attributeDefinitions();
                System.out.println("Attributes");

                for (AttributeDefinition a : attributes) {
                    System.out.format("  %s (%s)\n", a.attributeName(), a.attributeType());
                }
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("\nDone!");
    }
```
+  For API details, see [DescribeTable](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/DescribeTable) in *AWS SDK for Java 2\.x API Reference*\. 

### List tables<a name="dynamodb_ListTables_java_topic"></a>

The following code example shows how to list DynamoDB tables\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb#readme)\. 
  

```
    public static void listAllTables(DynamoDbClient ddb){

        boolean moreTables = true;
        String lastName = null;

        while(moreTables) {
            try {
                ListTablesResponse response = null;
                if (lastName == null) {
                    ListTablesRequest request = ListTablesRequest.builder().build();
                    response = ddb.listTables(request);
                } else {
                    ListTablesRequest request = ListTablesRequest.builder()
                            .exclusiveStartTableName(lastName).build();
                    response = ddb.listTables(request);
                }

                List<String> tableNames = response.tableNames();
                if (tableNames.size() > 0) {
                    for (String curName : tableNames) {
                        System.out.format("* %s\n", curName);
                    }
                } else {
                    System.out.println("No tables found!");
                    System.exit(0);
                }

                lastName = response.lastEvaluatedTableName();
                if (lastName == null) {
                    moreTables = false;
                }

            } catch (DynamoDbException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        System.out.println("\nDone!");
    }
```
+  For API details, see [ListTables](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/ListTables) in *AWS SDK for Java 2\.x API Reference*\. 

### Put an item in a table<a name="dynamodb_PutItem_java_topic"></a>

The following code example shows how to put an item in a DynamoDB table\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb#readme)\. 
Puts an item into a table by using the enhanced client\.  

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
            custRecord.setId("id119");
            custRecord.setEmail("tred@noserver.com");
            custRecord.setRegistrationDate(instant) ;

            // Put the customer data into an Amazon DynamoDB table.
            custTable.putItem(custRecord);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Customer data added to the table.");
    }
```
+  For API details, see [PutItem](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/PutItem) in *AWS SDK for Java 2\.x API Reference*\. 

### Query a table<a name="dynamodb_Query_java_topic"></a>

The following code example shows how to query a DynamoDB table\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb#readme)\. 
Queries a table by using the enhanced client\.  

```
    public static String queryTable(DynamoDbEnhancedClient enhancedClient) {

        try{
            DynamoDbTable<Customer> mappedTable = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                .partitionValue("id101")
                .build());

            // Get items in the table and write out the ID value.
            Iterator<Customer> results = mappedTable.query(queryConditional).items().iterator();
            String result="";

            while (results.hasNext()) {
                Customer rec = results.next();
                result = rec.getId();
                System.out.println("The record id is "+result);
            }
            return result;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }
```
Queries a table by using the enhanced client and a secondary index\.  

```
    public static void queryIndex(DynamoDbClient ddb, String tableName) {

        try {
            // Create a DynamoDbEnhancedClient and use the DynamoDbClient object.
            DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();

            //Create a DynamoDbTable object based on Movies.
            DynamoDbTable<Movies> table = enhancedClient.table("Movies", TableSchema.fromBean(Movies.class));
            String dateVal = "2013";

            DynamoDbIndex<Movies> secIndex = enhancedClient.table("Movies", TableSchema.fromBean(Movies.class)) .index("year-index");
            AttributeValue attVal = AttributeValue.builder()
                .n(dateVal)
                .build();

            // Create a QueryConditional object that's used in the query operation.
            QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(attVal)
                .build());

            // Get items in the table.
            SdkIterable<Page<Movies>> results = secIndex.query(QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .limit(300)
                .build());

            // Display the results.
            results.forEach(page -> {
                List<Movies> allMovies = page.items();
                for (Movies myMovies: allMovies) {
                    System.out.println("The movie title is " + myMovies.getTitle() + ". The year is " + myMovies.getYear());
                }
            });

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
Queries a table by using the DynamoDbClient\.  

```
    public static int queryTable(DynamoDbClient ddb, String tableName, String partitionKeyName, String partitionKeyVal, String partitionAlias) {

        // Set up an alias for the partition key name in case it's a reserved word.
        HashMap<String,String> attrNameAlias = new HashMap<String,String>();
        attrNameAlias.put(partitionAlias, partitionKeyName);

        // Set up mapping of the partition name with the value.
        HashMap<String, AttributeValue> attrValues = new HashMap<>();

        attrValues.put(":"+partitionKeyName, AttributeValue.builder()
            .s(partitionKeyVal)
            .build());

        QueryRequest queryReq = QueryRequest.builder()
            .tableName(tableName)
            .keyConditionExpression(partitionAlias + " = :" + partitionKeyName)
            .expressionAttributeNames(attrNameAlias)
            .expressionAttributeValues(attrValues)
            .build();

        try {
            QueryResponse response = ddb.query(queryReq);
            return response.count();

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return -1;
    }
```
Queries a table by using the DynamoDbClient and a secondary index\.  

```
    public static void queryIndex(DynamoDbClient ddb, String tableName) {

        try {
            Map<String, String> expressionAttributesNames = new HashMap<>();
            expressionAttributesNames.put("#year", "year");
            Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
            expressionAttributeValues.put(":yearValue", AttributeValue.builder().n("2013").build());

            QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName("year-index")
                .keyConditionExpression("#year = :yearValue")
                .expressionAttributeNames(expressionAttributesNames)
                .expressionAttributeValues(expressionAttributeValues)
                .build();

            System.out.println("=== Movie Titles ===");
            QueryResponse response = ddb.query(request);
            response.items()
                .forEach(movie -> System.out.println(movie.get("title").s()));

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [Query](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/Query) in *AWS SDK for Java 2\.x API Reference*\. 

### Scan a table<a name="dynamodb_Scan_java_topic"></a>

The following code example shows how to scan a DynamoDB table\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb#readme)\. 
Scans an Amazon DynamoDB table by using the enhanced client\.  

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
+  For API details, see [Scan](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/Scan) in *AWS SDK for Java 2\.x API Reference*\. 

### Update an item in a table<a name="dynamodb_UpdateItem_java_topic"></a>

The following code example shows how to update an item in a DynamoDB table\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb#readme)\. 
Updates an item located in a table by using the enhanced client\.  

```
    public static String modifyItem(DynamoDbEnhancedClient enhancedClient, String keyVal, String email) {

        try {

            DynamoDbTable<Customer> mappedTable = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));
            Key key = Key.builder()
                .partitionValue(keyVal)
                .build();

            // Get the item by using the key and update the email value.
            Customer customerRec = mappedTable.getItem(r->r.key(key));
            customerRec.setEmail(email);
            mappedTable.updateItem(customerRec);
            return customerRec.getEmail();

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [UpdateItem](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/UpdateItem) in *AWS SDK for Java 2\.x API Reference*\. 

### Write a batch of items<a name="dynamodb_BatchWriteItem_java_topic"></a>

The following code example shows how to write a batch of DynamoDB items\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb/#readme)\. 
Inserts many items into a table by using the enhanced client\.  

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

            BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(WriteBatch.builder(Customer.class)
                    .mappedTableResource(mappedTable)
                    .addPutItem(r -> r.item(record2))
                    .addPutItem(r -> r.item(record3))
                    .build())
                .build();

            // Add these two items to the table.
            enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);
            System.out.println("done");

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [BatchWriteItem](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/BatchWriteItem) in *AWS SDK for Java 2\.x API Reference*\. 

## Scenarios<a name="w591aac15c14b9c27c15"></a>

### Get started using tables, items, and queries<a name="dynamodb_Scenario_GettingStartedMovies_java_topic"></a>

The following code example shows how to:
+ Create a table that can hold movie data\.
+ Put, get, and update a single movie in the table\.
+ Write movie data to the table from a sample JSON file\.
+ Query for movies that were released in a given year\.
+ Scan for movies that were released in a range of years\.
+ Delete a movie from the table\.
+ Delete the table\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb#readme)\. 
Create a DynamoDB table\.  

```
    // Create a table with a Sort key.
    public static void createTable(DynamoDbClient ddb, String tableName) {

        DynamoDbWaiter dbWaiter = ddb.waiter();
        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<>();

        // Define attributes.
        attributeDefinitions.add(AttributeDefinition.builder()
            .attributeName("year")
            .attributeType("N")
            .build());

        attributeDefinitions.add(AttributeDefinition.builder()
            .attributeName("title")
            .attributeType("S")
            .build());

        ArrayList<KeySchemaElement> tableKey = new ArrayList<>();
        KeySchemaElement key = KeySchemaElement.builder()
            .attributeName("year")
            .keyType(KeyType.HASH)
            .build();

        KeySchemaElement key2 = KeySchemaElement.builder()
            .attributeName("title")
            .keyType(KeyType.RANGE)
            .build();

        // Add KeySchemaElement objects to the list.
        tableKey.add(key);
        tableKey.add(key2);

        CreateTableRequest request = CreateTableRequest.builder()
            .keySchema(tableKey)
            .provisionedThroughput(ProvisionedThroughput.builder()
                .readCapacityUnits(new Long(10))
                .writeCapacityUnits(new Long(10))
                .build())
            .attributeDefinitions(attributeDefinitions)
            .tableName(tableName)
            .build();

        try {
            CreateTableResponse response = ddb.createTable(request);
            DescribeTableRequest tableRequest = DescribeTableRequest.builder()
                .tableName(tableName)
                .build();

            // Wait until the Amazon DynamoDB table is created.
            WaiterResponse<DescribeTableResponse> waiterResponse = dbWaiter.waitUntilTableExists(tableRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);
            String newTable = response.tableDescription().tableName();
            System.out.println("The " +newTable + " was successfully created.");

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
Create a helper function to download and extract the sample JSON file\.  

```
    // Load data into the table.
    public static void loadData(DynamoDbClient ddb, String tableName, String fileName) throws IOException {

        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build();

        DynamoDbTable<Movies> mappedTable = enhancedClient.table("Movies", TableSchema.fromBean(Movies.class));
        JsonParser parser = new JsonFactory().createParser(new File(fileName));
        com.fasterxml.jackson.databind.JsonNode rootNode = new ObjectMapper().readTree(parser);
        Iterator<JsonNode> iter = rootNode.iterator();
        ObjectNode currentNode;
        int t = 0 ;
        while (iter.hasNext()) {

            // Only add 200 Movies to the table.
            if (t == 200)
                break ;
            currentNode = (ObjectNode) iter.next();

            int year = currentNode.path("year").asInt();
            String title = currentNode.path("title").asText();
            String info = currentNode.path("info").toString();

            Movies movies = new Movies();
            movies.setYear(year);
            movies.setTitle(title);
            movies.setInfo(info);

            // Put the data into the Amazon DynamoDB Movie table.
            mappedTable.putItem(movies);
            t++;
        }
    }
```
Get an item from a table\.  

```
    public static void getItem(DynamoDbClient ddb) {

        HashMap<String,AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put("year", AttributeValue.builder()
            .n("1933")
            .build());

        keyToGet.put("title", AttributeValue.builder()
            .s("King Kong")
            .build());

        GetItemRequest request = GetItemRequest.builder()
            .key(keyToGet)
            .tableName("Movies")
            .build();

        try {
            Map<String,AttributeValue> returnedItem = ddb.getItem(request).item();

            if (returnedItem != null) {
                Set<String> keys = returnedItem.keySet();
                System.out.println("Amazon DynamoDB table attributes: \n");

                for (String key1 : keys) {
                    System.out.format("%s: %s\n", key1, returnedItem.get(key1).toString());
                }
            } else {
                System.out.format("No item found with the key %s!\n", "year");
            }

        } catch (DynamoDbException e) {
             System.err.println(e.getMessage());
             System.exit(1);
        }
    }
```
Full example\.  

```
public class Scenario {

    public static void main(String[] args) throws IOException {

        final String usage = "\n" +
            "Usage:\n" +
            "    <fileName>\n\n" +
            "Where:\n" +
            "    fileName - The path to the moviedata.json file that you can download from the Amazon DynamoDB Developer Guide.\n" ;

        if (args.length != 1) {
            System.out.println(usage);
            System.exit(1);
        }

        String tableName = "Movies";
        String fileName = args[0];
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.US_EAST_1;
        DynamoDbClient ddb = DynamoDbClient.builder()
            .region(region)
            .credentialsProvider(credentialsProvider)
            .build();

        System.out.println("******* Creating an Amazon DynamoDB table named Movies with a key named year and a sort key named title.");
        createTable(ddb, tableName);

        System.out.println("******* Loading data into the Amazon DynamoDB table.");
        loadData(ddb, tableName, fileName);

        System.out.println("******* Getting data from the Movie table.");
        getItem(ddb) ;

        System.out.println("******* Putting a record into the Amazon DynamoDB table.");
        putRecord(ddb);

        System.out.println("******* Updating a record.");
        updateTableItem(ddb, tableName);

        System.out.println("******* Scanning the Amazon DynamoDB table.");
        scanMovies(ddb, tableName);

        System.out.println("******* Querying the Movies released in 2013.");
        queryTable(ddb);

        System.out.println("******* Deleting the Amazon DynamoDB table.");
        deleteDynamoDBTable(ddb, tableName);
        ddb.close();
    }

    // Create a table with a Sort key.
    public static void createTable(DynamoDbClient ddb, String tableName) {

        DynamoDbWaiter dbWaiter = ddb.waiter();
        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<>();

        // Define attributes.
        attributeDefinitions.add(AttributeDefinition.builder()
            .attributeName("year")
            .attributeType("N")
            .build());

        attributeDefinitions.add(AttributeDefinition.builder()
            .attributeName("title")
            .attributeType("S")
            .build());

        ArrayList<KeySchemaElement> tableKey = new ArrayList<>();
        KeySchemaElement key = KeySchemaElement.builder()
            .attributeName("year")
            .keyType(KeyType.HASH)
            .build();

        KeySchemaElement key2 = KeySchemaElement.builder()
            .attributeName("title")
            .keyType(KeyType.RANGE)
            .build();

        // Add KeySchemaElement objects to the list.
        tableKey.add(key);
        tableKey.add(key2);

        CreateTableRequest request = CreateTableRequest.builder()
            .keySchema(tableKey)
            .provisionedThroughput(ProvisionedThroughput.builder()
                .readCapacityUnits(new Long(10))
                .writeCapacityUnits(new Long(10))
                .build())
            .attributeDefinitions(attributeDefinitions)
            .tableName(tableName)
            .build();

        try {
            CreateTableResponse response = ddb.createTable(request);
            DescribeTableRequest tableRequest = DescribeTableRequest.builder()
                .tableName(tableName)
                .build();

            // Wait until the Amazon DynamoDB table is created.
            WaiterResponse<DescribeTableResponse> waiterResponse = dbWaiter.waitUntilTableExists(tableRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);
            String newTable = response.tableDescription().tableName();
            System.out.println("The " +newTable + " was successfully created.");

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    // Query the table.
    public static void queryTable(DynamoDbClient ddb) {
        try {
            DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();

            DynamoDbTable<Movies> custTable = enhancedClient.table("Movies", TableSchema.fromBean(Movies.class));
            QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder()
                .partitionValue(2013)
                .build());

            // Get items in the table and write out the ID value.
            Iterator<Movies> results = custTable.query(queryConditional).items().iterator();
            String result="";

            while (results.hasNext()) {
                Movies rec = results.next();
                System.out.println("The title of the movie is "+rec.getTitle());
                System.out.println("The movie information  is "+rec.getInfo());
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            }
        }

        // Scan the table.
        public static void scanMovies(DynamoDbClient ddb, String tableName) {

            System.out.println("******* Scanning all movies.\n");
            try{
                DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                        .dynamoDbClient(ddb)
                        .build();

                DynamoDbTable<Movies> custTable = enhancedClient.table("Movies", TableSchema.fromBean(Movies.class));
                Iterator<Movies> results = custTable.scan().items().iterator();
                while (results.hasNext()) {
                    Movies rec = results.next();
                    System.out.println("The movie title is "+rec.getTitle());
                    System.out.println("The movie year is " +rec.getYear());
                }

            } catch (DynamoDbException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }

    // Load data into the table.
    public static void loadData(DynamoDbClient ddb, String tableName, String fileName) throws IOException {

        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build();

        DynamoDbTable<Movies> mappedTable = enhancedClient.table("Movies", TableSchema.fromBean(Movies.class));
        JsonParser parser = new JsonFactory().createParser(new File(fileName));
        com.fasterxml.jackson.databind.JsonNode rootNode = new ObjectMapper().readTree(parser);
        Iterator<JsonNode> iter = rootNode.iterator();
        ObjectNode currentNode;
        int t = 0 ;
        while (iter.hasNext()) {

            // Only add 200 Movies to the table.
            if (t == 200)
                break ;
            currentNode = (ObjectNode) iter.next();

            int year = currentNode.path("year").asInt();
            String title = currentNode.path("title").asText();
            String info = currentNode.path("info").toString();

            Movies movies = new Movies();
            movies.setYear(year);
            movies.setTitle(title);
            movies.setInfo(info);

            // Put the data into the Amazon DynamoDB Movie table.
            mappedTable.putItem(movies);
            t++;
        }
    }

    // Update the record to include show only directors.
    public static void updateTableItem(DynamoDbClient ddb, String tableName){

        HashMap<String,AttributeValue> itemKey = new HashMap<>();
        itemKey.put("year", AttributeValue.builder().n("1933").build());
        itemKey.put("title", AttributeValue.builder().s("King Kong").build());

        HashMap<String,AttributeValueUpdate> updatedValues = new HashMap<>();
        updatedValues.put("info", AttributeValueUpdate.builder()
            .value(AttributeValue.builder().s("{\"directors\":[\"Merian C. Cooper\",\"Ernest B. Schoedsack\"]").build())
            .action(AttributeAction.PUT)
            .build());

        UpdateItemRequest request = UpdateItemRequest.builder()
            .tableName(tableName)
            .key(itemKey)
            .attributeUpdates(updatedValues)
            .build();

        try {
            ddb.updateItem(request);
        } catch (ResourceNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Item was updated!");
    }

    public static void deleteDynamoDBTable(DynamoDbClient ddb, String tableName) {

        DeleteTableRequest request = DeleteTableRequest.builder()
            .tableName(tableName)
            .build();

        try {
            ddb.deleteTable(request);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println(tableName +" was successfully deleted!");
    }

    public static void putRecord(DynamoDbClient ddb) {

        try {
            DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();

            DynamoDbTable<Movies> table = enhancedClient.table("Movies", TableSchema.fromBean(Movies.class));

            // Populate the Table.
            Movies record = new Movies();
            record.setYear(2020);
            record.setTitle("My Movie2");
            record.setInfo("no info");
            table.putItem(record);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Added a new movie to the table.");
    }

    public static void getItem(DynamoDbClient ddb) {

        HashMap<String,AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put("year", AttributeValue.builder()
            .n("1933")
            .build());

        keyToGet.put("title", AttributeValue.builder()
            .s("King Kong")
            .build());

        GetItemRequest request = GetItemRequest.builder()
            .key(keyToGet)
            .tableName("Movies")
            .build();

        try {
            Map<String,AttributeValue> returnedItem = ddb.getItem(request).item();

            if (returnedItem != null) {
                Set<String> keys = returnedItem.keySet();
                System.out.println("Amazon DynamoDB table attributes: \n");

                for (String key1 : keys) {
                    System.out.format("%s: %s\n", key1, returnedItem.get(key1).toString());
                }
            } else {
                System.out.format("No item found with the key %s!\n", "year");
            }

        } catch (DynamoDbException e) {
             System.err.println(e.getMessage());
             System.exit(1);
        }
    }
}
```
+ For API details, see the following topics in *AWS SDK for Java 2\.x API Reference*\.
  + [BatchWriteItem](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/BatchWriteItem)
  + [CreateTable](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/CreateTable)
  + [DeleteItem](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/DeleteItem)
  + [DeleteTable](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/DeleteTable)
  + [DescribeTable](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/DescribeTable)
  + [GetItem](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/GetItem)
  + [PutItem](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/PutItem)
  + [Query](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/Query)
  + [Scan](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/Scan)
  + [UpdateItem](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/UpdateItem)

### Query a table by using batches of PartiQL statements<a name="dynamodb_Scenario_PartiQLBatch_java_topic"></a>

The following code example shows how to:
+ Get a batch of items by running multiple SELECT statements\.
+ Add a batch of items by running multiple INSERT statements\.
+ Update a batch of items by running multiple UPDATE statements\.
+ Delete a batch of items by running multiple DELETE statements\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb#readme)\. 
  

```
public class ScenarioPartiQLBatch {

    public static void main(String [] args) throws IOException {

        String tableName = "MoviesPartiQBatch";
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.US_EAST_1;
        DynamoDbClient ddb = DynamoDbClient.builder()
            .credentialsProvider(credentialsProvider)
            .region(region)
            .build();

        System.out.println("******* Creating an Amazon DynamoDB table named "+tableName +" with a key named year and a sort key named title.");
        createTable(ddb, tableName);

        System.out.println("******* Adding multiple records into the "+ tableName +" table using a batch command.");
        putRecordBatch(ddb);

        System.out.println("******* Updating multiple records using a batch command.");
        updateTableItemBatch(ddb);

        System.out.println("******* Deleting multiple records using a batch command.");
        deleteItemBatch(ddb);

        System.out.println("******* Deleting the Amazon DynamoDB table.");
        deleteDynamoDBTable(ddb, tableName);
        ddb.close();
    }

    public static void createTable(DynamoDbClient ddb, String tableName) {
        DynamoDbWaiter dbWaiter = ddb.waiter();
        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<>();

        // Define attributes.
        attributeDefinitions.add(AttributeDefinition.builder()
            .attributeName("year")
            .attributeType("N")
            .build());

        attributeDefinitions.add(AttributeDefinition.builder()
            .attributeName("title")
            .attributeType("S")
            .build());

        ArrayList<KeySchemaElement> tableKey = new ArrayList<>();
        KeySchemaElement key = KeySchemaElement.builder()
            .attributeName("year")
            .keyType(KeyType.HASH)
            .build();

        KeySchemaElement key2 = KeySchemaElement.builder()
            .attributeName("title")
            .keyType(KeyType.RANGE) // Sort
            .build();

        // Add KeySchemaElement objects to the list.
        tableKey.add(key);
        tableKey.add(key2);

        CreateTableRequest request = CreateTableRequest.builder()
            .keySchema(tableKey)
            .provisionedThroughput(ProvisionedThroughput.builder()
                .readCapacityUnits(new Long(10))
                .writeCapacityUnits(new Long(10))
                .build())
            .attributeDefinitions(attributeDefinitions)
            .tableName(tableName)
            .build();

        try {
            CreateTableResponse response = ddb.createTable(request);
            DescribeTableRequest tableRequest = DescribeTableRequest.builder()
                .tableName(tableName)
                .build();

            // Wait until the Amazon DynamoDB table is created.
            WaiterResponse<DescribeTableResponse> waiterResponse = dbWaiter.waitUntilTableExists(tableRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);
            String newTable = response.tableDescription().tableName();
            System.out.println("The " +newTable + " was successfully created.");

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void putRecordBatch(DynamoDbClient ddb) {
        String sqlStatement = "INSERT INTO MoviesPartiQBatch VALUE {'year':?, 'title' : ?, 'info' : ?}";
        try {
            // Create three movies to add to the Amazon DynamoDB table.
            // Set data for Movie 1.
            List<AttributeValue> parameters = new ArrayList<>();

            AttributeValue att1 = AttributeValue.builder()
                .n(String.valueOf("2022"))
                .build();

            AttributeValue att2 = AttributeValue.builder()
                .s("My Movie 1")
                .build();

            AttributeValue att3 = AttributeValue.builder()
                .s("No Information")
                .build();

            parameters.add(att1);
            parameters.add(att2);
            parameters.add(att3);

            BatchStatementRequest statementRequestMovie1 = BatchStatementRequest.builder()
                .statement(sqlStatement)
                .parameters(parameters)
                .build();

            // Set data for Movie 2.
            List<AttributeValue> parametersMovie2 = new ArrayList<>();
            AttributeValue attMovie2 = AttributeValue.builder()
                .n(String.valueOf("2022"))
                .build();

            AttributeValue attMovie2A = AttributeValue.builder()
                .s("My Movie 2")
                .build();

            AttributeValue attMovie2B = AttributeValue.builder()
                .s("No Information")
                .build();

            parametersMovie2.add(attMovie2);
            parametersMovie2.add(attMovie2A);
            parametersMovie2.add(attMovie2B);

            BatchStatementRequest statementRequestMovie2 = BatchStatementRequest.builder()
                .statement(sqlStatement)
                .parameters(parametersMovie2)
                .build();

            // Set data for Movie 3.
            List<AttributeValue> parametersMovie3 = new ArrayList<>();
            AttributeValue attMovie3 = AttributeValue.builder()
                .n(String.valueOf("2022"))
                .build();

            AttributeValue attMovie3A = AttributeValue.builder()
                .s("My Movie 3")
                .build();

            AttributeValue attMovie3B = AttributeValue.builder()
                .s("No Information")
                .build();

            parametersMovie3.add(attMovie3);
            parametersMovie3.add(attMovie3A);
            parametersMovie3.add(attMovie3B);

            BatchStatementRequest statementRequestMovie3 = BatchStatementRequest.builder()
                .statement(sqlStatement)
                .parameters(parametersMovie3)
                .build();

            // Add all three movies to the list.
            List<BatchStatementRequest> myBatchStatementList = new ArrayList<>();
            myBatchStatementList.add(statementRequestMovie1);
            myBatchStatementList.add(statementRequestMovie2);
            myBatchStatementList.add(statementRequestMovie3);

            BatchExecuteStatementRequest batchRequest = BatchExecuteStatementRequest.builder()
                .statements(myBatchStatementList)
                .build();

            BatchExecuteStatementResponse response = ddb.batchExecuteStatement(batchRequest);
            System.out.println("ExecuteStatement successful: "+ response.toString());
            System.out.println("Added new movies using a batch command.");

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void updateTableItemBatch(DynamoDbClient ddb){
        String sqlStatement = "UPDATE MoviesPartiQBatch SET info = 'directors\":[\"Merian C. Cooper\",\"Ernest B. Schoedsack' where year=? and title=?";
        List<AttributeValue> parametersRec1 = new ArrayList<>();

        // Update three records.
        AttributeValue att1 = AttributeValue.builder()
            .n(String.valueOf("2022"))
            .build();

        AttributeValue att2 = AttributeValue.builder()
            .s("My Movie 1")
            .build();

        parametersRec1.add(att1);
        parametersRec1.add(att2);

        BatchStatementRequest statementRequestRec1 = BatchStatementRequest.builder()
            .statement(sqlStatement)
            .parameters(parametersRec1)
            .build();

        // Update record 2.
        List<AttributeValue> parametersRec2 = new ArrayList<>();
        AttributeValue attRec2 = AttributeValue.builder()
            .n(String.valueOf("2022"))
            .build();

        AttributeValue attRec2a = AttributeValue.builder()
            .s("My Movie 2")
            .build();

        parametersRec2.add(attRec2);
        parametersRec2.add(attRec2a);
        BatchStatementRequest statementRequestRec2 = BatchStatementRequest.builder()
            .statement(sqlStatement)
            .parameters(parametersRec2)
            .build();

        // Update record 3.
        List<AttributeValue> parametersRec3 = new ArrayList<>();
        AttributeValue attRec3 = AttributeValue.builder()
            .n(String.valueOf("2022"))
            .build();

        AttributeValue attRec3a = AttributeValue.builder()
            .s("My Movie 3")
            .build();

        parametersRec3.add(attRec3);
        parametersRec3.add(attRec3a);
        BatchStatementRequest statementRequestRec3 = BatchStatementRequest.builder()
            .statement(sqlStatement)
            .parameters(parametersRec3)
            .build();

        // Add all three movies to the list.
        List<BatchStatementRequest> myBatchStatementList = new ArrayList<>();
        myBatchStatementList.add(statementRequestRec1);
        myBatchStatementList.add(statementRequestRec2);
        myBatchStatementList.add(statementRequestRec3);

        BatchExecuteStatementRequest batchRequest = BatchExecuteStatementRequest.builder()
            .statements(myBatchStatementList)
            .build();

        try {
            BatchExecuteStatementResponse response = ddb.batchExecuteStatement(batchRequest);
            System.out.println("ExecuteStatement successful: "+ response.toString());
            System.out.println("Updated three movies using a batch command.");

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Item was updated!");
    }

    public static void deleteItemBatch(DynamoDbClient ddb){
        String sqlStatement = "DELETE FROM MoviesPartiQBatch WHERE year = ? and title=?";
        List<AttributeValue> parametersRec1 = new ArrayList<>();

        // Specify three records to delete.
        AttributeValue att1 = AttributeValue.builder()
            .n(String.valueOf("2022"))
            .build();

        AttributeValue att2 = AttributeValue.builder()
            .s("My Movie 1")
            .build();

        parametersRec1.add(att1);
        parametersRec1.add(att2);

        BatchStatementRequest statementRequestRec1 = BatchStatementRequest.builder()
            .statement(sqlStatement)
            .parameters(parametersRec1)
            .build();

        // Specify record 2.
        List<AttributeValue> parametersRec2 = new ArrayList<>();
        AttributeValue attRec2 = AttributeValue.builder()
            .n(String.valueOf("2022"))
            .build();

        AttributeValue attRec2a = AttributeValue.builder()
            .s("My Movie 2")
            .build();

        parametersRec2.add(attRec2);
        parametersRec2.add(attRec2a);
        BatchStatementRequest statementRequestRec2 = BatchStatementRequest.builder()
            .statement(sqlStatement)
            .parameters(parametersRec2)
            .build();

        // Specify record 3.
        List<AttributeValue> parametersRec3 = new ArrayList<>();
        AttributeValue attRec3 = AttributeValue.builder()
            .n(String.valueOf("2022"))
            .build();

        AttributeValue attRec3a = AttributeValue.builder()
            .s("My Movie 3")
            .build();

        parametersRec3.add(attRec3);
        parametersRec3.add(attRec3a);

        BatchStatementRequest statementRequestRec3 = BatchStatementRequest.builder()
            .statement(sqlStatement)
            .parameters(parametersRec3)
            .build();

        // Add all three movies to the list.
        List<BatchStatementRequest> myBatchStatementList = new ArrayList<>();
        myBatchStatementList.add(statementRequestRec1);
        myBatchStatementList.add(statementRequestRec2);
        myBatchStatementList.add(statementRequestRec3);

        BatchExecuteStatementRequest batchRequest = BatchExecuteStatementRequest.builder()
            .statements(myBatchStatementList)
            .build();

        try {
            ddb.batchExecuteStatement(batchRequest);
            System.out.println("Deleted three movies using a batch command.");

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void deleteDynamoDBTable(DynamoDbClient ddb, String tableName) {
        DeleteTableRequest request = DeleteTableRequest.builder()
            .tableName(tableName)
            .build();

        try {
            ddb.deleteTable(request);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println(tableName +" was successfully deleted!");
    }

    private static ExecuteStatementResponse executeStatementRequest(DynamoDbClient ddb, String statement, List<AttributeValue> parameters ) {
        ExecuteStatementRequest request = ExecuteStatementRequest.builder()
            .statement(statement)
            .parameters(parameters)
            .build();

        return ddb.executeStatement(request);
    }
}
```
+  For API details, see [BatchExecuteStatement](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/BatchExecuteStatement) in *AWS SDK for Java 2\.x API Reference*\. 

### Query a table using PartiQL<a name="dynamodb_Scenario_PartiQLSingle_java_topic"></a>

The following code example shows how to:
+ Get an item by running a SELECT statement\.
+ Add an item by running an INSERT statement\.
+ Update an item by running an UPDATE statement\.
+ Delete an item by running a DELETE statement\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb#readme)\. 
  

```
public class ScenarioPartiQ {

    public static void main(String [] args) throws IOException {

        final String usage = "\n" +
            "Usage:\n" +
            "    <fileName>\n\n" +
            "Where:\n" +
            "    fileName - The path to the moviedata.json file that you can download from the Amazon DynamoDB Developer Guide.\n" ;

       if (args.length != 1) {
           System.out.println(usage);
           System.exit(1);
       }

       String fileName = args[0];
       String tableName = "MoviesPartiQ";
       ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
       Region region = Region.US_EAST_1;
       DynamoDbClient ddb = DynamoDbClient.builder()
           .credentialsProvider(credentialsProvider)
           .region(region)
           .build();

       System.out.println("******* Creating an Amazon DynamoDB table named MoviesPartiQ with a key named year and a sort key named title.");
       createTable(ddb, tableName);

       System.out.println("******* Loading data into the MoviesPartiQ table.");
       loadData(ddb, fileName);

       System.out.println("******* Getting data from the MoviesPartiQ table.");
       getItem(ddb);

       System.out.println("******* Putting a record into the MoviesPartiQ table.");
       putRecord(ddb);

       System.out.println("******* Updating a record.");
       updateTableItem(ddb);

       System.out.println("******* Querying the movies released in 2013.");
       queryTable(ddb);

       System.out.println("******* Deleting the Amazon DynamoDB table.");
       deleteDynamoDBTable(ddb, tableName);
       ddb.close();
    }

    public static void createTable(DynamoDbClient ddb, String tableName) {
        DynamoDbWaiter dbWaiter = ddb.waiter();
        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<>();

        // Define attributes.
        attributeDefinitions.add(AttributeDefinition.builder()
            .attributeName("year")
            .attributeType("N")
            .build());

        attributeDefinitions.add(AttributeDefinition.builder()
            .attributeName("title")
            .attributeType("S")
            .build());

        ArrayList<KeySchemaElement> tableKey = new ArrayList<>();
        KeySchemaElement key = KeySchemaElement.builder()
            .attributeName("year")
            .keyType(KeyType.HASH)
            .build();

        KeySchemaElement key2 = KeySchemaElement.builder()
            .attributeName("title")
            .keyType(KeyType.RANGE) // Sort
            .build();

        // Add KeySchemaElement objects to the list.
        tableKey.add(key);
        tableKey.add(key2);

        CreateTableRequest request = CreateTableRequest.builder()
            .keySchema(tableKey)
            .provisionedThroughput(ProvisionedThroughput.builder()
                .readCapacityUnits(new Long(10))
                .writeCapacityUnits(new Long(10))
                .build())
            .attributeDefinitions(attributeDefinitions)
            .tableName(tableName)
            .build();

        try {
            CreateTableResponse response = ddb.createTable(request);
            DescribeTableRequest tableRequest = DescribeTableRequest.builder()
                .tableName(tableName)
                .build();

            // Wait until the Amazon DynamoDB table is created.
            WaiterResponse<DescribeTableResponse> waiterResponse = dbWaiter.waitUntilTableExists(tableRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);
            String newTable = response.tableDescription().tableName();
            System.out.println("The " +newTable + " was successfully created.");

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    // Load data into the table.
    public static void loadData(DynamoDbClient ddb, String fileName) throws IOException {

        String sqlStatement = "INSERT INTO MoviesPartiQ VALUE {'year':?, 'title' : ?, 'info' : ?}";
        JsonParser parser = new JsonFactory().createParser(new File(fileName));
        com.fasterxml.jackson.databind.JsonNode rootNode = new ObjectMapper().readTree(parser);
        Iterator<JsonNode> iter = rootNode.iterator();
        ObjectNode currentNode;
        int t = 0 ;
        List<AttributeValue> parameters = new ArrayList<>();
        while (iter.hasNext()) {

            // Add 200 movies to the table.
            if (t == 200)
                break ;
            currentNode = (ObjectNode) iter.next();

            int year = currentNode.path("year").asInt();
            String title = currentNode.path("title").asText();
            String info = currentNode.path("info").toString();

            AttributeValue att1 = AttributeValue.builder()
                .n(String.valueOf(year))
                .build();

            AttributeValue att2 = AttributeValue.builder()
                .s(title)
                .build();

            AttributeValue att3 = AttributeValue.builder()
                .s(info)
                .build();

            parameters.add(att1);
            parameters.add(att2);
            parameters.add(att3);

            // Insert the movie into the Amazon DynamoDB table.
            executeStatementRequest(ddb, sqlStatement, parameters);
            System.out.println("Added Movie " +title);

            parameters.remove(att1);
            parameters.remove(att2);
            parameters.remove(att3);
            t++;
        }
    }

    public static void getItem(DynamoDbClient ddb) {

        String sqlStatement = "SELECT * FROM MoviesPartiQ where year=? and title=?";
        List<AttributeValue> parameters = new ArrayList<>();
        AttributeValue att1 = AttributeValue.builder()
            .n("2012")
            .build();

        AttributeValue att2 = AttributeValue.builder()
            .s("The Perks of Being a Wallflower")
            .build();

        parameters.add(att1);
        parameters.add(att2);

        try {
            ExecuteStatementResponse response = executeStatementRequest(ddb, sqlStatement, parameters);
            System.out.println("ExecuteStatement successful: "+ response.toString());

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void putRecord(DynamoDbClient ddb) {

        String sqlStatement = "INSERT INTO MoviesPartiQ VALUE {'year':?, 'title' : ?, 'info' : ?}";
        try {
            List<AttributeValue> parameters = new ArrayList<>();

            AttributeValue att1 = AttributeValue.builder()
                .n(String.valueOf("2020"))
                .build();

            AttributeValue att2 = AttributeValue.builder()
                .s("My Movie")
                .build();

            AttributeValue att3 = AttributeValue.builder()
                .s("No Information")
                .build();

            parameters.add(att1);
            parameters.add(att2);
            parameters.add(att3);

            executeStatementRequest(ddb, sqlStatement, parameters);
            System.out.println("Added new movie.");

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void updateTableItem(DynamoDbClient ddb){

        String sqlStatement = "UPDATE MoviesPartiQ SET info = 'directors\":[\"Merian C. Cooper\",\"Ernest B. Schoedsack' where year=? and title=?";
        List<AttributeValue> parameters = new ArrayList<>();
        AttributeValue att1 = AttributeValue.builder()
            .n(String.valueOf("2013"))
            .build();

        AttributeValue att2 = AttributeValue.builder()
            .s("The East")
            .build();

        parameters.add(att1);
        parameters.add(att2);

        try {
            executeStatementRequest(ddb, sqlStatement, parameters);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Item was updated!");
    }

    // Query the table where the year is 2013.
    public static void queryTable(DynamoDbClient ddb) {
        String sqlStatement = "SELECT * FROM MoviesPartiQ where year = ? ORDER BY year";
        try {

            List<AttributeValue> parameters = new ArrayList<>();
            AttributeValue att1 = AttributeValue.builder()
                .n(String.valueOf("2013"))
                .build();
            parameters.add(att1);

            // Get items in the table and write out the ID value.
            ExecuteStatementResponse response = executeStatementRequest(ddb, sqlStatement, parameters);
            System.out.println("ExecuteStatement successful: "+ response.toString());

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void deleteDynamoDBTable(DynamoDbClient ddb, String tableName) {

        DeleteTableRequest request = DeleteTableRequest.builder()
            .tableName(tableName)
            .build();

        try {
            ddb.deleteTable(request);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println(tableName +" was successfully deleted!");
    }

    private static ExecuteStatementResponse executeStatementRequest(DynamoDbClient ddb, String statement, List<AttributeValue> parameters ) {
        ExecuteStatementRequest request = ExecuteStatementRequest.builder()
            .statement(statement)
            .parameters(parameters)
            .build();

        return ddb.executeStatement(request);
    }

    private static void processResults(ExecuteStatementResponse executeStatementResult) {
        System.out.println("ExecuteStatement successful: "+ executeStatementResult.toString());
    }
}
```
+  For API details, see [ExecuteStatement](https://docs.aws.amazon.com/goto/SdkForJavaV2/dynamodb-2012-08-10/ExecuteStatement) in *AWS SDK for Java 2\.x API Reference*\. 