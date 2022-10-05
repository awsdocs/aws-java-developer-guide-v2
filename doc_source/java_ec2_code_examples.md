--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Amazon EC2 examples using SDK for Java 2\.x<a name="java_ec2_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Amazon EC2\.

*Actions* are code excerpts that show you how to call individual Amazon EC2 functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple Amazon EC2 functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w620aac15c13b9c29c13)

## Actions<a name="w620aac15c13b9c29c13"></a>

### Allocate an Elastic IP address<a name="ec2_AllocateAddress_java_topic"></a>

The following code example shows how to allocate an Elastic IP address for Amazon EC2\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static String getAllocateAddress( Ec2Client ec2, String instanceId) {

        try {
            AllocateAddressRequest allocateRequest = AllocateAddressRequest.builder()
                .domain(DomainType.VPC)
                .build();

            AllocateAddressResponse allocateResponse = ec2.allocateAddress(allocateRequest);
            String allocationId = allocateResponse.allocationId();
            AssociateAddressRequest associateRequest = AssociateAddressRequest.builder()
               .instanceId(instanceId)
               .allocationId(allocationId)
               .build();

            AssociateAddressResponse associateResponse = ec2.associateAddress(associateRequest);
            return associateResponse.associationId();

        } catch (Ec2Exception e) {
           System.err.println(e.awsErrorDetails().errorMessage());
           System.exit(1);
        }
        return "";
    }
```
+  For API details, see [AllocateAddress](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/AllocateAddress) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a security group<a name="ec2_CreateSecurityGroup_java_topic"></a>

The following code example shows how to create an Amazon EC2 security group\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static String createEC2SecurityGroup( Ec2Client ec2,String groupName, String groupDesc, String vpcId) {
        try {

            CreateSecurityGroupRequest createRequest = CreateSecurityGroupRequest.builder()
                .groupName(groupName)
                .description(groupDesc)
                .vpcId(vpcId)
                .build();

            CreateSecurityGroupResponse resp= ec2.createSecurityGroup(createRequest);

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
            System.out.printf("Successfully added ingress policy to Security Group %s", groupName);
            return resp.groupId();

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateSecurityGroup](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/CreateSecurityGroup) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a security key pair<a name="ec2_CreateKeyPair_java_topic"></a>

The following code example shows how to create a security key pair for Amazon EC2\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static void createEC2KeyPair(Ec2Client ec2,String keyName ) {
        try {
            CreateKeyPairRequest request = CreateKeyPairRequest.builder()
                .keyName(keyName)
                .build();

            ec2.createKeyPair(request);
            System.out.printf("Successfully created key pair named %s", keyName);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
     }
```
+  For API details, see [CreateKeyPair](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/CreateKeyPair) in *AWS SDK for Java 2\.x API Reference*\. 

### Create and run an instance<a name="ec2_RunInstances_java_topic"></a>

The following code example shows how to create and run an Amazon EC2 instance\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static String createEC2Instance(Ec2Client ec2,String name, String amiId ) {

        RunInstancesRequest runRequest = RunInstancesRequest.builder()
            .imageId(amiId)
            .instanceType(InstanceType.T1_MICRO)
            .maxCount(1)
            .minCount(1)
            .build();

        RunInstancesResponse response = ec2.runInstances(runRequest);
        String instanceId = response.instances().get(0).instanceId();
        Tag tag = Tag.builder()
            .key("Name")
            .value(name)
            .build();

        CreateTagsRequest tagRequest = CreateTagsRequest.builder()
            .resources(instanceId)
            .tags(tag)
            .build();

        try {
            ec2.createTags(tagRequest);
            System.out.printf(  "Successfully started EC2 Instance %s based on AMI %s", instanceId, amiId);
            return instanceId;

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        return "";
    }
```
+  For API details, see [RunInstances](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/RunInstances) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a security group<a name="ec2_DeleteSecurityGroup_java_topic"></a>

The following code example shows how to delete an Amazon EC2 security group\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static void deleteEC2SecGroup(Ec2Client ec2,String groupId) {

        try {
            DeleteSecurityGroupRequest request = DeleteSecurityGroupRequest.builder()
                .groupId(groupId)
                .build();

            ec2.deleteSecurityGroup(request);
            System.out.printf("Successfully deleted Security Group with id %s", groupId);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
     }
```
+  For API details, see [DeleteSecurityGroup](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DeleteSecurityGroup) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a security key pair<a name="ec2_DeleteKeyPair_java_topic"></a>

The following code example shows how to delete an Amazon EC2 security key pair\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static void deleteKeys(Ec2Client ec2, String keyPair) {

        try {
            DeleteKeyPairRequest request = DeleteKeyPairRequest.builder()
                .keyName(keyPair)
                .build();

            ec2.deleteKeyPair(request);
            System.out.printf("Successfully deleted key pair named %s", keyPair);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteKeyPair](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DeleteKeyPair) in *AWS SDK for Java 2\.x API Reference*\. 

### Release an Elastic IP address<a name="ec2_ReleaseAddress_java_topic"></a>

The following code example shows how to release an Elastic IP address\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static void releaseEC2Address(Ec2Client ec2,String allocId) {

        try {
            ReleaseAddressRequest request = ReleaseAddressRequest.builder()
                .allocationId(allocId)
                .build();

            ec2.releaseAddress(request);
            System.out.printf("Successfully released elastic IP address %s", allocId);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
     }
```
+  For API details, see [ReleaseAddress](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/ReleaseAddress) in *AWS SDK for Java 2\.x API Reference*\. 

### Start an instance<a name="ec2_StartInstances_java_topic"></a>

The following code example shows how to start an Amazon EC2 instance\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static void startInstance(Ec2Client ec2, String instanceId) {

        StartInstancesRequest request = StartInstancesRequest.builder()
            .instanceIds(instanceId)
            .build();

        ec2.startInstances(request);
        System.out.printf("Successfully started instance %s", instanceId);
    }
```
+  For API details, see [StartInstances](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/StartInstances) in *AWS SDK for Java 2\.x API Reference*\. 

### Stop an instance<a name="ec2_StopInstances_java_topic"></a>

The following code example shows how to stop an Amazon EC2 instance\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static void stopInstance(Ec2Client ec2, String instanceId) {

        StopInstancesRequest request = StopInstancesRequest.builder()
            .instanceIds(instanceId)
            .build();

        ec2.stopInstances(request);
        System.out.printf("Successfully stopped instance %s", instanceId);
    }
```
+  For API details, see [StopInstances](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/StopInstances) in *AWS SDK for Java 2\.x API Reference*\. 

### Terminate an instance<a name="ec2_TerminateInstances_java_topic"></a>

The following code example shows how to terminate an Amazon EC2 instance\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static void terminateEC2( Ec2Client ec2, String instanceID) {

        try{
            TerminateInstancesRequest ti = TerminateInstancesRequest.builder()
                .instanceIds(instanceID)
                .build();

            TerminateInstancesResponse response = ec2.terminateInstances(ti);
            List<InstanceStateChange> list = response.terminatingInstances();
            for (InstanceStateChange sc : list) {
                System.out.println("The ID of the terminated instance is " + sc.instanceId());
            }

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [TerminateInstances](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/TerminateInstances) in *AWS SDK for Java 2\.x API Reference*\. 