# Explicitly include or exclude attributes<a name="ddb-en-client-adv-features-inex-attr"></a>

The DynamoDB Enhanced Client API offers annotations to exclude data class attributes from becoming attributes on a table\. With the API, you can also use an attribute name that's different from the data class attribute name\.

## Exclude attributes<a name="ddb-en-client-adv-features-inex-attr-ex"></a>

To ignore attributes that should not be mapped to a DynamoDB table, mark the attribute with the `@DynamoDbIgnore` annotation\.

```
private String internalKey;

@DynamoDbIgnore
public String getInternalKey() { return this.internalKey; }
public void setInternalKey(String internalKey) { return this.internalKey = internalKey;}
```

## Include attributes<a name="ddb-en-client-adv-features-inex-attr-in"></a>

To change the name of an attribute used in the DynamoDB table, mark it with the `@DynamoDbAttribute` annotation and supply a different name\.

```
private String internalKey;

@DynamoDbAttribute("renamedInternalKey")
public String getInternalKey() { return this.internalKey; }
public void setInternalKey(String internalKey) { return this.internalKey = internalKey;}
```