--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Working with Amazon Simple Notification Service<a name="examples-simple-notification-service"></a>

With Amazon Simple Notification Service, you can easily push real\-time notification messages from your applications to subscribers over multiple communication channels\. This topic describes how to perform some of the basic functions of Amazon SNS\.

## Create a topic<a name="sns-create-topic"></a>

A **topic** is a logical grouping of communication channels that defines which systems to send a message to, for example, fanning out a message to AWS Lambda and an HTTP webhook\. You send messages to Amazon SNS, then they’re distributed to the channels defined in the topic\. This makes the messages available to subscribers\.

To create a topic, first build a [CreateTopicRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sns/model/CreateTopicRequest.html) object, with the name of the topic set using the `name()` method in the builder\. Then, send the request object to Amazon SNS by using the `createTopic()` method of the [SnsClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sns/SnsClient.html)\. You can capture the result of this request as a [CreateTopicResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sns/model/CreateTopicResponse.html) object, as demonstrated in the following code snippet\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
```

 **Code** 

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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sns/src/main/java/com/example/sns/CreateTopic.java) on GitHub\.

## List your Amazon SNS topics<a name="sns-crelistate-topics"></a>

To retrieve a list of your existing Amazon SNS topics, build a [ListTopicsRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sns/model/ListTopicsRequest.html) object\. Then, send the request object to Amazon SNS by using the `listTopics()` method of the `SnsClient`\. You can capture the result of this request as a [ListTopicsResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sns/model/ListTopicsResponse.html) object\.

The following code snippet prints out the HTTP status code of the request and a list of Amazon Resource Names \(ARNs\) for your Amazon SNS topics\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.ListTopicsRequest;
import software.amazon.awssdk.services.sns.model.ListTopicsResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
```

 **Code** 

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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sns/src/main/java/com/example/sns/ListTopics.java) on GitHub\.

## Subscribe an endpoint to a topic<a name="sns-subscribe-endpoint-topic"></a>

After you create a topic, you can configure which communication channels will be endpoints for that topic\. Messages are distributed to these endpoints after Amazon SNS receives them\.

To configure a communication channel as an endpoint for a topic, subscribe that endpoint to the topic\. To start, build a [SubscribeRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sns/model/SubscribeRequest.html) object\. Specify the communication channel \(for example, `lambda` or `email`\) as the `protocol()`\. Set the `endpoint()` to the relevant output location \(for example, the ARN of a Lambda function or an email address\), and then set the ARN of the topic to which you want to subscribe as the `topicArn()`\. Send the request object to Amazon SNS by using the `subscribe()` method of the `SnsClient`\. You can capture the result of this request as a [SubscribeResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sns/model/SubscribeResponse.html) object\.

The following code snippet shows how to subscribe an email address to a topic\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SnsException;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;
```

 **Code** 

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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sns/src/main/java/com/example/sns/SubscribeEmail.java) on GitHub\.

## Publish a message to a topic<a name="sns-publish-message-topic"></a>

After you have a topic and one or more endpoints configured for it, you can publish a message to it\. To start, build a [PublishRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sns/model/PublishRequest.html) object\. Specify the `message()` to send, and the ARN of the topic \(`topicArn()`\) to send it to\. Then, send the request object to Amazon SNS by using the `publish()` method of the `SnsClient`\. You can capture the result of this request as a [PublishResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sns/model/PublishResponse.html) object\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
```

 **Code** 

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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sns/src/main/java/com/example/sns/PublishTopic.java) on GitHub\.

## Unsubscribe an endpoint from a topic<a name="sns-unsubscribe-endpoint-topic"></a>

You can remove the communication channels configured as endpoints for a topic\. After doing that, the topic itself continues to exist and distribute messages to any other endpoints configured for that topic\.

To remove a communication channel as an endpoint for a topic, unsubscribe that endpoint from the topic\. To start, build an [UnsubscribeRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sns/model/UnsubscribeRequest.html) object and set the ARN of the topic you want to unsubscribe from as the `subscriptionArn()`\. Then send the request object to SNS by using the `unsubscribe()` method of the `SnsClient`\. You can capture the result of this request as an [UnsubscribeResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sns/model/UnsubscribeResponse.html) object\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SnsException;
import software.amazon.awssdk.services.sns.model.UnsubscribeRequest;
import software.amazon.awssdk.services.sns.model.UnsubscribeResponse;
```

 **Code** 

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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sns/src/main/java/com/example/sns/Unsubscribe.java) on GitHub\.

## Delete a topic<a name="sns-delete-topic"></a>

To delete an Amazon SNS topic, first build a [DeleteTopicRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sns/model/DeleteTopicRequest.html) object with the ARN of the topic set as the `topicArn()` method in the builder\. Then send the request object to Amazon SNS by using the `deleteTopic()` method of the `SnsClient`\. You can capture the result of this request as a [DeleteTopicResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sns/model/DeleteTopicResponse.html) object, as demonstrated in the following code snippet\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.DeleteTopicRequest;
import software.amazon.awssdk.services.sns.model.DeleteTopicResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
```

 **Code** 

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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sns/src/main/java/com/example/sns/DeleteTopic.java) on GitHub\.

For more information, see the [Amazon Simple Notification Service Developer Guide](http://docs.aws.amazon.com/sns/latest/dg/)\.