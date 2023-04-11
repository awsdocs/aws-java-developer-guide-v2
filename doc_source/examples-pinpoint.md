# Working with Amazon Pinpoint<a name="examples-pinpoint"></a>

You can use Amazon Pinpoint to send relevant, personalized messages to your customers via multiple communication channels, such as push notifications, SMS, and email\.

## Create a project<a name="pinpoint-create-project"></a>

A project \(or application\) in Amazon Pinpoint is a collection of settings, customer data, segments, and campaigns\.

To create a project, start by building a [CreateApplicationRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/CreateApplicationRequest.html) object with the name of the project as the value of its `name()`\. Then build a [CreateAppRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/CreateAppRequest.html) object, passing in the `CreateApplicationRequest` object as the value of its `createApplicationRequest()` method\. Call the `createApp()` method of your [PinpointClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/PinpointClient.html), passing in the `CreateAppRequest` object\. Capture the result of this request as a [CreateAppResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/CreateAppResponse.html) object, as demonstrated in the following code snippet\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.CreateAppRequest;
import software.amazon.awssdk.services.pinpoint.model.CreateAppResponse;
import software.amazon.awssdk.services.pinpoint.model.CreateApplicationRequest;
import software.amazon.awssdk.services.pinpoint.model.PinpointException;
```

 **Code** 

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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/pinpoint/src/main/java/com/example/pinpoint/CreateApp.java) on GitHub\.

## Create a dynamic segment<a name="pinpoint-create-dynamic"></a>

A segment is a set of customers who share specific attributes, such as the city they live in or how frequently they visit your website\. A dynamic segment is one that’s based on attributes that you define, and can change over time\.

To create a dynamic segment, first build all of the dimensions you want for this segment\. For example, the following code snippet is set to include customers who were active on the site in the last 30 days\. You can do this by first building a [RecencyDimension](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/RecencyDimension.html) object with the `duration()` and `recencyType()` you want \(that is, `ACTIVE` or `INACTIVE`\), and then passing this object to a [SegmentBehaviors](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/SegmentBehaviors.html) builder object as the value of `recency()`\.

When you have defined your segment attributes, build them into a [SegmentDimensions](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/SegmentDimensions.html) object\. Then build a [WriteSegmentRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/WriteSegmentRequest.html) object, passing in the `SegmentDimensions` object as the value of its `dimensions()`\. Next, build a [CreateSegmentRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/CreateSegmentRequest.html) object, passing in the `WriteSegmentRequest` object as the value of its `writeSegmentRequest()`\. Finally, call the `createSegment()` method of your `PinpointClient`, passing in the `CreateSegmentRequest` object\. Capture the result of this request as a [CreateSegmentResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/CreateSegmentResponse.html) object\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.AttributeDimension;
import software.amazon.awssdk.services.pinpoint.model.SegmentResponse;
import software.amazon.awssdk.services.pinpoint.model.AttributeType;
import software.amazon.awssdk.services.pinpoint.model.RecencyDimension;
import software.amazon.awssdk.services.pinpoint.model.SegmentBehaviors;
import software.amazon.awssdk.services.pinpoint.model.SegmentDemographics;
import software.amazon.awssdk.services.pinpoint.model.SegmentLocation;
import software.amazon.awssdk.services.pinpoint.model.SegmentDimensions;
import software.amazon.awssdk.services.pinpoint.model.WriteSegmentRequest;
import software.amazon.awssdk.services.pinpoint.model.CreateSegmentRequest;
import software.amazon.awssdk.services.pinpoint.model.CreateSegmentResponse;
import software.amazon.awssdk.services.pinpoint.model.PinpointException;
import java.util.HashMap;
import java.util.Map;
```

 **Code** 

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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/pinpoint/src/main/java/com/example/pinpoint/CreateSegment.java) on GitHub\.

## Import a static segment<a name="pinpoint-import-static"></a>

A static segment is one you create and import from outside of Amazon Pinpoint\. The following example code shows how to create a static segment by importing it from Amazon S3\.

