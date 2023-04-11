# Working with Amazon Cognito<a name="examples-cognito"></a>

With Amazon Cognito, you can quickly add user sign\-up or sign\-in capability to your web or mobile app\. The examples here demonstrate some of the basic functionality of Amazon Cognito\.

## Create a user pool<a name="cognito-create-userpool"></a>

A user pool is a directory of users that you can configure for your web or mobile app\.

To create a user pool, start by building a [CreateUserPoolRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cognitoidentityprovider/model/CreateUserPoolRequest.html) object, with the name of the user pool as the value of its `poolName()`\. Call the `createUserPool()` method of your [CreateUserPoolRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cognitoidentityprovider/CognitoIdentityProviderClient.html), passing in the `CreateUserPoolRequest` object\. You can capture the result of this request as a [CreateUserPoolResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cognitoidentityprovider/model/CreateUserPoolResponse.html) object, as demonstrated in the following code snippet\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolResponse;
```

 **Code** 

```
    public static String createPool(CognitoIdentityProviderClient cognitoClient, String userPoolName ) {

        try {
            CreateUserPoolResponse response = cognitoClient.createUserPool(
                    CreateUserPoolRequest.builder()
                            .poolName(userPoolName)
                            .build()
            );
            return response.userPool().id();

        } catch (CognitoIdentityProviderException e){
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/cognito/src/main/java/com/example/cognito/CreateUserPool.java) on GitHub\.

## List users from a user pool<a name="cognito-list-userpool"></a>

To list users from your user pools, start by building a [ListUserPoolsRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cognitoidentityprovider/model/ListUserPoolsRequest.html) object, with the number of maximum results as the value of its `maxResults()`\. Call the `listUserPools()` method of your `CognitoIdentityProviderClient`, passing in the `ListUserPoolsRequest` object\. You can capture the result of this request as a [ListUserPoolsResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cognitoidentityprovider/model/ListUserPoolsResponse.html) object, as demonstrated in the following code snippet\. Create a [UserPoolDescriptionType](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cognitoidentityprovider/model/UserPoolDescriptionType.html) object to easily iterate over the results and pull out the attributes of each user\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolsResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolsRequest;
```

 **Code** 

```
    public static void listAllUserPools(CognitoIdentityProviderClient cognitoClient ) {

        try {
            ListUserPoolsRequest request = ListUserPoolsRequest.builder()
                    .maxResults(10)
                    .build();

            ListUserPoolsResponse response = cognitoClient.listUserPools(request);
            response.userPools().forEach(userpool -> {
                        System.out.println("User pool " + userpool.name() + ", User ID " + userpool.id() );
                    }
            );

        } catch (CognitoIdentityProviderException e){
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/cognito/src/main/java/com/example/cognito/ListUserPools.java) on GitHub\.

## Create an identity pool<a name="cognito-create-identitypool"></a>

An identity pool is a container that organizes the IDs from your external identity provider, keeping a unique identifier for each user\. To create an identity pool, start by building a [CreateIdentityPoolRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cognitoidentity/model/CreateIdentityPoolRequest.html) with the name of the user pool as the value of its `identityPoolName()`\. Set `allowUnauthenticatedIdentities()` to `true` or `false`\. Call the `createIdentityPool()` method of your `CognitoIdentityClient` object, passing in the `CreateIdentityPoolRequest` object\. You can capture the result of this request as a [CreateIdentityPoolResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cognitoidentity/model/CreateIdentityPoolResponse.html) object, as demonstrated in the following code snippet\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentity.model.CreateIdentityPoolRequest;
import software.amazon.awssdk.services.cognitoidentity.model.CreateIdentityPoolResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
```

 **Code** 

```
    public static String createIdPool(CognitoIdentityClient cognitoClient, String identityPoolName ) {

        try {
            CreateIdentityPoolRequest poolRequest = CreateIdentityPoolRequest.builder()
                    .allowUnauthenticatedIdentities(false)
                    .identityPoolName(identityPoolName)
                    .build() ;

            CreateIdentityPoolResponse response = cognitoClient.createIdentityPool(poolRequest);
            return response.identityPoolId();

        } catch (CognitoIdentityProviderException e){
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/cognito/src/main/java/com/example/cognito/CreateIdentityPool.java) on GitHub\.

## Add an app client<a name="cognito-add-appclient"></a>

To enable the hosted web sign\-up or sign\-in UI for your app, create an app client\. To create an app client, start by building a [CreateUserPoolClientRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cognitoidentityprovider/model/CreateUserPoolClientRequest.html) object, with the name of the client as the value of its `clientName()`\. Set `userPoolId()` to the ID of the user pool to which you want to attach this app client\. Call the `createUserPoolClient()` method of your `CognitoIdentityProviderClient`, passing in the `CreateUserPoolClientRequest` object\. You can capture the result of this request as a [CreateUserPoolClientResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cognitoidentityprovider/model/CreateUserPoolClientResponse.html) object, as demonstrated in the following code snippet\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolClientRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolClientResponse;
```

 **Code** 

```
    public static void createPoolClient ( CognitoIdentityProviderClient cognitoClient,
                                          String clientName,
                                          String userPoolId ) {

        try {

            CreateUserPoolClientResponse response = cognitoClient.createUserPoolClient(
                    CreateUserPoolClientRequest.builder()
                            .clientName(clientName)
                            .userPoolId(userPoolId)
                            .build()
            );

            System.out.println("User pool " + response.userPoolClient().clientName() + " created. ID: " + response.userPoolClient().clientId());

        } catch (CognitoIdentityProviderException e){
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/cognito/src/main/java/com/example/cognito/CreateUserPoolClient.java) on GitHub\.

## Add a third\-party identity provider<a name="cognito-thirdparty-idp"></a>

Adding an external identity provider \(IdP\) enables your users to log into your app using that service’s login mechanism\. To add a third\-party IdP, start by building an [UpdateIdentityPoolRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cognitoidentity/model/UpdateIdentityPoolRequest.html) object, with the name of the identity pool as the value of its `identityPoolName()`\. Set `allowUnauthenticatedIdentities()` to `true` or `false`, specify the `identityPoolId()`, and define which login providers will be supported with `supportedLoginProviders()`\. Call the `updateIdentityPool()` method of your `CognitoIdentityClient`, passing in the `UpdateIdentityPoolRequest` object\. You can capture the result of this request as an [UpdateIdentityPoolResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cognitoidentity/model/UpdateIdentityPoolResponse.html) object, as demonstrated in the following code snippet\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentity.model.CognitoIdentityProvider;
import software.amazon.awssdk.services.cognitoidentity.model.UpdateIdentityPoolRequest;
import software.amazon.awssdk.services.cognitoidentity.model.UpdateIdentityPoolResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import java.util.ArrayList;
import java.util.List;
```

 **Code** 

```
    public static void createNewUser(CognitoIdentityProviderClient cognitoClient,
                                   String userPoolId,
                                   String name,
                                   String email,
                                   String password){

        try{

            AttributeType userAttrs = AttributeType.builder()
                    .name("email")
                    .value(email)
                    .build();

            AdminCreateUserRequest userRequest = AdminCreateUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(name)
                    .temporaryPassword(password)
                    .userAttributes(userAttrs)
                    .messageAction("SUPPRESS")
                    .build() ;

            AdminCreateUserResponse response = cognitoClient.adminCreateUser(userRequest);
            System.out.println("User " + response.user().username() + "is created. Status: " + response.user().userStatus());

        } catch (CognitoIdentityProviderException e){
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/cognito/src/main/java/com/example/cognito/AddLoginProvider.java) on GitHub\.

## Get credentials for an ID<a name="cognito-get-credentials"></a>

To get the credentials for an identity in an identity pool, first build a [GetCredentialsForIdentityRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cognitoidentity/model/GetCredentialsForIdentityRequest.html) with the identity ID as the value of its `identityId()`\. Call the `getCredentialsForIdentity()` method of your `CognitoIdentityClient`, passing in the `GetCredentialsForIdentityRequest`\. You can capture the result of this request as a [GetCredentialsForIdentityResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cognitoidentity/model/GetCredentialsForIdentityResponse.html) object, as demonstrated in the following code snippet\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentity.model.GetCredentialsForIdentityRequest;
import software.amazon.awssdk.services.cognitoidentity.model.GetCredentialsForIdentityResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
```

 **Code** 

```
    public static void getCredsForIdentity(CognitoIdentityClient cognitoClient, String identityId) {

        try {
            GetCredentialsForIdentityRequest getCredentialsForIdentityRequest = GetCredentialsForIdentityRequest.builder()
                    .identityId(identityId)
                    .build();

            GetCredentialsForIdentityResponse response = cognitoClient.getCredentialsForIdentity(getCredentialsForIdentityRequest);
            System.out.println("Identity ID " + response.identityId() + ", Access key ID " + response.credentials().accessKeyId());

        } catch (CognitoIdentityProviderException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/cognito/src/main/java/com/example/cognito/GetIdentityCredentials.java) on GitHub\.

For more information, see the [Amazon Cognito Developer Guide](http://docs.aws.amazon.com/cognito/latest/developerguide/)\.