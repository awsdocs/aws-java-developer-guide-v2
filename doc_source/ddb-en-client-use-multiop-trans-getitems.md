# `transactGetItems()` example<a name="ddb-en-client-use-multiop-trans-getitems"></a>

The `[transactGetItems\(\)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbEnhancedClient.html#transactGetItems(java.util.function.Consumer))` method accepts up to 100 individual requests for items\. All items are read in a single atomic transaction\. The *Amazon DynamoDB Developer Guide* has information about the [conditions that cause a `transactGetItems()` method to fail](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/transaction-apis.html#transaction-apis-txgetitems), and also about the isolation level used when you call `[transactGetItem\(\)](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/transaction-apis.html#transaction-isolation)`\.

After comment line 1 in the following example, the code calls the `transactGetItems()` method with a `[builder](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/TransactGetItemsEnhancedRequest.Builder.html)` parameter\. The builder's `[addGetItem\(\)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/TransactGetItemsEnhancedRequest.Builder.html#addGetItem(software.amazon.awssdk.enhanced.dynamodb.MappedTableResource,T))` is invoked three times with a data object that contains the key values that the SDK will use to generate the final request\.

The request returns a list of `[Document](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/Document.html)` objects after comment line 2\. The list of documents that is returned contains non\-null [Document](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/Document.html) instances of item data in the same order as requested\. The `[Document\.getItem\(MappedTableResource<T> mappedTableResource\)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/Document.html#getItem(software.amazon.awssdk.enhanced.dynamodb.MappedTableResource))` method converts an untyped `Document` object into a typed Java object if item data was returned, otherwise the method returns null\.

```
    public static void transactGetItemsExample(DynamoDbEnhancedClient enhancedClient,
                                               DynamoDbTable<ProductCatalog> catalogTable,
                                               DynamoDbTable<MovieActor> movieActorTable) {

        // 1. Request three items from two tables using a builder.
        final List<Document> documents = enhancedClient.transactGetItems(b -> b
                .addGetItem(catalogTable, Key.builder().partitionValue(2).sortValue("Title 55").build())
                .addGetItem(movieActorTable, Key.builder().partitionValue("Sophie's Choice").sortValue("Meryl Streep").build())
                .addGetItem(movieActorTable, Key.builder().partitionValue("Blue Jasmine").sortValue("Cate Blanchett").build())
                .build());

        // 2. A list of Document objects is returned in the same order as requested.
        ProductCatalog title55 = documents.get(0).getItem(catalogTable);
        if (title55 != null) {
            logger.info(title55.toString());
        }

        MovieActor sophiesChoice = documents.get(1).getItem(movieActorTable);
        if (sophiesChoice != null) {
            logger.info(sophiesChoice.toString());
        }

        // 3. The getItem() method returns null if the Document object contains no item from DynamoDB.
        MovieActor blueJasmine = documents.get(2).getItem(movieActorTable);
        if (blueJasmine != null) {
            logger.info(blueJasmine.toString());
        }
    }
```

The DynamoDB tables contain the following items before the code example runs\.

```
ProductCatalog{id=2, title='Title 55', isbn='orig_isbn', authors=[b, g], price=10}
MovieActor{movieName='Sophie's Choice', actorName='Meryl Streep', actingAward='Best Actress', actingYear=1982, actingSchoolName='Yale School of Drama'}
```

The following output is logged\. If an item is requested but not found, it not returned as is the case for the request for the movie named `Blue Jasmine`\.

```
ProductCatalog{id=2, title='Title 55', isbn='orig_isbn', authors=[b, g], price=10}
MovieActor{movieName='Sophie's Choice', actorName='Meryl Streep', actingAward='Best Actress', actingYear=1982, actingSchoolName='Yale School of Drama'}
```