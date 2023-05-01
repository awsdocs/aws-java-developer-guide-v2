# Default credentials provider chain<a name="credentials-chain"></a>

The default credentials provider chain is implemented by the [DefaultCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/DefaultCredentialsProvider.html) class\. It sequentially checks each place where you can set the default configuration for supplying temporary credentials, and then selects the first one you set\.

To use the default credentials provider chain to supply temporary credentials that are used in your application, create a service client builder but don't specify a credentials provider\. The following code snippet creates a `DynamoDbEnhancedClient` that uses the default credentials provider chain to locate and retrieve default configuration settings\.

```
Region region = Region.US_WEST_2;
DynamoDbEnhancedClient ddb = 
    DynamoDbEnhancedClient.builder()
                          .region(region)
                          .build();
```

## Credential settings retrieval order<a name="credentials-default"></a>

The default credentials provider chain of the SDK for Java 2\.x searches for configuration in your environment using a predefined sequence\.

1. Java system properties
   + The SDK uses the [SystemPropertyCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/SystemPropertyCredentialsProvider.html) class to load temporary credentials from the `aws.accessKeyId`, `aws.secretAccessKey`, and `aws.sessionToken` Java system properties\.
**Note**  
For information on how to set Java system properties, see the [System Properties](https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html) tutorial on the official *Java Tutorials* website\.

1. Environment variables
   + The SDK uses the [EnvironmentVariableCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/EnvironmentVariableCredentialsProvider.html) class to load temporary credentials from the `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, and `AWS_SESSION_TOKEN` environment variables\.

1. Web identity token from AWS Security Token Service
   + The SDK uses the [WebIdentityTokenFileCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/WebIdentityTokenFileCredentialsProvider.html) class to load temporary credentials from Java system properties or environment variables\.

1. The shared `credentials` and `config` files
   + The SDK uses the [ProfileCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/ProfileCredentialsProvider.html) to load IAM Identity Center single sign\-on settings or temporary credentials from the `[default]` profile in the shared `credentials` and `config` files\. 

     The AWS SDKs and Tools Reference Guide has [detailed information](https://docs.aws.amazon.com/sdkref/latest/guide/understanding-sso.html#idccredres) about how the SDK for Java works with the IAM Identity Center single sign\-on token to get temporary credentials that the SDK uses to call AWS services\.
**Note**  
The `credentials` and `config` files are shared by various AWS SDKs and Tools\. For more information, see [The \.aws/credentials and \.aws/config files](https://docs.aws.amazon.com/sdkref/latest/guide/creds-config-files.html) in the AWS SDKs and Tools Reference Guide\.

1.  Amazon ECS container credentials
   + The SDK uses the [ContainerCredentialsProvider](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/auth/credentials/ContainerCredentialsProvider.html) class to load temporary credentials from the `AWS_CONTAINER_CREDENTIALS_RELATIVE_URI` system environment variable\.

1.  Amazon EC2 instance IAM role\-provided credentials
   + The SDK uses the [InstanceProfileCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/InstanceProfileCredentialsProvider.html) class to load temporary credentials from the Amazon EC2 metadata service\.