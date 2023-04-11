# CloudWatch Logs examples using SDK for Java 2\.x<a name="java_cloudwatch-logs_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with CloudWatch Logs\.

*Actions* are code excerpts that show you how to call individual service functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple functions within the same service\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#actions)

## Actions<a name="actions"></a>

### Create a subscription filter<a name="cloudwatch-logs_PutSubscriptionFilter_java_topic"></a>

The following code example shows how to create an Amazon CloudWatch Logs subscription filter\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudwatch#readme)\. 
  

```
    public static void putSubFilters(CloudWatchLogsClient cwl,
                                     String filter,
                                     String pattern,
                                     String logGroup,
                                     String functionArn) {

        try {
            PutSubscriptionFilterRequest request = PutSubscriptionFilterRequest.builder()
                .filterName(filter)
                .filterPattern(pattern)
                .logGroupName(logGroup)
                .destinationArn(functionArn)
                .build();

            cwl.putSubscriptionFilter(request);
            System.out.printf(
                    "Successfully created CloudWatch logs subscription filter %s",
                    filter);

        } catch (CloudWatchLogsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [PutSubscriptionFilter](https://docs.aws.amazon.com/goto/SdkForJavaV2/logs-2014-03-28/PutSubscriptionFilter) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a subscription filter<a name="cloudwatch-logs_DeleteSubscriptionFilter_java_topic"></a>

The following code example shows how to delete an Amazon CloudWatch Logs subscription filter\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudwatch#readme)\. 
  

```
    public static void deleteSubFilter(CloudWatchLogsClient logs, String filter, String logGroup) {

        try {
            DeleteSubscriptionFilterRequest request = DeleteSubscriptionFilterRequest.builder()
                .filterName(filter)
                .logGroupName(logGroup)
                .build();

            logs.deleteSubscriptionFilter(request);
            System.out.printf("Successfully deleted CloudWatch logs subscription filter %s", filter);

        } catch (CloudWatchException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
   }
```
+  For API details, see [DeleteSubscriptionFilter](https://docs.aws.amazon.com/goto/SdkForJavaV2/logs-2014-03-28/DeleteSubscriptionFilter) in *AWS SDK for Java 2\.x API Reference*\. 

### Describe existing subscription filters<a name="cloudwatch-logs_DescribeSubscriptionFilters_java_topic"></a>

The following code example shows how to describe Amazon CloudWatch Logs existing subscription filters\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudwatch#readme)\. 
  

```
    public static void describeFilters(CloudWatchLogsClient logs, String logGroup) {

        try {
            boolean done = false;
            String newToken = null;

            while(!done) {
                DescribeSubscriptionFiltersResponse response;
                if (newToken == null) {
                    DescribeSubscriptionFiltersRequest request = DescribeSubscriptionFiltersRequest.builder()
                        .logGroupName(logGroup)
                        .limit(1).build();

                    response = logs.describeSubscriptionFilters(request);
                } else {
                    DescribeSubscriptionFiltersRequest request = DescribeSubscriptionFiltersRequest.builder()
                        .nextToken(newToken)
                        .logGroupName(logGroup)
                        .limit(1).build();
                    response = logs.describeSubscriptionFilters(request);
                }

                for(SubscriptionFilter filter : response.subscriptionFilters()) {
                    System.out.printf("Retrieved filter with name %s, " + "pattern %s " + "and destination arn %s",
                        filter.filterName(),
                        filter.filterPattern(),
                        filter.destinationArn());
                }

                if(response.nextToken() == null) {
                    done = true;
                } else {
                    newToken = response.nextToken();
                }
           }

        } catch (CloudWatchException e) {
           System.err.println(e.awsErrorDetails().errorMessage());
           System.exit(1);
        }
        System.out.printf("Done");
    }
```
+  For API details, see [DescribeSubscriptionFilters](https://docs.aws.amazon.com/goto/SdkForJavaV2/logs-2014-03-28/DescribeSubscriptionFilters) in *AWS SDK for Java 2\.x API Reference*\. 