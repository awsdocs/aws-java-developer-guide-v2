# Control attribute conversion<a name="ddb-en-client-adv-features-conversion"></a>

By default, a table schema provides converters for all primitive and many common Java types through a default implementation of the `[AttributeConverterProvider](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/AttributeConverterProvider.html)` interface\. You can change the default behavior overall with a custom `AttributeConverterProvider` implementation\. You can also change the converter for a single attribute\.

You can find a list of the available converters in the [AttributeConverter](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/AttributeConverter.html) interface Java doc\.

## Provide custom attribute converter providers<a name="ddb-en-client-adv-features-conversion-prov"></a>

You can provide a single `AttributeConverterProvider` or a chain of ordered `AttributeConverterProvider`s through the `@DynamoDbBean` `(converterProviders = {…})` annotation\. Any custom `AttributeConverterProvider` must extend the `AttributeConverterProvider` interface\.

Note that if you supply your own chain of attribute converter providers, you will override the default converter provider, `DefaultAttributeConverterProvider`\. If you want to use the functionality of the `DefaultAttributeConverterProvider`, you must include it in the chain\. 

It's also possible to annotate the bean with an empty array `{}`\. This disables the use of any attribute converter providers including the default\. In this case all attributes that are to be mapped must have their own attribute converter\.

The following snipped shows a single converter provider\.

```
@DynamoDbBean(converterProviders = ConverterProvider1.class)
public class Customer {

}
```

The following snippet shows the use of a chain of converter providers\. Since the SDK's default is provided last, it has the lowest priority\.

```
@DynamoDbBean(converterProviders = {
   ConverterProvider1.class, 
   ConverterProvider2.class,
   DefaultAttributeConverterProvider.class})
public class Customer {

}
```

The static table schema builders have an `attributeConverterProviders()` method that works the same way\. This is shown in the following snippet\.

```
private static final StaticTableSchema<Customer> CUSTOMER_TABLE_SCHEMA =
  StaticTableSchema.builder(Customer.class)
    .newItemSupplier(Customer::new)
    .addAttribute(String.class, a -> a.name("name")
                                     a.getter(Customer::getName)
                                     a.setter(Customer::setName))
    .attributeConverterProviders(converterProvider1, converterProvider2)
    .build();
```

## Override the mapping of a single attribute<a name="ddb-en-client-adv-features-conversion-single"></a>

To override the way a single attribute is mapped, supply an `AttributeConverter` for the attribute\. This addition overrides any converters provided by the table schema's `AttributeConverterProviders`\. This adds a custom converter for only that attribute\. Other attributes, even those of the same type, won't use that converter unless it is explicitly specified for those other attributes\.

The `@DynamoDbConvertedBy` annotation is used to specify the custom `AttributeConverter` class as shown in the following snippet\.

```
@DynamoDbBean
public class Customer {
    private String name;

    @DynamoDbConvertedBy(CustomAttributeConverter.class)
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name;}
}
```

The builders for static schemas have an equivalent attribute builder `attributeConverter()` method\. This method takes an instance of an `AttributeConverter` as the following shows\.

```
private static final StaticTableSchema<Customer> CUSTOMER_TABLE_SCHEMA =
  StaticTableSchema.builder(Customer.class)
    .newItemSupplier(Customer::new)
    .addAttribute(String.class, a -> a.name("name")
                                     a.getter(Customer::getName)
                                     a.setter(Customer::setName)
                                     a.attributeConverter(customAttributeConverter))
    .build();
```

## Example<a name="ddb-en-client-adv-features-conversion-example"></a>

This example shows an `AttributeConverterProvider` implementation that provides an attribute converter for [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/net/HttpCookie.html](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/net/HttpCookie.html) objects\. 

The following `SimpleUser` class contains an attribute named `lastUsedCookie` that is an instance of `HttpCookie`\.

The parameter to the `@DynamoDbBean` annotations lists the two `AttributeConverterProvider` classes that provide converters\.

------
#### [ Class with annotations ]

```
    @DynamoDbBean(converterProviders = {CookieConverterProvider.class, DefaultAttributeConverterProvider.class})
    public static final class SimpleUser {
        private String name;
        private HttpCookie lastUsedCookie;

        @DynamoDbPartitionKey
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public HttpCookie getLastUsedCookie() {
            return lastUsedCookie;
        }

        public void setLastUsedCookie(HttpCookie lastUsedCookie) {
            this.lastUsedCookie = lastUsedCookie;
        }
```

------
#### [ Static table schema ]

```
    private static final TableSchema<SimpleUser> SIMPLE_USER_TABLE_SCHEMA =
            TableSchema.builder(SimpleUser.class)
                    .newItemSupplier(SimpleUser::new)
                    .attributeConverterProviders(CookieConverterProvider.create(), AttributeConverterProvider.defaultProvider())
                    .addAttribute(String.class, a -> a.name("name")
                            .setter(SimpleUser::setName)
                            .getter(SimpleUser::getName)
                            .tags(StaticAttributeTags.primaryPartitionKey()))
                    .addAttribute(HttpCookie.class, a -> a.name("lastUsedCookie")
                            .setter(SimpleUser::setLastUsedCookie)
                            .getter(SimpleUser::getLastUsedCookie))
                    .build();
```

------

The `CookieConverterProvider` in the following example provides an instance of an `HttpCookeConverter`\.

```
    public static final class CookieConverterProvider implements AttributeConverterProvider {
        private final Map<EnhancedType<?>, AttributeConverter<?>> converterCache = ImmutableMap.of(
                // 1. Add HttpCookieConverter to the internal cache.
                EnhancedType.of(HttpCookie.class), new HttpCookieConverter());

        public static CookieConverterProvider create() {
            return new CookieConverterProvider();
        }

        // The SDK calls this method to find out if the provider contains a AttributeConverter instance
        // for the EnhancedType<T> argument.
        @SuppressWarnings("unchecked")
        @Override
        public <T> AttributeConverter<T> converterFor(EnhancedType<T> enhancedType) {
            return (AttributeConverter<T>) converterCache.get(enhancedType);
        }
    }
```

### Conversion code<a name="ddb-en-client-adv-features-conversion-example-code"></a>

In the `transformFrom()` method of the following `HttpCookieConverter` class, the code receives an `HttpCookie` instance and transforms it into a DynamoDB map that will be stored as an attribute\.

The `transformTo()` method receives a DynamoDB map parameter then invokes the `HttpCookie` constructor that requires a name and a value\.

```
    public static final class HttpCookieConverter implements AttributeConverter<HttpCookie> {

        @Override
        public AttributeValue transformFrom(HttpCookie httpCookie) {

            return AttributeValue.fromM(
            Map.of ("cookieName", AttributeValue.fromS(httpCookie.getName()),
                    "cookieValue", AttributeValue.fromS(httpCookie.getValue()))
            );
        }

        @Override
        public HttpCookie transformTo(AttributeValue attributeValue) {
            Map<String, AttributeValue> map = attributeValue.m();
            return new HttpCookie(
                    map.get("cookieName").s(),
                    map.get("cookieValue").s());
        }

        @Override
        public EnhancedType<HttpCookie> type() {
            return EnhancedType.of(HttpCookie.class);
        }

        @Override
        public AttributeValueType attributeValueType() {
            return AttributeValueType.M;
        }
    }
```