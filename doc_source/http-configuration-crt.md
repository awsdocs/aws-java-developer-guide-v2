# Configuring the AWS CRT\-based HTTP client<a name="http-configuration-crt"></a>

The [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/crt/AwsCrtAsyncHttpClient.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/crt/AwsCrtAsyncHttpClient.html) is a new asynchronous HTTP client that you can use with the AWS SDK for Java 2\.x\. The `AwsCrtAsyncHttpClient` brings the following benefits for an HTTP client to the SDK\.
+ faster SDK startup time
+ smaller memory footprint
+ reduced latency time
+ connection health management
+ DNS load balancing

This topic is about using and configuring the AWS CRT\-based HTTP client\. For information about configuring the Netty\-based HTTP client, see the [previous topic](http-configuration-netty.md)\.

**AWS CRT\-based components in the SDK**

The AWS CRT\-based* HTTP* client, described in this topic, and the AWS CRT\-based *S3* client are different components in the SDK\. 

The **AWS CRT\-based HTTP client** is an implementation of the [SdkAsyncHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/async/SdkAsyncHttpClient.html) interface and is used for general HTTP communication\. It is an alternative to the Netty implementation of the `SdkAsyncHttpClient` interface and offers several benefits\.

The **[AWS CRT\-based S3 client](crt-based-s3-client.md)** is an implementation of the [S3AsyncClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/S3AsyncClient.html) interface and is used for working with the Amazon S3 service\. It is an alternative to the Java\-based implementation of the `S3AsyncClient` interface and offers several advantages\.

