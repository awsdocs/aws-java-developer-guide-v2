# Perform CRUD operations<a name="ddb-en-client-doc-api-steps-use"></a>

After you define an `EnhancedDocument` instance, you can save it to a DynamoDB table as shown in the following code snippet\.

```
documentDynamoDbTable.putItem(personDocument);
```

After you read an enhanced document instance from DynamoDB, you can extract the individual attribute values using getters as shown in the following code snippet\. You can alternatively extract the complete content to a JSON string as shown in the last part of the example code\.

```
        // Read the item.
        EnhancedDocument personDocFromDb = documentDynamoDbTable.getItem(Key.builder().partitionValue(50).build());

        // Access top-level attributes.
        logger.info("Name: {} {}", personDocFromDb.getString("firstName"), personDocFromDb.getString("lastName"));
        // Name: Shirley Rodriguez

        // Typesafe access of a deeply nested attribute. The addressMapEnhancedType shown previously defines the shape of an addresses map.
        Map<String, Map<String, String>> addresses = personDocFromDb.getMap("addresses", EnhancedType.of(String.class), addressMapEnhancedType);
        addresses.keySet().forEach(k -> logger.info(addresses.get(k).toString()));
        // {zipCode=00002, city=Any Town, street=123 Any Street, state=ME}

        // Alternatively, work with AttributeValue types checking along the way for deeply nested attributes.
        Map<String, AttributeValue> addressesMap = personDocFromDb.getMapOfUnknownType("addresses");
        addressesMap.keySet().forEach((String k) -> {
            logger.info("Looking at data for [{}] address", k);
            // Looking at data for [home] address
            AttributeValue value = addressesMap.get(k);
            AttributeValue cityValue = value.m().get("city");
            if (cityValue != null) {
                logger.info(cityValue.s());
                // Any Town
            }
        });

        List<AttributeValue> phoneNumbers = personDocFromDb.getListOfUnknownType("phoneNumbers");
        phoneNumbers.forEach((AttributeValue av) -> {
            if (av.hasM()) {
                AttributeValue type = av.m().get("type");
                if (type.s() != null) {
                    logger.info("Type of phone: {}", type.s());
                    // Type of phone: Home
                    // Type of phone: Work
                }
            }
        });

        String jsonPerson = personDocFromDb.toJson();
        logger.info(jsonPerson);
        // {"firstName":"Shirley","lastName":"Rodriguez","addresses":{"home":{"zipCode":"00002","city":"Any Town","street":"123 Any Street","state":"ME"}},"hobbies":["Theater","Golf"],
        //     "id":50,"nullAttribute":null,"age":53,"phoneNumbers":[{"number":"555-0140","type":"Home"},{"number":"555-0155","type":"Work"}]}
```

`EnhancedDocument` instances can be used with any method of `[DynamoDbTable](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html)` or [DynamoDbEnhancedClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbEnhancedClient.html) in place of mapped data classes\.