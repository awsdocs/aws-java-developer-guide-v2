--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Using alarm actions in CloudWatch<a name="examples-cloudwatch-use-alarm-actions"></a>

Using CloudWatch alarm actions, you can create alarms that perform actions such as automatically stopping, terminating, rebooting, or recovering Amazon EC2 instances\.

**Note**  
Alarm actions can be added to an alarm by using the [PutMetricAlarmRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cloudwatch/model/PutMetricAlarmRequest.html)'s `alarmActions` method when [creating an alarm](examples-cloudwatch-create-alarms.md)\.

## Enable alarm actions<a name="enable-alarm-actions"></a>

To enable alarm actions for a CloudWatch alarm, call the CloudWatchClient’s `enableAlarmActions` with a [EnableAlarmActionsRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cloudwatch/model/EnableAlarmActionsRequest.html) containing one or more names of alarms whose actions you want to enable\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.CloudWatchException;
import software.amazon.awssdk.services.cloudwatch.model.EnableAlarmActionsRequest;
import software.amazon.awssdk.services.cloudwatch.model.EnableAlarmActionsResponse;
```

 **Code** 

```
    public static void enableActions(CloudWatchClient cw, String alarm) {

        try {
            EnableAlarmActionsRequest request = EnableAlarmActionsRequest.builder()
                .alarmNames(alarm).build();

           cw.enableAlarmActions(request);
            System.out.printf(
                    "Successfully enabled actions on alarm %s", alarm);

        } catch (CloudWatchException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
   }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/cloudwatch/src/main/java/com/example/cloudwatch/EnableAlarmActions.java) on GitHub\.

## Disable alarm actions<a name="disable-alarm-actions"></a>

To disable alarm actions for a CloudWatch alarm, call the CloudWatchClient’s `disableAlarmActions` with a [DisableAlarmActionsRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/cloudwatch/model/DisableAlarmActionsRequest.html) containing one or more names of alarms whose actions you want to disable\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.CloudWatchException;
import software.amazon.awssdk.services.cloudwatch.model.DisableAlarmActionsRequest;
```

 **Code** 

```
    public static void disableActions(CloudWatchClient cw, String alarmName) {

        try {
             DisableAlarmActionsRequest request = DisableAlarmActionsRequest.builder()
                     .alarmNames(alarmName)
                     .build();

            cw.disableAlarmActions(request);
            System.out.printf(
                    "Successfully disabled actions on alarm %s", alarmName);

        } catch (CloudWatchException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/cloudwatch/src/main/java/com/example/cloudwatch/DisableAlarmActions.java) on GitHub\.

## More information<a name="more-information"></a>
+  [Create Alarms to Stop, Terminate, Reboot, or Recover an Instance](http://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/UsingAlarmActions.html) in the Amazon CloudWatch User Guide
+  [PutMetricAlarm](http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_PutMetricAlarm.html) in the Amazon CloudWatch API Reference
+  [EnableAlarmActions](http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_EnableAlarmActions.html) in the Amazon CloudWatch API Reference
+  [DisableAlarmActions](http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_DisableAlarmActions.html) in the Amazon CloudWatch API Reference