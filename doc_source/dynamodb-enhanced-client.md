--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Using the DynamoDB Enhanced Client in the AWS SDK for Java 2\.x<a name="dynamodb-enhanced-client"></a>

The Amazon DynamoDB enhanced client is a high\-level library that is part of the AWS SDK for Java version 2 \(v2\)\. It offers a straightforward way to map client\-side classes to DynamoDB tables\. You define the relationships between tables and their corresponding model classes in your code\. Then you can intuitively perform various create, read, update, or delete \(CRUD\) operations on tables or items in DynamoDB\.

The AWS SDK for Java v2 includes a set of annotations that you can use with a Java bean to quickly generate a [TableSchema](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/TableSchema.html) for mapping your classes to tables\. Alternatively, if you declare each [TableSchema](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/enhanced/dynamodb/TableSchema.html) explicitly, you don’t need to include annotations in your classes\.

For information about how to use the DynamoDB Enhanced Client, refer to [Mapping items in DynamoDB tables](examples-dynamodb-enhanced.md)\.