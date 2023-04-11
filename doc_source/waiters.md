# Using waiters in the AWS SDK for Java 2\.x<a name="waiters"></a>

The waiters utility of the AWS SDK for Java 2\.x enables you to validate that AWS resources are in a specified state before performing operations on those resources\.

A *waiter* is an abstraction used to poll AWS resources, such as DynamoDB tables or Amazon S3 buckets, until a desired state is reached \(or until a determination is made that the resource won’t ever reach the desired state\)\. Instead of writing logic to continuously poll your AWS resources, which can be cumbersome and error\-prone, you can use waiters to poll a resource and have your code continue to run after the resource is ready\.

## Prerequisites<a name="prerequisiteswaiters"></a>

Before you can use waiters in a project with the AWS SDK for Java, you must complete the steps in [Setting up the AWS SDK for Java 2\.x](setup.md)\.

You must also configure your project dependencies \(for example, in your `pom.xml` or `build.gradle` file\) to use version `2.15.0` or later of the AWS SDK for Java\.

For example:

```
<project>
  <dependencyManagement>
   <dependencies>
      <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>bom</artifactId>
        <version>2.15.0</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
   </dependencies>
  </dependencyManagement>
</project>
```

## Using waiters<a name="id1waiters"></a>

To instantiate a waiters object, first create a service client\. Set the service client’s `waiter()` method as the value of the waiter object\. Once the waiter instance exists, set its response options to execute the appropriate code\.

### Synchronous programming<a name="synchronous-programming"></a>

The following code snippet shows how to wait for a DynamoDB table to exist and be in an **ACTIVE** state\.

```
DynamoDbClient dynamo = DynamoDbClient.create();
DynamoDbWaiter waiter = dynamo.waiter();

WaiterResponse<DescribeTableResponse> waiterResponse =
  waiter.waitUntilTableExists(r -> r.tableName("myTable"));

// print out the matched response with a tableStatus of ACTIVE
waiterResponse.matched().response().ifPresent(System.out::println);
```

### Asynchronous programming<a name="asynchronous-programming"></a>

The following code snippet shows how to wait for a DynamoDB table to no longer exist\.

```
DynamoDbAsyncClient asyncDynamo = DynamoDbAsyncClient.create();
DynamoDbAsyncWaiter asyncWaiter = asyncDynamo.waiter();

CompletableFuture<WaiterResponse<DescribeTableResponse>> waiterResponse =
          asyncWaiter.waitUntilTableNotExists(r -> r.tableName("myTable"));

waiterResponse.whenComplete((r, t) -> {
  if (t == null) {
   // print out the matched ResourceNotFoundException
   r.matched().exception().ifPresent(System.out::println);
  }
}).join();
```

## Configuring waiters<a name="configuring-waiters"></a>

You can customize the configuration for a waiter by using the `overrideConfiguration()` on its builder\. For some operations, you can apply a custom configuration when you make the request\.

### Configure a waiter<a name="configure-a-waiter"></a>

The following code snippet shows how to override the configuration on a waiter\.

```
// sync
DynamoDbWaiter waiter =
   DynamoDbWaiter.builder()
          .overrideConfiguration(b -> b.maxAttempts(10))
          .client(dynamoDbClient)
          .build();
// async
DynamoDbAsyncWaiter asyncWaiter =
   DynamoDbAsyncWaiter.builder()
          .client(dynamoDbAsyncClient)
          .overrideConfiguration(o -> o.backoffStrategy(
               FixedDelayBackoffStrategy.create(Duration.ofSeconds(2))))
          .scheduledExecutorService(Executors.newScheduledThreadPool(3))
          .build();
```

### Override configuration for a specific request<a name="override-configuration-for-a-specific-request"></a>

The following code snippet shows how to override the configuration for a waiter on a per\-request basis\. Note that only some operations have customizable configurations\.

```
waiter.waitUntilTableNotExists(b -> b.tableName("myTable"),
               o -> o.maxAttempts(10));

asyncWaiter.waitUntilTableExists(b -> b.tableName("myTable"),
                 o -> o.waitTimeout(Duration.ofMinutes(1)));
```

## Code examples<a name="code-examples"></a>

For a complete example using waiters with DynamoDB, see [CreateTable\.java](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/CreateTable.java) in the AWS Code Examples Repository\.

For a complete example using waiters with Amazon S3, see [S3BucketOps\.java](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/S3BucketOps.java) in the AWS Code Examples Repository\.