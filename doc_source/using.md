--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Using the AWS SDK for Java 2\.x<a name="using"></a>

After completing the steps in [Setting up the SDK](setup.md), you are ready to make requests to AWS services such as Amazon S3, DynamoDB, IAM, Amazon EC2, and more\.

## Creating service clients<a name="creating-clients"></a>

To make a request to an AWS service, you must first instantiate an object to serve as a client for that service using the static factory method `builder`\. Then customize it by using the setters in the builder\. The fluent setter methods return the `builder` object, so that you can chain the method calls for convenience and for more readable code\. After you configure the properties you want, you can call the `build` method to create the client\.

As an example, this code snippet instantiates an `Ec2Client` object as a service client for Amazon EC2:

```
Region region = Region.US_WEST_2;
Ec2Client ec2Client = Ec2Client.builder()
        .region(region)
        .build();
```

**Note**  
Service clients in the SDK are thread\-safe\. For best performance, treat them as long\-lived objects\. Each client has its own connection pool resource that is released when the client is garbage collected\.  
A service client object is immutable, so you must create a new client for each service to which you make requests, or if you want to use a different configuration for making requests to the same service\.  
Specifying the `Region` in the service client builder is not required for all AWS services; however, it is a best practice to set the Region for the API calls you make in your applications\. See [AWS region selection](region-selection.md) for more information\.

### Using the default client<a name="using-default-client"></a>

The client builders have another factory method named `create`\. This method creates a service client with the default configuration\. It uses the default provider chain to load credentials and the AWS Region\. If credentials or the region can’t be determined from the environment that the application is running in, the call to `create` fails\. See [Using credentials](credentials.md) and [Region selection](region-selection.md) for more information about how credentials and region are determined\.

As an example, this code snippet instantiates a `DynamoDbClient` object as a service client for Amazon DynamoDB:

```
DynamoDbClient dynamoDbClient = DynamoDbClient.create();
```

### Making requests<a name="using-making-requests"></a>

You use the service client to make requests to that AWS service\.

For example, this code snippet shows how to create a `RunInstancesRequest` object to create a new Amazon EC2 instance:

```
RunInstancesRequest runInstancesRequest = RunInstancesRequest.builder()
        .imageId(amiId)
        .instanceType(InstanceType.T1_MICRO)
        .maxCount(1)
        .minCount(1)
        .build();

ec2Client.runInstances(runInstancesRequest);
```

### Handling responses<a name="using-handling-responses"></a>

You use a response handler to process the response back from the AWS service\.

For example, this code snippet shows how to create a `RunInstancesResponse` object to handle the response from Amazon EC2 by printing out the `instanceId` for the new instance from the request above:

```
RunInstancesResponse runInstancesResponse = ec2Client.runInstances(runInstancesRequest);
System.out.println(runInstancesResponse.instances().get(0).instanceId());
```

### Closing the client<a name="using-closing-client"></a>

When you no longer need the service client, close it\.

```
ec2Client.close();
```

**Note**  
Service clients extend the `AutoClosable` interface, but as a best practice \- especially with short\-lived code such as AWS Lambda functions \- you should explicitly call the `close()` method\.

## Handling exceptions<a name="using-handling-exceptions"></a>

The SDK uses runtime \(or unchecked\) exceptions, providing you fine\-grained control over error handling and ensuring that exception handling will scale with your application\.

An [http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/exception/SdkServiceException.html](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/exception/SdkServiceException.html), or one of its sub\-classes, is the most common form of exception the SDK will throw\. These exceptions represent responses from the AWS service\. You can also handle an [http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/exception/SdkClientException.html](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/exception/SdkClientException.html), which occurs when there’s a problem on the client side \(i\.e\., in your development or application environment\), such a network connection failure\.

This code snippet demonstrates one way to handle service exceptions while uploading a file to Amazon S3\.

```
Region region = Region.US_WEST_2;
s3Client = S3Client.builder()
        .region(region)
        .build();

try {

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

    s3Client.putObject(putObjectRequest, RequestBody.fromString("SDK for Java test"));

} catch (S3Exception e) {
    System.err.println(e.awsErrorDetails().errorMessage());
    System.exit(1);
}
```

