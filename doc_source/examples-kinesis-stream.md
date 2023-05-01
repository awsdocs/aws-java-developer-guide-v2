# Subscribing to Amazon Kinesis Data Streams<a name="examples-kinesis-stream"></a>

The following examples show you how to retrieve and process data from Amazon Kinesis Data Streams using the `subscribeToShard` method\. Kinesis Data Streams now employs the enhanced fanout feature and a low\-latency HTTP/2 data retrieval API, making it easier for developers to run multiple low\-latency, high\-performance applications on the same Kinesis Data Stream\.

## Set up<a name="set-up"></a>

First, create an asynchronous Kinesis client and a [SubscribeToShardRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/kinesis/model/SubscribeToShardRequest.html) object\. These objects are used in each of the following examples to subscribe to Kinesis events\.

 **Imports** 

```
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.ShardIteratorType;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardEvent;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardEventStream;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardRequest;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardResponse;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardResponseHandler;
```

 **Code** 

```
        Region region = Region.US_EAST_1;
        KinesisAsyncClient client = KinesisAsyncClient.builder()
        .region(region)
        .build();

        SubscribeToShardRequest request = SubscribeToShardRequest.builder()
                .consumerARN(CONSUMER_ARN)
                .shardId("arn:aws:kinesis:us-east-1:111122223333:stream/StockTradeStream")
                .startingPosition(s -> s.type(ShardIteratorType.LATEST)).build();
```

## Use the builder interface<a name="use-the-builder-interface"></a>

You can use the `builder` method to simplify the creation of the [SubscribeToShardResponseHandler](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/kinesis/model/SubscribeToShardResponseHandler.html)\.

Using the builder, you can set each lifecycle callback with a method call instead of implementing the full interface\.

 **Code** 

```
    private static CompletableFuture<Void> responseHandlerBuilder(KinesisAsyncClient client, SubscribeToShardRequest request) {
        SubscribeToShardResponseHandler responseHandler = SubscribeToShardResponseHandler
                .builder()
                .onError(t -> System.err.println("Error during stream - " + t.getMessage()))
                .onComplete(() -> System.out.println("All records stream successfully"))
                // Must supply some type of subscriber
                .subscriber(e -> System.out.println("Received event - " + e))
                .build();
        return client.subscribeToShard(request, responseHandler);
    }
```

For more control of the publisher, you can use the `publisherTransformer` method to customize the publisher\.

 **Code** 

```
    private static CompletableFuture<Void> responseHandlerBuilderPublisherTransformer(KinesisAsyncClient client, SubscribeToShardRequest request) {
        SubscribeToShardResponseHandler responseHandler = SubscribeToShardResponseHandler
                .builder()
                .onError(t -> System.err.println("Error during stream - " + t.getMessage()))
                .publisherTransformer(p -> p.filter(e -> e instanceof SubscribeToShardEvent).limit(100))
                .subscriber(e -> System.out.println("Received event - " + e))
                .build();
        return client.subscribeToShard(request, responseHandler);
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/kinesis/src/main/java/com/example/kinesis/KinesisStreamEx.java) on GitHub\.

## Use a custom response handler<a name="use-a-custom-response-handler"></a>

For full control of the subscriber and publisher, implement the [SubscribeToShardResponseHandler](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/kinesis/model/SubscribeToShardResponseHandler.html) interface\.

In this example, you implement the `onEventStream` method, which allows you full access to the publisher\. This demonstrates how to transform the publisher to event records for printing by the subscriber\.

 **Code** 

```
    private static CompletableFuture<Void> responseHandlerBuilderClassic(KinesisAsyncClient client, SubscribeToShardRequest request) {
        SubscribeToShardResponseHandler responseHandler = new SubscribeToShardResponseHandler() {

            @Override
            public void responseReceived(SubscribeToShardResponse response) {
                System.out.println("Receieved initial response");
            }

            @Override
            public void onEventStream(SdkPublisher<SubscribeToShardEventStream> publisher) {
                publisher
                        // Filter to only SubscribeToShardEvents
                        .filter(SubscribeToShardEvent.class)
                        // Flat map into a publisher of just records
                        .flatMapIterable(SubscribeToShardEvent::records)
                        // Limit to 1000 total records
                        .limit(1000)
                        // Batch records into lists of 25
                        .buffer(25)
                        // Print out each record batch
                        .subscribe(batch -> System.out.println("Record Batch - " + batch));
            }

            @Override
            public void complete() {
                System.out.println("All records stream successfully");
            }

            @Override
            public void exceptionOccurred(Throwable throwable) {
                System.err.println("Error during stream - " + throwable.getMessage());
            }
        };
        return client.subscribeToShard(request, responseHandler);
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/kinesis/src/main/java/com/example/kinesis/KinesisStreamEx.java) on GitHub\.

