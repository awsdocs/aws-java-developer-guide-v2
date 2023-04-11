# Managing IAM Users<a name="examples-iam-users"></a>

## Creating a User<a name="creating-a-user"></a>

Create a new IAM user by providing the user name to the IamClient’s `createUser` method using a [CreateUserRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/CreateUserRequest.html) object containing the user name\.

 **Imports** 

```
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.iam.model.CreateUserRequest;
import software.amazon.awssdk.services.iam.model.CreateUserResponse;
import software.amazon.awssdk.services.iam.model.IamException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.waiters.IamWaiter;
import software.amazon.awssdk.services.iam.model.GetUserRequest;
import software.amazon.awssdk.services.iam.model.GetUserResponse;
```

 **Code** 

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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/CreateUser.java) on GitHub\.

## Listing Users<a name="listing-users"></a>

To list the IAM users for your account, create a new [ListUsersRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/ListUsersRequest.html) and pass it to the IamClient’s `listUsers` method\. You can retrieve the list of users by calling `users` on the returned [ListUsersResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/ListUsersResponse.html) object\.

The list of users returned by `listUsers` is paged\. You can check to see there are more results to retrieve by calling the response object’s `isTruncated` method\. If it returns `true`, then call the response object’s `marker()` method\. Use the marker value to create a new request object\. Then call the `listUsers` method again with the new request\.

 **Imports** 

```
import software.amazon.awssdk.services.iam.model.IamException;
import software.amazon.awssdk.services.iam.model.ListUsersRequest;
import software.amazon.awssdk.services.iam.model.ListUsersResponse;
import software.amazon.awssdk.services.iam.model.User;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
```

 **Code** 

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
                        .marker(newMarker).build();
                    response = iam.listUsers(request);
                }

                for(User user : response.users()) {
                 System.out.format("\n Retrieved user %s", user.userName());
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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/ListUsers.java) on GitHub\.

## Updating a User<a name="updating-a-user"></a>

To update a user, call the IamClient object’s `updateUser` method, which takes a [UpdateUserRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/UpdateUserRequest.html) object that you can use to change the user’s *name* or *path*\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.IamException;
import software.amazon.awssdk.services.iam.model.UpdateUserRequest;
```

 **Code** 

```
    public static void updateIAMUser(IamClient iam, String curName,String newName ) {

        try {
            UpdateUserRequest request = UpdateUserRequest.builder()
                    .userName(curName)
                    .newUserName(newName)
                    .build();

            iam.updateUser(request);
            System.out.printf("Successfully updated user to username %s",
                newName);
        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
      }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/UpdateUser.java) on GitHub\.

## Deleting a User<a name="deleting-a-user"></a>

To delete a user, call the IamClient’s `deleteUser` request with a [UpdateUserRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/UpdateUserRequest.html) object set with the user name to delete\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.DeleteUserRequest;
import software.amazon.awssdk.services.iam.model.IamException;
```

 **Code** 

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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/DeleteUser.java) on GitHub\.

## More Information<a name="more-information"></a>
+  [IAM Users](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users.html) in the IAM User Guide
+  [Managing IAM Users](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_manage.html) in the IAM User Guide
+  [CreateUser](https://docs.aws.amazon.com/IAM/latest/APIReference/API_CreateUser.html) in the IAM API Reference
+  [ListUsers](https://docs.aws.amazon.com/IAM/latest/APIReference/API_ListUsers.html) in the IAM API Reference
+  [UpdateUser](https://docs.aws.amazon.com/IAM/latest/APIReference/API_UpdateUser.html) in the IAM API Reference
+  [DeleteUser](https://docs.aws.amazon.com/IAM/latest/APIReference/API_DeleteUser.html) in the IAM API Reference