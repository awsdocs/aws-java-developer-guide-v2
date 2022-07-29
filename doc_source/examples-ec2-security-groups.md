--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Work with security groups in Amazon EC2<a name="examples-ec2-security-groups"></a>

## Create a security group<a name="create-a-security-group"></a>

To create a security group, call the Ec2Client’s `createSecurityGroup` method with a [CreateSecurityGroupRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/CreateSecurityGroupRequest.html) that contains the key’s name\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupRequest;
import software.amazon.awssdk.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import software.amazon.awssdk.services.ec2.model.AuthorizeSecurityGroupIngressResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.ec2.model.IpPermission;
import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupResponse;
import software.amazon.awssdk.services.ec2.model.IpRange;
```

 **Code** 

```
            CreateSecurityGroupRequest createRequest = CreateSecurityGroupRequest.builder()
                .groupName(groupName)
                .description(groupDesc)
                .vpcId(vpcId)
                .build();

            CreateSecurityGroupResponse resp= ec2.createSecurityGroup(createRequest);
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/ec2/src/main/java/com/example/ec2/CreateSecurityGroup.java) on GitHub\.

## Configure a security group<a name="configure-a-security-group"></a>

A security group can control both inbound \(ingress\) and outbound \(egress\) traffic to your Amazon EC2 instances\.

To add ingress rules to your security group, use the Ec2Client’s `authorizeSecurityGroupIngress` method, providing the name of the security group and the access rules \([IpPermission](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/IpPermission.html)\) you want to assign to it within an [AuthorizeSecurityGroupIngressRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/AuthorizeSecurityGroupIngressRequest.html) object\. The following example shows how to add IP permissions to a security group\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupRequest;
import software.amazon.awssdk.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import software.amazon.awssdk.services.ec2.model.AuthorizeSecurityGroupIngressResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.ec2.model.IpPermission;
import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupResponse;
import software.amazon.awssdk.services.ec2.model.IpRange;
```

 **Code** 

First, create an Ec2Client

```
        Region region = Region.US_WEST_2;
        Ec2Client ec2 = Ec2Client.builder()
                .region(region)
                .build();
```

Then use the Ec2Client’s `authorizeSecurityGroupIngress` method,

```
            IpRange ipRange = IpRange.builder()
                .cidrIp("0.0.0.0/0").build();

            IpPermission ipPerm = IpPermission.builder()
                .ipProtocol("tcp")
                .toPort(80)
                .fromPort(80)
                .ipRanges(ipRange)
                .build();

            IpPermission ipPerm2 = IpPermission.builder()
                .ipProtocol("tcp")
                .toPort(22)
                .fromPort(22)
                .ipRanges(ipRange)
                .build();

            AuthorizeSecurityGroupIngressRequest authRequest =
                AuthorizeSecurityGroupIngressRequest.builder()
                        .groupName(groupName)
                        .ipPermissions(ipPerm, ipPerm2)
                        .build();

            AuthorizeSecurityGroupIngressResponse authResponse =
            ec2.authorizeSecurityGroupIngress(authRequest);

            System.out.printf(
                "Successfully added ingress policy to Security Group %s",
                groupName);

            return resp.groupId();

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```

To add an egress rule to the security group, provide similar data in an [AuthorizeSecurityGroupEgressRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/AuthorizeSecurityGroupEgressRequest.html) to the Ec2Client’s `authorizeSecurityGroupEgress` method\.

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/ec2/src/main/java/com/example/ec2/CreateSecurityGroup.java) on GitHub\.

## Describe security groups<a name="describe-security-groups"></a>

To describe your security groups or get information about them, call the Ec2Client’s `describeSecurityGroups` method\. It returns a [DescribeSecurityGroupsResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/DescribeSecurityGroupsResponse.html) that you can use to access the list of security groups by calling its `securityGroups` method, which returns a list of [SecurityGroup](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/SecurityGroup.html) objects\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsResponse;
import software.amazon.awssdk.services.ec2.model.SecurityGroup;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
```

 **Code** 

```
     public static void describeEC2SecurityGroups(Ec2Client ec2, String groupId) {

        try {
            DescribeSecurityGroupsRequest request =
                DescribeSecurityGroupsRequest.builder()
                        .groupIds(groupId).build();

            DescribeSecurityGroupsResponse response =
                ec2.describeSecurityGroups(request);

             for(SecurityGroup group : response.securityGroups()) {
                System.out.printf(
                    "Found Security Group with id %s, " +
                            "vpc id %s " +
                            "and description %s",
                    group.groupId(),
                    group.vpcId(),
                    group.description());
            }
        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/ec2/src/main/java/com/example/ec2/DescribeSecurityGroups.java) on GitHub\.

## Delete a security group<a name="delete-a-security-group"></a>

To delete a security group, call the Ec2Client’s `deleteSecurityGroup` method, passing it a [DeleteSecurityGroupRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/DeleteSecurityGroupRequest.html) that contains the ID of the security group to delete\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DeleteSecurityGroupRequest;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
```

 **Code** 

```
    public static void deleteEC2SecGroup(Ec2Client ec2,String groupId) {

        try {
            DeleteSecurityGroupRequest request = DeleteSecurityGroupRequest.builder()
                .groupId(groupId)
                .build();

            ec2.deleteSecurityGroup(request);
            System.out.printf(
                "Successfully deleted Security Group with id %s", groupId);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
     }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/ec2/src/main/java/com/example/ec2/DeleteSecurityGroup.java) on GitHub\.

## More information<a name="more-information"></a>
+  [Amazon EC2 Security Groups](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-key-pairs.html) in the Amazon EC2 User Guide for Linux Instances
+  [Authorizing Inbound Traffic for Your Linux Instances](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/authorizing-access-to-an-instance.html) in the Amazon EC2 User Guide for Linux Instances
+  [CreateSecurityGroup](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_CreateSecurityGroup.html) in the Amazon EC2 API Reference
+  [DescribeSecurityGroups](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSecurityGroups.html) in the Amazon EC2 API Reference
+  [DeleteSecurityGroup](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DeleteSecurityGroup.html) in the Amazon EC2 API Reference
+  [AuthorizeSecurityGroupIngress](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_AuthorizeSecurityGroupIngress.html) in the Amazon EC2 API Reference