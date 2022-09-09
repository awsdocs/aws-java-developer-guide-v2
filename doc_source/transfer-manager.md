--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Amazon S3 Transfer Manager \(Preview\)<a name="transfer-manager"></a>

The Amazon S3 Transfer Manager \(Preview\) is an open\-source high level file transfer utility for the AWS SDK for Java 2\.x that you can use to easily transfer files to and from Amazon Simple Storage Service \(S3\)\. It’s built on top of the Java bindings of the [AWS Common Runtime S3 Client](https://github.com/awslabs/aws-crt-java), benefiting from its enhanced throughput, performance, and reliability by leveraging Amazon S3 multipart upload and byte\-range fetches for parallel transfers\.

This topic helps you use the S3 Transfer Manager\.

## Prerequisites<a name="transfer-manager-prerequisites"></a>

Before you can use the Transfer Manager, you must do the following:
+ Complete the steps in [Setting up the AWS SDK for Java 2\.x](setup.md)\.
+ Configure your project dependencies \(for example, in your `pom.xml` or `build.gradle` file\) to use version `2.17.16` or later of the SDK for Java\.
+ Include version `2.17.16-PREVIEW` of the *artifactId* `s3-transfer-manager`\.

The following code example shows how to configure your project dependencies\.

```
<project>
  <dependencyManagement>
   <dependencies>
      <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>bom</artifactId>
        <version>2.17.16</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
   </dependencies>
  </dependencyManagement>
  <dependencies>
   <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>s3-transfer-manager</artifactId>
      <version>2.17.16-PREVIEW</version>
   </dependency>
  </dependencies>
</project>
```

## Imports<a name="transfer-manager-imports"></a>

To make use of the code snippets in this topic, include the following imports:

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.transfer.s3.S3ClientConfiguration;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.CompletedDownload;
import software.amazon.awssdk.transfer.s3.CompletedUpload;
import software.amazon.awssdk.transfer.s3.Download;
import software.amazon.awssdk.transfer.s3.Upload;
import software.amazon.awssdk.transfer.s3.UploadRequest;
```

## Using the Transfer Manager \(Preview\)<a name="transfer-manager-using"></a>

With the Preview of the Amazon S3 Transfer Manager, you can upload or download one file per request\.

To upload or download a file, first instantiate an [http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/transfer/s3/S3TransferManager.html](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/transfer/s3/S3TransferManager.html) object to use as a service client\.

To instantiate a service client using the default settings, use the `create()` method of `S3TransferManager`\.

```
S3TransferManager s3TransferManager = S3TransferManager.create();
```

To customize the configuration of the service client, such as to select a region or to use a specific credentials provider for the request, build an [http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/transfer/s3/S3ClientConfiguration.html](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/transfer/s3/S3ClientConfiguration.html) object and then specify that configuration with the `s3ClientConfiguration()` method on the service client builder\.

```
Region region = Region.US_WEST_2;
S3ClientConfiguration s3ClientConfiguration =
        S3ClientConfiguration.builder()
                             .region(region)
                             .minimumPartSizeInBytes(10 * MB)
                             .targetThroughputInGbps(20.0)
                             .build();
```

## Upload a file to S3<a name="transfer-manager-upload"></a>

To upload a file to Amazon S3 using the Transfer Manager \(Preview\), first build a [http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/model/PutObjectRequest.html](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/model/PutObjectRequest.html), specifying the Amazon S3 bucket and key to which you want to upload with the `bucket()` and `key()` methods\. Next, instantiate an [http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/transfer/s3/UploadRequest.html](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/transfer/s3/UploadRequest.html) object, passing the `PutObjectRequest` object using the `putObjectRequest()` method\. Set the path to the file via the `source()` method\. Then build an [http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/transfer/s3/Upload.html](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/transfer/s3/Upload.html) object, passing in the `UploadRequest` object via the `upload()` method\.

With the Transfer Manager, you can complete all of the above steps using short\-hand \(Java lambda\) notation, so that all you have to do is specify the path to the file you are uploading and the bucket and key to which you want to upload the file\.

```
Upload upload =
        s3TransferManager.upload(b -> b.putObjectRequest(r -> r.bucket(bucket).key(key))
                         .source(Paths.get("fileToUpload.txt")));
```

To capture the response, use a [http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/transfer/s3/CompletedUpload.html](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/transfer/s3/CompletedUpload.html) object\.

```
CompletedUpload completedUpload = upload.completionFuture().join();

System.out.println("PutObjectResponse: " + completedUpload.response());
```

## Download a file from S3<a name="transfer-manager-download"></a>

To download a file from Amazon S3 using the Transfer Manager \(Preview\), build a [http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/transfer/s3/Download.html](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/transfer/s3/Download.html) object\. Using short\-hand notation, you can specify the Amazon S3 bucket and key using the `getObjectRequest()` method and use the `destination()` to set where the file will be saved\.

```
Download download =
        s3TransferManager.download(b -> b.getObjectRequest(r -> r.bucket(bucket).key(key))
                         .destination(Paths.get("downloadedFile.txt")));
```

To capture the response, use a [http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/transfer/s3/CompletedDownload.html](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/transfer/s3/CompletedDownload.html) object\.

```
CompletedFileDownload completedDownload = download.completionFuture().join();

System.out.println("Content length: "+ completedDownload.response().contentLength());
```
