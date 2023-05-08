# Query a table<a name="ddb-en-client-use-multirecord-query"></a>

The [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html#query(java.util.function.Consumer)](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbTable.html#query(java.util.function.Consumer)) method of the `DynamoDbTable` class finds items based on primary key values\. The `@DynamoDbPartitionKey` annotation and the optional `@DynamoDbSortKey` annotation are used to define the primary key on your data class\.

The `query()` method requires a partition key value that finds items that match the supplied value\. If your table also defines a sort key, you can add a value for it to your query as an additional comparison condition to fine tune the results\. 

Except for processing the results, the synchronous and asynchronous versions of `query()` work the same\. As with the `scan` API, the `query` API returns a `PageIterable` for a synchronous call and a `PagePublisher` for asynchronous call\. We discussed the use of `PageIterable` and `PagePublisher` previously in the scan section\.

## `Query` method examples<a name="ddb-en-client-use-multirecord-query-example"></a>

The `query()` method code example that follows uses the `MovieActor` class\. The data class defines a composite primary key that is made up of the **`movie`** attribute for the partition key and the **`actor`** attribute for the sort key\. 

The class also signals that it uses a global secondary index named **`acting_award_year`**\. The index's composite primary key is composed of the **`actingaward`** attribute for the partition key and the **`actingyear`** for the sort key\. Later in this topic, when we show how to create and use indexes, we'll refer to the** `acting_award_year`** index\.

### `MovieActor` class<a name="ddb-en-client-use-movieactor-class"></a>

```
package org.example.tests.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.Objects;

@DynamoDbBean
public class MovieActor implements Comparable<MovieActor> {

    private String movieName;
    private String actorName;
    private String actingAward;
    private Integer actingYear;
    private String actingSchoolName;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("movie")
    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("actor")
    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "acting_award_year")
    @DynamoDbAttribute("actingaward")
    public String getActingAward() {
        return actingAward;
    }

    public void setActingAward(String actingAward) {
        this.actingAward = actingAward;
    }

    @DynamoDbSecondarySortKey(indexNames = {"acting_award_year", "movie_year"})
    @DynamoDbAttribute("actingyear")
    public Integer getActingYear() {
        return actingYear;
    }

    public void setActingYear(Integer actingYear) {
        this.actingYear = actingYear;
    }

    @DynamoDbAttribute("actingschoolname")
    public String getActingSchoolName() {
        return actingSchoolName;
    }

    public void setActingSchoolName(String actingSchoolName) {
        this.actingSchoolName = actingSchoolName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MovieActor{");
        sb.append("movieName='").append(movieName).append('\'');
        sb.append(", actorName='").append(actorName).append('\'');
        sb.append(", actingAward='").append(actingAward).append('\'');
        sb.append(", actingYear=").append(actingYear);
        sb.append(", actingSchoolName='").append(actingSchoolName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieActor that = (MovieActor) o;
        return Objects.equals(movieName, that.movieName) && Objects.equals(actorName, that.actorName) && Objects.equals(actingAward, that.actingAward) && Objects.equals(actingYear, that.actingYear) && Objects.equals(actingSchoolName, that.actingSchoolName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieName, actorName, actingAward, actingYear, actingSchoolName);
    }

    @Override
    public int compareTo(MovieActor o) {
        if (this.movieName.compareTo(o.movieName) != 0){
            return this.movieName.compareTo(o.movieName);
        } else {
            return this.actorName.compareTo(o.actorName);
        }
    }
}
```

The code examples that follow query against the following items\.

### Items in the `MovieActor` table<a name="ddb-en-client-use-movieactor-items"></a>

```
MovieActor{movieName='movie01', actorName='actor0', actingAward='actingaward0', actingYear=2001, actingSchoolName='null'}
MovieActor{movieName='movie01', actorName='actor1', actingAward='actingaward1', actingYear=2001, actingSchoolName='actingschool1'}
MovieActor{movieName='movie01', actorName='actor2', actingAward='actingaward2', actingYear=2001, actingSchoolName='actingschool2'}
MovieActor{movieName='movie01', actorName='actor3', actingAward='actingaward3', actingYear=2001, actingSchoolName='null'}
MovieActor{movieName='movie01', actorName='actor4', actingAward='actingaward4', actingYear=2001, actingSchoolName='actingschool4'}
MovieActor{movieName='movie02', actorName='actor0', actingAward='actingaward0', actingYear=2002, actingSchoolName='null'}
MovieActor{movieName='movie02', actorName='actor1', actingAward='actingaward1', actingYear=2002, actingSchoolName='actingschool1'}
MovieActor{movieName='movie02', actorName='actor2', actingAward='actingaward2', actingYear=2002, actingSchoolName='actingschool2'}
MovieActor{movieName='movie02', actorName='actor3', actingAward='actingaward3', actingYear=2002, actingSchoolName='null'}
MovieActor{movieName='movie02', actorName='actor4', actingAward='actingaward4', actingYear=2002, actingSchoolName='actingschool4'}
MovieActor{movieName='movie03', actorName='actor0', actingAward='actingaward0', actingYear=2003, actingSchoolName='null'}
MovieActor{movieName='movie03', actorName='actor1', actingAward='actingaward1', actingYear=2003, actingSchoolName='actingschool1'}
MovieActor{movieName='movie03', actorName='actor2', actingAward='actingaward2', actingYear=2003, actingSchoolName='actingschool2'}
MovieActor{movieName='movie03', actorName='actor3', actingAward='actingaward3', actingYear=2003, actingSchoolName='null'}
MovieActor{movieName='movie03', actorName='actor4', actingAward='actingaward4', actingYear=2003, actingSchoolName='actingschool4'}
```

The following code defines two [QueryConditional](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/QueryConditional.html) instances\. `QueryConditionals` work with key values—either the partition key alone or in combination with the sort key—and correspond to the [key conditional expressions](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Query.html#Query.KeyConditionExpressions) of the DynamoDB service API\. After comment line 1, the example defines the `keyEqual` instance that matches items with a partition value of **`movie01`**\. 

This example also defines a filter expression that filters off any item that has no **`actingschoolname`** on after comment line 2\.

After comment line 3, the example shows the [QueryEnhancedRequest](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/model/QueryEnhancedRequest.html) instance that the code passes to the `DynamoDbTable.query()` method\. This object combines the key condition and filter that the SDK uses to generate the request to the DynamoDB service\.

```
    public static void query(DynamoDbTable movieActorTable) {

        // 1. Define a QueryConditional instance to return items matching a partition value.
        QueryConditional keyEqual = QueryConditional.keyEqualTo(b -> b.partitionValue("movie01"));
        // 1a. Define a QueryConditional that adds a sort key criteria to the partition value criteria.
        QueryConditional sortGreaterThanOrEqualTo = QueryConditional.sortGreaterThanOrEqualTo(b -> b.partitionValue("movie01").sortValue("actor2"));
        // 2. Define a filter expression that filters out items whose attribute value is null.
        final Expression filterOutNoActingschoolname = Expression.builder().expression("attribute_exists(actingschoolname)").build();

        // 3. Build the query request.
        QueryEnhancedRequest tableQuery = QueryEnhancedRequest.builder()
                .queryConditional(keyEqual)
                .filterExpression(filterOutNoActingschoolname)
                .build();
        // 4. Perform the query.
        PageIterable<MovieActor> pagedResults = movieActorTable.query(tableQuery);
        logger.info("page count: {}", pagedResults.stream().count()); // Log  number of pages.

        pagedResults.items().stream()
                .sorted()
                .forEach(
                        item -> logger.info(item.toString()) // Log the sorted list of items.
                );
```

The following is the output from running the method\. The output displays items with a `movieName` value of **movie01** and displays no items with `actingSchoolName` equal to **`null`**\.

```
2023-03-05 13:11:05 [main] INFO  org.example.tests.QueryDemo:46 - page count: 1
2023-03-05 13:11:05 [main] INFO  org.example.tests.QueryDemo:51 - MovieActor{movieName='movie01', actorName='actor1', actingAward='actingaward1', actingYear=2001, actingSchoolName='actingschool1'}
2023-03-05 13:11:05 [main] INFO  org.example.tests.QueryDemo:51 - MovieActor{movieName='movie01', actorName='actor2', actingAward='actingaward2', actingYear=2001, actingSchoolName='actingschool2'}
2023-03-05 13:11:05 [main] INFO  org.example.tests.QueryDemo:51 - MovieActor{movieName='movie01', actorName='actor4', actingAward='actingaward4', actingYear=2001, actingSchoolName='actingschool4'}
```

In the following query request variation shown previously after comment line 3, the code replaces the `keyEqual` `QueryConditional` with the `sortGreaterThanOrEqualTo` `QueryConditional` that was defined after comment line 1a\. The following code also removes the filter expression\.

```
        QueryEnhancedRequest tableQuery = QueryEnhancedRequest.builder()
                .queryConditional(sortGreaterThanOrEqualTo)
```

Because this table has a composite primary key, all `QueryConditional` instances require a partition key value\. `QueryConditional` methods that begin with `sort...` indicate that a *sort* key is required\. The results are not sorted\.

The following output displays the results from the query\. The query returns items that have a `movieName` value equal to **movie01** and only items that have an `actorName` value that is greater than or equal to **actor2**\. Because the filter was removed, the query returns items that have no value for the `actingSchoolName` attribute\.

```
2023-03-05 13:15:00 [main] INFO  org.example.tests.QueryDemo:46 - page count: 1
2023-03-05 13:15:00 [main] INFO  org.example.tests.QueryDemo:51 - MovieActor{movieName='movie01', actorName='actor2', actingAward='actingaward2', actingYear=2001, actingSchoolName='actingschool2'}
2023-03-05 13:15:00 [main] INFO  org.example.tests.QueryDemo:51 - MovieActor{movieName='movie01', actorName='actor3', actingAward='actingaward3', actingYear=2001, actingSchoolName='null'}
2023-03-05 13:15:00 [main] INFO  org.example.tests.QueryDemo:51 - MovieActor{movieName='movie01', actorName='actor4', actingAward='actingaward4', actingYear=2001, actingSchoolName='actingschool4'}
```