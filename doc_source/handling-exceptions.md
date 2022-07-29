--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Exception handling for the AWS SDK for Java<a name="handling-exceptions"></a>

Understanding how and when the AWS SDK for Java throws exceptions is important to building high\-quality applications using the SDK\. The following sections describe the different cases of exceptions that are thrown by the SDK and how to handle them appropriately\.

## Why unchecked exceptions?<a name="why-unchecked-exceptions"></a>

The AWS SDK for Java uses runtime \(or unchecked\) exceptions instead of checked exceptions for these reasons:
+ To allow developers fine\-grained control over the errors they want to handle without forcing them to handle exceptional cases they aren’t concerned about \(and making their code overly verbose\)
+ To prevent scalability issues inherent with checked exceptions in large applications

In general, checked exceptions work well on small scales, but can become troublesome as applications grow and become more complex\.

## SdkServiceException \(and subclasses\)<a name="sdkserviceexception-and-subclasses"></a>

 [SdkServiceException](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/exception/SdkServiceException.html) is the most common exception that you’ll experience when using the AWS SDK for Java\. This exception represents an error response from an AWS service\. For example, if you try to terminate an Amazon EC2 instance that doesn’t exist, Amazon EC2 will return an error response and all the details of that error response will be included in the `SdkServiceException` that’s thrown\. For some cases, a subclass of `SdkServiceException` is thrown to allow developers fine\-grained control over handling error cases through catch blocks\.

When you encounter an `SdkServiceException`, you know that your request was successfully sent to the AWS service but couldn’t be successfully processed\. This can be because of errors in the request’s parameters or because of issues on the service side\.

 `SdkServiceException` provides you with information such as:
+ Returned HTTP status code
+ Returned AWS error code
+ Detailed error message from the service
+  AWS request ID for the failed request

## SdkClientException<a name="sdkclientexception"></a>

 [SdkClientException](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/exception/SdkClientException.html) indicates that a problem occurred inside the Java client code, either while trying to send a request to AWS or while trying to parse a response from AWS\. An `SdkClientException` is generally more severe than an `SdkServiceException`, and indicates a major problem that is preventing the client from making service calls to AWS services\. For example, the AWS SDK for Java throws an `SdkClientException` if no network connection is available when you try to call an operation on one of the clients\.