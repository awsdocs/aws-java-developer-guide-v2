--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# IAM examples using SDK for Java 2\.x<a name="java_iam_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with IAM\.

*Actions* are code excerpts that show you how to call individual IAM functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple IAM functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w591aac15c14b9c37c13)
+ [Scenarios](#w591aac15c14b9c37c15)

## Actions<a name="w591aac15c14b9c37c13"></a>

### Attach a policy to a role<a name="iam_AttachRolePolicy_java_topic"></a>

The following code example shows how to attach an IAM policy to a role\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

```
    public static void attachIAMRolePolicy(IamClient iam, String roleName, String policyArn ) {

        try {
            ListAttachedRolePoliciesRequest request = ListAttachedRolePoliciesRequest.builder()
                .roleName(roleName)
                .build();

            ListAttachedRolePoliciesResponse response = iam.listAttachedRolePolicies(request);
            List<AttachedPolicy> attachedPolicies = response.attachedPolicies();

            // Ensure that the policy is not attached to this role
            String polArn = "";
            for (AttachedPolicy policy: attachedPolicies) {
                polArn = policy.policyArn();
                if (polArn.compareTo(policyArn)==0) {
                    System.out.println(roleName + " policy is already attached to this role.");
                    return;
                }
            }

           AttachRolePolicyRequest attachRequest = AttachRolePolicyRequest.builder()
               .roleName(roleName)
               .policyArn(policyArn)
               .build();

           iam.attachRolePolicy(attachRequest);

           System.out.println("Successfully attached policy " + policyArn +
                " to role " + roleName);

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        System.out.println("Done");
    }
```
+  For API details, see [AttachRolePolicy](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/AttachRolePolicy) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a policy<a name="iam_CreatePolicy_java_topic"></a>

The following code example shows how to create an IAM policy\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

```
    public static String createIAMPolicy(IamClient iam, String policyName ) {

        try {
            // Create an IamWaiter object.
            IamWaiter iamWaiter = iam.waiter();

            CreatePolicyRequest request = CreatePolicyRequest.builder()
                .policyName(policyName)
                .policyDocument(PolicyDocument)
                .build();

            CreatePolicyResponse response = iam.createPolicy(request);

            // Wait until the policy is created.
            GetPolicyRequest polRequest = GetPolicyRequest.builder()
                .policyArn(response.policy().arn())
                .build();

            WaiterResponse<GetPolicyResponse> waitUntilPolicyExists = iamWaiter.waitUntilPolicyExists(polRequest);
            waitUntilPolicyExists.matched().response().ifPresent(System.out::println);
            return response.policy().arn();

         } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "" ;
    }
```
+  For API details, see [CreatePolicy](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/CreatePolicy) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a role<a name="iam_CreateRole_java_topic"></a>

The following code example shows how to create an IAM role\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

```
    public static String createIAMRole(IamClient iam, String rolename, String fileLocation ) throws Exception {

        try {
            JSONObject jsonObject = (JSONObject) readJsonSimpleDemo(fileLocation);
            CreateRoleRequest request = CreateRoleRequest.builder()
                .roleName(rolename)
                .assumeRolePolicyDocument(jsonObject.toJSONString())
                .description("Created using the AWS SDK for Java")
                .build();

            CreateRoleResponse response = iam.createRole(request);
            System.out.println("The ARN of the role is "+response.role().arn());

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }

    public static Object readJsonSimpleDemo(String filename) throws Exception {
        FileReader reader = new FileReader(filename);
        JSONParser jsonParser = new JSONParser();
        return jsonParser.parse(reader);
    }
```
+  For API details, see [CreateRole](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/CreateRole) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a user<a name="iam_CreateUser_java_topic"></a>

The following code example shows how to create an IAM user\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

```
    public static String createIAMUser(IamClient iam, String username ) {

        try {
            // Create an IamWaiter object
            IamWaiter iamWaiter = iam.waiter();

            CreateUserRequest request = CreateUserRequest.builder()
                .userName(username)
                .build();

            CreateUserResponse response = iam.createUser(request);

            // Wait until the user is created
            GetUserRequest userRequest = GetUserRequest.builder()
                .userName(response.user().userName())
                .build();

            WaiterResponse<GetUserResponse> waitUntilUserExists = iamWaiter.waitUntilUserExists(userRequest);
            waitUntilUserExists.matched().response().ifPresent(System.out::println);
            return response.user().userName();

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
       return "";
    }
```
+  For API details, see [CreateUser](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/CreateUser) in *AWS SDK for Java 2\.x API Reference*\. 

### Create an access key<a name="iam_CreateAccessKey_java_topic"></a>

The following code example shows how to create an IAM access key\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

```
    public static String createIAMAccessKey(IamClient iam,String user) {

        try {
            CreateAccessKeyRequest request = CreateAccessKeyRequest.builder()
                .userName(user)
                .build();

            CreateAccessKeyResponse response = iam.createAccessKey(request);
            return response.accessKey().accessKeyId();

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateAccessKey](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/CreateAccessKey) in *AWS SDK for Java 2\.x API Reference*\. 

### Create an alias for an account<a name="iam_CreateAccountAlias_java_topic"></a>

The following code example shows how to create an alias for an IAM account\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

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
+  For API details, see [CreateAccountAlias](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/CreateAccountAlias) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a policy<a name="iam_DeletePolicy_java_topic"></a>

The following code example shows how to delete an IAM policy\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

```
    public static void deleteIAMPolicy(IamClient iam,String policyARN) {

        try {
            DeletePolicyRequest request = DeletePolicyRequest.builder()
                .policyArn(policyARN)
                .build();

            iam.deletePolicy(request);
            System.out.println("Successfully deleted the policy");

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        System.out.println("Done");
    }
```
+  For API details, see [DeletePolicy](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/DeletePolicy) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a user<a name="iam_DeleteUser_java_topic"></a>

The following code example shows how to delete an IAM user\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

```
    public static void deleteIAMUser(IamClient iam, String userName) {

        try {
            DeleteUserRequest request = DeleteUserRequest.builder()
                .userName(userName)
                .build();

            iam.deleteUser(request);
            System.out.println("Successfully deleted IAM user " + userName);

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteUser](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/DeleteUser) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete an access key<a name="iam_DeleteAccessKey_java_topic"></a>

The following code example shows how to delete an IAM access key\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

```
    public static void deleteKey(IamClient iam ,String username, String accessKey ) {

        try {
            DeleteAccessKeyRequest request = DeleteAccessKeyRequest.builder()
                .accessKeyId(accessKey)
                .userName(username)
                .build();

            iam.deleteAccessKey(request);
            System.out.println("Successfully deleted access key " + accessKey +
                " from user " + username);

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteAccessKey](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/DeleteAccessKey) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete an account alias<a name="iam_DeleteAccountAlias_java_topic"></a>

The following code example shows how to delete an IAM account alias\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

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
+  For API details, see [DeleteAccountAlias](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/DeleteAccountAlias) in *AWS SDK for Java 2\.x API Reference*\. 

### Detach a policy from a role<a name="iam_DetachRolePolicy_java_topic"></a>

The following code example shows how to detach an IAM policy from a role\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

```
    public static void detachPolicy(IamClient iam, String roleName, String policyArn ) {

        try {
            DetachRolePolicyRequest request = DetachRolePolicyRequest.builder()
                .roleName(roleName)
                .policyArn(policyArn)
                .build();

            iam.detachRolePolicy(request);
            System.out.println("Successfully detached policy " + policyArn +
                " from role " + roleName);

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DetachRolePolicy](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/DetachRolePolicy) in *AWS SDK for Java 2\.x API Reference*\. 

### List a user's access keys<a name="iam_ListAccessKeys_java_topic"></a>

The following code example shows how to list a user's IAM access keys\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

```
    public static void listKeys( IamClient iam,String userName ){

        try {
            boolean done = false;
            String newMarker = null;

            while (!done) {
                ListAccessKeysResponse response;

                if(newMarker == null) {
                    ListAccessKeysRequest request = ListAccessKeysRequest.builder()
                    .userName(userName)
                    .build();

                    response = iam.listAccessKeys(request);

                } else {
                    ListAccessKeysRequest request = ListAccessKeysRequest.builder()
                        .userName(userName)
                        .marker(newMarker)
                        .build();

                    response = iam.listAccessKeys(request);
                }

                for (AccessKeyMetadata metadata : response.accessKeyMetadata()) {
                    System.out.format("Retrieved access key %s", metadata.accessKeyId());
            }

            if (!response.isTruncated()) {
                done = true;
            } else {
                newMarker = response.marker();
            }
        }

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListAccessKeys](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/ListAccessKeys) in *AWS SDK for Java 2\.x API Reference*\. 

### List account aliases<a name="iam_ListAccountAliases_java_topic"></a>

The following code example shows how to list IAM account aliases\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

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
+  For API details, see [ListAccountAliases](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/ListAccountAliases) in *AWS SDK for Java 2\.x API Reference*\. 

### List users<a name="iam_ListUsers_java_topic"></a>

The following code example shows how to list IAM users\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

```
    public static void listAllUsers(IamClient iam ) {

        try {

            boolean done = false;
            String newMarker = null;

            while(!done) {
                ListUsersResponse response;
                if (newMarker == null) {
                    ListUsersRequest request = ListUsersRequest.builder().build();
                    response = iam.listUsers(request);
                } else {
                    ListUsersRequest request = ListUsersRequest.builder()
                        .marker(newMarker)
                        .build();

                    response = iam.listUsers(request);
                }

                for(User user : response.users()) {
                    System.out.format("\n Retrieved user %s", user.userName());
                    AttachedPermissionsBoundary permissionsBoundary = user.permissionsBoundary();
                    if (permissionsBoundary != null)
                        System.out.format("\n Permissions boundary details %s", permissionsBoundary.permissionsBoundaryTypeAsString());
                }

                if(!response.isTruncated()) {
                    done = true;
                } else {
                    newMarker = response.marker();
                }
            }

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListUsers](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/ListUsers) in *AWS SDK for Java 2\.x API Reference*\. 

### Update a user<a name="iam_UpdateUser_java_topic"></a>

The following code example shows how to update an IAM user\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

```
    public static void updateIAMUser(IamClient iam, String curName,String newName ) {

        try {
            UpdateUserRequest request = UpdateUserRequest.builder()
                .userName(curName)
                .newUserName(newName)
                .build();

            iam.updateUser(request);
            System.out.printf("Successfully updated user to username %s", newName);

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [UpdateUser](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/UpdateUser) in *AWS SDK for Java 2\.x API Reference*\. 

### Update an access key<a name="iam_UpdateAccessKey_java_topic"></a>

The following code example shows how to update an IAM access key\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
  

```
    public static void updateKey(IamClient iam, String username, String accessId, String status ) {

        try {
            if (status.toLowerCase().equalsIgnoreCase("active")) {
                statusType = StatusType.ACTIVE;
            } else if (status.toLowerCase().equalsIgnoreCase("inactive")) {
                 statusType = StatusType.INACTIVE;
            } else {
                statusType = StatusType.UNKNOWN_TO_SDK_VERSION;
            }

            UpdateAccessKeyRequest request = UpdateAccessKeyRequest.builder()
                .accessKeyId(accessId)
                .userName(username)
                .status(statusType)
                .build();

            iam.updateAccessKey(request);
            System.out.printf("Successfully updated the status of access key %s to" +
                        "status %s for user %s", accessId, status, username);

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [UpdateAccessKey](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/UpdateAccessKey) in *AWS SDK for Java 2\.x API Reference*\. 

## Scenarios<a name="w591aac15c14b9c37c15"></a>

### Create a user and assume a role<a name="iam_Scenario_CreateUserAssumeRole_java_topic"></a>

The following code example shows how to:
+ Create a user who has no permissions\.
+ Create a role that grants permission to list Amazon S3 buckets for the account\.
+ Add a policy to let the user assume the role\.
+ Assume the role and list Amazon S3 buckets using temporary credentials\.
+ Delete the policy, role, and user\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/iam#readme)\. 
Create functions that wrap IAM user actions\.  

```
public class IAMScenario {

    public static final String PolicyDocument =
            "{" +
                    "  \"Version\": \"2012-10-17\"," +
                    "  \"Statement\": [" +
                    "    {" +
                    "        \"Effect\": \"Allow\"," +
                    "        \"Action\": [" +
                    "            \"s3:*\"" +
                    "       ]," +
                    "       \"Resource\": \"*\"" +
                    "    }" +
                    "   ]" +
                    "}";

    public static void main(String[] args) throws Exception {

        final String usage = "\n" +
            "Usage:\n" +
            "    <username> <policyName> <roleName> <roleSessionName> <fileLocation> <bucketName> \n\n" +
            "Where:\n" +
            "    username - The name of the IAM user to create. \n\n" +
            "    policyName - The name of the policy to create. \n\n" +
            "    roleName - The name of the role to create. \n\n" +
            "    roleSessionName - The name of the session required for the assumeRole operation. \n\n" +
            "    fileLocation - The file location to the JSON required to create the role (see Readme). \n\n" +
            "    bucketName - The name of the Amazon S3 bucket from which objects are read. \n\n" ;

        if (args.length != 6) {
            System.out.println(usage);
            System.exit(1);
        }

        String userName = args[0];
        String policyName = args[1];
        String roleName = args[2];
        String roleSessionName = args[3];
        String fileLocation = args[4];
        String bucketName = args[5];

        Region region = Region.AWS_GLOBAL;
        IamClient iam = IamClient.builder()
            .region(region)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

        // Create the IAM user.
        Boolean createUser = createIAMUser(iam, userName);

       if (createUser) {
           System.out.println(userName + " was successfully created.");
           String polArn = createIAMPolicy(iam, policyName);
           System.out.println("The policy " + polArn + " was successfully created.");
           String roleArn = createIAMRole(iam, roleName, fileLocation);
           System.out.println(roleArn + " was successfully created.");
           attachIAMRolePolicy(iam, roleName, polArn);

           System.out.println("*** Wait for 1 MIN so the resource is available");
           TimeUnit.MINUTES.sleep(1);
           assumeGivenRole(roleArn, roleSessionName, bucketName);

           System.out.println("*** Getting ready to delete the AWS resources");
           deleteRole(iam, roleName, polArn);
           deleteIAMUser(iam, userName);
           System.out.println("This IAM Scenario has successfully completed");
       } else {
           System.out.println(userName +" was not successfully created.");
       }
    }

    public static Boolean createIAMUser(IamClient iam, String username ) {

        try {
            // Create an IamWaiter object
            IamWaiter iamWaiter = iam.waiter();
            CreateUserRequest request = CreateUserRequest.builder()
                .userName(username)
                .build();

            // Wait until the user is created.
            CreateUserResponse response = iam.createUser(request);
            GetUserRequest userRequest = GetUserRequest.builder()
                .userName(response.user().userName())
                .build();

            WaiterResponse<GetUserResponse> waitUntilUserExists = iamWaiter.waitUntilUserExists(userRequest);
            waitUntilUserExists.matched().response().ifPresent(System.out::println);
            return true;

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return false;
    }

    public static String createIAMRole(IamClient iam, String rolename, String fileLocation ) throws Exception {

        try {
            JSONObject jsonObject = (JSONObject) readJsonSimpleDemo(fileLocation);
            CreateRoleRequest request = CreateRoleRequest.builder()
                .roleName(rolename)
                .assumeRolePolicyDocument(jsonObject.toJSONString())
                .description("Created using the AWS SDK for Java")
                .build();

            CreateRoleResponse response = iam.createRole(request);
            System.out.println("The ARN of the role is "+response.role().arn());
            return response.role().arn();

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }

    public static String createIAMPolicy(IamClient iam, String policyName ) {

        try {
            // Create an IamWaiter object.
            IamWaiter iamWaiter = iam.waiter();
            CreatePolicyRequest request = CreatePolicyRequest.builder()
                .policyName(policyName)
                .policyDocument(PolicyDocument).build();

            CreatePolicyResponse response = iam.createPolicy(request);

            // Wait until the policy is created.
            GetPolicyRequest polRequest = GetPolicyRequest.builder()
                .policyArn(response.policy().arn())
                .build();

            WaiterResponse<GetPolicyResponse> waitUntilPolicyExists = iamWaiter.waitUntilPolicyExists(polRequest);
            waitUntilPolicyExists.matched().response().ifPresent(System.out::println);
            return response.policy().arn();

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "" ;
    }

    public static void attachIAMRolePolicy(IamClient iam, String roleName, String policyArn ) {

        try {
            ListAttachedRolePoliciesRequest request = ListAttachedRolePoliciesRequest.builder()
                .roleName(roleName)
                .build();

            ListAttachedRolePoliciesResponse response = iam.listAttachedRolePolicies(request);
            List<AttachedPolicy> attachedPolicies = response.attachedPolicies();

            String polArn;
            for (AttachedPolicy policy: attachedPolicies) {
                polArn = policy.policyArn();
                if (polArn.compareTo(policyArn)==0) {
                    System.out.println(roleName + " policy is already attached to this role.");
                    return;
                }
            }

            AttachRolePolicyRequest attachRequest = AttachRolePolicyRequest.builder()
                .roleName(roleName)
                .policyArn(policyArn)
                .build();

            iam.attachRolePolicy(attachRequest);
            System.out.println("Successfully attached policy " + policyArn + " to role " + roleName);

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    // Invoke an Amazon S3 operation using the Assumed Role.
    public static void assumeGivenRole(String roleArn, String roleSessionName, String bucketName) {

        StsClient stsClient = StsClient.builder()
            .region(Region.US_EAST_1)
            .build();

        try {
            AssumeRoleRequest roleRequest = AssumeRoleRequest.builder()
                .roleArn(roleArn)
                .roleSessionName(roleSessionName)
                .build();

            AssumeRoleResponse roleResponse = stsClient.assumeRole(roleRequest);
            Credentials myCreds = roleResponse.credentials();
            String key = myCreds.accessKeyId();
            String secKey = myCreds.secretAccessKey();
            String secToken = myCreds.sessionToken();

            // List all objects in an Amazon S3 bucket using the temp creds.
            Region region = Region.US_EAST_1;
            S3Client s3 = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsSessionCredentials.create(key, secKey, secToken)))
                .region(region)
                .build();

            System.out.println("Created a S3Client using temp credentials.");
            System.out.println("Listing objects in "+bucketName);
            ListObjectsRequest listObjects = ListObjectsRequest.builder()
                .bucket(bucketName)
                .build();

            ListObjectsResponse res = s3.listObjects(listObjects);
            List<S3Object> objects = res.contents();
            for (S3Object myValue : objects) {
                System.out.println("The name of the key is " + myValue.key());
                System.out.println("The owner is " + myValue.owner());
            }

        } catch (StsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void deleteRole(IamClient iam, String roleName, String polArn) {

        try {
            // First the policy needs to be detached.
            DetachRolePolicyRequest rolePolicyRequest = DetachRolePolicyRequest.builder()
                .policyArn(polArn)
                .roleName(roleName)
                .build();

            iam.detachRolePolicy(rolePolicyRequest);

            // Delete the policy.
            DeletePolicyRequest request = DeletePolicyRequest.builder()
                .policyArn(polArn)
                .build();

            iam.deletePolicy(request);
            System.out.println("*** Successfully deleted "+polArn);

            // Delete the role.
            DeleteRoleRequest roleRequest = DeleteRoleRequest.builder()
                .roleName(roleName)
                .build();

            iam.deleteRole(roleRequest);
            System.out.println("*** Successfully deleted " +roleName);

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void deleteIAMUser(IamClient iam, String userName) {

        try {
            DeleteUserRequest request = DeleteUserRequest.builder()
                .userName(userName)
                .build();

            iam.deleteUser(request);
            System.out.println("*** Successfully deleted " + userName);

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static Object readJsonSimpleDemo(String filename) throws Exception {
        FileReader reader = new FileReader(filename);
        JSONParser jsonParser = new JSONParser();
        return jsonParser.parse(reader);
    }
}
```
+ For API details, see the following topics in *AWS SDK for Java 2\.x API Reference*\.
  + [AttachRolePolicy](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/AttachRolePolicy)
  + [CreateAccessKey](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/CreateAccessKey)
  + [CreatePolicy](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/CreatePolicy)
  + [CreateRole](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/CreateRole)
  + [CreateUser](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/CreateUser)
  + [DeleteAccessKey](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/DeleteAccessKey)
  + [DeletePolicy](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/DeletePolicy)
  + [DeleteRole](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/DeleteRole)
  + [DeleteUser](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/DeleteUser)
  + [DeleteUserPolicy](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/DeleteUserPolicy)
  + [DetachRolePolicy](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/DetachRolePolicy)
  + [PutUserPolicy](https://docs.aws.amazon.com/goto/SdkForJavaV2/iam-2010-05-08/PutUserPolicy)