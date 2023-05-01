# Batch operations<a name="ddb-en-client-use-multiop-batch"></a>

The DynamoDB Enhanced Client API offers two batch methods [`batchGetItem`\(\)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbEnhancedClient.html#batchGetItem(java.util.function.Consumer)) and [`batchWriteItem`\(\)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbEnhancedClient.html#batchWriteItem(java.util.function.Consumer))\.

## `batchGetItem()` example<a name="ddb-en-client-use-multiop-batch-get"></a>

The [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbEnhancedClient.html#batchGetItem(java.util.function.Consumer)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbEnhancedClient.html#batchGetItem(java.util.function.Consumer)) method lets you retrieve up to 100 individual items across multiple tables in one overall request\. The following example uses the [`Customer`](ddb-en-client-gs-tableschema.md#ddb-en-client-gs-tableschema-anno-bean-cust) and [`MovieActor`](ddb-en-client-use-multirecord-query.md#ddb-en-client-use-movieactor-class) data classes shown previously\.

In the example after lines 1 and 2, we build `[ReadBatch](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/ReadBatch.html)` objects that we later add as parameters to the `batchGetItem()` method after comment line 3\. The code after comment line 1 builds the batch to read from the `Customer` table\. The code after comment line 1a shows the use of a `[GetItemEnhancedRequest](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/GetItemEnhancedRequest.Builder.html)` builder that takes primary key values to specify the item to read\. In contrast to specifying key values to request an item, you can use a data class to request an item as shown after comment line 1b\. The SDK extracts the key values behind the scenes before submitting the request\.

When you specify the item using the key\-based approach as shown in the two statements after 2a, you can also specify that DynamoDB should perform a[ strongly consistent read](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.ReadConsistency.html)\. When the `consistentRead()` method is used, it must be used on all requested items for the same table\.

To retrieve the items that DynamoDB found, use the `[resultsForTable\(\) ](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/BatchGetResultPage.html#resultsForTable(software.amazon.awssdk.enhanced.dynamodb.MappedTableResource))`method that is shown after comment line 4\. Call the method for each table that was read in the request\. `resultsForTable()` returns a list of found items that you can process using any `java.util.List` method\. This example logs each item\.

To discover items that DynamoDB did not process, use the approach after comment line 5\. The `BatchGetResultPage` class has the `[unproccessKeysForTable\(\)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/BatchGetResultPage.html#unprocessedKeysForTable(software.amazon.awssdk.enhanced.dynamodb.MappedTableResource))` method that gives you access to each key that was unprocessed\. The [BatchGetItem API reference](https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_BatchGetItem.html) has more information about situations that result in unprocessed items\.

```
    public static void batchGetItemExample(DynamoDbEnhancedClient enhancedClient,
                                           DynamoDbTable<Customer> customerTable,
                                           DynamoDbTable<MovieActor> movieActorTable) {

        Customer customer2 = new Customer();
        customer2.setId("2");
        customer2.setEmail("cust2@orgname.org");

        // 1. Build a batch to read from the Customer table.
        ReadBatch customerBatch = ReadBatch.builder(Customer.class)
                .mappedTableResource(customerTable)
                // 1a. Specify the primary key values for the item
                .addGetItem(b -> b.key(k -> k.partitionValue("1").sortValue("cust1@orgname.org")))
                // 1b. Alternatively, supply a data class instances to provide the primary key values.
                .addGetItem(customer2)
                .build();

        // 2. Build a batch to read from the MovieActor table.
        ReadBatch moveActorBatch = ReadBatch.builder(MovieActor.class)
                .mappedTableResource(movieActorTable)
                // 2a. Call consistentRead(Boolean.TRUE) for each item for the same table.
                .addGetItem(b -> b.key(k -> k.partitionValue("movie01").sortValue("actor1")).consistentRead(Boolean.TRUE))
                .addGetItem(b -> b.key(k -> k.partitionValue("movie01").sortValue("actor4")).consistentRead(Boolean.TRUE))
                .build();

        // 3. Add ReadBatch objects to the request.
        BatchGetResultPageIterable resultPages = enhancedClient.batchGetItem(b -> b.readBatches(customerBatch, moveActorBatch));

        // 4. Retrieve the successfully requested items from each table.
        resultPages.resultsForTable(customerTable).forEach(item -> logger.info(item.toString()));
        resultPages.resultsForTable(movieActorTable).forEach(item -> logger.info(item.toString()));

        // 5. Retrieve the keys of the items requested but not processed by the service.
        resultPages.forEach((BatchGetResultPage pageResult) -> {
            pageResult.unprocessedKeysForTable(customerTable).forEach(key -> logger.info("Unprocessed item key: " + key.toString()));
            pageResult.unprocessedKeysForTable(customerTable).forEach(key -> logger.info("Unprocessed item key: " + key.toString()));
        });
    }
```

Assume that the following items are in the two tables before running the example code\.

### Items in tables<a name="ddb-en-client-use-multiop-batch-get-tableitems"></a>

```
Customer [id=1, name=CustName1, email=cust1@orgname.org, regDate=2023-03-31T15:46:27.688Z]
Customer [id=2, name=CustName2, email=cust2@orgname.org, regDate=2023-03-31T15:46:28.688Z]
Customer [id=3, name=CustName3, email=cust3@orgname.org, regDate=2023-03-31T15:46:29.688Z]
Customer [id=4, name=CustName4, email=cust4@orgname.org, regDate=2023-03-31T15:46:30.688Z]
Customer [id=5, name=CustName5, email=cust5@orgname.org, regDate=2023-03-31T15:46:31.689Z]
MovieActor{movieName='movie01', actorName='actor0', actingAward='actingaward0', actingYear=2001, actingSchoolName='null'}
MovieActor{movieName='movie01', actorName='actor1', actingAward='actingaward1', actingYear=2001, actingSchoolName='actingschool1'}
MovieActor{movieName='movie01', actorName='actor2', actingAward='actingaward2', actingYear=2001, actingSchoolName='actingschool2'}
MovieActor{movieName='movie01', actorName='actor3', actingAward='actingaward3', actingYear=2001, actingSchoolName='null'}
MovieActor{movieName='movie01', actorName='actor4', actingAward='actingaward4', actingYear=2001, actingSchoolName='actingschool4'}
```

The following output shows the items returned and logged after comment line 4\.

```
Customer [id=1, name=CustName1, email=cust1@orgname.org, regDate=2023-03-31T15:46:27.688Z]
Customer [id=2, name=CustName2, email=cust2@orgname.org, regDate=2023-03-31T15:46:28.688Z]
MovieActor{movieName='movie01', actorName='actor4', actingAward='actingaward4', actingYear=2001, actingSchoolName='actingschool4'}
MovieActor{movieName='movie01', actorName='actor1', actingAward='actingaward1', actingYear=2001, actingSchoolName='actingschool1'}
```

## `batchWriteItem()` example<a name="ddb-en-client-use-multiop-batch-write"></a>

The `batchWriteItem()` method puts or deletes multiple items in one or more tables\. You can specify up to 25 individual put or delete operations in the request\. The following example uses the [`ProductCatalog`](ddb-en-client-use.md#ddb-en-client-use-compare-cs3) and [`MovieActor`](ddb-en-client-use-multirecord-query.md#ddb-en-client-use-movieactor-class) model classes shown previously\.

`WriteBatch` objects are built after comment lines 1 and 2\. For the `ProductCatalog` table, the code puts one item and deletes one item\. For the `MovieActor` table after comment line 2, the code puts two items and deletes one\.

The `batchWriteItem` method is called after comment line 3\. The `[builder](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/BatchWriteItemEnhancedRequest.Builder.html)` parameter provides the batch requests for each table\.

The returned `[BatchWriteResult](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/BatchWriteResult.html)` object provides separate methods for each operation to view unprocessed requests\. The code after comment line 4a provides the keys for unprocessed delete requests and the code after comment line 4b provides the unprocessed put items\.

```
    public static void batchWriteItemExample(DynamoDbEnhancedClient enhancedClient,
                                             DynamoDbTable<ProductCatalog> catalogTable,
                                             DynamoDbTable<MovieActor> movieActorTable) {

        // 1. Build a batch to write to the ProductCatalog table.
        WriteBatch products = WriteBatch.builder(ProductCatalog.class)
                .mappedTableResource(catalogTable)
                .addPutItem(b -> b.item(getProductCatItem1()))
                .addDeleteItem(b -> b.key(k -> k
                        .partitionValue(getProductCatItem2().id())
                        .sortValue(getProductCatItem2().title())))
                .build();

        // 2. Build a batch to write to the MovieActor table.
        WriteBatch movies = WriteBatch.builder(MovieActor.class)
                .mappedTableResource(movieActorTable)
                .addPutItem(getMovieActorYeoh())
                .addPutItem(getMovieActorBlanchettPartial())
                .addDeleteItem(b -> b.key(k -> k
                        .partitionValue(getMovieActorStreep().getMovieName())
                        .sortValue(getMovieActorStreep().getActorName())))
                .build();

        // 3. Add WriteBatch objects to the request.
        BatchWriteResult batchWriteResult = enhancedClient.batchWriteItem(b -> b.writeBatches(products, movies));
        // 4. Retrieve keys for items the service did not process.
        // 4a. 'unprocessedDeleteItemsForTable()' returns keys for delete requests that did not process.
        if (batchWriteResult.unprocessedDeleteItemsForTable(movieActorTable).size() > 0) {
            batchWriteResult.unprocessedDeleteItemsForTable(movieActorTable).forEach(key ->
                    logger.info(key.toString()));
        }
        // 4b. 'unprocessedPutItemsForTable()' returns keys for put requests that did not process.
        if (batchWriteResult.unprocessedPutItemsForTable(catalogTable).size() > 0) {
            batchWriteResult.unprocessedPutItemsForTable(catalogTable).forEach(key ->
                    logger.info(key.toString()));
        }
    }
```

The following helper methods provide the model objects for the put and delete operations\.

### Helper methods<a name="ddb-en-client-use-multiop-batch-write-helpers"></a>

```
 1.     public static ProductCatalog getProductCatItem1() {
 2.         return ProductCatalog.builder()
 3.                 .id(2)
 4.                 .isbn("1-565-85698")
 5.                 .authors(new HashSet<>(Arrays.asList("a", "b")))
 6.                 .price(BigDecimal.valueOf(30.22))
 7.                 .title("Title 55")
 8.                 .build();
 9.     }
10. 
11.     public static ProductCatalog getProductCatItem2() {
12.         return ProductCatalog.builder()
13.                 .id(4)
14.                 .price(BigDecimal.valueOf(40.00))
15.                 .title("Title 1")
16.                 .build();
17.     }  
18. 
19.     public static MovieActor getMovieActorBlanchettPartial() {
20.         MovieActor movieActor = new MovieActor();
21.         movieActor.setActorName("Cate Blanchett");
22.         movieActor.setMovieName("Blue Jasmine");
23.         movieActor.setActingYear(2023);
24.         movieActor.setActingAward("Best Actress");
25.         return movieActor;
26.     }
27. 
28.     public static MovieActor getMovieActorStreep() {
29.         MovieActor movieActor = new MovieActor();
30.         movieActor.setActorName("Meryl Streep");
31.         movieActor.setMovieName("Sophie's Choice");
32.         movieActor.setActingYear(1982);
33.         movieActor.setActingAward("Best Actress");
34.         movieActor.setActingSchoolName("Yale School of Drama");
35.         return movieActor;
36.     }
37. 
38.     public static MovieActor getMovieActorYeoh(){
39.         MovieActor movieActor = new MovieActor();
40.         movieActor.setActorName("Michelle Yeoh");
41.         movieActor.setMovieName("Everything Everywhere All at Once");
42.         movieActor.setActingYear(2023);
43.         movieActor.setActingAward("Best Actress");
44.         movieActor.setActingSchoolName("Royal Academy of Dance");
45.         return movieActor;
46.     }
```

Assume that the tables contain the following items before you run the example code\.

```
MovieActor{movieName='Blue Jasmine', actorName='Cate Blanchett', actingAward='Best Actress', actingYear=2013, actingSchoolName='National Institute of Dramatic Art'}
MovieActor{movieName='Sophie's Choice', actorName='Meryl Streep', actingAward='Best Actress', actingYear=1982, actingSchoolName='Yale School of Drama'}
ProductCatalog{id=4, title='Title 1', isbn='orig_isbn', authors=[b, g], price=10}
```

After the example code finishes, the tables contain the following items\.

```
MovieActor{movieName='Blue Jasmine', actorName='Cate Blanchett', actingAward='Best Actress', actingYear=2013, actingSchoolName='null'}
MovieActor{movieName='Everything Everywhere All at Once', actorName='Michelle Yeoh', actingAward='Best Actress', actingYear=2023, actingSchoolName='Royal Academy of Dance'}
ProductCatalog{id=2, title='Title 55', isbn='1-565-85698', authors=[a, b], price=30.22}
```

Notice in the `MovieActor` table that the `Blue Jasmine` movie item has been replaced with the item used in the put request acquired through the `getMovieActorBlanchettPartial()` helper method\. If a data bean attribute value was not provided, the value in the database is removed\. This is why the resulting `actingSchoolName` is null for the `Blue Jasmine` movie item\.

**Note**  
Although the API documentation suggests that condition expressions can be used and that consumed capacity and collection metrics can be returned with individual [put](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/PutItemEnhancedRequest.html) and [delete](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/DeleteItemEnhancedRequest.html) requests, this is not the case in a batch write scenario\. To improve performance for batch operations, these individual options are ignored\.