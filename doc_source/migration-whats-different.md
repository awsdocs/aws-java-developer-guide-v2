# What's different between the AWS SDK for Java 1\.x and 2\.x<a name="migration-whats-different"></a>

This section describes the main changes to be aware of when converting an application from using the AWS SDK for Java version 1\.x to version 2\.x\.

## Package name change<a name="mig-diff-package-name-change"></a>

A noticable change from the the SDK for Java 1\.x to the SDK for Java 2\.x is the package name change\. Package names begin with `software.amazon.awssdk` in SDK 2\.x, whereas the SDK 1\.x uses `com.amazonaws`\.

These same names differentiate Maven artifacts from SDK 1\.x to SDK 2\.x\. Maven artifacts for the SDK 2\.x use the `software.amazon.awssdk` groupId, whereas the SDK 1\.x uses the `com.amazonaws` groupId\.

There are a few times when your code requires a `com.amazonaws` dependency for a project that otherwise uses only SDK 2\.x artifacts\. One example of this is when you work with server\-side AWS Lambda\. This was shown in the [Set up an Apache Maven project](setup-project-maven.md#modules-dependencies) section earlier in this guide\.

**Note**  
Several package names in the SDK 1\.x contain `v2`\. The use of `v2` in this case usually means that code in the package is targted to work with version 2 of the service\.   
Since the full package name begins with `com.amazonaws`, these are SDK 1\.x components\. Examples of these package names in the SDK 1\.x are:   
`com.amazonaws.services.dynamodbv2`
`com.amazonaws.retry.v2`
`com.amazonaws.services.apigatewayv2`
`com.amazonaws.services.simpleemailv2`

## High\-level libraries<a name="highlevel-libraries"></a>

High\-level libraries, such as Amazon SQS Client\-side Buffering, are not yet available in version 2\.x\. For a list of libraries that have been released, see the AWS SDK for Java 2\.x [changelog](https://github.com/aws/aws-sdk-java-v2/blob/master/docs/LaunchChangelog.md#7-high-level-libraries)\.

If your application depends on libraries that aren't released in version 2\.x, configure your `pom.xml` file to use both 1\.x and 2\.x\. For more information, see [Using both SDKs side\-by\-side](migration-side-by-side.md)\.

## Adding version 2\.x to your project<a name="adding-v2"></a>

Maven is the recommended way to manage dependencies when using the AWS SDK for Java 2\.x\. To add version 2 components to your project, update your `pom.xml` file with a dependency on the SDK\. 

**Example**  

```
<dependencyManagement>
    <dependencies>
        <dependency>
          <groupId>software.amazon.awssdk</groupId>
          <artifactId>bom</artifactId>
          <version>2.16.1</version>
          <type>pom</type>
          <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>dynamodb</artifactId>
    </dependency>
</dependencies>
```

## Client builders<a name="client-builder"></a>

You must create all clients using the client builder method\. Constructors are no longer available\.

**Example of creating a client in version 1\.x**  

```
AmazonDynamoDB ddbClient = AmazonDynamoDBClientBuilder.defaultClient();
AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient();
```

**Example of creating a client in version 2\.x**  

```
DynamoDbClient ddbClient = DynamoDbClient.create();
DynamoDbClient ddbClient = DynamoDbClient.builder().build();
```

## Client Configuration<a name="client-configuration"></a>

In 1\.x, SDK client configuration was modified by setting a `ClientConfiguration` instance on the client or client builder\. In version 2\.x, the client configuration is split into separate configuration classes\. With the separate configuration classes, you can configure different HTTP clients for async versus synchronous clients but still use the same `ClientOverrideConfiguration` class\.

**Example of client configuration in version 1\.x**  

```
AmazonDynamoDBClientBuilder.standard()
.withClientConfiguration(clientConfiguration)
.build()
```

**Example of synchronous client configuration in version 2\.x**  

```
ProxyConfiguration.Builder proxyConfig = ProxyConfiguration.builder();

ApacheHttpClient.Builder httpClientBuilder =
        ApacheHttpClient.builder()
                        .proxyConfiguration(proxyConfig.build());

ClientOverrideConfiguration.Builder overrideConfig =
        ClientOverrideConfiguration.builder();

DynamoDbClient client =
        DynamoDbClient.builder()
                      .httpClientBuilder(httpClientBuilder)
                      .overrideConfiguration(overrideConfig.build())
                      .build();
```

**Example of asynchronous client configuration in version 2\.x**  

```
NettyNioAsyncHttpClient.Builder httpClientBuilder =
        NettyNioAsyncHttpClient.builder();

ClientOverrideConfiguration.Builder overrideConfig =
        ClientOverrideConfiguration.builder();

ClientAsyncConfiguration.Builder asyncConfig =
        ClientAsyncConfiguration.builder();

DynamoDbAsyncClient client =
        DynamoDbAsyncClient.builder()
                           .httpClientBuilder(httpClientBuilder)
                           .overrideConfiguration(overrideConfig.build())
                           .asyncConfiguration(asyncConfig.build())
                           .build();
```

For a complete mapping of client configuration methods between 1\.x and 2\.x, see the AWS SDK for Java 2\.x [changelog](https://github.com/aws/aws-sdk-java-v2/blob/master/docs/LaunchChangelog.md#13-sdk-client-configuration)\.

## Setter Methods<a name="setter-methods"></a>

In the AWS SDK for Java 2\.x, setter method names don’t include the `set` or `with` prefix\. For example, `*.withEndpoint()` is now `*.endpoint()`\.

**Example of using setting methods in 1\.x**  

```
AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
        		.withRegion("us-east-1")
        		.build();
```

**Example of using setting methods in 2\.x**  

```
DynamoDbClient client = DynamoDbClient.builder()
        		.region(Region.US_EAST_1)
        		.build();
```

## Class Names<a name="class-names"></a>

All client class names are now fully camel cased and no longer prefixed by `Amazon`\. These changes are aligned with names used in the AWS CLI\. For a full list of client name changes, see the AWS SDK for Java 2\.x [changelog](https://github.com/aws/aws-sdk-java-v2/blob/master/docs/LaunchChangelog.md#63-client-names)\.

**Example of class names in 1\.x**  

```
AmazonDynamoDB
AWSACMPCAAsyncClient
```

**Example of class names in 2\.x**  

```
DynamoDbClient
AcmAsyncClient
```

## Region class<a name="region-class"></a>

The AWS SDK for Java version 1\.x had multiple `Region` and `Regions` classes, both in the core package and in many of the service packages\. `Region` and `Regions` classes in version 2\.x are now collapsed into one core class, `Region`\.

**Example Region and Regions classes in 1\.x**  

```
com.amazonaws.regions.Region
com.amazonaws.regions.Regions
com.amazonaws.services.ec2.model.Region
```

**Example Region class in 2\.x**  

```
software.amazon.awssdk.regions.Region
```

For more details about changes related to using the `Region` class, see [Region class name changes](migration-client-region.md)\.

## Immutable POJOs<a name="immutable-classes"></a>

Clients and operation request and response objects are now immutable and cannot be changed after creation\. To reuse a request or response variable, you must build a new object to assign to it\.

**Example of updating a request object in 1\.x**  

```
DescribeAlarmsRequest request = new DescribeAlarmsRequest();
DescribeAlarmsResult response = cw.describeAlarms(request);

request.setNextToken(response.getNextToken());
```

**Example of updating a request object in 2\.x**  

```
DescribeAlarmsRequest request = DescribeAlarmsRequest.builder().build();
DescribeAlarmsResponse response = cw.describeAlarms(request);

request = DescribeAlarmsRequest.builder()
        .nextToken(response.nextToken())
        .build();
```

## Streaming operations<a name="streaming-operations"></a>

Streaming operations such as the Amazon S3 `getObject` and `putObject` methods now support non\-blocking I/O\. As a result, the request and response POJOs no longer take `InputStream` as a parameter\. Instead, the request object accepts `RequestBody`, which is a stream of bytes\. The asynchronous client accepts `AsyncRequestBody`\.

**Example of Amazon S3`putObject` operation in 1\.x**  

```
s3client.putObject(BUCKET, KEY, new File(file_path));
```

**Example of Amazon S3`putObject` operation in 2\.x**  

```
s3client.putObject(PutObjectRequest.builder()
                                 .bucket(BUCKET)
                                 .key(KEY)
                                 .build(),
                 RequestBody.of(Paths.get("myfile.in")));
```

In parallel, the response object accepts `ResponseTransformer` for synchronous clients and `AsyncResponseTransformer` for asynchronous clients\.

**Example of Amazon S3`getObject` operation in 1\.x**  

```
S3Object o = s3.getObject(bucket, key);
S3ObjectInputStream s3is = o.getObjectContent();
FileOutputStream fos = new FileOutputStream(new File(key));
```

**Example of Amazon S3`getObject` operation in 2\.x**  

```
s3client.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build(),
		ResponseTransformer.toFile(Paths.get("key")));
```

## Exception changes<a name="exceptions-changes"></a>

Exception class names, their structures, and their relationships have also changed\. `software.amazon.awssdk.core.exception.SdkException` is the new base `Exception` class that all the other exceptions extend\.

For a full list of the 2\.x exception class names mapped to the 1\.x exceptions, see [Exception class name changes](migration-exception-changes.md)\.

## Service\-specific changes<a name="service-changes"></a>

### Amazon S3 operation name changes<a name="s3-operations-name"></a>

Many of the operation names for the Amazon S3 client have changed in the AWS SDK for Java 2\.x\. In version 1\.x, the Amazon S3 client is not generated directly from the service API\. This results in inconsistency between the SDK operations and the service API\. In version 2\.x, we now generate the Amazon S3 client to be more consistent with the service API\.

**Example of Amazon S3 client operation in 1\.x**  

```
changeObjectStorageClass
```

**Example of Amazon S3 client operation in 2\.x**  

```
copyObject
```

**Example of Amazon S3 client operation in the Amazon S3 service API**  

```
CopyObject
```

For a full list of the operation name mappings, see the AWS SDK for Java 2\.x [changelog](https://github.com/aws/aws-sdk-java-v2/blob/master/docs/LaunchChangelog.md#4-service-changes)\.

### Cross\-Region access<a name="cross-region-access"></a>

For security best practices, cross\-Region access is no longer supported for single clients\.

In version 1\.x, services such as Amazon S3, Amazon SNS, and Amazon SQS are allowed to access resources across Region boundaries\. This is no longer allowed in version 2\.x using the same client\. If you need to access a resource in a different Region, you must create a client in that Region and retrieve the resource using the appropriate client\.

**Topics**