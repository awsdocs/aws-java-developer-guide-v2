# Work with Amazon EC2 key pairs<a name="examples-ec2-key-pairs"></a>

## Create a key pair<a name="create-a-key-pair"></a>

To create a key pair, call the Ec2Client’s `createKeyPair` method with a [CreateKeyPairRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/CreateKeyPairRequest.html) that contains the key’s name\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateKeyPairRequest;
import software.amazon.awssdk.services.ec2.model.CreateKeyPairResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
```

 **Code** 

```
    public static void createEC2KeyPair(Ec2Client ec2,String keyName ) {

        try {
            CreateKeyPairRequest request = CreateKeyPairRequest.builder()
                .keyName(keyName).build();

            ec2.createKeyPair(request);
            System.out.printf(
                "Successfully created key pair named %s",
                keyName);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
     }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/ec2/src/main/java/com/example/ec2/CreateKeyPair.java) on GitHub\.

## Describe key pairs<a name="describe-key-pairs"></a>

To list your key pairs or to get information about them, call the Ec2Client’s `describeKeyPairs` method\. It returns a [DescribeKeyPairsResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/DescribeKeyPairsResponse.html) that you can use to access the list of key pairs by calling its `keyPairs` method, which returns a list of [KeyPairInfo](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/KeyPairInfo.html) objects\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeKeyPairsResponse;
import software.amazon.awssdk.services.ec2.model.KeyPairInfo;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
```

 **Code** 

```
    public static void describeEC2Keys( Ec2Client ec2){

        try {
            DescribeKeyPairsResponse response = ec2.describeKeyPairs();

            for(KeyPairInfo keyPair : response.keyPairs()) {
                System.out.printf(
                    "Found key pair with name %s " +
                            "and fingerprint %s",
                    keyPair.keyName(),
                    keyPair.keyFingerprint());
             }

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
         }

    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/ec2/src/main/java/com/example/ec2/DescribeKeyPairs.java) on GitHub\.

## Delete a key pair<a name="delete-a-key-pair"></a>

To delete a key pair, call the Ec2Client’s `deleteKeyPair` method, passing it a [DeleteKeyPairRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/ec2/model/DeleteKeyPairRequest.html) that contains the name of the key pair to delete\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DeleteKeyPairRequest;
import software.amazon.awssdk.services.ec2.model.DeleteKeyPairResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
```

 **Code** 

```
    public static void deleteKeys(Ec2Client ec2, String keyPair) {

       try {

           DeleteKeyPairRequest request = DeleteKeyPairRequest.builder()
                .keyName(keyPair)
                .build();

           DeleteKeyPairResponse response = ec2.deleteKeyPair(request);
            System.out.printf(
                "Successfully deleted key pair named %s", keyPair);

        } catch (Ec2Exception e) {
           System.err.println(e.awsErrorDetails().errorMessage());
           System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/ec2/src/main/java/com/example/ec2/DeleteKeyPair.java) on GitHub\.

## More information<a name="more-information"></a>
+  [Amazon EC2 Key Pairs](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-key-pairs.html) in the Amazon EC2 User Guide for Linux Instances
+  [CreateKeyPair](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_CreateKeyPair.html) in the Amazon EC2 API Reference
+  [DescribeKeyPairs](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeKeyPairs.html) in the Amazon EC2 API Reference
+  [DeleteKeyPair](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DeleteKeyPair.html) in the Amazon EC2 API Reference