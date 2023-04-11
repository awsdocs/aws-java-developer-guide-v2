# Amazon S3 Transfer Manager<a name="transfer-manager"></a>

The Amazon S3 Transfer Manager is an open source, high level file transfer utility for the AWS SDK for Java 2\.x\. Use it to transfer files and directories to and from Amazon Simple Storage Service \(Amazon S3\)\. 

When built on top of the [AWS CRT\-based S3 client](crt-based-s3-client.md), the S3 Transfer Manager can take advantage of performance improvements such as [multipart upload API](https://docs.aws.amazon.com/AmazonS3/latest/userguide/mpuoverview.html) and [byte\-range fetches](https://docs.aws.amazon.com/whitepapers/latest/s3-optimizing-performance-best-practices/use-byte-range-fetches.html)\. 

With the S3 Transfer Manager, you can also monitor a transfer's progress in real time and pause the transfer for later execution\.

## Get started<a name="transfer-manager-prerequisites"></a>

### Add dependencies to your build file<a name="transfer-manager-add-dependency"></a>

To use the S3 Transfer Manager with enhanced performance based on the AWS CRT\-based S3 client, configure your build file with the following dependencies\.
+ Use version *2\.19\.1* or higher of the SDK for Java 2\.x\.
+ Add the `s3-transfer-manager` artifact as a dependency\.
+ Add the `aws-crt` artifact as a dependency at version *0\.20\.3* or higher\.

The following code example shows how to configure your project dependencies for Maven\.

```
<project>
   <properties>
     <aws.sdk.version>2.19.1</aws.sdk.version>
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
      <artifactId>s3-transfer-manager</artifactId>
   </dependency>
   <dependency>
      <groupId>software.amazon.awssdk.crt</groupId>
      <artifactId>aws-crt</artifactId>
      <version>0.20.3</version>
   </dependency>
  </dependencies>
</project>
```

Search the Maven central repository for the most recent versions of the [s3\-transfer\-manager](https://search.maven.org/search?q=g:software.amazon.awssdk%20AND%20a:s3-transfer-manager) and [aws\-crt](https://search.maven.org/search?q=g:software.amazon.awssdk.crt%20AND%20a:aws-crt) artifacts\.

### Create an instance of the S3 Transfer Manager<a name="transfer-manager-create"></a>

The following snippet shows how to create a [S3TransferManager](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/S3TransferManager.html) instance with default settings\.

```
        S3TransferManager transferManager = S3TransferManager.create();   
```

The following example shows how to configure a S3 Transfer Manager with custom settings\. In this example, a [AWS CRT\-based S3AsyncClient](crt-based-s3-client.md) instance is used as the underlying client for the S3 Transfer Manager\.

```
        S3AsyncClient s3AsyncClient =
            S3AsyncClient.crtBuilder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.US_EAST_1)
                .targetThroughputInGbps(20.0)
                .minimumPartSizeInBytes(8 * MB)
                .build();

        S3TransferManager transferManager =
            S3TransferManager.builder()
                .s3Client(s3AsyncClient)
                .build();
```

**Note**  
If the `aws-crt` dependency is not included in the build file, the S3 Transfer Manager is built on top of the standard S3 asynchronous client used in the SDK for Java 2\.x\.

## Upload a file to an S3 bucket<a name="transfer-manager-upload"></a>

To upload a file to Amazon S3 using the S3 Transfer Manager, pass an [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/model/UploadFileRequest.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/model/UploadFileRequest.html) object to the `S3TransferManager`'s [uploadFile](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/S3TransferManager.html#uploadFile(software.amazon.awssdk.transfer.s3.model.UploadFileRequest)) method\.

The [FileUpload](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/model/FileUpload.html) object returned from the `uploadFile` method represents the upload process\. After the request finishes, the [CompletedFileUpload](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/model/CompletedFileUpload.html) object contains information about the upload\.

The following example shows a file upload example along with the optional use of a [LoggingTransferListener](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/progress/LoggingTransferListener.html), which logs the progress of the upload\.

### Imports<a name="transfer-manager-upload-imports"></a>

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedFileUpload;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

import java.net.URL;
import java.nio.file.Paths;
import java.util.UUID;
```

### Code<a name="transfer-manager-upload-code"></a>

```
    public String uploadFile(S3TransferManager transferManager, String bucketName,
                             String key, String filePath) {
        UploadFileRequest uploadFileRequest =
            UploadFileRequest.builder()
                .putObjectRequest(b -> b.bucket(bucketName).key(key))
                .addTransferListener(LoggingTransferListener.create())
                .source(Paths.get(filePath))
                .build();

        FileUpload fileUpload = transferManager.uploadFile(uploadFileRequest);

        CompletedFileUpload uploadResult = fileUpload.completionFuture().join();
        return uploadResult.response().eTag();
    }
```

## Download a file from an S3 bucket<a name="transfer-manager-download"></a>

To download an object from an S3 bucket using the S3 Transfer Manager, build a [DownloadFileRequest](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/model/DownloadFileRequest.html) object and pass it to the [downloadFile](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/S3TransferManager.html#downloadFile(software.amazon.awssdk.transfer.s3.model.DownloadFileRequest)) method\.

The [FileDownload](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/model/FileDownload.html) object returned by the `S3TransferManager`'s `downloadFile` method represents the file transfer\. After the download completes, the [CompletedFileDownload](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/model/CompletedFileDownload.html) contains access to information about the download\.

The following example also shows a download example plus the optional use of a [LoggingTransferListener](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/progress/LoggingTransferListener.html), which logs the progress of the download\.

### Imports<a name="transfer-manager-download-import"></a>

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedFileDownload;
import software.amazon.awssdk.transfer.s3.model.DownloadFileRequest;
import software.amazon.awssdk.transfer.s3.model.FileDownload;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
```

### Code<a name="transfer-manager-download-code"></a>

```
    public Long downloadFile(S3TransferManager transferManager, String bucketName,
                             String key, String downloadedFileWithPath) {
        DownloadFileRequest downloadFileRequest =
            DownloadFileRequest.builder()
                .getObjectRequest(b -> b.bucket(bucketName).key(key))
                .addTransferListener(LoggingTransferListener.create())
                .destination(Paths.get(downloadedFileWithPath))
                .build();

        FileDownload downloadFile = transferManager.downloadFile(downloadFileRequest);

        CompletedFileDownload downloadResult = downloadFile.completionFuture().join();
        logger.info("Content length [{}]", downloadResult.response().contentLength());
        return downloadResult.response().contentLength();
    }
```

## Copy an Amazon S3 object to another bucket<a name="transfer-manager-copy"></a>

To begin the copy of an object from an S3 bucket to another bucket, create a basic [CopyObjectRequest](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/model/CopyObjectRequest.html) instance\.

Next, wrap the basic `CopyObjectRequest` in a [CopyRequest](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/model/CopyRequest.html) that can be used by the S3 Transfer Manager\. 

The `Copy` object returned by the `S3TransferManager`'s `copy` method represents the copy process\. After the copy process completes, the [CompletedCopy](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/model/CompletedCopy.html) object contains details about the response\.

### Imports<a name="transfer-manager-copy-import"></a>

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedCopy;
import software.amazon.awssdk.transfer.s3.model.Copy;
import software.amazon.awssdk.transfer.s3.model.CopyRequest;

import java.util.UUID;
```

### Code<a name="transfer-manager-copy-code"></a>

```
    public String copyObject(S3TransferManager transferManager, String bucketName,
                             String key, String destinationBucket, String destinationKey){
        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
            .sourceBucket(bucketName)
            .sourceKey(key)
            .destinationBucket(destinationBucket)
            .destinationKey(destinationKey)
            .build();

        CopyRequest copyRequest = CopyRequest.builder()
            .copyObjectRequest(copyObjectRequest)
            .build();

        Copy copy = transferManager.copy(copyRequest);

        CompletedCopy completedCopy = copy.completionFuture().join();
        return completedCopy.response().copyObjectResult().eTag();
    }
```

**Note**  
Cross\-Region copies are not currently supported\.

## Upload a local directory to an S3 bucket<a name="transfer-manager-upload_directory"></a>

To upload a local directory to an S3 bucket, start by calling the [uploadDirectory](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/S3TransferManager.html#uploadDirectory(software.amazon.awssdk.transfer.s3.model.UploadDirectoryRequest)) method of the `S3TransferManager` instance, passing in an [UploadDirectoryRequest](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/model/UploadDirectoryRequest.html)\.

The [DirectoryUpload](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/model/DirectoryUpload.html) object represents the upload process, which generates a [CompletedDirectoryUpload](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/model/CompletedDirectoryUpload.html) when the request completes\. The `CompleteDirectoryUpload` object contains information about the results of the transfer, including which files failed to transfer\.

### Imports<a name="transfer-manager-upload_directory-import"></a>

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedDirectoryUpload;
import software.amazon.awssdk.transfer.s3.model.DirectoryUpload;
import software.amazon.awssdk.transfer.s3.model.UploadDirectoryRequest;

import java.net.URL;
import java.nio.file.Paths;
import java.util.UUID;
```

### Code<a name="transfer-manager-upload_directory-code"></a>

```
    public Integer uploadDirectory(S3TransferManager transferManager,
                                   String sourceDirectory, String bucketName){
        DirectoryUpload directoryUpload =
            transferManager.uploadDirectory(UploadDirectoryRequest.builder()
                .source(Paths.get(sourceDirectory))
                .bucket(bucketName)
                .build());

        CompletedDirectoryUpload completedDirectoryUpload = directoryUpload.completionFuture().join();
        completedDirectoryUpload.failedTransfers().forEach(fail ->
            logger.warn("Object [{}] failed to transfer", fail.toString()));
        return completedDirectoryUpload.failedTransfers().size();
    }
```

## Download S3 bucket objects to a local directory<a name="transfer-manager-download_directory"></a>

To download the objects in an S3 bucket to a local directory, begin by calling the [downloadDirectory](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/S3TransferManager.html#downloadDirectory(software.amazon.awssdk.transfer.s3.model.DownloadDirectoryRequest)) method of the Transfer Manager, passing in a [DownloadDirectoryRequest](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/model/DownloadDirectoryRequest.html)\.

The [DirectoryDownload](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/model/DirectoryDownload.html) object represents the download process, which generates a [CompletedDirectoryDownload](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/model/CompletedDirectoryDownload.html) when the request completes\. The `CompleteDirectoryDownload` object contains information about the results of the transfer, including which files failed to transfer\.

### Imports<a name="transfer-manager-download_directory-import"></a>

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedDirectoryDownload;
import software.amazon.awssdk.transfer.s3.model.DirectoryDownload;
import software.amazon.awssdk.transfer.s3.model.DownloadDirectoryRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
```

### Code<a name="transfer-manager-download_directory-code"></a>

```
    public Integer downloadObjectsToDirectory(S3TransferManager transferManager,
                                              String destinationPath, String bucketName) {
        DirectoryDownload directoryDownload =
            transferManager.downloadDirectory(DownloadDirectoryRequest.builder()
                .destination(Paths.get(destinationPath))
                .bucket(bucketName)
                .build());
        CompletedDirectoryDownload completedDirectoryDownload = directoryDownload.completionFuture().join();

        completedDirectoryDownload.failedTransfers().forEach(fail ->
            logger.warn("Object [{}] failed to transfer", fail.toString()));
        return completedDirectoryDownload.failedTransfers().size();
    }
```