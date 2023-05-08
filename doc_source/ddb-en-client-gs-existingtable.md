# Work with an existing table<a name="ddb-en-client-gs-existingtable"></a>

The previous section showed how to create a DynamoDB table starting with a Java data class\. If you already have an existing table and want to use the features of the enhanced client, you can create a Java data class to work with the table\. You need to examine the DynamoDB table and add the necessary annotations to the data class\. 

Before you work with an existing table, call the `DynamoDbEnhanced.table()` method\. This was done in the previous example with the following statement\.

```
DynamoDbTable<Customer> customerTable = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));
```

After the `DynamoDbTable` instance is returned, you can begin working right away with the underlying table\. You do not need to recreate the table by calling the `DynamoDbTable.createTable()` method\.

The following example demonstrates this by immediately retrieving a `Customer` instance from the DynamoDB table\.

```
DynamoDbTable<Customer> customerTable = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));
// The Customer table exists already and has an item with a primary key value of "a123".
Customer customer = customerTable.getItem(Key.builder().partitionValue("a123").build());
```

**Important**  
The table name used in the `table()` method must match the existing DynamoDB table name\.