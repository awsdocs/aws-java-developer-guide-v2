# Best practices for AWS SDK for Java 2\.x<a name="best-practices"></a>

This section lists best practices for using the SDK for Java 2\.x\.

**Topics**
+ [Reuse an SDK client, if possible](#bestpractice1)
+ [Close input streams from client operations](#bestpractice2)
+ [Tune HTTP configurations based on performance tests](#bestpractice3)
+ [Use OpenSSL for the Netty\-based HTTP client](#bestpractice4)
+ [Configure API timeouts](#bestpractice5)
+ [Use metrics](#bestpractice6)

## Reuse an SDK client, if possible<a name="bestpractice1"></a>

Each SDK client maintains its own HTTP connection pool\. A connection that already exists in the pool can be reused by a new request to cut down the time to establish a new connection\. We recommend sharing a single instance of the client to avoid the overhead of having too many connection pools that aren't used effectively\. All SDK clients are thread safe\.

If you don't want to share a client instance, call `close()` on the instance to release the resources when the client is not needed\.

## Close input streams from client operations<a name="bestpractice2"></a>

For streaming operations such as `[S3Client\#getObject](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/S3Client.html#getObject(java.util.function.Consumer,java.nio.file.Path))`, if you are working with `[ResponseInputStream](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/ResponseInputStream.html)` directly, we recommend that you do the following:
+ Read all the data from the input stream as soon as possible\.
+ Close the input stream as soon as possible\.

We make these recommendations because the input stream is a direct stream of data from the HTTP connection and the underlying HTTP connection can't be reused until all data from the stream has been read and the stream is closed\. If these rules are not followed, the client can run out of resources by allocating too many open, but unused, HTTP connections\.

## Tune HTTP configurations based on performance tests<a name="bestpractice3"></a>

The SDK provides a set of [default http configurations](https://github.com/aws/aws-sdk-java-v2/blob/master/http-client-spi/src/main/java/software/amazon/awssdk/http/SdkHttpConfigurationOption.java) that apply to general use cases\. We recommend that customers tune HTTP configurations for their applications based on their use cases\. 

As a good starting point, the SDK offers a [smart configuration defaults](http-configuration.md#http-config-smart-defaults) feature\. This feature is available starting with version 2\.17\.102\. You choose a mode depending on your use case, which provides sensible configuration values\. 

## Use OpenSSL for the Netty\-based HTTP client<a name="bestpractice4"></a>

By default, the SDK's [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/nio/netty/NettyNioAsyncHttpClient.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/nio/netty/NettyNioAsyncHttpClient.html) uses the JDK's default SSL implementation as the `SslProvider`\. Our testing found that OpenSSL performs better than JDK's default implementation\. The Netty community also [recommends using OpenSSL](https://netty.io/wiki/requirements-for-4.x.html#tls-with-openssl)\. 

To use OpenSSL, add `netty-tcnative` to your dependencies\. For configuration details, see the [Netty project documentation](https://netty.io/wiki/forked-tomcat-native.html)\.

After you have `netty-tcnative` configured for your project, the `NettyNioAsyncHttpClient` instance will automatically select OpenSSL\. Alternatively, you can set the `SslProvider` explicitly using the `NettyNioAsyncHttpClient` builder as shown in the following snippet\.

```
NettyNioAsyncHttpClient.builder()
                        .sslProvider(SslProvider.OPENSSL)
                        .build();
```

## Configure API timeouts<a name="bestpractice5"></a>

The SDK provides [default values](https://github.com/aws/aws-sdk-java-v2/blob/a0c8a0af1fa572b16b5bd78f310594d642324156/http-client-spi/src/main/java/software/amazon/awssdk/http/SdkHttpConfigurationOption.java#L134) for some timeout options, such as connection timeout and socket timeouts, but not for API call timeouts or individual API call attempt timeouts\. It is a good practice to set timeouts for both the individual attempts and the entire request\. This will ensure your application fails fast in an optimal way when there are transient issues that could cause request attempts to take longer to complete or fatal network issues\.

You can configure timeouts for all requests made by a service clients using `[ClientOverrideConfiguration\#apiCallAttemptTimeout](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/client/config/ClientOverrideConfiguration.html#apiCallAttemptTimeout())` and `[ClientOverrideConfiguration\#apiCallTimeout](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/client/config/ClientOverrideConfiguration.html#apiCallTimeout())`\.

The following example shows the configuration of an Amazon S3 client with custom timeout values\.

```
S3Client.builder()
        .overrideConfiguration(
             b -> b.apiCallTimeout(Duration.ofSeconds(<custom value>))
                   .apiCallAttemptTimeout(Duration.ofMillis(<custom value>))
        .build();
```

**`apiCallAttemptTimeout`**  
This setting sets the amount of time for a single HTTP attempt, after which the API call can be retried\.

**`apiCallTimeout`**  
The value for this property configures the amount of time for the entire execution, including all retry attempts\.

As an alternative to setting these timeout values on the service client, you can use [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/RequestOverrideConfiguration.html#apiCallTimeout()](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/RequestOverrideConfiguration.html#apiCallTimeout()) and `[RequestOverrideConfiguration\#apiCallAttemptTimeout\(\)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/RequestOverrideConfiguration.html#apiCallAttemptTimeout())` to configure a single request \.

The following example configures a single `listBuckets` request with custom timeout values\.

```
s3Client.listBuckets(lbr -> lbr.overrideConfiguration(
        b -> b.apiCallTimeout(Duration.ofSeconds(<custom value>))
               .apiCallAttemptTimeout(Duration.ofMillis(<custom value>))));
```

When you use these properties together, you set a hard limit on the total time spent on all attempts across retries\. You also set an individual HTTP request to fail fast on a slow request\.

## Use metrics<a name="bestpractice6"></a>

The SDK for Java can [collect metrics](metrics.md) for the service clients in your application\. You can use these metrics to identify performance issues, review overall usage trends, review service client exceptions returned, or to dig in to understand a particular issue\.

We recommend that you collect metrics, then analyze the Amazon CloudWatch Logs, in order to gain a deeper understanding of your application's performance\.