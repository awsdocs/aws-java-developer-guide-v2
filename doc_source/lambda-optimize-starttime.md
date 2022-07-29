--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Optimizing cold start performance for AWS Lambda<a name="lambda-optimize-starttime"></a>

Among the improvements in the AWS SDK for Java 2\.x is the SDK cold startup time for Java functions in Lambda\. This is the time it takes for a Java Lambda function to start up and respond to its first request\.

Version 2\.x includes three primary changes that contribute to this improvement:
+ Use of [jackson\-jr](https://github.com/FasterXML/jackson-jr), which is a serialization library that improves initialization time\.
+ Use of the [java\.time](https://docs.oracle.com/javase/8/docs/api/index.html?java/time.html) libraries for date and time objects\.
+ Use of [Slf4j](https://www.slf4j.org/) for a logging facade\.

You can gain additional SDK startup time improvement by setting specific configuration values on the client builder\. They each save some time at startup by reducing the amount of information your application needs to find for initialization\.

In your client builder, specify a region, use Environment Variable credentials provider, and specify UrlConnectionClient as the httpClient\. See the code snippet below for an example\.
+  [The region lookup process](http://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/java-dg-region-selection.html#default-region-provider-chain) for the SDK takes time\. By specifying a region, you can save up to 80ms of initialization time\.
**Note**  
By specifying an AWS region, the code will not run in other regions without modification\.
+ The process the SDK uses to look for credentials can take up to 90ms\. By using the [EnvironmentVariableCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/EnvironmentVariableCredentialsProvider.html) 
**Note**  
Using this credentials provider enables the code to be used in Lambda functions, but may not work on Amazon EC2 or other systems\.
+ Instantiation time for JDK’s [URLConnection](https://docs.oracle.com/javase/8/docs/api/index.html?java/net/URLConnection.html) library is much lower than Apache HTTP Client or Netty\. You can save up to 1 second by using this HTTP client\.

## Example client configuration<a name="example-client-configuration"></a>

```
S3Client client = S3Client.builder()
       .region(Region.US_WEST_2)
       .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
       .httpClient(UrlConnectionHttpClient.builder().build())
       .build();
```