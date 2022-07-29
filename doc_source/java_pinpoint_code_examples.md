--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Amazon Pinpoint examples using SDK for Java 2\.x<a name="java_pinpoint_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Amazon Pinpoint\.

*Actions* are code excerpts that show you how to call individual Amazon Pinpoint functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple Amazon Pinpoint functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w591aac15c14b9c51c13)

## Actions<a name="w591aac15c14b9c51c13"></a>

### Create a campaign<a name="pinpoint_CreateCampaign_java_topic"></a>

The following code example shows how to create a campaign\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/pinpoint#readme)\. 
Create a campaign\.  

```
    public static void createPinCampaign(PinpointClient pinpoint, String appId, String segmentId) {
        CampaignResponse result = createCampaign(pinpoint, appId, segmentId);
        System.out.println("Campaign " + result.name() + " created.");
        System.out.println(result.description());
    }

    public static CampaignResponse createCampaign(PinpointClient client, String appID, String segmentID) {

        try {
            Schedule schedule = Schedule.builder()
                .startTime("IMMEDIATE")
                .build();

            Message defaultMessage = Message.builder()
                .action(Action.OPEN_APP)
                .body("My message body.")
                .title("My message title.")
                .build();

            MessageConfiguration messageConfiguration = MessageConfiguration.builder()
                .defaultMessage(defaultMessage)
                .build();

            WriteCampaignRequest request = WriteCampaignRequest.builder()
                .description("My description")
                .schedule(schedule)
                .name("MyCampaign")
                .segmentId(segmentID)
                .messageConfiguration(messageConfiguration)
                .build();

            CreateCampaignResponse result = client.createCampaign(CreateCampaignRequest.builder()
                            .applicationId(appID)
                            .writeCampaignRequest(request).build()
            );

            System.out.println("Campaign ID: " + result.campaignResponse().id());
            return result.campaignResponse();

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        return null;
    }
```
+  For API details, see [CreateCampaign](https://docs.aws.amazon.com/goto/SdkForJavaV2/pinpoint-2016-12-01/CreateCampaign) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a segment<a name="pinpoint_CreateSegment_java_topic"></a>

The following code example shows how to create a segment\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/pinpoint#readme)\. 
  

```
    public static SegmentResponse createSegment(PinpointClient client, String appId) {

        try {
            Map<String, AttributeDimension> segmentAttributes = new HashMap<>();
            segmentAttributes.put("Team", AttributeDimension.builder()
                .attributeType(AttributeType.INCLUSIVE)
                .values("Lakers")
                .build());

            RecencyDimension recencyDimension = RecencyDimension.builder()
                .duration("DAY_30")
                .recencyType("ACTIVE")
                .build();

            SegmentBehaviors segmentBehaviors = SegmentBehaviors.builder()
                .recency(recencyDimension)
                .build();

            SegmentDemographics segmentDemographics = SegmentDemographics
                .builder()
                .build();

            SegmentLocation segmentLocation = SegmentLocation
                .builder()
                .build();

            SegmentDimensions dimensions = SegmentDimensions
                .builder()
                .attributes(segmentAttributes)
                .behavior(segmentBehaviors)
                .demographic(segmentDemographics)
                .location(segmentLocation)
                .build();

            WriteSegmentRequest writeSegmentRequest = WriteSegmentRequest.builder()
                .name("MySegment")
                .dimensions(dimensions)
                .build();

            CreateSegmentRequest createSegmentRequest = CreateSegmentRequest.builder()
                .applicationId(appId)
                .writeSegmentRequest(writeSegmentRequest)
                .build();

            CreateSegmentResponse createSegmentResult = client.createSegment(createSegmentRequest);
            System.out.println("Segment ID: " + createSegmentResult.segmentResponse().id());
            System.out.println("Done");
            return createSegmentResult.segmentResponse();

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
    }
```
+  For API details, see [CreateSegment](https://docs.aws.amazon.com/goto/SdkForJavaV2/pinpoint-2016-12-01/CreateSegment) in *AWS SDK for Java 2\.x API Reference*\. 

### Create an application<a name="pinpoint_CreateApp_java_topic"></a>

The following code example shows how to create an appliation\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/pinpoint#readme)\. 
  

```
    public static String createApplication(PinpointClient pinpoint, String appName) {

        try {
            CreateApplicationRequest appRequest = CreateApplicationRequest.builder()
                .name(appName)
                .build();

            CreateAppRequest request = CreateAppRequest.builder()
                .createApplicationRequest(appRequest)
                .build();

            CreateAppResponse result = pinpoint.createApp(request);
            return result.applicationResponse().id();

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateApp](https://docs.aws.amazon.com/goto/SdkForJavaV2/pinpoint-2016-12-01/CreateApp) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete an application<a name="pinpoint_DeleteApp_java_topic"></a>

The following code example shows how to delete an application\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/pinpoint#readme)\. 
Delete an application\.  

```
    public static void deletePinApp(PinpointClient pinpoint, String appId ) {

        try {
            DeleteAppRequest appRequest = DeleteAppRequest.builder()
                .applicationId(appId)
                .build();

            DeleteAppResponse result = pinpoint.deleteApp(appRequest);
            String appName = result.applicationResponse().name();
            System.out.println("Application " + appName + " has been deleted.");

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteApp](https://docs.aws.amazon.com/goto/SdkForJavaV2/pinpoint-2016-12-01/DeleteApp) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete an endpoint<a name="pinpoint_DeleteEndpoint_java_topic"></a>

The following code example shows how to delete an endpoint\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/pinpoint#readme)\. 
Delete an endpoint\.  

```
    public static void deletePinEncpoint(PinpointClient pinpoint, String appId, String endpointId ) {

        try {
            DeleteEndpointRequest appRequest = DeleteEndpointRequest.builder()
                .applicationId(appId)
                .endpointId(endpointId)
                .build();

            DeleteEndpointResponse result = pinpoint.deleteEndpoint(appRequest);
            String id = result.endpointResponse().id();
            System.out.println("The deleted endpoint id  " + id);

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        System.out.println("Done");
    }
```
+  For API details, see [DeleteEndpoint](https://docs.aws.amazon.com/goto/SdkForJavaV2/pinpoint-2016-12-01/DeleteEndpoint) in *AWS SDK for Java 2\.x API Reference*\. 

### Export an endpoint<a name="pinpoint_CreateExportJob_java_topic"></a>

The following code example shows how to export an endpoint\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/pinpoint#readme)\. 
Export an endpoint\.  

```
    public static void exportAllEndpoints(PinpointClient pinpoint,
                                          S3Client s3Client,
                                          String applicationId,
                                          String s3BucketName,
                                          String path,
                                          String iamExportRoleArn) {

        try {
            List<String> objectKeys = exportEndpointsToS3(pinpoint, s3Client, s3BucketName, iamExportRoleArn, applicationId);
            List<String> endpointFileKeys = objectKeys.stream().filter(o -> o.endsWith(".gz")).collect(Collectors.toList());
            downloadFromS3(s3Client, path, s3BucketName, endpointFileKeys);

        } catch ( PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static List<String> exportEndpointsToS3(PinpointClient pinpoint, S3Client s3Client, String s3BucketName, String iamExportRoleArn, String applicationId) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH_mm:ss.SSS_z");
        String endpointsKeyPrefix = "exports/" + applicationId + "_" + dateFormat.format(new Date());
        String s3UrlPrefix = "s3://" + s3BucketName + "/" + endpointsKeyPrefix + "/";
        List<String> objectKeys = new ArrayList<>();
        String key;

        try {
            // Defines the export job that Amazon Pinpoint runs.
            ExportJobRequest jobRequest = ExportJobRequest.builder()
                .roleArn(iamExportRoleArn)
                .s3UrlPrefix(s3UrlPrefix)
                .build();

            CreateExportJobRequest exportJobRequest = CreateExportJobRequest.builder()
                .applicationId(applicationId)
                .exportJobRequest(jobRequest)
                .build();

            System.out.format("Exporting endpoints from Amazon Pinpoint application %s to Amazon S3 " +
                    "bucket %s . . .\n", applicationId, s3BucketName);

            CreateExportJobResponse exportResult = pinpoint.createExportJob(exportJobRequest);
            String jobId = exportResult.exportJobResponse().id();
            System.out.println(jobId);
            printExportJobStatus(pinpoint, applicationId, jobId);

            ListObjectsV2Request v2Request = ListObjectsV2Request.builder()
                .bucket(s3BucketName)
                .prefix(endpointsKeyPrefix)
                .build();

            // Create a list of object keys.
            ListObjectsV2Response v2Response = s3Client.listObjectsV2(v2Request);
            List<S3Object> objects = v2Response.contents();
            for (S3Object object: objects) {
                key = object.key();
                objectKeys.add(key);
            }

            return objectKeys;

        } catch ( PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
    }

    private static void printExportJobStatus(PinpointClient pinpointClient,
                                             String applicationId,
                                             String jobId) {

        GetExportJobResponse getExportJobResult;
        String status;

        try {
            // Checks the job status until the job completes or fails.
            GetExportJobRequest exportJobRequest = GetExportJobRequest.builder()
                .jobId(jobId)
                .applicationId(applicationId)
                .build();

            do {
                getExportJobResult = pinpointClient.getExportJob(exportJobRequest);
                status = getExportJobResult.exportJobResponse().jobStatus().toString().toUpperCase();
                System.out.format("Export job %s . . .\n", status);
                TimeUnit.SECONDS.sleep(3);

            } while (!status.equals("COMPLETED") && !status.equals("FAILED"));

            if (status.equals("COMPLETED")) {
                System.out.println("Finished exporting endpoints.");
            } else {
                System.err.println("Failed to export endpoints.");
                System.exit(1);
            }

        } catch (PinpointException | InterruptedException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    // Download files from an Amazon S3 bucket and write them to the path location.
    public static void downloadFromS3(S3Client s3Client, String path, String s3BucketName, List<String> objectKeys) {

        String newPath;
        try {
            for (String key : objectKeys) {
                GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(s3BucketName)
                    .key(key)
                    .build();

                ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
                byte[] data = objectBytes.asByteArray();

                // Write the data to a local file.
                String fileSuffix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                newPath = path + fileSuffix+".gz";
                File myFile = new File(newPath);
                OutputStream os = new FileOutputStream(myFile);
                os.write(data);
            }
            System.out.println("Download finished.");

        } catch (S3Exception | NullPointerException | IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [CreateExportJob](https://docs.aws.amazon.com/goto/SdkForJavaV2/pinpoint-2016-12-01/CreateExportJob) in *AWS SDK for Java 2\.x API Reference*\. 

### Get endpoints<a name="pinpoint_GetEndpoint_java_topic"></a>

The following code example shows how to get endpoints\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/pinpoint#readme)\. 
  

```
    public static void lookupPinpointEndpoint(PinpointClient pinpoint, String appId, String endpoint ) {

        try {
            GetEndpointRequest appRequest = GetEndpointRequest.builder()
                .applicationId(appId)
                .endpointId(endpoint)
                .build();

            GetEndpointResponse result = pinpoint.getEndpoint(appRequest);
            EndpointResponse endResponse = result.endpointResponse();

            // Uses the Google Gson library to pretty print the endpoint JSON.
            Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

            String endpointJson = gson.toJson(endResponse);
            System.out.println(endpointJson);

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        System.out.println("Done");
    }
```
+  For API details, see [GetEndpoint](https://docs.aws.amazon.com/goto/SdkForJavaV2/pinpoint-2016-12-01/GetEndpoint) in *AWS SDK for Java 2\.x API Reference*\. 

### Import a segment<a name="pinpoint_CreateImportJob_java_topic"></a>

The following code example shows how to import a segment\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/pinpoint#readme)\. 
Import a segment\.  

```
    public static ImportJobResponse createImportSegment(PinpointClient client,
                                                        String appId,
                                                        String bucket,
                                                        String key,
                                                        String roleArn) {

        try {
            ImportJobRequest importRequest = ImportJobRequest.builder()
                .defineSegment(true)
                .registerEndpoints(true)
                .roleArn(roleArn)
                .format(Format.JSON)
                .s3Url("s3://" + bucket + "/" + key)
                .build();

            CreateImportJobRequest jobRequest = CreateImportJobRequest.builder()
                .importJobRequest(importRequest)
                .applicationId(appId)
                .build();

            CreateImportJobResponse jobResponse = client.createImportJob(jobRequest);
            return jobResponse.importJobResponse();

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
    }
```
+  For API details, see [CreateImportJob](https://docs.aws.amazon.com/goto/SdkForJavaV2/pinpoint-2016-12-01/CreateImportJob) in *AWS SDK for Java 2\.x API Reference*\. 

### List endpoints<a name="pinpoint_GetUserEndpoints_java_topic"></a>

The following code example shows how to list endpoints\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/pinpoint#readme)\. 
  

```
    public static void listAllEndpoints(PinpointClient pinpoint,
                                        String applicationId,
                                       String userId) {

        try {
            GetUserEndpointsRequest endpointsRequest = GetUserEndpointsRequest.builder()
                .userId(userId)
                .applicationId(applicationId)
                .build();

            GetUserEndpointsResponse response = pinpoint.getUserEndpoints(endpointsRequest);
            List<EndpointResponse> endpoints = response.endpointsResponse().item();

            // Display the results.
            for (EndpointResponse endpoint: endpoints) {
                System.out.println("The channel type is: "+endpoint.channelType());
                System.out.println("The address is  "+endpoint.address());
            }

        } catch ( PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [GetUserEndpoints](https://docs.aws.amazon.com/goto/SdkForJavaV2/pinpoint-2016-12-01/GetUserEndpoints) in *AWS SDK for Java 2\.x API Reference*\. 

### List segments<a name="pinpoint_GetSegments_java_topic"></a>

The following code example shows how to list segments\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/pinpoint#readme)\. 
List segments\.  

```
    public static void listSegs( PinpointClient pinpoint, String appId) {

        try {
            GetSegmentsRequest request = GetSegmentsRequest.builder()
                .applicationId(appId)
                .build();

            GetSegmentsResponse response = pinpoint.getSegments(request);
            List<SegmentResponse> segments = response.segmentsResponse().item();
            for(SegmentResponse segment: segments) {
                System.out.println("Segement " + segment.id() + " " + segment.name() + " " + segment.lastModifiedDate());
            }

        } catch ( PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [GetSegments](https://docs.aws.amazon.com/goto/SdkForJavaV2/pinpoint-2016-12-01/GetSegments) in *AWS SDK for Java 2\.x API Reference*\. 

### Send email and text messages<a name="pinpoint_SendMessages_java_topic"></a>

The following code example shows how to send email and text messages with Amazon Pinpoint\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/pinpoint#readme)\. 
Send an email message\.  

```
    public static void sendEmail(PinpointClient pinpoint,
                                 String subject,
                                 String appId,
                                 String senderAddress,
                                 String toAddress) {

        try {
            Map<String,AddressConfiguration> addressMap = new HashMap<>();
            AddressConfiguration configuration = AddressConfiguration.builder()
                .channelType(ChannelType.EMAIL)
                .build();

            addressMap.put(toAddress, configuration);
            SimpleEmailPart emailPart = SimpleEmailPart.builder()
                .data(htmlBody)
                .charset(charset)
                .build() ;

            SimpleEmailPart subjectPart = SimpleEmailPart.builder()
                .data(subject)
                .charset(charset)
                .build() ;

            SimpleEmail simpleEmail = SimpleEmail.builder()
                .htmlPart(emailPart)
                .subject(subjectPart)
                .build();

            EmailMessage emailMessage = EmailMessage.builder()
                .body(htmlBody)
                .fromAddress(senderAddress)
                .simpleEmail(simpleEmail)
                .build();

            DirectMessageConfiguration directMessageConfiguration = DirectMessageConfiguration.builder()
                .emailMessage(emailMessage)
                .build();

            MessageRequest messageRequest = MessageRequest.builder()
                .addresses(addressMap)
                .messageConfiguration(directMessageConfiguration)
                .build();

            SendMessagesRequest messagesRequest = SendMessagesRequest.builder()
                .applicationId(appId)
                .messageRequest(messageRequest)
                .build();

            pinpoint.sendMessages(messagesRequest);

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
Send an SMS message\.  

```
    public static void sendSMSMessage(PinpointClient pinpoint, String message, String appId, String originationNumber, String destinationNumber) {

        try {
            Map<String, AddressConfiguration> addressMap = new HashMap<String, AddressConfiguration>();
            AddressConfiguration addConfig = AddressConfiguration.builder()
                .channelType(ChannelType.SMS)
                .build();

            addressMap.put(destinationNumber, addConfig);
            SMSMessage smsMessage = SMSMessage.builder()
                .body(message)
                .messageType(messageType)
                .originationNumber(originationNumber)
                .senderId(senderId)
                .keyword(registeredKeyword)
                .build();

            // Create a DirectMessageConfiguration object.
            DirectMessageConfiguration direct = DirectMessageConfiguration.builder()
                .smsMessage(smsMessage)
                .build();

            MessageRequest msgReq = MessageRequest.builder()
                .addresses(addressMap)
                .messageConfiguration(direct)
                .build();

            // create a  SendMessagesRequest object
            SendMessagesRequest request = SendMessagesRequest.builder()
                .applicationId(appId)
                .messageRequest(msgReq)
                .build();

            SendMessagesResponse response= pinpoint.sendMessages(request);
            MessageResponse msg1 = response.messageResponse();
            Map map1 = msg1.result();

            //Write out the result of sendMessage.
            map1.forEach((k, v) -> System.out.println((k + ":" + v)));

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [SendMessages](https://docs.aws.amazon.com/goto/SdkForJavaV2/pinpoint-2016-12-01/SendMessages) in *AWS SDK for Java 2\.x API Reference*\. 

### Update an endpoint<a name="pinpoint_UpdateEndpoint_java_topic"></a>

The following code example shows how to update an endpoint\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/pinpoint#readme)\. 
  

```
    public static EndpointResponse createEndpoint(PinpointClient client, String appId) {
        String endpointId = UUID.randomUUID().toString();
        System.out.println("Endpoint ID: " + endpointId);

        try {
            EndpointRequest endpointRequest = createEndpointRequestData();
            UpdateEndpointRequest updateEndpointRequest = UpdateEndpointRequest.builder()
                .applicationId(appId)
                .endpointId(endpointId)
                .endpointRequest(endpointRequest)
                .build();

            UpdateEndpointResponse updateEndpointResponse = client.updateEndpoint(updateEndpointRequest);
            System.out.println("Update Endpoint Response: " + updateEndpointResponse.messageBody());

            GetEndpointRequest getEndpointRequest = GetEndpointRequest.builder()
                .applicationId(appId)
                .endpointId(endpointId)
                .build();

            GetEndpointResponse getEndpointResponse = client.getEndpoint(getEndpointRequest);
            System.out.println(getEndpointResponse.endpointResponse().address());
            System.out.println(getEndpointResponse.endpointResponse().channelType());
            System.out.println(getEndpointResponse.endpointResponse().applicationId());
            System.out.println(getEndpointResponse.endpointResponse().endpointStatus());
            System.out.println(getEndpointResponse.endpointResponse().requestId());
            System.out.println(getEndpointResponse.endpointResponse().user());

            return getEndpointResponse.endpointResponse();

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
    }

    private static EndpointRequest createEndpointRequestData() {

        try {
            List<String> favoriteTeams = new ArrayList<>();
            favoriteTeams.add("Lakers");
            favoriteTeams.add("Warriors");
            HashMap<String, List<String>> customAttributes = new HashMap<>();
            customAttributes.put("team", favoriteTeams);

            EndpointDemographic demographic = EndpointDemographic.builder()
                .appVersion("1.0")
                .make("apple")
                .model("iPhone")
                .modelVersion("7")
                .platform("ios")
                .platformVersion("10.1.1")
                .timezone("America/Los_Angeles")
                .build();

            EndpointLocation location = EndpointLocation.builder()
                .city("Los Angeles")
                .country("US")
                .latitude(34.0)
                .longitude(-118.2)
                .postalCode("90068")
                .region("CA")
                .build();

            Map<String,Double> metrics = new HashMap<>();
            metrics.put("health", 100.00);
            metrics.put("luck", 75.00);

            EndpointUser user = EndpointUser.builder()
                .userId(UUID.randomUUID().toString())
                .build();

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
            String nowAsISO = df.format(new Date());

            return EndpointRequest.builder()
                .address(UUID.randomUUID().toString())
                .attributes(customAttributes)
                .channelType("APNS")
                .demographic(demographic)
                .effectiveDate(nowAsISO)
                .location(location)
                .metrics(metrics)
                .optOut("NONE")
                .requestId(UUID.randomUUID().toString())
                .user(user)
                .build();

        } catch (PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
    }
```
+  For API details, see [UpdateEndpoint](https://docs.aws.amazon.com/goto/SdkForJavaV2/pinpoint-2016-12-01/UpdateEndpoint) in *AWS SDK for Java 2\.x API Reference*\. 

### Update channels<a name="pinpoint_GetSmsChannel_java_topic"></a>

The following code example shows how to update channels\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/pinpoint#readme)\. 
  

```
    private static SMSChannelResponse getSMSChannel(PinpointClient client, String appId) {

        try {
            GetSmsChannelRequest request = GetSmsChannelRequest.builder()
                .applicationId(appId)
                .build();

            SMSChannelResponse response = client.getSmsChannel(request).smsChannelResponse();
            System.out.println("Channel state is " + response.enabled());
            return response;

        } catch ( PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
    }

    private static void toggleSmsChannel(PinpointClient client, String appId, SMSChannelResponse getResponse) {
        boolean enabled = !getResponse.enabled();

        try {
            SMSChannelRequest request = SMSChannelRequest.builder()
                .enabled(enabled)
                .build();

            UpdateSmsChannelRequest updateRequest = UpdateSmsChannelRequest.builder()
                .smsChannelRequest(request)
                .applicationId(appId)
                .build();

            UpdateSmsChannelResponse result = client.updateSmsChannel(updateRequest);
            System.out.println("Channel state: " + result.smsChannelResponse().enabled());

        } catch ( PinpointException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [GetSmsChannel](https://docs.aws.amazon.com/goto/SdkForJavaV2/pinpoint-2016-12-01/GetSmsChannel) in *AWS SDK for Java 2\.x API Reference*\. 