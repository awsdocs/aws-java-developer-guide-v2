# Configuring the Apache\-based HTTP client<a name="http-configuration-apache"></a>

Synchronous service clients in the AWS SDK for Java 2\.x use an Apache\-based HTTP client, [ApacheHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/apache/ApacheHttpClient.html) by default\. The SDK's `ApacheHttpClient` is based on the Apache [HttpClient](https://hc.apache.org/httpcomponents-client-4.5.x/index.html)\.

The SDK also offers the [UrlConnectionHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/urlconnection/UrlConnectionHttpClient.html), which loads more quickly, but has fewer features\. For information about configuring the `UrlConnectionHttpClient`, see [Configuring the URLConnection\-based HTTP client](http-configuration-url.md)\. 

To see the full set of configuration options available to you for the `ApacheHttpClient`, see [ApacheHttpClient\.Builder](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/apache/ApacheHttpClient.Builder.html) and [ProxyConfiguration\.Builder](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/apache/ProxyConfiguration.Builder.html)\.

## Accessing the `ApacheHttpClient`<a name="http-apache-dependency"></a>

In most situations, you use the `ApacheHttpClient` without any explicit configuration\. You declare your service clients and the SDK will configure the ApacheHttpClient with standard values for you\.

If you want to explicitly configure the `ApacheHttpClient` or use it with multiple service clients, you need to make it available for configuration\.

### No configuration needed<a name="http-config-apache-no-config"></a>

When you declare a dependency on a service client in Maven, the SDK adds a *runtime* dependency on the `apache-client` artifact\. This makes the `ApacheHttpClient` class available to your code at runtime\. If you are not configuring the Apache\-based HTTP client, you do not need to specify a dependency for it\.

In the following XML snippet of a Maven `pom.xml` file, the dependency declared with `<artifactId>s3</artifactId>` automatically brings in the Apache\-based HTTP client\. You don't need to declare a dependency specifically for it\.

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
    <!-- The s3 dependency automatically adds a runtime dependency on the ApacheHttpClient-->
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>s3</artifactId>
    </dependency>
</dependencies>
```

With these dependencies, you cannot make any explicit HTTP configuration changes, because the `ApacheHttpClient` library is only on the runtime classpath\. 

### Configuration needed<a name="http-config-apache-yes-config"></a>

To configure the `ApacheHttpClient`, you need to add a dependency on the `apache-client` library at *compile* time\. 

Refer to the following example of a Maven `pom.xml` file to configure the `ApacheHttpClient`\.

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
            <artifactId>s3</artifactId>
        </dependency>
        <!-- By adding the apache-client dependency, ApacheHttpClient will be added to 
             the compile classpath so you can configure it. -->
        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>apache-client</artifactId>
        </dependency>
    </dependencies>
```

## Configuring the `ApacheHttpClient`<a name="http-apache-config"></a>

You can configure an instance of `ApacheHttpClient` along with building a service client, or you can configure a single instance to share across multiple service clients\. 

With either approach, you use the `[ApacheHttpClient\.Builder](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/apache/ApacheHttpClient.Builder.html)` to configure the properties for the Apache\-based HTTP client\.

### Best practice: dedicate an `ApacheHttpClient` instance to a service client<a name="http-config-apache-recomm"></a>

If you need to configure an instance of the `ApacheHttpClient`, we recommend that you build the dedicated `ApacheHttpClient` instance\. You can do so by using the `httpClientBuilder` method of the service client's builder\. This way, the lifecycle of the HTTP client is managed by the SDK, which helps avoid potential memory leaks if the `ApacheHttpClient` instance is not closed down when it's no longer needed\.

The following example creates an `S3Client` and configures the embedded instance of `ApacheHttpClient` with `maxConnections` and `connectionTimeout` values\. The HTTP instance is created using the `httpClientBuilder` method of `S3Client.Builder`\.

 **Imports** 

```
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.s3.S3Client;
import java.time.Duration;
```

 **Code** 

```
S3Client s3Client = S3Client   // Singleton: Use the s3Client for all requests.
    .builder()
    .httpClientBuilder(ApacheHttpClient.builder()
        .maxConnections(100)
        .connectionTimeout(Duration.ofSeconds(5))
    ).build();

// Perform work with the s3Client.

s3Client.close();   // Requests completed: Close all service clients.
```

### Alternative approach: share an `ApacheHttpClient` instance<a name="http-config-apache-alt"></a>

To help keep resource and memory usage lower for your application, you can configure an `ApacheHttpClient` and share it across multiple service clients\. The HTTP connection pool will be shared, which lowers resource usage\.

**Note**  
When an `ApacheHttpClient` instance is shared, you must close it when it is ready to be disposed\. The SDK will not close the instance when the service client is closed\.

The following example configures an Apache\-based HTTP client, which is used by two service clients\. The configured `ApacheHttpClient` instance is passed to the `httpClient` method of the service client's builder\. When the service clients and the HTTP client are no longer needed, they are explicitly closed\. The HTTP client is closed last\.

**Imports**

```
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
```

 **Code** 

```
SdkHttpClient apacheHttpClient = 
    ApacheHttpClient.builder()
                    .maxConnections(100)
                    .tcpKeepAlive(true)
                    .build();

// Singletons: Use the s3Client and dynamoDbClient for all requests.
S3Client s3Client = 
    S3Client.builder()
            .httpClient(apacheHttpClient).build();

DynamoDbClient dynamoDbClient = 
    DynamoDbClient.builder()
                  .httpClient(apacheHttpClient).build();

// Perform work with the s3Client and dynamoDbClient.

// Requests completed: Close all service clients.
s3Client.close();
dynamoDbClient.close();
apacheHttpClient.close();  // Explicitly close apacheHttpClient.
```