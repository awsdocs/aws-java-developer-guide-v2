# EventBridge examples using SDK for Java 2\.x<a name="java_eventbridge_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with EventBridge\.

*Actions* are code excerpts that show you how to call individual service functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple functions within the same service\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Get started**

## Hello EventBridge<a name="example_eventbridge_Hello_section"></a>

The following code examples show how to get started using EventBridge\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
  

```
/**
 * Before running this Java V2 code example, set up your development environment, including your credentials.
 *
 * For more information, see the following documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 *
 */
public class HelloEventBridge {

    public static void main(String[] args) {
        Region region = Region.US_WEST_2;
        EventBridgeClient eventBrClient = EventBridgeClient.builder()
            .region(region)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

        listBuses(eventBrClient) ;
        eventBrClient.close();
    }

    public static void listBuses( EventBridgeClient eventBrClient) {
        try {
            ListEventBusesRequest busesRequest = ListEventBusesRequest.builder()
                .limit(10)
                .build();

            ListEventBusesResponse response = eventBrClient.listEventBuses(busesRequest);
            List<EventBus> buses = response.eventBuses();
            for (EventBus bus: buses) {
                System.out.println("The name of the event bus is: "+bus.name());
                System.out.println("The ARN of the event bus is: "+bus.arn());
            }

        } catch (EventBridgeException e) {

            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListEventBuses](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/ListEventBuses) in *AWS SDK for Java 2\.x API Reference*\. 

**Topics**
+ [Actions](#actions)
+ [Scenarios](#scenarios)

## Actions<a name="actions"></a>

### Add a target<a name="eventbridge_PutTargets_java_topic"></a>

The following code example shows how to add a target to an Amazon EventBridge event\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
Add an Amazon SNS topic as a target for a rule\.  

```
    // Add a rule which triggers an SNS target when a file is uploaded to an S3 bucket.
    public static void addSnsEventRule(EventBridgeClient eventBrClient, String ruleName, String topicArn, String topicName, String eventRuleName, String bucketName) {
        String targetID = java.util.UUID.randomUUID().toString();
        Target myTarget = Target.builder()
            .id(targetID)
            .arn(topicArn)
            .build();

        List<Target> targets = new ArrayList<>();
        targets.add(myTarget);
        PutTargetsRequest request = PutTargetsRequest.builder()
            .eventBusName(null)
            .targets(targets)
            .rule(ruleName)
            .build();

        eventBrClient.putTargets(request);
        System.out.println("Added event rule "+eventRuleName +" with Amazon SNS target "+topicName +" for bucket "+bucketName +".");
    }
