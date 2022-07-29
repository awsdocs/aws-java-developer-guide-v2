--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Additional setup information<a name="setup-additional"></a>

This topic supplements the information in [Setting up the AWS SDK for Java 2\.x](setup.md)\.

## Set up credentials profiles<a name="setup-additional-credentials"></a>

You can use more than one set of credentials in your application by setting up additional credentials profiles\. Like the `[default]` profile, you can set up custom profiles to use programmatic access keys as credentials or to use temporary credentials\.

To configure your own credentials profiles, use the shared `credentials` and `config` files\. See the snippets below for example usage\.

\*A profile,\**cloudwatch\_metrics* **, configured in the** `credentials` **file to use a programmatic access key as credentials:** 

```
[cloudwatch_metrics]
aws_access_key_id = your_access_key_id
aws_secret_access_key = your_secret_access_key
region = us-east-2
```

A profile, *devuser*, configured in the `config` file to use temporary credentials by assuming a role based on an Amazon Resource Name \(ARN\)\.

```
[profile devuser]
role_arn = {region-arn}iam::123456789012:role/developers
source_profile = dev-user
region = {region_api_default}
output = json
```

For an example using single sign\-on \(SSO\) authentication, using AWS IAM Identity Center \(successor to AWS Single Sign\-On\), see [SSO Credentials](https://docs.aws.amazon.com/sdkref/latest/guide/feature-sso-credentials.html) in the *AWS SDKs and Tools Reference Guide*\. In addition to `config` file changes, a dependency must be added to your project’s `pom.xml` file: 

```
<dependency>
   <groupId>software.amazon.awssdk</groupId>
   <artifactId>sso</artifactId>
   <version>xxx</version>
</dependency>
```

For additional information about configuring the shared `credentials` and `config` files, see:
+  [Set default credentials and Region](setup.md#setup-credentials) 
+  [Example config and credentials files](https://docs.aws.amazon.com/sdkref/latest/guide/file-format.html#file-format-example) 
+  [The \.aws/credentials and \.aws/config files](https://docs.aws.amazon.com/sdkref/latest/guide/creds-config-files.html) 
+  [Credentials for an IAM role assumed as an IAM user](https://docs.aws.amazon.com/sdkref/latest/guide/usage-examples-role_from_user.html) 

### Set an alternate credentials file location<a name="set-an-alternate-credentials-file-location"></a>

By default, the AWS SDK for Java looks for the `credentials` file at `~/.aws/credentials`\. To customize the location of the shared `credentials` file, set the `AWS_SHARED_CREDENTIALS_FILE` environment variable to an alternate location\.

To set this variable on Linux, macOS, or Unix, use `export`:

```
export AWS_SHARED_CREDENTIALS_FILE=path/to/credentials_file
```

To set this variable on Windows, use `set`:

```
set AWS_SHARED_CREDENTIALS_FILE=path/to/credentials_file
```