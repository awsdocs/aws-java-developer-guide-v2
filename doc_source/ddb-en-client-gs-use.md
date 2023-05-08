# Perform operations<a name="ddb-en-client-gs-use"></a>

After the table is created, use the `DynamoDbTable` instance to perform operations against the DynamoDB table\. 

In the following example, a singleton `DynamoDbTable<Customer>` is passed as a parameter along with a [`Customer` data class](ddb-en-client-gs-tableschema.md#ddb-en-client-gs-tableschema-anno-bean-cust) instance to add a new item to the table\.

```
    public static void putItemExample(DynamoDbTable<Customer> customerTable, Customer customer){
        logger.info(customer.toString());
        customerTable.putItem(customer);
    }
```

Before sending the `customer` object to the DynamoDB service, log the output of the object's `toString()` method to compare it to what the enhanced client sends\.

```
Customer [id=1, name=Customer Name, email=customer@example.org, regDate=2023-03-16T21:05:43.265Z]
```

Wire\-level logging shows the payload of the generated request\. The enhanced client generated the low\-level representation from the data class\. The `regDate` attribute, which is an `Instant` type in Java, is represented as a DynamoDB string\.

```
{
  "TableName": "Customer",
  "Item": {
    "registrationDate": {
      "S": "2023-03-16T21:05:43.265Z"
    },
    "id": {
      "S": "1"
    },
    "custName": {
      "S": "Customer Name"
    },
    "email": {
      "S": "customer@example.org"
    }
  }
}
```