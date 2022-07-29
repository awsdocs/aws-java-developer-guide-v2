--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Getting metrics from CloudWatch<a name="examples-cloudwatch-get-metrics"></a>

## Listing metrics<a name="listing-metrics"></a>

To list CloudWatch metrics, create a [ListMetricsRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cloudwatch/model/ListMetricsRequest.html) and call the CloudWatchClient’s `listMetrics` method\. You can use the `ListMetricsRequest` to filter the returned metrics by namespace, metric name, or dimensions\.

**Note**  
A list of metrics and dimensions that are posted by AWS services can be found within the [Amazon CloudWatch Metrics and Dimensions Reference](http://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/CW_Support_For_AWS.html) in the Amazon CloudWatch User Guide\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.CloudWatchException;
import software.amazon.awssdk.services.cloudwatch.model.ListMetricsRequest;
import software.amazon.awssdk.services.cloudwatch.model.ListMetricsResponse;
import software.amazon.awssdk.services.cloudwatch.model.Metric;
```

 **Code** 

```
    public static void listMets( CloudWatchClient cw, String namespace) {

        boolean done = false;
        String nextToken = null;

        try {
            while(!done) {

                ListMetricsResponse response;

                if (nextToken == null) {
                   ListMetricsRequest request = ListMetricsRequest.builder()
                        .namespace(namespace)
                        .build();

                 response = cw.listMetrics(request);
                } else {
                  ListMetricsRequest request = ListMetricsRequest.builder()
                        .namespace(namespace)
                        .nextToken(nextToken)
                        .build();

                response = cw.listMetrics(request);
            }

            for (Metric metric : response.metrics()) {
                System.out.printf(
                        "Retrieved metric %s", metric.metricName());
                System.out.println();
            }

            if(response.nextToken() == null) {
                done = true;
            } else {
                nextToken = response.nextToken();
            }
        }

        } catch (CloudWatchException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

The metrics are returned in a [ListMetricsResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cloudwatch/model/ListMetricsResponse.html) by calling its `getMetrics` method\.

The results may be *paged*\. To retrieve the next batch of results, call `nextToken` on the response object and use the token value to build a new request object\. Then call the `listMetrics` method again with the new request\.

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/cloudwatch/src/main/java/com/example/cloudwatch/ListMetrics.java) on GitHub\.

## More information<a name="more-information"></a>
+  [ListMetrics](http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_ListMetrics.html) in the Amazon CloudWatch API Reference