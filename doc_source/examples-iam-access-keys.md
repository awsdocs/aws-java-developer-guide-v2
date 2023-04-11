# Managing IAM access keys<a name="examples-iam-access-keys"></a>

## Create an access key<a name="create-an-access-key"></a>

To create an IAM access key, call the IamClient’s `createAccessKey` method with a [CreateAccessKeyRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/CreateAccessKeyRequest.html) object\.

**Note**  
You must set the region to **AWS\_GLOBAL** for IamClient calls to work because IAM is a global service\.

 **Imports** 

```
import software.amazon.awssdk.services.iam.model.CreateAccessKeyRequest;
import software.amazon.awssdk.services.iam.model.CreateAccessKeyResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.IamException;
```

 **Code** 

```
    public static String createIAMAccessKey(IamClient iam,String user) {

        try {
            CreateAccessKeyRequest request = CreateAccessKeyRequest.builder()
                .userName(user).build();

            CreateAccessKeyResponse response = iam.createAccessKey(request);
           String keyId = response.accessKey().accessKeyId();
           return keyId;

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/CreateAccessKey.java) on GitHub\.

## List access keys<a name="list-access-keys"></a>

To list the access keys for a given user, create a [ListAccessKeysRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/ListAccessKeysRequest.html) object that contains the user name to list keys for, and pass it to the IamClient’s `listAccessKeys` method\.

**Note**  
If you do not supply a user name to `listAccessKeys`, it will attempt to list access keys associated with the AWS account that signed the request\.

 **Imports** 

```
import software.amazon.awssdk.services.iam.model.AccessKeyMetadata;
import software.amazon.awssdk.services.iam.model.IamException;
import software.amazon.awssdk.services.iam.model.ListAccessKeysRequest;
import software.amazon.awssdk.services.iam.model.ListAccessKeysResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
```

 **Code** 

```
    public static void listKeys( IamClient iam,String userName ){

        try {
            boolean done = false;
            String newMarker = null;

            while (!done) {
                ListAccessKeysResponse response;

            if(newMarker == null) {
                ListAccessKeysRequest request = ListAccessKeysRequest.builder()
                        .userName(userName).build();
                response = iam.listAccessKeys(request);
            } else {
                ListAccessKeysRequest request = ListAccessKeysRequest.builder()
                        .userName(userName)
                        .marker(newMarker).build();
                response = iam.listAccessKeys(request);
            }

            for (AccessKeyMetadata metadata :
                    response.accessKeyMetadata()) {
                System.out.format("Retrieved access key %s",
                        metadata.accessKeyId());
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

The results of `listAccessKeys` are paged \(with a default maximum of 100 records per call\)\. You can call `isTruncated` on the returned [ListAccessKeysResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/ListAccessKeysResponse.html) object to see if the query returned fewer results then are available\. If so, then call `marker` on the `ListAccessKeysResponse` and use it when creating a new request\. Use that new request in the next invocation of `listAccessKeys`\.

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/ListAccessKeys.java) on GitHub\.

## Retrieve an access key’s last used time<a name="retrieve-an-access-key-s-last-used-time"></a>

To get the time an access key was last used, call the IamClient’s `getAccessKeyLastUsed` method with the access key’s ID \(which can be passed in using a [GetAccessKeyLastUsedRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/GetAccessKeyLastUsedRequest.html) object\.

You can then use the returned [GetAccessKeyLastUsedResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/GetAccessKeyLastUsedResponse.html) object to retrieve the key’s last used time\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.GetAccessKeyLastUsedRequest;
import software.amazon.awssdk.services.iam.model.GetAccessKeyLastUsedResponse;
import software.amazon.awssdk.services.iam.model.IamException;
```

 **Code** 

```
    public static void getAccessKeyLastUsed(IamClient iam, String accessId ){

        try {
            GetAccessKeyLastUsedRequest request = GetAccessKeyLastUsedRequest.builder()
                    .accessKeyId(accessId).build();

            GetAccessKeyLastUsedResponse response = iam.getAccessKeyLastUsed(request);

            System.out.println("Access key was last used at: " +
                    response.accessKeyLastUsed().lastUsedDate());

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        System.out.println("Done");
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/AccessKeyLastUsed.java) on GitHub\.

## Activate or deactivate access keys<a name="iam-access-keys-update"></a>

You can activate or deactivate an access key by creating an [UpdateAccessKeyRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/UpdateAccessKeyRequest.html) object, providing the access key ID, optionally the user name, and the desired [status](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/StatusType.html), then passing the request object to the IamClient’s `updateAccessKey` method\.

 **Imports** 

```
import software.amazon.awssdk.services.iam.model.IamException;
import software.amazon.awssdk.services.iam.model.StatusType;
import software.amazon.awssdk.services.iam.model.UpdateAccessKeyRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
```

 **Code** 

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

              System.out.printf(
                "Successfully updated the status of access key %s to" +
                        "status %s for user %s", accessId, status, username);

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/UpdateAccessKey.java) on GitHub\.

## Delete an access key<a name="delete-an-access-key"></a>

To permanently delete an access key, call the IamClient’s `deleteKey` method, providing it with a [DeleteAccessKeyRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/DeleteAccessKeyRequest.html) containing the access key’s ID and username\.

**Note**  
Once deleted, a key can no longer be retrieved or used\. To temporarily deactivate a key so that it can be activated again later, use [updateAccessKey](#iam-access-keys-update) method instead\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.DeleteAccessKeyRequest;
import software.amazon.awssdk.services.iam.model.IamException;
```

 **Code** 

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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/DeleteAccessKey.java) on GitHub\.

## More information<a name="more-information"></a>
+  [CreateAccessKey](https://docs.aws.amazon.com/IAM/latest/APIReference/API_CreateAccessKey.html) in the IAM API Reference
+  [ListAccessKeys](https://docs.aws.amazon.com/IAM/latest/APIReference/API_ListAccessKeys.html) in the IAM API Reference
+  [GetAccessKeyLastUsed](https://docs.aws.amazon.com/IAM/latest/APIReference/API_GetAccessKeyLastUsed.html) in the IAM API Reference
+  [UpdateAccessKey](https://docs.aws.amazon.com/IAM/latest/APIReference/API_UpdateAccessKey.html) in the IAM API Reference
+  [DeleteAccessKey](https://docs.aws.amazon.com/IAM/latest/APIReference/API_DeleteAccessKey.html) in the IAM API Reference