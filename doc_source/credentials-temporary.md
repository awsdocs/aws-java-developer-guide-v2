# Use temporary credentials<a name="credentials-temporary"></a>

For increased security, AWS recommends that you configure the SDK for Java to [use temporary credentials](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html#bp-users-federation-idp) instead of long\-lived credentials\. Temporary credentials consist of access keys \(access key id and secret access key\) and a session token\. We recommend that you [configure the SDK](#credentials-temporary-idc) to automatically get temporary credentials, since the token refresh process is automatic\. You can, however, [provide the SDK with temporary credentials](#credentials-temporary-from-portal) directly\.

## IAM Identity Center configuration<a name="credentials-temporary-idc"></a>

When you configure the SDK to use IAM Identity Center single sign\-on access as described in [Basic setup to work with AWS services](setup-basics.md) in this guide, the SDK automatically uses temporary credentials\. 

The SDK uses the IAM Identity Center access token to gain access to the IAM role that is configured with the `sso_role_name` setting in your `config` file\. The SDK assumes this IAM role and retrieves temporary credentials to use for AWS service requests\.

For more details about how the SDK gets temporary credentials from the configuration, see the [Understanding IAM Identity Center authentication](https://docs.aws.amazon.com/sdkref/latest/guide/understanding-sso.html) section of the AWS SDKs and Tools Reference Guide\.

## Retrieve from AWS access portal<a name="credentials-temporary-from-portal"></a>

As an alternative to IAM Identity Center single sign\-on configuration, you can copy and use temporary credentials available in the AWS access portal\. You can use the temporary credentials in a profile or use them as values for system properties and environment variables\.

**Set up a local credentials file for temporary credentials**

1. [Create a shared credentials file](https://docs.aws.amazon.com/sdkref/latest/guide/file-location.html)

1. In the credentials file, paste the following placeholder text until you paste in working temporary credentials\.

   ```
   [default]
   aws_access_key_id=<value from AWS access portal>
   aws_secret_access_key=<value from AWS access portal>
   aws_session_token=<value from AWS access portal>
   ```

1. Save the file\. The file `~/.aws/credentials` should now exist on your local development system\. This file contains the [\[default\] profile](https://docs.aws.amazon.com/sdkref/latest/guide/file-format.html#file-format-profile) that the SDK for Java uses if a specific named profile is not specified\. 

1. [Sign in to the AWS access portal](https://docs.aws.amazon.com/singlesignon/latest/userguide/howtosignin.html)

1. Follow [these instructions](https://docs.aws.amazon.com/singlesignon/latest/userguide/howtogetcredentials.html) to copy IAM role credentials from the AWS access portal\.
**Note**  
Although the title of the page reads *Getting IAM role credentials for CLI access*, the instructions apply to the SDK for Java and also the AWS Command Line Interface \(AWS CLI\)\.

   1. For step 2 in the instructions, choose the AWS account and IAM role name that grants access for your development needs\. This role typically has a name like **PowerUser** or **Developer**\.

   1. For step 4, select the option that reads **Add a profile to your AWS credentials file** and copy the contents\.

1. Paste the copied credentials into your local `credentials` file and remove the generated profile name\. Your file should resemble the following\.

   ```
   [default]
   aws_access_key_id=AKIAIOSFODNN7EXAMPLE
   aws_secret_access_key=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
   aws_session_token=IQoJb3JpZ2luX2IQoJb3JpZ2luX2IQoJb3JpZ2luX2IQoJb3JpZ2luX2IQoJb3JpZVERYLONGSTRINGEXAMPLE
   ```

1. Save the `credentials` file\.

When the SDK for Java creates a service client, it will access these temporary credentials and use them for each request\. The settings for the IAM role chosen in step 5a determine [how long the temporary credentials are valid](https://docs.aws.amazon.com/singlesignon/latest/userguide/howtosessionduration.html)\. The maximum duration is twelve hours\.

After the temporary credentials expire, repeat steps 4 through 7\.