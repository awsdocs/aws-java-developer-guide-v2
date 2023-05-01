# `transactWriteItems()` examples<a name="ddb-en-client-use-multiop-trans-writeitems"></a>

The `[transactWriteItems\(\)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbEnhancedClient.html#transactWriteItems(java.util.function.Consumer))` accepts up to 100 put, update, or delete actions in a single atomic transaction across multiple tables\. The DynamoDB Developer Guide contains details about restrictions and failure conditions of the [underlying DynamoDB service operation](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/transaction-apis.html#transaction-apis-txwriteitems)\.

## Basic example<a name="ddb-en-client-use-multiop-trans-writeitems-basic"></a>

In the following example, four operations are requested for two tables\. The corresponding model classes [`ProductCatalog`](ddb-en-client-use.md#ddb-en-client-use-compare-cs3) and [`MovieActor`](ddb-en-client-use-multirecord-query.md#ddb-en-client-use-movieactor-class) were shown previously\.

Each of the three possible operations—put, update, and delete—uses a dedicated request parameter to specify the details\. 

The code after comment line 1 shows the simple variation of the `addPutItem()` method\. The method accepts a `[MappedTableResource](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/MappedTableResource.html)` object and the data object instance to put\. The statement after comment line 2 shows the variation that accepts a `[TransactPutItemEnhancedRequest](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/TransactPutItemEnhancedRequest.html)` instance\. This variation lets you add more options in the request, such as a condition expression\. A subsequent [example](#ddb-en-client-use-multiop-trans-writeitems-opcondition) shows a condition expression for an individual operation\.

An update operation is requested after comment line 3\. `[TransactUpdateItemEnhancedRequest](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/TransactUpdateItemEnhancedRequest.Builder.html)` has an `ignoreNulls() `method that lets you configure what the SDK does with `null` values on the model object\. If the `ignoreNulls()` method returns true, the SDK does not remove the table's attribute values for data object attributes that are `null`\. If the `ignoreNulls()` method returns false, the SDK requests the DynamoDB service to remove the attributes from the item in the table\. The default value for `ignoreNulls` is false\.

The statement after comment line 4 shows the variation of a delete request that takes a data object\. The enhanced client extracts the key values before dispatching the final request\.

```
    public static void transactWriteItems(DynamoDbEnhancedClient enhancedClient,
                                          DynamoDbTable<ProductCatalog> catalogTable,
                                          DynamoDbTable<MovieActor> movieActorTable) {

        enhancedClient.transactWriteItems(b -> b
                // 1. Simplest variation of put item request.
                .addPutItem(catalogTable, getProductCatId2())
                // 2. Put item request variation that accommodates condition expressions.
                .addPutItem(movieActorTable, TransactPutItemEnhancedRequest.builder(MovieActor.class)
                        .item(getMovieActorStreep())
                        .conditionExpression(Expression.builder().expression("attribute_not_exists (movie)").build())
                        .build())
                // 3. Update request that does not remove attribute values on the table if the data object's value is null.
                .addUpdateItem(catalogTable, TransactUpdateItemEnhancedRequest.builder(ProductCatalog.class)
                        .item(getProductCatId4ForUpdate())
                        .ignoreNulls(Boolean.TRUE)
                        .build())
                // 4. Variation of delete request that accepts a data object. The key values are extracted for the request.
                .addDeleteItem(movieActorTable, getMovieActorBlanchett())
        );
    }
```

The following helper methods provide the data objects for the `add*Item` parameters\.

### Helper methods<a name="ddb-en-client-use-multiop-trans-writeitems-basic-helpers"></a>

```
    public static ProductCatalog getProductCatId2() {
        return ProductCatalog.builder()
                .id(2)
                .isbn("1-565-85698")
                .authors(new HashSet<>(Arrays.asList("a", "b")))
                .price(BigDecimal.valueOf(30.22))
                .title("Title 55")
                .build();
    }

    public static ProductCatalog getProductCatId4ForUpdate() {
        return ProductCatalog.builder()
                .id(4)
                .price(BigDecimal.valueOf(40.00))
                .title("Title 1")
                .build();
    }

    public static MovieActor getMovieActorBlanchett() {
        MovieActor movieActor = new MovieActor();
        movieActor.setActorName("Cate Blanchett");
        movieActor.setMovieName("Tar");
        movieActor.setActingYear(2022);
        movieActor.setActingAward("Best Actress");
        movieActor.setActingSchoolName("National Institute of Dramatic Art");
        return movieActor;
    }

    public static MovieActor getMovieActorStreep() {
        MovieActor movieActor = new MovieActor();
        movieActor.setActorName("Meryl Streep");
        movieActor.setMovieName("Sophie's Choice");
        movieActor.setActingYear(1982);
        movieActor.setActingAward("Best Actress");
        movieActor.setActingSchoolName("Yale School of Drama");
        return movieActor;
    }
```

The DynamoDB tables contain the following items before the code example runs\.

```
1 | ProductCatalog{id=4, title='Title 1', isbn='orig_isbn', authors=[b, g], price=10}
2 | MovieActor{movieName='Tar', actorName='Cate Blanchett', actingAward='Best Actress', actingYear=2022, actingSchoolName='National Institute of Dramatic Art'}
```

The following items are in the tables after the code finishes running\.

```
3 | ProductCatalog{id=2, title='Title 55', isbn='1-565-85698', authors=[a, b], price=30.22}
4 | ProductCatalog{id=4, title='Title 1', isbn='orig_isbn', authors=[b, g], price=40.0}
5 | MovieActor{movieName='Sophie's Choice', actorName='Meryl Streep', actingAward='Best Actress', actingYear=1982, actingSchoolName='Yale School of Drama'}
```

The item on line 2 has been deleted and lines 3 and 5 show the items that were put\. Line 4 shows the update of line 1\. The 'price' value is the only value that changed on the item\. If `ignoreNulls()` had returned false, line 4 would look like the following line\.

```
ProductCatalog{id=4, title='Title 1', isbn='null', authors=null, price=40.0}
```

## Condition check example<a name="ddb-en-client-use-multiop-trans-writeitems-checkcond"></a>

The following example shows the use of a condition check\. A condition check is used to check that an item exists or to check the condition of specific attributes of an item in the database\. The item checked in the condition check cannot be used in another operation in the transaction\.

**Note**  
You can't target the same item with multiple operations within the same transaction\. For example, you can't perform a condition check and also attempt to update the same item in the same transaction\.

The example shows one of each type of operation in a transactional write items request\. After comment line 2, the `addConditionCheck()` method supplies the condition that fails the transaction if the `conditionExpression` parameter evaluates to `false`\. The condition expression that is returned from the method shown in the Helper methods block checks if the award year for the movie 'Sophie's Choice' is not equal to 1982\. If it is the expression evaluates to `false` and the transaction fails\.

This guide discusses [expressions](ddb-en-client-expressions.md) in depth in a following topic\.

```
    public static void conditionCheckFailExample(DynamoDbEnhancedClient enhancedClient,
                                                 DynamoDbTable<ProductCatalog> catalogTable,
                                                 DynamoDbTable<MovieActor> movieActorTable) {

        try {
            enhancedClient.transactWriteItems(b -> b
                    // 1. Perform one of each type of operation with the next three methods.
                    .addPutItem(catalogTable, TransactPutItemEnhancedRequest.builder(ProductCatalog.class)
                            .item(getProductCatId2()).build())
                    .addUpdateItem(catalogTable, TransactUpdateItemEnhancedRequest.builder(ProductCatalog.class)
                            .item(getProductCatId4ForUpdate())
                            .ignoreNulls(Boolean.TRUE).build())
                    .addDeleteItem(movieActorTable, TransactDeleteItemEnhancedRequest.builder()
                            .key(b1 -> b1
                                    .partitionValue(getMovieActorBlanchett().getMovieName())
                                    .sortValue(getMovieActorBlanchett().getActorName())).build())
                    // 2. Add a condition check on a table item that is not involved in another operation in this request.
                    .addConditionCheck(movieActorTable, ConditionCheck.builder()
                            .conditionExpression(buildConditionCheckExpression())
                            .key(k -> k
                                    .partitionValue("Sophie's Choice")
                                    .sortValue("Meryl Streep"))
                            // 3. Specify the request to return existing values from the item if the condition evaluates to true.
                            .returnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD)
                            .build())
                    .build());
        // 4. Catch the exception if the transaction fails and log the information.
        } catch (TransactionCanceledException ex) {
            ex.cancellationReasons().stream().forEach(cancellationReason -> {
                logger.info(cancellationReason.toString());
            });
        }
    }
```

The following helper methods are used in the previous code example\.

### Helper methods<a name="ddb-en-client-use-multiop-trans-writeitems-checkcond-helpers"></a>

```
    private static Expression buildConditionCheckExpression() {
        Map<String, AttributeValue> expressionValue = Map.of(
                ":year", numberValue(1982));

        return Expression.builder()
                .expression("actingyear <> :year")
                .expressionValues(expressionValue)
                .build();
    }

    public static ProductCatalog getProductCatId2() {
        return ProductCatalog.builder()
                .id(2)
                .isbn("1-565-85698")
                .authors(new HashSet<>(Arrays.asList("a", "b")))
                .price(BigDecimal.valueOf(30.22))
                .title("Title 55")
                .build();
    }

    public static ProductCatalog getProductCatId4ForUpdate() {
        return ProductCatalog.builder()
                .id(4)
                .price(BigDecimal.valueOf(40.00))
                .title("Title 1")
                .build();
    }

    public static MovieActor getMovieActorBlanchett() {
        MovieActor movieActor = new MovieActor();
        movieActor.setActorName("Cate Blanchett");
        movieActor.setMovieName("Blue Jasmine");
        movieActor.setActingYear(2013);
        movieActor.setActingAward("Best Actress");
        movieActor.setActingSchoolName("National Institute of Dramatic Art");
        return movieActor;
    }
```

The DynamoDB tables contain the following items before the code example runs\.

```
1 | ProductCatalog{id=4, title='Title 1', isbn='orig_isbn', authors=[b, g], price=10}
2 | MovieActor{movieName='Sophie's Choice', actorName='Meryl Streep', actingAward='Best Actress', actingYear=1982, actingSchoolName='Yale School of Drama'}
3 | MovieActor{movieName='Tar', actorName='Cate Blanchett', actingAward='Best Actress', actingYear=2022, actingSchoolName='National Institute of Dramatic Art'}
```

The following items are in the tables after the code finishes running\.

```
ProductCatalog{id=4, title='Title 1', isbn='orig_isbn', authors=[b, g], price=10}
MovieActor{movieName='Sophie's Choice', actorName='Meryl Streep', actingAward='Best Actress', actingYear=1982, actingSchoolName='Yale School of Drama'}
MovieActor{movieName='Tar', actorName='Cate Blanchett', actingAward='Best Actress', actingYear=2022, actingSchoolName='National Institute of Dramatic Art'}
```

Items remain unchanged in the tables because the transaction failed\. The `actingYear` value for the movie *Sophie's Choice* is 1982 as shown on line 2 of the items in the table before the `transactWriteItem()` method was called\.

To capture the cancellation information for the transaction, enclose the `transactWriteItems()` method call in a `try` block and `catch` the [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/model/TransactionCanceledException.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/model/TransactionCanceledException.html)\. After comment line 4 of the example, the code logs each `[CancellationReason](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/model/CancellationReason.html)` object\. Because the code following comment line 3 of the example specifies that values should be returned for the item that caused the transaction to fail, the log displays the raw database values for the *Sophie's Choice* movie item\.

```
CancellationReason(Code=None)
CancellationReason(Code=None)
CancellationReason(Code=None)
CancellationReason(Item={actor=AttributeValue(S=Meryl Streep), movie=AttributeValue(S=Sophie's Choice), actingaward=AttributeValue(S=Best Actress), actingyear=AttributeValue(N=1982), actingschoolname=AttributeValue(S=Yale School of Drama)}, ¬
    Code=ConditionalCheckFailed, Message=The conditional request failed.)
```

## Single operation condition example<a name="ddb-en-client-use-multiop-trans-writeitems-opcondition"></a>

The following example shows the use of a condition on a single operation in a transaction request\. The delete operation after comment line 1 contains a condition that checks the value of the target item of the operation against the database\. In this example, the condition expression created with the helper method after comment line 2 specifies that the item should be deleted from the database if the acting year of the movie is not equal to 2013\.

[Expressions](ddb-en-client-expressions.md) are discussed later in this guide\.

```
    public static void singleOperationConditionFailExample(DynamoDbEnhancedClient enhancedClient,
                                                           DynamoDbTable<ProductCatalog> catalogTable,
                                                           DynamoDbTable<MovieActor> movieActorTable) {
        try {
            enhancedClient.transactWriteItems(b -> b
                    .addPutItem(catalogTable, TransactPutItemEnhancedRequest.builder(ProductCatalog.class)
                            .item(getProductCatId2())
                            .build())
                    .addUpdateItem(catalogTable, TransactUpdateItemEnhancedRequest.builder(ProductCatalog.class)
                            .item(getProductCatId4ForUpdate())
                            .ignoreNulls(Boolean.TRUE).build())
                    // 1. Delete operation that contains a condition expression
                    .addDeleteItem(movieActorTable, TransactDeleteItemEnhancedRequest.builder()
                            .key((Key.Builder k) -> {
                                MovieActor blanchett = getMovieActorBlanchett();
                                k.partitionValue(blanchett.getMovieName())
                                        .sortValue(blanchett.getActorName());
                            })
                            .conditionExpression(buildDeleteItemExpression())
                            .returnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD)
                            .build())
                    .build());
        } catch (TransactionCanceledException ex) {
            ex.cancellationReasons().forEach(cancellationReason -> logger.info(cancellationReason.toString()));
        }
    }

    // 2. Provide condition expression to check if 'actingyear' is not equal to 2013.
    private static Expression buildDeleteItemExpression() {
        Map<String, AttributeValue> expressionValue = Map.of(
                ":year", numberValue(2013));

        return Expression.builder()
                .expression("actingyear <> :year")
                .expressionValues(expressionValue)
                .build();
    }
```

The following helper methods are used in the previous code example\.

### Helper methods<a name="ddb-en-client-use-multiop-trans-writeitems-opcondition-helpers"></a>

```
    public static ProductCatalog getProductCatId2() {
        return ProductCatalog.builder()
                .id(2)
                .isbn("1-565-85698")
                .authors(new HashSet<>(Arrays.asList("a", "b")))
                .price(BigDecimal.valueOf(30.22))
                .title("Title 55")
                .build();
    }

    public static ProductCatalog getProductCatId4ForUpdate() {
        return ProductCatalog.builder()
                .id(4)
                .price(BigDecimal.valueOf(40.00))
                .title("Title 1")
                .build();
    }
    public static MovieActor getMovieActorBlanchett() {
        MovieActor movieActor = new MovieActor();
        movieActor.setActorName("Cate Blanchett");
        movieActor.setMovieName("Blue Jasmine");
        movieActor.setActingYear(2013);
        movieActor.setActingAward("Best Actress");
        movieActor.setActingSchoolName("National Institute of Dramatic Art");
        return movieActor;
    }
```

The DynamoDB tables contain the following items before the code example runs\.

```
1 | ProductCatalog{id=4, title='Title 1', isbn='orig_isbn', authors=[b, g], price=10}
2 | MovieActor{movieName='Blue Jasmine', actorName='Cate Blanchett', actingAward='Best Actress', actingYear=2013, actingSchoolName='National Institute of Dramatic Art'}
```

The following items are in the tables after the code finishes running\.

```
ProductCatalog{id=4, title='Title 1', isbn='orig_isbn', authors=[b, g], price=10}
2023-03-15 11:29:07 [main] INFO  org.example.tests.TransactDemoTest:168 - MovieActor{movieName='Blue Jasmine', actorName='Cate Blanchett', actingAward='Best Actress', actingYear=2013, actingSchoolName='National Institute of Dramatic Art'}
```

Items remain unchanged in the tables because the transaction failed\. The `actingYear` value for the movie *Blue Jasmine* is 2013 as shown on line 2 in the list of items before the code example runs\.

The following lines are logged to the console\.

```
CancellationReason(Code=None)
CancellationReason(Code=None)
CancellationReason(Item={actor=AttributeValue(S=Cate Blanchett), movie=AttributeValue(S=Blue Jasmine), actingaward=AttributeValue(S=Best Actress), actingyear=AttributeValue(N=2013), actingschoolname=AttributeValue(S=National Institute of Dramatic Art)}, 
    Code=ConditionalCheckFailed, Message=The conditional request failed)
```