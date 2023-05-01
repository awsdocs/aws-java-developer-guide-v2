# Multiple operations<a name="ddb-en-client-use-multiop"></a>

The DynamoDB Enhanced Client API supports multiple options per request with batches and transactions\. The difference between them is that a transaction fails if one or more individual operations fail, but a batch request lets individual operations fail while processing the others\.

**Contents**
+ [Batch operations](ddb-en-client-use-multiop-batch.md)
  + [`batchGetItem()` example](ddb-en-client-use-multiop-batch.md#ddb-en-client-use-multiop-batch-get)
  + [`batchWriteItem()` example](ddb-en-client-use-multiop-batch.md#ddb-en-client-use-multiop-batch-write)
+ [Transaction operations](ddb-en-client-use-multiop-trans.md)
  + [`transactGetItems()` example](ddb-en-client-use-multiop-trans-getitems.md)
  + [`transactWriteItems()` examples](ddb-en-client-use-multiop-trans-writeitems.md)
    + [Basic example](ddb-en-client-use-multiop-trans-writeitems.md#ddb-en-client-use-multiop-trans-writeitems-basic)
    + [Condition check example](ddb-en-client-use-multiop-trans-writeitems.md#ddb-en-client-use-multiop-trans-writeitems-checkcond)
    + [Single operation condition example](ddb-en-client-use-multiop-trans-writeitems.md#ddb-en-client-use-multiop-trans-writeitems-opcondition)