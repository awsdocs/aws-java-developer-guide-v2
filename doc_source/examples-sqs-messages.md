--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Sending, receiving, and deleting Amazon Simple Queue Service messages<a name="examples-sqs-messages"></a>

A message is a piece of data that can be sent and received by distributed components\. Messages are always delivered using an [SQS Queue](examples-sqs-message-queues.md)\.

## Send a message<a name="sqs-message-send"></a>

Add a single message to an Amazon Simple Queue Service queue by calling the SqsClient client `sendMessage` method\. Provide a [SendMessageRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sqs/model/SendMessageRequest.html) object that contains the queue’s [URL](examples-sqs-message-queues.md#sqs-get-queue-url), message body, and optional delay value \(in seconds\)\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import java.util.List;
```

 **Code** 

```
            sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody("Hello world!")
                .delaySeconds(10)
                .build());
```

## Send multiple messages in a request<a name="sqs-messages-send-multiple"></a>

Send more than one message in a single request by using the SqsClient `sendMessageBatch` method\. This method takes a [SendMessageBatchRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sqs/model/SendMessageBatchRequest.html) that contains the queue URL and a list of messages to send\. \(Each message is a [SendMessageBatchRequestEntry](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sqs/model/SendMessageBatchRequestEntry.html)\.\) You can also delay sending a specific message by setting a delay value on the message\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import java.util.List;
```

 **Code** 

```
            SendMessageBatchRequest sendMessageBatchRequest = SendMessageBatchRequest.builder()
                .queueUrl(queueUrl)
                .entries(SendMessageBatchRequestEntry.builder().id("id1").messageBody("Hello from msg 1").build(),
                        SendMessageBatchRequestEntry.builder().id("id2").messageBody("msg 2").delaySeconds(10).build())
                .build();
            sqsClient.sendMessageBatch(sendMessageBatchRequest);
```

See the [complete sample](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sqs/src/main/java/com/example/sqs/SQSExample.java) on GitHub\.

## Retrieve Messages<a name="sqs-messages-receive"></a>

Retrieve any messages that are currently in the queue by calling the SqsClient `receiveMessage` method\. This method takes a [ReceiveMessageRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sqs/model/ReceiveMessageRequest.html) that contains the queue URL\. You can also specify the maximum number of messages to return\. Messages are returned as a list of [Message](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sqs/model/Message.html) objects\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import java.util.List;
```

 **Code** 

```
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .build();
            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
            return messages;
        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
```

## Delete a message after receipt<a name="sqs-messages-delete"></a>

After receiving a message and processing its contents, delete the message from the queue by sending the message’s receipt handle and queue URL to the SqsClient `deleteMessage` method\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import java.util.List;
```

 **Code** 

```
        try {
            for (Message message : messages) {
                DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(message.receiptHandle())
                    .build();
                sqsClient.deleteMessage(deleteMessageRequest);
            }
```

See the [complete sample](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sqs/src/main/java/com/example/sqs/SQSExample.java) on GitHub\.

## More Info<a name="more-info"></a>
+  [How Amazon Simple Queue Service Queues Work](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-how-it-works.html) in the Amazon Simple Queue Service Developer Guide
+  [SendMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_SendMessage.html) in the Amazon Simple Queue Service API Reference
+  [SendMessageBatch](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_SendMessageBatch.html) in the Amazon Simple Queue Service API Reference
+  [ReceiveMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html) in the Amazon Simple Queue Service API Reference
+  [DeleteMessage](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_DeleteMessage.html) in the Amazon Simple Queue Service API Reference