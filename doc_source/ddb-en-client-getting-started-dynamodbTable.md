# Create an enhanced client and `DynamoDbTable`<a name="ddb-en-client-getting-started-dynamodbTable"></a>

## Create an enhanced client<a name="ddb-en-client-getting-started-dynamodbTable-eclient"></a>

The [DynamoDbEnhancedClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbEnhancedClient.html) class or its asynchronous counterpart, [DynamoDbEnhancedAsyncClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbEnhancedAsyncClient.html), is the entry point to working with the DynamoDB Enhanced Client API\.

The enhanced client requires a standard `[DynamoDbClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/DynamoDbClient.html)` to perform work\. The API offers two ways to create a `DynamoDbEnhancedClient` instance\. The first, shown in the following snippet, creates a standard `DynamoDbClient` under the hood with default settings picked up from configuration settings\.

```
DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.create();
```

If you want to configure the underlying standard client, you can supply it to the enhanced client's builder method as shown in the following snippet\.

```
DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(
                            // Configure an instance of the standard client.
                            DynamoDbClient.builder()
                                    .region(Region.US_EAST_1)
                                    .credentialsProvider(ProfileCredentialsProvider.create())
                                    .build())
                    .build();
```

## Create a `DynamoDbTable` instance<a name="ddb-en-client-getting-started-dynamodbTable-table"></a>

You can think of a [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html) as the client\-side representation of a DynamoDB table that uses the mapping functionality provided by a `TableSchema`\. The `DyanamoDbTable` class provides methods for CRUD operations that let you interact with a single DynamoDB table\.

`DynamoDbTable<T>` is a generic class that takes a single type argument, whether it is a custom class or an `EnhancedDocument` when working with document\-type items\. This argument type establishes the relationship between the class that you use and the single DynamoDB table\.

Use the `table()` factory method of the `DynamoDbEnhancedClient` to create a `DynamoDbTable` instance as shown in the following snippet\.

```
static final DynamoDbTable<Customer> customerTable = 
        enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));
```

`DynamoDbTable` instances are candidates for singletons because they are immutable and can be used throughout your application\.

Your code now has an in\-memory representation of a DynamoDB table that can store `Customer` instances\. The actual DynamoDB table may or may not exist\. If the table named "Customer" already exists, you can begin performing CRUD operations against it\. If it doesn't exist, use the `DynamoDbTable` instance to create the table as discussed in the next section\.