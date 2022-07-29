--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Amazon RDS examples using SDK for Java 2\.x<a name="java_rds_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Amazon RDS\.

*Actions* are code excerpts that show you how to call individual Amazon RDS functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple Amazon RDS functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w591aac15c14b9c53c13)

## Actions<a name="w591aac15c14b9c53c13"></a>

### Create an instance<a name="rds_CreateDBInstance_java_topic"></a>

The following code example shows how to create an Amazon RDS instance and wait for it to be in an available state\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/rds#readme)\. 
  

```
    public static void createDatabaseInstance(RdsClient rdsClient,
                                                  String dbInstanceIdentifier,
                                                  String dbName,
                                                  String masterUsername,
                                                  String masterUserPassword) {

        try {
            CreateDbInstanceRequest instanceRequest = CreateDbInstanceRequest.builder()
                .dbInstanceIdentifier(dbInstanceIdentifier)
                .allocatedStorage(100)
                .dbName(dbName)
                .engine("mysql")
                .dbInstanceClass("db.m4.large")
                .engineVersion("8.0.15")
                .storageType("standard")
                .masterUsername(masterUsername)
                .masterUserPassword(masterUserPassword)
                .build();

            CreateDbInstanceResponse response = rdsClient.createDBInstance(instanceRequest);
            System.out.print("The status is " + response.dbInstance().dbInstanceStatus());

        } catch (RdsException e) {
           System.out.println(e.getLocalizedMessage());
           System.exit(1);
        }
    }

    // Waits until the database instance is available
    public static void waitForInstanceReady(RdsClient rdsClient, String dbInstanceIdentifier) {

        Boolean instanceReady = false;
        String instanceReadyStr = "";
        System.out.println("Waiting for instance to become available.");

        try {
            DescribeDbInstancesRequest instanceRequest = DescribeDbInstancesRequest.builder()
                .dbInstanceIdentifier(dbInstanceIdentifier)
                .build();

            // Loop until the cluster is ready
            while (!instanceReady) {

                DescribeDbInstancesResponse response = rdsClient.describeDBInstances(instanceRequest);
                List<DBInstance> instanceList = response.dbInstances();
                for (DBInstance instance : instanceList) {
                    instanceReadyStr = instance.dbInstanceStatus();
                    if (instanceReadyStr.contains("available"))
                        instanceReady = true;
                    else {
                        System.out.print(".");
                        Thread.sleep(sleepTime * 1000);
                    }
                }
            }
            System.out.println("Database instance is available!");

        } catch (RdsException | InterruptedException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [CreateDBInstance](https://docs.aws.amazon.com/goto/SdkForJavaV2/rds-2014-10-31/CreateDBInstance) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete an instance<a name="rds_DeleteDBInstance_java_topic"></a>

The following code example shows how to delete an Amazon RDS instance\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/rds#readme)\. 
  

```
    public static void deleteDatabaseInstance( RdsClient rdsClient, String dbInstanceIdentifier) {
        try {
            DeleteDbInstanceRequest deleteDbInstanceRequest = DeleteDbInstanceRequest.builder()
                .dbInstanceIdentifier(dbInstanceIdentifier)
                .deleteAutomatedBackups(true)
                .skipFinalSnapshot(true)
                .build();

            DeleteDbInstanceResponse response = rdsClient.deleteDBInstance(deleteDbInstanceRequest);
            System.out.print("The status of the database is " + response.dbInstance().dbInstanceStatus());

        } catch (RdsException e) {
            System.out.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteDBInstance](https://docs.aws.amazon.com/goto/SdkForJavaV2/rds-2014-10-31/DeleteDBInstance) in *AWS SDK for Java 2\.x API Reference*\. 

### Describe an instance<a name="rds_DescribeDBInstances_java_topic"></a>

The following code example shows how to describe an Amazon RDS instance\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/rds#readme)\. 
  

```
    public static void describeInstances(RdsClient rdsClient) {

        try {
            DescribeDbInstancesResponse response = rdsClient.describeDBInstances();
            List<DBInstance> instanceList = response.dbInstances();
            for (DBInstance instance: instanceList) {
                System.out.println("Instance ARN is: "+instance.dbInstanceArn());
                System.out.println("The Engine is " +instance.engine());
                System.out.println("Connection endpoint is" +instance.endpoint().address());
            }

        } catch (RdsException e) {
            System.out.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DescribeDBInstances](https://docs.aws.amazon.com/goto/SdkForJavaV2/rds-2014-10-31/DescribeDBInstances) in *AWS SDK for Java 2\.x API Reference*\. 

### Modify an instance<a name="rds_ModifyDBInstance_java_topic"></a>

The following code example shows how to modify an Amazon RDS instance\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/rds#readme)\. 
  

```
    public static void describeInstances(RdsClient rdsClient) {

        try {
            DescribeDbInstancesResponse response = rdsClient.describeDBInstances();
            List<DBInstance> instanceList = response.dbInstances();
            for (DBInstance instance: instanceList) {
                System.out.println("Instance ARN is: "+instance.dbInstanceArn());
                System.out.println("The Engine is " +instance.engine());
                System.out.println("Connection endpoint is" +instance.endpoint().address());
            }

        } catch (RdsException e) {
            System.out.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ModifyDBInstance](https://docs.aws.amazon.com/goto/SdkForJavaV2/rds-2014-10-31/ModifyDBInstance) in *AWS SDK for Java 2\.x API Reference*\. 

### Reboot an instance<a name="rds_RebootDBInstance_java_topic"></a>

The following code example shows how to reboot an Amazon RDS instance\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/rds#readme)\. 
  

```
    public static void describeInstances(RdsClient rdsClient) {

        try {
            DescribeDbInstancesResponse response = rdsClient.describeDBInstances();
            List<DBInstance> instanceList = response.dbInstances();
            for (DBInstance instance: instanceList) {
                System.out.println("Instance ARN is: "+instance.dbInstanceArn());
                System.out.println("The Engine is " +instance.engine());
                System.out.println("Connection endpoint is" +instance.endpoint().address());
            }

        } catch (RdsException e) {
            System.out.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [RebootDBInstance](https://docs.aws.amazon.com/goto/SdkForJavaV2/rds-2014-10-31/RebootDBInstance) in *AWS SDK for Java 2\.x API Reference*\. 

### Retrieve attributes<a name="rds_DescribeAccountAttributes_java_topic"></a>

The following code example shows how to retrieve attributes that belong to an Amazon RDS account\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/rds#readme)\. 
  

```
    public static void getAccountAttributes(RdsClient rdsClient) {

        try {
            DescribeAccountAttributesResponse response = rdsClient.describeAccountAttributes();
            List<AccountQuota> quotasList = response.accountQuotas();
            for (AccountQuota quotas: quotasList) {
                System.out.println("Name is: "+quotas.accountQuotaName());
                System.out.println("Max value is " +quotas.max());
            }

        } catch (RdsException e) {
            System.out.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DescribeAccountAttributes](https://docs.aws.amazon.com/goto/SdkForJavaV2/rds-2014-10-31/DescribeAccountAttributes) in *AWS SDK for Java 2\.x API Reference*\. 