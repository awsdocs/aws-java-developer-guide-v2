--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Use API Gateway to invoke a Lambda function<a name="cross_LambdaAPIGateway_java_topic"></a>

**SDK for Java 2\.x**  
 Shows how to create an AWS Lambda function by using the Lambda Java runtime API\. This example invokes different AWS services to perform a specific use case\. This example demonstrates how to create a Lambda function invoked by Amazon API Gateway that scans an Amazon DynamoDB table for work anniversaries and uses Amazon Simple Notification Service \(Amazon SNS\) to send a text message to your employees that congratulates them at their one year anniversary date\.   
 For complete source code and instructions on how to set up and run, see the full example on [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/usecases/creating_lambda_apigateway)\.   

**Services used in this example**
+ API Gateway
+ DynamoDB
+ Lambda
+ Amazon SNS