## Use the visitor interface<a name="use-the-visitor-interface"></a>

You can use a [Visitor](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/kinesis/model/SubscribeToShardResponseHandler.Visitor.html) object to subscribe to specific events you’re interested in watching\.

 **Code** 

```
    private static CompletableFuture<Void> responseHandlerBuilderVisitorBuilder(KinesisAsyncClient client, SubscribeToShardRequest request) {
        SubscribeToShardResponseHandler.Visitor visitor = SubscribeToShardResponseHandler.Visitor
                .builder()
                .onSubscribeToShardEvent(e -> System.out.println("Received subscribe to shard event " + e))
                .build();
        SubscribeToShardResponseHandler responseHandler = SubscribeToShardResponseHandler
                .builder()
                .onError(t -> System.err.println("Error during stream - " + t.getMessage()))
                .subscriber(visitor)
                .build();
        return client.subscribeToShard(request, responseHandler);
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/kinesis/src/main/java/com/example/kinesis/KinesisStreamEx.java) on GitHub\.

## Use a custom subscriber<a name="use-a-custom-subscriber"></a>

You can also implement your own custom subscriber to subscribe to the stream\.

This code snippet shows an example subscriber\.

 **Code** 

```
    private static class MySubscriber implements Subscriber<SubscribeToShardEventStream> {

        private Subscription subscription;
        private AtomicInteger eventCount = new AtomicInteger(0);

        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscription = subscription;
            this.subscription.request(1);
        }

        @Override
        public void onNext(SubscribeToShardEventStream shardSubscriptionEventStream) {
            System.out.println("Received event " + shardSubscriptionEventStream);
            if (eventCount.incrementAndGet() >= 100) {
                // You can cancel the subscription at any time if you wish to stop receiving events.
                subscription.cancel();
            }
            subscription.request(1);
        }

        @Override
        public void onError(Throwable throwable) {
            System.err.println("Error occurred while stream - " + throwable.getMessage());
        }

        @Override
        public void onComplete() {
            System.out.println("Finished streaming all events");
        }
    }
```

You can pass that custom subscriber to the `subscribe` method, similarly to preview examples\. The following code snippet shows this example\.

 **Code** 

```
    private static CompletableFuture<Void> responseHandlerBuilderSubscriber(KinesisAsyncClient client, SubscribeToShardRequest request) {
        SubscribeToShardResponseHandler responseHandler = SubscribeToShardResponseHandler
                .builder()
                .onError(t -> System.err.println("Error during stream - " + t.getMessage()))
                .subscriber(MySubscriber::new)
                .build();
        return client.subscribeToShard(request, responseHandler);
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/kinesis/src/main/java/com/example/kinesis/KinesisStreamEx.java) on GitHub\.

## Write data records into a Kinesis data stream<a name="write-data-records-into-a-kinesis-data-stream"></a>

You can use the [KinesisClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/kinesis/KinesisClient.html) object to write data records into a Kinesis data stream by using the `putRecords` method\. To successfully invoke this method, create a [PutRecordsRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/kinesis/model/PutRecordsRequest.html) object\. You pass the name of the data stream to the `streamName` method\. Also you must pass the data by using the `putRecords` method \(as shown in the following code example\)\.

 **Imports** 

```
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.services.kinesis.model.KinesisException;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamRequest;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamResponse;
```

In the following Java code example, notice that **StockTrade** object is used as the data to write to the Kinesis data stream\. Before running this example, ensure that you have created the data stream\.

 **Code** 

