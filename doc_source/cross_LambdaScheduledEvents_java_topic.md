--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Use scheduled events to invoke a Lambda function<a name="cross_LambdaScheduledEvents_java_topic"></a>

**SDK for Java 2\.x**  
 Shows how to create an Amazon EventBridge scheduled event that invokes an AWS Lambda function\. Configure EventBridge to use a cron expression to schedule when the Lambda function is invoked\. In this example, you create a Lambda function by using the Lambda Java runtime API\. This example invokes different AWS services to perform a specific use case\. This example demonstrates how to create an app that sends a mobile text message to your employees that congratulates them at the one year anniversary date\.   
 For complete source code and instructions on how to set up and run, see the full example on [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/usecases/creating_scheduled_events)\.   

**Services used in this example**
+ DynamoDB
+ EventBridge
+ Lambda
+ Amazon SNS