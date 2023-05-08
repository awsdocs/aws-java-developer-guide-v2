# Extensions<a name="ddb-en-client-extensions"></a>

The DynamoDB Enhanced Client API supports plugin extensions that provide functionality beyond mapping operations\. Extensions have two hook methods, `beforeWrite()` and `afterRead()`\. `beforeWrite()` modifies a write operation before it happens, and the `afterRead()` method modifies the results of a read operation after it happens\. Because some operations \(such as item updates\) perform both a write and then a read, both hook methods are called\. 

Extensions are loaded in the order that they are specified in the enhanced client builder\. The load order can be important because one extension can act on values that have been transformed by a previous extension\. 

The enhanced client API comes with a set of plugin extensions that are located in the `[extensions](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/extensions/package-summary.html)` package\. By default, the enhanced client loads the `[VersionedRecordExtension](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/extensions/VersionedRecordExtension.html)` and the `[AtomicCounterExtension](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/extensions/AtomicCounterExtension.html)`\. You can override the default behavior with the enhance client builder and load any extension\. You can also specify none if you don't want the default extensions\. 

If you load your own extensions, the enhanced client doesn't load any default extensions\. If you want the behavior provided by either default extension, you need to explicitly add it to the list of extensions\. 

In the following example, a custom extension named `verifyChecksumExtension` is loaded after the `VersionedRecordExtension`, which is usually loaded by default by itself\. The `AtomicCounterExtension` is not loaded in this example\.

```
DynamoDbEnhancedClientExtension versionedRecordExtension = VersionedRecordExtension.builder().build();

DynamoDbEnhancedClient enhancedClient = 
    DynamoDbEnhancedClient.builder()
                          .dynamoDbClient(dynamoDbClient)
                          .extensions(versionedRecordExtension, verifyChecksumExtension)
                          .build();
```

## VersionedRecordExtension<a name="ddb-en-client-extensions-VRE"></a>

The `VersionedRecordExtension` is loaded by default and will increment and track an item version number as items are written to the database\. A condition will be added to every write that causes the write to fail if the version number of the actual persisted item doesn't match the value that the application last read\. This behavior effectively provides optimistic locking for item updates\. If another process updates an item between the time the first process has read the item and is writing an update to it, the write will fail\.

To specify which attribute to use to track the item version number, tag a numeric attribute in the table schema\. 

The following snippet specifies that the `version` attribute should hold the item version number\.

```
    @DynamoDbVersionAttribute
    public Integer getVersion() {...};
    public void setVersion(Integer version) {...};
```

The equivalent static table schema approach is shown in the following snippet\.

```
    .addAttribute(Integer.class, a -> a.name("version")
                                       .getter(Customer::getVersion)
                                       .setter(Customer::setVersion)
                                        // Apply the 'version' tag to the attribute.
                                       .tags(VersionedRecordExtension.AttributeTags.versionAttribute())
```

## AtomicCounterExtension<a name="ddb-en-client-extensions-ACE"></a>

The `AtomicCounterExtension` is loaded by default and increments a tagged numerical attribute each time a record is written to the database\. Start and increment values can be specified\. If no values are specified, the start value is set to 0 and the attribute's value increments by 1\.

To specify which attribute is a counter, tag an attribute of type `Long` in the table schema\.

The following snippet shows the use of the default start and increment values for the `counter` attribute\.

```
    @DynamoDbAtomicCounter
    public Long getCounter() {...};
    public void setCounter(Long counter) {...};
```

The static table schema approach is shown in the following snippet\. The atomic counter extension uses a start value of 10 and increments the value by 5 each time the record is written\.

```
    .addAttribute(Integer.class, a -> a.name("counter")
                                       .getter(Customer::getCounter)
                                       .setter(Customer::setCounter)
                                        // Apply the 'atomicCounter' tag to the attribute with start and increment values.
                                       .tags(StaticAttributeTags.atomicCounter(10L, 5L))
```

## AutoGeneratedTimestampRecordExtension<a name="ddb-en-client-extensions-AGTE-"></a>

The `AutoGeneratedTimestampRecordExtension` automatically updates tagged attributes of type `[Instant](https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html)` with a current timestamp every time the item is successfully written to the database\.

This extension is not loaded by default\. Therefore, you need to specify it as a custom extension when you build the enhanced client as shown in the first example in this topic\.

To specify which attribute to update with the current timestamp, tag the `Instant` attribute in the table schema\.

The `lastUpdate` attribute is the target of the extensions behavior in the following snippet\. Note the requirement that the attribute must be an `Instant` type\.

```
    @DynamoDbAutoGeneratedTimestampAttribute
    public Instant getLastUpdate() {...}
    public void setLastUpdate(Instant lastUpdate) {...}
```

The equivalent static table schema approach is shown in the following snippet\.

```
     .addAttribute(Instant.class, a -> a.name("lastUpdate")
                                        .getter(Customer::getLastUpdate)
                                        .setter(Customer::setLastUpdate)
                                        // Applying the 'autoGeneratedTimestamp' tag to the attribute.
                                        .tags(AutoGeneratedTimestampRecordExtension.AttributeTags.autoGeneratedTimestampAttribute())
```

## Custom extensions<a name="ddb-en-client-extensions-custom"></a>

The following custom extension class shows a `beforeWrite()` method that uses an update expression\. After comment line 2, we create a `SetAction` to set the `registrationDate` attribute if the item in the database doesn't already have a `registrationDate` attribute\. Whenever a `Customer` object is updated, the extension makes sure that a `registrationDate` is set\.

```
public final class CustomExtension implements DynamoDbEnhancedClientExtension {

    // 1. In a custom extension, use an UpdateExpression to define what action to take before
    //    an item is updated.
    @Override
    public WriteModification beforeWrite(DynamoDbExtensionContext.BeforeWrite context) {
        if ( context.operationContext().tableName().equals("Customer")
                && context.operationName().equals(OperationName.UPDATE_ITEM)) {
            return WriteModification.builder()
                    .updateExpression(createUpdateExpression())
                    .build();
        }
        return null;
    }

    private static UpdateExpression createUpdateExpression() {

        // 2. Use a SetAction, a subclass of UpdateAction, to provide the values in the update.
        SetAction setAction =
                SetAction.builder()
                        .path("registrationDate")
                        .value("if_not_exists(registrationDate, :regValue)")
                        .putExpressionValue(":regValue", AttributeValue.fromS(Instant.now().toString()))
                        .build();
        // 3. Build the UpdateExpression with one or more UpdateAction.
        return UpdateExpression.builder()
                .addAction(setAction)
                .build();
    }
}
```