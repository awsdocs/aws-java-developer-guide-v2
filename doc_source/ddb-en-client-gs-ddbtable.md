# Create a DynamoDB table if needed<a name="ddb-en-client-gs-ddbtable"></a>

After you have created a `DynamoDbTable` instance, use it to perform a one\-time creation of a table in DynamoDB\.

## Create table example code<a name="ddb-en-client-gs-ddbtable-createex"></a>

The following example creates a DynamoDB table based on the `Customer` data class\. 

This example creates a DynamoDB table with the name `Customer`—identical to the class name—but the table name can be something else\. Whatever you name the table, you must use this name in additional applications to work with the table\. Supply this name to the `table()` method anytime you create another `DynamoDbTable` object in order to work with the underlying DynamoDB table\.

The Java lambda parameter, `builder`, passed to the `createTable` method lets you [customize the table](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/CreateTableEnhancedRequest.Builder.html)\. In this example, [provisioned throughput](http://amazonaws.com/amazondynamodb/latest/APIReference/API_ProvisionedThroughput.html) is configured, but you can also configure any secondary indexes with the builder methods\. 

The example also uses a `[DynamoDbWaiter](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/waiters/DynamoDbWaiter.html)` before attempting to print out the table name received in the response\. The creation of a table takes some time\. Therefore, using a waiter means you don't have to write logic that polls the DynamoDB service to see if the table exists before using the table\.

### Imports<a name="ddb-en-client-gs-ddbtable-imports"></a>

```
import com.example.dynamodb.Customer;
import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;
```

### Code<a name="ddb-en-client-gs-ddbtable-code"></a>

```
public static void createCustomerTable(DynamoDbTable<Customer> customerDynamoDbTable, DynamoDbClient dynamoDbClient) {
    // Create the DynamoDB table by using the 'customerDynamoDbTable' DynamoDbTable instance.
    customerDynamoDbTable.createTable(builder -> builder
            .provisionedThroughput(b -> b
                    .readCapacityUnits(10L)
                    .writeCapacityUnits(10L)
                    .build())
    );
    // The 'dynamoDbClient' instance that's passed to the builder for the DynamoDbWaiter is the same instance
    // that was passed to the builder of the DynamoDbEnhancedClient instance used to create the 'customerDynamoDbTable'.
    // This means that the same Region that was configured on the standard 'dynamoDbClient' instance is used for all service clients.
    try (DynamoDbWaiter waiter = DynamoDbWaiter.builder().client(dynamoDbClient).build()) { // DynamoDbWaiter is Autocloseable
        ResponseOrException<DescribeTableResponse> response = waiter
                .waitUntilTableExists(builder -> builder.tableName("Customer").build())
                .matched();
        DescribeTableResponse tableDescription = response.response().orElseThrow(
                () -> new RuntimeException("Customer table was not created."));
        // The actual error can be inspected in response.exception()
        logger.info("Customer table was created.");
    }
}
```

**Note**  
A DynamoDB table's attribute names begin with a lowercase letter when the table is generated from a data class\. If you want the table's attribute name to begin with an uppercase letter, use the [`@DynamoDbAttribute(NAME)` annotation](ddb-en-client-adv-features-inex-attr.md) and provide the name you want as a parameter\.