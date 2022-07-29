--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# AWS KMS examples using SDK for Java 2\.x<a name="java_kms_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with AWS KMS\.

*Actions* are code excerpts that show you how to call individual AWS KMS functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple AWS KMS functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w591aac15c14b9c39c13)

## Actions<a name="w591aac15c14b9c39c13"></a>

### Create a grant for a key<a name="kms_CreateGrant_java_topic"></a>

The following code example shows how to create a grant for a KMS key\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/kms#readme)\. 
  

```
    public static String createGrant(KmsClient kmsClient, String keyId, String granteePrincipal, String operation) {

        try {
            CreateGrantRequest grantRequest = CreateGrantRequest.builder()
                .keyId(keyId)
                .granteePrincipal(granteePrincipal)
                .operationsWithStrings(operation)
                .build();

            CreateGrantResponse response = kmsClient.createGrant(grantRequest);
            return response.grantId();

        }catch (KmsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateGrant](https://docs.aws.amazon.com/goto/SdkForJavaV2/kms-2014-11-01/CreateGrant) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a key<a name="kms_CreateKey_java_topic"></a>

The following code example shows how to create an AWS KMS key\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/kms#readme)\. 
  

```
    public static String createKey(KmsClient kmsClient, String keyDesc) {

        try {
            CreateKeyRequest keyRequest = CreateKeyRequest.builder()
                .description(keyDesc)
                .customerMasterKeySpec(CustomerMasterKeySpec.SYMMETRIC_DEFAULT)
                .keyUsage("ENCRYPT_DECRYPT")
                .build();

            CreateKeyResponse result = kmsClient.createKey(keyRequest);
            System.out.printf("Created a customer key with id \"%s\"%n", result.keyMetadata().arn());
            return result.keyMetadata().keyId();

        } catch (KmsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateKey](https://docs.aws.amazon.com/goto/SdkForJavaV2/kms-2014-11-01/CreateKey) in *AWS SDK for Java 2\.x API Reference*\. 

### Create an alias for a key<a name="kms_CreateAlias_java_topic"></a>

The following code example shows how to create an alias for a KMS key key\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/kms#readme)\. 
  

```
    public static void createCustomAlias(KmsClient kmsClient, String targetKeyId, String aliasName) {

        try {
            CreateAliasRequest aliasRequest = CreateAliasRequest.builder()
                .aliasName(aliasName)
                .targetKeyId(targetKeyId)
                .build();

            kmsClient.createAlias(aliasRequest);

        } catch (KmsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [CreateAlias](https://docs.aws.amazon.com/goto/SdkForJavaV2/kms-2014-11-01/CreateAlias) in *AWS SDK for Java 2\.x API Reference*\. 

### Decrypt ciphertext<a name="kms_Decrypt_java_topic"></a>

The following code example shows how to decrypt ciphertext that was encrypted by a KMS key\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/kms#readme)\. 
  

```
    public static void decryptData(KmsClient kmsClient, SdkBytes encryptedData, String keyId) {

        try {
             DecryptRequest decryptRequest = DecryptRequest.builder()
                 .ciphertextBlob(encryptedData)
                 .keyId(keyId)
                 .build();

            DecryptResponse decryptResponse = kmsClient.decrypt(decryptRequest);
            decryptResponse.plaintext();

        } catch (KmsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [Decrypt](https://docs.aws.amazon.com/goto/SdkForJavaV2/kms-2014-11-01/Decrypt) in *AWS SDK for Java 2\.x API Reference*\. 

### Describe a key<a name="kms_DescribeKey_java_topic"></a>

The following code example shows how to describe a KMS key\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/kms#readme)\. 
  

```
    public static void describeSpecifcKey(KmsClient kmsClient,String keyId ){

        try {
            DescribeKeyRequest keyRequest = DescribeKeyRequest.builder()
                .keyId(keyId)
                .build();

            DescribeKeyResponse response = kmsClient.describeKey(keyRequest);
            System.out.println("The key description is "+response.keyMetadata().description());
            System.out.println("The key ARN is "+response.keyMetadata().arn());

        } catch (KmsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
       }
    }
```
+  For API details, see [DescribeKey](https://docs.aws.amazon.com/goto/SdkForJavaV2/kms-2014-11-01/DescribeKey) in *AWS SDK for Java 2\.x API Reference*\. 

### Disable a key<a name="kms_DisableKey_java_topic"></a>

The following code example shows how to disable a KMS key\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/kms#readme)\. 
  

```
    public static void disableKey( KmsClient kmsClient, String keyId) {

        try {
            DisableKeyRequest keyRequest = DisableKeyRequest.builder()
                .keyId(keyId)
                .build();

            kmsClient.disableKey(keyRequest);

        } catch (KmsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DisableKey](https://docs.aws.amazon.com/goto/SdkForJavaV2/kms-2014-11-01/DisableKey) in *AWS SDK for Java 2\.x API Reference*\. 

### Enable a key<a name="kms_EnableKey_java_topic"></a>

The following code example shows how to enable a KMS key\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/kms#readme)\. 
  

```
    public static void enableKey(KmsClient kmsClient, String keyId) {

        try {
            EnableKeyRequest enableKeyRequest = EnableKeyRequest.builder()
                .keyId(keyId)
                .build();

            kmsClient.enableKey(enableKeyRequest);

        } catch (KmsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
   }
```
+  For API details, see [EnableKey](https://docs.aws.amazon.com/goto/SdkForJavaV2/kms-2014-11-01/EnableKey) in *AWS SDK for Java 2\.x API Reference*\. 

### Encrypt text using a key<a name="kms_Encrypt_java_topic"></a>

The following code example shows how to encrypt text using a KMS key\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/kms#readme)\. 
  

```
     public static SdkBytes encryptData(KmsClient kmsClient, String keyId) {

         try {
             SdkBytes myBytes = SdkBytes.fromByteArray(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0});

             EncryptRequest encryptRequest = EncryptRequest.builder()
                 .keyId(keyId)
                 .plaintext(myBytes)
                 .build();

             EncryptResponse response = kmsClient.encrypt(encryptRequest);
             String algorithm = response.encryptionAlgorithm().toString();
             System.out.println("The encryption algorithm is " + algorithm);

             // Get the encrypted data.
             SdkBytes encryptedData = response.ciphertextBlob();
             return encryptedData;

         } catch (KmsException e) {
             System.err.println(e.getMessage());
             System.exit(1);
         }
         return null;
     }
```
+  For API details, see [Encrypt](https://docs.aws.amazon.com/goto/SdkForJavaV2/kms-2014-11-01/Encrypt) in *AWS SDK for Java 2\.x API Reference*\. 

### List aliases for a key<a name="kms_ListAliases_java_topic"></a>

The following code example shows how to list aliases for a KMS key\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/kms#readme)\. 
  

```
    public static void listAllAliases( KmsClient kmsClient) {

        try {
            ListAliasesRequest aliasesRequest = ListAliasesRequest.builder()
                .limit(15)
                .build();

            ListAliasesResponse aliasesResponse = kmsClient.listAliases(aliasesRequest) ;
            List<AliasListEntry> aliases = aliasesResponse.aliases();
            for (AliasListEntry alias: aliases) {
                System.out.println("The alias name is: "+alias.aliasName());
            }

        } catch (KmsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListAliases](https://docs.aws.amazon.com/goto/SdkForJavaV2/kms-2014-11-01/ListAliases) in *AWS SDK for Java 2\.x API Reference*\. 

### List grants for a key<a name="kms_ListGrants_java_topic"></a>

The following code example shows how to list grants for a KMS key\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/kms#readme)\. 
  

```
    public static void displayGrantIds(KmsClient kmsClient, String keyId) {

        try {
            ListGrantsRequest grantsRequest = ListGrantsRequest.builder()
                .keyId(keyId)
                .limit(15)
                .build();

            ListGrantsResponse response = kmsClient.listGrants(grantsRequest);
            List<GrantListEntry> grants = response.grants();
            for ( GrantListEntry grant: grants) {
                System.out.println("The grant Id is : "+grant.grantId());
            }

        } catch (KmsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListGrants](https://docs.aws.amazon.com/goto/SdkForJavaV2/kms-2014-11-01/ListGrants) in *AWS SDK for Java 2\.x API Reference*\. 

### List keys<a name="kms_ListKeys_java_topic"></a>

The following code example shows how to list KMS keys\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/kms#readme)\. 
  

```
    public static void listAllKeys(KmsClient kmsClient) {

        try {
            ListKeysRequest listKeysRequest = ListKeysRequest.builder()
                .limit(15)
                .build();

            ListKeysResponse keysResponse = kmsClient.listKeys(listKeysRequest);
            List<KeyListEntry> keyListEntries = keysResponse.keys();
            for (KeyListEntry key : keyListEntries) {
                System.out.println("The key ARN is: " + key.keyArn());
                System.out.println("The key Id is: " + key.keyId());
            }

        } catch (KmsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListKeys](https://docs.aws.amazon.com/goto/SdkForJavaV2/kms-2014-11-01/ListKeys) in *AWS SDK for Java 2\.x API Reference*\. 