# Credentials provider changes<a name="migration-client-credentials"></a>

## Credentials provider<a name="client-credentials"></a>

This section provides a mapping of the name changes of credential provider classes and methods between versions 1\.x and 2\.x of the AWS SDK for Java\. The following also lists some of the key differences in the way credentials are processed by the SDK in version 2\.x:
+ The default credentials provider loads system properties before environment variables in version 2\.x\. For more information, see [Using credentials](credentials.md)\.
+ The constructor method is replaced with the `create` or `builder` methods\.  
**Example**  

  ```
  DefaultCredentialsProvider.create();
  ```
+ Asynchronous refresh is no longer set by default\. You must specify it with the `builder` of the credentials provider\.  
**Example**  

  ```
  ContainerCredentialsProvider provider = ContainerCredentialsProvider.builder()
          		.asyncCredentialUpdateEnabled(true)
          		.build();
  ```
+ You can specify a path to a custom profile file using the `ProfileCredentialsProvier.builder()`\.  
**Example**  

  ```
  ProfileCredentialsProvider profile = ProfileCredentialsProvider.builder()
          		.profileFile(ProfileFile.builder().content(Paths.get("myProfileFile.file")).build())
          		.build();
  ```
+ Profile file format has changed to more closely match the AWS CLI\. For details, see [Configuring the AWS CLI](http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html) in the *AWS Command Line Interface User Guide*\.

## Credentials provider changes mapped between versions 1\.x and 2\.x<a name="credentials-changes-mapping"></a>


**Method name changes**  

| 1\.x | 2\.x | 
| --- | --- | 
|   `AWSCredentialsProvider.getCredentials`   |   `AwsCredentialsProvider.resolveCredentials`   | 
|   `DefaultAWSCredentialsProviderChain.getInstance`   |  Not supported  | 
|   `AWSCredentialsProvider.getInstance`   |  Not supported  | 
|   `AWSCredentialsProvider.refresh`   |  Not supported  | 


**Environment variable name changes**  

| 1\.x | 2\.x | 
| --- | --- | 
|   `AWS_ACCESS_KEY`   |   `AWS_ACCESS_KEY_ID`   | 
|   `AWS_SECRET_KEY`   |   `AWS_SECRET_ACCESS_KEY`   | 
|   `AWS_CREDENTIAL_PROFILES_FILE`   |   `AWS_SHARED_CREDENTIALS_FILE`   | 


**System property name changes**  

| 1\.x | 2\.x | 
| --- | --- | 
|   `aws.secretKey`   |   `aws.secretAccessKey`   | 
|   `com.amazonaws.sdk.disableEc2Metadata`   |   `aws.disableEc2Metadata`   | 
|   `com.amazonaws.sdk.ec2MetadataServiceEndpointOverride`   |   `aws.ec2MetadataServiceEndpoint`   | 