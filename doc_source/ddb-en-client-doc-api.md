# Enhanced Document API for DynamoDB<a name="ddb-en-client-doc-api"></a>

The [Enhanced Document API](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/document/package-summary.html) for AWS SDK for Java 2\.x is designed to work with document\-oriented data that has no fixed schema, but also lets you use custom classes to map individual attributes\.

 The Enhanced Document API is the successor to the [Document API](https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/dynamodbv2/document/DynamoDB.html) of the AWS SDK for Java v1\.x\.

**Contents**
+ [Preliminary steps to work with the Enhanced Document API](ddb-en-client-doc-api-steps.md)
  + [Create a `DocumentTableSchema` and a `DynamoDbTable`](ddb-en-client-doc-api-steps.md#ddb-en-client-doc-api-steps-createschema)
+ [Build enhanced documents](ddb-en-client-doc-api-steps-create-ed.md)
  + [Build from a JSON string](ddb-en-client-doc-api-steps-create-ed.md#ddb-en-client-doc-api-steps-create-ed-fromJson)
  + [Build from individual elements](ddb-en-client-doc-api-steps-create-ed.md#ddb-en-client-doc-api-steps-create-ed-fromparts)
+ [Perform CRUD operations](ddb-en-client-doc-api-steps-use.md)
+ [Access enhanced document attributes as custom objects](ddb-en-client-doc-api-convert.md)