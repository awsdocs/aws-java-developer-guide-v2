# AWS CRT\-based S3 client<a name="crt-based-s3-client"></a>

The AWS CRT\-based S3 client—built on top of the [AWS Common Runtime \(CRT\)](https://docs.aws.amazon.com/sdkref/latest/guide/common-runtime.html)—is an alternative S3 asynchronous client\. It transfers objects to and from Amazon Simple Storage Service \(Amazon S3\) with enhanced performance and reliability by automatically using Amazon S3's [multipart upload API](https://docs.aws.amazon.com/AmazonS3/latest/userguide/mpuoverview.html) and [byte\-range fetches](https://docs.aws.amazon.com/whitepapers/latest/s3-optimizing-performance-best-practices/use-byte-range-fetches.html)\. 

The AWS CRT\-based S3 client improves transfer reliability in case there is a network failure\. Reliability is improved by retrying individual failed parts of a file transfer without restarting the transfer from the beginning\.

In addition, the AWS CRT\-based S3 client offers enhanced connection pooling and Domain Name System \(DNS\) load balancing, which also improves throughput\.

You can use the AWS CRT\-based S3 client in place of the SDK's standard S3 asynchronous client and take advantage of its improved throughput right away\.

**AWS CRT\-based components in the SDK**

The AWS CRT\-based* S3* client, described in this topic, and the AWS CRT\-based *HTTP* client are different components in the SDK\. 

The **AWS CRT\-based S3 client** is an implementation of the [S3AsyncClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/S3AsyncClient.html) interface and is used for working with the Amazon S3 service\. It is an alternative to the Java\-based implementation of the `S3AsyncClient` interface and offers several benefits\.

The [AWS CRT\-based HTTP client](http-configuration-crt.md) is an implementation of the [SdkAsyncHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/async/SdkAsyncHttpClient.html) interface and is used for general HTTP communication\. It is an alternative to the Netty implementation of the `SdkAsyncHttpClient` interface and offers several advantages\.

Although both components use libraries from the [AWS Common Runtime](https://docs.aws.amazon.com/sdkref/latest/guide/common-runtime.html), the AWS CRT\-based S3 client uses the [aws\-c\-s3 library](https://github.com/awslabs/aws-c-s3) and supports the [S3 multipart upload API](https://docs.aws.amazon.com/AmazonS3/latest/userguide/mpuoverview.html) features\. Since the AWS CRT\-based HTTP client is meant for general purpose use, it does not support the S3 multipart upload API features\.

## Add dependencies to use the AWS CRT\-based S3 client<a name="crt-based-s3-client-depend"></a>

To use the AWS CRT\-based S3 client, add the following two dependencies to your Maven project file\. The example shows the minimum versions to use\. Search the Maven central repository for the most recent versions of the [s3](https://search.maven.org/search?q=g:software.amazon.awssdk%20AND%20a:s3) and [aws\-crt](https://search.maven.org/search?q=g:software.amazon.awssdk.crt%20AND%20a:aws-crt) artifacts\.

```
<dependency>
  <groupId>software.amazon.awssdk</groupId>
  <artifactId>s3</artifactId>
  <version>2.19.0</version>
</dependency>
<dependency>
  <groupId>software.amazon.awssdk.crt</groupId>
  <artifactId>aws-crt</artifactId>
  <version>0.20.3</version>
</dependency>
```

## Create an instance of the AWS CRT\-based S3 client<a name="crt-based-s3-client-create"></a>

 Create an instance of the AWS CRT\-based S3 client with default settings as shown in the following code snippet\.

```
S3AsyncClient s3AsyncClient = S3AsyncClient.crtCreate();
```

To configure the client, use the AWS CRT client builder\. You can switch from the standard S3 asynchronous client to AWS CRT\-based client by changing the builder method\.

```
S3AsyncClient s3AsyncClient = 
        S3AsyncClient.crtBuilder()
                     .credentialsProvider(DefaultCredentialsProvider.create())
                     .region(Region.US_WEST_2)
                     .targetThroughputInGbps(20.0)
                     .minimumPartSizeInBytes(8 * MB)
                     .build();
```

**Note**  
Some of the settings in the standard builder might not be currently supported in the AWS CRT client builder\. Get the standard builder by calling `S3AsyncClient#builder()`\.

## Use the AWS CRT\-based S3 client<a name="crt-based-s3-client-use"></a>

Use the AWS CRT\-based S3 client to call Amazon S3 API operations\. The following example demonstrates the [PutObject](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/S3AsyncClient.html#putObject(java.util.function.Consumer,software.amazon.awssdk.core.async.AsyncRequestBody)) and [GetObject](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/S3AsyncClient.html#getObject(java.util.function.Consumer,software.amazon.awssdk.core.async.AsyncResponseTransformer)) operations available through the AWS SDK for Java\.

```
S3AsyncClient s3Client = S3AsyncClient.crtCreate();

// Upload a local file to Amazon S3.
PutObjectResponse response = 
      s3Client.putObject(req -> req.bucket(<BUCKET_NAME>)
                                   .key(<KEY_NAME>),
                        AsyncRequestBody.fromFile(Paths.get(<FILE_NAME>)))
              .join();

// Download an object from Amazon S3 to a local file.
GetObjectResponse response = 
      s3Client.getObject(req -> req.bucket(<BUCKET_NAME>)
                                   .key(<KEY_NAME>),
                        AsyncResponseTransformer.toFile(Paths.get(<FILE_NAME>)))
              .join();
```