# Transaction operations<a name="ddb-en-client-use-multiop-trans"></a>

The DynamoDB Enhanced Client API provides the `transactGetItems()` and the `transactWriteItems()` methods\. The transaction methods of the SDK for Java provide atomicity, consistency, isolation, and durability \(ACID\) in DynamoDB tables, helping you to maintain data correctness in your applications\.

**Contents**
+ [`transactGetItems()` example](ddb-en-client-use-multiop-trans-getitems.md)
+ [`transactWriteItems()` examples](ddb-en-client-use-multiop-trans-writeitems.md)
  + [Basic example](ddb-en-client-use-multiop-trans-writeitems.md#ddb-en-client-use-multiop-trans-writeitems-basic)
  + [Condition check example](ddb-en-client-use-multiop-trans-writeitems.md#ddb-en-client-use-multiop-trans-writeitems-checkcond)
  + [Single operation condition example](ddb-en-client-use-multiop-trans-writeitems.md#ddb-en-client-use-multiop-trans-writeitems-opcondition)