--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Publishing custom metric data to CloudWatch<a name="examples-cloudwatch-publish-custom-metrics"></a>

A number of AWS services publish [their own metrics](http://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/aws-namespaces.html) in namespaces beginning with " `AWS` " You can also publish custom metric data using your own namespace \(as long as it doesn’t begin with " `AWS` "\)\.

## Publish custom metric data<a name="cwid1"></a>

To publish your own metric data, call the CloudWatchClient’s `putMetricData` method with a [PutMetricDataRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cloudwatch/model/PutMetricDataRequest.html)\. The `PutMetricDataRequest` must include the custom namespace to use for the data, and information about the data point itself in a [MetricDatum](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cloudwatch/model/MetricDatum.html) object\.

**Note**  
You cannot specify a namespace that begins with " `AWS` "\. Namespaces that begin with " `AWS` " are reserved for use by Amazon Web Services products\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatch.model.MetricDatum;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataRequest;
import software.amazon.awssdk.services.cloudwatch.model.CloudWatchException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
```

 **Code** 

```
    public static void putMetData(CloudWatchClient cw, Double dataPoint ) {

        try {
            Dimension dimension = Dimension.builder()
                    .name("UNIQUE_PAGES")
                    .value("URLS")
                    .build();

            // Set an Instant object
            String time = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT );
            Instant instant = Instant.parse(time);

            MetricDatum datum = MetricDatum.builder()
                .metricName("PAGES_VISITED")
                .unit(StandardUnit.NONE)
                .value(dataPoint)
                .timestamp(instant)
                .dimensions(dimension).build();

            PutMetricDataRequest request = PutMetricDataRequest.builder()
                .namespace("SITE/TRAFFIC")
                .metricData(datum).build();

            cw.putMetricData(request);

        } catch (CloudWatchException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        System.out.printf("Successfully put data point %f", dataPoint);
     }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/cloudwatch/src/main/java/com/example/cloudwatch/PutMetricData.java) on GitHub\.

## More information<a name="more-information"></a>
+  [Using Amazon CloudWatch Metrics](http://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/working_with_metrics.html) in the Amazon CloudWatch User Guide\.
+  [AWS Namespaces](http://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/aws-namespaces.html) in the Amazon CloudWatch User Guide\.
+  [PutMetricData](http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_PutMetricData.html) in the Amazon CloudWatch API Reference\.