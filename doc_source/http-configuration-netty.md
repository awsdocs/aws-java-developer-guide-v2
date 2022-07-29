--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Configuring the Netty\-based HTTP client<a name="http-configuration-netty"></a>

For asynchronous operations in the AWS SDK for Java 2\.x, you can use Netty \([NettyNioAsyncHttpClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/http/nio/netty/NettyNioAsyncHttpClient.html)\) as the HTTP client or you can use the new AWS Common Runtime \(CRT\) HTTP client [AwsCrtAsyncHttpClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/http/crt/AwsCrtAsyncHttpClient.html)\. This topics shows you how to configure the Netty\-based HTTP client\.

For a full list of options you can set with these clients, see the [AWS SDK for Java API Reference version 2\.x](http://docs.aws.amazon.com/sdk-for-java/latest/reference/)\.

## Prerequisite<a name="prerequisite"></a>

Before you can use use the Netty client, you need to configure your project dependencies in your `pom.xml` or `build.gradle` file to include version `2.0.0` or later of the `artifactId``netty-nio-client`\.

The following code example shows how to configure your project dependencies\.

```
<dependency>
  <artifactId>netty-nio-client</artifactId>
  <groupId>software.amazon.awssdk</groupId>
  <version>2.0.0</version>
</dependency>
```

## Configuring service clients<a name="configuring-service-clients"></a>

Use the HTTP client builder to have the SDK manage its lifecycle\. The HTTP client will be closed for you when the service client is shut down\.

 **Imports** 

```
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.services.kinesis.AmazonKinesisAsyncClient;
```

 **Code** 

```
AmazonKinesisAsyncClient client = AmazonKinesisAsyncClient.builder()
    .httpClientBuilder(NettyNioAsyncHttpClient.builder()
        .maxConcurrency(100)
        .maxPendingConnectionAcquires(10_000))
    .build();
```

You can also pass the HTTP client directly to the service client if you want to manage the lifecycle yourself\.

 **Code** 

```
SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
    .maxConcurrency(100)
    .maxPendingConnectionAcquires(10_000)
    .build();

AmazonKinesisAsyncClient kinesisClient = AmazonKinesisAsyncClient.builder()
    .httpClient(httpClient)
    .build();

httpClient.close();
```