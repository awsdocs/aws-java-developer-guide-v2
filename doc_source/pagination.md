# Retrieving paginated results using the AWS SDK for Java 2\.x<a name="pagination"></a>

Many AWS operations return paginated results when the response object is too large to return in a single response\. In the AWS SDK for Java 1\.0, the response contained a token you had to use to retrieve the next page of results\. New in the AWS SDK for Java 2\.x are autopagination methods that make multiple service calls to get the next page of results for you automatically\. You only have to write code that processes the results\. Additionally both types of methods have synchronous and asynchronous versions\. See examples\-asynchronous for more detail about asynchronous clients\.

The following examples use Amazon S3 and Amazon DynamoDB operations to demonstrate the various methods of retrieving your data from paginated responses\.

**Note**  
These code snippets assume that you understand the material in basics, and have configured default AWS credentials using the information in setup\-credentials\.

## Synchronous pagination<a name="synchronous-pagination"></a>

These examples use the synchronous pagination methods for listing objects in an Amazon S3 bucket\.

### Iterate over pages<a name="iterate-pages"></a>

Build a [ListObjectsV2Request](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/model/ListObjectsV2Request.html) and provide a bucket name\. Optionally you can provide the maximum number of keys to retrieve at one time\. Pass it to the S3Client’s `listObjectsV2Paginator` method\. This method returns a [ListObjectsV2Iterable](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/paginators/ListObjectsV2Iterable.html) object, which is an `Iterable` of the [ListObjectsV2Response](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/model/ListObjectsV2Response.html) class\.

The first example demonstrates using the paginator object to iterate through all the response pages with the `stream` method\. You can directly stream over the response pages, convert the response stream to a stream of [S3Object](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/s3/model/S3Object.html) content, and then process the content of the Amazon S3 object\.

 **Imports** 

```
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
```

 **Code** 

```
        ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .maxKeys(1)
                .build();

        ListObjectsV2Iterable listRes = s3.listObjectsV2Paginator(listReq);
        // Process response pages
        listRes.stream()
                .flatMap(r -> r.contents().stream())
                .forEach(content -> System.out.println(" Key: " + content.key() + " size = " + content.size()));
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java) on GitHub\.

### Iterate over objects<a name="iterate-objects"></a>

The following examples show ways to iterate over the objects returned in the response instead of the pages of the response\.

#### Use a stream<a name="use-a-stream"></a>

Use the `stream` method on the response content to iterate over the paginated item collection\.

 **Code** 

```
        // Helper method to work with paginated collection of items directly
        listRes.contents().stream()
                .forEach(content -> System.out.println(" Key: " + content.key() + " size = " + content.size()));
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java) on GitHub\.

#### Use a for loop<a name="for-loop"></a>

Use a standard `for` loop to iterate through the contents of the response\.

 **Code** 

```
        for (S3Object content : listRes.contents()) {
            System.out.println(" Key: " + content.key() + " size = " + content.size());
        }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java) on GitHub\.

### Manual pagination<a name="manual-pagination"></a>

If your use case requires it, manual pagination is still available\. Use the next token in the response object for the subsequent requests\. Here’s an example using a `while` loop\.

 **Code** 

```
        ListObjectsV2Request listObjectsReqManual = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .maxKeys(1)
                .build();

        boolean done = false;
        while (!done) {
            ListObjectsV2Response listObjResponse = s3.listObjectsV2(listObjectsReqManual);
            for (S3Object content : listObjResponse.contents()) {
                System.out.println(content.key());
            }

            if (listObjResponse.nextContinuationToken() == null) {
                done = true;
            }

            listObjectsReqManual = listObjectsReqManual.toBuilder()
                    .continuationToken(listObjResponse.nextContinuationToken())
                    .build();
        }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java) on GitHub\.

## Asynchronous pagination<a name="asynchronous-pagination"></a>

These examples use the asynchronous pagination methods for listing tables in DynamoDB\. A manual pagination example is available in the basics\-async topic\.

### Iterate over pages of table names<a name="iterate-pages-async"></a>

