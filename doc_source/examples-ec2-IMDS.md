# Work with Amazon EC2 instance metadata<a name="examples-ec2-IMDS"></a>

A Java SDK client for the Amazon EC2 Instance Metadata Service \(metadata client\) allows your applications to access metadata on their local EC2 instance\. The metadata client works with the local instance of [IMDSv2](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/configuring-instance-metadata-service.html) \(Instance Metadata Service v2\) and uses session\-oriented requests\. 

Two client classes are available in the SDK\. The synchronous `[Ec2MetadataClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/imds/Ec2MetadataClient.html)` is for blocking operations, and the [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/imds/Ec2MetadataAsyncClient.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/imds/Ec2MetadataAsyncClient.html) is for asynchronous, non\-blocking use cases\. 

## Get started<a name="examples-ec2-IMDS-getstarted"></a>

To use the metadata client, add the `imds` Maven artifact to your project\. You also need classes for an `[SdkHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/SdkHttpClient.html)` \(or an `[SdkAsyncHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/async/SdkAsyncHttpClient.html)` for the asynchronous variant\) on the classpath\. 

The following Maven XML shows dependency snippets for using the synchronous [UrlConnectionHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/urlconnection/UrlConnectionHttpClient.html) along with the dependency for metadata clients\.

```
<dependencyManagement>
   <dependencies>
        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>bom</artifactId>
            <version>VERSION</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>imds</artifactId>
    </dependency>
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>url-connection-client</artifactId>
    </dependency>
    <!-- other dependencies --> 
</dependencies>
```

