# Amazon EC2 examples using SDK for Java 2\.x<a name="java_ec2_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Amazon EC2\.

*Actions* are code excerpts that show you how to call individual service functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple functions within the same service\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Get started**

## Hello Amazon EC2<a name="example_ec2_Hello_section"></a>

The following code examples show how to get started using Amazon EC2\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
     public static void describeEC2SecurityGroups(Ec2Client ec2, String groupId) {
         try {
             DescribeSecurityGroupsRequest request = DescribeSecurityGroupsRequest.builder()
                 .groupIds(groupId)
                 .build();

             DescribeSecurityGroupsResponse response = ec2.describeSecurityGroups(request);
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
+  For API details, see [DescribeSecurityGroups](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DescribeSecurityGroups) in *AWS SDK for Java 2\.x API Reference*\. 

**Topics**
+ [Actions](#actions)
+ [Scenarios](#scenarios)

## Actions<a name="actions"></a>

### Allocate an Elastic IP address<a name="ec2_AllocateAddress_java_topic"></a>

The following code example shows how to allocate an Elastic IP address for Amazon EC2\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

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

### Associate an Elastic IP address with an instance<a name="ec2_AssociateAddress_java_topic"></a>

The following code example shows how to associate an Elastic IP address with an Amazon EC2 instance\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static String associateAddress(Ec2Client ec2, String instanceId, String allocationId) {
        try {
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
+  For API details, see [AssociateAddress](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/AssociateAddress) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a security group<a name="ec2_CreateSecurityGroup_java_topic"></a>

The following code example shows how to create an Amazon EC2 security group\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

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
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

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
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

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
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

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
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

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

### Describe instances<a name="ec2_DescribeInstances_java_topic"></a>

The following code example shows how to describe Amazon EC2 instances\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static String describeEC2Instances( Ec2Client ec2, String newInstanceId) {
        try {
            String pubAddress = "";
            boolean isRunning = false;
            DescribeInstancesRequest request = DescribeInstancesRequest.builder()
                .instanceIds(newInstanceId)
                .build();

            while (!isRunning) {
                DescribeInstancesResponse response = ec2.describeInstances(request);
                String state = response.reservations().get(0).instances().get(0).state().name().name();
                if (state.compareTo("RUNNING") ==0) {
                     System.out.println("Image id is " + response.reservations().get(0).instances().get(0).imageId());
                     System.out.println("Instance type is " + response.reservations().get(0).instances().get(0).instanceType());
                     System.out.println("Instance state is " + response.reservations().get(0).instances().get(0).state().name());
                     pubAddress = response.reservations().get(0).instances().get(0).publicIpAddress();
                     System.out.println("Instance address is " + pubAddress);
                     isRunning = true;
                }
            }
            return pubAddress;
        } catch (SsmException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [DescribeInstances](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DescribeInstances) in *AWS SDK for Java 2\.x API Reference*\. 

### Disassociate an Elastic IP address from an instance<a name="ec2_DisassociateAddress_java_topic"></a>

The following code example shows how to disassociate an Elastic IP address from an Amazon EC2 instance\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static void disassociateAddress(Ec2Client ec2, String associationId) {
        try {
            DisassociateAddressRequest addressRequest = DisassociateAddressRequest.builder()
                .associationId(associationId)
                .build();

            ec2.disassociateAddress(addressRequest);
            System.out.println("You successfully disassociated the address!");

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DisassociateAddress](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DisassociateAddress) in *AWS SDK for Java 2\.x API Reference*\. 

### Get data about a security group<a name="ec2_DescribeSecurityGroups_java_topic"></a>

The following code example shows how to get data about an Amazon EC2 security group\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static void describeSecurityGroups(Ec2Client ec2, String groupId) {
        try {
            DescribeSecurityGroupsRequest request = DescribeSecurityGroupsRequest.builder()
                .groupIds(groupId)
                .build();

            DescribeSecurityGroupsResponse response = ec2.describeSecurityGroups(request);
            for(SecurityGroup group : response.securityGroups()) {
                System.out.println( "Found Security Group with Id " +group.groupId() +" and group VPC "+ group.vpcId());
            }

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DescribeSecurityGroups](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DescribeSecurityGroups) in *AWS SDK for Java 2\.x API Reference*\. 

### Get data about instance types<a name="ec2_DescribeInstanceTypes_java_topic"></a>

The following code example shows how to get data about Amazon EC2 instance types\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    // Get a list of instance types.
    public static String getInstanceTypes(Ec2Client ec2) {
        String instanceType="";
        try {
            List<Filter> filters = new ArrayList<>();
            Filter filter = Filter.builder()
                .name("processor-info.supported-architecture")
                .values("arm64")
                .build();

            filters.add(filter);
            DescribeInstanceTypesRequest typesRequest = DescribeInstanceTypesRequest.builder()
                .filters(filters)
                .maxResults(10)
                .build();

            DescribeInstanceTypesResponse response = ec2.describeInstanceTypes(typesRequest);
            List<InstanceTypeInfo> instanceTypes = response.instanceTypes();
            for (InstanceTypeInfo type: instanceTypes) {
                System.out.println("The memory information of this type is "+type.memoryInfo().sizeInMiB());
                System.out.println("Network information is "+type.networkInfo().toString());
                instanceType = type.instanceType().toString();
            }

            return instanceType;

        } catch (SsmException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [DescribeInstanceTypes](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DescribeInstanceTypes) in *AWS SDK for Java 2\.x API Reference*\. 

### List security key pairs<a name="ec2_DescribeKeyPairs_java_topic"></a>

The following code example shows how to list Amazon EC2 security key pairs\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static void describeEC2Keys( Ec2Client ec2){

        try {
            DescribeKeyPairsResponse response = ec2.describeKeyPairs();
            response.keyPairs().forEach(keyPair -> System.out.printf(
                "Found key pair with name %s " +
                   "and fingerprint %s",
                    keyPair.keyName(),
                    keyPair.keyFingerprint())
            );

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
         }
    }
```
+  For API details, see [DescribeKeyPairs](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DescribeKeyPairs) in *AWS SDK for Java 2\.x API Reference*\. 

### Release an Elastic IP address<a name="ec2_ReleaseAddress_java_topic"></a>

The following code example shows how to release an Elastic IP address\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

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

### Set inbound rules for a security group<a name="ec2_AuthorizeSecurityGroupIngress_java_topic"></a>

The following code example shows how to set inbound rules for an Amazon EC2 security group\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static String createSecurityGroup(Ec2Client ec2,String groupName, String groupDesc, String vpcId, String myIpAddress) {
        try {
            CreateSecurityGroupRequest createRequest = CreateSecurityGroupRequest.builder()
                .groupName(groupName)
                .description(groupDesc)
                .vpcId(vpcId)
                .build();

            CreateSecurityGroupResponse resp= ec2.createSecurityGroup(createRequest);
            IpRange ipRange = IpRange.builder()
                .cidrIp(myIpAddress+"/0")
                .build();

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

            AuthorizeSecurityGroupIngressRequest authRequest = AuthorizeSecurityGroupIngressRequest.builder()
                .groupName(groupName)
                .ipPermissions(ipPerm, ipPerm2)
                .build();

            ec2.authorizeSecurityGroupIngress(authRequest);
            System.out.println("Successfully added ingress policy to security group "+groupName);
            return resp.groupId();

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [AuthorizeSecurityGroupIngress](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/AuthorizeSecurityGroupIngress) in *AWS SDK for Java 2\.x API Reference*\. 

### Start an instance<a name="ec2_StartInstances_java_topic"></a>

The following code example shows how to start an Amazon EC2 instance\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static void startInstance(Ec2Client ec2, String instanceId) {
        Ec2Waiter ec2Waiter = Ec2Waiter.builder()
            .overrideConfiguration(b -> b.maxAttempts(100))
            .client(ec2)
            .build();

        StartInstancesRequest request = StartInstancesRequest.builder()
            .instanceIds(instanceId)
            .build();

        System.out.println("Use an Ec2Waiter to wait for the instance to run. This will take a few minutes.");
        ec2.startInstances(request);
        DescribeInstancesRequest instanceRequest = DescribeInstancesRequest.builder()
            .instanceIds(instanceId)
            .build();

        WaiterResponse<DescribeInstancesResponse> waiterResponse = ec2Waiter.waitUntilInstanceRunning(instanceRequest);
        waiterResponse.matched().response().ifPresent(System.out::println);
        System.out.println("Successfully started instance "+instanceId);
    }
```
+  For API details, see [StartInstances](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/StartInstances) in *AWS SDK for Java 2\.x API Reference*\. 

### Stop an instance<a name="ec2_StopInstances_java_topic"></a>

The following code example shows how to stop an Amazon EC2 instance\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
    public static void stopInstance(Ec2Client ec2, String instanceId) {
        Ec2Waiter ec2Waiter = Ec2Waiter.builder()
            .overrideConfiguration(b -> b.maxAttempts(100))
            .client(ec2)
            .build();
        StopInstancesRequest request = StopInstancesRequest.builder()
            .instanceIds(instanceId)
            .build();

        System.out.println("Use an Ec2Waiter to wait for the instance to stop. This will take a few minutes.");
        ec2.stopInstances(request);
        DescribeInstancesRequest instanceRequest = DescribeInstancesRequest.builder()
            .instanceIds(instanceId)
            .build();

       WaiterResponse<DescribeInstancesResponse> waiterResponse = ec2Waiter.waitUntilInstanceStopped(instanceRequest);
       waiterResponse.matched().response().ifPresent(System.out::println);
       System.out.println("Successfully stopped instance "+instanceId);
    }
```
+  For API details, see [StopInstances](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/StopInstances) in *AWS SDK for Java 2\.x API Reference*\. 

### Terminate an instance<a name="ec2_TerminateInstances_java_topic"></a>

The following code example shows how to terminate an Amazon EC2 instance\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

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

## Scenarios<a name="scenarios"></a>

### Get started with instances<a name="ec2_Scenario_GetStartedInstances_java_topic"></a>

The following code example shows how to:
+ Create a key pair and security group\.
+ Select an Amazon Machine Image \(AMI\) and compatible instance type, then create an instance\.
+ Stop and restart the instance\.
+ Associate an Elastic IP address with your instance\.
+ Connect to your instance with SSH, then clean up resources\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ec2#readme)\. 
  

```
/**
 * Before running this Java (v2) code example, set up your development environment, including your credentials.
 *
 * For more information, see the following documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 *
 * This Java example performs the following tasks:
 *
 * 1. Creates an RSA key pair and saves the private key data as a .pem file.
 * 2. Lists key pairs.
 * 3. Creates a security group for the default VPC.
 * 4. Displays security group information.
 * 5. Gets a list of Amazon Linux 2 AMIs and selects one.
 * 6. Gets more information about the image.
 * 7. Gets a list of instance types that are compatible with the selected AMI’s architecture.
 * 8. Creates an instance with the key pair, security group, AMI, and an instance type.
 * 9. Displays information about the instance.
 * 10. Stops the instance and waits for it to stop.
 * 11. Starts the instance and waits for it to start.
 * 12. Allocates an Elastic IP address and associates it with the instance.
 * 13. Displays SSH connection info for the instance.
 * 14. Disassociates and deletes the Elastic IP address.
 * 15. Terminates the instance and waits for it to terminate.
 * 16. Deletes the security group.
 * 17. Deletes the key pair.
 */
public class EC2Scenario {

    public static final String DASHES = new String(new char[80]).replace("\0", "-");
    public static void main(String[] args) throws InterruptedException {

        final String usage = "\n" +
            "Usage:\n" +
            "   <keyName> <fileName> <groupName> <groupDesc> <vpcId>\n\n" +
            "Where:\n" +
            "   keyName -  A key pair name (for example, TestKeyPair). \n\n" +
            "   fileName -  A file name where the key information is written to. \n\n" +
            "   groupName - The name of the security group. \n\n" +
            "   groupDesc - The description of the security group. \n\n" +
            "   vpcId - A VPC Id value. You can get this value from the AWS Management Console. \n\n" +
            "   myIpAddress - The IP address of your development machine. \n\n" ;

        if (args.length != 6) {
            System.out.println(usage);
            System.exit(1);
        }

        String keyName = args[0];
        String fileName = args[1];
        String groupName = args[2];
        String groupDesc = args[3];
        String vpcId = args[4];
        String myIpAddress = args[5];

        Region region = Region.US_WEST_2;
        Ec2Client ec2 = Ec2Client.builder()
            .region(region)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

        SsmClient ssmClient = SsmClient.builder()
            .region(region)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

        System.out.println(DASHES);
        System.out.println("Welcome to the Amazon EC2 example scenario.");
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("1. Create an RSA key pair and save the private key material as a .pem file.");
        createKeyPair(ec2, keyName, fileName);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("2. List key pairs.");
        describeKeys(ec2);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("3. Create a security group.");
        String groupId = createSecurityGroup(ec2, groupName, groupDesc, vpcId, myIpAddress);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("4. Display security group info for the newly created security group.");
        describeSecurityGroups(ec2, groupId);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("5. Get a list of Amazon Linux 2 AMIs and selects one with amzn2 in the name.");
        String instanceId = getParaValues(ssmClient);
        System.out.println("The instance Id is "+instanceId);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("6. Get more information about an amzn2 image.");
        String amiValue = describeImage(ec2, instanceId);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("7. Get a list of instance types.");
        String instanceType = getInstanceTypes(ec2);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("8. Create an instance.");
        String newInstanceId = runInstance(ec2, instanceType, keyName, groupName, amiValue );
        System.out.println("The instance Id is "+newInstanceId);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("9. Display information about the running instance. ");
        String ipAddress = describeEC2Instances(ec2, newInstanceId);
        System.out.println("You can SSH to the instance using this command:");
        System.out.println("ssh -i "+fileName +"ec2-user@"+ipAddress);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("10. Stop the instance and use a waiter.");
        stopInstance(ec2, newInstanceId);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("11. Start the instance and use a waiter.");
        startInstance(ec2, newInstanceId);
        ipAddress = describeEC2Instances(ec2, newInstanceId);
        System.out.println("You can SSH to the instance using this command:");
        System.out.println("ssh -i "+fileName +"ec2-user@"+ipAddress);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("12. Allocate an Elastic IP address and associate it with the instance.");
        String allocationId = allocateAddress(ec2);
        System.out.println("The allocation Id value is "+allocationId);
        String associationId = associateAddress(ec2, newInstanceId, allocationId);
        System.out.println("The associate Id value is "+associationId);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("13. Describe the instance again.");
        ipAddress = describeEC2Instances(ec2, newInstanceId);
        System.out.println("You can SSH to the instance using this command:");
        System.out.println("ssh -i "+fileName +"ec2-user@"+ipAddress);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("14. Disassociate and release the Elastic IP address.");
        disassociateAddress(ec2, associationId);
        releaseEC2Address(ec2, allocationId);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("15. Terminate the instance and use a waiter.");
        terminateEC2(ec2, newInstanceId);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("16. Delete the security group.");
        deleteEC2SecGroup(ec2, groupId);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("17. Delete the key.");
        deleteKeys(ec2, keyName);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("You successfully completed the Amazon EC2 scenario.");
        System.out.println(DASHES);
        ec2.close();
    }

    public static void deleteEC2SecGroup(Ec2Client ec2,String groupId) {
        try {
            DeleteSecurityGroupRequest request = DeleteSecurityGroupRequest.builder()
                .groupId(groupId)
                .build();

            ec2.deleteSecurityGroup(request);
            System.out.println("Successfully deleted security group with Id " +groupId);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void terminateEC2(Ec2Client ec2, String instanceId) {
        try{
            Ec2Waiter ec2Waiter = Ec2Waiter.builder()
                .overrideConfiguration(b -> b.maxAttempts(100))
                .client(ec2)
                .build();

            TerminateInstancesRequest ti = TerminateInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

            System.out.println("Use an Ec2Waiter to wait for the instance to terminate. This will take a few minutes.");
            ec2.terminateInstances(ti);
            DescribeInstancesRequest instanceRequest = DescribeInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

            WaiterResponse<DescribeInstancesResponse> waiterResponse = ec2Waiter.waitUntilInstanceTerminated(instanceRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);
            System.out.println("Successfully started instance "+instanceId);
            System.out.println(instanceId +" is terminated!");

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void deleteKeys(Ec2Client ec2, String keyPair) {
        try {
            DeleteKeyPairRequest request = DeleteKeyPairRequest.builder()
                .keyName(keyPair)
                .build();

            ec2.deleteKeyPair(request);
            System.out.println("Successfully deleted key pair named "+keyPair);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void releaseEC2Address(Ec2Client ec2,String allocId) {
        try {
            ReleaseAddressRequest request = ReleaseAddressRequest.builder()
                .allocationId(allocId)
                .build();

            ec2.releaseAddress(request);
            System.out.println("Successfully released Elastic IP address "+allocId);
        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void disassociateAddress(Ec2Client ec2, String associationId) {
        try {
            DisassociateAddressRequest addressRequest = DisassociateAddressRequest.builder()
                .associationId(associationId)
                .build();

            ec2.disassociateAddress(addressRequest);
            System.out.println("You successfully disassociated the address!");

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static String associateAddress(Ec2Client ec2, String instanceId, String allocationId) {
        try {
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

    public static String allocateAddress(Ec2Client ec2) {
        try {
            AllocateAddressRequest allocateRequest = AllocateAddressRequest.builder()
                .domain(DomainType.VPC)
                .build();

            AllocateAddressResponse allocateResponse = ec2.allocateAddress(allocateRequest);
            return allocateResponse.allocationId();

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }

    public static void startInstance(Ec2Client ec2, String instanceId) {
        Ec2Waiter ec2Waiter = Ec2Waiter.builder()
            .overrideConfiguration(b -> b.maxAttempts(100))
            .client(ec2)
            .build();

        StartInstancesRequest request = StartInstancesRequest.builder()
            .instanceIds(instanceId)
            .build();

        System.out.println("Use an Ec2Waiter to wait for the instance to run. This will take a few minutes.");
        ec2.startInstances(request);
        DescribeInstancesRequest instanceRequest = DescribeInstancesRequest.builder()
            .instanceIds(instanceId)
            .build();

        WaiterResponse<DescribeInstancesResponse> waiterResponse = ec2Waiter.waitUntilInstanceRunning(instanceRequest);
        waiterResponse.matched().response().ifPresent(System.out::println);
        System.out.println("Successfully started instance "+instanceId);
    }

    public static void stopInstance(Ec2Client ec2, String instanceId) {
        Ec2Waiter ec2Waiter = Ec2Waiter.builder()
            .overrideConfiguration(b -> b.maxAttempts(100))
            .client(ec2)
            .build();
        StopInstancesRequest request = StopInstancesRequest.builder()
            .instanceIds(instanceId)
            .build();

        System.out.println("Use an Ec2Waiter to wait for the instance to stop. This will take a few minutes.");
        ec2.stopInstances(request);
        DescribeInstancesRequest instanceRequest = DescribeInstancesRequest.builder()
            .instanceIds(instanceId)
            .build();

       WaiterResponse<DescribeInstancesResponse> waiterResponse = ec2Waiter.waitUntilInstanceStopped(instanceRequest);
       waiterResponse.matched().response().ifPresent(System.out::println);
       System.out.println("Successfully stopped instance "+instanceId);
    }

    public static String describeEC2Instances( Ec2Client ec2, String newInstanceId) {
        try {
            String pubAddress = "";
            boolean isRunning = false;
            DescribeInstancesRequest request = DescribeInstancesRequest.builder()
                .instanceIds(newInstanceId)
                .build();

            while (!isRunning) {
                DescribeInstancesResponse response = ec2.describeInstances(request);
                String state = response.reservations().get(0).instances().get(0).state().name().name();
                if (state.compareTo("RUNNING") ==0) {
                     System.out.println("Image id is " + response.reservations().get(0).instances().get(0).imageId());
                     System.out.println("Instance type is " + response.reservations().get(0).instances().get(0).instanceType());
                     System.out.println("Instance state is " + response.reservations().get(0).instances().get(0).state().name());
                     pubAddress = response.reservations().get(0).instances().get(0).publicIpAddress();
                     System.out.println("Instance address is " + pubAddress);
                     isRunning = true;
                }
            }
            return pubAddress;
        } catch (SsmException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }

    public static String runInstance(Ec2Client ec2, String instanceType, String keyName, String groupName, String amiId ) {
        try {
            RunInstancesRequest runRequest = RunInstancesRequest.builder()
                .instanceType(instanceType)
                .keyName(keyName)
                .securityGroups(groupName)
                .maxCount(1)
                .minCount(1)
                .imageId(amiId)
                .build();

            RunInstancesResponse response = ec2.runInstances(runRequest);
            String instanceId = response.instances().get(0).instanceId();
            System.out.println("Successfully started EC2 instance "+instanceId +" based on AMI "+amiId);
            return instanceId;

        } catch (SsmException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }

    // Get a list of instance types.
    public static String getInstanceTypes(Ec2Client ec2) {
        String instanceType="";
        try {
            List<Filter> filters = new ArrayList<>();
            Filter filter = Filter.builder()
                .name("processor-info.supported-architecture")
                .values("arm64")
                .build();

            filters.add(filter);
            DescribeInstanceTypesRequest typesRequest = DescribeInstanceTypesRequest.builder()
                .filters(filters)
                .maxResults(10)
                .build();

            DescribeInstanceTypesResponse response = ec2.describeInstanceTypes(typesRequest);
            List<InstanceTypeInfo> instanceTypes = response.instanceTypes();
            for (InstanceTypeInfo type: instanceTypes) {
                System.out.println("The memory information of this type is "+type.memoryInfo().sizeInMiB());
                System.out.println("Network information is "+type.networkInfo().toString());
                instanceType = type.instanceType().toString();
            }

            return instanceType;

        } catch (SsmException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }

    // Display the Description field that corresponds to the instance Id value.
    public static String describeImage(Ec2Client ec2, String instanceId) {
        try {
            DescribeImagesRequest imagesRequest = DescribeImagesRequest.builder()
                .imageIds(instanceId)
                .build();

            DescribeImagesResponse response = ec2.describeImages(imagesRequest);
            System.out.println("The description of the first image is "+response.images().get(0).description());
            System.out.println("The name of the first image is "+response.images().get(0).name());

            // Return the image Id value.
            return response.images().get(0).imageId();

        } catch (SsmException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }

    // Get the Id value of an instance with amzn2 in the name.
    public static String getParaValues(SsmClient ssmClient) {
        try {
            GetParametersByPathRequest parameterRequest = GetParametersByPathRequest.builder()
                .path("/aws/service/ami-amazon-linux-latest")
                .build();

            GetParametersByPathIterable responses = ssmClient.getParametersByPathPaginator(parameterRequest);
            for (software.amazon.awssdk.services.ssm.model.GetParametersByPathResponse response : responses) {
                System.out.println("Test "+response.nextToken());
                List<Parameter> parameterList = response.parameters();
                for (Parameter para: parameterList) {
                    System.out.println("The name of the para is: "+para.name());
                    System.out.println("The type of the para is: "+para.type());
                    if (filterName(para.name())) {
                        return para.value();
                    }
                }
            }

        } catch (SsmException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "" ;
    }

    // Return true if the name has amzn2 in it. For example:
    // /aws/service/ami-amazon-linux-latest/amzn2-ami-hvm-arm64-gp2
    private static boolean filterName(String name) {
        String[] parts = name.split("/");
        String myValue = parts[4];
        return myValue.contains("amzn2");
    }

    public static void describeSecurityGroups(Ec2Client ec2, String groupId) {
        try {
            DescribeSecurityGroupsRequest request = DescribeSecurityGroupsRequest.builder()
                .groupIds(groupId)
                .build();

            DescribeSecurityGroupsResponse response = ec2.describeSecurityGroups(request);
            for(SecurityGroup group : response.securityGroups()) {
                System.out.println( "Found Security Group with Id " +group.groupId() +" and group VPC "+ group.vpcId());
            }

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static String createSecurityGroup(Ec2Client ec2,String groupName, String groupDesc, String vpcId, String myIpAddress) {
        try {
            CreateSecurityGroupRequest createRequest = CreateSecurityGroupRequest.builder()
                .groupName(groupName)
                .description(groupDesc)
                .vpcId(vpcId)
                .build();

            CreateSecurityGroupResponse resp= ec2.createSecurityGroup(createRequest);
            IpRange ipRange = IpRange.builder()
                .cidrIp(myIpAddress+"/0")
                .build();

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

            AuthorizeSecurityGroupIngressRequest authRequest = AuthorizeSecurityGroupIngressRequest.builder()
                .groupName(groupName)
                .ipPermissions(ipPerm, ipPerm2)
                .build();

            ec2.authorizeSecurityGroupIngress(authRequest);
            System.out.println("Successfully added ingress policy to security group "+groupName);
            return resp.groupId();

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }

    public static void describeKeys( Ec2Client ec2){
        try {
            DescribeKeyPairsResponse response = ec2.describeKeyPairs();
            response.keyPairs().forEach(keyPair -> System.out.printf(
                "Found key pair with name %s " +
                    "and fingerprint %s",
                keyPair.keyName(),
                keyPair.keyFingerprint())
            );

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void createKeyPair(Ec2Client ec2, String keyName, String fileName) {
        try {
            CreateKeyPairRequest request = CreateKeyPairRequest.builder()
                .keyName(keyName)
                .build();

            CreateKeyPairResponse response = ec2.createKeyPair(request);
            String content = response.keyMaterial();
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(content);
            writer.close();
            System.out.println("Successfully created key pair named "+keyName);

        } catch (Ec2Exception | IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
```
+ For API details, see the following topics in *AWS SDK for Java 2\.x API Reference*\.
  + [AllocateAddress](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/AllocateAddress)
  + [AssociateAddress](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/AssociateAddress)
  + [AuthorizeSecurityGroupIngress](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/AuthorizeSecurityGroupIngress)
  + [CreateKeyPair](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/CreateKeyPair)
  + [CreateSecurityGroup](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/CreateSecurityGroup)
  + [DeleteKeyPair](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DeleteKeyPair)
  + [DeleteSecurityGroup](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DeleteSecurityGroup)
  + [DescribeImages](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DescribeImages)
  + [DescribeInstanceTypes](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DescribeInstanceTypes)
  + [DescribeInstances](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DescribeInstances)
  + [DescribeKeyPairs](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DescribeKeyPairs)
  + [DescribeSecurityGroups](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DescribeSecurityGroups)
  + [DisassociateAddress](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/DisassociateAddress)
  + [ReleaseAddress](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/ReleaseAddress)
  + [RunInstances](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/RunInstances)
  + [StartInstances](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/StartInstances)
  + [StopInstances](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/StopInstances)
  + [TerminateInstances](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/TerminateInstances)
  + [UnmonitorInstances](https://docs.aws.amazon.com/goto/SdkForJavaV2/ec2-2016-11-15/UnmonitorInstances)