--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Kinesis examples using SDK for Java 2\.x<a name="java_kinesis_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Kinesis\.

*Actions* are code excerpts that show you how to call individual Kinesis functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple Kinesis functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w591aac15c14b9c41c13)

## Actions<a name="w591aac15c14b9c41c13"></a>

### Create a stream<a name="kinesis_CreateStream_java_topic"></a>

The following code example shows how to create a Kinesis stream\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/kinesis#readme)\. 
  

```
    public static void createStream(KinesisClient kinesisClient, String streamName) {

        try {
            CreateStreamRequest streamReq = CreateStreamRequest.builder()
                .streamName(streamName)
                .shardCount(1)
                .build();

            kinesisClient.createStream(streamReq);

        } catch (KinesisException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [CreateStream](https://docs.aws.amazon.com/goto/SdkForJavaV2/kinesis-2013-12-02/CreateStream) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a stream<a name="kinesis_DeleteStream_java_topic"></a>

The following code example shows how to delete a Kinesis stream\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/kinesis#readme)\. 
  

```
    public static void deleteStream(KinesisClient kinesisClient, String streamName) {

        try {
            DeleteStreamRequest delStream = DeleteStreamRequest.builder()
                .streamName(streamName)
                .build();

            kinesisClient.deleteStream(delStream);

        } catch (KinesisException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteStream](https://docs.aws.amazon.com/goto/SdkForJavaV2/kinesis-2013-12-02/DeleteStream) in *AWS SDK for Java 2\.x API Reference*\. 

### Get data in batches from a stream<a name="kinesis_GetRecords_java_topic"></a>

The following code example shows how to get data in batches from a Kinesis stream\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/kinesis#readme)\. 
  

```
    public static void getStockTrades(KinesisClient kinesisClient, String streamName) {

        String shardIterator;
        String lastShardId = null;

        // Retrieve the Shards from a Stream
        DescribeStreamRequest describeStreamRequest = DescribeStreamRequest.builder()
            .streamName(streamName)
            .build();

        List<Shard> shards = new ArrayList<>();
        DescribeStreamResponse streamRes;
        do {
            streamRes = kinesisClient.describeStream(describeStreamRequest);
            shards.addAll(streamRes.streamDescription().shards());

            if (shards.size() > 0) {
                lastShardId = shards.get(shards.size() - 1).shardId();
            }
            } while (streamRes.streamDescription().hasMoreShards());

        GetShardIteratorRequest itReq = GetShardIteratorRequest.builder()
            .streamName(streamName)
            .shardIteratorType("TRIM_HORIZON")
            .shardId(lastShardId)
            .build();

        GetShardIteratorResponse shardIteratorResult = kinesisClient.getShardIterator(itReq);
        shardIterator = shardIteratorResult.shardIterator();

        // Continuously read data records from shard.
        List<Record> records;

        // Create new GetRecordsRequest with existing shardIterator.
        // Set maximum records to return to 1000.
        GetRecordsRequest recordsRequest = GetRecordsRequest.builder()
            .shardIterator(shardIterator)
            .limit(1000)
            .build();

        GetRecordsResponse result = kinesisClient.getRecords(recordsRequest);

        // Put result into record list. Result may be empty.
        records = result.records();

        // Print records
        for (Record record : records) {
            SdkBytes byteBuffer = record.data();
            System.out.printf("Seq No: %s - %s%n", record.sequenceNumber(), new String(byteBuffer.asByteArray()));
        }
    }
```
+ For API details, see the following topics in *AWS SDK for Java 2\.x API Reference*\.
  + [GetRecords](https://docs.aws.amazon.com/goto/SdkForJavaV2/kinesis-2013-12-02/GetRecords)
  + [GetShardIterator](https://docs.aws.amazon.com/goto/SdkForJavaV2/kinesis-2013-12-02/GetShardIterator)

### Put data into a stream<a name="kinesis_PutRecord_java_topic"></a>

The following code example shows how to put data into a Kinesis stream\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/kinesis#readme)\. 
  

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
            e.getMessage();
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
+  For API details, see [PutRecord](https://docs.aws.amazon.com/goto/SdkForJavaV2/kinesis-2013-12-02/PutRecord) in *AWS SDK for Java 2\.x API Reference*\. 