First, create an asynchronous DynamoDB client\. Then, call the `listTablesPaginator` method to get a [ListTablesPublisher](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/dynamodb/paginators/ListTablesPublisher.html)\. This is an implementation of the reactive streams Publisher interface\. To learn more about the reactive streams model, see the [Reactive Streams Github repo](https://github.com/reactive-streams/reactive-streams-jvm/blob/v1.0.2/README.md)\.

Call the `subscribe` method on the [ListTablesPublisher](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/dynamodb/paginators/ListTablesPublisher.html) and pass a subscriber implementation\. In this example, the subscriber has an `onNext` method that requests one item at a time from the publisher\. This is the method that is called repeatedly until all pages are retrieved\. The `onSubscribe` method calls the `Subscription.request` method to initiate requests for data from the publisher\. This method must be called to start getting data from the publisher\. The `onError` method is triggered if an error occurs while retrieving data\. Finally, the `onComplete` method is called when all pages have been requested\.

#### Use a subscriber<a name="use-a-subscriber"></a>

 **Imports** 

```
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.ListTablesRequest;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;
import software.amazon.awssdk.services.dynamodb.paginators.ListTablesPublisher;
import io.reactivex.Flowable;
import reactor.core.publisher.Flux;
```

 **Code** 

First create an async client

```
        // Creates a default client with credentials and regions loaded from the environment
        final DynamoDbAsyncClient asyncClient = DynamoDbAsyncClient.create();

        ListTablesRequest listTablesRequest = ListTablesRequest.builder().limit(3).build();
```

Then use Subscriber to get results\.

```
        // Or subscribe method should be called to create a new Subscription.
        // A Subscription represents a one-to-one life-cycle of a Subscriber subscribing to a Publisher.
        publisher.subscribe(new Subscriber<ListTablesResponse>() {
            // Maintain a reference to the subscription object, which is required to request data from the publisher
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
                // Request method should be called to demand data. Here we request a single page
                subscription.request(1);
            }

            @Override
            public void onNext(ListTablesResponse response) {
                response.tableNames().forEach(System.out::println);
                // Once you process the current page, call the request method to signal that you are ready for next page
                subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                // Called when an error has occurred while processing the requests
            }

            @Override
            public void onComplete() {
                // This indicates all the results are delivered and there are no more pages left
            }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodbasync/src/main/java/com/example/dynamodbasync/AsyncPagination.java) on GitHub\.

#### Use a for loop<a name="id1pagination"></a>

Use a `for` loop to iterate through the pages for simple use cases when creating a new subscriber might be too much overhead\. The response publisher object has a `forEach` helper method for this purpose\.

 **Code** 

```
        ListTablesPublisher publisher = asyncClient.listTablesPaginator(listTablesRequest);

        // Use a for-loop for simple use cases
        CompletableFuture<Void> future = publisher.subscribe(response -> response.tableNames()
                                                                               .forEach(System.out::println));
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodbasync/src/main/java/com/example/dynamodbasync/AsyncPagination.java) on GitHub\.

### Iterate over table names<a name="iterate-objects-async"></a>

The following examples show ways to iterate over the objects returned in the response instead of the pages of the response\. Similar to the synchronous result, the asynchronous result class has a method to interact with the underlying item collection\. The return type of the convenience method is a publisher that can be used to request items across all pages\.

#### Use a subscriber<a name="id2"></a>

 **Code** 

First create an async client

```
        System.out.println("running AutoPagination - iterating on item collection...\n");

        // Creates a default client with credentials and regions loaded from the environment
        final DynamoDbAsyncClient asyncClient = DynamoDbAsyncClient.create();

        ListTablesRequest listTablesRequest = ListTablesRequest.builder().limit(3).build();
```

Then use Subscriber to get results\.

```
        // Use subscriber
        publisher.subscribe(new Subscriber<String>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
                subscription.request(1);
            }

            @Override
            public void onNext(String tableName) {
                System.out.println(tableName);
                subscription.request(1);
            }

            @Override
            public void onError(Throwable t) { }

            @Override
            public void onComplete() { }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodbasync/src/main/java/com/example/dynamodbasync/AsyncPagination.java) on GitHub\.

#### Use a for loop<a name="for-loop-async"></a>

Use the `forEach` convenience method to iterate through the results\.

 **Code** 

```
        // Use forEach
        CompletableFuture<Void> future = publisher.subscribe(System.out::println);
        future.get();
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodbasync/src/main/java/com/example/dynamodbasync/AsyncPagination.java) on GitHub\.

### Use third\-party library<a name="use-third-party-library"></a>

You can use other third party libraries instead of implementing a custom subscriber\. This example demonstrates using the RxJava implementation but any library that implements the reactive stream interfaces can be used\. See the [RxJava wiki page on Github](https://github.com/ReactiveX/RxJava/wiki) for more information on that library\.

To use the library, add it as a dependency\. If using Maven, the example shows the POM snippet to use\.

 **POM Entry** 

```
<dependency>
      <groupId>io.reactivex.rxjava2</groupId>
      <artifactId>rxjava</artifactId>
      <version>2.2.21</version>
</dependency>
```

 **Imports** 

```
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.ListTablesRequest;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;
import software.amazon.awssdk.services.dynamodb.paginators.ListTablesPublisher;
import io.reactivex.Flowable;
import reactor.core.publisher.Flux;
```

 **Code** 

```
        System.out.println("running AutoPagination - using third party subscriber...\n");

        DynamoDbAsyncClient asyncClient = DynamoDbAsyncClient.create();
        ListTablesPublisher publisher = asyncClient.listTablesPaginator(ListTablesRequest.builder()
                                                                                         .build());

        // The Flowable class has many helper methods that work with any reactive streams compatible publisher implementation
        List<String> tables = Flowable.fromPublisher(publisher)
                                      .flatMapIterable(ListTablesResponse::tableNames)
                                      .toList()
                                      .blockingGet();
        System.out.println(tables);
```