Search the [Maven central repository](https://search.maven.org/search?q=g:software.amazon.awssdk%20AND%20a:bom) for the latest version of the `bom` artifact\.

To use an asynchronous HTTP client, replace the dependency snippet for the `url-connection-client` artifact\. For example, the following snippet brings in the [NettyNioAsyncHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/nio/netty/NettyNioAsyncHttpClient.html) implementation\.

```
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>netty-nio-client</artifactId>
    </dependency>
```

## Use the metadata client<a name="examples-ec2-IMDS-use"></a>

### Instantiate a metadata client<a name="examples-ec2-IMDS-use-create"></a>

You can instantiate an instance of a synchronous `Ec2MetadataClient` when only one implementation of the `SdkHttpClient` interface is present on the classpath\. To do so, call the static `Ec2MetadataClient#create()` method as shown in the following snippet\. 

```
Ec2MetadataClient client = Ec2MetadataClient.create(); // 'Ec2MetadataAsyncClient#create' is the asynchronous version.
```

If your application has multiple implementations of the `SdkHttpClient` or `SdkHttpAsyncClient` interface, you must specify an implementation for the metadata client to use as shown in the [Configurable HTTP client](#examples-ec2-IMDS-features-http) section\. 

**Note**  
For most service clients, such as Amazon S3, the SDK for Java automatically adds implementations of the `SdkHttpClient` or `SdkHttpAsyncClient` interface\. If your metadata client uses the same implementation, then `Ec2MetadataClient#create()` will work\. If you require a different implementation, you must specify it when you create the metadata client\.

### Send requests<a name="examples-ec2-IMDS-use-req"></a>

To retrieve instance metadata, instantiate the `EC2MetadataClient` class and call the `get` method with a path parameter that specifies the [instance metadata category](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/instancedata-data-categories.html)\.

The following example prints the value associated with the `ami-id` key to the console\.

```
Ec2MetadataClient client = Ec2MetadataClient.create();
Ec2MetadataResponse response = client.get("/latest/meta-data/ami-id");
System.out.println(response.asString());
client.close(); // Closes the internal resources used by the Ec2MetadataClient class.
```

If the path isn't valid, the `get` method throws an exception\. 

Reuse the same client instance for multiple requests, but call `close` on the client when it is no longer needed to release resources\. After the close method is called, the client instance can't be used anymore\.

### Parse responses<a name="examples-ec2-IMDS-use-pares"></a>

EC2 instance metadata can be output in different formats\. Plain text and JSON are the most commonly used formats\. The metadata clients offer ways to work with those formats\. 

As the following example shows, use the `asString` method to get the data as a Java string\. You can also use the `asList` method to separate a plain text response that returns multiple lines\. 

```
Ec2MetadataClient client = Ec2MetadataClient.create();
Ec2MetadataResponse response = client.get("/latest/meta-data/");
String fullResponse = response.asString();
List<String> splits = response.asList();
```

If the response is in JSON, use the `Ec2MetadataResponse#asDocument` method to parse the JSON response into a [Document](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/document/Document.html) instance as shown in the following code snippet\.

```
Document fullResponse = response.asDocument();
```

An exception will be thrown if the format of the metadata is not in JSON\. If the response is successfully parsed, you can use the [document API](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/document/package-summary.html) to inspect the response in more detail\. Consult the instance [metadata category chart](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/instancedata-data-categories.html) to learn which metadata categories deliver JSON\-formatted responses\.

## Configure a metadata client<a name="examples-ec2-IMDS-config"></a>

### Retries<a name="examples-ec2-IMDS-config-retries"></a>

You can configure a metadata client with a retry mechanism\. If you do, then the client can automatically retry requests that fail for unexpected reasons\. By default, the client retries three times on a failed request with an exponential backoff time between attempts\.

If your use case requires a different retry mechanism, you can customize the client using the `retryPolicy` method on its builder\. For example, the following example shows a synchronous client configured with a fixed delay of two seconds between attempts and five retry attempts\.

```
BackoffStrategy fixedBackoffStrategy = FixedDelayBackoffStrategy.create(Duration.ofSeconds(2));
Ec2MetadataClient client =
    Ec2MetadataClient.builder()
                     .retryPolicy(retryPolicyBuilder -> retryPolicyBuilder.numRetries(5)
                                                                           .backoffStrategy(fixedBackoffStrategy))
                     .build();
```

There are several [BackoffStrategies](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/retry/backoff/package-summary.html) that you can use with a metadata client\.

You can also disable the retry mechanism entirely, as the following snippet shows\.

```
Ec2MetadataClient client =
    Ec2MetadataClient.builder()
                    .retryPolicy(Ec2MetadataRetryPolicy.none())
                    .build();
```

Using `Ec2MetadataRetryPolicy#none()` disables the default retry policy so that the metadata client attempts no retries\.

### IP version<a name="examples-ec2-IMDS-config-ipversion"></a>

By default, a metadata client uses the IPV4 endpoint at `http://169.254.169.254`\. To change the client to use the IPV6 version, use either the `endpointMode` or the `endpoint` method of the builder\. An exception results if both methods are called on the builder\.

The following examples show both IPV6 options\.

```
Ec2MetadataClient client =
    Ec2MetadataClient.builder()
                     .endpointMode(EndpointMode.IPV6)
                     .build();
```

```
Ec2MetadataClient client =
    Ec2MetadataClient.builder()
                     .endpoint(URI.create("http://[fd00:ec2::254]"))
                     .build();
```

## Key features<a name="examples-ec2-IMDS-features"></a>

### Asynchronous client<a name="examples-ec2-IMDS-features-async"></a>

To use the non\-blocking version of the client, instantiate an instance of the `Ec2MetadataAsyncClient` class\. The code in the following example creates an asynchronous client with default settings and uses the `get` method to retrieve the value for the `ami-id` key\.

```
Ec2MetadataAsyncClient asyncClient = Ec2MetadataAsyncClient.create();
CompletableFuture<Ec2MetadataResponse> response = asyncClient.get("/latest/meta-data/ami-id");
```

The `java.util.concurrent.CompletableFuture` returned by the `get` method completes when the response returns\. The following example prints the `ami-id` metadata to the console\.

```
response.thenAccept(metadata -> System.out.println(metadata.asString()));
```

### Configurable HTTP client<a name="examples-ec2-IMDS-features-http"></a>

The builder for each metadata client has a `httpClient` method that you can use to supply a customized HTTP client\. 

The following example shows code for a custom `UrlConnectionHttpClient` instance\.

```
SdkHttpClient httpClient =
    UrlConnectionHttpClient.builder()
                           .socketTimeout(Duration.ofMinutes(5))
                           .proxyConfiguration(proxy -> proxy.endpoint(URI.create("http://proxy.example.net:8888"))))
                           .build();
Ec2MetadataClient metaDataClient =
    Ec2MetadataClient.builder()
                     .httpClient(httpClient)
                     .build();
// Use the metaDataClient instance.
metaDataClient.close();   // Close the instance when no longer needed.
```

The following example shows code for a custom `NettyNioAsyncHttpClient` instance with an asynchronous metadata client\.

```
SdkAsyncHttpClient httpAsyncClient = 
    NettyNioAsyncHttpClient.builder()
                           .connectionTimeout(Duration.ofMinutes(5))
                           .maxConcurrency(100)
                           .build();
Ec2MetadataAsyncClient asyncMetaDataClient =
    Ec2MetadataAsyncClient.builder()
                          .httpClient(httpAsyncClient)
                          .build();
// Use the asyncMetaDataClient instance.
asyncMetaDataClient.close();   // Close the instance when no longer needed.
```

The [HTTP clients](http-configuration.md) topic in this guide provides details on how to configure the HTTP clients that are available in the SDK for Java\.

### Token caching<a name="examples-ec2-IMDS-features-token"></a>

Because the metadata clients use IMDSv2, all requests are associated with a session\. A session is defined by a token that has an expiration, which the metadata client manages for you\. Every metadata request automatically reuses the token until it expires\. 

By default, a token lasts for six hours \(21,600 seconds\)\. We recommend that you keep the default time\-to\-live value, unless your specific use case requires advanced configuration\. 

If needed, configure the duration by using the `tokenTtl` builder method\. For example, the code in the following snippet creates a client with a session duration of five minutes\. 

```
Ec2MetadataClient client =
    Ec2MetadataClient.builder()
                     .tokenTtl(Duration.ofMinutes(5))
                     .build();
```

If you omit calling the `tokenTtl` method on the builder, the default duration of 21,600 is used instead\. 