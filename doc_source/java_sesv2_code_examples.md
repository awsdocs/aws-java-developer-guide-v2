--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Amazon SES API v2 examples using SDK for Java 2\.x<a name="java_sesv2_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Amazon SES API v2\.

*Actions* are code excerpts that show you how to call individual Amazon SES API v2 functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple Amazon SES API v2 functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w620aac15c13b9c67c13)

## Actions<a name="w620aac15c13b9c67c13"></a>

### Send an email<a name="sesv2_SendEmail_java_topic"></a>

The following code example shows how to send an Amazon SES API v2 email\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/ses#readme)\. 
Sends a message\.  

```
        public static void send(SesV2Client client,
                                String sender,
                                String recipient,
                                String subject,
                                String bodyHTML
        ){

            Destination destination = Destination.builder()
                .toAddresses(recipient)
                .build();

            Content content = Content.builder()
                .data(bodyHTML)
                .build();

            Content sub = Content.builder()
                .data(subject)
                .build();

            Body body = Body.builder()
                .html(content)
                .build();

            Message msg = Message.builder()
                .subject(sub)
                .body(body)
                .build();

            EmailContent emailContent = EmailContent.builder()
                .simple(msg)
                 .build();

            SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(destination)
                .content(emailContent)
                .fromEmailAddress(sender)
                .build();

            try {
                System.out.println("Attempting to send an email through Amazon SES " + "using the AWS SDK for Java...");
                client.sendEmail(emailRequest);
                System.out.println("email was sent");

            } catch (SesV2Exception e) {
                System.err.println(e.awsErrorDetails().errorMessage());
                System.exit(1);
            }
        }
```
+  For API details, see [SendEmail](https://docs.aws.amazon.com/goto/SdkForJavaV2/sesv2-2019-09-27/SendEmail) in *AWS SDK for Java 2\.x API Reference*\. 