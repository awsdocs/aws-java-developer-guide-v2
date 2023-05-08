# Change update behavior of attributes<a name="ddb-en-client-adv-features-upd-behavior"></a>

You can customize the update behavior of individual attributes when you perform an *update* operation\. Some examples of update operations in the DynamoDB Enhanced Client API are [updateItem\(\)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html#updateItem(T)) and [transactWriteItems\(\)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbEnhancedClient.html#transactWriteItems(java.util.function.Consumer))\.

For example, imagine that you want to store a *created on* timestamp on your record\. However, you want its value to be written only if there's no existing value for the attribute already in the database\. In this case, you use the `[WRITE\_IF\_NOT\_EXISTS](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/mapper/UpdateBehavior.html#WRITE_IF_NOT_EXISTS)` update behavior\.

The following example shows the annotation that adds the behavior to the `createdOn` attribute\.

```
@DynamoDbBean
public class Customer extends GenericRecord {
    private String id;
    private Instant createdOn;

    @DynamoDbPartitionKey
    public String getId() { return this.id; }
    public void setId(String id) { this.name = id; }

    @DynamoDbUpdateBehavior(UpdateBehavior.WRITE_IF_NOT_EXISTS)
    public Instant getCreatedOn() { return this.createdOn; }    
    public void setCreatedOn(Instant createdOn) { this.createdOn = createdOn; }
}
```

You can declare the same update behavior when you build a static table schema as shown in the following example after comment line 1\.

```
static final TableSchema<Customer> CUSTOMER_TABLE_SCHEMA =
     TableSchema.builder(Customer.class)
       .newItemSupplier(Customer::new)
       .addAttribute(String.class, a -> a.name("id")
                                         .getter(Customer::getId)
                                         .setter(Customer::setId)
                                         .tags(StaticAttributeTags.primaryPartitionKey()))
       .addAttribute(Instant.class, a -> a.name("createdOn")
                                          .getter(Customer::getCreatedOn)
                                          .setter(Customer::setCreatedOn)
                                          // 1. Add an UpdateBehavior.
                                          .tags(StaticAttributeTags.updateBehavior(UpdateBehavior.WRITE_IF_NOT_EXISTS)))
       .build();
```