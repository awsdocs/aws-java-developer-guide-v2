# Working with Amazon S3 objects<a name="examples-s3-objects"></a>

An Amazon S3 object represents a file or collection of data\. Every object must be contained in a [bucket](examples-s3-buckets.md)\.

**Note**  
Best Practice  
We recommend that you enable the [AbortIncompleteMultipartUpload](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlifecycle.html) lifecycle rule on your Amazon S3 buckets\.  
This rule directs Amazon S3 to abort multipart uploads that don’t complete within a specified number of days after being initiated\. When the set time limit is exceeded, Amazon S3 aborts the upload and then deletes the incomplete upload data\.  
For more information, see [Lifecycle Configuration for a Bucket with Versioning](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/lifecycle-configuration-bucket-with-versioning.html) in the Amazon Simple Storage Service User Guide\.

**Note**  
These code snippets assume that you understand the material in basics, and have configured default AWS credentials using the information in [Set up single sign\-on access for the SDK](setup-basics.md#setup-credentials)\.

**Topics**
+ [Upload an object](#upload-object)
+ [Upload objects in multiple parts](#list-objects)
+ [Delete an object](#delete-object)
+ [Copy an object](#copy-an-object)
+ [List objects](#list-object)
+ [More examples](#examples-s3-objects-more-ex)

## Upload an object<a name="upload-object"></a>

Build a [PutObjectRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/model/PutObjectRequest.html) and supply a bucket name and key name\. Then use the S3Client’s `putObject` method with a [RequestBody](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/sync/RequestBody.html) that contains the object content and the `PutObjectRequest` object\. *The bucket must exist, or the service will return an error\.* 

 **Imports** 

```
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
```

 **Code** 

```
        Region region = Region.US_WEST_2;
        s3 = S3Client.builder()
                .region(region)
                .build();

        createBucket(s3, bucketName, region);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3.putObject(objectRequest, RequestBody.fromByteBuffer(getRandomByteBuffer(10_000)));
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java) on GitHub\.

## Upload objects in multiple parts<a name="list-objects"></a>

Use the S3Client’s `createMultipartUpload` method to get an upload ID\. Then use the `uploadPart` method to upload each part\. Finally, use the S3Client’s `completeMultipartUpload` method to tell Amazon S3 to merge all the uploaded parts and finish the upload operation\.

 **Imports** 

```
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
```

 **Code** 

```
        // First create a multipart upload and get the upload id
        CreateMultipartUploadRequest createMultipartUploadRequest = CreateMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        CreateMultipartUploadResponse response = s3.createMultipartUpload(createMultipartUploadRequest);
        String uploadId = response.uploadId();
        System.out.println(uploadId);

        // Upload all the different parts of the object
        UploadPartRequest uploadPartRequest1 = UploadPartRequest.builder()
                .bucket(bucketName)
                 .key(key)
                .uploadId(uploadId)
                .partNumber(1).build();

        String etag1 = s3.uploadPart(uploadPartRequest1, RequestBody.fromByteBuffer(getRandomByteBuffer(5 * mB))).eTag();

        CompletedPart part1 = CompletedPart.builder().partNumber(1).eTag(etag1).build();

        UploadPartRequest uploadPartRequest2 = UploadPartRequest.builder().bucket(bucketName).key(key)
                .uploadId(uploadId)
                .partNumber(2).build();
        String etag2 = s3.uploadPart(uploadPartRequest2, RequestBody.fromByteBuffer(getRandomByteBuffer(3 * mB))).eTag();
        CompletedPart part2 = CompletedPart.builder().partNumber(2).eTag(etag2).build();


        // Finally call completeMultipartUpload operation to tell S3 to merge all uploaded
        // parts and finish the multipart operation.
        CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder()
                .parts(part1, part2)
                .build();

        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                CompleteMultipartUploadRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .uploadId(uploadId)
                        .multipartUpload(completedMultipartUpload)
                        .build();

        s3.completeMultipartUpload(completeMultipartUploadRequest);
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java) on GitHub\.

## Delete an object<a name="delete-object"></a>

Build a [DeleteObjectRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/model/DeleteObjectRequest.html) and supply a bucket name and key name\. Use the S3Client’s `deleteObject` method, and pass it the name of a bucket and object to delete\. *The specified bucket and object key must exist, or the service will return an error\.* 

 **Imports** 

```
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
```

 **Code** 

```
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3.deleteObject(deleteObjectRequest);
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java) on GitHub\.

## Copy an object<a name="copy-an-object"></a>

Build a [CopyObjectRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/model/CopyObjectRequest.html) and supply a bucket name that the object is coped into, a URL encoded string value \(see the URLEncoder\.encode method\), and the key name of the object\. Use the S3Client’s `copyObject` method, and pass the [CopyObjectRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/model/CopyObjectRequest.html) object\. *The specified bucket and object key must exist, or the service will return an error\.* 

 **Imports** 

```
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
```

 **Code** 

```
    public static String copyBucketObject (S3Client s3, String fromBucket, String objectKey, String toBucket) {

        CopyObjectRequest copyReq = CopyObjectRequest.builder()
            .sourceBucket(fromBucket)
            .sourceKey(objectKey)
            .destinationBucket(toBucket)
            .destinationKey(objectKey)
            .build();

        try {
            CopyObjectResponse copyRes = s3.copyObject(copyReq);
            return copyRes.copyObjectResult().toString();

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/CopyObject.java) on GitHub\.

## List objects<a name="list-object"></a>

Build a [ListObjectsRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/model/ListObjectsRequest.html) and supply the bucket name\. Then invoke the S3Client’s `listObjects` method and pass the [ListObjectsRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/model/ListObjectsRequest.html) object\. This method returns a [ListObjectsResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/model/ListObjectsResponse.html) that contains all of the objects in the bucket\. You can invoke this object’s *contents* method to get a list of objects\. You can iterate through this list to display the objects, as shown in the following code example\.

 **Imports** 

```
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import java.util.List;
```

 **Code** 

```
    public static void listBucketObjects(S3Client s3, String bucketName ) {

        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                .builder()
                .bucket(bucketName)
                .build();

            ListObjectsResponse res = s3.listObjects(listObjects);
            List<S3Object> objects = res.contents();
            for (S3Object myValue : objects) {
                System.out.print("\n The name of the key is " + myValue.key());
                System.out.print("\n The object is " + calKb(myValue.size()) + " KBs");
                System.out.print("\n The owner is " + myValue.owner());
            }

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    //convert bytes to kbs.
    private static long calKb(Long val) {
        return val/1024;
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/ListObjects.java) on GitHub\.

## More examples<a name="examples-s3-objects-more-ex"></a>

The [Code examples](java_s3_code_examples.md) section of this guide contains more examples of working with Amazon S3 objects including how to [download an object](java_s3_code_examples.md#s3_GetObject_java_topic)\.