# Use profiles<a name="credentials-profiles"></a>

Using the shared `config` and `credentials` file, you can set up several profiles\. This enables your application to use multiple sets of credentials configuration\. The `[default]` profile was mentioned previously\. The SDK uses the [ProfileCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/ProfileCredentialsProvider.html) class to load settings from profiles defined in the shared `credentials` file\.

The following code snippet demonstrates how to build a service client that uses the settings defined as part of the profile named `my_profile`\.

```
Region region = Region.US_WEST_2;
DynamoDbClient ddb = DynamoDbClient.builder()
      .region(region)
      .credentialsProvider(ProfileCredentialsProvider.create("my_profile"))
      .build();
```

## Set a different profile as the default<a name="set-a-custom-profile-as-the-default"></a>

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

## Reload profile credentials<a name="profile-reloading"></a>

You can configure any credentials provider that has a `profileFile()` method on its builder to reload profile credentials\. These credentials profile classes are: `ProfileCredentialsProvider`, `DefaultCredentialsProvider`, `InstanceProfileCredentialsProvider`, and `ProfileTokenProvider.`

**Note**  
Profile credential reloading works only with the following settings in the profile file : `aws_access_key_id`, `aws_secret_access_key`, and `aws_session_token`\.  
Settings such as `region`, `sso_session`, `sso_account_id`, and `source_profile` are ignored\.

To configure a supported credentials provider to reload profile settings, provide an instance of [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/profiles/ProfileFileSupplier.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/profiles/ProfileFileSupplier.html) to the `profileFile()` builder method\. The following code example demonstrates a `ProfileCredentialsProvider` that reloads credential settings from the `[default]` profile\.

```
ProfileCredentialsProvider provider = ProfileCredentialsProvider
    .builder()
    .profileFile(ProfileFileSupplier.defaultSupplier())
    .build();

// Set up a service client with the provider instance.
DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                    .region(Region.US_EAST_1)
                    .credentialsProvider(provider)
                    .build();

/*
    Before dynamoDbClient makes a request, it reloads the credentials settings 
    by calling provider.resolveCredentials().
*/
```

When `ProfileCredentialsProvider.resolveCredentials()` is called, the SDK for Java reloads the settings\. `ProfileFileSupplier.defaultSupplier()` is one of [several convenience implementations](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/profiles/ProfileFileSupplier.html) of `ProfileFileSupplier` provided by the SDK\. If your use case requires, you can provide your own implementation\.

The following example shows the use of the `ProfileFileSupplier.reloadWhenModified()` convenience method\. `reloadWhenModified()` takes a `Path` parameter, which gives you flexibility in designating the source file for the configuration rather than the standard `~/.aws/credentials` \(or `config`\) location\.

The settings will be reloaded when `resolveCredentials()` is called only if SDK determines the file's content has been modified\.

```
Path credentialsFilePath = ...

ProfileCredentialsProvider provider = ProfileCredentialsProvider
    .builder()
    .profileFile(ProfileFileSupplier.reloadWhenModified(credentialsFilePath, ProfileFile.Type.CREDENTIALS))
    .profileName("my-profile")
    .build();
/*
    A service client configured with the provider instance calls provider.resolveCredential()
    before each request.
*/
```

The `ProfileFileSupplier.aggregate()` method merges the contents of multiple configuration files\. You decide whether a file is reloaded per call to `resolveCredentials()` or a file's settings are fixed at the time it was first read\. 

The following example shows a `DefaultCredentialsProvider` that merges the settings of two files that contain profile settings\. The SDK reloads the settings in the file pointed to by the `credentialsFilePath` variable each time `resolveCredentials()` is called and the settings have changed\. The settings from the `profileFile` object remain the same\.

```
Path credentialsFilePath = ...;
ProfileFile profileFile = ...;

DefaultCredentialsProvider provider = DefaultCredentialsProvider
        .builder()
        .profileFile(ProfileFileSupplier.aggregate(
                ProfileFileSupplier.reloadWhenModified(credentialsFilePath, ProfileFile.Type.CREDENTIALS),
                ProfileFileSupplier.fixedProfileFile(profileFile)))
        .profileName("my-profile")
        .build();
/*
    A service client configured with the provider instance calls provider.resolveCredential()
    before each request.
*/
```