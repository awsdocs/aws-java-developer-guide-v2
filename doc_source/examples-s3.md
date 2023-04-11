# Working with Amazon S3<a name="examples-s3"></a>

This section provides examples of programming with [Amazon Simple Storage Service \(S3\)](http://aws.amazon.com/s3/) using the AWS SDK for Java 2\.x\.

The following examples include only the code needed to demonstrate each technique\. The [complete example code is available on GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/master/javav2)\. From there, you can download a single source file or clone the repository locally to get all the examples to build and run\.

**Note**  
From version 2\.18\.x and onward, the AWS SDK for Java 2\.x uses virtual host\-style addressing when including an endpoint override, as long as the bucket name is a valid DNS label\.   
Call the [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/S3BaseClientBuilder.html#forcePathStyle(java.lang.Boolean](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/S3BaseClientBuilder.html#forcePathStyle(java.lang.Boolean) method with `true` in your client builder to force the client to use path\-style addressing for buckets\.  
The following example shows a service client configured with an endpoint override and using path\-style addressing\.  

```
S3Client client = S3Client.builder()
                          .region(Region.US_WEST_2)
                          .endpointOverride(URI.create("https://s3.us-west-2.amazonaws.com"))
                          .forcePathStyle(true)
                          .build();
```

## Use access points or Multi\-Region Access Points<a name="examples-s3-access-points"></a>

After [Amazon S3 access points](AmazonS3/latest/userguide/access-points.html) or [Multi\-Region Access Points](AmazonS3/latest/userguide/MultiRegionAccessPoints.html) are set up, you can call object methods, such as `putObject` and `getObject` and provide the access point identifier instead of a bucket name\.

For example, if an access point ARN identifier is `arn:aws:s3:us-west-2:123456789012:accesspoint/test`, you can use the following snippet to call the `putObject` method\.

```
        Path path = Paths.get(URI.create("file:///temp/file.txt"));
        
        s3Client.putObject(builder -> builder
                        .key("myKey")
                        .bucket("arn:aws:s3:us-west-2:123456789012:accesspoint/test")
                , path);
```

In place of the ARN string, you can also use the [bucket\-style alias](https://docs.aws.amazon.com/AmazonS3/latest/userguide/access-points-alias.html) of the access point for the `bucket` parameter\.

To use Multi\-Region Access Point, replace the `bucket` parameter with the Multi\-Region Access Point ARN that has the following format\. 

```
arn:aws:s3::account-id:accesspoint/MultiRegionAccessPoint_alias
```

Add the following Maven dependency to work with Multi\-Region Access Points using the SDK for Java\. Search maven central for the [latest version](https://search.maven.org/search?q=a:auth-crt)\.

```
<dependency>
  <groupId>software.amazon.awssdk</groupId>
  <artifactId>auth-crt</artifactId>
  <version>VERSION</version>
</dependency>
```

**Topics**
+ [Use access points or Multi\-Region Access Points](#examples-s3-access-points)
+ [Bucket operations](examples-s3-buckets.md)
+ [Object operations](examples-s3-objects.md)
+ [Presigned URLs](examples-s3-presign.md)
+ [S3 Glacier](examples-glacier.md)