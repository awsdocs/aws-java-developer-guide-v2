--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Amazon Personalize examples using SDK for Java 2\.x<a name="java_personalize_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Amazon Personalize\.

*Actions* are code excerpts that show you how to call individual Amazon Personalize functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple Amazon Personalize functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w620aac15c13b9c45c13)

## Actions<a name="w620aac15c13b9c45c13"></a>

### Create a batch interface job<a name="personalize_createBatchInferenceJob_java_topic"></a>

The following code example shows how to create a Amazon Personalize batch interface job\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static String createPersonalizeBatchInferenceJob(PersonalizeClient personalizeClient,
                                                            String solutionVersionArn,
                                                            String jobName,
                                                            String s3InputDataSourcePath,
                                                            String s3DataDestinationPath,
                                                            String roleArn,
                                                            String explorationWeight,
                                                            String explorationItemAgeCutOff) {

        long waitInMilliseconds = 60 * 1000;
        String status;
        String batchInferenceJobArn;

        try {

            // Set up data input and output parameters.
            S3DataConfig inputSource = S3DataConfig.builder()
                .path(s3InputDataSourcePath)
                .build();

            S3DataConfig outputDestination = S3DataConfig.builder()
                .path(s3DataDestinationPath)
                .build();

            BatchInferenceJobInput jobInput = BatchInferenceJobInput.builder()
                .s3DataSource(inputSource)
                .build();

            BatchInferenceJobOutput jobOutputLocation = BatchInferenceJobOutput.builder()
                .s3DataDestination(outputDestination)
                .build();

            // Optional code to build the User-Personalization specific item exploration config.
            HashMap<String, String> explorationConfig = new HashMap<>();

            explorationConfig.put("explorationWeight", explorationWeight);
            explorationConfig.put("explorationItemAgeCutOff", explorationItemAgeCutOff);

            BatchInferenceJobConfig jobConfig = BatchInferenceJobConfig.builder()
                .itemExplorationConfig(explorationConfig)
                .build();

            // End optional User-Personalization recipe specific code.

            CreateBatchInferenceJobRequest createBatchInferenceJobRequest = CreateBatchInferenceJobRequest.builder()
                .solutionVersionArn(solutionVersionArn)
                .jobInput(jobInput)
                .jobOutput(jobOutputLocation)
                .jobName(jobName)
                .roleArn(roleArn)
                .batchInferenceJobConfig(jobConfig)   // Optional
                .build();

            batchInferenceJobArn = personalizeClient.createBatchInferenceJob(createBatchInferenceJobRequest)
                 .batchInferenceJobArn();

            DescribeBatchInferenceJobRequest describeBatchInferenceJobRequest = DescribeBatchInferenceJobRequest.builder()
                .batchInferenceJobArn(batchInferenceJobArn)
                .build();

            long maxTime = Instant.now().getEpochSecond() + 3 * 60 * 60;
            while (Instant.now().getEpochSecond() < maxTime) {

                BatchInferenceJob batchInferenceJob = personalizeClient
                    .describeBatchInferenceJob(describeBatchInferenceJobRequest)
                    .batchInferenceJob();

                status = batchInferenceJob.status();
                System.out.println("Batch inference job status: " + status);

                if (status.equals("ACTIVE") || status.equals("CREATE FAILED")) {
                    break;
                }
                try {
                    Thread.sleep(waitInMilliseconds);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            return batchInferenceJobArn;

        } catch (PersonalizeException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
        }
        return "";
    }
```
+  For API details, see [CreateBatchInferenceJob](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/CreateBatchInferenceJob) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a campaign<a name="personalize_createCampaign_java_topic"></a>

The following code example shows how to create a Amazon Personalize campaign\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static void createPersonalCompaign(PersonalizeClient personalizeClient, String solutionVersionArn, String name) {

        try {
            CreateCampaignRequest createCampaignRequest = CreateCampaignRequest.builder()
                .minProvisionedTPS(1)
                .solutionVersionArn(solutionVersionArn)
                .name(name)
                .build();

            CreateCampaignResponse campaignResponse = personalizeClient.createCampaign(createCampaignRequest);
            System.out.println("The campaign ARN is "+campaignResponse.campaignArn());

        } catch (PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [CreateCampaign](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/CreateCampaign) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a dataset<a name="personalize_createDataset_java_topic"></a>

The following code example shows how to create a Amazon Personalize dataset\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static String createDataset(PersonalizeClient personalizeClient,
                                       String datasetName,
                                       String datasetGroupArn,
                                       String datasetType,
                                       String schemaArn) {
        try {
            CreateDatasetRequest request = CreateDatasetRequest.builder()
                .name(datasetName)
                .datasetGroupArn(datasetGroupArn)
                .datasetType(datasetType)
                .schemaArn(schemaArn)
                .build();

            String datasetArn = personalizeClient.createDataset(request)
                .datasetArn();
            System.out.println("Dataset " + datasetName + " created.");
            return datasetArn;

        } catch (PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateDataset](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/CreateDataset) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a dataset export job<a name="personalize_createDatasetExportJob_java_topic"></a>

The following code example shows how to create a Amazon Personalize dataset export job\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static String createDatasetExportJob(PersonalizeClient personalizeClient,
                                            String jobName,
                                            String datasetArn, 
                                            IngestionMode ingestionMode, 
                                            String roleArn,
                                            String s3BucketPath,
                                            String kmsKeyArn) {
        
        long waitInMilliseconds = 30 * 1000; // 30 seconds
        String status = null;

        try {

            S3DataConfig exportS3DataConfig = S3DataConfig.builder().path(s3BucketPath).kmsKeyArn(kmsKeyArn).build();
            DatasetExportJobOutput jobOutput = DatasetExportJobOutput.builder().s3DataDestination(exportS3DataConfig).build();

            CreateDatasetExportJobRequest createRequest = CreateDatasetExportJobRequest.builder()
                    .jobName(jobName)
                    .datasetArn(datasetArn)
                    .ingestionMode(ingestionMode)
                    .jobOutput(jobOutput)
                    .roleArn(roleArn)
                    .build();

            String datasetExportJobArn = personalizeClient.createDatasetExportJob(createRequest).datasetExportJobArn();
            
            DescribeDatasetExportJobRequest describeDatasetExportJobRequest = DescribeDatasetExportJobRequest.builder()
                .datasetExportJobArn(datasetExportJobArn)
                .build();

            long maxTime = Instant.now().getEpochSecond() + 3 * 60 * 60;

            while (Instant.now().getEpochSecond() < maxTime) {

                DatasetExportJob datasetExportJob = personalizeClient.describeDatasetExportJob(describeDatasetExportJobRequest)
                        .datasetExportJob();

                status = datasetExportJob.status();
                System.out.println("Export job status: " + status);
                
                if (status.equals("ACTIVE") || status.equals("CREATE FAILED")) {
                   return status;
                }
                try {
                    Thread.sleep(waitInMilliseconds);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (PersonalizeException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
        }
        return "";
    }
```
+  For API details, see [CreateDatasetExportJob](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/CreateDatasetExportJob) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a dataset group<a name="personalize_createDatasetGroup_java_topic"></a>

The following code example shows how to create a Amazon Personalize dataset group\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static String createDatasetGroup(PersonalizeClient personalizeClient, String datasetGroupName) {

        try {
            CreateDatasetGroupRequest createDatasetGroupRequest = CreateDatasetGroupRequest.builder()
                    .name(datasetGroupName)
                    .build();
            return personalizeClient.createDatasetGroup(createDatasetGroupRequest).datasetGroupArn();
        } catch (PersonalizeException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
        }
        return "";
    }
```
Create a domain dataset group\.  

```
    public static String createDomainDatasetGroup(PersonalizeClient personalizeClient,
                                                  String datasetGroupName,
                                                  String domain) {

        try {
            CreateDatasetGroupRequest createDatasetGroupRequest = CreateDatasetGroupRequest.builder()
                    .name(datasetGroupName)
                    .domain(domain)
                    .build();
            return personalizeClient.createDatasetGroup(createDatasetGroupRequest).datasetGroupArn();
        } catch (PersonalizeException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
        }
        return "";
    }
```
+  For API details, see [CreateDatasetGroup](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/CreateDatasetGroup) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a dataset import job<a name="personalize_createDatasetImportJob_java_topic"></a>

The following code example shows how to create a Amazon Personalize dataset import job\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static String createPersonalizeDatasetImportJob(PersonalizeClient personalizeClient,
                                                           String jobName,
                                                           String datasetArn,
                                                           String s3BucketPath,
                                                           String roleArn) {

        long waitInMilliseconds = 60 * 1000;
        String status;
        String datasetImportJobArn;

        try {
            DataSource importDataSource = DataSource.builder()
                    .dataLocation(s3BucketPath)
                    .build();

            CreateDatasetImportJobRequest createDatasetImportJobRequest = CreateDatasetImportJobRequest.builder()
                    .datasetArn(datasetArn)
                    .dataSource(importDataSource)
                    .jobName(jobName)
                    .roleArn(roleArn)
                    .build();

            datasetImportJobArn = personalizeClient.createDatasetImportJob(createDatasetImportJobRequest)
                    .datasetImportJobArn();
            DescribeDatasetImportJobRequest describeDatasetImportJobRequest = DescribeDatasetImportJobRequest.builder()
                    .datasetImportJobArn(datasetImportJobArn)
                    .build();

            long maxTime = Instant.now().getEpochSecond() + 3 * 60 * 60;

            while (Instant.now().getEpochSecond() < maxTime) {

                DatasetImportJob datasetImportJob = personalizeClient
                        .describeDatasetImportJob(describeDatasetImportJobRequest)
                        .datasetImportJob();

                status = datasetImportJob.status();
                System.out.println("Dataset import job status: " + status);

                if (status.equals("ACTIVE") || status.equals("CREATE FAILED")) {
                    break;
                }
                try {
                    Thread.sleep(waitInMilliseconds);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            return datasetImportJobArn;

        } catch (PersonalizeException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
        }
        return "";
    }
```
+  For API details, see [CreateDatasetImportJob](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/CreateDatasetImportJob) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a domain schema<a name="personalize_createDomainSchema_java_topic"></a>

The following code example shows how to create a Amazon Personalize domain schema\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static String createDomainSchema(PersonalizeClient personalizeClient, String schemaName, String domain, String filePath) {

        String schema = null;
        try {
            schema = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            CreateSchemaRequest createSchemaRequest = CreateSchemaRequest.builder()
                    .name(schemaName)
                    .domain(domain)
                    .schema(schema)
                    .build();

            String schemaArn = personalizeClient.createSchema(createSchemaRequest).schemaArn();

            System.out.println("Schema arn: " + schemaArn);

            return schemaArn;

        } catch (PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateSchema](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/CreateSchema) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a filter<a name="personalize_createFilter_java_topic"></a>

The following code example shows how to create a Amazon Personalize filter\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static String createFilter(PersonalizeClient personalizeClient,
                                       String filterName,
                                       String datasetGroupArn,
                                       String filterExpression) {
        try {
            CreateFilterRequest request = CreateFilterRequest.builder()
                    .name(filterName)
                    .datasetGroupArn(datasetGroupArn)
                    .filterExpression(filterExpression)
                    .build();

            return personalizeClient.createFilter(request).filterArn();
        }
        catch(PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateFilter](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/CreateFilter) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a recommender<a name="personalize_createRecommender_java_topic"></a>

The following code example shows how to create a Amazon Personalize recommender\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static String createRecommender(PersonalizeClient personalizeClient,
                                           String name,
                                           String datasetGroupArn,
                                           String recipeArn) {

        long maxTime = 0;
        long waitInMilliseconds = 30 * 1000; // 30 seconds
        String recommenderStatus = "";

        try {
                CreateRecommenderRequest createRecommenderRequest = CreateRecommenderRequest.builder()
                        .datasetGroupArn(datasetGroupArn)
                        .name(name)
                        .recipeArn(recipeArn)
                        .build();

                CreateRecommenderResponse recommenderResponse = personalizeClient.createRecommender(createRecommenderRequest);
                String recommenderArn = recommenderResponse.recommenderArn();
                System.out.println("The recommender ARN is " + recommenderArn);

                DescribeRecommenderRequest describeRecommenderRequest = DescribeRecommenderRequest.builder()
                        .recommenderArn(recommenderArn)
                        .build();

                maxTime = Instant.now().getEpochSecond() + 3 * 60 * 60;

                while (Instant.now().getEpochSecond() < maxTime) {

                    recommenderStatus = personalizeClient.describeRecommender(describeRecommenderRequest).recommender().status();
                    System.out.println("Recommender status: " + recommenderStatus);

                    if (recommenderStatus.equals("ACTIVE") || recommenderStatus.equals("CREATE FAILED")) {
                        break;
                    }
                    try {
                        Thread.sleep(waitInMilliseconds);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
                return recommenderArn;

        } catch(PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateRecommender](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/CreateRecommender) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a schema<a name="personalize_createSchema_java_topic"></a>

The following code example shows how to create a Amazon Personalize schema\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static String createSchema(PersonalizeClient personalizeClient, String schemaName, String filePath) {

        String schema = null;
        try {
            schema = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            CreateSchemaRequest createSchemaRequest = CreateSchemaRequest.builder()
                    .name(schemaName)
                    .schema(schema)
                    .build();

            String schemaArn = personalizeClient.createSchema(createSchemaRequest).schemaArn();

            System.out.println("Schema arn: " + schemaArn);

            return schemaArn;

        } catch (PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateSchema](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/CreateSchema) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a solution<a name="personalize_createSolution_java_topic"></a>

The following code example shows how to create a Amazon Personalize solution\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static String createPersonalizeSolution(PersonalizeClient personalizeClient,
                                                 String datasetGroupArn,
                                                 String solutionName,
                                                 String recipeArn) {

        try {
            CreateSolutionRequest solutionRequest = CreateSolutionRequest.builder()
                .name(solutionName)
                .datasetGroupArn(datasetGroupArn)
                .recipeArn(recipeArn)
                .build();

            CreateSolutionResponse solutionResponse = personalizeClient.createSolution(solutionRequest);
            return solutionResponse.solutionArn();

        } catch (PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateSolution](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/CreateSolution) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a solution version<a name="personalize_createSolutionVersion_java_topic"></a>

The following code example shows how to create a Amazon Personalize solution\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static String createPersonalizeSolutionVersion(PersonalizeClient personalizeClient, String solutionArn) {
        long maxTime = 0;
        long waitInMilliseconds = 30 * 1000; // 30 seconds
        String solutionStatus = "";
        String solutionVersionStatus = "";
        String solutionVersionArn = "";

        try {
            DescribeSolutionRequest describeSolutionRequest = DescribeSolutionRequest.builder()
                .solutionArn(solutionArn)
                .build();
            
            maxTime = Instant.now().getEpochSecond() + 3 * 60 * 60;

            // Wait until solution is active. 
            while (Instant.now().getEpochSecond() < maxTime) {

                solutionStatus = personalizeClient.describeSolution(describeSolutionRequest).solution().status();
                System.out.println("Solution status: " + solutionStatus);

                if (solutionStatus.equals("ACTIVE") || solutionStatus.equals("CREATE FAILED")) {
                    break;
                }
                try {
                    Thread.sleep(waitInMilliseconds);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }

            if (solutionStatus.equals("ACTIVE")) {

                CreateSolutionVersionRequest createSolutionVersionRequest = CreateSolutionVersionRequest.builder()
                    .solutionArn(solutionArn)
                    .build();

                CreateSolutionVersionResponse createSolutionVersionResponse = personalizeClient.createSolutionVersion(createSolutionVersionRequest);
                solutionVersionArn = createSolutionVersionResponse.solutionVersionArn();

                System.out.println("Solution version ARN: " + solutionVersionArn);

                DescribeSolutionVersionRequest describeSolutionVersionRequest = DescribeSolutionVersionRequest.builder()
                    .solutionVersionArn(solutionVersionArn)
                    .build();

                while (Instant.now().getEpochSecond() < maxTime) {

                    solutionVersionStatus = personalizeClient.describeSolutionVersion(describeSolutionVersionRequest).solutionVersion().status();
                    System.out.println("Solution version status: " + solutionVersionStatus);

                    if (solutionVersionStatus.equals("ACTIVE") || solutionVersionStatus.equals("CREATE FAILED")) {
                        break;
                    }
                    try {
                        Thread.sleep(waitInMilliseconds);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
                return solutionVersionArn;
            }
        } catch(PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateSolutionVersion](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/CreateSolutionVersion) in *AWS SDK for Java 2\.x API Reference*\. 

### Create an event tracker<a name="personalize_createEventTracker_java_topic"></a>

The following code example shows how to create a Amazon Personalize event tracker\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static String createEventTracker(PersonalizeClient personalizeClient, String eventTrackerName, String datasetGroupArn) {
        
        String eventTrackerId = "";
        String eventTrackerArn;
        long maxTime = 3 * 60 * 60; // 3 hours
        long waitInMilliseconds = 20 * 1000; // 20 seconds
        String status;
        
        try {

            CreateEventTrackerRequest createEventTrackerRequest = CreateEventTrackerRequest.builder()
                .name(eventTrackerName)
                .datasetGroupArn(datasetGroupArn)
                .build();
           
            CreateEventTrackerResponse createEventTrackerResponse = personalizeClient.createEventTracker(createEventTrackerRequest);
            
            eventTrackerArn = createEventTrackerResponse.eventTrackerArn();
            eventTrackerId = createEventTrackerResponse.trackingId();
            System.out.println("Event tracker ARN: " + eventTrackerArn);
            System.out.println("Event tracker ID: " + eventTrackerId);

            maxTime = Instant.now().getEpochSecond() + maxTime;

            DescribeEventTrackerRequest describeRequest = DescribeEventTrackerRequest.builder()
                .eventTrackerArn(eventTrackerArn)
                .build();

            
            while (Instant.now().getEpochSecond() < maxTime) {

                status = personalizeClient.describeEventTracker(describeRequest).eventTracker().status();
                System.out.println("EventTracker status: " + status);

                if (status.equals("ACTIVE") || status.equals("CREATE FAILED")) {
                    break;
                }
                try {
                    Thread.sleep(waitInMilliseconds);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            return eventTrackerId;
        }
        catch (PersonalizeException e){
            System.out.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return eventTrackerId;
    }
```
+  For API details, see [CreateEventTracker](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/CreateEventTracker) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a campaign<a name="personalize_deleteCampaign_java_topic"></a>

The following code example shows how to delete a campaign in Amazon Personalize\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static void deleteSpecificCampaign(PersonalizeClient personalizeClient, String campaignArn ) {

        try {
            DeleteCampaignRequest campaignRequest = DeleteCampaignRequest.builder()
                .campaignArn(campaignArn)
                .build();

            personalizeClient.deleteCampaign(campaignRequest);

        } catch (PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteCampaign](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/DeleteCampaign) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a solution<a name="personalize_deleteSolution_java_topic"></a>

The following code example shows how to delete a solution in Amazon Personalize\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static void deleteGivenSolution(PersonalizeClient personalizeClient, String solutionArn ) {

        try {
            DeleteSolutionRequest solutionRequest = DeleteSolutionRequest.builder()
                .solutionArn(solutionArn)
                .build();

            personalizeClient.deleteSolution(solutionRequest);
            System.out.println("Done");

        } catch (PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteSolution](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/DeleteSolution) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete an event tracker<a name="personalize_deleteEventTracker_java_topic"></a>

The following code example shows how to delete an event tracker in Amazon Personalize\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static void deleteEventTracker(PersonalizeClient personalizeClient, String eventTrackerArn) {
        try {
            DeleteEventTrackerRequest deleteEventTrackerRequest = DeleteEventTrackerRequest.builder()
                .eventTrackerArn(eventTrackerArn)
                .build();
                
            int status = 
                personalizeClient.deleteEventTracker(deleteEventTrackerRequest).sdkHttpResponse().statusCode();

            System.out.println("Status code:" + status);
            
        }
        catch (PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteEventTracker](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/DeleteEventTracker) in *AWS SDK for Java 2\.x API Reference*\. 

### Describe a campaign<a name="personalize_describeCampaign_java_topic"></a>

The following code example shows how to describe a campaign in Amazon Personalize\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static void describeSpecificCampaign(PersonalizeClient personalizeClient, String campaignArn) {

        try {
            DescribeCampaignRequest campaignRequest = DescribeCampaignRequest.builder()
                .campaignArn(campaignArn)
                .build();

            DescribeCampaignResponse campaignResponse = personalizeClient.describeCampaign(campaignRequest);
            Campaign myCampaign = campaignResponse.campaign();
            System.out.println("The Campaign name is "+myCampaign.name());
            System.out.println("The Campaign status is "+myCampaign.status());

        } catch (PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DescribeCampaign](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/DescribeCampaign) in *AWS SDK for Java 2\.x API Reference*\. 

### Describe a recipe<a name="personalize_describeRecipe_java_topic"></a>

The following code example shows how to describe a recipe in Amazon Personalize\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static void describeSpecificRecipe(PersonalizeClient personalizeClient, String recipeArn) {

        try{
            DescribeRecipeRequest recipeRequest = DescribeRecipeRequest.builder()
                .recipeArn(recipeArn)
                .build();

            DescribeRecipeResponse recipeResponse = personalizeClient.describeRecipe(recipeRequest);
            System.out.println("The recipe name is "+recipeResponse.recipe().name());

        } catch (PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DescribeRecipe](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/DescribeRecipe) in *AWS SDK for Java 2\.x API Reference*\. 

### Describe a solution<a name="personalize_describeSolution_java_topic"></a>

The following code example shows how to describe a solution in Amazon Personalize\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
   public static void describeSpecificSolution(PersonalizeClient personalizeClient, String solutionArn) {

       try {
           DescribeSolutionRequest solutionRequest = DescribeSolutionRequest.builder()
               .solutionArn(solutionArn)
               .build();

           DescribeSolutionResponse response = personalizeClient.describeSolution(solutionRequest);
           System.out.println("The Solution name is "+response.solution().name());

       } catch (PersonalizeException e) {
           System.err.println(e.awsErrorDetails().errorMessage());
           System.exit(1);
       }
   }
```
+  For API details, see [DescribeSolution](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/DescribeSolution) in *AWS SDK for Java 2\.x API Reference*\. 

### List campaigns<a name="personalize_listCampaigns_java_topic"></a>

The following code example shows how to list campaigns in Amazon Personalize\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static void listAllCampaigns(PersonalizeClient personalizeClient, String solutionArn) {

        try{
            ListCampaignsRequest campaignsRequest = ListCampaignsRequest.builder()
                .maxResults(10)
                .solutionArn(solutionArn)
                .build();

            ListCampaignsResponse response = personalizeClient.listCampaigns(campaignsRequest);
            List<CampaignSummary> campaigns = response.campaigns();
            for (CampaignSummary campaign: campaigns) {
                System.out.println("Campaign name is : "+campaign.name());
                System.out.println("Campaign ARN is : "+campaign.campaignArn());
            }

        } catch (PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListCampaigns](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/ListCampaigns) in *AWS SDK for Java 2\.x API Reference*\. 

### List dataset groups<a name="personalize_listDatasetGroups_java_topic"></a>

The following code example shows how to list dataset groups in Amazon Personalize\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static void listDSGroups( PersonalizeClient personalizeClient ) {

        try {
            ListDatasetGroupsRequest groupsRequest = ListDatasetGroupsRequest.builder()
                .maxResults(15)
                .build();

            ListDatasetGroupsResponse groupsResponse = personalizeClient.listDatasetGroups(groupsRequest);
            List<DatasetGroupSummary> groups = groupsResponse.datasetGroups();
            for (DatasetGroupSummary group: groups) {
                System.out.println("The DataSet name is : "+group.name());
                System.out.println("The DataSet ARN is : "+group.datasetGroupArn());
            }

        } catch (PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListDatasetGroups](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/ListDatasetGroups) in *AWS SDK for Java 2\.x API Reference*\. 

### List recipes<a name="personalize_listRecipes_java_topic"></a>

The following code example shows how to list recipes in Amazon Personalize\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static void listAllRecipes(PersonalizeClient personalizeClient) {

        try {
            ListRecipesRequest recipesRequest = ListRecipesRequest.builder()
                .maxResults(15)
                .build();

            ListRecipesResponse response = personalizeClient.listRecipes(recipesRequest);
            List<RecipeSummary> recipes = response.recipes();
            for (RecipeSummary recipe: recipes) {
                System.out.println("The recipe ARN is: "+recipe.recipeArn());
                System.out.println("The recipe name is: "+recipe.name());
            }

        } catch (PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListRecipes](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/ListRecipes) in *AWS SDK for Java 2\.x API Reference*\. 

### List solutions<a name="personalize_listSolutions_java_topic"></a>

The following code example shows how to list solutions in Amazon Personalize\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/personalize#readme)\. 
  

```
    public static void listAllSolutions(PersonalizeClient personalizeClient, String datasetGroupArn) {

        try {
            ListSolutionsRequest solutionsRequest = ListSolutionsRequest.builder()
                .maxResults(10)
                .datasetGroupArn(datasetGroupArn)
                .build() ;

            ListSolutionsResponse response = personalizeClient.listSolutions(solutionsRequest);
            List<SolutionSummary> solutions = response.solutions();
            for (SolutionSummary solution: solutions) {
                System.out.println("The solution ARN is: "+solution.solutionArn());
                System.out.println("The solution name is: "+solution.name());
            }

        } catch (PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListSolutions](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/ListSolutions) in *AWS SDK for Java 2\.x API Reference*\. 

### Update a campaign<a name="personalize_updateCampaign_java_topic"></a>

The following code example shows how to update a campaign Amazon Personalize\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javascipt/example_code/personalize#readme)\. 
  

```
    public static String updateCampaign(PersonalizeClient personalizeClient,
                                      String campaignArn,
                                      String solutionVersionArn,
                                      Integer minProvisionedTPS) {

        try {
            // build the updateCampaignRequest
            UpdateCampaignRequest updateCampaignRequest = UpdateCampaignRequest.builder()
                    .campaignArn(campaignArn)
                    .solutionVersionArn(solutionVersionArn)
                    .minProvisionedTPS(minProvisionedTPS)
                    .build();

            // update the campaign
            personalizeClient.updateCampaign(updateCampaignRequest);

            DescribeCampaignRequest campaignRequest = DescribeCampaignRequest.builder()
                    .campaignArn(campaignArn)
                    .build();

            DescribeCampaignResponse campaignResponse = personalizeClient.describeCampaign(campaignRequest);
            Campaign updatedCampaign = campaignResponse.campaign();

            System.out.println("The Campaign status is " + updatedCampaign.status());
            return updatedCampaign.status();

        } catch (PersonalizeException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [UpdateCampaign](https://docs.aws.amazon.com/goto/SdkForJavaV2/personalize-2018-05-22/UpdateCampaign) in *AWS SDK for Java 2\.x API Reference*\. 