# AWS region selection<a name="region-selection"></a>

Regions enable you to access AWS services that physically reside in a specific geographic area\. This can be useful both for redundancy and to keep your data and applications running close to where you and your users will access them\.

In AWS SDK for Java 2\.x, all the different region related classes from version 1\.x have been collapsed into one Region class\. You can use this class for all region\-related actions such as retrieving metadata about a region or checking whether a service is available in a region\.

## Choosing a region<a name="region-selection-choose-region"></a>

You can specify a region name and the SDK will automatically choose an appropriate endpoint for you\.

To explicitly set a region, we recommend that you use the constants defined in the [Region](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/regions/Region.html) class\. This is an enumeration of all publicly available regions\. To create a client with a region from the class, use the following code\.

```
Ec2Client ec2 = Ec2Client.builder()
          .region(Region.US_WEST_2)
          .build();
```

If the region you are attempting to use isn’t one of the constants in the `Region` class, you can create a new region using the `of` method\. This feature allows you access to new Regions without upgrading the SDK\.

```
Region newRegion = Region.of("us-east-42");
Ec2Client ec2 = Ec2Client.builder()
          .region(newRegion)
          .build();
```

**Note**  
After you build a client with the builder, it’s *immutable* and the region *cannot be changed*\. If you are working with multiple AWS Regions for the same service, you should create multiple clients—​one per region\.

## Choosing a specific endpoint<a name="choosing-a-specific-endpoint"></a>

Each AWS client can be configured to use a *specific endpoint* within a region by calling the `endpointOverride` method\.

For example, to configure the Amazon EC2 client to use the Europe \(Ireland\) Region, use the following code\.

```
Ec2Client ec2 = Ec2Client.builder()
               .region(Region.EU_WEST_1)
               .endpointOverride(URI.create("https://ec2.eu-west-1.amazonaws.com"))
               .build();
```

See [Regions and Endpoints](https://docs.aws.amazon.com/general/latest/gr/rande.html) for the current list of regions and their corresponding endpoints for all AWS services\.

## Automatically determine the Region from the environment<a name="automatically-determine-the-aws-region-from-the-environment"></a>

When running on Amazon EC2 or AWS Lambda, you might want to configure clients to use the same region that your code is running on\. This decouples your code from the environment it’s running in and makes it easier to deploy your application to multiple regions for lower latency or redundancy\.

To use the default credential/region provider chain to determine the region from the environment, use the client builder’s `create` method\.

```
Ec2Client ec2 = Ec2Client.create();
```

If you don’t explicitly set a region using the `region` method, the SDK consults the default region provider chain to try and determine the region to use\.

### Default region provider chain<a name="default-region-provider-chain"></a>

 **The following is the region lookup process:** 

1. Any explicit region set by using `region` on the builder itself takes precedence over anything else\.

1. The `AWS_REGION` environment variable is checked\. If it’s set, that region is used to configure the client\.
**Note**  
This environment variable is set by the Lambda container\.

1. The SDK checks the AWS shared configuration file \(usually located at `~/.aws/config`\)\. If the *region* property is present, the SDK uses it\.
   + The `AWS_CONFIG_FILE` environment variable can be used to customize the location of the shared config file\.
   + The `AWS_PROFILE` environment variable or the `aws.profile` system property can be used to customize the profile that the SDK loads\.

1. The SDK attempts to use the Amazon EC2 instance metadata service to determine the region of the currently running Amazon EC2 instance\.

1. If the SDK still hasn’t found a region by this point, client creation fails with an exception\.

When developing AWS applications, a common approach is to use the *shared configuration file* \(described in [Credential retrieval order](credentials-chain.md#credentials-default)\) to set the region for local development, and rely on the default region provider chain to determine the region when running on AWS infrastructure\. This greatly simplifies client creation and keeps your application portable\.

## Checking for service availability in a Region<a name="region-selection-query-service"></a>

To see if a particular AWS service is available in a region, use the `serviceMetadata` and `region` method on the service that you’d like to check\.

```
DynamoDbClient.serviceMetadata().regions().forEach(System.out::println);
```

See the [Region](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/regions/Region.html) class documentation for the regions you can specify, and use the endpoint prefix of the service to query\.