```
Add an input transformer to a target for a rule\.  

```
    public static void updateCustomRuleTargetWithTransform(EventBridgeClient eventBrClient, String topicArn, String ruleName){
        String targetId = java.util.UUID.randomUUID().toString();
        InputTransformer inputTransformer = InputTransformer.builder()
            .inputTemplate("\"Notification: sample event was received.\"")
            .build();

        Target target = Target.builder()
            .id(targetId)
            .arn(topicArn)
            .inputTransformer(inputTransformer)
            .build();

        try {
            PutTargetsRequest targetsRequest = PutTargetsRequest.builder()
                .rule(ruleName)
                .targets(target)
                .eventBusName(null)
                .build();

            eventBrClient.putTargets(targetsRequest);
        } catch (EventBridgeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [PutTargets](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/PutTargets) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a rule<a name="eventbridge_PutRule_java_topic"></a>

The following code example shows how to create an Amazon EventBridge rule\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
Create a scheduled rule\.  

```
        public static void createEBRule(EventBridgeClient eventBrClient, String ruleName, String cronExpression) {

            try {
                PutRuleRequest ruleRequest = PutRuleRequest.builder()
                    .name(ruleName)
                    .eventBusName("default")
                    .scheduleExpression(cronExpression)
                    .state("ENABLED")
                    .description("A test rule that runs on a schedule created by the Java API")
                    .build();

                PutRuleResponse ruleResponse = eventBrClient.putRule(ruleRequest);
                System.out.println("The ARN of the new rule is "+ ruleResponse.ruleArn());

            } catch (EventBridgeException e) {
                System.err.println(e.awsErrorDetails().errorMessage());
                System.exit(1);
            }
        }
```
Create a rule that triggers when an object is added to an Amazon Simple Storage Service bucket\.  

```
    // Create a new event rule that triggers when an Amazon S3 object is created in a bucket.
    public static void addEventRule( EventBridgeClient eventBrClient, String roleArn, String bucketName, String eventRuleName) {
        String pattern = "{\n" +
            "  \"source\": [\"aws.s3\"],\n" +
            "  \"detail-type\": [\"Object Created\"],\n" +
            "  \"detail\": {\n" +
            "    \"bucket\": {\n" +
            "      \"name\": [\""+bucketName+"\"]\n" +
            "    }\n" +
            "  }\n" +
            "}";

        try {
            PutRuleRequest ruleRequest = PutRuleRequest.builder()
                .description("Created by using the AWS SDK for Java v2")
                .name(eventRuleName)
                .eventPattern(pattern)
                .roleArn(roleArn)
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

### Delete a rule<a name="eventbridge_DeleteRule_java_topic"></a>

The following code example shows how to delete an Amazon EventBridge rule\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
  

```
    public static void deleteRuleByName(EventBridgeClient eventBrClient, String ruleName) {
        DeleteRuleRequest ruleRequest = DeleteRuleRequest.builder()
            .name(ruleName)
            .build();

        eventBrClient.deleteRule(ruleRequest);
        System.out.println("Successfully deleted the rule");
    }
```
+  For API details, see [DeleteRule](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/DeleteRule) in *AWS SDK for Java 2\.x API Reference*\. 

### Describe a rule<a name="eventbridge_DescribeRule_java_topic"></a>

The following code example shows how to describe an Amazon EventBridge rule\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
  

```
    public static void checkRule(EventBridgeClient eventBrClient, String eventRuleName) {
        try {
            DescribeRuleRequest ruleRequest = DescribeRuleRequest.builder()
                .name(eventRuleName)
                .build();

            DescribeRuleResponse response = eventBrClient.describeRule(ruleRequest);
            System.out.println("The state of the rule is "+response.stateAsString());

        } catch (EventBridgeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DescribeRule](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/DescribeRule) in *AWS SDK for Java 2\.x API Reference*\. 

### Disable a rule<a name="eventbridge_DisableRule_java_topic"></a>

The following code example shows how to disable an Amazon EventBridge rule\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
Disable a rule by using its rule name\.  

```
    public static void changeRuleState(EventBridgeClient eventBrClient, String eventRuleName, Boolean isEnabled) {
        try {
            if (!isEnabled) {
                System.out.println("Disabling the rule: "+eventRuleName);
                DisableRuleRequest ruleRequest = DisableRuleRequest.builder()
                    .name(eventRuleName)
                    .build();

                eventBrClient.disableRule(ruleRequest);
            } else {
                System.out.println("Enabling the rule: "+eventRuleName);
                EnableRuleRequest ruleRequest = EnableRuleRequest.builder()
                    .name(eventRuleName)
                    .build();
                eventBrClient.enableRule(ruleRequest);
            }

        } catch (EventBridgeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DisableRule](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/DisableRule) in *AWS SDK for Java 2\.x API Reference*\. 

### Enable a rule<a name="eventbridge_EnableRule_java_topic"></a>

The following code example shows how to enable an Amazon EventBridge rule\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
Enable a rule by using its rule name\.  

```
    public static void changeRuleState(EventBridgeClient eventBrClient, String eventRuleName, Boolean isEnabled) {
        try {
            if (!isEnabled) {
                System.out.println("Disabling the rule: "+eventRuleName);
                DisableRuleRequest ruleRequest = DisableRuleRequest.builder()
                    .name(eventRuleName)
                    .build();

                eventBrClient.disableRule(ruleRequest);
            } else {
                System.out.println("Enabling the rule: "+eventRuleName);
                EnableRuleRequest ruleRequest = EnableRuleRequest.builder()
                    .name(eventRuleName)
                    .build();
                eventBrClient.enableRule(ruleRequest);
            }

        } catch (EventBridgeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [EnableRule](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/EnableRule) in *AWS SDK for Java 2\.x API Reference*\. 

### List rule names for a target<a name="eventbridge_ListRuleNamesByTarget_java_topic"></a>

The following code example shows how to list Amazon EventBridge rule names for a target\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
List all of the rule names by using the target\.  

```
    public static void listTargetRules(EventBridgeClient eventBrClient, String topicArn) {
        ListRuleNamesByTargetRequest ruleNamesByTargetRequest = ListRuleNamesByTargetRequest.builder()
            .targetArn(topicArn)
            .build();

        ListRuleNamesByTargetResponse response = eventBrClient.listRuleNamesByTarget(ruleNamesByTargetRequest);
        List<String> rules = response.ruleNames();
        for (String rule:rules) {
            System.out.println("The rule name is "+rule);
        }
    }
```
+  For API details, see [ListRuleNamesByTarget](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/ListRuleNamesByTarget) in *AWS SDK for Java 2\.x API Reference*\. 

### List rules<a name="eventbridge_ListRules_java_topic"></a>

The following code example shows how to list Amazon EventBridge rules\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
Enable a rule by using its rule name\.  

```
    public static void listRules(EventBridgeClient eventBrClient) {
        try {
            ListRulesRequest rulesRequest = ListRulesRequest.builder()
                .eventBusName("default")
                .limit(10)
                .build();

            ListRulesResponse response = eventBrClient.listRules(rulesRequest);
            List<Rule> rules = response.rules();
            for (Rule rule : rules) {
                System.out.println("The rule name is : "+rule.name());
                System.out.println("The rule description is : "+rule.description());
                System.out.println("The rule state is : "+rule.stateAsString());
            }

        } catch (EventBridgeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListRules](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/ListRules) in *AWS SDK for Java 2\.x API Reference*\. 

### List targets for a rule<a name="eventbridge_ListTargetsByRule_java_topic"></a>

The following code example shows how to list Amazon EventBridge targets for a rule\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
List all of the targets for a rule by using the rule name\.  

```
    public static void listTargets(EventBridgeClient eventBrClient, String ruleName) {
        ListTargetsByRuleRequest ruleRequest = ListTargetsByRuleRequest.builder()
            .rule(ruleName)
            .build();

        ListTargetsByRuleResponse res = eventBrClient.listTargetsByRule(ruleRequest);
        List<Target> tagets = res.targets();
        for (Target target :tagets) {
            System.out.println("Target ARN: "+target.arn());
        }
    }
```
+  For API details, see [ListTargetsByRule](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/ListTargetsByRule) in *AWS SDK for Java 2\.x API Reference*\. 

### Remove targets from a rule<a name="eventbridge_RemoveTargets_java_topic"></a>

The following code example shows how to remove Amazon EventBridge targets from a rule\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
Remove all of the targets for a rule by using the rule name\.  

```
    public static void deleteTargetsFromRule(EventBridgeClient eventBrClient, String eventRuleName) {
        // First, get all targets that will be deleted.
        ListTargetsByRuleRequest request = ListTargetsByRuleRequest.builder()
            .rule(eventRuleName)
            .build();

        ListTargetsByRuleResponse response = eventBrClient.listTargetsByRule(request);
        List<Target> allTargets = response.targets();

        // Get all targets and delete them.
        for (Target myTarget:allTargets) {
            RemoveTargetsRequest removeTargetsRequest = RemoveTargetsRequest.builder()
                .rule(eventRuleName)
                .ids(myTarget.id())
                .build();

            eventBrClient.removeTargets(removeTargetsRequest);
            System.out.println("Successfully removed the target");
        }
    }
```
+  For API details, see [RemoveTargets](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/RemoveTargets) in *AWS SDK for Java 2\.x API Reference*\. 

### Send events<a name="eventbridge_PutEvents_java_topic"></a>

The following code example shows how to send Amazon EventBridge events\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
  

```
    public static void triggerCustomRule(EventBridgeClient eventBrClient, String email) {
        String json = "{" +
            "\"UserEmail\": \""+email+"\"," +
            "\"Message\": \"This event was generated by example code.\"" +
            "\"UtcTime\": \"Now.\"" +
            "}";

        PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
            .source("ExampleSource")
            .detail(json)
            .detailType("ExampleType")
            .build();

        PutEventsRequest eventsRequest = PutEventsRequest.builder()
            .entries(entry)
            .build();

        eventBrClient.putEvents(eventsRequest);
    }
```
+  For API details, see [PutEvents](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/PutEvents) in *AWS SDK for Java 2\.x API Reference*\. 

## Scenarios<a name="scenarios"></a>

### Get started with rules and targets<a name="eventbridge_Scenario_GettingStarted_java_topic"></a>

The following code example shows how to:
+ Create a rule and add a target to it\.
+ Enable and disable rules\.
+ List and update rules and targets\.
+ Send events, then clean up resources\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/eventbridge#readme)\. 
  

```
/**
 * Before running this Java V2 code example, set up your development environment, including your credentials.
 *
 * For more information, see the following documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 *
 * This Java code example performs the following tasks:
 *
 * This Java V2 example performs the following tasks with Amazon EventBridge:
 *
 * 1. Creates an AWS Identity and Access Management (IAM) role to use with Amazon EventBridge.
 * 2. Amazon Simple Storage Service (Amazon S3) bucket with EventBridge events enabled.
 * 3. Creates a rule that triggers when an object is uploaded to Amazon S3.
 * 4. Lists rules on the event bus.
 * 5. Creates a new Amazon Simple Notification Service (Amazon SNS) topic and lets the user subscribe to it.
 * 6. Adds a target to the rule that sends an email to the specified topic.
 * 7. Creates an EventBridge event that sends an email when an Amazon S3 object is created.
 * 8. Lists Targets.
 * 9. Lists the rules for the same target.
 * 10. Triggers the rule by uploading a file to the Amazon S3 bucket.
 * 11. Disables a specific rule.
 * 12. Checks and print the state of the rule.
 * 13. Adds a transform to the rule to change the text of the email.
 * 14. Enables a specific rule.
 * 15. Triggers the updated rule by uploading a file to the Amazon S3 bucket.
 * 16. Updates the rule to be a custom rule pattern.
 * 17. Sending an event to trigger the rule.
 * 18. Cleans up resources.
 *
 */
public class EventbridgeMVP {
    public static final String DASHES = new String(new char[80]).replace("\0", "-");
    public static void main(String[] args) throws InterruptedException, IOException {
        final String usage = "\n" +
            "Usage:\n" +
            "    <roleName> <bucketName> <topicName> <eventRuleName>\n\n" +
            "Where:\n" +
            "    roleName - The name of the role to create.\n" +
            "    bucketName - The Amazon Simple Storage Service (Amazon S3) bucket name to create.\n" +
            "    topicName - The name of the Amazon Simple Notification Service (Amazon SNS) topic to create.\n" +
            "    eventRuleName - The Amazon EventBridge rule name to create.\n" ;

        if (args.length != 5) {
            System.out.println(usage);
            System.exit(1);
        }

        String polJSON = "{" +
            "\"Version\": \"2012-10-17\"," +
            "\"Statement\": [{" +
            "\"Effect\": \"Allow\"," +
            "\"Principal\": {" +
            "\"Service\": \"events.amazonaws.com\"" +
            "}," +
            "\"Action\": \"sts:AssumeRole\"" +
            "}]" +
            "}";

        Scanner sc = new Scanner(System.in);
        String roleName = args[0];
        String bucketName = args[1];
        String topicName = args[2];
        String eventRuleName = args[3];

        Region region = Region.US_EAST_1;
        EventBridgeClient eventBrClient = EventBridgeClient.builder()
            .region(region)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

        S3Client s3Client = S3Client.builder()
            .region(region)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

        Region regionGl = Region.AWS_GLOBAL;
        IamClient iam = IamClient.builder()
            .region(regionGl)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

        SnsClient snsClient = SnsClient.builder()
            .region(region)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

        System.out.println(DASHES);
        System.out.println("Welcome to the Amazon EventBridge example scenario.");
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("1. Create an AWS Identity and Access Management (IAM) role to use with Amazon EventBridge.");
        String roleArn = createIAMRole(iam, roleName, polJSON);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("2. Create an S3 bucket with EventBridge events enabled.");
        if (checkBucket(s3Client, bucketName)) {
            System.out.println("Bucket "+ bucketName +" already exists. Ending this scenario.");
            System.exit(1);
        }

        createBucket(s3Client, bucketName);
        Thread.sleep(3000);
        setBucketNotification(s3Client, bucketName);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("3. Create a rule that triggers when an object is uploaded to Amazon S3.");
        Thread.sleep(10000);
        addEventRule(eventBrClient, roleArn, bucketName, eventRuleName);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("4. List rules on the event bus.");
        listRules(eventBrClient);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("5. Create a new SNS topic for testing and let the user subscribe to the topic.");
        String topicArn = createSnsTopic(snsClient, topicName);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("6. Add a target to the rule that sends an email to the specified topic.");
        System.out.println("Enter your email to subscribe to the Amazon SNS topic:");
        String email = sc.nextLine();
        subEmail(snsClient, topicArn, email);
        System.out.println("Use the link in the email you received to confirm your subscription. Then, press Enter to continue.");
        sc.nextLine();
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("7. Create an EventBridge event that sends an email when an Amazon S3 object is created.");
        addSnsEventRule(eventBrClient, eventRuleName, topicArn, topicName, eventRuleName, bucketName);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println(" 8. List Targets.");
        listTargets(eventBrClient, eventRuleName);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println(" 9. List the rules for the same target.");
        listTargetRules(eventBrClient, topicArn);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println(" 10. Trigger the rule by uploading a file to the S3 bucket.");
        System.out.println("Press Enter to continue.");
        sc.nextLine();
        uploadTextFiletoS3(s3Client, bucketName);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("11. Disable a specific rule.");
        changeRuleState(eventBrClient, eventRuleName, false);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("12. Check and print the state of the rule.");
        checkRule(eventBrClient, eventRuleName);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("13. Add a transform to the rule to change the text of the email.");
        updateSnsEventRule(eventBrClient, topicArn, eventRuleName);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("14. Enable a specific rule.");
        changeRuleState(eventBrClient, eventRuleName, true);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println(" 15. Trigger the updated rule by uploading a file to the S3 bucket.");
        System.out.println("Press Enter to continue.");
        sc.nextLine();
        uploadTextFiletoS3(s3Client, bucketName);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println(" 16. Update the rule to be a custom rule pattern.");
        updateToCustomRule(eventBrClient, eventRuleName);
        System.out.println("Updated event rule "+eventRuleName +" to use a custom pattern.");
        updateCustomRuleTargetWithTransform(eventBrClient, topicArn, eventRuleName);
        System.out.println("Updated event target "+topicArn +".");
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("17. Sending an event to trigger the rule. This will trigger a subscription email.");
        triggerCustomRule(eventBrClient, email);
        System.out.println("Events have been sent. Press Enter to continue.");
        sc.nextLine();
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("18. Clean up resources.");
        System.out.println("Do you want to clean up resources (y/n)");
        String ans = sc.nextLine();
        if (ans.compareTo("y") == 0) {
            cleanupResources(eventBrClient, snsClient, s3Client, iam, topicArn, eventRuleName, bucketName, roleName );
        } else {
            System.out.println("The resources will not be cleaned up. ");
        }
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("The Amazon EventBridge example scenario has successfully completed.");
        System.out.println(DASHES);
    }

    public static void cleanupResources(EventBridgeClient eventBrClient, SnsClient snsClient, S3Client s3Client, IamClient iam, String topicArn, String eventRuleName, String bucketName, String roleName) {
        System.out.println("Removing all targets from the event rule.");
        deleteTargetsFromRule(eventBrClient, eventRuleName);
        deleteRuleByName(eventBrClient, eventRuleName);
        deleteSNSTopic(snsClient, topicArn);
        deleteS3Bucket(s3Client, bucketName);
        deleteRole(iam, roleName);
    }

    public static void deleteRole(IamClient iam, String roleName) {
        String policyArn = "arn:aws:iam::aws:policy/AmazonEventBridgeFullAccess";
        DetachRolePolicyRequest policyRequest = DetachRolePolicyRequest.builder()
            .policyArn(policyArn)
            .roleName(roleName)
            .build();

        iam.detachRolePolicy(policyRequest);
        System.out.println("Successfully detached policy " + policyArn + " from role " + roleName);

        // Delete the role.
        DeleteRoleRequest roleRequest = DeleteRoleRequest.builder()
            .roleName(roleName)
            .build();

        iam.deleteRole(roleRequest);
        System.out.println("*** Successfully deleted " + roleName);
    }

    public static void deleteS3Bucket( S3Client s3Client, String bucketName) {
        // Remove all the objects from the S3 bucket.
        ListObjectsRequest listObjects = ListObjectsRequest.builder()
            .bucket(bucketName)
            .build();

        ListObjectsResponse res = s3Client.listObjects(listObjects);
        List<S3Object> objects = res.contents();
        ArrayList<ObjectIdentifier> toDelete = new ArrayList<>();

        for (S3Object myValue : objects) {
            toDelete.add(ObjectIdentifier.builder()
                .key(myValue.key())
                .build());
        }

        DeleteObjectsRequest dor = DeleteObjectsRequest.builder()
            .bucket(bucketName)
            .delete(Delete.builder()
            .objects(toDelete).build())
            .build();

        s3Client.deleteObjects(dor);

        // Delete the S3 bucket.
        DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder()
            .bucket(bucketName)
            .build();

        s3Client.deleteBucket(deleteBucketRequest);
        System.out.println("You have deleted the bucket and the objects");
    }

    // Delete the SNS topic.
    public static void deleteSNSTopic(SnsClient snsClient, String topicArn ) {
        try {
            DeleteTopicRequest request = DeleteTopicRequest.builder()
                .topicArn(topicArn)
                .build();

            DeleteTopicResponse result = snsClient.deleteTopic(request);
            System.out.println("\n\nStatus was " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void deleteRuleByName(EventBridgeClient eventBrClient, String ruleName) {
        DeleteRuleRequest ruleRequest = DeleteRuleRequest.builder()
            .name(ruleName)
            .build();

        eventBrClient.deleteRule(ruleRequest);
        System.out.println("Successfully deleted the rule");
    }

    public static void deleteTargetsFromRule(EventBridgeClient eventBrClient, String eventRuleName) {
        // First, get all targets that will be deleted.
        ListTargetsByRuleRequest request = ListTargetsByRuleRequest.builder()
            .rule(eventRuleName)
            .build();

        ListTargetsByRuleResponse response = eventBrClient.listTargetsByRule(request);
        List<Target> allTargets = response.targets();

        // Get all targets and delete them.
        for (Target myTarget:allTargets) {
            RemoveTargetsRequest removeTargetsRequest = RemoveTargetsRequest.builder()
                .rule(eventRuleName)
                .ids(myTarget.id())
                .build();

            eventBrClient.removeTargets(removeTargetsRequest);
            System.out.println("Successfully removed the target");
        }
    }

    public static void triggerCustomRule(EventBridgeClient eventBrClient, String email) {
        String json = "{" +
            "\"UserEmail\": \""+email+"\"," +
            "\"Message\": \"This event was generated by example code.\"" +
            "\"UtcTime\": \"Now.\"" +
            "}";

        PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
            .source("ExampleSource")
            .detail(json)
            .detailType("ExampleType")
            .build();

        PutEventsRequest eventsRequest = PutEventsRequest.builder()
            .entries(entry)
            .build();

        eventBrClient.putEvents(eventsRequest);
    }

    public static void updateCustomRuleTargetWithTransform(EventBridgeClient eventBrClient, String topicArn, String ruleName){
        String targetId = java.util.UUID.randomUUID().toString();
        InputTransformer inputTransformer = InputTransformer.builder()
            .inputTemplate("\"Notification: sample event was received.\"")
            .build();

        Target target = Target.builder()
            .id(targetId)
            .arn(topicArn)
            .inputTransformer(inputTransformer)
            .build();

        try {
            PutTargetsRequest targetsRequest = PutTargetsRequest.builder()
                .rule(ruleName)
                .targets(target)
                .eventBusName(null)
                .build();

            eventBrClient.putTargets(targetsRequest);
        } catch (EventBridgeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void updateToCustomRule(EventBridgeClient eventBrClient, String ruleName) {
        String customEventsPattern = "{" +
            "\"source\": [\"ExampleSource\"]," +
            "\"detail-type\": [\"ExampleType\"]" +
            "}";

        PutRuleRequest request = PutRuleRequest.builder()
            .name(ruleName)
            .description("Custom test rule")
            .eventPattern(customEventsPattern)
            .build();

        eventBrClient.putRule(request);
    }

    // Update an Amazon S3 object created rule with a transform on the target.
    public static void updateSnsEventRule(EventBridgeClient eventBrClient, String topicArn, String ruleName){
        String targetId = java.util.UUID.randomUUID().toString();
        Map<String, String> myMap = new HashMap<>();
        myMap.put("bucket", "$.detail.bucket.name");
        myMap.put("time", "$.time");

        InputTransformer inputTransformer = InputTransformer.builder()
            .inputTemplate("\"Notification: an object was uploaded to bucket <bucket> at <time>.\"")
            .inputPathsMap(myMap)
            .build();

        Target target = Target.builder()
            .id(targetId)
            .arn(topicArn)
            .inputTransformer(inputTransformer)
            .build();

        try {
            PutTargetsRequest targetsRequest = PutTargetsRequest.builder()
                .rule(ruleName)
                .targets(target)
                .eventBusName(null)
                .build();

            eventBrClient.putTargets(targetsRequest);

        } catch (EventBridgeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void checkRule(EventBridgeClient eventBrClient, String eventRuleName) {
        try {
            DescribeRuleRequest ruleRequest = DescribeRuleRequest.builder()
                .name(eventRuleName)
                .build();

            DescribeRuleResponse response = eventBrClient.describeRule(ruleRequest);
            System.out.println("The state of the rule is "+response.stateAsString());

        } catch (EventBridgeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void changeRuleState(EventBridgeClient eventBrClient, String eventRuleName, Boolean isEnabled) {
        try {
            if (!isEnabled) {
                System.out.println("Disabling the rule: "+eventRuleName);
                DisableRuleRequest ruleRequest = DisableRuleRequest.builder()
                    .name(eventRuleName)
                    .build();

                eventBrClient.disableRule(ruleRequest);
            } else {
                System.out.println("Enabling the rule: "+eventRuleName);
                EnableRuleRequest ruleRequest = EnableRuleRequest.builder()
                    .name(eventRuleName)
                    .build();
                eventBrClient.enableRule(ruleRequest);
            }

        } catch (EventBridgeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    // Create and upload a file to an S3 bucket to trigger an event.
    public static void uploadTextFiletoS3(S3Client s3Client, String bucketName) throws IOException {
        // Create a unique file name.
        String fileSuffix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "TextFile"+fileSuffix+".txt";

        File myFile = new File(fileName);
        FileWriter fw = new FileWriter(myFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("This is a sample file for testing uploads.");
        bw.close();

        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

            s3Client.putObject(putOb, RequestBody.fromFile(myFile));

        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void listTargetRules(EventBridgeClient eventBrClient, String topicArn) {
        ListRuleNamesByTargetRequest ruleNamesByTargetRequest = ListRuleNamesByTargetRequest.builder()
            .targetArn(topicArn)
            .build();

        ListRuleNamesByTargetResponse response = eventBrClient.listRuleNamesByTarget(ruleNamesByTargetRequest);
        List<String> rules = response.ruleNames();
        for (String rule:rules) {
            System.out.println("The rule name is "+rule);
        }
    }

    public static void listTargets(EventBridgeClient eventBrClient, String ruleName) {
        ListTargetsByRuleRequest ruleRequest = ListTargetsByRuleRequest.builder()
            .rule(ruleName)
            .build();

        ListTargetsByRuleResponse res = eventBrClient.listTargetsByRule(ruleRequest);
        List<Target> tagets = res.targets();
        for (Target target :tagets) {
            System.out.println("Target ARN: "+target.arn());
        }
    }

    // Add a rule which triggers an SNS target when a file is uploaded to an S3 bucket.
    public static void addSnsEventRule(EventBridgeClient eventBrClient, String ruleName, String topicArn, String topicName, String eventRuleName, String bucketName) {
        String targetID = java.util.UUID.randomUUID().toString();
        Target myTarget = Target.builder()
            .id(targetID)
            .arn(topicArn)
            .build();

        List<Target> targets = new ArrayList<>();
        targets.add(myTarget);
        PutTargetsRequest request = PutTargetsRequest.builder()
            .eventBusName(null)
            .targets(targets)
            .rule(ruleName)
            .build();

        eventBrClient.putTargets(request);
        System.out.println("Added event rule "+eventRuleName +" with Amazon SNS target "+topicName +" for bucket "+bucketName +".");
    }

    public static void subEmail(SnsClient snsClient, String topicArn, String email) {
        try {
            SubscribeRequest request = SubscribeRequest.builder()
                .protocol("email")
                .endpoint(email)
                .returnSubscriptionArn(true)
                .topicArn(topicArn)
                .build();

            SubscribeResponse result = snsClient.subscribe(request);
            System.out.println("Subscription ARN: " + result.subscriptionArn() + "\n\n Status is " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void listRules(EventBridgeClient eventBrClient) {
        try {
            ListRulesRequest rulesRequest = ListRulesRequest.builder()
                .eventBusName("default")
                .limit(10)
                .build();

            ListRulesResponse response = eventBrClient.listRules(rulesRequest);
            List<Rule> rules = response.rules();
            for (Rule rule : rules) {
                System.out.println("The rule name is : "+rule.name());
                System.out.println("The rule description is : "+rule.description());
                System.out.println("The rule state is : "+rule.stateAsString());
            }

        } catch (EventBridgeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static String createSnsTopic(SnsClient snsClient, String topicName) {
        String topicPolicy = "{" +
            "\"Version\": \"2012-10-17\"," +
            "\"Statement\": [{" +
            "\"Sid\": \"EventBridgePublishTopic\"," +
            "\"Effect\": \"Allow\"," +
            "\"Principal\": {" +
            "\"Service\": \"events.amazonaws.com\"" +
            "}," +
            "\"Resource\": \"*\"," +
            "\"Action\": \"sns:Publish\"" +
            "}]" +
            "}";

        Map<String, String> topicAttributes = new HashMap<>();
        topicAttributes.put("Policy", topicPolicy);
        CreateTopicRequest topicRequest = CreateTopicRequest.builder()
            .name(topicName)
            .attributes(topicAttributes)
            .build();

        CreateTopicResponse response = snsClient.createTopic(topicRequest);
        System.out.println("Added topic "+topicName +" for email subscriptions.");
        return response.topicArn();
    }

    // Create a new event rule that triggers when an Amazon S3 object is created in a bucket.
    public static void addEventRule( EventBridgeClient eventBrClient, String roleArn, String bucketName, String eventRuleName) {
        String pattern = "{\n" +
            "  \"source\": [\"aws.s3\"],\n" +
            "  \"detail-type\": [\"Object Created\"],\n" +
            "  \"detail\": {\n" +
            "    \"bucket\": {\n" +
            "      \"name\": [\""+bucketName+"\"]\n" +
            "    }\n" +
            "  }\n" +
            "}";

        try {
            PutRuleRequest ruleRequest = PutRuleRequest.builder()
                .description("Created by using the AWS SDK for Java v2")
                .name(eventRuleName)
                .eventPattern(pattern)
                .roleArn(roleArn)
                .build();

            PutRuleResponse ruleResponse = eventBrClient.putRule(ruleRequest);
            System.out.println("The ARN of the new rule is "+ ruleResponse.ruleArn());

        } catch (EventBridgeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    // Determine if the S3 bucket exists.
    public static Boolean checkBucket(S3Client s3Client, String bucketName) {
        try {
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                .bucket(bucketName)
                .build();

            s3Client.headBucket(headBucketRequest);
            return true;
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }
        return false;
    }

    // Set the S3 bucket notification configuration.
    public static void setBucketNotification(S3Client s3Client, String bucketName) {
        try {
            EventBridgeConfiguration eventBridgeConfiguration = EventBridgeConfiguration.builder()
                 .build();

            NotificationConfiguration configuration = NotificationConfiguration.builder()
                .eventBridgeConfiguration(eventBridgeConfiguration)
                .build();

            PutBucketNotificationConfigurationRequest configurationRequest = PutBucketNotificationConfigurationRequest.builder()
                .bucket(bucketName)
                .notificationConfiguration(configuration)
                .skipDestinationValidation(true)
                .build();

            s3Client.putBucketNotificationConfiguration(configurationRequest);
            System.out.println("Added bucket " + bucketName + " with EventBridge events enabled.");

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void createBucket(S3Client s3Client, String bucketName) {
        try {
            S3Waiter s3Waiter = s3Client.waiter();
            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();

            s3Client.createBucket(bucketRequest);
            HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                .bucket(bucketName)
                .build();

            // Wait until the bucket is created and print out the response.
            WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);
            waiterResponse.matched().response().ifPresent(System.out::println);
            System.out.println(bucketName +" is ready");

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static String createIAMRole(IamClient iam, String rolename, String polJSON ) {
        try {
            CreateRoleRequest request = CreateRoleRequest.builder()
                .roleName(rolename)
                .assumeRolePolicyDocument(polJSON)
                .description("Created using the AWS SDK for Java")
                .build();

            CreateRoleResponse response = iam.createRole(request);
            AttachRolePolicyRequest rolePolicyRequest = AttachRolePolicyRequest.builder()
                .roleName(rolename)
                .policyArn("arn:aws:iam::aws:policy/AmazonEventBridgeFullAccess")
                .build();

            iam.attachRolePolicy(rolePolicyRequest);
            return response.role().arn();

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
}
```
+ For API details, see the following topics in *AWS SDK for Java 2\.x API Reference*\.
  + [DeleteRule](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/DeleteRule)
  + [DescribeRule](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/DescribeRule)
  + [DisableRule](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/DisableRule)
  + [EnableRule](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/EnableRule)
  + [ListRuleNamesByTarget](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/ListRuleNamesByTarget)
  + [ListRules](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/ListRules)
  + [ListTargetsByRule](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/ListTargetsByRule)
  + [PutEvents](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/PutEvents)
  + [PutRule](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/PutRule)
  + [PutTargets](https://docs.aws.amazon.com/goto/SdkForJavaV2/eventbridge-2015-10-07/PutTargets)