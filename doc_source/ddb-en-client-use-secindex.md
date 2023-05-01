# Secondary indices<a name="ddb-en-client-use-secindex"></a>

Secondary indices improve data access by defining alternative keys that you use in query and scan operations\. Global secondary indices \(GSI\) have a partition key and a sort key that can be different from those on the base table\. In contrast, local secondary indices \(LSI\) use the partition key of the primary index\.

Two steps are involved when you work with secondary indexes using the enhanced client\. First, annotate the data class\. Second, when you create the table, configure the index using names that match those used in the annotation\. The following examples demonstrate both steps\.

## Annotate mapping class with secondary index annotations<a name="ddb-en-client-use-secindex-annomodel"></a>

Attributes that participate in secondary indices require either the `@DynamoDbSecondaryPartitionKey` or `@DynamoDbSecondarySortKey` annotation\.

The following class shows annotations for two indices\. The GSI named *SubjectLastPostedDateIndex* uses the `Subject` attribute for the partition key and the `LastPostedDateTime` for the sort key\. The LSI named *ForumLastPostedDateIndex* uses the `ForumName` as its partition key and `LastPostedDateTime` as its sort key\.

Note that the `Subject` attribute serves a dual role\. It is the primary key's sort key and the partition key of the GSI named *SubjectLastPostedDateIndex*\.

### `MessageThread` class used in the [DynamoDB Developer Guide](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/SampleData.CreateTables.html)<a name="ddb-en-client-use-secindex-class"></a>

