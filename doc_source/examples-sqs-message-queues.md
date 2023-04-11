# Working with Amazon Simple Queue Service message queues<a name="examples-sqs-message-queues"></a>

A *message queue* is the logical container used for sending messages reliably in Amazon Simple Queue Service\. There are two types of queues: *standard* and *first\-in, first\-out* \(FIFO\)\. To learn more about queues and the differences between these types, see the [Amazon Simple Queue Service Developer Guide](http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/)\.

This topic describes how to create, list, delete, and get the URL of an Amazon Simple Queue Service queue by using the AWS SDK for Java\.

## Create a queue<a name="sqs-create-queue"></a>

Use the SqsClient’s `createQueue` method, and provide a [CreateQueueRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sqs/model/CreateQueueRequest.html) object that describes the queue parameters\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import java.util.List;
```

 **Code** 

```
            CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                .queueName(queueName)
                .build();

            sqsClient.createQueue(createQueueRequest);
```

See the [complete sample](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sqs/src/main/java/com/example/sqs/SQSExample.java) on GitHub\.

## List queues<a name="sqs-list-queues"></a>

To list the Amazon Simple Queue Service queues for your account, call the SqsClient’s `listQueues` method with a [ListQueuesRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sqs/model/ListQueuesRequest.html) object\.

Using the `listQueues` overload without any parameters returns *all queues*, up to 1,000 queues \. You can supply a queue name prefix to the `ListQueuesRequest` object to limit the results to queues that match that prefix\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import java.util.List;
```

 **Code** 

```
        String prefix = "que";

        try {
            ListQueuesRequest listQueuesRequest = ListQueuesRequest.builder().queueNamePrefix(prefix).build();
            ListQueuesResponse listQueuesResponse = sqsClient.listQueues(listQueuesRequest);

            for (String url : listQueuesResponse.queueUrls()) {
                System.out.println(url);
            }

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
```

See the [complete sample](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sqs/src/main/java/com/example/sqs/SQSExample.java) on GitHub\.

## Get the URL for a queue<a name="sqs-get-queue-url"></a>

Call the SqsClient’s `getQueueUrl` method\. with a [GetQueueUrlRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sqs/model/GetQueueUrlRequest.html) object\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import java.util.List;
```

 **Code** 

```
            GetQueueUrlResponse getQueueUrlResponse =
                sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());
            String queueUrl = getQueueUrlResponse.queueUrl();
            return queueUrl;

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
```

See the [complete sample](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sqs/src/main/java/com/example/sqs/SQSExample.java) on GitHub\.

## Delete a queue<a name="sqs-delete-queue"></a>

Provide the queue’s [URL](#sqs-get-queue-url) to the [DeleteMessageRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/sqs/model/DeleteMessageRequest.html) object\. Then call the SqsClient’s `deleteQueue` method\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import java.util.List;
```

 **Code** 

```
    public static void deleteSQSQueue(SqsClient sqsClient, String queueName) {

        try {

            GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build();

            String queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();

            DeleteQueueRequest deleteQueueRequest = DeleteQueueRequest.builder()
                    .queueUrl(queueUrl)
                    .build();

            sqsClient.deleteQueue(deleteQueueRequest);

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

See the [complete sample](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/sqs/src/main/java/com/example/sqs/SQSExample.java) on GitHub\.

## More information<a name="more-information"></a>
+  [How Amazon Simple Queue Service Queues Work](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-how-it-works.html) in the Amazon Simple Queue Service Developer Guide
+  [CreateQueue](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_CreateQueue.html) in the Amazon Simple Queue Service API Reference
+  [GetQueueUrl](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_GetQueueUrl.html) in the Amazon Simple Queue Service API Reference
+  [ListQueues](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ListQueues.html) in the Amazon Simple Queue Service API Reference
+  [DeleteQueues](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_DeleteQueues.html) in the Amazon Simple Queue Service API Reference