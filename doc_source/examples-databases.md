--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# AWS database services and AWS SDK for Java 2\.x<a name="examples-databases"></a>

AWS offers several database types: relational, key\-value, in\-memory, document, and [several others](http://aws.amazon.com/products/databases/)\. The SDK for Java 2\.x support varies depending the nature of the database service in AWS\.

Some database services, for example [Amazon DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/Welcome.html) service, have web service APIs to manage the AWS resource \(database\) as well as web service APIs to interact with the data\. In the SDK for Java 2\.x these types of services have dedicated service clients, for example [DynamoDBClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/DynamoDbClient.html)\.

Other database services have web service APIs that interact with the resource, such the [Amazon DocumentDB](https://docs.aws.amazon.com/documentdb/latest/developerguide/api-reference.html) API \(for cluster, instance and resource management\), but do not have a web service API for working with the data\. The SDK for Java 2\.x has a corresponding [DocDbClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/docdb/DocDbClient.html) interface for working with the resource\. However, you need another Java API, such as [MongoDB for Java](https://www.mongodb.com/developer/languages/java/) to work with the data\.

Use the examples below to learn how you use the SDK for Java 2\.x service clients with the different types of databases\.

## Amazon DynamoDB examples<a name="examples-db-dynamodb"></a>


| Working with the data | Working with the database | 
| --- |--- |
| SDK service client: [DynamoDBClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/DynamoDbClient.html) | SDK service client: [DynamoDBClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/DynamoDbClient.html) | 
| Example: [React/Spring REST application using DynamoDB](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/usecases/creating_dynamodb_web_app) | Examples: [CreateTable, ListTables, DeleteTable](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb) | 
| Examples: [Several DynamoDB examples](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb) |  | 
|  | 
| --- |
| SDK service client: [DynamoDBEnhancedClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbEnhancedClient.html) |  | 
| Example: [React/Spring REST application using DynamoDB](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/usecases/creating_dynamodb_web_app) |  | 
| Examples: [Several DynamoDB examples](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb) \(names starting with 'Enhanced"\) |  | 

See [additional DynamoDB examples](examples-dynamodb.md) in the guided code examples section of this guide\.

## Amazon RDS examples<a name="examples-db-rds"></a>


|  Working with the data  |  Working with the database  | 
| --- | --- | 
| Non\-SDK API: JDBC, database\-specific SQL flavor | SDK service client: [RdsClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/rds/RdsClient.html) | 
| Example: [React/Spring REST application using MySQL](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/usecases/Creating_rds_item_tracker) | Examples: [Several RdsClient examples](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/rds/src/main/java/com/example/rds) | 

## Amazon Redshift examples<a name="examples-db-redshift"></a>


|  Working with the data  |  Working with the database  | 
| --- | --- | 
| SDK service client: [RedshiftDataClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/redshiftdata/RedshiftDataClient.html) | SDK service client: [RedshiftClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/redshift/RedshiftClient.html) | 
| Examples: [Several RedshiftDataClient examples](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/redshift/src/main/java/com/example/redshiftdata) | Examples: [Several RedshiftClient examples](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/redshift/src/main/java/com/example/redshift) | 
| Example: [React/Spring REST application using RedshiftDataClient](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/usecases/CreatingSpringRedshiftRest) |  | 

## Amazon Aurora Serverless v1 examples<a name="examples-db-aurora-sv1"></a>


|  Working with the data  |  Working with the database  | 
| --- | --- | 
| SDK service client: [RdsDataClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/rdsdata/RdsDataClient.html) | SDK service client: [RdsClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/rds/RdsClient.html) | 
| Example: [React/Spring REST application using RdsDataClient](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/usecases/Creating_Spring_RDS_%20Rest) | Examples: [Several RdsClient examples](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/rds/src/main/java/com/example/rds) | 

## Amazon DocumentDB examples<a name="examples-db-docdb"></a>


|  Working with the data  |  Working with the database  | 
| --- | --- | 
| Non\-SDK API: MongoDB\-specific Java library \(for example [MongoDB for Java](https://www.mongodb.com/developer/languages/java/)\) | SDK service client: [DocDbClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/docdb/DocDbClient.html) | 
| Examples: [DocumentDB \(Mongo\) Developer Guide](https://docs.aws.amazon.com/documentdb/latest/developerguide/connect_programmatically.html#connect_programmatically-tls_enabled) \(select 'Java' tab\) |  | 