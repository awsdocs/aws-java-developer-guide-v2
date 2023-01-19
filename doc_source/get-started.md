--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Get started with the AWS SDK for Java 2\.x<a name="get-started"></a>

The AWS SDK for Java 2\.x provides Java APIs for Amazon Web Services \(AWS\)\. Using the SDK, you can build Java applications that work with Amazon S3, Amazon EC2, DynamoDB, and more\.

This tutorial shows you how you can use [Apache Maven](https://maven.apache.org/) to define dependencies for the AWS SDK for Java and then write code that connects to Amazon S3 to upload a file\.

Follow these steps to complete this tutorial:
+  [Step 1: Set up for this tutorial](#get-started-setup) 
+  [Step 2: Create the project](#get-started-projectsetup) 
+  [Step 3: Write the code](#get-started-code) 
+  [Step 4: Build and run the application](#get-started-run) 

## Step 1: Set up for this tutorial<a name="get-started-setup"></a>

Before you begin this tutorial, you need an active AWS account, an AWS Identity and Access Management \(IAM\) user with a programmatic access key and permissions to Amazon S3, and a Java development environment configured to use that access key as credentials for AWS\.

Follow these steps to set up for this tutorial:
+  [Create an AWS account](#get-started-setup-account) 
+  [Create an IAM user](#get-started-setup-user) 
+  [Install Java and Apache Maven](#get-started-setup-javamaven) 
+  [Configure credentials](#get-started-setup-credentials) 

### Create an account<a name="get-started-setup-account"></a>

If you do not have an AWS account, visit [the Amazon Web Services signup page](http://portal.aws.amazon.com/billing/signup) and follow the on\-screen prompts to create and activate a new account\. For detailed instructions, see [How do I create and activate a new AWS account?](http://aws.amazon.com/premiumsupport/knowledge-center/create-and-activate-aws-account/)\.

After you activate your new AWS account, follow the instructions in [Creating your first IAM admin user and group](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html#getting-started_create-admin-group-console) in the IAM User Guide\. Use this account instead of the root account when accessing the AWS Console\. For more information, see [IAM User Guide](http://docs.aws.amazon.com/IAM/latest/UserGuide/)\.

### Create an IAM user<a name="get-started-setup-user"></a>

To complete this tutorial, you need to use credentials for an IAM user that has read and write access to Amazon S3\. To make requests to Amazon Web Services using the AWS SDK for Java, create an access key to use as credentials\.

1. Sign in to [the IAM console](https://console.aws.amazon.com/iam/) 

1. In the navigation pane on the left, choose **Users**\. Then choose **Add user**\.

1. Enter *TestSDK* as the **User name** and select the **Programmatic access** checkbox\. Choose **Next: Permissions**\.

1. Under **Set permissions**, select **Attach existing policies directly**\.

1. In the list of policies, select the checkbox for the **AmazonS3FullAccess** policy\. Choose **Next: Tags**\.

1. Choose **Next: Review**\. Then choose **Create user**\.

1. On the *Success* screen, choose **Download \.csv**\.

   The downloaded file contains the Access Key ID and the Secret Access Key for this tutorial\. Treat your Secret Access Key as a password; save in a trusted location and do not share it\.
**Note**  
You will **not** have another opportunity to download or copy the Secret Access Key\.

### Install Java and Apache Maven<a name="get-started-setup-javamaven"></a>

Your development environment needs to have Java 8 or later and Apache Maven installed\.
+ For Java, use [Oracle Java SE Development Kit](https://www.oracle.com/java/technologies/javase-downloads.html), [Amazon Corretto](http://aws.amazon.com/corretto/), [Red Hat OpenJDK](https://developers.redhat.com/products/openjdk), or [AdoptOpenJDK](https://adoptopenjdk.net/)\.
+ For Maven, go to [https://maven\.apache\.org/](https://maven.apache.org/)\.

### Configure credentials<a name="get-started-setup-credentials"></a>

Configure your development environment with your Access Key ID and the Secret Access Key\. The AWS SDK for Java uses this access key as credentials when your application makes requests to Amazon Web Services\.

1. In a text editor, create a new file with the following code:

   ```
   [default]
   aws_access_key_id = YOUR_AWS_ACCESS_KEY_ID
   aws_secret_access_key = YOUR_AWS_SECRET_ACCESS_KEY
   ```

1. In the text file you just created, replace *YOUR\_AWS\_ACCESS\_KEY* with your unique AWS access key ID, and replace *YOUR\_AWS\_SECRET\_ACCESS\_KEY* with your unique AWS secret access key\.

1. Save the file without a file extension\. Refer to the following table for the correct location and file name based on your operating system\.  
**​**    
[\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html)

## Step 2: Create the project<a name="get-started-projectsetup"></a>

To create the project for this tutorial, you first create a Maven project\. Next, you configure your project with a dependency on AWS SDK for Java and for any AWS service you use, for example Amazon S3\. Then you configure the Maven compiler to use Java 1\.8\.

1. Open a terminal or command prompt window and navigate to a directory of your choice, for example, your Desktop or Home folder\.

1. Use the following command to create a new directory called `myapp` with a project configuration file \(`pom.xml`\) and a basic Java class\.

   ```
   mvn -B archetype:generate \
    -DarchetypeGroupId=org.apache.maven.archetypes \
    -DgroupId=com.example.myapp \
    -DartifactId=myapp
   ```

 **To configure your project with dependencies for the AWS SDK for Java and Amazon S3, and to use Java 1\.8** 
+ In the folder `myapp` that you created in the previous procedure, open the `pom.xml` file\. Replace its contents with the following code, and then save your changes\.

  ```
  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
   <modelVersion>4.0.0</modelVersion>
   <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
   </properties>
   <groupId>com.example.myapp</groupId>
   <artifactId>myapp</artifactId>
   <packaging>jar</packaging>
   <version>1.0-SNAPSHOT</version>
   <name>myapp</name>
   <dependencyManagement>
    <dependencies>
     <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>bom</artifactId>
      <version>2.16.60</version>
      <type>pom</type>
      <scope>import</scope>
     </dependency>
    </dependencies>
   </dependencyManagement>
   <dependencies>
    <dependency>
     <groupId>junit</groupId>
     <artifactId>junit</artifactId>
     <version>3.8.1</version>
     <scope>test</scope>
    </dependency>
    <dependency>
     <groupId>software.amazon.awssdk</groupId>
     <artifactId>s3</artifactId>
    </dependency>
   </dependencies>
   <build>
    <plugins>
     <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <version>3.8.1</version>
      <configuration>
       <source>8</source>
       <target>8</target>
      </configuration>
     </plugin>
    </plugins>
   </build>
  </project>
  ```

The `dependencyManagement` section contains a dependency to the AWS SDK for Java and the `dependencies` section has a dependency for Amazon S3\. The Apache Maven Compiler Plugin is configured in the `build` section to use Java 1\.8\.

## Step 3: Write the code<a name="get-started-code"></a>

After the project has been created and configured, edit the project’s default class `App` to use the example code below\.

The example class below creates a service client for Amazon S3 and then uses it to upload a text file\. To create a service client for Amazon S3, instantiate an [S3Client](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/S3Client.html) object using the static factory method `builder`\. To upload a file to Amazon S3, first build a [PutObjectRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/model/PutObjectRequest.html) object, supplying a bucket name and a key name\. Then, call the S3Client’s `putObject` method, with a [RequestBody](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/sync/RequestBody.html) that contains the object content and the `PutObjectRequest` object\.

1. In your project folder `myapp`, navigate to the directory `src/main/java/com/example/myapp`\. Open the `App.java` file\.

1. Replace its contents with the following code and save the file\.

   ```
   package com.example.myapp;
   
   import java.io.IOException;
   import software.amazon.awssdk.core.sync.RequestBody;
   import software.amazon.awssdk.regions.Region;
   import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
   import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
   import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
   import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
   import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
   import software.amazon.awssdk.services.s3.model.PutObjectRequest;
   import software.amazon.awssdk.services.s3.model.S3Exception;
   import software.amazon.awssdk.services.s3.S3Client;
   
   
   public class App {
   
     public static void main(String[] args) throws IOException {
   
       Region region = Region.US_WEST_2;
       S3Client s3 = S3Client.builder().region(region).build();
   
       String bucket = "bucket" + System.currentTimeMillis();
       String key = "key";
   
       tutorialSetup(s3, bucket, region);
   
       System.out.println("Uploading object...");
   
       s3.putObject(PutObjectRequest.builder().bucket(bucket).key(key)
               .build(),
           RequestBody.fromString("Testing with the {sdk-java}"));
   
       System.out.println("Upload complete");
       System.out.printf("%n");
   
       cleanUp(s3, bucket, key);
   
       System.out.println("Closing the connection to {S3}");
       s3.close();
       System.out.println("Connection closed");
       System.out.println("Exiting...");
     }
   
     public static void tutorialSetup(S3Client s3Client, String bucketName, Region region) {
       try {
         s3Client.createBucket(CreateBucketRequest
             .builder()
             .bucket(bucketName)
             .createBucketConfiguration(
                 CreateBucketConfiguration.builder()
                     .locationConstraint(region.id())
                     .build())
             .build());
         System.out.println("Creating bucket: " + bucketName);
         s3Client.waiter().waitUntilBucketExists(HeadBucketRequest.builder()
             .bucket(bucketName)
             .build());
         System.out.println(bucketName +" is ready.");
         System.out.printf("%n");
       } catch (S3Exception e) {
         System.err.println(e.awsErrorDetails().errorMessage());
         System.exit(1);
       }
     }
   
     public static void cleanUp(S3Client s3Client, String bucketName, String keyName) {
       System.out.println("Cleaning up...");
       try {
         System.out.println("Deleting object: " + keyName);
         DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(keyName).build();
         s3Client.deleteObject(deleteObjectRequest);
         System.out.println(keyName +" has been deleted.");
         System.out.println("Deleting bucket: " + bucketName);
         DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucketName).build();
         s3Client.deleteBucket(deleteBucketRequest);
         System.out.println(bucketName +" has been deleted.");
         System.out.printf("%n");
       } catch (S3Exception e) {
         System.err.println(e.awsErrorDetails().errorMessage());
         System.exit(1);
       }
       System.out.println("Cleanup complete");
       System.out.printf("%n");
     }
   }
   ```

## Step 4: Build and run the application<a name="get-started-run"></a>

After the project is created and contains the example class, build and run the application\. To view the uploaded file in the Amazon S3 console, edit the code to remove the cleanup steps and then rebuild the project\.

1. Open a terminal or command prompt window and navigate to your project directory `myapp`\.

1. Use the following command to build your project:

   ```
   mvn package
   ```

1. Open a terminal or command prompt window and navigate to your project directory `myapp`\.

1. Use the following command to run the application\.

   ```
   mvn exec:java -Dexec.mainClass="com.example.myapp.App"
   ```

When you run the application, it uploads a new a text file to a new bucket in Amazon S3\. Afterward, it will also delete the file and bucket\.

1. In `Handler.java`, comment out the line `cleanUp(s3, bucket, key);` and save the file\.

1. Rebuild the project by running `mvn package`\.

1. Upload the file by running `mvn exec:java -Dexec.mainClass="com.example.myapp.App"` again\.

1. Sign in to [the S3 console](https://console.aws.amazon.com/s3/) to view the new file in the newly\-created bucket\.

After you view the file, clean up test resources by deleting the object and then deleting the bucket\.

### Success\!<a name="get-started-success"></a>

If your Maven project built and ran without error, then congratulations\! You have successfully built your first Java application using the AWS SDK for Java\.

### Cleanup<a name="cleanup"></a>

To clean up the resources you created during this tutorial:
+ In [the S3 console](https://console.aws.amazon.com/s3/), delete any objects and any buckets created when you ran the application\.
+ In [the IAM console](https://console.aws.amazon.com/iam/home#/users), delete the *TestSDK* user\.

  If you delete this user, also remove the contents of the `credentials` file you created during setup\.
+ Delete the project folder \(`myapp`\)\.

## Next steps<a name="get-started-next"></a>

Now that you have the basics down, you can learn about:
+  [Working with Amazon S3](examples-s3.md) 
+  [Working with other Amazon Web Services](examples.md), such as [DynamoDB](examples-dynamodb.md), [Amazon EC2](examples-ec2.md), and [IAM](examples-iam.md) 
+  [Using the SDK](using.md) 
+  [Security for the AWS SDK for Java](security.md) 
