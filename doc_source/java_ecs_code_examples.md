# Amazon ECS examples using SDK for Java 2\.x<a name="java_ecs_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Amazon ECS\.

*Actions* are code excerpts that show you how to call individual service functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple functions within the same service\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#actions)

## Actions<a name="actions"></a>

### Create a cluster<a name="ecs_CreateCluster_java_topic"></a>

The following code example shows how to create an Amazon ECS cluster\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ecs#readme)\. 
  

```
    public static String createGivenCluster( EcsClient ecsClient, String clusterName) {

        try {
            ExecuteCommandConfiguration commandConfiguration = ExecuteCommandConfiguration.builder()
                .logging(ExecuteCommandLogging.DEFAULT)
                .build();

            ClusterConfiguration clusterConfiguration = ClusterConfiguration.builder()
                .executeCommandConfiguration(commandConfiguration)
                .build();

            CreateClusterRequest clusterRequest = CreateClusterRequest.builder()
                .clusterName(clusterName)
                .configuration(clusterConfiguration)
                .build();

            CreateClusterResponse response = ecsClient.createCluster(clusterRequest) ;
            return response.cluster().clusterArn();

        } catch (EcsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateCluster](https://docs.aws.amazon.com/goto/SdkForJavaV2/ecs-2014-11-13/CreateCluster) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a service<a name="ecs_CreateService_java_topic"></a>

The following code example shows how to create an Amazon ECS service\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ecs#readme)\. 
  

```
    public static String createNewService(EcsClient ecsClient,
                                          String clusterName,
                                          String serviceName,
                                          String securityGroups,
                                          String subnets,
                                          String taskDefinition) {

        try {
            AwsVpcConfiguration vpcConfiguration = AwsVpcConfiguration.builder()
                .securityGroups(securityGroups)
                .subnets(subnets)
                .build();

            NetworkConfiguration configuration = NetworkConfiguration.builder()
                .awsvpcConfiguration(vpcConfiguration)
                .build();

            CreateServiceRequest serviceRequest = CreateServiceRequest.builder()
                .cluster(clusterName)
                .networkConfiguration(configuration)
                .desiredCount(1)
                .launchType(LaunchType.FARGATE)
                .serviceName(serviceName)
                .taskDefinition(taskDefinition)
                .build();

            CreateServiceResponse response = ecsClient.createService(serviceRequest) ;
            return response.service().serviceArn();

        } catch (EcsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateService](https://docs.aws.amazon.com/goto/SdkForJavaV2/ecs-2014-11-13/CreateService) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a service<a name="ecs_DeleteService_java_topic"></a>

The following code example shows how to delete an Amazon ECS service\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ecs#readme)\. 
  

```
    public static void deleteSpecificService(EcsClient ecsClient, String clusterName, String serviceArn) {

        try {
            DeleteServiceRequest serviceRequest = DeleteServiceRequest.builder()
                .cluster(clusterName)
                .service(serviceArn)
                .build();

            ecsClient.deleteService(serviceRequest);
            System.out.println("The Service was successfully deleted");

        } catch (EcsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteService](https://docs.aws.amazon.com/goto/SdkForJavaV2/ecs-2014-11-13/DeleteService) in *AWS SDK for Java 2\.x API Reference*\. 

### Describe clusters<a name="ecs_DescribeClusters_java_topic"></a>

The following code example shows how to describe your Amazon ECS clusters\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ecs#readme)\. 
  

```
    public static void descCluster(EcsClient ecsClient, String clusterArn) {

        try {
            DescribeClustersRequest clustersRequest = DescribeClustersRequest.builder()
                .clusters(clusterArn)
                .build();

            DescribeClustersResponse response = ecsClient.describeClusters(clustersRequest);
            List<Cluster> clusters = response.clusters();
            for (Cluster cluster: clusters) {
                System.out.println("The cluster name is "+cluster.clusterName());
            }

        } catch (EcsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DescribeClusters](https://docs.aws.amazon.com/goto/SdkForJavaV2/ecs-2014-11-13/DescribeClusters) in *AWS SDK for Java 2\.x API Reference*\. 

### Describe tasks<a name="ecs_DescribeTasks_java_topic"></a>

The following code example shows how to describe your Amazon ECS tasks\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ecs#readme)\. 
  

```
    public static void getAllTasks(EcsClient ecsClient, String clusterArn, String taskId) {

        try {
            DescribeTasksRequest tasksRequest = DescribeTasksRequest.builder()
               .cluster(clusterArn)
               .tasks(taskId)
               .build();

            DescribeTasksResponse response = ecsClient.describeTasks(tasksRequest);
            List<Task> tasks = response.tasks();
            for (Task task: tasks) {
                System.out.println("The task ARN is "+task.taskDefinitionArn());
            }

        } catch (EcsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DescribeTasks](https://docs.aws.amazon.com/goto/SdkForJavaV2/ecs-2014-11-13/DescribeTasks) in *AWS SDK for Java 2\.x API Reference*\. 

### List clusters<a name="ecs_ListClusters_java_topic"></a>

The following code example shows how to list your Amazon ECS clusters\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ecs#readme)\. 
  

```
    public static void listAllClusters(EcsClient ecsClient) {

        try {
            ListClustersResponse response = ecsClient.listClusters();
            List<String> clusters = response.clusterArns();
            for (String cluster: clusters) {
                System.out.println("The cluster arn is "+cluster);
            }

        } catch (EcsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListClusters](https://docs.aws.amazon.com/goto/SdkForJavaV2/ecs-2014-11-13/ListClusters) in *AWS SDK for Java 2\.x API Reference*\. 

### Update a service<a name="ecs_UpdateService_java_topic"></a>

The following code example shows how to update an Amazon ECS service\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ecs#readme)\. 
  

```
    public static void updateSpecificService( EcsClient ecsClient, String clusterName, String serviceArn) {

        try {
            UpdateServiceRequest serviceRequest = UpdateServiceRequest.builder()
                .cluster(clusterName)
                .service(serviceArn)
                .desiredCount(0)
                .build();

            ecsClient.updateService(serviceRequest);
            System.out.println("The service was modified");

        } catch (EcsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [UpdateService](https://docs.aws.amazon.com/goto/SdkForJavaV2/ecs-2014-11-13/UpdateService) in *AWS SDK for Java 2\.x API Reference*\. 