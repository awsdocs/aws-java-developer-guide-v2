# Exception handling for the AWS SDK for Java 2\.x<a name="handling-exceptions"></a>

Understanding how and when the AWS SDK for Java 2\.x throws exceptions is important to building high\-quality applications using the SDK\. The following sections describe the different cases of exceptions that are thrown by the SDK and how to handle them appropriately\.

## Why unchecked exceptions?<a name="why-unchecked-exceptions"></a>

The AWS SDK for Java uses runtime \(or unchecked\) exceptions instead of checked exceptions for these reasons:
+ To allow developers fine\-grained control over the errors they want to handle without forcing them to handle exceptional cases they aren’t concerned about \(and making their code overly verbose\)
+ To prevent scalability issues inherent with checked exceptions in large applications

In general, checked exceptions work well on small scales, but can become troublesome as applications grow and become more complex\.

## AwsServiceException \(and subclasses\)<a name="sdkserviceexception-and-subclasses"></a>

 [AwsServiceException](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/awscore/exception/AwsServiceException.html) is the most common exception that you’ll experience when using the AWS SDK for Java\. `AwsServiceException` is a subclass of the more general [SdkServiceException](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/exception/SdkServiceException.html)\. `AwsServiceException`s represent an error response from an AWS service\. For example, if you try to terminate an Amazon EC2 instance that doesn’t exist, Amazon EC2 will return an error response and all the details of that error response will be included in the `AwsServiceException` that’s thrown\. 

When you encounter an `AwsServiceException`, you know that your request was successfully sent to the AWS service but couldn’t be successfully processed\. This can be because of errors in the request’s parameters or because of issues on the service side\.

 `AwsServiceException` provides you with information such as:
+ Returned HTTP status code
+ Returned AWS error code
+ Detailed error message from the service in the [AwsErrorDetails](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/awscore/exception/AwsErrorDetails.html) class
+  AWS request ID for the failed request

In most cases, a service\-specific subclass of `AwsServiceException` is thrown to allow developers fine\-grained control over handling error cases through catch blocks\. The Java SDK API reference for [AwsServiceException](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/awscore/exception/AwsServiceException.html) displays the large number of `AwsServiceException` subclasses\. Use the subclass links to drill down to see the granular exceptions thrown by a service\.

For example, the following links to the SDK API reference show the exception hierarchies for a few common AWS services\. The list of subclasses shown on each pages shows the specific exceptions that your code can catch\.
+ [Amazon S3](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/model/S3Exception.html)
+ [DynamoDB](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/model/DynamoDbException.html)
+ [Amazon SQS](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/sqs/model/SqsException.html)

To learn more about an exception, inspect the `errorCode` on the [AwsErrorDetails](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/awscore/exception/AwsErrorDetails.html) object\. You can use the `errorCode` value to look up information in the service guide API\. For example if an `S3Exception` is caught and the `AwsErrorDetails#errorCode()` value is `InvalidRequest`, use the [list of error codes](https://docs.aws.amazon.com/AmazonS3/latest/API/ErrorResponses.html#ErrorCodeList) in the Amazon S3 API Reference to see more details\.

## SdkClientException<a name="sdkclientexception"></a>

 [SdkClientException](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/exception/SdkClientException.html) indicates that a problem occurred inside the Java client code, either while trying to send a request to AWS or while trying to parse a response from AWS\. An `SdkClientException` is generally more severe than an `SdkServiceException`, and indicates a major problem that is preventing the client from making service calls to AWS services\. For example, the AWS SDK for Java throws an `SdkClientException` if no network connection is available when you try to call an operation on one of the clients\.