--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Using IAM account aliases<a name="examples-iam-account-aliases"></a>

If you want the URL for your sign\-in page to contain your company name or other friendly identifier instead of your AWS account ID, you can create an alias for your AWS account\.

**Note**  
 AWS supports exactly one account alias per account\.

## Create an account alias<a name="create-an-account-alias"></a>

To create an account alias, call the IamClient’s `createAccountAlias` method with a [CreateAccountAliasRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/CreateAccountAliasRequest.html) object that contains the alias name\.

 **Imports** 

```
import software.amazon.awssdk.services.iam.model.CreateAccountAliasRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.IamException;
```

 **Code** 

```
    public static void createIAMAccountAlias(IamClient iam, String alias) {

        try {
            CreateAccountAliasRequest request = CreateAccountAliasRequest.builder()
                .accountAlias(alias)
                .build();

            iam.createAccountAlias(request);
            System.out.println("Successfully created account alias: " + alias);

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/CreateAccountAlias.java) on GitHub\.

## List account aliases<a name="list-account-aliases"></a>

To list your account’s alias, if any, call the IamClient’s `listAccountAliases` method\.

**Note**  
The returned [ListAccountAliasesResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/ListAccountAliasesResponse.html) supports the same `isTruncated` and `marker` methods as other AWS SDK for Java *list* methods, but an S account can have only *one* account alias\.

 **Imports** 

```
import software.amazon.awssdk.services.iam.model.IamException;
import software.amazon.awssdk.services.iam.model.ListAccountAliasesResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
```

 **Code** 

```
    public static void listAliases(IamClient iam) {

        try {
            ListAccountAliasesResponse response = iam.listAccountAliases();

            for (String alias : response.accountAliases()) {
                System.out.printf("Retrieved account alias %s", alias);
            }

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

see the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/ListAccountAliases.java) on GitHub\.

## Delete an account alias<a name="delete-an-account-alias"></a>

To delete your account’s alias, call the IamClient’s `deleteAccountAlias` method\. When deleting an account alias, you must supply its name using a [DeleteAccountAliasRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/DeleteAccountAliasRequest.html) object\.

 **Imports** 

```
import software.amazon.awssdk.services.iam.model.DeleteAccountAliasRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.IamException;
```

 **Code** 

```
    public static void deleteIAMAccountAlias(IamClient iam, String alias ) {

        try {
            DeleteAccountAliasRequest request = DeleteAccountAliasRequest.builder()
                    .accountAlias(alias)
                    .build();

            iam.deleteAccountAlias(request);
            System.out.println("Successfully deleted account alias " + alias);

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        System.out.println("Done");
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/DeleteAccountAlias.java) on GitHub\.

## More information<a name="more-information"></a>
+  [Your AWS Account ID and Its Alias](https://docs.aws.amazon.com/IAM/latest/UserGuide/console_account-alias.html) in the IAM User Guide
+  [CreateAccountAlias](https://docs.aws.amazon.com/IAM/latest/APIReference/API_CreateAccountAlias.html) in the IAM API Reference
+  [ListAccountAliases](https://docs.aws.amazon.com/IAM/latest/APIReference/API_ListAccountAliases.html) in the IAM API Reference
+  [DeleteAccountAlias](https://docs.aws.amazon.com/IAM/latest/APIReference/API_DeleteAccountAlias.html) in the IAM API Reference