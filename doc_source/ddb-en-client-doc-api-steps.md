# Preliminary steps to work with the Enhanced Document API<a name="ddb-en-client-doc-api-steps"></a>

The Enhanced Document API requires the same [dependencies](ddb-en-client-getting-started.md#ddb-en-client-gs-dep) that are needed for the DynamoDB Enhanced Client API\. It also requires a [`DynamoDbEnhancedClient` instance](ddb-en-client-getting-started-dynamodbTable.md#ddb-en-client-getting-started-dynamodbTable-eclient) as shown at the start of this topic\.

Because the Enhanced Document API was released with version 2\.20\.3 of the AWS SDK for Java 2\.x, you need that version or greater\.

## Create a `DocumentTableSchema` and a `DynamoDbTable`<a name="ddb-en-client-doc-api-steps-createschema"></a>

To invoke commands against a DynamoDB table using the Enhanced Document API, associate the table with a client\-side [DynamoDbTable<EnhancedDocument>](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html) resource object\. 

The enhanced client's `table()` method creates a `DynamoDbTable<EnhancedDocument>` instance and requires parameters for the DynamoDB table name and a `DocumentTableSchema`\. 

The builder for a [DocumentTableSchema](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/document/DocumentTableSchema.html) requires a primary index key and one or more attribute converter providers\. The `AttributeConverterProvider.defaultProvider()` method provides converters for [default types](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/internal/converter/attribute/package-summary.html)\. It should be specified even if you provide a custom attribute converter provider\. You can add an optional secondary index key to the builder\.

The following code snippet shows the code that generates the client\-side representation of a DynamoDB `person` table that stores schemaless `EnhancedDocument` objects\.

```
DynamoDbTable<EnhancedDocument> documentDynamoDbTable = 
                enhancedClient.table("person",
                        TableSchema.documentSchemaBuilder()
                            // Specify the primary key attributes.
                            .addIndexPartitionKey(TableMetadata.primaryIndexName(),"id", AttributeValueType.S)
                            .addIndexSortKey(TableMetadata.primaryIndexName(), "lastName", AttributeValueType.S)
                            // Specify attribute converter providers. Minimally add the default one.
                            .attributeConverterProviders(AttributeConverterProvider.defaultProvider())
                            .build());
                                                         
// Call documentTable.createTable() if "person" does not exist in DynamoDB.
// createTable() should be called only one time.
```

The following shows the JSON representation of a `person` object that is used throughout this section\.

### JSON `person` object<a name="ddb-en-client-doc-api-steps-createschema-obj"></a>

```
{
  "id": 1,
  "firstName": "Richard",
  "lastName": "Roe",
  "age": 25,
  "addresses":
    {
      "home": {
        "zipCode": "00000",
        "city": "Any Town",
        "state": "FL",
        "street": "123 Any Street"
      },
      "work": {
        "zipCode": "00001",
        "city": "Anywhere",
        "state": "FL",
        "street": "100 Main Street"
      }
    },
  "hobbies": [
    "Hobby 1",
    "Hobby 2"
  ],
  "phoneNumbers": [
    {
      "type": "Home",
      "number": "555-0100"
    },
    {
      "type": "Work",
      "number": "555-0119"
    }
  ]
}
```