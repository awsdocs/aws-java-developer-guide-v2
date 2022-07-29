--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Invoke, list, and delete AWS Lambda functions<a name="examples-lambda"></a>

This section provides examples of programming with the Lambda service client by using the AWS SDK for Java 2\.x\.

**Topics**
+ [Invoke a Lambda function](#invoke-function)
+ [List Lambda functions](#list-function)
+ [Delete a Lambda function](#delete-function)

## Invoke a Lambda function<a name="invoke-function"></a>

You can invoke a Lambda function by creating a [LambdaClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/lambda/LambdaClient.html) object and invoking its `invoke` method\. Create an [InvokeRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/lambda/model/InvokeRequest.html) object to specify additional information such as the function name and the payload to pass to the Lambda function\. Function names appear as *arn:aws:lambda:us\-east\-1:123456789012:function:HelloFunction*\. You can retrieve the value by looking at the function in the AWS Management Console\.

To pass payload data to a function, create a [SdkBytes](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/core/SdkBytes.html) object that contains information\. For example, in the following code example, notice the JSON data passed to the Lambda function\.

 **Imports** 

```
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import software.amazon.awssdk.services.lambda.model.LambdaException;
```

 **Code** 

The following code example demonstrates how to invoke a Lambda function\.

```
    public static void invokeFunction(LambdaClient awsLambda, String functionName) {

         InvokeResponse res = null ;
        try {
            //Need a SdkBytes instance for the payload
            String json = "{\"Hello \":\"Paris\"}";
            SdkBytes payload = SdkBytes.fromUtf8String(json) ;

            //Setup an InvokeRequest
            InvokeRequest request = InvokeRequest.builder()
                    .functionName(functionName)
                    .payload(payload)
                    .build();

            res = awsLambda.invoke(request);
            String value = res.payload().asUtf8String() ;
            System.out.println(value);

        } catch(LambdaException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/lambda/src/main/java/com/example/lambda/LambdaInvoke.java) on GitHub\.

## List Lambda functions<a name="list-function"></a>

Build a [LambdaClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/lambda/LambdaClient.html) object and invoke its `listFunctions` method\. This method returns a [ListFunctionsResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/lambda/model/ListFunctionsResponse.html) object\. You can invoke this object’s `functions` method to return a list of [FunctionConfiguration](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/lambda/model/FunctionConfiguration.html) objects\. You can iterate through the list to retrieve information about the functions\. For example, the following Java code example shows how to get each function name\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.LambdaException;
import software.amazon.awssdk.services.lambda.model.ListFunctionsResponse;
import software.amazon.awssdk.services.lambda.model.FunctionConfiguration;
import java.util.List;
```

 **Code** 

The following Java code example demonstrates how to retrieve a list of function names\.

```
    public static void listFunctions(LambdaClient awsLambda) {

        try {
            ListFunctionsResponse functionResult = awsLambda.listFunctions();
            List<FunctionConfiguration> list = functionResult.functions();

            for (FunctionConfiguration config: list) {
                System.out.println("The function name is "+config.functionName());
            }

        } catch(LambdaException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/lambda/src/main/java/com/example/lambda/ListLambdaFunctions.java) on GitHub\.

## Delete a Lambda function<a name="delete-function"></a>

Build a [LambdaClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/lambda/LambdaClient.html) object and invoke its `deleteFunction` method\. Create a [DeleteFunctionRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/lambda/model/DeleteFunctionRequest.html) object and pass it to the `deleteFunction` method\. This object contains information such as the name of the function to delete\. Function names appear as *arn:aws:lambda:us\-east\-1:123456789012:function:HelloFunction*\. You can retrieve the value by looking at the function in the AWS Management Console\.

 **Imports** 

```
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.model.DeleteFunctionRequest;
import software.amazon.awssdk.services.lambda.model.LambdaException;
```

 **Code** 

The following Java code demonstrates how to delete a Lambda function\.

```
    public static void deleteLambdaFunction(LambdaClient awsLambda, String functionName ) {
        try {
            //Setup an DeleteFunctionRequest
            DeleteFunctionRequest request = DeleteFunctionRequest.builder()
                    .functionName(functionName)
                    .build();

            awsLambda.deleteFunction(request);
            System.out.println("The "+functionName +" function was deleted");

        } catch(LambdaException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/lambda/src/main/java/com/example/lambda/DeleteFunction.java) on GitHub\.