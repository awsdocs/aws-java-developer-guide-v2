--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Amazon SNS examples using SDK for Java 2\.x<a name="java_sns_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Amazon SNS\.

*Actions* are code excerpts that show you how to call individual Amazon SNS functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple Amazon SNS functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w591aac15c14b9c63c13)

## Actions<a name="w591aac15c14b9c63c13"></a>

### Add tags to a topic<a name="sns_TagResource_java_topic"></a>

The following code example shows how to add tags to an Amazon SNS topic\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void addTopicTags(SnsClient snsClient, String topicArn) {

        try {
            Tag tag = Tag.builder()
                .key("Team")
                .value("Development")
                .build();

            Tag tag2 = Tag.builder()
                .key("Environment")
                .value("Gamma")
                .build();

            List<Tag> tagList = new ArrayList<>();
            tagList.add(tag);
            tagList.add(tag2);

            TagResourceRequest tagResourceRequest = TagResourceRequest.builder()
                .resourceArn(topicArn)
                .tags(tagList)
                .build();

            snsClient.tagResource(tagResourceRequest);
            System.out.println("Tags have been added to "+topicArn);

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [TagResource](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/TagResource) in *AWS SDK for Java 2\.x API Reference*\. 

### Check whether a phone number is opted out<a name="sns_CheckIfPhoneNumberIsOptedOut_java_topic"></a>

The following code example shows how to check whether a phone number is opted out of receiving Amazon SNS messages\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void checkPhone(SnsClient snsClient, String phoneNumber) {

        try {
            CheckIfPhoneNumberIsOptedOutRequest request = CheckIfPhoneNumberIsOptedOutRequest.builder()
                .phoneNumber(phoneNumber)
                .build();

            CheckIfPhoneNumberIsOptedOutResponse result = snsClient.checkIfPhoneNumberIsOptedOut(request);
            System.out.println(result.isOptedOut() + "Phone Number " + phoneNumber + " has Opted Out of receiving sns messages." +
                "\n\nStatus was " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [CheckIfPhoneNumberIsOptedOut](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/CheckIfPhoneNumberIsOptedOut) in *AWS SDK for Java 2\.x API Reference*\. 

### Confirm an endpoint owner wants to receive messages<a name="sns_ConfirmSubscription_java_topic"></a>

The following code example shows how to confirm the owner of an endpoint wants to receive Amazon SNS messages by validating the token sent to the endpoint by an earlier Subscribe action\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void confirmSub(SnsClient snsClient, String subscriptionToken, String topicArn ) {

        try {
            ConfirmSubscriptionRequest request = ConfirmSubscriptionRequest.builder()
                .token(subscriptionToken)
                .topicArn(topicArn)
                .build();

            ConfirmSubscriptionResponse result = snsClient.confirmSubscription(request);
            System.out.println("\n\nStatus was " + result.sdkHttpResponse().statusCode() + "\n\nSubscription Arn: \n\n" + result.subscriptionArn());

        } catch (SnsException e) {
        System.err.println(e.awsErrorDetails().errorMessage());
        System.exit(1);
        }
    }
```
+  For API details, see [ConfirmSubscription](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/ConfirmSubscription) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a topic<a name="sns_CreateTopic_java_topic"></a>

The following code example shows how to create an Amazon SNS topic\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static String createSNSTopic(SnsClient snsClient, String topicName ) {

        CreateTopicResponse result = null;
        try {
            CreateTopicRequest request = CreateTopicRequest.builder()
                .name(topicName)
                .build();

            result = snsClient.createTopic(request);
            return result.topicArn();

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateTopic](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/CreateTopic) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a subscription<a name="sns_Unsubscribe_java_topic"></a>

The following code example shows how to delete an Amazon SNS subscription\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void unSub(SnsClient snsClient, String subscriptionArn) {

        try {
            UnsubscribeRequest request = UnsubscribeRequest.builder()
                .subscriptionArn(subscriptionArn)
                .build();

            UnsubscribeResponse result = snsClient.unsubscribe(request);
            System.out.println("\n\nStatus was " + result.sdkHttpResponse().statusCode()
                + "\n\nSubscription was removed for " + request.subscriptionArn());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [Unsubscribe](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/Unsubscribe) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a topic<a name="sns_DeleteTopic_java_topic"></a>

The following code example shows how to delete an Amazon SNS topic and all subscriptions to that topic\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void deleteSNSTopic(SnsClient snsClient, String topicArn ) {

        try {
            DeleteTopicRequest request = DeleteTopicRequest.builder()
                .topicArn(topicArn)
                .build();

            DeleteTopicResponse result = snsClient.deleteTopic(request);
            System.out.println("\n\nStatus was " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteTopic](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/DeleteTopic) in *AWS SDK for Java 2\.x API Reference*\. 

### Get the properties of a topic<a name="sns_GetTopicAttributes_java_topic"></a>

The following code example shows how to get the properties of an Amazon SNS topic\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void getSNSTopicAttributes(SnsClient snsClient, String topicArn ) {

        try {
            GetTopicAttributesRequest request = GetTopicAttributesRequest.builder()
                .topicArn(topicArn)
                .build();

            GetTopicAttributesResponse result = snsClient.getTopicAttributes(request);
            System.out.println("\n\nStatus is " + result.sdkHttpResponse().statusCode() + "\n\nAttributes: \n\n" + result.attributes());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [GetTopicAttributes](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/GetTopicAttributes) in *AWS SDK for Java 2\.x API Reference*\. 

### Get the settings for sending SMS messages<a name="sns_GetSMSAttributes_java_topic"></a>

The following code example shows how to get the settings for sending Amazon SNS SMS messages\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void getSNSAttrutes(SnsClient snsClient,String topicArn ) {

        try {
            GetSubscriptionAttributesRequest request = GetSubscriptionAttributesRequest.builder()
                .subscriptionArn(topicArn)
                .build();

            // Get the Subscription attributes
            GetSubscriptionAttributesResponse res = snsClient.getSubscriptionAttributes(request);
            Map<String, String> map = res.attributes();

            // Iterate through the map
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                System.out.println("[Key] : " + entry.getKey() + " [Value] : " + entry.getValue());
            }

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        System.out.println("\n\nStatus was good");
    }
```
+  For API details, see [GetSMSAttributes](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/GetSMSAttributes) in *AWS SDK for Java 2\.x API Reference*\. 

### List opted out phone numbers<a name="sns_ListPhoneNumbersOptedOut_java_topic"></a>

The following code example shows how to list phone numbers that are opted out of receiving Amazon SNS messages\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void listOpts( SnsClient snsClient) {

        try {
            ListPhoneNumbersOptedOutRequest request = ListPhoneNumbersOptedOutRequest.builder().build();
            ListPhoneNumbersOptedOutResponse result = snsClient.listPhoneNumbersOptedOut(request);
            System.out.println("Status is " + result.sdkHttpResponse().statusCode() + "\n\nPhone Numbers: \n\n" + result.phoneNumbers());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListPhoneNumbersOptedOut](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/ListPhoneNumbersOptedOut) in *AWS SDK for Java 2\.x API Reference*\. 

### List the subscribers of a topic<a name="sns_ListSubscriptions_java_topic"></a>

The following code example shows how to retrieve the list of subscribers of an Amazon SNS topic\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void listSNSSubscriptions( SnsClient snsClient) {

        try {
            ListSubscriptionsRequest request = ListSubscriptionsRequest.builder()
                .build();

            ListSubscriptionsResponse result = snsClient.listSubscriptions(request);
            System.out.println(result.subscriptions());

        } catch (SnsException e) {

            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListSubscriptions](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/ListSubscriptions) in *AWS SDK for Java 2\.x API Reference*\. 

### List topics<a name="sns_ListTopics_java_topic"></a>

The following code example shows how to list Amazon SNS topics\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void listSNSTopics(SnsClient snsClient) {

        try {
            ListTopicsRequest request = ListTopicsRequest.builder()
                   .build();

            ListTopicsResponse result = snsClient.listTopics(request);
            System.out.println("Status was " + result.sdkHttpResponse().statusCode() + "\n\nTopics\n\n" + result.topics());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListTopics](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/ListTopics) in *AWS SDK for Java 2\.x API Reference*\. 

### Publish an SMS text message<a name="sns_PublishTextSMS_java_topic"></a>

The following code example shows how to publish SMS messages using Amazon SNS\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void pubTextSMS(SnsClient snsClient, String message, String phoneNumber) {
        try {
            PublishRequest request = PublishRequest.builder()
                .message(message)
                .phoneNumber(phoneNumber)
                .build();

            PublishResponse result = snsClient.publish(request);
            System.out.println(result.messageId() + " Message sent. Status was " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [Publish](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/Publish) in *AWS SDK for Java 2\.x API Reference*\. 

### Publish to a topic<a name="sns_Publish_java_topic"></a>

The following code example shows how to publish messages to an Amazon SNS topic\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void pubTopic(SnsClient snsClient, String message, String topicArn) {

        try {
            PublishRequest request = PublishRequest.builder()
                .message(message)
                .topicArn(topicArn)
                .build();

            PublishResponse result = snsClient.publish(request);
            System.out.println(result.messageId() + " Message sent. Status is " + result.sdkHttpResponse().statusCode());

         } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
         }
    }
```
+  For API details, see [Publish](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/Publish) in *AWS SDK for Java 2\.x API Reference*\. 

### Set a filter policy<a name="sns_SetSubscriptionAttributes_java_topic"></a>

The following code example shows how to set an Amazon SNS filter policy\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void usePolicy(SnsClient snsClient, String subscriptionArn) {

        try {
            SNSMessageFilterPolicy fp = new SNSMessageFilterPolicy();
            // Add a filter policy attribute with a single value
            fp.addAttribute("store", "example_corp");
            fp.addAttribute("event", "order_placed");

            // Add a prefix attribute
            fp.addAttributePrefix("customer_interests", "bas");

            // Add an anything-but attribute
            fp.addAttributeAnythingBut("customer_interests", "baseball");

            // Add a filter policy attribute with a list of values
            ArrayList<String> attributeValues = new ArrayList<>();
            attributeValues.add("rugby");
            attributeValues.add("soccer");
            attributeValues.add("hockey");
            fp.addAttribute("customer_interests", attributeValues);

            // Add a numeric attribute
            fp.addAttribute("price_usd", "=", 0);

            // Add a numeric attribute with a range
            fp.addAttributeRange("price_usd", ">", 0, "<=", 100);

            // Apply the filter policy attributes to an Amazon SNS subscription
            fp.apply(snsClient, subscriptionArn);

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [SetSubscriptionAttributes](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/SetSubscriptionAttributes) in *AWS SDK for Java 2\.x API Reference*\. 

### Set the default settings for sending SMS messages<a name="sns_SetSmsAttributes_java_topic"></a>

The following code example shows how to set the default settings for sending SMS messages using Amazon SNS\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void setSNSAttributes( SnsClient snsClient, HashMap<String, String> attributes) {

        try {
            SetSmsAttributesRequest request = SetSmsAttributesRequest.builder()
                .attributes(attributes)
                .build();

            SetSmsAttributesResponse result = snsClient.setSMSAttributes(request);
            System.out.println("Set default Attributes to " + attributes + ". Status was " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [SetSmsAttributes](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/SetSmsAttributes) in *AWS SDK for Java 2\.x API Reference*\. 

### Set topic attributes<a name="sns_SetTopicAttributes_java_topic"></a>

The following code example shows how to set Amazon SNS topic attributes\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void setTopAttr(SnsClient snsClient, String attribute, String topicArn, String value) {

        try {
            SetTopicAttributesRequest request = SetTopicAttributesRequest.builder()
                .attributeName(attribute)
                .attributeValue(value)
                .topicArn(topicArn)
                .build();

            SetTopicAttributesResponse result = snsClient.setTopicAttributes(request);
            System.out.println("\n\nStatus was " + result.sdkHttpResponse().statusCode() + "\n\nTopic " + request.topicArn()
                + " updated " + request.attributeName() + " to " + request.attributeValue());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [SetTopicAttributes](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/SetTopicAttributes) in *AWS SDK for Java 2\.x API Reference*\. 

### Subscribe a Lambda function to a topic<a name="sns_Subscribe_Lambda_java_topic"></a>

The following code example shows how to subscribe a Lambda function so it receives notifications from an Amazon SNS topic\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static String subLambda(SnsClient snsClient, String topicArn, String lambdaArn) {

        try {
            SubscribeRequest request = SubscribeRequest.builder()
                .protocol("lambda")
                .endpoint(lambdaArn)
                .returnSubscriptionArn(true)
                .topicArn(topicArn)
                .build();

            SubscribeResponse result = snsClient.subscribe(request);
            return result.subscriptionArn();

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [Subscribe](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/Subscribe) in *AWS SDK for Java 2\.x API Reference*\. 

### Subscribe an HTTP endpoint to a topic<a name="sns_Subscribe_HTTP_java_topic"></a>

The following code example shows how to subscribe an HTTP or HTTPS endpoint so it receives notifications from an Amazon SNS topic\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void subHTTPS(SnsClient snsClient, String topicArn, String url ) {

        try {
            SubscribeRequest request = SubscribeRequest.builder()
                .protocol("http")
                .endpoint(url)
                .returnSubscriptionArn(true)
                .topicArn(topicArn)
                .build();

            SubscribeResponse result = snsClient.subscribe(request);
            System.out.println("Subscription ARN is " + result.subscriptionArn() + "\n\n Status is " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
         }
    }
```
+  For API details, see [Subscribe](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/Subscribe) in *AWS SDK for Java 2\.x API Reference*\. 

### Subscribe an email address to a topic<a name="sns_Subscribe_java_topic"></a>

The following code example shows how to subscribe an email address to an Amazon SNS topic\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sns#readme)\. 
  

```
    public static void subEmail(SnsClient snsClient, String topicArn, String email) {

        try {
            SubscribeRequest request = SubscribeRequest.builder()
                .protocol("email")
                .endpoint(email)
                .returnSubscriptionArn(true)
                .topicArn(topicArn)
                .build();

            SubscribeResponse result = snsClient.subscribe(request);
            System.out.println("Subscription ARN: " + result.subscriptionArn() + "\n\n Status is " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [Subscribe](https://docs.aws.amazon.com/goto/SdkForJavaV2/sns-2010-03-31/Subscribe) in *AWS SDK for Java 2\.x API Reference*\. 