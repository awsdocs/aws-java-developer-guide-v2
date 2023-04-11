# Configuring the Netty\-based HTTP client<a name="http-configuration-netty"></a>

The default HTTP client for asynchronous operations in the AWS SDK for Java 2\.x is the Netty\-based [NettyNioAsyncHttpClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/http/nio/netty/NettyNioAsyncHttpClient.html)\. The Netty\-based client is based on the asynchronous event\-driven network framework of the [Netty project](https://netty.io/)\.

As an alternative, you can use the new [AWS CRT\-based HTTP client](http-configuration-crt.md)\. This topic shows you how to configure the `NettyNioAsyncHttpClient`\.

## Accessing the `NettyNioAsyncHttpClient`<a name="http-config-netty-access"></a>

In most situations, you use the `NettyNioAsyncHttpClient` without any explicit configuration in asynchronous programs\. You declare your asynchronous service clients and the SDK will configure the `NettyNioAsyncHttpClient` with standard values for you\.

If you want to explicitly configure the `NettyNioAsyncHttpClient` or use it with multiple service clients, you need to make it available for configuration\.

### No configuration needed<a name="http-config-netty-no-config"></a>

When you declare a dependency on a service client in Maven, the SDK adds a *runtime* dependency on the `netty-nio-client` artifact\. This makes the `NettyNioAsyncHttpClient` class available to your code at runtime\. If you are not configuring the Netty\-based HTTP client, you don't need to specify a dependency for it\.

In the following XML snippet of a Maven `pom.xml` file, the dependency declared with `<artifactId>dynamodb-enhanced</artifactId>` transitively brings in the Netty\-based HTTP client\. You don't need to declare a dependency specifically for it\.

```
<dependencyManagement>
   <dependencies>
        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>bom</artifactId>
            <version>2.17.290</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
<dependencies>
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>dynamodb-enhanced</artifactId>
    </dependency>
</dependencies>
```

With these dependencies, you cannot make any HTTP configuration changes, since the `NettyNioAsyncHttpClient` library is only on the runtime classpath\. 

### Configuration needed<a name="http-config-netty-yes-config"></a>

To configure the `NettyNioAsyncHttpClient`, you need to add a dependency on the `netty-nio-client` artifact at *compile* time\. 

Refer to the following example of a Maven `pom.xml` file to configure the `NettyNioAsyncHttpClient`\.

```
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>software.amazon.awssdk</groupId>
                <artifactId>bom</artifactId>
                <version>2.17.290</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>dynamodb-enhanced</artifactId>
        </dependency>
        <!-- By adding the netty-nio-client dependency, NettyNioAsyncHttpClient will be 
             added to the compile classpath so you can configure it. -->
        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>netty-nio-client</artifactId>
        </dependency>
    </dependencies>
```

## Configuring the `NettyNioAsyncHttpClient`<a name="http-netty-config"></a>

You can configure an instance of `NettyNioAsyncHttpClient` along with building a service client, or you can configure a single instance to share across multiple service clients\. 

With either approach, you use the [NettyNioAsyncHttpClient\.Builder](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/nio/netty/NettyNioAsyncHttpClient.Builder.html) to configure the properties for the Netty\-based HTTP client instance\.

### Best practice: dedicate a `NettyNioAsyncHttpClient` instance to a service client<a name="http-config-netty-one-client"></a>

If you need to configure an instance of the `NettyNioAsyncHttpClient`, we recommend that you build the dedicated `NettyNioAsyncHttpClient` instance\. You can do so by using the `httpClientBuilder` method of the service client's builder\. This way, the lifecycle of the HTTP client is managed by the SDK, which helps avoid potential memory leaks if the `NettyNioAsyncHttpClient` instance is not closed down when it's no longer needed\.

The following example creates a `DynamoDbAsyncClient` instance, which is also used by a `DynamoDbEnhancedAsyncClient` instance\. The `DynamoDbAsyncClient` instance contains the `NettyNioAsyncHttpClient` instance with `connectionTimeout` and `maxConcurrency` values\. The HTTP instance is created using `httpClientBuilder` method of `DynamoDbAsyncClient.Builder`\.

 **Imports** 

```
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.awscore.defaultsmode.DefaultsMode;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.extensions.AutoGeneratedTimestampRecordExtension;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import java.time.Duration;
```

 **Code** 

```
// DynamoDbAsyncClient is the lower-level client used by the enhanced client.
DynamoDbAsyncClient dynamoDbAsyncClient = 
    DynamoDbAsyncClient
        .builder()
        .httpClientBuilder(NettyNioAsyncHttpClient.builder()
            .connectionTimeout(Duration.ofMinutes(5))
            .maxConcurrency(100))
        .defaultsMode(DefaultsMode.IN_REGION)
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .build();

// Singleton: Use dynamoDbAsyncClient and enhancedClient for all requests.
DynamoDbEnhancedAsyncClient enhancedClient = 
    DynamoDbEnhancedAsyncClient
        .builder()
        .dynamoDbClient(dynamoDbAsyncClient)
        .extensions(AutoGeneratedTimestampRecordExtension.create())
        .build();

// Perform work with the dynamoDbAsyncClient and enhancedClient.

// Requests completed: Close dynamoDbAsyncClient.
dynamoDbAsyncClient.close();
```

### Alternative approach: share a `NettyNioAsyncHttpClient` instance<a name="http-config-netty-multi-clients"></a>

To help keep resource and memory usage lower for your application, you can configure a `NettyNioAsyncHttpClient` and share it across multiple service clients\. The HTTP connection pool will be shared, which lowers resource usage\.

**Note**  
When a `NettyNioAsyncHttpClient` instance is shared, you must close it when it is ready to be disposed\. The SDK will not close the instance when the service client is closed\.

The following example configures a Netty\-based HTTP client, which is used by two service clients\. The configured `NettyNioAsyncHttpClient` instance is passed to the `httpClient` method of each service client's builder\. When the service clients and the HTTP client are no longer needed, they are explicitly closed\. The HTTP client is closed last\.

**Imports**

```
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
```

 **Code** 

```
// Create a NettyNioAsyncHttpClient shared instance.
SdkAsyncHttpClient nettyHttpClient = 
    NettyNioAsyncHttpClient.builder()
                           .connectionTimeout(Duration.ofMinutes(5))
                           .maxConcurrency(100)
                           .build();

// Singletons: Use the s3AsyncClient, dbAsyncClient, and enhancedAsyncClient for all requests.
S3AsyncClient s3AsyncClient = 
    S3AsyncClient.builder()
                 .httpClient(nettyHttpClient)
                 .build();

DynamoDbAsyncClient dbAsyncClient = 
    DynamoDbAsyncClient.builder()
                       .httpClient(nettyHttpClient)
                       .defaultsMode(DefaultsMode.IN_REGION)
                       .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                       .build();

DynamoDbEnhancedAsyncClient enhancedAsyncClient = 
    DynamoDbEnhancedAsyncClient.builder()
                               .dynamoDbClient(dbAsyncClient)
                               .extensions(AutoGeneratedTimestampRecordExtension.create())
                               .build();

// Perform work with s3AsyncClient, dbAsyncClient, and enhancedAsyncClient.

// Requests completed: Close all service clients.
s3AsyncClient.close();
dbAsyncClient.close()
nettyHttpClient.close();  // Explicitly close nettyHttpClient.
```