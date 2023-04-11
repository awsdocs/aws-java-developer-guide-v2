# Working with Amazon S3 presigned URLs<a name="examples-s3-presign"></a>

You can use a [S3Presigner](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/presigner/S3Presigner.html) object to sign an Amazon S3 `SdkRequest` so that it’s executed without requiring authentication on the part of the caller\. For example, assume Alice has access to an S3 object, and she wants to temporarily share access to that object with Bob\. Alice can generate a pre\-signed [GetObjectRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/model/GetObjectRequest.html) object to secure share with Bob so that he can download the object without requiring access to Alice’s credentials\.

**Topics**
+ [Generate a Presigned URL and Upload an Object](#generate-presignedurl)
+ [Get a Presigned Object](#get-presignedobject)

## Generate a Presigned URL and Upload an Object<a name="generate-presignedurl"></a>

Build a [S3Presigner](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/presigner/S3Presigner.html) object that represents the client object\. Next create a [PresignedPutObjectRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/presigner/model/PresignedPutObjectRequest.html) object that can be executed at a later time without requiring additional signing or authentication\. When you create this object, you can specify the bucket name and the key name\. In addition, you can also specify the time in minutes that the bucket can be accessed without using credentials by invoking the `signatureDuration` method \(as shown in the following code example\)\.

You can use the [PresignedPutObjectRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/presigner/model/PresignedPutObjectRequest.html) object to obtain the URL by invoking its `url` method\.

 **Imports** 

```
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
```

 **Code** 

The following Java code example uploads content to a presigned S3 bucket\.

```
    public static void signBucket(S3Presigner presigner, String bucketName, String keyName) {

        try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("author","Mary Doe");
            metadata.put("version","1.0.0.0");

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .contentType("text/plain")
                .metadata(metadata)
                .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
                .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            System.out.println("Presigned URL to upload a file to: " + presignedRequest.url());
            System.out.println("Which HTTP method needs to be used when uploading a file: " + presignedRequest.httpRequest().method());

            // Upload content to the Amazon S3 bucket by using this URL.
            URL url = presignedRequest.url();

            // Create the connection and use it to upload the new object.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type","text/plain");
            connection.setRequestProperty("x-amz-meta-author","Mary Doe");
            connection.setRequestProperty("x-amz-meta-version","1.0.0.0");
            connection.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write("This text was uploaded as an object by using a presigned URL.");
            out.close();

            connection.getResponseCode();
            System.out.println("HTTP response code is " + connection.getResponseCode());

        } catch (S3Exception | IOException e) {
            e.getStackTrace();
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/GeneratePresignedUrlAndUploadObject.java) on GitHub\.

## Get a Presigned Object<a name="get-presignedobject"></a>

Build a [S3Presigner](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/presigner/S3Presigner.html) object that represents the client object\. Next, create a [GetObjectRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/model/GetObjectRequest.html) object and specify the bucket name and key name\. In addition, create a [GetObjectPresignRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/presigner/model/GetObjectPresignRequest.html) object that can be executed at a later time without requiring additional signing or authentication\. When you create this object, you can specify the time in minutes that the bucket can be accessed without using credentials by invoking the `signatureDuration` method \(as shown in the following code example\)\.

Invoke the `presignGetObject` method that belongs to the [S3Presigner](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/presigner/S3Presigner.html) object to create a [PresignedGetObjectRequest](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/presigner/model/PresignedGetObjectRequest.html) object\. You can invoke this object’s `url` method to obtain the URL to use\. Once you have the URL, you can use standard HTTP Java logic to read the contents of the bucket, as shown in the following Java code example\.

 **Imports** 

```
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.time.Duration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.utils.IoUtils;
```

 **Code** 

The following Java code example reads content from a presigned S3 bucket\.

```
       public static void getPresignedUrl(S3Presigner presigner, String bucketName, String keyName ) {

           try {
               GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                   .bucket(bucketName)
                   .key(keyName)
                   .build();

               GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                   .signatureDuration(Duration.ofMinutes(60))
                   .getObjectRequest(getObjectRequest)
                   .build();

               PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
               String theUrl = presignedGetObjectRequest.url().toString();
               System.out.println("Presigned URL: " + theUrl);
               HttpURLConnection connection = (HttpURLConnection) presignedGetObjectRequest.url().openConnection();
               presignedGetObjectRequest.httpRequest().headers().forEach((header, values) -> {
                values.forEach(value -> {
                    connection.addRequestProperty(header, value);
                 });
               });

               // Send any request payload that the service needs (not needed when isBrowserExecutable is true).
               if (presignedGetObjectRequest.signedPayload().isPresent()) {
                   connection.setDoOutput(true);

               try (InputStream signedPayload = presignedGetObjectRequest.signedPayload().get().asInputStream();
                    OutputStream httpOutputStream = connection.getOutputStream()) {
                    IoUtils.copy(signedPayload, httpOutputStream);
               }
           }

           // Download the result of executing the request.
           try (InputStream content = connection.getInputStream()) {
               System.out.println("Service returned response: ");
               IoUtils.copy(content, System.out);
           }

       } catch (S3Exception | IOException e) {
           e.getStackTrace();
       }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/GetObjectPresignedUrl.java) on GitHub\.