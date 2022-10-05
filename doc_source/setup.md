--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Setting up the AWS SDK for Java 2\.x<a name="setup"></a>

The AWS SDK for Java 2\.x provides Java APIs for Amazon Web Services \(AWS\)\. Using the SDK, you can build Java applications that work with Amazon S3, Amazon EC2, DynamoDB, and more\.

This section provides information about how to set up your development environment and projects to use the latest version \(2\.x\) of the AWS SDK for Java\.

## Overview<a name="setup-overview"></a>

To make requests to AWS using the AWS SDK for Java, you need the following:
+ An active AWS account 
+ An AWS Identity and Access Management \(IAM\) user with:
  + A programmatic access key
  + Permissions to the AWS resources you’ll access using your application
+ A development environment with:
  + Your access key configured as credentials for AWS 
  + Java 8 or later
  + A build automation tool

## Create an account<a name="setup-awsaccount"></a>

If you do not have an AWS account, visit [the Amazon Web Services signup page](http://portal.aws.amazon.com/billing/signup) and follow the on\-screen prompts to create and activate a new account\.

For more detailed instructions, see [How do I create and activate a new AWS account?](http://aws.amazon.com/premiumsupport/knowledge-center/create-and-activate-aws-account/)\.

After you activate your new AWS account, follow the instructions in [Creating your first IAM admin user and group](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html#getting-started_create-admin-group-console) in the [IAM User Guide](http://docs.aws.amazon.com/IAM/latest/UserGuide/)\. Use this account instead of the root account when accessing the AWS Management Console\. For more information, see [Security best practices in IAM](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html#lock-away-credentials)\] in the [IAM User Guide](http://docs.aws.amazon.com/IAM/latest/UserGuide/)\.

## Create an IAM user and programmatic access key<a name="setup-iamuser"></a>

To use the AWS SDK for Java to access AWS services, you need an AWS account and AWS credentials\. To increase the security of your AWS account, for access credentials, we recommend that you use an IAM user instead of your AWS account credentials\.

**Note**  
For an overview of IAM users and why they are important for the security of your account, see [AWS security credentials](https://docs.aws.amazon.com/general/latest/gr/aws-security-credentials.html) in the Amazon Web Services General Reference\.

For instructions on creating an access key for an existing IAM user, see [Programmatic access](https://docs.aws.amazon.com/general/latest/gr/aws-sec-cred-types.html#access-keys-and-secret-access-keys) in the [IAM User Guide](http://docs.aws.amazon.com/IAM/latest/UserGuide/)\.

1. Go to the [IAM console](https://console.aws.amazon.com/iam/home) \(you may need to sign in to AWS first\)\.

1. Click **Users** in the sidebar to view your IAM users\.

1. If you don’t have any IAM users set up, click **Create New Users** to create one\.

1. Select the IAM user in the list that you’ll use to access AWS\.

1. Open the **Security Credentials** tab, and click **Create Access Key**\. NOTE: You can have a maximum of two active access keys for any given IAM user\. If your IAM user has two access keys already, then you’ll need to delete one of them before creating a new key\.

1. On the resulting dialog box, click the **Download Credentials** button to download the credential file to your computer, or click **Show User Security Credentials** to view the IAM user’s access key ID and secret access key \(which you can copy and paste\)\.

**Important**  
There is no way to obtain the secret access key once you close the dialog box\. You can, however, delete its associated access key ID and create a new one\.

## Set default credentials and Region<a name="setup-credentials"></a>

To make requests to AWS using the AWS SDK for Java, you must use cryptographically\-signed credentials issued by AWS\. With AWS SDKs and Tools like the AWS SDK for Java, you use a programmatic access key, consisting of an Access Key ID and and a Secret Access Key, as credentials\. You should set your credentials as the default credentials for accessing AWS with your application\.

If you already have an IAM account created, see [Create an IAM user and programmatic access key](#setup-iamuser) for instructions on creating a programmatic access key\.

You should also set a default AWS Region for accessing AWS with your application\. Some operations require a Region to be set\. For the best network performance, you can select a Region that is geographically near to you or your customers\.

The most common way to set the default credentials and AWS Region is to use the shared `config` and `credentials` files\. You can also set the default credentials and Region using environment variables, using Java system properties or, for your applications running on Amazon EC2, using [ContainerCredentialsProvider](http://amazonaws.com/java/api/latest/software/amazon/awssdk/auth/credentials/ContainerCredentialsProvider.html) or [InstanceProfileCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/InstanceProfileCredentialsProvider.html)\.

### Setting the default credentials<a name="setting-the-default-credentials"></a>

Select one of these options to set the default credentials:
+ Set credentials in the AWS credentials profile file on your local system, located at:
  +  `~/.aws/credentials` on Linux, macOS, or Unix
  +  `C:\Users\USERNAME\.aws\credentials` on Windows

This file should contain lines in the following format:

```
[default]
aws_access_key_id = your_access_key_id
aws_secret_access_key = your_secret_access_key
```

Substitute your own AWS credentials values for the values *your\_access\_key\_id* and *your\_secret\_access\_key*\.
+ Set the `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` environment variables\.

To set these variables on Linux, macOS, or Unix, use ** `export` **:

```
export AWS_ACCESS_KEY_ID=your_access_key_id
export AWS_SECRET_ACCESS_KEY=your_secret_access_key
```

To set these variables on Windows, use ** `set` **:

```
set AWS_ACCESS_KEY_ID=your_access_key_id
set AWS_SECRET_ACCESS_KEY=your_secret_access_key
```
+ For an Amazon EC2 instance, specify an IAM role and then give your Amazon EC2 instance access to that role\. See [IAM Roles for Amazon EC2](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/iam-roles-for-amazon-ec2.html) in the Amazon EC2 User Guide for Linux Instances for a detailed discussion about how this works\.
+ Set the `aws.accessKeyId` and `aws.secretAccessKey` Java system properties\.

```
java app.jar -Daws.accessKeyId=\
"your_access_key_id" \
-Daws.secretAccessKey=\
"your_secret_access_key"
```

### Setting the default Region<a name="setting-the-default-aws-region"></a>

Select one of these options to set the default Region:
+ Set the AWS Region in the AWS config file on your local system, located at:
  + \~/\.aws/config on Linux, macOS, or Unix
  + C:\\Users\\USERNAME\\\.aws\\config on Windows

This file should contain lines in the following format:

```
[default]
region = your_aws_region
```

Substitute your desired AWS Region \(for example, "us\-east\-1"\) for *your\_aws\_region*\.
+ Set the `AWS_REGION` environment variable\.

On Linux, macOS, or Unix, use ** `export` **:

```
export AWS_REGION=your_aws_region
```

On Windows, use ** `set` **:

```
set AWS_REGION=your_aws_region
```

Where *your\_aws\_region* is the desired AWS Region name\.

For additional information about setting credentials and Region, see [The \.aws/credentials and \.aws/config files](https://docs.aws.amazon.com/sdkref/latest/guide/creds-config-files.html), [AWS Region](https://docs.aws.amazon.com/sdkref/latest/guide/setting-global-region.html), and [Using environment variables](https://docs.aws.amazon.com/sdkref/latest/guide/environment-variables.html) in the [AWS SDKs and Tools Reference Guide](https://docs.aws.amazon.com/sdkref/latest/guide)\.

## Install Java and a build tool<a name="setup-envtools"></a>

Your development environment needs the following:
+ Java 8 or later\. The AWS SDK for Java works with the [Oracle Java SE Development Kit](https://www.oracle.com/java/technologies/javase-downloads.html) and with distributions of Open Java Development Kit \(OpenJDK\) such as [Amazon Corretto](http://aws.amazon.com/corretto/), [Red Hat OpenJDK](https://developers.redhat.com/products/openjdk), and [AdoptOpenJDK](https://adoptopenjdk.net/)\.
+ A build tool or IDE that supports Maven Central such as Apache Maven, Gradle, or IntelliJ\.
  + For information about how to install and use Maven, see [http://maven\.apache\.org/](http://maven.apache.org/)\.
  + For information about how to install and use Gradle, see [https://gradle\.org/](https://gradle.org/)\.
  + For information about how to install and use IntelliJ IDEA, see [https://www\.jetbrains\.com/idea/](https://www.jetbrains.com/idea/)\.

## Next steps<a name="setup-nextsteps"></a>

Once you have your AWS account and development environment set up, create a Java project using your preferred build tool\. Check [the Maven bill of materials \(BOM\) for the AWS SDK for Java 2\.x from Maven Central](https://mvnrepository.com/artifact/software.amazon.awssdk/bom/latest), for the latest version\. Use that version number in the `<dependencyManagement>` section below\. Then add dependencies for the services you’ll use in your application\.

Example Maven `pom.xml` file:

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
    <version>2.17.261</version>
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
   <artifactId>dynamodb</artifactId>
  </dependency>
  <dependency>
   <groupId>software.amazon.awssdk</groupId>
   <artifactId>iam</artifactId>
  </dependency>
  <dependency>
   <groupId>software.amazon.awssdk</groupId>
   <artifactId>kinesis</artifactId>
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

Example `build.gradle` file:

```
group 'com.example.myapp'
version '1.0'

apply plugin: 'java'

sourceCompatibility = 1.8

repositories {
 mavenCentral()
}

dependencies {
 implementation platform('software.amazon.awssdk:bom:2.15.0')
 implementation 'software.amazon.awssdk:dynamodb'
 implementation 'software.amazon.awssdk:iam'
 implementation 'software.amazon.awssdk:kinesis'
 implementation 'software.amazon.awssdk:s3'
 testImplementation group: 'junit', name: 'junit', version: '4.11'
}
```

For more information, see [Setting up an Apache Maven project](setup-project-maven.md) or [Setting up a Gradle project](setup-project-gradle.md)\.