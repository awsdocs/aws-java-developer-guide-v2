--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Working with items in DynamoDB<a name="examples-dynamodb-items"></a>

In DynamoDB, an item is a collection of *attributes*, each of which has a *name* and a *value*\. An attribute value can be a scalar, set, or document type\. For more information, see [Naming Rules and Data Types](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.NamingRulesDataTypes.html) in the Amazon DynamoDB Developer Guide\.

## Retrieve \(get\) an item from a table<a name="dynamodb-get-item"></a>

Call the DynamoDbClient’s `getItem` method and pass it a [GetItemRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/dynamodb/model/GetItemRequest.html) object with the table name and primary key value of the item you want\. It returns a [GetItemResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/dynamodb/model/GetItemResponse.html) object with all of the attributes for that item\. You can specify one or more [projection expressions](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.ProjectionExpressions.html) in the `GetItemRequest` to retrieve specific attributes\.

You can use the returned `GetItemResponse` object’s `item()` method to retrieve a [Map](https://docs.oracle.com/javase/8/docs/api/index.html?java/util/Map.html) of key \(String\) and value \([AttributeValue](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/dynamodb/model/AttributeValue.html)\) pairs that are associated with the item\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
```

 **Code** 

```
    public static void getDynamoDBItem(DynamoDbClient ddb,String tableName,String key,String keyVal ) {

        HashMap<String,AttributeValue> keyToGet = new HashMap<String,AttributeValue>();

        keyToGet.put(key, AttributeValue.builder()
                .s(keyVal).build());

        GetItemRequest request = GetItemRequest.builder()
                .key(keyToGet)
                .tableName(tableName)
                .build();

        try {
            Map<String,AttributeValue> returnedItem = ddb.getItem(request).item();

            if (returnedItem != null) {
                Set<String> keys = returnedItem.keySet();
                System.out.println("Amazon DynamoDB table attributes: \n");

                for (String key1 : keys) {
                    System.out.format("%s: %s\n", key1, returnedItem.get(key1).toString());
                }
            } else {
                System.out.format("No item found with the key %s!\n", key);
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/GetItem.java) on GitHub\.

## Retrieve \(get\) an item from a table using the asynchronous client<a name="id1ddb"></a>

Invoke the `getItem` method of the DynamoDbAsyncClient and pass it a [GetItemRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/dynamodb/model/GetItemRequest.html) object with the table name and primary key value of the item you want\.

You can return a [Collection](https://docs.oracle.com/javase/8/docs/api/index.html?java/util/Collection.html) instance with all of the attributes for that item \(refer to the following example\)\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
```

 **Code** 

```
    public static void getItem(DynamoDbAsyncClient client, String tableName, String key,  String keyVal) {

        HashMap<String, AttributeValue> keyToGet =
                new HashMap<String, AttributeValue>();

        keyToGet.put(key, AttributeValue.builder()
                .s(keyVal).build());

        try {

            // Create a GetItemRequest instance
            GetItemRequest request = GetItemRequest.builder()
                    .key(keyToGet)
                    .tableName(tableName)
                    .build();

            // Invoke the DynamoDbAsyncClient object's getItem
            java.util.Collection<AttributeValue> returnedItem = client.getItem(request).join().item().values();

            // Convert Set to Map
            Map<String, AttributeValue> map = returnedItem.stream().collect(Collectors.toMap(AttributeValue::s, s->s));
            Set<String> keys = map.keySet();
            for (String sinKey : keys) {
                System.out.format("%s: %s\n", sinKey, map.get(sinKey).toString());
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodbasync/src/main/java/com/example/dynamodbasync/DynamoDBAsyncGetItem.java) on GitHub\.

## Add a new item to a table<a name="dynamodb-add-item"></a>

Create a [Map](https://docs.oracle.com/javase/8/docs/api/index.html?java/util/Map.html) of key\-value pairs that represent the item’s attributes\. These must include values for the table’s primary key fields\. If the item identified by the primary key already exists, its fields are *updated* by the request\.

**Note**  
If the named table doesn’t exist for your account and region, a [ResourceNotFoundException](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/dynamodb/model/ResourceNotFoundException.html) is thrown\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import java.util.HashMap;
```

 **Code** 

```
    public static void putItemInTable(DynamoDbClient ddb,
                                      String tableName,
                                      String key,
                                      String keyVal,
                                      String albumTitle,
                                      String albumTitleValue,
                                      String awards,
                                      String awardVal,
                                      String songTitle,
                                      String songTitleVal){

        HashMap<String,AttributeValue> itemValues = new HashMap<String,AttributeValue>();

        // Add all content to the table
        itemValues.put(key, AttributeValue.builder().s(keyVal).build());
        itemValues.put(songTitle, AttributeValue.builder().s(songTitleVal).build());
        itemValues.put(albumTitle, AttributeValue.builder().s(albumTitleValue).build());
        itemValues.put(awards, AttributeValue.builder().s(awardVal).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(itemValues)
                .build();

        try {
            ddb.putItem(request);
            System.out.println(tableName +" was successfully updated");

        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The Amazon DynamoDB table \"%s\" can't be found.\n", tableName);
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/PutItem.java) on GitHub\.

## Update an existing item in a table<a name="dynamodb-update-item"></a>

You can update an attribute for an item that already exists in a table by using the DynamoDbClient’s `updateItem` method, providing a table name, primary key value, and a map of fields to update\.

**Note**  
If the named table doesn’t exist for your account and region, or if the item identified by the primary key you passed in doesn’t exist, a [ResourceNotFoundException](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/dynamodb/model/ResourceNotFoundException.html) is thrown\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import java.util.HashMap;
```

 **Code** 

```
    public static void updateTableItem(DynamoDbClient ddb,
                                       String tableName,
                                       String key,
                                       String keyVal,
                                       String name,
                                       String updateVal){

        HashMap<String,AttributeValue> itemKey = new HashMap<String,AttributeValue>();

        itemKey.put(key, AttributeValue.builder().s(keyVal).build());

        HashMap<String,AttributeValueUpdate> updatedValues =
                new HashMap<String,AttributeValueUpdate>();

        // Update the column specified by name with updatedVal
        updatedValues.put(name, AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(updateVal).build())
                .action(AttributeAction.PUT)
                .build());

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(itemKey)
                .attributeUpdates(updatedValues)
                .build();

        try {
            ddb.updateItem(request);
        } catch (ResourceNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Done!");
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/UpdateItem.java) on GitHub\.

## Delete an existing item in a table<a name="dynamodb-delete-item"></a>

You can delete an item that exists in a table by using the DynamoDbClient’s `deleteItem` method and providing a table name as well as the primary key value\.

**Note**  
If the named table doesn’t exist for your account and region, or if the item identified by the primary key you passed in doesn’t exist, a [ResourceNotFoundException](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/dynamodb/model/ResourceNotFoundException.html) is thrown\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import java.util.HashMap;
```

 **Code** 

```
  public static void deleteDymamoDBItem(DynamoDbClient ddb, String tableName, String key, String keyVal) {

        HashMap<String,AttributeValue> keyToGet =
                new HashMap<String,AttributeValue>();

        keyToGet.put(key, AttributeValue.builder()
                .s(keyVal)
                .build());

        DeleteItemRequest deleteReq = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(keyToGet)
                .build();

        try {
            ddb.deleteItem(deleteReq);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/DeleteItem.java) on GitHub\.

## More information<a name="more-information"></a>
+  [Guidelines for Working with Items](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GuidelinesForItems.html) in the Amazon DynamoDB Developer Guide
+  [Working with Items in DynamoDB](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/WorkingWithItems.html) in the Amazon DynamoDB Developer Guide