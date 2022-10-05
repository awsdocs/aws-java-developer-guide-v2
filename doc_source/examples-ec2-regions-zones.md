--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Use regions and availability zones<a name="examples-ec2-regions-zones"></a>

## Describe regions<a name="describe-regions"></a>

To list the Regions available to your account, call the Ec2Client’s `describeRegions` method\. It returns a [DescribeRegionsResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/DescribeRegionsResponse.html)\. Call the returned object’s `regions` method to get a list of [Region](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/Region.html) objects that represent each Region\.

 **Imports** 

```
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeRegionsResponse;
import software.amazon.awssdk.services.ec2.model.Region;
import software.amazon.awssdk.services.ec2.model.AvailabilityZone;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.ec2.model.DescribeAvailabilityZonesResponse;
```

 **Code** 

```
        try {

            DescribeRegionsResponse regionsResponse = ec2.describeRegions();
            for(Region region : regionsResponse.regions()) {
                System.out.printf(
                        "Found Region %s " +
                                "with endpoint %s",
                        region.regionName(),
                        region.endpoint());
                System.out.println();
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/ec2/src/main/java/com/example/ec2/DescribeRegionsAndZones.java) on GitHub\.

## Describe availability zones<a name="describe-availability-zones"></a>

To list each Availability Zone available to your account, call the Ec2Client’s `describeAvailabilityZones` method\. It returns a [DescribeAvailabilityZonesResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/DescribeAvailabilityZonesResponse.html)\. Call its `availabilityZones` method to get a list of [AvailabilityZone](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/AvailabilityZone.html) objects that represent each Availability Zone\.

 **Imports** 

```
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeRegionsResponse;
import software.amazon.awssdk.services.ec2.model.Region;
import software.amazon.awssdk.services.ec2.model.AvailabilityZone;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.ec2.model.DescribeAvailabilityZonesResponse;
```

 **Code** 

Create the Ec2Client\.

```
        software.amazon.awssdk.regions.Region region = software.amazon.awssdk.regions.Region.US_EAST_1;
        Ec2Client ec2 = Ec2Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
```

Then call describeAvailabilityZones\(\) and retrieve results\.

```
            DescribeAvailabilityZonesResponse zonesResponse =
                    ec2.describeAvailabilityZones();

            for(AvailabilityZone zone : zonesResponse.availabilityZones()) {
                System.out.printf(
                        "Found Availability Zone %s " +
                                "with status %s " +
                                "in region %s",
                        zone.zoneName(),
                        zone.state(),
                        zone.regionName());
                System.out.println();
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/ec2/src/main/java/com/example/ec2/DescribeRegionsAndZones.java) on GitHub\.

## Describe accounts<a name="describe-accounts"></a>

To describe your account, call the Ec2Client’s `describeAccountAttributes` method\. This method returns a [DescribeAccountAttributesResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/DescribeAccountAttributesResponse.html) object\. Invoke this objects `accountAttributes` method to get a list of [AccountAttribute](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/AccountAttribute.html) objects\. You can iterate through the list to retrieve an [AccountAttribute](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/AccountAttribute.html) object\.

You can get your account’s attribute values by invoking the [AccountAttribute](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/AccountAttribute.html) object’s `attributeValues` method\. This method returns a list of [AccountAttributeValue](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/AccountAttributeValue.html) objects\. You can iterate through this second list to display the value of attributes \(see the following code example\)\.

 **Imports** 

```
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeAccountAttributesResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
```

 **Code** 

```
    public static void describeEC2Account(Ec2Client ec2) {

        try{
            DescribeAccountAttributesResponse accountResults = ec2.describeAccountAttributes();
            accountResults.accountAttributes().forEach(attribute -> {
                        System.out.print("\n The name of the attribute is "+attribute.attributeName());

                        attribute.attributeValues().forEach(myValue ->
                            System.out.print("\n The value of the attribute is "+myValue.attributeValue()));
                        }
            );

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/ec2/src/main/java/com/example/ec2/DescribeAccount.java) on GitHub\.

## More information<a name="more-information"></a>
+  [Regions and Availability Zones](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-regions-availability-zones.html) in the Amazon EC2 User Guide for Linux Instances
+  [DescribeRegions](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeRegions.html) in the Amazon EC2 API Reference
+  [DescribeAvailabilityZones](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeAvailabilityZones.html) in the Amazon EC2 API Reference