The `MessageThread` class is suitable to use as a data class for the [example `Thread` table in the DynamoDB Developer Guide](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/SampleData.CreateTables.html#SampleData.CreateTablesSteps.Thread)\.

#### Imports<a name="ddb-en-client-use-secindex-classimports"></a>

```
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;
```

```
@DynamoDbBean
public class MessageThread {
    private String ForumName;
    private String Subject;
    private String Message;
    private String LastPostedBy;
    private String LastPostedDateTime;
    private Integer Views;
    private Integer Replies;
    private Integer Answered;
    private List<String> Tags;

    @DynamoDbPartitionKey
    public String getForumName() {
        return ForumName;
    }

    public void setForumName(String forumName) {
        ForumName = forumName;
    }

    // Sort key for primary index and partition key for GSI "SubjectLastPostedDateIndex".
    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = "SubjectLastPostedDateIndex")
    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    // Sort key for GSI "SubjectLastPostedDateIndex" and sort key for LSI "ForumLastPostedDateIndex".
    @DynamoDbSecondarySortKey(indexNames = {"SubjectLastPostedDateIndex", "ForumLastPostedDateIndex"})
    public String getLastPostedDateTime() {
        return LastPostedDateTime;
    }

    public void setLastPostedDateTime(String lastPostedDateTime) {
        LastPostedDateTime = lastPostedDateTime;
    }
    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getLastPostedBy() {
        return LastPostedBy;
    }

    public void setLastPostedBy(String lastPostedBy) {
        LastPostedBy = lastPostedBy;
    }

    public Integer getViews() {
        return Views;
    }

    public void setViews(Integer views) {
        Views = views;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "ForumRepliesIndex")
    public Integer getReplies() {
        return Replies;
    }

    public void setReplies(Integer replies) {
        Replies = replies;
    }

    public Integer getAnswered() {
        return Answered;
    }

    public void setAnswered(Integer answered) {
        Answered = answered;
    }

    public List<String> getTags() {
        return Tags;
    }

    public void setTags(List<String> tags) {
        Tags = tags;
    }

    public MessageThread() {
        this.Answered = 0;
        this.LastPostedBy = "";
        this.ForumName = "";
        this.Message = "";
        this.LastPostedDateTime = "";
        this.Replies = 0;
        this.Views = 0;
        this.Subject = "";
    }

    @Override
    public String toString() {
        return "MessageThread{" +
                "ForumName='" + ForumName + '\'' +
                ", Subject='" + Subject + '\'' +
                ", Message='" + Message + '\'' +
                ", LastPostedBy='" + LastPostedBy + '\'' +
                ", LastPostedDateTime='" + LastPostedDateTime + '\'' +
                ", Views=" + Views +
                ", Replies=" + Replies +
                ", Answered=" + Answered +
                ", Tags=" + Tags +
                '}';
    }
}
```

## Create the index<a name="ddb-en-client-use-secindex-confindex"></a>

Unlike the primary key, which is generated from the `@DynamoDbPartitionKey` and `@DynamoDbSortKey` annotations, the enhanced client does not automatically generate the secondary indexes\. These indexes are configured and created when the table is created\.

The following example builds the two indexes for the `Thread` table\. The [builder](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/CreateTableEnhancedRequest.Builder.html) parameter has methods to configure both types of indexes as shown after comment lines 1 and 2\. You use the index builder's `indexName()` method to associate the index names specified in the data class' annotations with the intended type of index\.

This code configures all of the tables attributes to end up in both indexes after comment lines 3 and 4\. More information about [attribute projections](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/LSI.html#LSI.Projections) is available in the DynamoDB Developer Guide\.

```
    public static void createMessageThreadTable(DynamoDbTable<MessageThread> messageThreadDynamoDbTable, DynamoDbClient dynamoDbClient) {
        messageThreadDynamoDbTable.createTable(b -> b
                // 1. Generate the GSI
                .globalSecondaryIndices(gsi -> gsi.indexName("SubjectLastPostedDateIndex")
                        // 3. Populate the GSI with all attributes
                        .projection(p -> p
                                .projectionType(ProjectionType.ALL))
                )
                // 2. Generate the LSI
                .localSecondaryIndices(lsi -> lsi.indexName("ForumLastPostedDateIndex")
                        // 4. Populate the LSI with all attributes.
                        .projection(p -> p
                                .projectionType(ProjectionType.ALL))
                )
        );
```

## Query using an index<a name="ddb-en-client-use-secindex-query"></a>

The following example queries the local secondary index *ForumLastPostedDateIndex*\.

Following comment line 2, we create a [QueryConditional](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/QueryConditional.html) object that is required when calling the [DynamoDbIndex\.query\(\)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbIndex.html#query(java.util.function.Consumer)) method\. 

We get a reference to the index we want to query after comment line 3 by passing in the name of the index\. Following comment line 4, we call the `query()` method on the index passing in the `QueryConditional` object\. 

We also configure the query to return three attribute values as shown after comment line 5\. If `attributesToProject()` is not called, the query returns all attribute values\. Notice that the attribute names specified begin with lowercase letters\. These attribute names match those used in the table not necessarily the attribute names of the data class\.

Following comment line 6, we iterate through the results and log each item returned by the query and also store it in list to return to the caller\.

```
public static List<MessageThread> queryUsingSecondaryIndices(DynamoDbEnhancedClient enhancedClient,
                                                                 String lastPostedDate,
                                                                 DynamoDbTable<MessageThread> threadTable) {
        // 1. Log the parameter value.
        logger.info("lastPostedDate value: {}", lastPostedDate);

        // 2. Create a QueryConditional whose sort key value must be greater than or equal to the parameter value.
        QueryConditional queryConditional = QueryConditional.sortGreaterThanOrEqualTo(qc ->
                qc.partitionValue("Forum02").sortValue(lastPostedDate));

        // 3. Specify the index name to query the DynamoDbIndex instance.
        final DynamoDbIndex<MessageThread> forumLastPostedDateIndex = threadTable.index("ForumLastPostedDateIndex");

        // 4. Perform the query using the QueryConditional object.
        final SdkIterable<Page<MessageThread>> pagedResult = forumLastPostedDateIndex.query(q -> q
                .queryConditional(queryConditional)
                // 5. Request three attribute in the results.
                .attributesToProject("forumName", "subject", "lastPostedDateTime"));

        List<MessageThread> collectedItems = new ArrayList<>();
        // 6. Iterate through pages response and sort the items.
        pagedResult.stream().forEach(page -> page.items().stream()
                        .sorted(Comparator.comparing(MessageThread::getLastPostedDateTime))
                        .forEach(mt -> {
                            // 7. Log the returned items and add the collection to return to the caller.
                            logger.info(mt.toString());
                            collectedItems.add(mt);
                        }));
        return collectedItems;
    }
```

The following items exist in the database before the query is run\.

```
MessageThread{ForumName='Forum01', Subject='Subject01', Message='Message01', LastPostedBy='', LastPostedDateTime='2023.03.28', Views=0, Replies=0, Answered=0, Tags=null}
MessageThread{ForumName='Forum02', Subject='Subject02', Message='Message02', LastPostedBy='', LastPostedDateTime='2023.03.29', Views=0, Replies=0, Answered=0, Tags=null}
MessageThread{ForumName='Forum02', Subject='Subject04', Message='Message04', LastPostedBy='', LastPostedDateTime='2023.03.31', Views=0, Replies=0, Answered=0, Tags=null}
MessageThread{ForumName='Forum02', Subject='Subject08', Message='Message08', LastPostedBy='', LastPostedDateTime='2023.04.04', Views=0, Replies=0, Answered=0, Tags=null}
MessageThread{ForumName='Forum02', Subject='Subject10', Message='Message10', LastPostedBy='', LastPostedDateTime='2023.04.06', Views=0, Replies=0, Answered=0, Tags=null}
MessageThread{ForumName='Forum03', Subject='Subject03', Message='Message03', LastPostedBy='', LastPostedDateTime='2023.03.30', Views=0, Replies=0, Answered=0, Tags=null}
MessageThread{ForumName='Forum03', Subject='Subject06', Message='Message06', LastPostedBy='', LastPostedDateTime='2023.04.02', Views=0, Replies=0, Answered=0, Tags=null}
MessageThread{ForumName='Forum03', Subject='Subject09', Message='Message09', LastPostedBy='', LastPostedDateTime='2023.04.05', Views=0, Replies=0, Answered=0, Tags=null}
MessageThread{ForumName='Forum05', Subject='Subject05', Message='Message05', LastPostedBy='', LastPostedDateTime='2023.04.01', Views=0, Replies=0, Answered=0, Tags=null}
MessageThread{ForumName='Forum07', Subject='Subject07', Message='Message07', LastPostedBy='', LastPostedDateTime='2023.04.03', Views=0, Replies=0, Answered=0, Tags=null}
```

The logging statements at lines 1 and 6 result in the following console output\.

```
lastPostedDate value: 2023.03.31
MessageThread{ForumName='Forum02', Subject='Subject04', Message='', LastPostedBy='', LastPostedDateTime='2023.03.31', Views=0, Replies=0, Answered=0, Tags=null}
MessageThread{ForumName='Forum02', Subject='Subject08', Message='', LastPostedBy='', LastPostedDateTime='2023.04.04', Views=0, Replies=0, Answered=0, Tags=null}
MessageThread{ForumName='Forum02', Subject='Subject10', Message='', LastPostedBy='', LastPostedDateTime='2023.04.06', Views=0, Replies=0, Answered=0, Tags=null}
```

The query returned items with a `forumName` value of *Forum02* and a `lastPostedDateTime` value greater than or equal to *2023\.03\.31*\. The results show `message` values with an empty string although the `message` attributes have values in the index\. This is because the message attribute was not projected at after comment line 5\. 