--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Configuring the AWS CRT\-based HTTP client<a name="http-configuration-crt"></a>

The AWS Common Runtime \(CRT\) HTTP client is a new HTTP client you can use with the AWS SDK for Java 2\.x\. The CRT\-based HTTP client is an asynchronous, non\-blocking HTTP client built on top of the Java bindings of the [AWS Common Runtime](https://github.com/awslabs/aws-crt-java)\. You can use the CRT\-based HTTP client to benefit from features such as improved performance, connection health checks, and post\-quantum TLS support\.

For asynchronous operations in the AWS SDK for Java 2\.x, you can use Netty \([NettyNioAsyncHttpClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/http/nio/netty/NettyNioAsyncHttpClient.html)\) as the HTTP client or you can use the new AWS Common Runtime \(CRT\) HTTP client [AwsCrtAsyncHttpClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/http/crt/AwsCrtAsyncHttpClient.html)\. This topics shows you how to configure the AWS CRT\-based HTTP client\.

## Prerequisites<a name="prerequisitescrt"></a>

Before you can use use the AWS CRT client, you need to configure your project dependencies in your `pom.xml` or `build.gradle` file to do the following:
+ Use version `2.14.13` or later of the AWS SDK for Java\.
+ Include version `2.14.13-PREVIEW` of the `artifactId``aws-crt-client`\.

The following code example shows how to configure your project dependencies\.

```
<project>
  <dependencyManagement>
   <dependencies>
      <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>bom</artifactId>
        <version>2.14.13</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
   </dependencies>
  </dependencyManagement>
  <dependencies>
   <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>aws-crt-client</artifactId>
      <version>2.14.13-PREVIEW</version>
   </dependency>
  </dependencies>
</project>
```

## Using the CRT\-based HTTP client<a name="using-the-crt-based-http-client"></a>

You can use the CRT\-based HTTP client for a specific service client, or you can create a single HTTP client to share across multiple service clients\. These options are recommended for most use cases\. Alternatively, you can set the CRT\-based client as the default HTTP client for all asynchronous service clients and requests in your application\.

The following code example shows how to use the CRT\-based HTTP client for a specific service client\.

```
S3AsyncClient.builder()
       .httpClientBuilder(AwsCrtAsyncHttpClient.builder()
                           .maxConcurrency(50))
       .build();
```

The following code example shows how to use the CRT\-based HTTP client as a shared HTTP client\.

```
SdkAsyncHttpClient crtClient = AwsCrtAsyncHttpClient.create()
S3AsyncClient.builder()
       .httpClient(crtClient)
       .build();
```

**Note**  
Your application must manage the lifecycle of an HTTP client instantiated outside of a service client builder\. \(A `builder` is static factory method used by the AWS SDK for Java to connect to Amazon Web Services such as Amazon S3 and Amazon Kinesis\. For more information, see [Creating service clients](using.md#creating-clients)\.\)

### Setting the CRT\-based HTTP client as the default<a name="setting-the-crt-based-http-client-as-the-default"></a>

For asynchronous operations in the AWS SDK for Java 2\.x, you can use Netty \([NettyNioAsyncHttpClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/http/nio/netty/NettyNioAsyncHttpClient.html)\) or the new AWS CRT\-based HTTP client \([AwsCrtAsyncHttpClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/http/crt/AwsCrtAsyncHttpClient.html)\) as the default asynchronous HTTP client in the AWS SDK for Java 2\.x\.

Instead of using Netty as the asynchronous HTTP client, you can set the CRT\-based HTTP client to be the default for your application\. You can set this in your project’s dependencies \(for example, Maven `pom.xml` file\) by explictly excluding Netty\. Alternatively, you can set the default HTTP client via Java system property when you run your app or in your application code\.

### Remove Netty from the project dependencies<a name="remove-netty-from-the-project-dependencies"></a>

Refer to the following snippet of a Maven `pom.xml` file\.

```
<project>
  <dependencies>
   <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>s3</artifactId>
      <version>2.14.13</version>
      <exclusions>
         <exclusion>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>netty-nio-client</artifactId>
         </exclusion>
      </exclusions>
   </dependency>
   <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>aws-crt-client</artifactId>
      <version>2.14.13-PREVIEW</version>
   </dependency>
  </dependencies>
</project>
```

### Setting via Java system property<a name="setting-via-java-system-property"></a>

To use the CRT\-based HTTP client as the default HTTP for your application, you can set the Java system property `software.amazon.awssdk.http.async.service.impl` to a value of `software.amazon.awssdk.http.crt.AwsCrtSdkHttpService`\.

To set during application startup, run a command similar to the following\.

```
java app.jar -Dsoftware.amazon.awssdk.http.async.service.impl=\
software.amazon.awssdk.http.crt.AwsCrtSdkHttpService
```

Use the following code snippet to set in your application code\.

```
System.setProperty("software.amazon.awssdk.http.async.service.impl",
"software.amazon.awssdk.http.crt.AwsCrtSdkHttpService");
```

#### Configuring the CRT\-based HTTP client<a name="configuring-the-crt-based-http-client"></a>

With the CRT\-based HTTP client with in the AWS SDK for Java, you can configure various settings including connection health checks and maximum idle time\. You can also configure post\-quantum TLS support when you make requests to AWS Key Management Service \(Amazon Kinesis\)\.

## Connection health checks<a name="connection-health-checks"></a>

You can configure connection health checks for the CRT\-based HTTP client using the `connectionHealthChecks` method on the HTTP client builder\. Refer to the following example code snippet and the [API documentation](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/http/crt/AwsCrtAsyncHttpClient.html)\.

```
AwsCrtAsyncHttpClient.builder()
           .connectionHealthChecksConfiguration(
              b -> b.minThroughputInBytesPerSecond(32000L)
                 .allowableThroughputFailureInterval(Duration.ofSeconds(3)))
           .build();
```

## Post\-quantum TLS support<a name="post-quantum-tls-support"></a>

You can configure the CRT\-based HTTP client to use post\-quantum TLS when your application makes requests to Amazon Kinesis\. Use the `tlsCipherPreference` method on the HTTP client builder\. Refer to the following example code snippet and the [API documentation](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/http/crtAwsCrtAsyncHttpClient.Builder.html)\.

```
SdkAsyncHttpClient awsCrtHttpClient = AwsCrtAsyncHttpClient.builder()
                               .tlsCipherPreference(TlsCipherPreference.TLS_CIPHER_KMS_PQ_TLSv1_0_2019_06)
                               .build();
KmsAsyncClient kms = KmsAsyncClient.builder()
                  .httpClient(awsCrtHttpClient)
                  .build();
```