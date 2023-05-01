# Work with multiple items<a name="ddb-en-client-use-multirecord"></a>

The `scan`, `query` and `batch` methods of the DynamoDB Enhanced Client API return responses with one or more *pages*\. A page contains one or more items\. Your code can process the response on per\-page basis or it can process individual items\.

A paginated response returned by the synchronous `DynamoDbEnhancedClient` client returns a [PageIterable](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/PageIterable.html) object, whereas a response returned by the asynchronous `DynamoDbEnhancedAsyncClient` returns a [PagePublisher](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/PagePublisher.html) object\.

This section looks at processing paginated results and provides examples that use the scan and query APIs\.

**Topics**
+ [Scan a table](ddb-en-client-use-multirecord-scan.md)
+ [Query a table](ddb-en-client-use-multirecord-query.md)