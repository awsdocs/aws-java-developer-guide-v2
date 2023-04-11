# Use a specific credentials provider or provider chain<a name="credentials-specify"></a>

As an alternative to the default credentials provider chain, you can specify which credentials provider the SDK should use\. When you supply a specific credentials provider, the SDK skips the process of checking various locations, which slightly reduces the time to create a service client\.

For example, if you set your default configuration using environment variables, supply an [EnvironmentVariableCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/EnvironmentVariableCredentialsProvider.html) object to the `credentialsProvider` method on the service client builder, as in the following code snippet\.

```
Region region = Region.US_WEST_2;
DynamoDbClient ddb = DynamoDbClient.builder()
      .region(region)
      .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
      .build();
```

For a complete list of credential providers and provider chains, see **All Known Implementing Classes** in [AwsCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/AwsCredentialsProvider.html)\.

**Note**  
You can use your own credentials provider or provider chains by implementing the `AwsCredentialsProvider` interface\.