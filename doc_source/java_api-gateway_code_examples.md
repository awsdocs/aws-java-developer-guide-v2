--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# API Gateway examples using SDK for Java 2\.x<a name="java_api-gateway_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with API Gateway\.

*Actions* are code excerpts that show you how to call individual API Gateway functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple API Gateway functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w620aac15c13b9c11c13)

## Actions<a name="w620aac15c13b9c11c13"></a>

### Create a REST API<a name="api-gateway_CreateRestApi_java_topic"></a>

The following code example shows how to create an API Gateway REST API\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/apigateway#readme)\. 
  

```
    public static String createAPI( ApiGatewayClient apiGateway, String restApiId, String restApiName) {

        try {
            CreateRestApiRequest request = CreateRestApiRequest.builder()
                .cloneFrom(restApiId)
                .description("Created using the Gateway Java API")
                .name(restApiName)
                .build();

            CreateRestApiResponse response = apiGateway.createRestApi(request);
            System.out.println("The id of the new api is "+response.id());
            return response.id();

        } catch (ApiGatewayException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateRestApi](https://docs.aws.amazon.com/goto/SdkForJavaV2/apigateway-2015-07-09/CreateRestApi) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a REST API<a name="api-gateway_DeleteRestApi_java_topic"></a>

The following code example shows how to delete an API Gateway REST API\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/apigateway#readme)\. 
  

```
    public static void deleteAPI( ApiGatewayClient apiGateway, String restApiId) {

        try {
            DeleteRestApiRequest request = DeleteRestApiRequest.builder()
                .restApiId(restApiId)
                .build();

            apiGateway.deleteRestApi(request);
            System.out.println("The API was successfully deleted");

        } catch (ApiGatewayException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteRestApi](https://docs.aws.amazon.com/goto/SdkForJavaV2/apigateway-2015-07-09/DeleteRestApi) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a deployment<a name="api-gateway_DeleteDeployment_java_topic"></a>

The following code example shows how to delete a deployment\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/apigateway#readme)\. 
  

```
    public static void deleteSpecificDeployment(ApiGatewayClient apiGateway, String restApiId, String deploymentId) {

        try {
            DeleteDeploymentRequest request = DeleteDeploymentRequest.builder()
                .restApiId(restApiId)
                .deploymentId(deploymentId)
                .build();

            apiGateway.deleteDeployment(request);
            System.out.println("Deployment was deleted" );

        } catch (ApiGatewayException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteDeployment](https://docs.aws.amazon.com/goto/SdkForJavaV2/apigateway-2015-07-09/DeleteDeployment) in *AWS SDK for Java 2\.x API Reference*\. 

### Deploy a REST API<a name="api-gateway_CreateDeployment_java_topic"></a>

The following code example shows how to deploy an API Gateway REST API\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/apigateway#readme)\. 
  

```
    public static String createNewDeployment(ApiGatewayClient apiGateway, String restApiId, String stageName) {

        try {
            CreateDeploymentRequest request = CreateDeploymentRequest.builder()
                .restApiId(restApiId)
                .description("Created using the AWS API Gateway Java API")
                .stageName(stageName)
                .build();

            CreateDeploymentResponse response = apiGateway.createDeployment(request);
            System.out.println("The id of the deployment is "+response.id());
            return response.id();

        } catch (ApiGatewayException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return ""  ;
    }
```
+  For API details, see [CreateDeployment](https://docs.aws.amazon.com/goto/SdkForJavaV2/apigateway-2015-07-09/CreateDeployment) in *AWS SDK for Java 2\.x API Reference*\. 