### Prerequisite<a name="prerequisite"></a>

Before you can complete this example, you need to create an IAM role that grants Amazon Pinpoint access to Amazon S3\. For more information, see [IAM role for importing endpoints or segments](http://docs.aws.amazon.com/pinpoint/latest/developerguide/permissions-import-segment.html) in the Amazon Pinpoint Developer Guide\.

To import a static segment, start by building an [ImportJobRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/ImportJobRequest.html) object\. In the builder, specify the `s3Url()`, `roleArn()`, and `format()`\.

**Note**  
For more information about the properties of an `ImportJobRequest`, see [the ImportJobRequest section of Import Jobs](http://docs.aws.amazon.com/pinpoint/latest/apireference/apps-application-id-jobs-import.html#apps-application-id-jobs-import-properties) in the Amazon Pinpoint API Reference\.

Then build a [CreateImportJobRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/CreateImportJobRequest.html) object, passing in the `ImportJobRequest` object as the value of its `importJobRequest()`, and the `ID` of your project as the `applicationId()`\. Call the `createImportJob()` method of your `PinpointClient`, passing in the `CreateImportJobRequest` object\. Capture the result of this request as a `CreateImportJobResponse` object, as demonstrated in the following code snippet\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.CreateImportJobRequest;
import software.amazon.awssdk.services.pinpoint.model.ImportJobResponse;
import software.amazon.awssdk.services.pinpoint.model.ImportJobRequest;
import software.amazon.awssdk.services.pinpoint.model.Format;
import software.amazon.awssdk.services.pinpoint.model.CreateImportJobResponse;
import software.amazon.awssdk.services.pinpoint.model.PinpointException;
```

 **Code** 

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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/pinpoint/src/main/java/com/example/pinpoint/ImportSegment.java) on GitHub\.

## List segments for your project<a name="pinpoint-list-segments"></a>

To list the segments associated with a particular project, start by building a [GetSegmentsRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/GetSegmentsRequest.html) object, with the `ID` of the project as the value of its `applicationId()`\. Next, call the `getSegments()` method of your `PinpointClient`, passing in the `GetSegmentsRequest` object\. Capture the result of this request as a [GetSegmentsResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/GetSegmentsResponse.html) object\. Finally, instantiate a [List](https://docs.oracle.com/javase/8/docs/api/index.html?java/util/Map.html) object upcasted to the [SegmentResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/SegmentResponse.html) class\. Then call the `segmentsResponse().item()` of `GetSegmentsResponse`, as demonstrated in the following code snippet\. From there, you can iterate through the results\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.GetSegmentsRequest;
import software.amazon.awssdk.services.pinpoint.model.GetSegmentsResponse;
import software.amazon.awssdk.services.pinpoint.model.PinpointException;
import software.amazon.awssdk.services.pinpoint.model.SegmentResponse;
import java.util.List;
```

 **Code** 

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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/pinpoint/src/main/java/com/example/pinpoint/ListSegments.java) on GitHub\.

## Create a campaign<a name="pinpoint-create-campaign"></a>

A campaign is an initiative meant to engage a particular audience segment by sending messages to those customers\.

To create a campaign, first build all of the settings you want for this campaign\. In the following code snippet, for example, the campaign will start immediately because the `startTime()` of the [Schedule](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/Schedule.html) is set to `IMMEDIATE`\. To set it to start at a specific time instead, specify a time in ISO 8601 format\.

**Note**  
For more information about the settings available for campaigns, see the **Schedule** section of [Campaigns](http://docs.aws.amazon.com/pinpoint/latest/apireference/apps-application-id-campaigns.html#apps-application-id-campaigns-model-schedule) in the Amazon Pinpoint API Reference\.

After you define your campaign configuration, build it into a [WriteCampaignRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/WriteCampaignRequest.html) object\. None of the methods of the `builder()` of the `WriteCampaignRequest` are required\. But you do need to include any of the configuration settings \([MessageConfiguration](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/MessageConfiguration.html)\) that you set for the campaign\. We also recommend that you include a `name` and a `description` for your campaign so you can easily distinguish it from other campaigns\. Call the `createCampaign ()` method of your `PinpointClient`, passing in the `WriteCampaignRequest` object\. Capture the result of this request as a [CreateCampaignResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/CreateCampaignResponse.html) object\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.CampaignResponse;
import software.amazon.awssdk.services.pinpoint.model.Message;
import software.amazon.awssdk.services.pinpoint.model.Schedule;
import software.amazon.awssdk.services.pinpoint.model.Action;
import software.amazon.awssdk.services.pinpoint.model.MessageConfiguration;
import software.amazon.awssdk.services.pinpoint.model.WriteCampaignRequest;
import software.amazon.awssdk.services.pinpoint.model.CreateCampaignResponse;
import software.amazon.awssdk.services.pinpoint.model.CreateCampaignRequest;
import software.amazon.awssdk.services.pinpoint.model.PinpointException;
```

 **Code** 

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

            CreateCampaignResponse result = client.createCampaign(
                    CreateCampaignRequest.builder()
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

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/pinpoint/src/main/java/com/example/pinpoint/CreateCampaign.java) on GitHub\.

## Send a message<a name="pinpoint-send-message"></a>

To send an SMS text message through Amazon Pinpoint, first build an [AddressConfiguration](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/AddressConfiguration.html) object to specify the `channelType()`\. \(In the following example, it’s set to `ChannelType.SMS` to indicate the message will be sent via SMS\.\) Initialize a [HashMap](https://docs.oracle.com/javase/8/docs/api/index.html?java/util/HashMap.html) to store the destination phone number and the `AddressConfiguration` object\. Next, build an [SMSMessage](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/SMSMessage.html) object containing the relevant values\. These include the `originationNumber`, the type of message \(`messageType`\), and the `body` of the message itself\.

When you have created the message, build the `SMSMessage` object into a [DirectMessageConfiguration](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/DirectMessageConfiguration.html) object\. Build your [Map](https://docs.oracle.com/javase/8/docs/api/index.html?java/util/Map.html) object and `DirectMessageConfiguration` object into a [MessageRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/MessageRequest.html) object\. Build a [SendMessagesRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/SendMessagesRequest.html) object, including your project ID \(`applicationId`\) and your `MessageRequest` object\. Call the `sendMessages()` method of your `PinpointClient`, passing in the `SendMessagesRequest` object\. Capture the result of this request as a [SendMessagesResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/pinpoint/model/SendMessagesResponse.html) object\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.DirectMessageConfiguration;
import software.amazon.awssdk.services.pinpoint.model.SMSMessage;
import software.amazon.awssdk.services.pinpoint.model.AddressConfiguration;
import software.amazon.awssdk.services.pinpoint.model.ChannelType;
import software.amazon.awssdk.services.pinpoint.model.MessageRequest;
import software.amazon.awssdk.services.pinpoint.model.SendMessagesRequest;
import software.amazon.awssdk.services.pinpoint.model.SendMessagesResponse;
import software.amazon.awssdk.services.pinpoint.model.MessageResponse;
import software.amazon.awssdk.services.pinpoint.model.PinpointException;
import java.util.HashMap;
import java.util.Map;
```

 **Code** 

```
    public static void sendSMSMessage(PinpointClient pinpoint, String message, String appId, String originationNumber, String destinationNumber) {

    try {

        Map<String, AddressConfiguration> addressMap =
                new HashMap<String, AddressConfiguration>();

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

        // Create a DirectMessageConfiguration object
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

        //Write out the result of sendMessage
        map1.forEach((k, v) -> System.out.println((k + ":" + v)));

    } catch (PinpointException e) {
        System.err.println(e.awsErrorDetails().errorMessage());
        System.exit(1);
    }
  }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/pinpoint/src/main/java/com/example/pinpoint/SendMessage.java) on GitHub\.

For more information, see the [Amazon Pinpoint Developer Guide](http://docs.aws.amazon.com/pinpoint/latest/developerguide/)\.