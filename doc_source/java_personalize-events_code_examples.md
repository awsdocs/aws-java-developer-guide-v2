# Amazon Personalize Events examples using SDK for Java 2\.x<a name="java_personalize-events_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Amazon Personalize Events\.

*Actions* are code excerpts that show you how to call individual service functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple functions within the same service\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#actions)

## Actions<a name="actions"></a>

### Import real\-time interaction event data<a name="personalize-events_putEvents_java_topic"></a>

The following code example shows how to import real\-time interaction event data into Amazon Personalize Events\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static int putItems(PersonalizeEventsClient personalizeEventsClient,
                               String datasetArn,
                               String item1Id,
                               String item1PropertyName,
                               String item1PropertyValue,
                               String item2Id,
                               String item2PropertyName,
                               String item2PropertyValue) {

        int responseCode = 0;
        ArrayList<Item> items = new ArrayList<>();

        try {
            Item item1 = Item.builder()
                    .itemId(item1Id)
                    .properties(String.format("{\"%1$s\": \"%2$s\"}",
                            item1PropertyName, item1PropertyValue))
                    .build();

            items.add(item1);

            Item item2 = Item.builder()
                    .itemId(item2Id)
                    .properties(String.format("{\"%1$s\": \"%2$s\"}",
                            item2PropertyName, item2PropertyValue))
                    .build();

            items.add(item2);

            PutItemsRequest putItemsRequest = PutItemsRequest.builder()
                    .datasetArn(datasetArn)
                    .items(items)
                    .build();

            responseCode = personalizeEventsClient.putItems(putItemsRequest).sdkHttpResponse().statusCode();
            System.out.println("Response code: " + responseCode);
            return responseCode;

        } catch (PersonalizeEventsException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
        }
        return responseCode;
    }
```
+  For API details, see [PutEvents](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-events-2018-03-22/PutEvents) in *AWS SDK for Java 2\.x API Reference*\. 

### Incrementally import a user<a name="personalize-events_putUsers_java_topic"></a>

The following code example shows how to incrementally import a user into Amazon Personalize Events Events\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static int putUsers(PersonalizeEventsClient personalizeEventsClient,
                               String datasetArn,
                               String user1Id,
                               String user1PropertyName,
                               String user1PropertyValue,
                               String user2Id,
                               String user2PropertyName,
                               String user2PropertyValue) {

        int responseCode = 0;
        ArrayList<User> users = new ArrayList<>();

        try {
            User user1 = User.builder()
                    .userId(user1Id)
                    .properties(String.format("{\"%1$s\": \"%2$s\"}",
                            user1PropertyName, user1PropertyValue))
                    .build();

            users.add(user1);

            User user2 = User.builder()
                    .userId(user2Id)
                    .properties(String.format("{\"%1$s\": \"%2$s\"}",
                            user2PropertyName, user2PropertyValue))
                    .build();

            users.add(user2);

            PutUsersRequest putUsersRequest = PutUsersRequest.builder()
                    .datasetArn(datasetArn)
                    .users(users)
                    .build();

            responseCode = personalizeEventsClient.putUsers(putUsersRequest).sdkHttpResponse().statusCode();
            System.out.println("Response code: " + responseCode);
            return responseCode;

        } catch (PersonalizeEventsException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
        }
        return responseCode;
    }
```
+  For API details, see [PutUsers](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-events-2018-03-22/PutUsers) in *AWS SDK for Java 2\.x API Reference*\. 