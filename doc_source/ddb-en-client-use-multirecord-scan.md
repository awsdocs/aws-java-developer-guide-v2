# Scan a table<a name="ddb-en-client-use-multirecord-scan"></a>

The SDK's [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbAsyncTable.html#scan(java.util.function.Consumer)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbAsyncTable.html#scan(java.util.function.Consumer)) method corresponds to the [DynamoDB operation](https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_Scan.html) of the same name\. The DynamoDB Enhanced Client API offers the same options but it uses a familiar object model and handles the pagination for you\.

First, explore the `PageIterable` interface by looking at the `scan` method of the synchronous mapping class, [DynamoDbTable](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html)\.

## Use the synchronous API<a name="ddb-en-client-use-multirecord-scan-sync"></a>

The following example shows the `scan` method that uses an [expression](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/Expression.html) to filter the items that are returned\. The [ProductCatalog](ddb-en-client-use.md#ddb-en-client-use-compare-cs3) is the model object that was shown earlier\.

The filtering expression shown after comment line 1 limits the `ProductCatalog` items that are returned to those with a price value between 8\.00 and 80\.00 inclusively\.

This example also excludes the `isbn` values by using the `attributesToProject` method shown after comment line 2\.

On comment line 3, the `PageIterable` object, `pagedResult`, is returned by the `scan` method\. The `stream` method of `PageIterable` returns a [https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html) object, which you can use to process the pages\. In this example, the number of pages is counted and logged\.

Starting with comment line 4, the example shows two variations of accessing the `ProductCatalog` items\. The version after comment line 2a streams through each page and sorts and logs the items on each page\. The version after comment line 2b skips the page iteration and accesses the items directly\.

The `PageIterable` interface offers multiple ways to process results because of its two parent interfaces—[https://docs.oracle.com/javase/8/docs/api/java/lang/Iterable.html](https://docs.oracle.com/javase/8/docs/api/java/lang/Iterable.html) and [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/pagination/sync/SdkIterable.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/pagination/sync/SdkIterable.html)\. `Iterable` brings the `forEach`, `iterator` and `spliterator` methods, and `SdkIterable` brings the `stream` method\.

```
    public static void scanSync(DynamoDbTable<ProductCatalog> productCatalog) {

        Map<String, AttributeValue> expressionValues = Map.of(
                ":min_value", numberValue(8.00),
                ":max_value", numberValue(80.00));

        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .consistentRead(true)
                // 1. the 'attributesToProject()' method allows you to specify which values you want returned.
                .attributesToProject("id", "title", "authors", "price")
                // 2. Filter expression limits the items returned that match the provided criteria.
                .filterExpression(Expression.builder()
                        .expression("price >= :min_value AND price <= :max_value")
                        .expressionValues(expressionValues)
                        .build())
                .build();

        // 3. A PageIterable object is returned by the scan method.
        PageIterable<ProductCatalog> pagedResults = productCatalog.scan(request);
        logger.info("page count: {}", pagedResults.stream().count());

        // 4. Log the returned ProductCatalog items using two variations.
        // 4a. This version sorts and logs the items of each page.
        pagedResults.stream().forEach(p -> p.items().stream()
                .sorted(Comparator.comparing(ProductCatalog::price))
                .forEach(
                        item -> logger.info(item.toString())
                ));
        // 4b. This version sorts and logs all items for all pages.
        pagedResults.items().stream()
                .sorted(Comparator.comparing(ProductCatalog::price))
                .forEach(
                        item -> logger.info(item.toString())
                );
    }
```

## Use the asynchronous API<a name="ddb-en-client-use-multirecord-scan-async"></a>

The asynchronous `scan` method returns results as a `PagePublisher` object\. The `PagePublisher` interface has two `subscribe` methods that you can use to process response pages\. One `subscribe` method comes from the `org.reactivestreams.Publisher` parent interface\. To process pages using this first option, pass a `[Subscriber](https://www.reactive-streams.org/reactive-streams-1.0.0-javadoc/org/reactivestreams/Subscriber.html)` instance to the `subscribe` method\. The first example that follows shows the use of `subscribe` method\.

The second `subscribe` method comes from the [SdkPublisher](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/async/SdkPublisher.html) interface\. This version of `subscribe` accepts a [https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html) rather than a `Subscriber`\. This `subscribe` method variation is shown in the second example that follows\.

The following example shows the asynchronous version of the `scan` method that uses the same filter expression shown in the previous example\. 

After comment line 3, `DynamoDbAsyncTable.scan` returns a `PagePublisher` object\. On the next line, the code creates an instance of the `org.reactivestreams.Subscriber` interface, `ProductCatalogSubscriber`, which subscribes to the `PagePublisher` after comment line 4\.

The `Subscriber` object collects the `ProductCatalog` items from each page in the `onNext` method after comment line 8 in the `ProductCatalogSubscriber` class example\. The items are stored in the private `List` variable and are accessed in the calling code with the `ProductCatalogSubscriber.getSubscribedItems()` method\. This is called after comment line 5\.

After the list is retrieved, the code sorts all `ProductCatalog` items by price and logs each item\.

The [CountDownLatch](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/CountDownLatch.html) in the `ProductCatalogSubscriber` class blocks the calling thread until all items have been added to the list before continuing after comment line 5\. 

```
    public static void scanAsync(DynamoDbAsyncTable productCatalog) {
        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .consistentRead(true)
                .attributesToProject("id", "title", "authors", "price")
                .filterExpression(Expression.builder()
                        // 1. :min_value and :max_value are placeholders for the values provided by the map
                        .expression("price >= :min_value AND price <= :max_value")
                        // 2. Two values are needed for the expression and each is supplied as a map entry.
                        .expressionValues(
                                Map.of( ":min_value", numberValue(8.00),
                                        ":max_value", numberValue(400_000.00)))
                        .build())
                .build();

        // 3. A PagePublisher object is returned by the scan method.
        PagePublisher<ProductCatalog> pagePublisher = productCatalog.scan(request);
        ProductCatalogSubscriber subscriber = new ProductCatalogSubscriber();
        // 4. Subscribe the ProductCatalogSubscriber to the PagePublisher.
        pagePublisher.subscribe(subscriber);
        // 5. Retrieve all collected ProductCatalog items accumulated by the subscriber.
        subscriber.getSubscribedItems().stream()
                .sorted(Comparator.comparing(ProductCatalog::price))
                .forEach(item ->
                        logger.info(item.toString()));
        // 6. Use a Consumer to work through each page.
        pagePublisher.subscribe(page -> page
                        .items().stream()
                        .sorted(Comparator.comparing(ProductCatalog::price))
                        .forEach(item ->
                                logger.info(item.toString())))
                .join(); // If needed, blocks the subscribe() method thread until it is finished processing.
        // 7. Use a Consumer to work through each ProductCatalog item.
        pagePublisher.items()
                .subscribe(product -> logger.info(product.toString()))
                .exceptionally(failure -> {
                    logger.error("ERROR  - ", failure);
                    return null;
                })
                .join(); // If needed, blocks the subscribe() method thread until it is finished processing.
    }
```

```
    private static class ProductCatalogSubscriber implements Subscriber<Page<ProductCatalog>> {
        private CountDownLatch latch = new CountDownLatch(1);
        private Subscription subscription;
        private List<ProductCatalog> itemsFromAllPages = new ArrayList<>();

        @Override
        public void onSubscribe(Subscription sub) {
            subscription = sub;
            subscription.request(1L);
            try {
                latch.await(); // Called by main thread blocking it until latch is released.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onNext(Page<ProductCatalog> productCatalogPage) {
            // 8. Collect all the ProductCatalog instances in the page, then ask the publisher for one more page.
            itemsFromAllPages.addAll(productCatalogPage.items());
            subscription.request(1L);
        }

        @Override
        public void onError(Throwable throwable) {
        }

        @Override
        public void onComplete() {
            latch.countDown(); // Call by subscription thread; latch releases.
        }

        List<ProductCatalog> getSubscribedItems() {
            return this.itemsFromAllPages;
        }
    }
```

The following snippet example uses the version of the `PagePublisher.subscribe` method that accepts a `Consumer` after comment line 6\. The Java lambda parameter consumes pages, which further process each item\. In this example, each page is processed and the items on each page are sorted and then logged\.

```
        // 6. Use a Consumer to work through each page.
        pagePublisher.subscribe(page -> page
                        .items().stream()
                        .sorted(Comparator.comparing(ProductCatalog::price))
                        .forEach(item ->
                                logger.info(item.toString())))
                .join(); // If needed, blocks the subscribe() method thread until it is finished processing.
```

The `items` method of `PagePublisher` unwraps the model instances so that your code can process the items directly\. This approach is shown in the following snippet\.

```
        // 7. Use a Consumer to work through each ProductCatalog item.
        pagePublisher.items()
                .subscribe(product -> logger.info(product.toString()))
                .exceptionally(failure -> {
                    logger.error("ERROR  - ", failure);
                    return null;
                })
                .join(); // If needed, blocks the subscribe() method thread until it is finished processing.
```