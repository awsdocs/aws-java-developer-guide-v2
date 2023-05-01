# DynamoDB Enhanced Client API in the AWS SDK for Java 2\.x<a name="dynamodb-enhanced-client"></a>

The [DynamoDB Enhanced Client API](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/package-summary.html) is a high\-level library that is the successor to the `DynamoDBMapper` class of in the SDK for Java v1\.x\. It offers a straightforward way to map client\-side classes to DynamoDB tables\. You define the relationships between tables and their corresponding model classes in your code\. After you define those relationships, you can intuitively perform various create, read, update, or delete \(CRUD\) operations on tables or items in DynamoDB\.

The DynamoDB Enhanced Client API also includes the [Enhanced Document API](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/document/package-summary.html) that enables you to work with document\-type items that do not follow a defined schema\. 

**Topics**
+ [Get Started using the DynamoDB Enhanced Client API](ddb-en-client-getting-started.md)
+ [Basics of the DynamoDB Enhanced Client API](ddb-en-client-use.md)
+ [Extensions](ddb-en-client-extensions.md)
+ [Advanced table schema features](ddb-en-client-adv-features.md)
+ [Enhanced Document API for DynamoDB](ddb-en-client-doc-api.md)
+ [Non\-blocking asynchronous operations](ddb-en-client-async.md)