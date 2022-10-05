--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# CloudFront examples using SDK for Java 2\.x<a name="java_cloudfront_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with CloudFront\.

*Actions* are code excerpts that show you how to call individual CloudFront functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple CloudFront functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w620aac15c13b9c15c13)

## Actions<a name="w620aac15c13b9c15c13"></a>

### Create a function<a name="cloudfront_CreateFunction_java_topic"></a>

The following code example shows how to create an Amazon CloudFront function\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudfront#readme)\. 
  

```
    public static String createNewFunction(CloudFrontClient cloudFrontClient, String functionName, String filePath) {

        try {

            InputStream is = new FileInputStream(filePath);
            SdkBytes functionCode = SdkBytes.fromInputStream(is);

            FunctionConfig config = FunctionConfig.builder()
                .comment("Created by using the CloudFront Java API")
                .runtime(FunctionRuntime.CLOUDFRONT_JS_1_0)
                .build();

            CreateFunctionRequest functionRequest = CreateFunctionRequest.builder()
                .name(functionName)
                .functionCode(functionCode)
                .functionConfig(config)
                .build();

            CreateFunctionResponse response = cloudFrontClient.createFunction(functionRequest);
            return response.functionSummary().functionMetadata().functionARN();

        } catch (CloudFrontException | FileNotFoundException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateFunction](https://docs.aws.amazon.com/goto/SdkForJavaV2/cloudfront-2020-05-31/CreateFunction) in *AWS SDK for Java 2\.x API Reference*\. 

### Update a distribution<a name="cloudfront_UpdateDistribution_java_topic"></a>

The following code example shows how to update an Amazon CloudFront distribution\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudfront#readme)\. 
  

```
    public static void modDistribution(CloudFrontClient cloudFrontClient, String idVal) {

        try {
            // Get the Distribution to modify.
            GetDistributionRequest disRequest = GetDistributionRequest.builder()
                 .id(idVal)
                 .build();

            GetDistributionResponse response = cloudFrontClient.getDistribution(disRequest);
            Distribution disObject = response.distribution();
            DistributionConfig config = disObject.distributionConfig();

            // Create a new  DistributionConfig object and add new values to comment and aliases
            DistributionConfig config1 = DistributionConfig.builder()
                .aliases(config.aliases()) // You can pass in new values here
                .comment("New Comment")
                .cacheBehaviors(config.cacheBehaviors())
                .priceClass(config.priceClass())
                .defaultCacheBehavior(config.defaultCacheBehavior())
                .enabled(config.enabled())
                .callerReference(config.callerReference())
                .logging(config.logging())
                .originGroups(config.originGroups())
                .origins(config.origins())
                .restrictions(config.restrictions())
                .defaultRootObject(config.defaultRootObject())
                .webACLId(config.webACLId())
                .httpVersion(config.httpVersion())
                .viewerCertificate(config.viewerCertificate())
                .customErrorResponses(config.customErrorResponses())
                .build();

            UpdateDistributionRequest updateDistributionRequest = UpdateDistributionRequest.builder()
                .distributionConfig(config1)
                .id(disObject.id())
                .ifMatch(response.eTag())
                .build();

            cloudFrontClient.updateDistribution(updateDistributionRequest);

        } catch (CloudFrontException e){
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [UpdateDistribution](https://docs.aws.amazon.com/goto/SdkForJavaV2/cloudfront-2020-05-31/UpdateDistribution) in *AWS SDK for Java 2\.x API Reference*\. 