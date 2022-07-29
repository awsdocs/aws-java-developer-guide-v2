--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# AWS Glue examples using SDK for Java 2\.x<a name="java_glue_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with AWS Glue\.

*Actions* are code excerpts that show you how to call individual AWS Glue functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple AWS Glue functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w591aac15c14b9c35c13)
+ [Scenarios](#w591aac15c14b9c35c15)

## Actions<a name="w591aac15c14b9c35c13"></a>

### Create a crawler<a name="glue_CreateCrawler_java_topic"></a>

The following code example shows how to create an AWS Glue crawler\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/glue#readme)\. 
  

```
    public static void createGlueCrawler(GlueClient glueClient,
                                         String iam,
                                         String s3Path,
                                         String cron,
                                         String dbName,
                                         String crawlerName) {

        try {
            S3Target s3Target = S3Target.builder()
                .path(s3Path)
                .build();

            // Add the S3Target to a list.
            List<S3Target> targetList = new ArrayList<>();
            targetList.add(s3Target);

            CrawlerTargets targets = CrawlerTargets.builder()
                .s3Targets(targetList)
                .build();

            CreateCrawlerRequest crawlerRequest = CreateCrawlerRequest.builder()
                .databaseName(dbName)
                .name(crawlerName)
                .description("Created by the AWS Glue Java API")
                .targets(targets)
                .role(iam)
                .schedule(cron)
                .build();

            glueClient.createCrawler(crawlerRequest);
            System.out.println(crawlerName +" was successfully created");

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [CreateCrawler](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/CreateCrawler) in *AWS SDK for Java 2\.x API Reference*\. 

### Get a crawler<a name="glue_GetCrawler_java_topic"></a>

The following code example shows how to get an AWS Glue crawler\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/glue#readme)\. 
  

```
    public static void getSpecificCrawler(GlueClient glueClient, String crawlerName) {

      try {
          GetCrawlerRequest crawlerRequest = GetCrawlerRequest.builder()
              .name(crawlerName)
              .build();

            GetCrawlerResponse response = glueClient.getCrawler(crawlerRequest);
            Instant createDate = response.crawler().creationTime();

            // Convert the Instant to readable date
            DateTimeFormatter formatter =
                  DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                          .withLocale( Locale.US)
                          .withZone( ZoneId.systemDefault() );

            formatter.format( createDate );
            System.out.println("The create date of the Crawler is " + createDate );

      } catch (GlueException e) {
          System.err.println(e.awsErrorDetails().errorMessage());
          System.exit(1);
      }
   }
```
+  For API details, see [GetCrawler](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/GetCrawler) in *AWS SDK for Java 2\.x API Reference*\. 

### Get a database from the Data Catalog<a name="glue_GetDatabase_java_topic"></a>

The following code example shows how to get a database from the AWS Glue Data Catalog\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/glue#readme)\. 
  

```
    public static void getSpecificDatabase(GlueClient glueClient, String databaseName) {

        try {
            GetDatabaseRequest databasesRequest = GetDatabaseRequest.builder()
                .name(databaseName)
                .build();

            GetDatabaseResponse response = glueClient.getDatabase(databasesRequest);
            Instant createDate = response.database().createTime();

            // Convert the Instant to readable date.
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.US)
                .withZone(ZoneId.systemDefault());

            formatter.format(createDate);
            System.out.println("The create date of the database is " + createDate);

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [GetDatabase](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/GetDatabase) in *AWS SDK for Java 2\.x API Reference*\. 

### Get tables from a database<a name="glue_GetTables_java_topic"></a>

The following code example shows how to get tables from a database in the AWS Glue Data Catalog\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/glue#readme)\. 
  

```
    public static void getGlueTable(GlueClient glueClient, String dbName, String tableName ) {

        try {
            GetTableRequest tableRequest = GetTableRequest.builder()
                .databaseName(dbName)
                .name(tableName)
                .build();

            GetTableResponse tableResponse = glueClient.getTable(tableRequest);
            Instant createDate = tableResponse.table().createTime();

            // Convert the Instant to readable date.
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                .withLocale( Locale.US)
                .withZone( ZoneId.systemDefault() );

            formatter.format( createDate );
            System.out.println("The create date of the table is " + createDate );

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [GetTables](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/GetTables) in *AWS SDK for Java 2\.x API Reference*\. 

### Start a crawler<a name="glue_StartCrawler_java_topic"></a>

The following code example shows how to start an AWS Glue crawler\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/glue#readme)\. 
  

```
    public static void startSpecificCrawler(GlueClient glueClient, String crawlerName) {

        try {
            StartCrawlerRequest crawlerRequest = StartCrawlerRequest.builder()
                .name(crawlerName)
                .build();

            glueClient.startCrawler(crawlerRequest);

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [StartCrawler](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/StartCrawler) in *AWS SDK for Java 2\.x API Reference*\. 

## Scenarios<a name="w591aac15c14b9c35c15"></a>

### Get started running crawlers and jobs<a name="glue_Scenario_GetStartedCrawlersJobs_java_topic"></a>

The following code example shows how to:
+ Create and run a crawler that crawls a public Amazon Simple Storage Service \(Amazon S3\) bucket and generates a metadata database that describes the CSV\-formatted data it finds\.
+ List information about databases and tables in your AWS Glue Data Catalog\.
+ Create and run a job that extracts CSV data from the source Amazon S3 bucket, transforms it by removing and renaming fields, and loads JSON\-formatted output into another Amazon S3 bucket\.
+ List information about job runs and view some of the transformed data\.
+ Delete all resources created by the demo\.

For more information, see [Tutorial: Getting started with AWS Glue Studio](https://docs.aws.amazon.com/glue/latest/ug/tutorial-create-job.html)\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/glue#readme)\. 
  

```
public class GlueScenario {

    public static void main(String[] args) throws InterruptedException {

        final String usage = "\n" +
            "Usage:\n" +
            "    <iam> <s3Path> <cron> <dbName> <crawlerName> <jobName> \n\n" +
            "Where:\n" +
            "    iam - The ARN of the IAM role that has AWS Glue and S3 permissions. \n" +
            "    s3Path - The Amazon Simple Storage Service (Amazon S3) target that contains data (for example, CSV data).\n" +
            "    cron - A cron expression used to specify the schedule  (i.e., cron(15 12 * * ? *).\n" +
            "    dbName - The database name. \n" +
            "    crawlerName - The name of the crawler. \n" +
            "    jobName - The name you assign to this job definition."+
            "    scriptLocation - The Amazon S3 path to a script that runs a job." +
            "    locationUri - The location of the database" ;

       if (args.length != 8) {
            System.out.println(usage);
            System.exit(1);
        }

        String iam = args[0];
        String s3Path = args[1];
        String cron = args[2];
        String dbName = args[3];
        String crawlerName = args[4];
        String jobName = args[5];
        String scriptLocation = args[6];
        String locationUri = args[7];
        Region region = Region.US_EAST_1;
        GlueClient glueClient = GlueClient.builder()
            .region(region)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

        System.out.println("About to start the AWS Glue Scenario");
        createDatabase(glueClient, dbName, locationUri);
        createGlueCrawler(glueClient, iam, s3Path, cron, dbName, crawlerName);
        getSpecificCrawler(glueClient, crawlerName);
        startSpecificCrawler(glueClient, crawlerName);
        getSpecificDatabase(glueClient, dbName);
        getGlueTables(glueClient, dbName);
        createJob(glueClient, jobName, iam, scriptLocation);
        startJob(glueClient, jobName);
        getAllJobs(glueClient);
        getJobRuns(glueClient, jobName);
        deleteJob(glueClient, jobName);
        System.out.println("*** Wait 5 MIN for the "+crawlerName +" to stop");
        TimeUnit.MINUTES.sleep(5);
        deleteDatabase(glueClient, dbName);
        deleteSpecificCrawler(glueClient, crawlerName);
        System.out.println("Successfully completed the AWS Glue Scenario");
    }

    public static void createDatabase(GlueClient glueClient, String dbName, String locationUri ) {

        try {
            DatabaseInput input = DatabaseInput.builder()
                .description("Built with the AWS SDK for Java V2")
                .name(dbName)
                .locationUri(locationUri)
                .build();

            CreateDatabaseRequest request = CreateDatabaseRequest.builder()
                .databaseInput(input)
                .build();

            glueClient.createDatabase(request);
            System.out.println("The database was successfully created");

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void createGlueCrawler(GlueClient glueClient,
                                         String iam,
                                         String s3Path,
                                         String cron,
                                         String dbName,
                                         String crawlerName) {

        try {
            S3Target s3Target = S3Target.builder()
                .path(s3Path)
                .build();

            List<S3Target> targetList = new ArrayList<>();
            targetList.add(s3Target);
            CrawlerTargets targets = CrawlerTargets.builder()
                .s3Targets(targetList)
                .build();

            CreateCrawlerRequest crawlerRequest = CreateCrawlerRequest.builder()
                .databaseName(dbName)
                .name(crawlerName)
                .description("Created by the AWS Glue Java API")
                .targets(targets)
                .role(iam)
                .schedule(cron)
                .build();

            glueClient.createCrawler(crawlerRequest);
            System.out.println(crawlerName +" was successfully created");

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void getSpecificCrawler(GlueClient glueClient, String crawlerName) {

        try {
            GetCrawlerRequest crawlerRequest = GetCrawlerRequest.builder()
                .name(crawlerName)
                .build();

            GetCrawlerResponse response = glueClient.getCrawler(crawlerRequest);
            Instant createDate = response.crawler().creationTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                .withLocale( Locale.US)
                .withZone( ZoneId.systemDefault() );

            formatter.format( createDate );
            System.out.println("The create date of the Crawler is " + createDate );

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void startSpecificCrawler(GlueClient glueClient, String crawlerName) {

        try {
            StartCrawlerRequest crawlerRequest = StartCrawlerRequest.builder()
                .name(crawlerName)
                .build();

            glueClient.startCrawler(crawlerRequest);
            System.out.println(crawlerName +" was successfully started!");

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void getSpecificDatabase(GlueClient glueClient, String databaseName) {

        try {
            GetDatabaseRequest databasesRequest = GetDatabaseRequest.builder()
                .name(databaseName)
                .build();

            GetDatabaseResponse response = glueClient.getDatabase(databasesRequest);
            Instant createDate = response.database().createTime();

            // Convert the Instant to readable date.
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                .withLocale( Locale.US)
                .withZone( ZoneId.systemDefault() );

            formatter.format( createDate );
            System.out.println("The create date of the database is " + createDate );

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void getGlueTables(GlueClient glueClient, String dbName){

        try {
            GetTablesRequest tableRequest = GetTablesRequest.builder()
                .databaseName(dbName)
                .build();

            GetTablesResponse response = glueClient.getTables(tableRequest);
            List<Table> tables = response.tableList();
            for (Table table: tables) {
                System.out.println("Table name is: "+table.name());
            }

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void startJob(GlueClient glueClient, String jobName) {

        try {
            StartJobRunRequest runRequest = StartJobRunRequest.builder()
                .workerType(WorkerType.G_1_X)
                .numberOfWorkers(10)
                .jobName(jobName)
                .build();

            StartJobRunResponse response = glueClient.startJobRun(runRequest);
            System.out.println("The request Id of the job is "+ response.responseMetadata().requestId());

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void createJob(GlueClient glueClient, String jobName, String iam, String scriptLocation) {

        try {
            JobCommand command = JobCommand.builder()
                .pythonVersion("3")
                .name("MyJob1")
                .scriptLocation(scriptLocation)
                .build();

            CreateJobRequest jobRequest = CreateJobRequest.builder()
                .description("A Job created by using the AWS SDK for Java V2")
                .glueVersion("2.0")
                .workerType(WorkerType.G_1_X)
                .numberOfWorkers(10)
                .name(jobName)
                .role(iam)
                .command(command)
                .build();

            glueClient.createJob(jobRequest);
            System.out.println(jobName +" was successfully created.");

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void getAllJobs(GlueClient glueClient) {

        try {
            GetJobsRequest jobsRequest = GetJobsRequest.builder()
                .maxResults(10)
                .build();

            GetJobsResponse jobsResponse = glueClient.getJobs(jobsRequest);
            List<Job> jobs = jobsResponse.jobs();
            for (Job job: jobs) {
                System.out.println("Job name is : "+job.name());
                System.out.println("The job worker type is : "+job.workerType().name());
            }

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void getJobRuns(GlueClient glueClient, String jobName) {

        try {
            GetJobRunsRequest runsRequest = GetJobRunsRequest.builder()
                .jobName(jobName)
                .maxResults(20)
                .build();

            GetJobRunsResponse response = glueClient.getJobRuns(runsRequest);
            List<JobRun> jobRuns = response.jobRuns();
            for (JobRun jobRun: jobRuns) {
                System.out.println("Job run state is "+jobRun.jobRunState().name());
                System.out.println("Job run Id is "+jobRun.id());
                System.out.println("The Glue version is "+jobRun.glueVersion());
            }

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void deleteJob(GlueClient glueClient, String jobName) {

        try {
            DeleteJobRequest jobRequest = DeleteJobRequest.builder()
                .jobName(jobName)
                .build();

            glueClient.deleteJob(jobRequest);
            System.out.println(jobName +" was successfully deleted");

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void deleteDatabase(GlueClient glueClient, String databaseName) {

        try {
            DeleteDatabaseRequest request = DeleteDatabaseRequest.builder()
                .name(databaseName)
                .build();

            glueClient.deleteDatabase(request);
            System.out.println(databaseName +" was successfully deleted");

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void deleteSpecificCrawler(GlueClient glueClient, String crawlerName) {

        try {
            DeleteCrawlerRequest deleteCrawlerRequest = DeleteCrawlerRequest.builder()
                .name(crawlerName)
                .build();

            glueClient.deleteCrawler(deleteCrawlerRequest);
            System.out.println(crawlerName +" was deleted");

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
```
+ For API details, see the following topics in *AWS SDK for Java 2\.x API Reference*\.
  + [CreateCrawler](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/CreateCrawler)
  + [CreateJob](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/CreateJob)
  + [DeleteCrawler](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/DeleteCrawler)
  + [DeleteDatabase](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/DeleteDatabase)
  + [DeleteJob](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/DeleteJob)
  + [DeleteTable](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/DeleteTable)
  + [GetCrawler](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/GetCrawler)
  + [GetDatabase](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/GetDatabase)
  + [GetJobRun](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/GetJobRun)
  + [GetJobRuns](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/GetJobRuns)
  + [GetTables](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/GetTables)
  + [ListJobs](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/ListJobs)
  + [StartCrawler](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/StartCrawler)
  + [StartJobRun](https://docs.aws.amazon.com/goto/SdkForJavaV2/glue-2017-03-31/StartJobRun)