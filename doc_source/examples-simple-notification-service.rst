.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

##############
|SNS| examples
##############

.. meta::
   :description: How to use the AWS SDK for Java to work with Amazon Simple Notification Service (SNS).
   :keywords: AWS for Java SDK code examples, SNS, topic, create, list, endpoint, subscribe,
              publish, message, unsubscribe

With |SNS|, you can easily push real-time notification messages from your applications to subscribers
over multiple communication channels. This article describes how to perform some of the basic
functions of SNS.


.. _sns-create-topic:

Create a new topic
==================

A **topic** is a logical grouping of communication channels that defines to which systems a message
is sent. For example, fanning out a message to AWS Lambda and an HTTP webhook. You send messages to
SNS and then they are distributed to the channels defined in the topic, making the messages
available to subscribers.

To create a topic, first build a CreateTopicRequest object with the name of the topic set using the
*.name()* method in the builder. Then, send the request object to SNS using SnsClientâ€™s
*.createTopic()* method. You can capture the result of this request as a CreateTopicResponse object,
as demonstrated in the code snippet below.

**Imports**

.. literalinclude:: cognito.java2.create_user_pool.import.txt
   :language: java

**Code**

.. literalinclude:: cognito.java2.create_user_pool.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-cognito:`complete example <CreateUserPool.java>` on GitHub.

Complete Example:
https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sns/src/main/java/com/example/sns/CreateTopic.java

List your SNS topics
====================

To retrieve a list of your existing SNS topics, build a ListTopicsRequest object. Then, send the
request object to SNS using SnsClient’s *.listTopics()* method. You must capture the result of this
request as a ListTopicsResponse object.

The code snippet below will print out the HTTP status code of the request along with a list of ARNs
for your SNS topics.

::

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

Complete example:
https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sns/src/main/java/com/example/sns/ListTopics.java

Subscribe an endpoint to a topic
================================

Once you have a topic created, you can configure which communication channels will be endpoints for
that topic. These endpoints are where the messages will be distributed after being received by SNS.

To configure a communication channel as an endpoint for a topic, subscribe that endpoint to the
topic. To start, build a SubscribeRequest object. Specify the communication channel (e.g. “lambda”
or “email”) as the *.protocol()*, set the *.endpoint()* to the relevant output location (e.g. the
ARN of a Lamdba function or an email address), and set the ARN of the topic to which you wish to
subscribe as the *.topicArn()*. Then, send the request object to SNS using SnsClient’s
*.subscribe()* method. You can capture the result of this request as a SubscribeResponse object.

For example, the following code snippet shows how to subscribe an email address to a topic.

::

       public static void subEmail(SnsClient snsClient, String topicArn, String email) {


           try {
               SubscribeRequest request = SubscribeRequest.builder()
                   .protocol("email")
                   .endpoint(email)
                   .returnSubscriptionArn(true)
                   .topicArn(topicArn)
                   .build();


               SubscribeResponse result = snsClient.subscribe(request);
               System.out.println("Subscription ARN: " + result.subscriptionArn() + "\n\n Status was " + result.sdkHttpResponse().statusCode());


           } catch (SnsException e) {
               System.err.println(e.awsErrorDetails().errorMessage());
               System.exit(1);
           }

Complete example:
https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sns/src/main/java/com/example/sns/SubscribeEmail.java

Publish a message to a topic
============================

Once you have a topic and one or more endpoints configured for it, you can publish a message to the
topic. To start, build a PublishRequest object. Specify the message to send as the *.message()* and
set the ARN of the topic to send the message to as the *.topicArn()*. Then, send the request object
to SNS using SnsClient’s *.publish()* method. You can capture the result of this request as a
PublishResponse object.

::

       public static void pubTopic(SnsClient snsClient, String message, String topicArn) {


           try {
               PublishRequest request = PublishRequest.builder()
                   .message(message)
                   .topicArn(topicArn)
                   .build();


               PublishResponse result = snsClient.publish(request);
               System.out.println(result.messageId() + " Message sent. Status was " + result.sdkHttpResponse().statusCode());


            } catch (SnsException e) {
               System.err.println(e.awsErrorDetails().errorMessage());
                 System.exit(1);
            }

Complete example:
https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sns/src/main/java/com/example/sns/PublishTopic.java

Unsubscribe an endpoint from a topic
====================================

You can remove the communication channels configured as endpoints for a topic. After doing so, the
topic itself will continue to exist and distribute messages to any other endpoints configured for
that topic.

To remove a communication channel as an endpoint for a topic, unsubscribe that endpoint from the
topic. To start, build a UnsubscribeRequest object and set the ARN of the topic you wish to
unsubscribe from as the *.subscriptionArn()*. Then, send the request object to SNS using SnsClient’s
*.unsubscribe()* method. You can capture the result of this request as a UnsubscribeResponse object.

::

       public static void unSub(SnsClient snsClient, String subscriptionToken) {


           try {
               UnsubscribeRequest request = UnsubscribeRequest.builder()
                   .subscriptionArn(subscriptionToken)
                   .build();


               UnsubscribeResponse result = snsClient.unsubscribe(request);


               System.out.println("\n\nStatus was " + result.sdkHttpResponse().statusCode()
                   + "\n\nSubscription was removed for " + request.subscriptionArn());


           } catch (SnsException e) {
               System.err.println(e.awsErrorDetails().errorMessage());
               System.exit(1);
           }

Complete example:
https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sns/src/main/java/com/example/sns/Unsubscribe.java

Delete a topic
==============

To delete an SNS topic, first build a DeleteTopicRequest object with the ARN of the topic set as the
*.topicArn()* method in the builder. Then, send the request object to SNS using SnsClient’s
*.deleteTopic()* method. You can capture the result of this request as a DeleteTopicResponse object,
as demonstrated in the code snippet below.

::

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

Complete example:
https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sns/src/main/java/com/example/sns/DeleteTopic.java