See [Handling exceptions](handling-exceptions.md) for more information\.

## Using waiters<a name="using-use-waiters"></a>

Some requests take time to process, such as creating a new table in DynamoDB or creating a new Amazon S3 bucket\. To ensure the resource is ready before your code continues to run, use a *Waiter*\.

For example, this code snippet creates a new table \("myTable"\) in DynamoDB, waits for the table to be in an `ACTIVE` status, and then prints out the response:

```
DynamoDbClient dynamoDbClient = DynamoDbClient.create();
DynamoDbWaiter dynamoDbWaiter = dynamoDbClient.waiter();

WaiterResponse<DescribeTableResponse> waiterResponse =
   dynamoDbWaiter.waitUntilTableExists(r -> r.tableName("myTable"));

waiterResponse.matched().response().ifPresent(System.out::println);
```

See [Using waiters](waiters.md) for more information\.

## Configuring service clients<a name="using-configure-service-clients"></a>

To customize the configuration of a service client, use the setters on the factory method `builder`\. For convenience and to create more readable code, you chain the methods to set multiple configuration options\.

As an example, refere to the following code snippet\.

```
ClientOverrideConfiguration clientOverrideConfiguration =
    ClientOverrideConfiguration.builder()
                    .apiCallAttemptTimeout(Duration.ofSeconds(1))
                    .retryPolicy(RetryPolicy.builder().numRetries(10).build())
                    .addMetricPublisher(CloudWatchMetricPublisher.create())
                    .build();

Region region = Region.US_WEST_2;
S3Client s3Client = S3Client.builder()
                    .region(region)
                    .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                    .overrideConfiguration(clientOverrideConfiguration)
                    .httpClientBuilder(ApacheHttpClient.builder()
                        .proxyConfiguration(proxyConfig.build(ProxyConfiguration.builder()))
                        .build())
                    .build();
```

## HTTP clients<a name="using-http-clients"></a>

You can change the default configuration for HTTP clients in applications you build with the AWS SDK for Java\. For information on how to configure HTTP clients and settings, see [HTTP configuration](http-configuration.md)\.

## Retries<a name="using-retries"></a>

You can change the default settings for retries in your service clients, including the retry mode and back\-off strategy\. For more information, refer to [the `RetryPolicy` class](https://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/retry/RetryPolicy.html) in the AWS SDK for Java API Reference\.

For more information about retries in AWS services, see [Error retries and exponential backoff in AWS](https://docs.aws.amazon.com/general/latest/gr/api-retries.html)\.

## Timeouts<a name="using-timeouts"></a>

You can configure timeouts for each of your service clients using the `apiCallTimeout` and the `apiCallAttemptTimeout` setters\. The `apiCallTimeout` setting is the amount of time to allow the client to complete the execution of an API call\. The `apiCallAttemptTimeout` setting is the amount of time to wait for the HTTP request to complete before giving up\.

For more information, see [http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/client/config/ClientOverrideConfiguration.Builder.html#apiCallTimeout-java.time.Duration-](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/client/config/ClientOverrideConfiguration.Builder.html#apiCallTimeout-java.time.Duration-) and [http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/client/config/ClientOverrideConfiguration.Builder.html#apiCallAttemptTimeout-java.time.Duration-](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/client/config/ClientOverrideConfiguration.Builder.html#apiCallAttemptTimeout-java.time.Duration-) in the AWS SDK for Java API Reference\.

## Execution interceptors<a name="using-execution-interceptors"></a>

You can write code that intercepts the execution of your API requests and responses at different parts of the request/response lifecycle\. This enables you to publish metrics, modify a request in\-flight, debug request processing, view exceptions, and more\. For more information, see [the `ExecutionInterceptor` interface](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/interceptor/ExecutionInterceptor.html) in the AWS SDK for Java API Reference\.

## Additional information<a name="using-addl-info"></a>
+ For complete examples of the code snippets above, see [Working with Amazon DynamoDB](examples-dynamodb.md), [Working with Amazon EC2](examples-ec2.md), and [Working with Amazon S3](examples-s3.md)\.