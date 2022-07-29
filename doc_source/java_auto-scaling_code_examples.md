--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Amazon EC2 Auto Scaling examples using SDK for Java 2\.x<a name="java_auto-scaling_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Amazon EC2 Auto Scaling\.

*Actions* are code excerpts that show you how to call individual Amazon EC2 Auto Scaling functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple Amazon EC2 Auto Scaling functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w591aac15c14b9c31c13)
+ [Scenarios](#w591aac15c14b9c31c15)

## Actions<a name="w591aac15c14b9c31c13"></a>

### Create a group<a name="auto-scaling_CreateAutoScalingGroup_java_topic"></a>

The following code example shows how to create an Auto Scaling group\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/autoscale#readme)\. 
  

```
    public static void createAutoScalingGroup(AutoScalingClient autoScalingClient,
                                              String groupName,
                                              String launchTemplateName,
                                              String serviceLinkedRoleARN,
                                              String vpcZoneId) {

        try {
            AutoScalingWaiter waiter = autoScalingClient.waiter();
            LaunchTemplateSpecification templateSpecification = LaunchTemplateSpecification.builder()
                .launchTemplateName(launchTemplateName)
                .build();

            CreateAutoScalingGroupRequest request = CreateAutoScalingGroupRequest.builder()
                .autoScalingGroupName(groupName)
                .availabilityZones("us-east-1a")
                .launchTemplate(templateSpecification)
                .maxSize(1)
                .minSize(1)
                .vpcZoneIdentifier(vpcZoneId)
                .serviceLinkedRoleARN(serviceLinkedRoleARN)
                .build();

            autoScalingClient.createAutoScalingGroup(request);
            DescribeAutoScalingGroupsRequest groupsRequest = DescribeAutoScalingGroupsRequest.builder()
                .autoScalingGroupNames(groupName)
                .build();

            WaiterResponse<DescribeAutoScalingGroupsResponse> waiterResponse = waiter.waitUntilGroupExists(groupsRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);
            System.out.println("Auto Scaling Group created");

        } catch (AutoScalingException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [CreateAutoScalingGroup](https://docs.aws.amazon.com/goto/SdkForJavaV2/autoscaling-2011-01-01/CreateAutoScalingGroup) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a group<a name="auto-scaling_DeleteAutoScalingGroup_java_topic"></a>

The following code example shows how to delete an Auto Scaling group\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/autoscale#readme)\. 
  

```
    public static void deleteAutoScalingGroup(AutoScalingClient autoScalingClient, String groupName) {
        try {
            DeleteAutoScalingGroupRequest deleteAutoScalingGroupRequest = DeleteAutoScalingGroupRequest.builder()
                .autoScalingGroupName(groupName)
                .forceDelete(true)
                .build() ;

            autoScalingClient.deleteAutoScalingGroup(deleteAutoScalingGroupRequest) ;
            System.out.println("You successfully deleted "+groupName);

        } catch (AutoScalingException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteAutoScalingGroup](https://docs.aws.amazon.com/goto/SdkForJavaV2/autoscaling-2011-01-01/DeleteAutoScalingGroup) in *AWS SDK for Java 2\.x API Reference*\. 

### Get information about groups<a name="auto-scaling_DescribeAutoScalingGroups_java_topic"></a>

The following code example shows how to get information about Auto Scaling groups\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/autoscale#readme)\. 
  

```
    public static String getAutoScaling( AutoScalingClient autoScalingClient, String groupName) {
        try{
            String instanceId = "";
            DescribeAutoScalingGroupsRequest scalingGroupsRequest = DescribeAutoScalingGroupsRequest.builder()
                .autoScalingGroupNames(groupName)
                .build();

            DescribeAutoScalingGroupsResponse response = autoScalingClient.describeAutoScalingGroups(scalingGroupsRequest);
            List<AutoScalingGroup> groups = response.autoScalingGroups();
            for (AutoScalingGroup group: groups) {
                System.out.println("The group name is " + group.autoScalingGroupName());
                System.out.println("The group ARN is " + group.autoScalingGroupARN());

                List<Instance> instances = group.instances();
                for (Instance instance : instances) {
                    instanceId = instance.instanceId();
                }
            }
            return instanceId;
        } catch (AutoScalingException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [DescribeAutoScalingGroups](https://docs.aws.amazon.com/goto/SdkForJavaV2/autoscaling-2011-01-01/DescribeAutoScalingGroups) in *AWS SDK for Java 2\.x API Reference*\. 

## Scenarios<a name="w591aac15c14b9c31c15"></a>

### Manage groups and instances<a name="auto-scaling_Scenario_GroupsAndInstances_java_topic"></a>

The following code example shows how to:
+ Create an Amazon EC2 Auto Scaling group and configure it with a launch template and Availability Zones\.
+ Get information about the group and running instances\.
+ Enable Amazon CloudWatch metrics collection on the group\.
+ Update the desired capacity of the group and wait for an instance to start\.
+ Terminate an instance in the group\.
+ List scaling activities that occur in response to user requests and capacity changes\.
+ Get statistics for CloudWatch metrics that are collected during the example\.
+ Stop collecting metrics, terminate all instances, and delete the group\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/autoscale#readme)\. 
  

```
public class AutoScalingScenario {

    public static void main(String[] args) throws InterruptedException {
        final String usage = "\n" +
            "Usage:\n" +
            "    <groupName> <launchTemplateName> <serviceLinkedRoleARN> <vpcZoneId>\n\n" +
            "Where:\n" +
            "    groupName - The name of the Auto Scaling group.\n" +
            "    launchTemplateName - The name of the launch template. \n" +
            "    serviceLinkedRoleARN - The Amazon Resource Name (ARN) of the service-linked role that the Auto Scaling group uses.\n" +
            "    vpcZoneId - A subnet Id for a virtual private cloud (VPC) where instances in the Auto Scaling group can be created.\n" ;

        if (args.length != 4) {
            System.out.println(usage);
            System.exit(1);
        }

        String groupName = args[0];
        String launchTemplateName = args[1];
        String serviceLinkedRoleARN = args[2];
        String vpcZoneId = args[3];
        AutoScalingClient autoScalingClient = AutoScalingClient.builder()
            .region(Region.US_EAST_1)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

        System.out.println("**** Create an Auto Scaling group named "+groupName);
        createAutoScalingGroup(autoScalingClient, groupName, launchTemplateName, serviceLinkedRoleARN, vpcZoneId);

        System.out.println("Wait 1 min for the resources, including the instance. Otherwise, an empty instance Id is returned");
        Thread.sleep(60000);

        System.out.println("**** Get Auto Scale group Id value");
        String instanceId = getSpecificAutoScalingGroups(autoScalingClient, groupName);
        if (instanceId.compareTo("") ==0) {
            System.out.println("Error - no instance Id value");
            System.exit(1);
        } else {
            System.out.println("The instance Id value is "+instanceId);
        }

        System.out.println("**** Describe Auto Scaling with the Id value "+instanceId);
        describeAutoScalingInstance( autoScalingClient, instanceId);

        System.out.println("**** Enable metrics collection "+instanceId);
        enableMetricsCollection(autoScalingClient, groupName);

        System.out.println("**** Update an Auto Scaling group to update max size to 3");
        updateAutoScalingGroup(autoScalingClient, groupName, launchTemplateName, serviceLinkedRoleARN);

        System.out.println("**** Describe all Auto Scaling groups to show the current state of the groups");
        describeAutoScalingGroups(autoScalingClient, groupName);

        System.out.println("**** Describe account details");
        describeAccountLimits(autoScalingClient);

        System.out.println("Wait 1 min for the resources, including the instance. Otherwise, an empty instance Id is returned");
        Thread.sleep(60000);

        System.out.println("**** Set desired capacity to 2");
        setDesiredCapacity(autoScalingClient, groupName);

        System.out.println("**** Get the two instance Id values and state");
        getSpecificAutoScalingGroups(autoScalingClient, groupName);

        System.out.println("**** List the scaling activities that have occurred for the group");
        describeScalingActivities(autoScalingClient, groupName);

        System.out.println("**** Terminate an instance in the Auto Scaling group");
        terminateInstanceInAutoScalingGroup(autoScalingClient, instanceId);

        System.out.println("**** Stop the metrics collection");
        disableMetricsCollection(autoScalingClient, groupName);

        System.out.println("**** Delete the Auto Scaling group");
        deleteAutoScalingGroup(autoScalingClient, groupName);
        autoScalingClient.close();
    }

    public static void describeScalingActivities(AutoScalingClient autoScalingClient, String groupName) {
        try {
            DescribeScalingActivitiesRequest scalingActivitiesRequest = DescribeScalingActivitiesRequest.builder()
                .autoScalingGroupName(groupName)
                .maxRecords(10)
                .build();

            DescribeScalingActivitiesResponse response = autoScalingClient.describeScalingActivities(scalingActivitiesRequest);
            List<Activity> activities = response.activities();
            for (Activity activity: activities) {
                System.out.println("The activity Id is "+activity.activityId());
                System.out.println("The activity details are "+activity.details());
            }

        } catch (AutoScalingException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void setDesiredCapacity(AutoScalingClient autoScalingClient, String groupName) {
        try {
            SetDesiredCapacityRequest capacityRequest = SetDesiredCapacityRequest.builder()
                .autoScalingGroupName(groupName)
                .desiredCapacity(2)
                .build();

            autoScalingClient.setDesiredCapacity(capacityRequest);
            System.out.println("You have set the DesiredCapacity to 2");

        } catch (AutoScalingException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void createAutoScalingGroup(AutoScalingClient autoScalingClient,
                                              String groupName,
                                              String launchTemplateName,
                                              String serviceLinkedRoleARN,
                                              String vpcZoneId) {
        try {
            AutoScalingWaiter waiter = autoScalingClient.waiter();
            LaunchTemplateSpecification templateSpecification = LaunchTemplateSpecification.builder()
                .launchTemplateName(launchTemplateName)
                .build();

            CreateAutoScalingGroupRequest request = CreateAutoScalingGroupRequest.builder()
                .autoScalingGroupName(groupName)
                .availabilityZones("us-east-1a")
                .launchTemplate(templateSpecification)
                .maxSize(1)
                .minSize(1)
                .vpcZoneIdentifier(vpcZoneId)
                .serviceLinkedRoleARN(serviceLinkedRoleARN)
                .build();

            autoScalingClient.createAutoScalingGroup(request);
            DescribeAutoScalingGroupsRequest groupsRequest = DescribeAutoScalingGroupsRequest.builder()
                .autoScalingGroupNames(groupName)
                .build();

            WaiterResponse<DescribeAutoScalingGroupsResponse> waiterResponse = waiter.waitUntilGroupExists(groupsRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);
            System.out.println("Auto Scaling Group created");

        } catch (AutoScalingException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void describeAutoScalingInstance( AutoScalingClient autoScalingClient, String id) {
        try {
            DescribeAutoScalingInstancesRequest describeAutoScalingInstancesRequest = DescribeAutoScalingInstancesRequest.builder()
                .instanceIds(id)
                .build();

            DescribeAutoScalingInstancesResponse response = autoScalingClient.describeAutoScalingInstances(describeAutoScalingInstancesRequest);
            List<AutoScalingInstanceDetails> instances = response.autoScalingInstances();
            for (AutoScalingInstanceDetails instance:instances ) {
                System.out.println("The instance lifecycle state is: "+instance.lifecycleState());
            }

        } catch (AutoScalingException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void describeAutoScalingGroups(AutoScalingClient autoScalingClient, String groupName) {
        try {
            DescribeAutoScalingGroupsRequest groupsRequest = DescribeAutoScalingGroupsRequest.builder()
                .autoScalingGroupNames(groupName)
                .maxRecords(10)
                .build();

            DescribeAutoScalingGroupsResponse response = autoScalingClient.describeAutoScalingGroups(groupsRequest);
            List<AutoScalingGroup> groups = response.autoScalingGroups();
            for (AutoScalingGroup group: groups) {
                System.out.println("*** The service to use for the health checks: "+ group.healthCheckType());
            }

        } catch (AutoScalingException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static String getSpecificAutoScalingGroups(AutoScalingClient autoScalingClient, String groupName) {
        try{
            String instanceId = "";
            DescribeAutoScalingGroupsRequest scalingGroupsRequest = DescribeAutoScalingGroupsRequest.builder()
                .autoScalingGroupNames(groupName)
                .build();

            DescribeAutoScalingGroupsResponse response = autoScalingClient.describeAutoScalingGroups(scalingGroupsRequest);
            List<AutoScalingGroup> groups = response.autoScalingGroups();
            for (AutoScalingGroup group: groups) {
                System.out.println("The group name is " + group.autoScalingGroupName());
                System.out.println("The group ARN is " + group.autoScalingGroupARN());
                List<Instance> instances = group.instances();

                for (Instance instance : instances) {
                    instanceId = instance.instanceId();
                    System.out.println("The instance id is " + instanceId);
                    System.out.println("The lifecycle state is " +instance.lifecycleState());
                }
            }

            return instanceId ;
        } catch (AutoScalingException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "" ;
    }

    public static void enableMetricsCollection(AutoScalingClient autoScalingClient, String groupName) {
        try {

            EnableMetricsCollectionRequest collectionRequest = EnableMetricsCollectionRequest.builder()
                .autoScalingGroupName(groupName)
                .metrics("GroupMaxSize")
                .granularity("1Minute")
                .build();

            autoScalingClient.enableMetricsCollection(collectionRequest);
            System.out.println("The enable metrics collection operation was successful");

        } catch (AutoScalingException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void disableMetricsCollection(AutoScalingClient autoScalingClient, String groupName) {
        try {
            DisableMetricsCollectionRequest disableMetricsCollectionRequest = DisableMetricsCollectionRequest.builder()
                .autoScalingGroupName(groupName)
                .metrics("GroupMaxSize")
                .build();

            autoScalingClient.disableMetricsCollection(disableMetricsCollectionRequest);
            System.out.println("The disable metrics collection operation was successful");

        } catch (AutoScalingException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void describeAccountLimits(AutoScalingClient autoScalingClient) {
        try {
            DescribeAccountLimitsResponse response = autoScalingClient.describeAccountLimits();
            System.out.println("The max number of auto scaling groups is "+response.maxNumberOfAutoScalingGroups());
            System.out.println("The current number of auto scaling groups is "+response.numberOfAutoScalingGroups());

        } catch (AutoScalingException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void updateAutoScalingGroup(AutoScalingClient autoScalingClient, String groupName, String launchTemplateName, String serviceLinkedRoleARN) {
        try {
            AutoScalingWaiter waiter = autoScalingClient.waiter();
            LaunchTemplateSpecification templateSpecification = LaunchTemplateSpecification.builder()
                .launchTemplateName(launchTemplateName)
                .build();

            UpdateAutoScalingGroupRequest groupRequest = UpdateAutoScalingGroupRequest.builder()
                .maxSize(3)
                .serviceLinkedRoleARN(serviceLinkedRoleARN)
                .autoScalingGroupName(groupName)
                .launchTemplate(templateSpecification)
                .build();

            autoScalingClient.updateAutoScalingGroup(groupRequest);
            DescribeAutoScalingGroupsRequest groupsRequest = DescribeAutoScalingGroupsRequest.builder()
                .autoScalingGroupNames(groupName)
                .build();

            WaiterResponse<DescribeAutoScalingGroupsResponse> waiterResponse = waiter.waitUntilGroupInService(groupsRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);
            System.out.println("You successfully updated the auto scaling group  "+groupName);

        } catch (AutoScalingException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void terminateInstanceInAutoScalingGroup(AutoScalingClient autoScalingClient, String instanceId){
        try {
            TerminateInstanceInAutoScalingGroupRequest request = TerminateInstanceInAutoScalingGroupRequest.builder()
                .instanceId(instanceId)
                .shouldDecrementDesiredCapacity(false)
                .build();

            autoScalingClient.terminateInstanceInAutoScalingGroup(request);
            System.out.println("You have terminated instance "+instanceId);

        } catch (AutoScalingException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void deleteAutoScalingGroup(AutoScalingClient autoScalingClient, String groupName) {
        try {
            DeleteAutoScalingGroupRequest deleteAutoScalingGroupRequest = DeleteAutoScalingGroupRequest.builder()
                .autoScalingGroupName(groupName)
                .forceDelete(true)
                .build() ;

            autoScalingClient.deleteAutoScalingGroup(deleteAutoScalingGroupRequest) ;
            System.out.println("You successfully deleted "+groupName);

        } catch (AutoScalingException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
```
+ For API details, see the following topics in *AWS SDK for Java 2\.x API Reference*\.
  + [CreateAutoScalingGroup](https://docs.aws.amazon.com/goto/SdkForJavaV2/autoscaling-2011-01-01/CreateAutoScalingGroup)
  + [DeleteAutoScalingGroup](https://docs.aws.amazon.com/goto/SdkForJavaV2/autoscaling-2011-01-01/DeleteAutoScalingGroup)
  + [DescribeAutoScalingGroups](https://docs.aws.amazon.com/goto/SdkForJavaV2/autoscaling-2011-01-01/DescribeAutoScalingGroups)
  + [DescribeAutoScalingInstances](https://docs.aws.amazon.com/goto/SdkForJavaV2/autoscaling-2011-01-01/DescribeAutoScalingInstances)
  + [DescribeScalingActivities](https://docs.aws.amazon.com/goto/SdkForJavaV2/autoscaling-2011-01-01/DescribeScalingActivities)
  + [DisableMetricsCollection](https://docs.aws.amazon.com/goto/SdkForJavaV2/autoscaling-2011-01-01/DisableMetricsCollection)
  + [EnableMetricsCollection](https://docs.aws.amazon.com/goto/SdkForJavaV2/autoscaling-2011-01-01/EnableMetricsCollection)
  + [SetDesiredCapacity](https://docs.aws.amazon.com/goto/SdkForJavaV2/autoscaling-2011-01-01/SetDesiredCapacity)
  + [TerminateInstanceInAutoScalingGroup](https://docs.aws.amazon.com/goto/SdkForJavaV2/autoscaling-2011-01-01/TerminateInstanceInAutoScalingGroup)
  + [UpdateAutoScalingGroup](https://docs.aws.amazon.com/goto/SdkForJavaV2/autoscaling-2011-01-01/UpdateAutoScalingGroup)