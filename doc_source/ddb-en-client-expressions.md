# Expressions and conditions<a name="ddb-en-client-expressions"></a>

Expressions in the DynamoDB Enhanced Client API are java representations of [DynamoDB expressions](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.html)\.

The DynamoDB Enhanced Client API uses three types of expressions:

[Expression](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/Expression.html)  
The `Expression` class is used when you define conditions and filters\.

[https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/QueryConditional.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/QueryConditional.html)  
This type of expression represents [key conditions](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Query.html#Query.KeyConditionExpressions) for query operations\.

[https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/update/UpdateExpression.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/update/UpdateExpression.html)  
This class helps you write DynamoDB [update expressions](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.UpdateExpressions.html) and is currently used in the extension framework when you update an item\.

## Expression anatomy<a name="ddb-en-client-expressions-compoonents"></a>

An expression is made up of:
+ A string expression \(required\)\. The string contains a DynamoDB logic expression with placeholder names for attribute names and attribute values\.
+ A map of expression values \(usually required\)\.
+ A map of expression names \(optional\)\.

Use a builder to generate an`Expression` object that takes the following general form\.

```
Expression espression = Expression.builder()
                            .expression(<String>)
                            .expressionNames(<Map>)
                            .expressionValues(<Map>)
                           .build()
```

`Expression`s usually require a map of expression values that provides the values for the placeholders in the string expression\. The map key consists of the placeholder name preceded with a colon \(`:`\) and the map value is an instance of [AttributeValue](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/model/AttributeValue.html)\. The [AttributeValues](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/internal/AttributeValues.html) class has convenience methods to generate an `AttributeValue` instance from a literal\. Alternatively, you can use the `AttributeValue.Builder` to generate an `AttributeValue` instance\.

The following snippet shows a map with two entries after comment line 2\. The string passed to the `expression()` method, shown after comment line 1, contains the placeholders that DynamoDB resolves before performing the operation\. This snippet contains no map of expression names, because *price* is a permissible attribute name\.

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
```

If an attribute name in the DynamoDB table is a reserved word, begins with a number, or contains a space, a map of expression names is required for the `Expression`\.

For example, if the attribute name was `1price` instead of `price` in the previous code example, the example would need to be modified as shown in the following example\.

```
        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .filterExpression(Expression.builder()
                        .expression("#price >= :min_value AND #price <= :max_value")
                        .expressionNames( Map.of("#price", "1price") )
                        .expressionValues(
                                Map.of(":min_value", numberValue(8.00),
                                        ":max_value", numberValue(400_000.00)))
                        .build())
                .build();
```

A placeholder for an expression name begins with the pound sign \(`#`\)\. An entry for the map of expression names uses the placeholder as the key and the attribute name as the value\. The map is added to the expression builder with the `expressionNames()` method\. DynamoDB resolves the attribute name before it performs the operation\.

Expression values are not required if a function is used in the string expression\. An example of an expression function is `attribute_exists(<attribute_name>)`\.

The following example builds an `Expression` that uses a [DynamoDB function](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.OperatorsAndFunctions.html#Expressions.OperatorsAndFunctions.Functions)\. The expression string in this example uses no placeholders\. This expression could be used on a `putItem` operation to check if an item already exists in the database with a `movie` attribute's value equal to the data object's `movie` attribute\.

```
Expression exp = Expression.builder().expression("attribute_not_exists (movie)").build();
```

The DynamoDB Developer Guide contains complete information on the[ low\-level expressions](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.html) used with DynamoDB\.

## Condition expressions and conditionals<a name="ddb-en-client-expressions-cond"></a>

When you use the `putItem()`, `updateItem()`, and `deleteItem()` methods, and also when you use transaction and batch operations, you use `[Expression](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/Expression.html)` objects to specify conditions that DynamoDB must meet to proceed with the operation\. These expressions are named condition expressions\. For an example, see the condition expression used in the `addDeleteItem()` method \(after comment line 1\) of [transaction example](ddb-en-client-use-multiop-trans-writeitems.md#ddb-en-client-use-multiop-trans-writeitems-opcondition) shown in this guide\.

When you work with the `query()` methods, a condition is expressed as a [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/QueryConditional.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/QueryConditional.html)\. The `QueryConditional` class has several static convenience methods that help you write the criteria that determine which items to read from DynamoDB\.

For examples of `QueryConditionals`, see the first code example of the [`Query` method examples](ddb-en-client-use-multirecord-query.md#ddb-en-client-use-multirecord-query-example) section of this guide\.

## Filter expressions<a name="ddb-en-client-expressions-filter"></a>

Filter expressions are used in scan and query operations to filter the items that are returned\. 

A filter expression is applied after all the data is read from the database, so the read cost is the same as if there were no filter\. The Amazon DynamoDB Developer Guide has more information about using filter expressions for both [query](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Query.html#Query.FilterExpression) and [scan](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Scan.html#Scan.FilterExpression) operations\.

The following example shows a filter expression added to a scan request\. The criteria restricts the items return where the price is between 8\.00 and 80\.00 inclusive\.

```
        Map<String, AttributeValue> expressionValues = Map.of(
                ":min_value", numberValue(8.00),
                ":max_value", numberValue(80.00));

        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .consistentRead(true)
                .attributesToProject("id", "title", "authors", "price")
                .filterExpression(Expression.builder()
                        .expression("price >= :min_value AND price <= :max_value")
                        .expressionValues(expressionValues)
                        .build())
                .build();
```

## Update expressions<a name="ddb-en-client-expressions-update"></a>

The DynamoDB Enhanced Client's `updateItem()` method provides a standard way to update items in DynamoDB\. However, when you require more functionality, [UpdateExpressions](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/update/UpdateExpression.html) provide a type\-safe representation of DynamoDB [update expression syntax\.](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.UpdateExpressions.html) For example, you can use `UpdateExpressions` to increase values without first reading items from DynamoDB, or add individual members to a list\. Update expressions are currently available in custom extensions for the `updateItem()` method\.

For an example that uses update expressions, see the [custom extension example](ddb-en-client-extensions.md#ddb-en-client-extensions-custom) in this guide\.

More information about update expressions is available in the [Amazon DynamoDB Developer Guide](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.UpdateExpressions.html)\.