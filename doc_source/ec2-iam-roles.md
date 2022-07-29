--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Configuring IAM roles for Amazon EC2<a name="ec2-iam-roles"></a>

All requests to AWS services must be cryptographically signed using credentials issued by AWS \. You can use * IAM roles* to conveniently grant secure access to AWS resources from your Amazon EC2 instances\.

This topic provides information about how to use IAM roles with AWS SDK for Java applications running on Amazon EC2\. For more information about IAM instances, see [IAM Roles for Amazon EC2](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/iam-roles-for-amazon-ec2.html) in the Amazon EC2 User Guide for Linux Instances\.

## Default provider chain and Amazon EC2 instance profiles<a name="default-provider-chain"></a>

If your application creates an AWS client using the `create` method, the client searches for credentials using the *default credentials provider chain*, in the following order:

1. In the Java system properties: `aws.accessKeyId` and `aws.secretAccessKey`\.

1. In system environment variables: `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`\.

1. In the default credentials file \(the location of this file varies by platform\)\.

1. In the Amazon ECS environment variable: `AWS_CONTAINER_CREDENTIALS_RELATIVE_URI`\.

1. In the *instance profile credentials*, which exist within the instance metadata associated with the IAM role for the Amazon EC2 instance\.

The final step in the default provider chain is available only when running your application on an Amazon EC2 instance\. However, it provides the greatest ease of use and best security when working with Amazon EC2 instances\. You can also pass an [InstanceProfileCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/InstanceProfileCredentialsProvider.html) instance directly to the client constructor to get instance profile credentials without proceeding through the entire default provider chain\.

For example:

```
S3Client s3 = S3Client.builder()
       .credentialsProvider(InstanceProfileCredentialsProvider.builder().build())
       .build();
```

When you use this approach, the SDK retrieves temporary AWS credentials that have the same permissions as those associated with the IAM role that is associated with the Amazon EC2 instance in its instance profile\. Although these credentials are temporary and would eventually expire, `InstanceProfileCredentialsProvider` periodically refreshes them for you so that the obtained credentials continue to allow access to AWS\.

## Walkthrough: Using IAM roles for EC2 instances<a name="roles-walkthrough"></a>

This walkthrough shows you how to retrieve an object from Amazon S3 using an IAM role to manage access\.

### Create an IAM role<a name="java-dg-create-the-role"></a>

Create an IAM role that grants read\-only access to Amazon S3\.

1. Open the [IAM console](https://console.aws.amazon.com/iam/home)\.

1. In the navigation pane, choose **Roles**, then **Create New Role**\.

1. On the **Select Role Type** page, under ** AWS service Roles**, choose ** Amazon EC2 **\.

1. On the **Attach Policy** page, choose ** Amazon S3 Read Only Access** from the policy list, then choose **Next Step**\.  
Enter a name for the role, then select **Next Step**\. Remember this name  
   + because you’ll need it when you launch your Amazon EC2 instance\.

1. On the **Review** page, choose **Create Role**\.

### Launch an EC2 instance and specify your IAM role<a name="java-dg-launch-ec2-instance-with-instance-profile"></a>

You can launch an Amazon EC2 instance with an IAM role using the Amazon EC2 console\.

To launch an Amazon EC2 instance using the console, follow the directions in [Getting Started with Amazon EC2 Linux Instances](http://docs.aws.amazon.com/AWSEC2/latest/GettingStartedGuide/) in the Amazon EC2 User Guide for Linux Instances\.

When you reach the **Review Instance Launch** page, select **Edit instance details**\. In ** IAM role**, choose the IAM role that you created previously\. Complete the procedure as directed\.

**Note**  
You need to create or use an existing security group and key pair to connect to the instance\.

With this IAM and Amazon EC2 setup, you can deploy your application to the Amazon EC2 instance and it will have read access to the Amazon S3 service\.