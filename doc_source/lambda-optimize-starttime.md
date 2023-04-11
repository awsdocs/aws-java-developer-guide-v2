# Reducing SDK startup time for AWS Lambda<a name="lambda-optimize-starttime"></a>

One of the goals of the AWS SDK for Java 2\.x is to reduce the startup latency for AWS Lambda functions\. The SDK contains changes that reduce startup time, which are discussed at the end of this topic\.

First, this topic focuses on changes that you can make to reduce cold start times\. These include making changes in your code structure and in the configuration of service clients\.

## Use the SDK’s `UrlConnectionHttpClient`<a name="lambda-quick-url"></a>

For *synchronous* scenarios, the SDK for Java 2\.x offers the [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/urlconnection/UrlConnectionHttpClient.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/urlconnection/UrlConnectionHttpClient.html) class, which is based on the JDK's HTTP client classes\. Because the `UrlConnectionHttpClient` is based on classes already on the classpath, there are no extra dependencies to load\. 

For information on adding the `UrlConnectionHttpClient` to your Lambda project and configuring its use, see [Configuring the URLConnection\-based HTTP client](http-configuration-url.md)\.

**Note**  
There are some feature limitations with the `UrlConnectionHttpClient` in comparison to the SDK's `[ApacheHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/apache/ApacheHttpClient.html)`\. The `ApacheHttpClient` is the default asynchronous HTTP client in the SDK\. For example, the `UrlConnectionHttpClient` does not support the HTTP PATCH method\.   
A handful of AWS API operations require PATCH requests\. Those operation names usually start with `Update*`\. The following are several examples\.  
[Several `Update*` operations](https://docs.aws.amazon.com/securityhub/1.0/APIReference/API_Operations.html) in the AWS Security Hub API and also the [BatchUpdateFindings](https://docs.aws.amazon.com/securityhub/1.0/APIReference/API_BatchUpdateFindings.html) operation
All Amazon API Gateway API [`Update*` operations](https://docs.aws.amazon.com/apigateway/latest/api/API_UpdateAccount.html)
[Several `Update*` operations](https://docs.aws.amazon.com/workdocs/latest/APIReference/API_UpdateDocument.html) in the Amazon WorkDocs API
If you might use the `UrlConnectionHttpClient`, first refer to the API Reference for the AWS service that you're using\. Check to see if the operations you need use the PATCH operation\.

## Use the SDK's `AwsCrtAsyncHttpClient`<a name="lambda-quick-crt"></a>

The [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/crt/AwsCrtAsyncHttpClient.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/crt/AwsCrtAsyncHttpClient.html) is the *asynchronous* counterpart for reducing Lambda startup time in the SDK\. 

The `AwsCrtAsyncHttpClient` is an asynchronous, non\-blocking HTTP client\. It's built on top of the Java bindings of the AWS Common Runtime, which is written in the C programming language\. Among the goals in the development of the AWS Common Runtime is fast performance\.

This guide's section on [configuring HTTP clients](http-configuration-crt.md) has information about adding an `AwsCrtAsyncHttpClient` to your Lambda project and configuring its use\.

## Remove unused HTTP client dependencies<a name="lambda-quick-remove-deps"></a>

Along with the explicit use of `UrlConnectionHttpClient` or `AwsCrtAsyncHttpClient`, you can remove other HTTP clients that the SDK brings in by default\. Lambda startup time is reduced when fewer libraries need to be loaded, so you should remove any unused artifacts that the JVM needs to load\.

The following snippet of a Maven `pom.xml` file shows the exclusion of the Apache\-based HTTP client and the Netty\-based HTTP client\. \(These clients aren't needed when you use the `UrlConnectionHttpClient`\.\) This example excludes the HTTP client artifacts from the S3 client dependency and adds the `url-connection-client` artifact, which brings in the `UrlConnectionHttpClient` class\.

```
<project>
    <properties>
        <aws.java.sdk.version>2.17.290</aws.java.sdk.version>
    <properties>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>software.amazon.awssdk</groupId>
                <artifactId>bom</artifactId>
                <version>${aws.java.sdk.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>url-connection-client</artifactId>
        </dependency>
        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>s3</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>software.amazon.awssdk</groupId>
                    <artifactId>netty-nio-client</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>software.amazon.awssdk</groupId>
                    <artifactId>apache-client</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>
</project>
```

If you use the `AwsCrtAsyncHttpClient`, replace the dependency on the `url-connection-client` to a dependency on the `aws-crt-client`\.

**Note**  
Add the `<exclusions>` element to all service client dependencies in your `pom.xml` file\.

## Configure service clients to shortcut lookups<a name="lambda-quick-clients"></a>

**Specify a region**  
When you create a service client, call the `region` method on the service client builder\. This shortcuts the SDK's default [Region lookup process](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/java-dg-region-selection.html#default-region-provider-chain) that checks several places for the AWS Region information\.  
To keep the Lambda code independent of the region, use the following code inside the `region` method\. This code accesses the `AWS_REGION` environment variable set by the Lambda container\.  

```
Region.of(System.getenv(SdkSystemSetting.AWS_REGION.environmentVariable()))
```

**Use the `EnvironmentVariableCredentialProvider`**  
Much like the default lookup behavior for the Region information, the SDK looks in several places for credentials\. By specifying the [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/auth/credentials/EnvironmentVariableCredentialsProvider.html](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/auth/credentials/EnvironmentVariableCredentialsProvider.html) when you build a service client, you save time in the SDK's lookup process\.  
Using this credentials provider enables the code to be used in Lambda functions, but might not work on Amazon EC2 or other systems\.

The following code snippet shows an S3 service client appropriately configured for use in a Lambda environment\.

```
S3Client client = S3Client.builder()
       .region(Region.of(System.getenv(SdkSystemSetting.AWS_REGION.environmentVariable())))
       .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
       .httpClient(UrlConnectionHttpClient.builder().build())
       .build();
```

## Initialize the SDK client outside of the Lambda function handler<a name="lambda-quick-initialize"></a>

We recommend initializing an SDK client outside of the Lambda handler method\. This way, if the execution context is reused, the initialization of the service client can be skipped\. By reusing the client instance and its connections, subsequent invocations of the handler method occur more quickly\.

In the following example, the `S3Client` instance is initialized in the constructor using a static factory method\. If the container that is managed by the Lambda environment is reused, the initialized `S3Client` instance is reused\.

```
public class App implements RequestHandler<Object, Object> {
    private final S3Client s3Client;

    public App() {
        s3Client = DependencyFactory.s3Client();
    }

    @Override
    public Object handle Request(final Object input, final Context context) {
         ListBucketResponse response = s3Client.listBuckets();
         // Process the response.
    }
}
```

## Minimize dependency injection<a name="lambda-quick-di"></a>

Dependency injection \(DI\) frameworks might take additional time to complete the setup process\. They might also require additional dependencies, which take time to load\.

If a DI framework is needed, we recommend using lightweight DI frameworks such as [Dagger](https://dagger.dev/dev-guide/)\.

## Use a Maven Archetype targeting AWS Lambda<a name="lambda-quick-maven"></a>

The AWS Java SDK team has developed a [Maven Archetype](https://github.com/aws/aws-sdk-java-v2/tree/master/archetypes/archetype-lambda) template to bootstrap a Lambda project with minimal startup time\. You can build out a Maven project from the archetype and know that the dependencies are configured suitably for the Lambda environment\. 

To learn more about the archetype and work through an example deployment, see this [blog post](https://aws.amazon.com/blogs/developer/bootstrapping-a-java-lambda-application-with-minimal-aws-java-sdk-startup-time-using-maven/)\.

## Consider Lambda SnapStart for Java<a name="lambda-quick-snapstart"></a>

If your runtime requirements are compatible, AWS offers [Lambda SnapStart for Java](https://docs.aws.amazon.com/lambda/latest/dg/snapstart.html)\. Lambda SnapStart is an infrastructure\-based solution that improves startup performance for Java functions\. When you publish a new version of a function, Lambda SnapStart initializes it and takes an immutable, encrypted snapshot of the memory and disk state\. SnapStart then caches the snapshot for reuse\.

## Version 2\.x changes that affect startup time<a name="example-client-configuration"></a>

In addition to changes that you make to your code, version 2\.x of the SDK for Java includes three primary changes that reduce startup time:
+ Use of [jackson\-jr](https://github.com/FasterXML/jackson-jr), which is a serialization library that improves initialization time
+ Use of the [java\.time](https://docs.oracle.com/javase/8/docs/api/index.html?java/time.html) libraries for date and time objects, which is part of the JDK
+ Use of [Slf4j](https://www.slf4j.org/) for a logging facade

## Additional resources<a name="lambda-quick-resources"></a>

The AWS Lambda Developer Guide contains a [section on best practices](https://docs.aws.amazon.com/lambda/latest/dg/best-practices.html) for developing Lambda functions that is not Java specific\.

For an example of building a cloud\-native application in Java that uses AWS Lambda, see this [workshop content](https://github.com/aws-samples/aws-lambda-java-workshop)\. The workshop discussion performance optimization and other best practices\.

You can consider using static images that are compiled ahead of time to reduce startup latency\. For example, you can use the SDK for Java 2\.x and Maven to [build a GraalVM native image](setup-project-graalvm.md)\.