# Amazon Pinpoint SMS and Voice API examples using SDK for Java 2\.x<a name="java_pinpoint-sms-voice_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Amazon Pinpoint SMS and Voice API\.

*Actions* are code excerpts that show you how to call individual service functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple functions within the same service\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#actions)

## Actions<a name="actions"></a>

### Send a voice message with Amazon Pinpoint SMS and Voice API<a name="pinpoint-sms-voice_SendVoiceMessage_java_topic"></a>

The following code example shows how to send a voice message with Amazon Pinpoint SMS and Voice API\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/pinpoint#readme)\. 
  

```
    public static void sendVoiceMsg(PinpointSmsVoiceClient client, String originationNumber, String destinationNumber ) {

        try {
            SSMLMessageType ssmlMessageType = SSMLMessageType.builder()
                .languageCode(languageCode)
                .text(ssmlMessage)
                .voiceId(voiceName)
                .build();

            VoiceMessageContent content = VoiceMessageContent.builder()
                .ssmlMessage(ssmlMessageType)
                .build();

            SendVoiceMessageRequest voiceMessageRequest = SendVoiceMessageRequest.builder()
                .destinationPhoneNumber(destinationNumber)
                .originationPhoneNumber(originationNumber)
                .content(content)
                .build();

            client.sendVoiceMessage(voiceMessageRequest);
            System.out.println("The message was sent successfully.");

        } catch (PinpointSmsVoiceException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [SendVoiceMessage](https://docs.aws.amazon.com/goto/SdkForJavaV2/pinpoint-sms-voice-2018-09-05/SendVoiceMessage) in *AWS SDK for Java 2\.x API Reference*\. 