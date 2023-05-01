# Get started with the AWS SDK for Java 2\.x<a name="get-started"></a>

The AWS SDK for Java 2\.x provides Java APIs for Amazon Web Services \(AWS\)\. Using the SDK, you can build Java applications that work with Amazon S3, Amazon EC2, DynamoDB, and more\.

This tutorial shows you how to use [Apache Maven](https://maven.apache.org/) to define dependencies for the SDK for Java 2\.x and then write code that connects to Amazon S3 to upload a file\.

Follow these steps to complete this tutorial:
+  [Step 1: Set up for this tutorial](#get-started-setup) 
+  [Step 2: Create the project](#get-started-projectsetup) 
+  [Step 3: Write the code](#get-started-code) 
+  [Step 4: Build and run the application](#get-started-run) 

## Step 1: Set up for this tutorial<a name="get-started-setup"></a>

Before you begin this tutorial, you need the following:
+ Permission to access Amazon S3
+ A Java development environment that is configured to access AWS services using single sign\-on to the AWS IAM Identity Center \(successor to AWS Single Sign\-On\)

Use the instructions in [Basic setup](setup-basics.md) to get set up for this tutorial\. After you have [configured your development environment with single sign\-on access](setup-basics.md#setup-credentials) for the Java SDK and you have an [active AWS access portal session](setup-basics.md#setup-login-sso), continue with Step 2 of this tutorial\.

## Step 2: Create the project<a name="get-started-projectsetup"></a>

To create the project for this tutorial, you run a Maven command that prompts you for input on how to configure the project\. After all input is entered and confirmed, Maven finishes building out the project by creating a `pom.xml` and creates stub Java files\.

1. Open a terminal or command prompt window and navigate to a directory of your choice, for example, your `Desktop` or `Home` folder\.

1. Enter the following command at the terminal and press `Enter`\.

   ```
   mvn archetype:generate \
     -DarchetypeGroupId=software.amazon.awssdk \
     -DarchetypeArtifactId=archetype-app-quickstart \
     -DarchetypeVersion=2.20.43
   ```

1. Enter the value listed in the second column for each prompt\.    
[\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html)

1. After the last value is entered, Maven lists the choices you made\. Confirm by entering *`Y`* or re\-enter values by entering *`N`*\.

Maven creates the project folder named `getstarted` based on the `artifactId` value that you entered\. Inside the `getstarted` folder, find a `README.md` file that you can review, a `pom.xml` file, and a `src` directory\.

Maven builds the following directory tree\.

```
getstarted
├── README.md
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── org
    │   │       └── example
    │   │           ├── App.java
    │   │           ├── DependencyFactory.java
    │   │           └── Handler.java
    │   └── resources
    │       └── simplelogger.properties
    └── test
        └── java
            └── org
                └── example
                    └── HandlerTest.java

10 directories, 7 files
```

The following shows the contents of the `pom.xml` project file\.

### `pom.xml`<a name="projectsetup-collapse2"></a>

The `dependencyManagement` section contains a dependency to the AWS SDK for Java 2\.x and the `dependencies` section has a dependency for Amazon S3\. The project uses Java 1\.8 because of the `1.8` value in the `maven.compiler.source` and `maven.compiler.target` properties\.

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>getstarted</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <maven.shade.plugin.version>3.2.1</maven.shade.plugin.version>
        <maven.compiler.plugin.version>3.6.1</maven.compiler.plugin.version>
        <exec-maven-plugin.version>1.6.0</exec-maven-plugin.version>
        <aws.java.sdk.version>2.20.43</aws.java.sdk.version> <-------- SDK version picked up from archetype version.
        <slf4j.version>1.7.28</slf4j.version>
        <junit5.version>5.8.1</junit5.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>software.amazon.awssdk</groupId>
                <artifactId>bom</artifactId>
                <version>${aws.java.sdk.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>s3</artifactId>  <-------- S3 dependency
            <exclusions>
                <exclusion>
                    <groupId>software.amazon.awssdk</groupId>
                    <artifactId>netty-nio-client</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>software.amazon.awssdk</groupId>
                    <artifactId>apache-client</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>sso</artifactId> <-------- Required for identity center authentication.
        </dependency>

        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>ssooidc</artifactId> <-------- Required for identity center authentication.
        </dependency>

        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>apache-client</artifactId> <-------- HTTP client specified.
            <exclusions>
                <exclusion>
                    <groupId>commons-logging</groupId>
                    <artifactId>commons-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>${slf4j.version}</version>
        </dependency>

        <!-- Needed to adapt Apache Commons Logging used by Apache HTTP Client to Slf4j to avoid
        ClassNotFoundException: org.apache.commons.logging.impl.LogFactoryImpl during runtime -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>jcl-over-slf4j</artifactId>
            <version>${slf4j.version}</version>
        </dependency>

        <!-- Test Dependencies -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit5.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven.compiler.plugin.version}</version>
            </plugin>
        </plugins>
    </build>

</project>
```

## Step 3: Write the code<a name="get-started-code"></a>

The following code shows the `App` class created by Maven\. The `main` method is the entry point into the application, which creates an instance of the `Handler` class and then calls its `sendRequest` method\.

### `App` class<a name="projectsetup-collapse2"></a>

```
package org.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String... args) {
        logger.info("Application starts");

        Handler handler = new Handler();
        handler.sendRequest();

        logger.info("Application ends");
    }
}
```

The `DependencyFactory` class created by Maven contains the `s3Client` factory method that build and returns an [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/S3Client.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/S3Client.html) instance\. The `S3Client` instance uses an instance of the Apache\-based HTTP client\. This is because you specified `apache-client` when Maven prompted you for which HTTP client to use\.

The `DependencyFactory` is shown in the following code\.

### `DependencyFactory` class<a name="code-collapse2"></a>

```
package org.example;

import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * The module containing all dependencies required by the {@link Handler}.
 */
public class DependencyFactory {

    private DependencyFactory() {}

    /**
     * @return an instance of S3Client
     */
    public static S3Client s3Client() {
        return S3Client.builder()
                       .httpClientBuilder(ApacheHttpClient.builder())
                       .build();
    }
}
```

The `Handler` class contains the main logic of your program\. When an instance of `Handler` is created in the `App` class, the `DependencyFactory` furnishes the `S3Client` service client\. Your code uses the `S3Client` instance to call the Amazon S3 service\.

Maven generates the following `Handler` class with a `TODO` comment\. The next step in the tutorial replaces the *`TODO`* with code\.

### `Handler` class, Maven\-generated<a name="code-collapsible3"></a>

```
package org.example;

import software.amazon.awssdk.services.s3.S3Client;


public class Handler {
    private final S3Client s3Client;

    public Handler() {
        s3Client = DependencyFactory.s3Client();
    }

    public void sendRequest() {
        // TODO: invoking the api calls using s3Client.
    }
}
```

To fill in the logic, replace the entire contents of the `Handler` class with the following code\. The `sendRequest` method is filled in and the necessary imports are added\.

### `Handler` class, implemented<a name="code-collapse4"></a>

The code first creates a new S3 bucket with the last part of the name generated using `System.currentTimeMillis()` in order to make the bucket name unique\.

After creating the bucket in the `createBucket()` method, the program uploads an object using the [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/S3Client.html#putObject(software.amazon.awssdk.services.s3.model.PutObjectRequest,software.amazon.awssdk.core.sync.RequestBody)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/S3Client.html#putObject(software.amazon.awssdk.services.s3.model.PutObjectRequest,software.amazon.awssdk.core.sync.RequestBody)) method of `S3Client`\. The contents of the object is a simple string created with the `RequestBody.fromString` method\.

Finally, the program deletes the object followed by the bucket in the `cleanUp` method\.

```
package org.example;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;


public class Handler {
    private final S3Client s3Client;

    public Handler() {
        s3Client = DependencyFactory.s3Client();
    }

    public void sendRequest() {
        String bucket = "bucket" + System.currentTimeMillis();
        String key = "key";

        createBucket(s3Client, bucket);

        System.out.println("Uploading object...");

        s3Client.putObject(PutObjectRequest.builder().bucket(bucket).key(key)
                        .build(),
                RequestBody.fromString("Testing with the {sdk-java}"));

        System.out.println("Upload complete");
        System.out.printf("%n");

        cleanUp(s3Client, bucket, key);

        System.out.println("Closing the connection to {S3}");
        s3Client.close();
        System.out.println("Connection closed");
        System.out.println("Exiting...");
    }

    public static void createBucket(S3Client s3Client, String bucketName) {
        try {
            s3Client.createBucket(CreateBucketRequest
                    .builder()
                    .bucket(bucketName)
                    .build());
            System.out.println("Creating bucket: " + bucketName);
            s3Client.waiter().waitUntilBucketExists(HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
            System.out.println(bucketName + " is ready.");
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
            System.out.println(keyName + " has been deleted.");
            System.out.println("Deleting bucket: " + bucketName);
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucketName).build();
            s3Client.deleteBucket(deleteBucketRequest);
            System.out.println(bucketName + " has been deleted.");
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

After the project is created and contains the complete `Handler` class, build and run the application\. 

1. Make sure that you have an active IAM Identity Center session\. To do so, run the AWS Command Line Interface command `aws sts get-caller-identity` and check the response\. If you don't have an active session, see [this section](setup-basics.md#setup-login-sso) for instructions\.

1. Open a terminal or command prompt window and navigate to your project directory `getstarted`\.

1. Use the following command to build your project:

   ```
   mvn clean package
   ```

1. Use the following command to run the application\.

   ```
   mvn exec:java -Dexec.mainClass="org.example.App"
   ```

To view the new bucket and object that the program creates, perform the following steps\.

1. In `Handler.java`, comment out the line `cleanUp(s3Client, bucket, key)` in the `sendRequest` method and save the file\.

1. Rebuild the project by running `mvn clean package`\.

1. Rerun `mvn exec:java -Dexec.mainClass="org.example.App"` to upload the text object once more\.

1. Sign in to [the S3 console](https://console.aws.amazon.com/s3/) to view the new object in the newly created bucket\.

After you view the file, delete the object, and then delete the bucket\.

### Success<a name="get-started-success"></a>

If your Maven project built and ran without error, then congratulations\! You have successfully built your first Java application using the SDK for Java 2\.x\.

### Cleanup<a name="cleanup"></a>

To clean up the resources you created during this tutorial, do the following:
+ If you haven't done so already, in [the S3 console](https://console.aws.amazon.com/s3/), delete any objects and any buckets created when you ran the application\.
+ In [the IAM console](https://console.aws.amazon.com/iam/home#/users), delete the *TestSDK* user\.

  If you delete this user, also remove the contents of the `credentials` file you created during setup\.
+ Delete the project folder \(`getstarted`\)\.

## Next steps<a name="get-started-next"></a>

Now that you have the basics down, you can learn about the following:
+  [Working with Amazon S3](examples-s3.md) 
+  [Working with other Amazon Web Services](examples.md), such as [DynamoDB](examples-dynamodb.md), [Amazon EC2](examples-ec2.md), and [various database services](examples-databases.md) 
+  [Use the SDK](using.md) 
+  [Security for the AWS SDK for Java](security.md) 