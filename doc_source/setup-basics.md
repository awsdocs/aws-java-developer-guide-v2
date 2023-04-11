# Basic setup to work with AWS services<a name="setup-basics"></a>

## Overview<a name="setup-overview"></a>

To successfully develop applications that access AWS services using the AWS SDK for Java, the following conditions are required:
+ You must be able to [sign in to the AWS access portal](#setup-awsaccount) available in the AWS IAM Identity Center \(successor to AWS Single Sign\-On\)\.
+ The [permissions of the IAM role](https://docs.aws.amazon.com/singlesignon/latest/userguide/permissionsetsconcept.html) configured for the SDK must allow access to the AWS services that your application requires\. The permissions associated with the **PowerUserAccess** AWS managed policy are sufficient for most development needs\.
+ A development environment with the following elements:
  + [Shared configuration files](https://docs.aws.amazon.com/sdkref/latest/guide/file-format.html) that are set up in at least one of the following ways:
    + The `config` file contains [IAM Identity Center single sign\-on settings](#setup-credentials) so that the SDK can get AWS credentials\.
    + The `credentials` file contains temporary credentials\.
  + An [installation of Java 8](#setup-envtools) or later\.
  + A [build automation tool ](#setup-envtools)such as [Maven](https://maven.apache.org/download.cgi) or [Gradle](https://gradle.org/install/)\.
  + A text editor to work with code\.
  + \(Optional, but recommended\) An IDE \(integrated development environment\) such as [IntelliJ IDEA](https://www.jetbrains.com/idea/download/#section=windows), [Eclipse](https://www.eclipse.org/ide/), or [NetBeans](https://netbeans.org/downloads/)\.

    When you use an IDE, you can also integrate AWS Toolkits to more easily work with AWS services\. The [AWS Toolkit for IntelliJ](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html) and [AWS Toolkit for Eclipse](https://docs.aws.amazon.com/toolkit-for-eclipse/v1/user-guide/welcome.html) are two toolkits that you can use for Java development\.
+ An active AWS access portal session when you are ready to run your application\. You use the AWS Command Line Interface to [initiate the sign\-in process](#setup-login-sso) to IAM Identity Center's AWS access portal\.

**Important**  
The instructions in this setup section assume that you or organization uses IAM Identity Center\. If your organization uses an external identity provider that works independently of IAM Identity Center, find out how you can get temporary credentials for the SDK for Java to use\. Follow [these instructions](credentials-temporary.md#credentials-temporary-from-portal) to add temporary credentials to the `~/.aws/credentials` file\.  
If your identity provider adds temporary credentials automatically to the `~/.aws/credentials` file, make sure that the profile name is `[default]` so that you do not need to provide a profile name to the SDK or AWS CLI\.

## Sign\-in ability to the AWS access portal<a name="setup-awsaccount"></a>

The AWS access portal is the web location where you manually sign in to the IAM Identity Center\. The format of the URL is `d-xxxxxxxxxx.awsapps.com/start`or `your_subdomain.awsapps.com/start`\. If you are not familiar with the AWS access portal, follow the guidance for account access in the [IAM Identity Center authentication](https://docs.aws.amazon.com/sdkref/latest/guide/access-sso.html) topic in the AWS SDKs and Tools Reference Guide\.

## Set up single sign\-on access for the SDK<a name="setup-credentials"></a>

After you complete Step 2 in the [programmatic access section](https://docs.aws.amazon.com/sdkref/latest/guide/access-sso.html#idcGettingStarted) in order for the SDK to use IAM Identity Center authentication, your system should contain the following elements\.
+ The AWS CLI, which you use to start an [AWS access portal session](#setup-login-sso) before you run your application\.
+ An `~/.aws/config` file that contains a [default profile](https://docs.aws.amazon.com/sdkref/latest/guide/file-format.html#file-format-profile)\. The SDK for Java uses the profile's SSO token provider configuration to acquire credentials before sending requests to AWS\. The `sso_role_name` value, which is an IAM role connected to an IAM Identity Center permission set, should allow access to the AWS services used in your application\.

  The following sample `config` file shows a default profile set up with SSO token provider configuration\. The profile's `sso_session` setting refers to the named `sso-session` section\. The `sso-session` section contains settings to initiate an AWS access portal session\.

  ```
  [default]
  sso_session = my-sso
  sso_account_id = 111122223333
  sso_role_name = SampleRole
  region = us-east-1
  output = json
  
  [sso-session my-sso]
  sso_region = us-east-1
  sso_start_url = https://provided-domain.awsapps.com/start
  sso_registration_scopes = sso:account:access
  ```

For more details about the settings used in the SSO token provider configuration, see [SSO token provider configuration](https://docs.aws.amazon.com/sdkref/latest/guide/feature-sso-credentials.html#sso-token-config) in the AWS SDKs and Tools Reference Guide\.

If your development environment is not set up for programmatic access as previously shown, follow [Step 2 in the SDKs Reference Guide](https://docs.aws.amazon.com/sdkref/latest/guide/access-sso.html#idcGettingStarted)\.

## Sign in using the AWS CLI<a name="setup-login-sso"></a>

Before running an application that accesses AWS services, you need an active AWS access portal session in order for the SDK to use IAM Identity Center authentication to resolve credentials\. Run the following command in the AWS CLI to sign in to the AWS access portal\.

```
aws sso login
```

Since you have a default profile setup, you do not need to call the command with a `--profile` option\. If your SSO token provider configuration is using a named profile, the command is `aws sso login --profile named-profile`\.

To test if you already have an active session, run the following AWS CLI command\.

```
aws sts get-caller-identity
```

The response to this command should report the IAM Identity Center account and permission set configured in the shared `config` file\.

**Note**  
If you already have an active AWS access portal session and run `aws sso login`, you will not be required to provide credentials\.   
However, you will see a dialog that requests permission for `botocore` to access your information\. `botocore` is the foundation for the AWS CLI \.   
Select **Allow** to authorize access to your information for the AWS CLI and SDK for Java\.

## Install Java and a build tool<a name="setup-envtools"></a>

Your development environment needs the following:
+ Java 8 or later\. The AWS SDK for Java works with the [Oracle Java SE Development Kit](https://www.oracle.com/java/technologies/javase-downloads.html) and with distributions of Open Java Development Kit \(OpenJDK\) such as [Amazon Corretto](http://aws.amazon.com/corretto/), [Red Hat OpenJDK](https://developers.redhat.com/products/openjdk), and [AdoptOpenJDK](https://adoptopenjdk.net/)\.
+ A build tool or IDE that supports Maven Central such as Apache Maven, Gradle, or IntelliJ\.
  + For information about how to install and use Maven, see [http://maven\.apache\.org/](http://maven.apache.org/)\.
  + For information about how to install and use Gradle, see [https://gradle\.org/](https://gradle.org/)\.
  + For information about how to install and use IntelliJ IDEA, see [https://www\.jetbrains\.com/idea/](https://www.jetbrains.com/idea/)\.

## Additional authentication options<a name="setup-additional"></a>

For more options on authentication for the SDK, such as the use of profiles and environment variables, see the [configuration](https://docs.aws.amazon.com/sdkref/latest/guide/creds-config-files.html) chapter in the AWS SDKs and Tools Reference Guide\.