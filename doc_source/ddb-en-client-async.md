# Non\-blocking asynchronous operations<a name="ddb-en-client-async"></a>

If your application requires non\-blocking, asynchronous calls to DynamoDB, you can use the [DynamoDbEnhancedAsyncClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbEnhancedAsyncClient.html)\. It's similar to the synchronous implementation but with a few key differences:

1. When you build the DynamoDbEnhancedAsyncClient, you must use the asynchronous version of the standard client, `DynamoDbAsyncClient` as shown in the following snippet\.

   ```
    DynamoDbEnhancedAsyncClient enhancedClient = 
        DynamoDbEnhancedAsyncClient.builder()
                                   .dynamoDbClient(dynamoDbAsyncClient)
                                   .build();
   ```

1. Methods that return a single data object return a `CompletableFuture` of the result instead of only the result\. Your application can then do other work without having to block on the result\. The following snippet shows the asynchronous `getItem()` method\. 

   ```
   CompletableFuture<Customer> result = customerDynamoDbTable.getItem(customer);
   // Perform other work here.
   return result.join();   // now block and wait for the result
   ```

1. Methods that return paginated lists of results will return an `SdkPublisher` of the results instead of an `SdkIterable`\. Your application can then subscribe a handler to that publisher and deal with the results asynchronously without having to block:

   ```
   PagePublisher<Customer> results = customerDynamoDbTable.query(r -> r.queryConditional(keyEqualTo(k -> k.partitionValue("Smith"))));
   results.subscribe(myCustomerResultsProcessor);
   // Perform other work and let the processor handle the results asynchronously,
   ```

   For a more complete example of working with the `SdkPublisher API`, see [the example](ddb-en-client-use-multirecord-scan.md#ddb-en-client-use-multirecord-scan-async) in the section that discusses the asynchronous `scan()` method of this guide\.