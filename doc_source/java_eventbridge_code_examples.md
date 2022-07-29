--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# EventBridge examples using SDK for Java 2\.x<a name="java_eventbridge_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with EventBridge\.

*Actions* are code excerpts that show you how to call individual EventBridge functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple EventBridge functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w591aac15c14b9c33c13)

## Actions<a name="w591aac15c14b9c33c13"></a>

### Create a scheduled rule<a name="eventbridge_PutRule_java_topic"></a>

The following code example shows how to create an Amazon EventBridge scheduled rule\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
  

```
    public static void createEBRule(EventBridgeClient eventBrClient, String ruleName) {

        try {
            PutRuleRequest ruleRequest = PutRuleRequest.builder()
                .name(ruleName)
                .eventBusName("default")
                .eventPattern("{\"source\":[\"aws.s3\"],\"detail-type\":[\"AWS API Call via CloudTrail\"],\"detail\":{\"eventSource\":[\"s3.amazonaws.com\"],\"eventName\":[\"DeleteBucket\"]}}")
                .description("A test rule created by the Java API")
                .build();

            PutRuleResponse ruleResponse = eventBrClient.putRule(ruleRequest);
            System.out.println("The ARN of the new rule is "+ ruleResponse.ruleArn());

        } catch (EventBridgeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [PutRule](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/PutRule) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a scheduled rule<a name="eventbridge_DeleteRule_java_topic"></a>

The following code example shows how to delete an Amazon EventBridge scheduled rule\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
  

```
    public static void deleteEBRule(EventBridgeClient eventBrClient, String ruleName) {

        try {
            DisableRuleRequest disableRuleRequest = DisableRuleRequest.builder()
                .name(ruleName)
                .eventBusName("default")
                .build();

            eventBrClient.disableRule(disableRuleRequest);
            DeleteRuleRequest ruleRequest = DeleteRuleRequest.builder()
                .name(ruleName)
                .eventBusName("default")
                .build();

            eventBrClient.deleteRule(ruleRequest);
            System.out.println("Rule "+ruleName + " was successfully deleted!");

        } catch (EventBridgeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteRule](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/DeleteRule) in *AWS SDK for Java 2\.x API Reference*\. 

### Send events<a name="eventbridge_PutEvents_java_topic"></a>

The following code example shows how to send Amazon EventBridge events\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
  

```
    public static void putEBEvents(EventBridgeClient eventBrClient, String resourceArn, String resourceArn2 ) {

        try {
            // Populate a List with the resource ARN values.
            List<String> resources = new ArrayList<>();
            resources.add(resourceArn);
            resources.add(resourceArn2);

            PutEventsRequestEntry reqEntry = PutEventsRequestEntry.builder()
                .resources(resources)
                .source("com.mycompany.myapp")
                .detailType("myDetailType")
                .detail("{ \"key1\": \"value1\", \"key2\": \"value2\" }")
                .build();

            PutEventsRequest eventsRequest = PutEventsRequest.builder()
                .entries(reqEntry)
                .build();

            PutEventsResponse result = eventBrClient.putEvents(eventsRequest);
            for (PutEventsResultEntry resultEntry : result.entries()) {
                if (resultEntry.eventId() != null) {
                    System.out.println("Event Id: " + resultEntry.eventId());
                } else {
                    System.out.println("Injection failed with Error Code: " + resultEntry.errorCode());
                }
            }

        } catch (EventBridgeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [PutEvents](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/PutEvents) in *AWS SDK for Java 2\.x API Reference*\. 