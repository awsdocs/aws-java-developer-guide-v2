# Secrets Manager examples using SDK for Java 2\.x<a name="java_secrets-manager_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Secrets Manager\.

*Actions* are code excerpts that show you how to call individual service functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple functions within the same service\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#actions)

## Actions<a name="actions"></a>

### Create a secret<a name="secrets-manager_CreateSecret_java_topic"></a>

The following code example shows how to create a Secrets Manager secret\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/secretsmanager#readme)\. 
  

```
    public static String createNewSecret( SecretsManagerClient secretsClient, String secretName, String secretValue) {

        try {
            CreateSecretRequest secretRequest = CreateSecretRequest.builder()
                .name(secretName)
                .description("This secret was created by the AWS Secret Manager Java API")
                .secretString(secretValue)
                .build();

            CreateSecretResponse secretResponse = secretsClient.createSecret(secretRequest);
            return secretResponse.arn();

        } catch (SecretsManagerException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateSecret](https://docs.aws.amazon.com/goto/SdkForJavaV2/secretsmanager-2017-10-17/CreateSecret) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a secret<a name="secrets-manager_DeleteSecret_java_topic"></a>

The following code example shows how to delete a Secrets Manager secret\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/secretsmanager#readme)\. 
  

```
    public static void deleteSpecificSecret(SecretsManagerClient secretsClient, String secretName) {

        try {
            DeleteSecretRequest secretRequest = DeleteSecretRequest.builder()
                .secretId(secretName)
                .build();

            secretsClient.deleteSecret(secretRequest);
            System.out.println(secretName +" is deleted.");

        } catch (SecretsManagerException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteSecret](https://docs.aws.amazon.com/goto/SdkForJavaV2/secretsmanager-2017-10-17/DeleteSecret) in *AWS SDK for Java 2\.x API Reference*\. 

### Describe a secret<a name="secrets-manager_DescribeSecret_java_topic"></a>

The following code example shows how to describe a Secrets Manager secret\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/secretsmanager#readme)\. 
  

```
    public static void describeGivenSecret(SecretsManagerClient secretsClient, String secretName) {

        try {
            DescribeSecretRequest secretRequest = DescribeSecretRequest.builder()
                .secretId(secretName)
                .build();

            DescribeSecretResponse secretResponse = secretsClient.describeSecret(secretRequest);
            Instant lastChangedDate = secretResponse.lastChangedDate();

            // Convert the Instant to readable date.
            DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withLocale( Locale.US)
                        .withZone( ZoneId.systemDefault() );

            formatter.format( lastChangedDate );
            System.out.println("The date of the last change to "+ secretResponse.name() +" is " + lastChangedDate );

        } catch (SecretsManagerException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DescribeSecret](https://docs.aws.amazon.com/goto/SdkForJavaV2/secretsmanager-2017-10-17/DescribeSecret) in *AWS SDK for Java 2\.x API Reference*\. 

### Get a secret value<a name="secrets-manager_GetSecretValue_java_topic"></a>

The following code example shows how to get a Secrets Manager secret value\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/secretsmanager#readme)\. 
  

```
    public static void getValue(SecretsManagerClient secretsClient,String secretName) {

        try {
            GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

            GetSecretValueResponse valueResponse = secretsClient.getSecretValue(valueRequest);
            String secret = valueResponse.secretString();
            System.out.println(secret);

        } catch (SecretsManagerException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [GetSecretValue](https://docs.aws.amazon.com/goto/SdkForJavaV2/secretsmanager-2017-10-17/GetSecretValue) in *AWS SDK for Java 2\.x API Reference*\. 

### List secrets<a name="secrets-manager_ListSecrets_java_topic"></a>

The following code example shows how to list Secrets Manager secrets\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/secretsmanager#readme)\. 
  

```
    public static void listAllSecrets(SecretsManagerClient secretsClient) {
        try {
            ListSecretsResponse secretsResponse = secretsClient.listSecrets();
            List<SecretListEntry> secrets = secretsResponse.secretList();
            for (SecretListEntry secret: secrets) {
                System.out.println("The secret name is "+secret.name());
                System.out.println("The secret descreiption is "+secret.description());
            }

        } catch (SecretsManagerException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListSecrets](https://docs.aws.amazon.com/goto/SdkForJavaV2/secretsmanager-2017-10-17/ListSecrets) in *AWS SDK for Java 2\.x API Reference*\. 

### Modifies the details of a secret<a name="secrets-manager_UpdateSecret_java_topic"></a>

The following code example shows how to modifies the secret\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/secretsmanager#readme)\. 
  

```
    public static void updateMySecret(SecretsManagerClient secretsClient, String secretName, String secretValue) {

        try {
            UpdateSecretRequest secretRequest = UpdateSecretRequest.builder()
                .secretId(secretName)
                .secretString(secretValue)
                .build();

            secretsClient.updateSecret(secretRequest);

        } catch (SecretsManagerException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [UpdateSecret](https://docs.aws.amazon.com/goto/SdkForJavaV2/secretsmanager-2017-10-17/UpdateSecret) in *AWS SDK for Java 2\.x API Reference*\. 

### Put a value in a secret<a name="secrets-manager_PutSecretValue_java_topic"></a>

The following code example shows how to put a value in a Secrets Manager secret\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/secretsmanager#readme)\. 
  

```
    public static void putSecret(SecretsManagerClient secretsClient, String secretName, String secretValue) {
        try {
            PutSecretValueRequest secretRequest = PutSecretValueRequest.builder()
                .secretId(secretName)
                .secretString(secretValue)
                .build();

            secretsClient.putSecretValue(secretRequest);
            System.out.println("A new version was created.");

        } catch (SecretsManagerException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [PutSecretValue](https://docs.aws.amazon.com/goto/SdkForJavaV2/secretsmanager-2017-10-17/PutSecretValue) in *AWS SDK for Java 2\.x API Reference*\. 