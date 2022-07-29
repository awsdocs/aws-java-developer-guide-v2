--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# CloudWatch Events examples using SDK for Java 2\.x<a name="java_cloudwatch-events_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with CloudWatch Events\.

*Actions* are code excerpts that show you how to call individual CloudWatch Events functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple CloudWatch Events functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w591aac15c14b9c19c13)

## Actions<a name="w591aac15c14b9c19c13"></a>

### Adding a Lambda function target<a name="cloudwatch-events_PutTargets_java_topic"></a>

The following code example shows how to add an AWS Lambda function target to an Amazon CloudWatch Events event\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudwatch#readme)\. 
  

```
    public static void putCWTargets(CloudWatchEventsClient cwe, String ruleName, String functionArn, String targetId ) {

        try {
            Target target = Target.builder()
                .arn(functionArn)
                .id(targetId)
                .build();

            PutTargetsRequest request = PutTargetsRequest.builder()
                .targets(target)
                .rule(ruleName)
                .build();

            cwe.putTargets(request);
            System.out.printf(
                "Successfully created CloudWatch events target for rule %s",
                ruleName);

        } catch (CloudWatchException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [PutTargets](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/PutTargets) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a scheduled rule<a name="cloudwatch-events_PutRule_java_topic"></a>

The following code example shows how to create an Amazon CloudWatch Events scheduled rule\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudwatch#readme)\. 
  

```
    public static void putCWRule(CloudWatchEventsClient cwe, String ruleName, String roleArn) {

        try {
            PutRuleRequest request = PutRuleRequest.builder()
                .name(ruleName)
                .roleArn(roleArn)
                .scheduleExpression("rate(5 minutes)")
                .state(RuleState.ENABLED)
                .build();

            PutRuleResponse response = cwe.putRule(request);
            System.out.printf(
                    "Successfully created CloudWatch events rule %s with arn %s",
                    roleArn, response.ruleArn());

        } catch (
            CloudWatchException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [PutRule](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/PutRule) in *AWS SDK for Java 2\.x API Reference*\. 

### Send events<a name="cloudwatch-events_PutEvents_java_topic"></a>

The following code example shows how to send Amazon CloudWatch Events events\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudwatch#readme)\. 
  

```
    public static void putCWEvents(CloudWatchEventsClient cwe, String resourceArn ) {
        try {

            final String EVENT_DETAILS =
                "{ \"key1\": \"value1\", \"key2\": \"value2\" }";

            PutEventsRequestEntry requestEntry = PutEventsRequestEntry.builder()
                .detail(EVENT_DETAILS)
                .detailType("sampleSubmitted")
                .resources(resourceArn)
                .source("aws-sdk-java-cloudwatch-example")
                .build();

            PutEventsRequest request = PutEventsRequest.builder()
                .entries(requestEntry)
                .build();

            cwe.putEvents(request);
            System.out.println("Successfully put CloudWatch event");

        } catch (CloudWatchException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [PutEvents](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/PutEvents) in *AWS SDK for Java 2\.x API Reference*\. 