--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Use elastic IP addresses in Amazon EC2<a name="examples-ec2-elastic-ip"></a>

## EC2\-Classic is retiring<a name="retiringEC2Classic"></a>

**Warning**  
We are retiring EC2\-Classic on August 15, 2022\. We recommend that you migrate from EC2\-Classic to a VPC\. For more information, see **Migrate from EC2\-Classic to a VPC** in the [Amazon EC2 User Guide for Linux Instances](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/vpc-migrate.html) or the [Amazon EC2 User Guide for Windows Instances](https://docs.aws.amazon.com/AWSEC2/latest/WindowsGuide/vpc-migrate.html)\. Also see the blog post [EC2\-Classic\-Classic Networking is Retiring – Here's How to Prepare](http://aws.amazon.com/blogs/aws/ec2-classic-is-retiring-heres-how-to-prepare/)\.

## Allocate an elastic IP address<a name="allocate-an-elastic-ip-address"></a>

To use an Elastic IP address, you first allocate one to your account, and then associate it with your instance or a network interface\.

To allocate an Elastic IP address, call the Ec2Client’s `allocateAddress` method with an [AllocateAddressRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/AllocateAddressRequest.html) object containing the network type \(classic Amazon EC2 or VPC\)\.

The returned [AllocateAddressResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/AllocateAddressResponse.html) contains an allocation ID that you can use to associate the address with an instance, by passing the allocation ID and instance ID in a [AssociateAddressRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/AssociateAddressRequest.html) to the Ec2Client’s `associateAddress` method\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.AllocateAddressRequest;
import software.amazon.awssdk.services.ec2.model.DomainType;
import software.amazon.awssdk.services.ec2.model.AllocateAddressResponse;
import software.amazon.awssdk.services.ec2.model.AssociateAddressRequest;
import software.amazon.awssdk.services.ec2.model.AssociateAddressResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
```

 **Code** 

```
    public static String getAllocateAddress( Ec2Client ec2, String instanceId) {

       try {
           AllocateAddressRequest allocateRequest = AllocateAddressRequest.builder()
                .domain(DomainType.VPC)
                .build();

           AllocateAddressResponse allocateResponse =
                ec2.allocateAddress(allocateRequest);

           String allocationId = allocateResponse.allocationId();

           AssociateAddressRequest associateRequest =
                AssociateAddressRequest.builder()
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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/ec2/src/main/java/com/example/ec2/AllocateAddress.java) on GitHub\.

## Describe elastic IP addresses<a name="describe-elastic-ip-addresses"></a>

To list the Elastic IP addresses assigned to your account, call the Ec2Client’s `describeAddresses` method\. It returns a [DescribeAddressesResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/DescribeAddressesResponse.html) which you can use to get a list of [Address](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/Address.html) objects that represent the Elastic IP addresses on your account\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Address;
import software.amazon.awssdk.services.ec2.model.DescribeAddressesResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
```

 **Code** 

```
    public static void describeEC2Address(Ec2Client ec2 ) {

        try {
            DescribeAddressesResponse response = ec2.describeAddresses();

            for(Address address : response.addresses()) {
                System.out.printf(
                    "Found address with public IP %s, " +
                            "domain %s, " +
                            "allocation id %s " +
                            "and NIC id %s",
                    address.publicIp(),
                    address.domain(),
                    address.allocationId(),
                    address.networkInterfaceId());
            }
        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/ec2/src/main/java/com/example/ec2/DescribeAddresses.java) on GitHub\.

## Release an elastic IP address<a name="release-an-elastic-ip-address"></a>

To release an Elastic IP address, call the Ec2Client’s `releaseAddress` method, passing it a [ReleaseAddressRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/ReleaseAddressRequest.html) containing the allocation ID of the Elastic IP address you want to release\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.ec2.model.ReleaseAddressRequest;
import software.amazon.awssdk.services.ec2.model.ReleaseAddressResponse;
```

 **Code** 

```
    public static void releaseEC2Address(Ec2Client ec2,String allocId) {

        try {
            ReleaseAddressRequest request = ReleaseAddressRequest.builder()
                .allocationId(allocId).build();

            ReleaseAddressResponse response = ec2.releaseAddress(request);

         System.out.printf(
                "Successfully released elastic IP address %s", allocId);
        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
     }
```

After you release an Elastic IP address, it is released to the AWS IP address pool and might be unavailable to you afterward\. Be sure to update your DNS records and any servers or devices that communicate with the address\.

If you are using *EC2\-Classic* or a *default VPC*, then releasing an Elastic IP address automatically disassociates it from any instance that it’s associated with\. To disassociate an Elastic IP address without releasing it, use the Ec2Client’s `disassociateAddress` method\.

If you are using a non\-default VPC, you *must* use `disassociateAddress` to disassociate the Elastic IP address before you try to release it\. Otherwise, Amazon EC2 returns an error \(*InvalidIPAddress\.InUse*\)\.

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/ec2/src/main/java/com/example/ec2/ReleaseAddress.java) on GitHub\.

## More information<a name="more-information"></a>
+  [Elastic IP Addresses](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/elastic-ip-addresses-eip.html) in the Amazon EC2 User Guide for Linux Instances
+  [AllocateAddress](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_AllocateAddress.html) in the Amazon EC2 API Reference
+  [DescribeAddresses](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeAddresses.html) in the Amazon EC2 API Reference
+  [ReleaseAddress](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_ReleaseAddress.html) in the Amazon EC2 API Reference