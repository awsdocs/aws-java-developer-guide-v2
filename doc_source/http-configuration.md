# HTTP clients<a name="http-configuration"></a>

You can change the default configuration for HTTP clients in applications you build with the AWS SDK for Java 2\.x\. This section discusses HTTP clients and settings for the SDK\.

## Clients available in the SDK for Java<a name="http-clients-available"></a>

### Synchronous clients<a name="http-config-sync"></a>

A synchronous service client, such as the [S3Client](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/S3Client.html) or the [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/DynamoDbClient.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/DynamoDbClient.html), requires the use of a synchronous HTTP client\. The AWS SDK for Java offers two synchronous HTTP clients\.

**ApacheHttpClient \(default\)**  
[ApacheHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/apache/ApacheHttpClient.html) is the default HTTP client for synchronous service clients\. For information about configuring the `ApacheHttpClient`, see [Configuring the Apache\-based HTTP client](http-configuration-apache.md)\. 

**UrlConnectionHttpClient**  
As a lighter weight option to the `ApacheHttpClient`, you can use the [UrlConnectionHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/urlconnection/UrlConnectionHttpClient.html)\. For information about configuring the `UrlConnectionHttpClient`, see [Configuring the URLConnection\-based HTTP client](http-configuration-url.md)\.

### Asynchronous clients<a name="http-config-async"></a>

An asynchronous service client, such as the [S3AsyncClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/S3AsyncClient.html) or the [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/DynamoDbAsyncClient.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/DynamoDbAsyncClient.html), requires the use of an asynchronous HTTP client\. The AWS SDK for Java offers two asynchronous HTTP clients\.

**NettyNioAsyncHttpClient \(default\)**  
[NettyNioAsyncHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/nio/netty/NettyNioAsyncHttpClient.html) is the default HTTP client used by asynchronous clients\. For information about configuring the `NettyNioAsyncHttpClient`, see [Configuring the Netty\-based HTTP client](http-configuration-netty.md)\.

**AwsCrtAsyncHttpClient**  
The new [AwsCrtAsyncHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/crt/AwsCrtAsyncHttpClient.html), which also has a quicker loading time compared to the `NettyNioAsyncHttpClient`, is also available\. For information about configuring the `AwsCrtAsyncHttpClient`, see [Configuring the AWS CRT\-based HTTP client](http-configuration-crt.md)\. 

## HTTP client recommendations<a name="http-clients-recommend"></a>

Several factors come into play when you choose an HTTP client implemenation\. Use the following information to help you decide\.

### Recommendation flowchart<a name="http-clients-recommend-flowchart"></a>

The following flowchart provides general guidance to help you determine which HTTP client to use\.

![\[Flowchart of HTTP client recommendations.\]](http://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/images/JavaDevGuide-HTTPflowchart-DI.png)

### HTTP client comparison<a name="http-clients-recommend-compare"></a>

The following table provides detailed information for each HTTP client\. 


| HTTP client | Sync or async | When to use | Limitation/drawback | 
| --- | --- | --- | --- | 
|  Apache\-based HTTP client *\(default sync HTTP client\)*  | Sync | Use it if you prefer low latency over high throughput  | Slower startup time compared to other HTTP clients | 
| URLConnection\-based HTTP client | Sync | Use it if you have a hard requirement for limiting third\-party dependencies | Does not support the HTTP PATCH method, required by some APIS like Amazon APIGateway Update operations | 
|  Netty\-based HTTP client *\(default async HTTP client\)*  | Async |  • Use it if your application invokes APIs that require HTTP/2 support such as Kinesis API [SubscribeToShard](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_SubscribeToShard.html) • Use it if you prefer high throughput over low latency AND your OS uses[ musl ](https://en.wikipedia.org/wiki/Musl) such as Alpine Linux  | Slower startup time compared to other HTTP clients | 
|  AWS CRT\-based HTTP client1  | Async |  • Use it if your application is running in AWS Lambda • Use it if you prefer high throughput over low latency  |  • Does not support service clients that require HTTP/2 support such as `KinesisAsynClient` and `TranscribeStreamingAsyncClient` •Does not support musl\-based OS such as Alpine Linux  | 

1We recommend that you use the AWS CRT\-based HTTP client if possible, because of its added benefits\.

## Smart configuration defaults<a name="http-config-smart-defaults"></a>

The AWS SDK for Java 2\.x \(version 2\.17\.102 or later\) offers a smart configuration defaults feature\. This feature optimizes two HTTP client properties along with other properties that don't affect the HTTP client\. 

The smart configuration defaults set sensible values for the `connectTimeoutInMillis` and `tlsNegotiationTimeoutInMillis` properties based on a defaults mode value that you provide\. You choose the defaults mode value based on your application's characteristics\. 

For more information about smart configuration defaults and how to choose the defaults mode value that is best suited for your applications, see the [AWS SDKs and Tools Reference Guide](https://docs.aws.amazon.com/sdkref/latest/guide/feature-smart-config-defaults.html)\.

Following are four ways to set the defaults mode for your application\.

------
#### [ Service client ]

Use the service client builder to configure the defaults mode directly on the service client\. The following example sets the defaults mode to `auto` for the `DynamoDbClient`\.

```
DynamoDbClient ddbClient = DynamoDbClient.builder()
                            .defaultsMode(DefaultsMode.AUTO)
                            .build();
```

------
#### [ System property ]

You can use the `aws.defaultsMode` system property to specify the defaults mode\. If you set the system property in Java, you need to set the property before initializing any service client\.

The following example shows you how to set the defaults mode to `auto` using a system property set in Java\.

```
System.setProperty("aws.defaultsMode", "auto");
```

The following example demonstrates how you set the defaults mode to `auto` using a `-D` option of the `java` command\.

```
java -Daws.defaultsMode=auto
```

------
#### [ Environment variable ]

Set a value for environment variable `AWS_DEFAULTS_MODE` to select the defaults mode for your application\. 

The following information shows the command to run to set the value for the defaults mode to `auto` using an environment variable\.


| Operating system | Command to set environment variables | 
| --- | --- | 
|  Linux, macOS, or Unix  | export AWS\_DEFAULTS\_MODE=auto | 
|  Windows  | set AWS\_DEFAULTS\_MODE=auto | 

------
#### [ AWS config file ]

You can add a `defaults_mode` configuration property to the shared AWS `config` file as the following example shows\.

```
[default]
defaults_mode = auto
```

------