```
    public static void setStockData( KinesisClient kinesisClient, String streamName) {

        try {
            // Repeatedly send stock trades with a 100 milliseconds wait in between.
            StockTradeGenerator stockTradeGenerator = new StockTradeGenerator();

            // Put in 50 Records for this example.
            int index = 50;
            for (int x=0; x<index; x++){
                StockTrade trade = stockTradeGenerator.getRandomTrade();
                sendStockTrade(trade, kinesisClient, streamName);
                Thread.sleep(100);
            }

        } catch (KinesisException | InterruptedException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Done");
    }

    private static void sendStockTrade(StockTrade trade, KinesisClient kinesisClient,
                                       String streamName) {
        byte[] bytes = trade.toJsonAsBytes();

        // The bytes could be null if there is an issue with the JSON serialization by the Jackson JSON library.
        if (bytes == null) {
            System.out.println("Could not get JSON bytes for stock trade");
            return;
        }

        System.out.println("Putting trade: " + trade);
        PutRecordRequest request = PutRecordRequest.builder()
            .partitionKey(trade.getTickerSymbol()) // We use the ticker symbol as the partition key, explained in the Supplemental Information section below.
            .streamName(streamName)
            .data(SdkBytes.fromByteArray(bytes))
            .build();

        try {
            kinesisClient.putRecord(request);
        } catch (KinesisException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void validateStream(KinesisClient kinesisClient, String streamName) {
        try {
            DescribeStreamRequest describeStreamRequest = DescribeStreamRequest.builder()
                .streamName(streamName)
                .build();

            DescribeStreamResponse describeStreamResponse = kinesisClient.describeStream(describeStreamRequest);

            if(!describeStreamResponse.streamDescription().streamStatus().toString().equals("ACTIVE")) {
                System.err.println("Stream " + streamName + " is not active. Please wait a few moments and try again.");
                System.exit(1);
            }

        }catch (KinesisException e) {
            System.err.println("Error found while describing the stream " + streamName);
            System.err.println(e);
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/kinesis/src/main/java/com/example/kinesis/StockTradesWriter.java) on GitHub\.

## Use a third\-party library<a name="use-a-third-party-library"></a>

You can use other third\-party libraries instead of implementing a custom subscriber\. This example demonstrates using the RxJava implementation, but you can use any library that implements the Reactive Streams interfaces\. See the [RxJava wiki page on Github](https://github.com/ReactiveX/RxJava/wiki) for more information on that library\.

To use the library, add it as a dependency\. If you’re using Maven, the example shows the POM snippet to use\.

 **POM Entry** 

```
<dependency>
 <groupId>io.reactivex.rxjava2</groupId>
 <artifactId>rxjava</artifactId>
 <version>2.1.14</version>
</dependency>
```

 **Imports** 

```
import java.net.URI;
import java.util.concurrent.CompletableFuture;

import io.reactivex.Flowable;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.http.Protocol;
import software.amazon.awssdk.http.SdkHttpConfigurationOption;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.ShardIteratorType;
import software.amazon.awssdk.services.kinesis.model.StartingPosition;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardEvent;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardRequest;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardResponseHandler;
import software.amazon.awssdk.utils.AttributeMap;
```

This example uses RxJava in the `onEventStream` lifecycle method\. This gives you full access to the publisher, which can be used to create an Rx Flowable\.

 **Code** 

```
        SubscribeToShardResponseHandler responseHandler = SubscribeToShardResponseHandler
            .builder()
            .onError(t -> System.err.println("Error during stream - " + t.getMessage()))
            .onEventStream(p -> Flowable.fromPublisher(p)
                                        .ofType(SubscribeToShardEvent.class)
                                        .flatMapIterable(SubscribeToShardEvent::records)
                                        .limit(1000)
                                        .buffer(25)
                                        .subscribe(e -> System.out.println("Record batch = " + e)))
            .build();
```

You can also use the `publisherTransformer` method with the `Flowable` publisher\. You must adapt the `Flowable` publisher to an *SdkPublisher*, as shown in the following example\.

 **Code** 

```
        SubscribeToShardResponseHandler responseHandler = SubscribeToShardResponseHandler
            .builder()
            .onError(t -> System.err.println("Error during stream - " + t.getMessage()))
            .publisherTransformer(p -> SdkPublisher.adapt(Flowable.fromPublisher(p).limit(100)))
            .build();
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/kinesis/src/main/java/com/example/kinesis/KinesisStreamRxJavaEx.java) on GitHub\.

## More information<a name="more-information"></a>
+  [SubscribeToShardEvent](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_SubscribeToShardEvent.html) in the Amazon Kinesis API Reference
+  [SubscribeToShard](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_SubscribeToShard.html) in the Amazon Kinesis API Reference