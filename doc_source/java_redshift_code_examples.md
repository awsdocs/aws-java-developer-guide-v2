--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Amazon Redshift examples using SDK for Java 2\.x<a name="java_redshift_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Amazon Redshift\.

*Actions* are code excerpts that show you how to call individual Amazon Redshift functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple Amazon Redshift functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w620aac15c13b9c57c13)

## Actions<a name="w620aac15c13b9c57c13"></a>

### Create a cluster<a name="redshift_CreateCluster_java_topic"></a>

The following code example shows how to create an Amazon Redshift cluster\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/redshift#readme)\. 
Create the cluster\.  

```
    public static void createCluster(RedshiftClient redshiftClient, String clusterId, String masterUsername, String masterUserPassword ) {

        try {
            CreateClusterRequest clusterRequest = CreateClusterRequest.builder()
                .clusterIdentifier(clusterId)
                .masterUsername(masterUsername) // set the user name here
                .masterUserPassword(masterUserPassword) // set the user password here
                .nodeType("ds2.xlarge")
                .publiclyAccessible(true)
                .numberOfNodes(2)
                .build();

        CreateClusterResponse clusterResponse = redshiftClient.createCluster(clusterRequest);
        System.out.println("Created cluster " + clusterResponse.cluster().clusterIdentifier());

       } catch (RedshiftException e) {

           System.err.println(e.getMessage());
           System.exit(1);
       }
    }
```
+  For API details, see [CreateCluster](https://docs.aws.amazon.com/goto/SdkForJavaV2/redshift-2012-12-01/CreateCluster) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a cluster<a name="redshift_DeleteCluster_java_topic"></a>

The following code example shows how to delete an Amazon Redshift cluster\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/redshift#readme)\. 
Delete the cluster\.  

```
    public static void deleteRedshiftCluster(RedshiftClient redshiftClient, String clusterId) {

        try {
            DeleteClusterRequest deleteClusterRequest = DeleteClusterRequest.builder()
                .clusterIdentifier(clusterId)
                .skipFinalClusterSnapshot(true)
                .build();

            DeleteClusterResponse response = redshiftClient.deleteCluster(deleteClusterRequest);
            System.out.println("The status is "+response.cluster().clusterStatus());

        } catch (RedshiftException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteCluster](https://docs.aws.amazon.com/goto/SdkForJavaV2/redshift-2012-12-01/DeleteCluster) in *AWS SDK for Java 2\.x API Reference*\. 

### Describe your clusters<a name="redshift_DescribeClusters_java_topic"></a>

The following code example shows how to describe your Amazon Redshift clusters\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/redshift#readme)\. 
Describe the cluster\.  

```
    public static void describeRedshiftClusters(RedshiftClient redshiftClient) {

       try {
            DescribeClustersResponse clusterResponse = redshiftClient.describeClusters();
            List<Cluster> clusterList = clusterResponse.clusters();
            for (Cluster cluster: clusterList) {
                System.out.println("Cluster database name is: "+cluster.dbName());
                System.out.println("Cluster status is: "+cluster.clusterStatus());
            }

       } catch (RedshiftException e) {
           System.err.println(e.getMessage());
           System.exit(1);
       }
   }
```
+  For API details, see [DescribeClusters](https://docs.aws.amazon.com/goto/SdkForJavaV2/redshift-2012-12-01/DescribeClusters) in *AWS SDK for Java 2\.x API Reference*\. 

### Modify a cluster<a name="redshift_ModifyCluster_java_topic"></a>

The following code example shows how to modify an Amazon Redshift cluster\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/redshift#readme)\. 
Modify a cluster\.  

```
     public static void modifyCluster(RedshiftClient redshiftClient, String clusterId) {

        try {
            ModifyClusterRequest modifyClusterRequest = ModifyClusterRequest.builder()
                .clusterIdentifier(clusterId)
                 .preferredMaintenanceWindow("wed:07:30-wed:08:00")
                .build();

            ModifyClusterResponse clusterResponse = redshiftClient.modifyCluster(modifyClusterRequest);
            System.out.println("The modified cluster was successfully modified and has "+ clusterResponse.cluster().preferredMaintenanceWindow() +" as the maintenance window");

        } catch (RedshiftException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ModifyCluster](https://docs.aws.amazon.com/goto/SdkForJavaV2/redshift-2012-12-01/ModifyCluster) in *AWS SDK for Java 2\.x API Reference*\. 