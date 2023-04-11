# AWS STS examples using SDK for Java 2\.x<a name="java_sts_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with AWS STS\.

*Actions* are code excerpts that show you how to call individual service functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple functions within the same service\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#actions)

## Actions<a name="actions"></a>

### Assume a role<a name="sts_AssumeRole_java_topic"></a>

The following code example shows how to assume a role with AWS STS\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/sts#readme)\. 
  

```
    public static void assumeGivenRole(StsClient stsClient, String roleArn, String roleSessionName) {

        try {
            AssumeRoleRequest roleRequest = AssumeRoleRequest.builder()
                .roleArn(roleArn)
                .roleSessionName(roleSessionName)
                .build();

           AssumeRoleResponse roleResponse = stsClient.assumeRole(roleRequest);
           Credentials myCreds = roleResponse.credentials();

           // Display the time when the temp creds expire.
           Instant exTime = myCreds.expiration();
           String tokenInfo = myCreds.sessionToken();

           // Convert the Instant to readable date.
           DateTimeFormatter formatter =
                   DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                           .withLocale( Locale.US)
                           .withZone( ZoneId.systemDefault() );

           formatter.format( exTime );
           System.out.println("The token "+tokenInfo + "  expires on " + exTime );

       } catch (StsException e) {
           System.err.println(e.getMessage());
           System.exit(1);
       }
   }
```
+  For API details, see [AssumeRole](https://docs.aws.amazon.com/goto/SdkForJavaV2/sts-2011-06-15/AssumeRole) in *AWS SDK for Java 2\.x API Reference*\. 