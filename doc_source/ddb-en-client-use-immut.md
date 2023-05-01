# Work with immutable data classes<a name="ddb-en-client-use-immut"></a>

The mapping feature of the DynamoDB Enhanced Client API works with immutable data classes\. An immutable class has only getters and requires a builder class that the SDK uses to create instances of the class\. Instead of using the `@DynamoDbBean` annotation as shown in the [Customer class](ddb-en-client-gs-tableschema.md#ddb-en-client-gs-tableschema-anno-bean-cust), immutable classes use the `@DynamoDbImmutable` annotation, which takes a parameter indicating the builder class to use\.

The following class is an immutable version of `Customer`\.

```
package org.example.tests.model.immutable;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;

@DynamoDbImmutable(builder = CustomerImmutable.Builder.class)
public class CustomerImmutable {
    private final String id;
    private final String name;
    private final String email;
    private final Instant regDate;

    private CustomerImmutable(Builder b) {
        this.id = b.id;
        this.email = b.email;
        this.name = b.name;
        this.regDate = b.regDate;
    }

    // This method will be automatically discovered and used by the TableSchema.
    public static Builder builder() { return new Builder(); }

    @DynamoDbPartitionKey
    public String id() { return this.id; }

    @DynamoDbSortKey
    public String email() { return this.email; }

    @DynamoDbSecondaryPartitionKey(indexNames = "customers_by_name")
    public String name() { return this.name; }

    @DynamoDbSecondarySortKey(indexNames = {"customers_by_date", "customers_by_name"})
    public Instant regDate() { return this.regDate; }

    public static final class Builder {
        private String id;
        private String email;
        private String name;
        private Instant regDate;

        // The private Builder constructor is visible to the enclosing Customer class.
        private Builder() {}

        public Builder id(String accountId) { this.id = id; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder regDate(Instant regDate) { this.regDate = regDate; return this; }

        // This method will be automatically discovered and used by the TableSchema.
        public CustomerImmutable build() { return new CustomerImmutable(this); }
    }
}
```

You must meet the following requirements when you annotate a data class with `@DynamoDbImmutable`\.

1. Every method that is both not an override of `Object.class` and has not been annotated with `@DynamoDbIgnore` must be a getter for an attribute of the DynamoDB table\.

1. Every getter must have a corresponding case\-sensitive setter on the builder class\.

1. Only one of the following construction conditions must be met\.
   + The builder class must have a public default constructor\.
   + The data class must have a public static method named `builder()` that takes no parameters and returns an instance of the builder class\. This option is shown in the immutable `Customer` class\.

1.  The builder class must have a public method named `build()` that takes no parameters and returns an instance of the immutable class\. 

To create a `TableSchema` for your immutable class, use the `fromImmutableClass()` method on `TableSchema` as shown in the following snippet\.

```
static final TableSchema<Customer> customerTableSchema = TableSchema.fromImmutableClass(Customer.class);
```

## Use third\-party libraries, such as Lombok<a name="ddb-en-client-use-immut-lombok"></a>

Third\-party libraries, such as [Project Lombok](https://projectlombok.org/), help generate boilerplate code associated with immutable objects\. The DynamoDB Enhanced Client API works with these libraries as long as the data classes follow the conventions detailed in this section\. 

The following example shows the immutable `CustomerImmutable` class with Lombok annotations\. Note how Lombok's `onMethod` feature copies attribute\-based DynamoDB annotations, such as `@DynamoDbPartitionKey`, onto the generated code\.

```
@Value
@Builder
@DynamoDbImmutable(builder = Customer.CustomerBuilder.class)
public class Customer {
    @Getter(onMethod_=@DynamoDbPartitionKey)
    private String id;

    @Getter(onMethod_=@DynamoDbSortKey)
    private String email;

    @Getter(onMethod_=@DynamoDbSecondaryPartitionKey(indexNames = "customers_by_name"))
    private String name;

    @Getter(onMethod_=@DynamoDbSecondarySortKey(indexNames = {"customers_by_date", "customers_by_name"}))
    private Instant createdDate;
}
```