Although both components use libraries from the [AWS Common Runtime](https://docs.aws.amazon.com/sdkref/latest/guide/common-runtime.html), the AWS CRT\-based HTTP client does not use the [aws\-c\-s3 library](https://github.com/awslabs/aws-c-s3) and does not support the [S3 multipart upload API](https://docs.aws.amazon.com/AmazonS3/latest/userguide/mpuoverview.html) features\. The AWS CRT\-based S3 client, by contrast, was purpose\-built to support the S3 multipart upload API features\.

## Accessing the `AwsCrtAsyncHttpClient`<a name="http-config-crt-access"></a>

Before you can use the AWS CRT\-based HTTP client, add the `aws-crt-client `artifact to your project's dependencies\.

The following Maven `pom.xml` shows the AWS CRT\-based HTTP client declared using the bill of materials \(BOM\) mechanism\.

```
<project>
   <properties>
     <aws.sdk.version>VERSION</aws.sdk.version>
  </properties>
  <dependencyManagement>
   <dependencies>
      <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>bom</artifactId>
        <version>${aws.sdk.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
   </dependencies>
  </dependencyManagement>
  <dependencies>
   <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>aws-crt-client</artifactId>
   </dependency>
  </dependencies>
</project>
```

Visit the Maven central repository for the latest [https://search.maven.org/search?q=g:software.amazon.awssdk%20AND%20a:bom](https://search.maven.org/search?q=g:software.amazon.awssdk%20AND%20a:bom) value\.

## Configuring the `AwsCrtAsyncHttpClient`<a name="http-crt-config"></a>

You can configure an instance of `AwsCrtAsyncHttpClient` along with building a service client, or you can configure a single instance to share across multiple service clients\. 

With either approach, you use the [AwsCrtAsyncHttpClient\.Builder](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/crt/AwsCrtAsyncHttpClient.Builder.html) to configure the properties for the AWS CRT\-based HTTP client instance\.

### Best practice: dedicate a `AwsCrtAsyncHttpClient` instance to a service client<a name="http-config-crt-one-client"></a>

If you need to configure an instance of the `AwsCrtAsyncHttpClient`, we recommend that you build the dedicated `AwsCrtAsyncHttpClient` instance\. You can do so by using the `httpClientBuilder` method of the service client's builder\. This way, the lifecycle of the HTTP client is managed by the SDK, which helps avoid potential memory leaks if the `AwsCrtAsyncHttpClient` instance is not closed down when it's no longer needed\.

The following example creates an `S3Client` and configures the `AwsCrtAsyncHttpClient` with `connectionTimeout` and `maxConcurrency` values\. 

**Imports**

```
import software.amazon.awssdk.http.crt.AwsCrtAsyncHttpClient;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import java.time.Duration;
```

**Code**

```
// Singleton: Use s3AsyncClient for all requests.
S3AsyncClient s3AsyncClient = S3AsyncClient.builder()
    .httpClientBuilder(AwsCrtAsyncHttpClient
            .builder()
            .connectionTimeout(Duration.ofSeconds(3))
            .maxConcurrency(100))
    .build();

// Perform work with the s3AsyncClient.

// Requests completed: Close the s3AsyncClient.
s3AsyncClient.close();
```

### Alternative approach: share a `AwsCrtAsyncHttpClient` instance<a name="http-config-crt-multi-clients"></a>

To help keep resource and memory usage lower for your application, you can configure a `AwsCrtAsyncHttpClient` and share it across multiple service clients\. The HTTP connection pool will be shared, which lowers resource usage\.

**Note**  
When a `AwsCrtAsyncHttpClient` instance is shared, you must close it when it is ready to be disposed\. The SDK will not close the instance when the service client is closed\.

The following example configures a AWS CRT\-based HTTP client instance with `connectionTimeout` and `maxConcurrency` values\. The configured `AwsCrtAsyncHttpClient` instance is passed to the `httpClient` method of each service client's builder\. When the service clients and the HTTP client are no longer needed, they are explicitly closed\. The HTTP client is closed last\.

**Imports**

```
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.awscore.defaultsmode.DefaultsMode;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.crt.AwsCrtAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import java.time.Duration;
```

**Code**

```
// Create an AwsCrtAsyncHttpClient shared instance.
SdkAsyncHttpClient crtAsyncHttpClient = AwsCrtAsyncHttpClient.builder()
    .connectionTimeout(Duration.ofSeconds(3))
    .maxConcurrency(100)
    .build();

// Singletons: Use the s3AsyncClient and dynamoDbAsyncClient for all requests.
S3AsyncClient s3AsyncClient = S3AsyncClient.builder()
    .httpClient(crtAsyncHttpClient)
    .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
    .defaultsMode(DefaultsMode.IN_REGION)
    .region(Region.US_EAST_1)
    .build();

DynamoDbAsyncClient dynamoDbAsyncClient = DynamoDbAsyncClient.builder()
    .httpClient(crtAsyncHttpClient)
    .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
    .defaultsMode(DefaultsMode.IN_REGION)
    .region(Region.US_EAST_1)
    .build();

// Requests completed: Close all service clients.
s3AsyncClient.close();
dynamoDbAsyncClient.close()
crtAsyncHttpClient.close();  // Explicitly close crtAsyncHttpClient.
```

## Setting the AWS CRT\-based HTTP client as the default<a name="setting-the-crt-based-http-client-as-the-default"></a>

For asynchronous operations in the AWS SDK for Java 2\.x, you can replace the `NettyNioAsyncHttpClient` as the default asynchronous HTTP client in your program with the `AwsCrtAsyncHttpClient`\.

You set this in your project’s Maven `pom.xml` file by excluding the dependency on the `netty-nio-client` for each service client\. Alternatively, you can set the default HTTP client by using a Java system property when you run your app or in your application code\.

### Remove Netty from the project dependencies<a name="remove-netty-from-the-project-dependencies"></a>

The following `pom.xml` example removes the Netty\-based HTTP client from the classpath so that the AWS CRT\-based HTTP client will be used instead\. 

```
<project>
   <properties>
     <aws.sdk.version>VERSION</aws.sdk.version>
  </properties>
  <dependencies>
   <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>s3</artifactId>
      <version>${aws.sdk.version}</version>
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
   </dependency>
  </dependencies>
</project>
```

Visit the Maven central repository for the latest [https://search.maven.org/search?q=g:software.amazon.awssdk%20AND%20a:bom](https://search.maven.org/search?q=g:software.amazon.awssdk%20AND%20a:bom) value\.

**Note**  
If multiple service clients are declared in a `pom.xml` file, all require the `exclusions`XML element\.

### Setting via Java system property<a name="setting-via-java-system-property"></a>

To use the AWS CRT\-based HTTP client as the default HTTP for your application, you can set the Java system property `software.amazon.awssdk.http.async.service.impl` to a value of `software.amazon.awssdk.http.crt.AwsCrtSdkHttpService`\.

To set during application startup, run a command similar to the following\.

```
java app.jar -Dsoftware.amazon.awssdk.http.async.service.impl=\
software.amazon.awssdk.http.crt.AwsCrtSdkHttpService
```

Use the following code snippet to set the system property in your application code\.

```
System.setProperty("software.amazon.awssdk.http.async.service.impl",
"software.amazon.awssdk.http.crt.AwsCrtSdkHttpService");
```

**Note**  
You need to add a dependency on the `aws-crt-client` artifact in your `poml.xml` file when you use a system property to configure the use of the AWS CRT\-based HTTP client\.

## Advanced configuration of `AwsCrtAsyncHttpClient`<a name="configuring-the-crt-based-http-client"></a>

You can use the AWS CRT\-based HTTP client to configure various settings, including connection health configuration and maximum idle time\. You can review the configuration [options available](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/crt/AwsCrtAsyncHttpClient.Builder.html) for the `AwsCrtAsyncHttpClient`\.

### Connection health configuration<a name="connection-health-checks"></a>

You can configure connection health configuration for the AWS CRT\-based HTTP client using the `connectionHealthConfiguration` method on the HTTP client builder\. 

The following example creates an `S3AsyncClient` that uses a `AwsCrtAsyncHttpClient` instance configured with connection health configuration and a maximum idle time for connections\. 

```
// Singleton: Use the s3AsyncClient for all requests.
S3AsyncClient s3AsyncClient = S3AsyncClient.builder()
    .httpClientBuilder(AwsCrtAsyncHttpClient
        .builder()
        .connectionHealthConfiguration(builder -> builder
              .minimumThroughputInBps(32000L)
              .minimumThroughputTimeout(Duration.ofSeconds(3)))
         .connectionMaxIdleTime(Duration.ofSeconds(5)))
    .build();

// Perform work with s3AsyncClient.

// Requests complete: Close the service client.
s3AsyncClient.close();
```

## HTTP/2 support<a name="limitation-the-crt-based-http-client"></a>

The HTTP/2 protocol is not yet supported in the AWS CRT\-based HTTP client, but is planned for a future release\. 

In the meantime, if you are using service clients that require HTTP/2 support such as the [KinesisAsyncClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/kinesis/KinesisAsyncClient.html) or the [TranscribeStreamingAsyncClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/transcribestreaming/TranscribeStreamingAsyncClient.html), consider using the [NettyNioAsyncHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/nio/netty/NettyNioAsyncHttpClient.html) instead\. 