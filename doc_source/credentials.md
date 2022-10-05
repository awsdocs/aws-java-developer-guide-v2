--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Using credentials<a name="credentials"></a>

To make requests to Amazon Web Services using the AWS SDK for Java, you must use cryptographically\-signed credentials issued by AWS\. You can use programmatic access keys or temporary security credentials such as AWS IAM Identity Center \(successor to AWS Single Sign\-On\) or IAM roles to grant access to AWS resources\.

For information on setting up credentials, see [Set default credentials and Region](setup.md#setup-credentials) and [Set up credentials profiles](setup-additional.md#setup-additional-credentials)\.

**Topics**
+ [Use the default credential provider chain](#credentials-chain)
+ [Use a specific credentials provider or provider chain](#credentials-specify)
+ [Use credentials profiles](#credentials-profiles)
+ [Supply credentials explicitly](#credentials-explicit)
+ [Configuring IAM roles for Amazon EC2](ec2-iam-roles.md)

## Use the default credential provider chain<a name="credentials-chain"></a>

After you [Set default credentials and Region](setup.md#setup-credentials) for your environment, the AWS SDK for Java will automatically use those credentials when your application makes requests to AWS\. The default credential provider chain, implemented by the [DefaultCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/DefaultCredentialsProvider.html) class, checks sequentially each of places where you can set default credentials and selects the first one you set\.

To use the default credential provider chain to supply credentials in your application, create a service client builder without specifying credentials provider configuration\.

```
Region region = Region.US_WEST_2;
DynamoDbClient ddb = DynamoDbClient.builder()
      .region(region)
      .build();
```

### Credential retrieval order<a name="credentials-default"></a>

The default credential provider chain of the AWS SDK for Java 2\.x searches for credentials in your environment using a predefined sequence\.

1. Java system properties
   + The SDK uses the [SystemPropertyCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/SystemPropertyCredentialsProvider.html) class to load credentials from the `aws.accessKeyId` and `aws.secretAccessKey` Java system properties\. If `aws.sessionToken` is also specified, the SDK will use temporary credentials\.
**Note**  
For information on how to set Java system properties, see the [System Properties](https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html) tutorial on the official *Java Tutorials* website\.

1. Environment variables
   + The SDK uses the [EnvironmentVariableCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/EnvironmentVariableCredentialsProvider.html) class to load credentials from the `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` system environment variables\. If `AWS_SESSION_TOKEN` is also specified, the SDK will use temporary credentials\.

1. Web identity token from AWS STS 
   + The SDK uses the [WebIdentityTokenFileCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/WebIdentityTokenFileCredentialsProvider.html) class to load credentials from Java system properties or environment variables\.

1. The shared `credentials` and `config` files
   + The SDK uses the [ProfileCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/ProfileCredentialsProvider.html) to load credentials from the `[default]` credentials profile in the shared `credentials` and `config` files\.
**Note**  
The `credentials` and `config` files are shared by various AWS SDKs and Tools\. For more information, see [The \.aws/credentials and \.aws/config files](https://docs.aws.amazon.com/sdkref/latest/guide/creds-config-files.html) in the [AWS SDKs and Tools Reference Guide](https://docs.aws.amazon.com/sdkref/latest/guide)\.

1.  Amazon ECS container credentials
   + The SDK uses the [ContainerCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/ContainerCredentialsProvider.html) class to load credentials from the `AWS_CONTAINER_CREDENTIALS_RELATIVE_URI` system environment variable\.

1.  Amazon EC2 instance profile credentials
   + The SDK uses the [InstanceProfileCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/InstanceProfileCredentialsProvider.html) class to load credentials from the Amazon EC2 metadata service\.

## Use a specific credentials provider or provider chain<a name="credentials-specify"></a>

Alternatively, you can specify which credentials provider the SDK should use\. For example, if you set your default credentials using environment variables, supply an [EnvironmentVariableCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/EnvironmentVariableCredentialsProvider.html) object to the `credentialsProvider` method on the service client builder, as in the following code snippet\.

```
Region region = Region.US_WEST_2;
DynamoDbClient ddb = DynamoDbClient.builder()
      .region(region)
      .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
      .build();
```

For a complete list of credential providers and provider chains, see **All Known Implementing Classes** in [AwsCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/AwsCredentialsProvider.html)\.

**Note**  
You can use your own credential provider or provider chains by implementing the `AwsCredentialsProvider` interface\.

## Use credentials profiles<a name="credentials-profiles"></a>

Using the shared `credentials` file, you can set up custom profiles which enables you to use multiple sets of credentials in your application\. The `[default]` profile was mentioned above\. The SDK uses the [ProfileCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/ProfileCredentialsProvider.html) class to load credentials from profiles defined in the shared `credentials` file\.

For information on how to set up custom profiles, see [Set up credentials profiles](setup-additional.md#setup-additional-credentials)\.

This code snippet demonstrates how to build a service client that uses the credentials defined as part of the `profile_name` profile\.

```
Region region = Region.US_WEST_2;
DynamoDbClient ddb = DynamoDbClient.builder()
      .region(region)
      .credentialsProvider(ProfileCredentialsProvider.create("profile_name"))
      .build();
```

### Set a custom profile as the default<a name="set-a-custom-profile-as-the-default"></a>

To set a profile other than the `[default]` profile as the default for your application, set the `AWS_PROFILE` environment variable to the name of your custom profile\.

To set this variable on Linux, macOS, or Unix, use `export`:

```
export AWS_PROFILE="other_profile"
```

To set these variables on Windows, use `set`:

```
set AWS_PROFILE="other_profile"
```

Alternatively, set the `aws.profile` Java system property to the name of the profile\.

## Supply credentials explicitly<a name="credentials-explicit"></a>

If the default credential chain or a specific or custom provider or provider chain doesn’t work for your application, you can supply the credentials that you want directly in code\. These can be AWS account credentials, IAM credentials, or temporary credentials retrieved from AWS Security Token Service \(AWS STS\)\. If you’ve retrieved temporary credentials using AWS STS, use this method to specify the credentials for AWS access\.

**Important**  
For security, use * IAM account credentials* instead of the AWS account credentials when accessing AWS\. For more information, see [AWS Security Credentials](https://docs.aws.amazon.com/general/latest/gr/aws-security-credentials.html) in the Amazon Web Services General Reference\.

1. Instantiate a class that provides the [AwsCredentials](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/AwsCredentials.html) interface, such as [AwsSessionCredentials](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/AwsSessionCredentials.html)\. Supply it with the AWS access key and secret key to use for the connection\.

1. Create a [StaticCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/StaticCredentialsProvider.html) object and supply it with the `AwsCredentials` object\.

1. Configure the service client builder with the `StaticCredentialsProvider` and build the client\.

The following example creates a new service client using credentials that you supply:

```
AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
  "your_access_key_id",
  "your_secret_access_key");

S3Client s3 = S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
            .build();
```