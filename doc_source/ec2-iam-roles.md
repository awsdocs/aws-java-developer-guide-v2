# Configuring IAM roles for Amazon EC2<a name="ec2-iam-roles"></a>

  You can use an IAM role to manage temporary credentials for applications that are running on an EC2 instance and making AWS CLI or AWS API requests\. This is preferable to storing access keys within the EC2 instance\. To assign an AWS role to an EC2 instance and make it available to all of its applications, you create an instance profile that is attached to the instance\. An instance profile contains the role and enables programs that are running on the EC2 instance to get temporary credentials\. For more information, see [Using an IAM role to grant permissions to applications running on Amazon EC2 instances](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_switch-role-ec2.html) in the *IAM User Guide*\. 

This topic provides information about how to use IAM roles with AWS SDK for Java applications running on Amazon EC2\.

## Default provider chain and Amazon EC2 instance profiles<a name="default-provider-chain"></a>

If your application creates an AWS client using the `create` method, the client searches for temporary credentials using the *default credentials provider chain*, as described in the [Default credentials provider chain](credentials-chain.md) section \.

The final step in the default provider chain is available only when running your application on an Amazon EC2 instance\. However, it provides better ease of use and security when working with Amazon EC2 instances\. You can also pass an [InstanceProfileCredentialsProvider](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/auth/credentials/InstanceProfileCredentialsProvider.html) instance directly to the client constructor to get instance profile credentials without going through the entire default provider chain\.

This is demonstrated in the following example:

```
S3Client s3 = S3Client.builder()
       .credentialsProvider(InstanceProfileCredentialsProvider.builder().build())
       .build();
```

When you use this approach, the SDK retrieves temporary AWS credentials that have the same permissions as those associated with the IAM role that is associated with the Amazon EC2 instance in its instance profile\. Although these credentials are temporary and would eventually expire, `InstanceProfileCredentialsProvider` periodically refreshes them for you so that they continue to allow access to AWS\.

## Walkthrough: Using IAM roles for EC2 instances<a name="roles-walkthrough"></a>

This walkthrough shows you how to retrieve an object from Amazon S3 using an IAM role to manage access\.

### Create an IAM role<a name="java-dg-create-the-role"></a>

Create an IAM role that grants read\-only access to Amazon S3\.

1. Open the [IAM console](https://console.aws.amazon.com/iam/home)\.

1. In the navigation pane, choose **Roles**, then **Create Role**\.

1. On the **Select trusted entity** page, choose **AWS service**\. Under **Use case**, choose **EC2**\. Choose **Next**\.

1. On the **Add permissions** page, choose **Amazon S3ReadOnlyAccess** from the permissions policy list, then choose **Next**\.

   Enter a name for the role, then select **Create role**\. Note this name\. You need it to launch your Amazon EC2 instance\.

### Launch an EC2 instance and specify your IAM role<a name="java-dg-launch-ec2-instance-with-instance-profile"></a>

To launch an Amazon EC2 instance with an IAM role using the console, follow the instructions in [the Amazon EC2 User Guide for Linux Instances](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/iam-roles-for-amazon-ec2.html#working-with-iam-roles)\.

**Note**  
You need to create or use an existing security group and key pair to connect to the instance\.

With this IAM and Amazon EC2 setup, you can deploy your application to the Amazon EC2 instance and it will have read access to Amazon S3\.