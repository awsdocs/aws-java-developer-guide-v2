# Build enhanced documents<a name="ddb-en-client-doc-api-steps-create-ed"></a>

An `[EnhancedDocument](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/document/EnhancedDocument.html)` represents a document\-type object that has complex structure with nested attributes\. An `EnhancedDocument` requires top\-level attributes that match the primary key attributes specified for the `DocumentTableSchema`\. The remaining content is arbitrary and can consist of top\-level attributes and also deeply nested attributes\.

You create an `EnhancedDocument` instance by using a builder that provides several ways to add elements\.

## Build from a JSON string<a name="ddb-en-client-doc-api-steps-create-ed-fromJson"></a>

With a JSON string, you can build an `EnhancedDocument` in one method call\. The following snippet creates an `EnhancedDocument` from a JSON string returned by the `jsonPerson()` helper method\. The `jsonPerson()` method returns the JSON string version of the [person object](ddb-en-client-doc-api-steps.md#ddb-en-client-doc-api-steps-createschema-obj) shown previously\.

```
EnhancedDocument document = 
        EnhancedDocument.builder()
                        .json( jsonPerson() )
                        .build());
```

## Build from individual elements<a name="ddb-en-client-doc-api-steps-create-ed-fromparts"></a>

Alternatively, you can build an `EnhancedDocument` instance from individual components using type\-safe methods of the builder\.

The following example builds a `person` enhanced document similar to the enhanced document that is built from the JSON string in the previous example\.

```
        /* Define the shape of an address map whose JSON representation looks like the following.
           Use 'addressMapEnhancedType' in the following EnhancedDocument.builder() to simplify the code.
           "home": {
             "zipCode": "00000",
             "city": "Any Town",
             "state": "FL",
             "street": "123 Any Street"
           }*/
        EnhancedType<Map<String, String>> addressMapEnhancedType =
                EnhancedType.mapOf(EnhancedType.of(String.class), EnhancedType.of(String.class));


        //  Use the builder's typesafe methods to add elements to the enhanced document.
        EnhancedDocument personDocument = EnhancedDocument.builder()
                .putNumber("id", 50)
                .putString("firstName", "Shirley")
                .putString("lastName", "Rodriguez")
                .putNumber("age", 53)
                .putNull("nullAttribute")
                .putJson("phoneNumbers", phoneNumbersJSONString())
                /* Add the map of addresses whose JSON representation looks like the following.
                        {
                          "home": {
                            "zipCode": "00000",
                            "city": "Any Town",
                            "state": "FL",
                            "street": "123 Any Street"
                          }
                        } */
                .putMap("addresses", getAddresses(), EnhancedType.of(String.class), addressMapEnhancedType)
                .putList("hobbies", List.of("Theater", "Golf"), EnhancedType.of(String.class))
                .build();
```

### Helper methods<a name="ddb-en-client-doc-api-steps-use-fromparts-helpers"></a>

```
    private static String phoneNumbersJSONString() {
        return "  [" +
                "    {" +
                "      \"type\": \"Home\"," +
                "      \"number\": \"555-0140\"" +
                "    }," +
                "    {" +
                "      \"type\": \"Work\"," +
                "      \"number\": \"555-0155\"" +
                "    }" +
                "  ]";
    }

    private static Map<String, Map<String, String>> getAddresses() {
        return Map.of(
                "home", Map.of(
                        "zipCode", "00002",
                        "city", "Any Town",
                        "state", "ME",
                        "street", "123 Any Street"));

    }
```