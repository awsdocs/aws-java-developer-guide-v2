# Logging with the SDK for Java 2\.x<a name="logging-slf4j"></a>

The AWS SDK for Java 2\.x uses [SLF4J](https://www.slf4j.org/manual.html), which is an abstraction layer that enables the use of any one of several logging systems at runtime\.

Supported logging systems include the Java Logging Framework and Apache[ Log4j 2](http://logging.apache.org/log4j/2.x/), among others\. This topic shows you how to use Log4j 2 as the logging system for working with the SDK\.

## Log4j 2 configuration file<a name="log4j-configuration-file"></a>

You typically use a configuration file, named`log4j2.xml` with Log4j 2\. Example configuration files are shown below\. To learn more about the values used in the configuration file, see the [manual for Log4j configuration](https://logging.apache.org/log4j/2.x/manual/configuration.html)\.

Place the `log4j2.xml` file in the `<project-dir>/src/main/resources` directory when using Maven\.

The `log4j2.xml` configuration file specifies properties such as [logging level](http://logging.apache.org/log4j/2.x/manual/configuration.html#Loggers), where logging output is sent \(for example, [to a file or to the console](http://logging.apache.org/log4j/2.x/manual/appenders.html)\), and the [format of the output](http://logging.apache.org/log4j/2.x/manual/layouts.html)\. The logging level specifies the level of detail that Log4j 2 outputs\. Log4j 2 supports the concept of multiple logging [https://logging.apache.org/log4j/2.x/manual/architecture.html#](https://logging.apache.org/log4j/2.x/manual/architecture.html#)\. The logging level is set independently for each hierarchy\. The main logging hierarchy that you use with the AWS SDK for Java 2\.x is `software.amazon.awssdk`\.

## Setting dependencies<a name="sdk-java-logging-classpath"></a>

To configure the Log4j 2 binding for SLF4J in Maven, use the following in your `pom.xml` file:

```
...
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-bom</artifactId>
            <version>VERSION</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
     </dependencies>
</dependencyManagement>
...
<dependency>
   <groupId>org.apache.logging.log4j</groupId>
   <artifactId>log4j-core</artifactId>
</dependency>
<dependency>
   <groupId>org.apache.logging.log4j</groupId>
   <artifactId>log4j-api</artifactId>
</dependency>
<dependency>
   <groupId>org.apache.logging.log4j</groupId>
   <artifactId>log4j-slf4j2-impl</artifactId>
</dependency>
...
```

Replace `VERSION` above with the latest version from [Maven central](https://search.maven.org/search?q=g:org.apache.logging.log4j%20AND%20a:log4j-bom)\.

## SDK\-specific errors and warnings<a name="sdk-java-logging-service"></a>

We recommend that you always leave the "software\.amazon\.awssdk" logger hierarchy set to "WARN" to catch any important messages from the SDK's client libraries\. For example, if the Amazon S3 client detects that your application hasn’t properly closed an `InputStream` and could be leaking resources, the S3 client reports it through a warning message to the logs\. This also ensures that messages are logged if the client has any problems handling requests or responses\.

The following `log4j2.xml` file sets the `rootLogger` to "WARN", which causes warning and error\-level messages from all loggers in the application to be output, *including* those in the "software\.amazon\.awssdk" hierarchy\. Alternatively, you can explicitly set the "software\.amazon\.awssdk" logger hierarchy to "WARN" if `<Root level="ERROR">` is used\.

**Example Log4j2\.xml configuration file**

This configuration will log messages at the "ERROR" and "WARN" levels to the console for all logger hierarchies\.

```
<Configuration status="WARN">
 <Appenders>
  <Console name="ConsoleAppender" target="SYSTEM_OUT">
   <PatternLayout pattern="%d{YYYY-MM-dd HH:mm:ss} [%t] %-5p %c:%L - %m%n" />
  </Console>
 </Appenders>

 <Loggers>
  <Root level="WARN">
   <AppenderRef ref="ConsoleAppender"/>
  </Root>
 </Loggers>
</Configuration>
```

## Request/response summary logging<a name="sdk-java-logging-request-response"></a>

Every request to an AWS service generates a unique AWS request ID that is useful if you run into an issue with how an AWS service is handling a request\. AWS request IDs are accessible programmatically through [https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/exception/SdkServiceException.html#requestId()](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/exception/SdkServiceException.html#requestId()) objects in the SDK for any failed service call, and can also be reported through the "DEBUG" log level of the "software\.amazon\.awssdk\.request" logger\.

The following `log4j2.xml` file enables a summary of requests and responses\.

```
<Configuration status="WARN">
 <Appenders>
  <Console name="ConsoleAppender" target="SYSTEM_OUT">
   <PatternLayout pattern="%d{YYYY-MM-dd HH:mm:ss} [%t] %-5p %c:%L - %m%n" />
  </Console>
 </Appenders>

 <Loggers>
  <Root level="ERROR">
   <AppenderRef ref="ConsoleAppender"/>
  </Root>
  <Logger name="software.amazon.awssdk" level="WARN" />
  <Logger name="software.amazon.awssdk.request" level="DEBUG" />
 </Loggers>
</Configuration>
```

Here is an example of the log output:

```
2022-09-23 16:02:08 [main] DEBUG software.amazon.awssdk.request:85 - Sending Request: DefaultSdkHttpFullRequest(httpMethod=POST, protocol=https, host=dynamodb.us-east-1.amazonaws.com, encodedPath=/, headers=[amz-sdk-invocation-id, Content-Length, Content-Type, User-Agent, X-Amz-Target], queryParameters=[])
2022-09-23 16:02:08 [main] DEBUG software.amazon.awssdk.request:85 - Received successful response: 200, Request ID: QS9DUMME2NHEDH8TGT9N5V53OJVV4KQNSO5AEMVJF66Q9ASUAAJG, Extended Request ID: not available
```

If you are interested in only the request ID use `<Logger name="software.amazon.awssdk.requestId" level="DEBUG" />`\.

## Verbose wire logging<a name="sdk-java-logging-verbose"></a>

It can be useful to see the exact requests and responses that the SDK for Java 2\.x sends and receives\. If you need access to this information, you can temporarily enable it by adding the necessary configuration depending on the HTTP client the service client uses\.

By default, synchronous service clients, such as the [S3Client](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/S3Client.html), use an underlying Apache HttpClient, and asynchronous service clients, such as the [S3AsyncClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/s3/S3AsyncClient.html), use a Netty non\-blocking HTTP client\.

Here is a breakdown of HTTP clients you can use for the two categories of service clients:


| Synchronous HTTP Clients | Asynchronous HTTP Clients | 
| --- | --- | 
| [ApacheHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/apache/ApacheHttpClient.html) \(default\) | [NettyNioAsyncHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/nio/netty/NettyNioAsyncHttpClient.html) \(default\) | 
| [UrlConnectionHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/urlconnection/UrlConnectionHttpClient.html) | [AwsCrtAsyncHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/crt/AwsCrtAsyncHttpClient.html) | 

Consult the appropriate tab below for configuration settings you need to add depending on the underlying HTTP client\.

**Warning**  
We recommend you only use wire logging for debugging purposes\. Disable it in your production environments because it can log sensitive data\. It logs the full request or response without encryption, even for an HTTPS call\. For large requests \(e\.g\., to upload a file to Amazon S3\) or responses, verbose wire logging can also significantly impact your application’s performance\.

------
#### [ ApacheHttpClient ]

Add the "org\.apache\.http\.wire" logger to the `log4j2.xml` configuration file and set the level to "DEBUG"\.

The following `log4j2.xml` file turns on full wire logging for the Apache HttpClient\.

```
<Configuration status="WARN">
 <Appenders>
  <Console name="ConsoleAppender" target="SYSTEM_OUT">
   <PatternLayout pattern="%d{YYYY-MM-dd HH:mm:ss} [%t] %-5p %c:%L - %m%n" />
  </Console>
 </Appenders>

 <Loggers>
  <Root level="WARN">
   <AppenderRef ref="ConsoleAppender"/>
  </Root>
  <Logger name="software.amazon.awssdk" level="WARN" />
  <Logger name="software.amazon.awssdk.request" level="DEBUG" />
  <Logger name="org.apache.http.wire" level="DEBUG" />
 </Loggers>
</Configuration>
```

An additional Maven dependency on the `log4j-1.2-api` artifact is required for wire logging with Apache as it uses 1\.2 under the hood\. Add the following to the `pom.xml` file to enable wire logging\.

```
<dependency>
 <groupId>org.apache.logging.log4j</groupId>
 <artifactId>log4j-1.2-api</artifactId>
</dependency>
```

The full set of Maven dependencies for using log4j 2, including wire logging for the Apache HttpClient, is:

```
...
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-bom</artifactId>
            <version>VERSION</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
     </dependencies>
</dependencyManagement>
...
<!-- The following 3 entries are needed for Log4j2 with SLF4J -->
<dependency>
   <groupId>org.apache.logging.log4j</groupId>
   <artifactId>log4j-core</artifactId>
</dependency>
<dependency>
   <groupId>org.apache.logging.log4j</groupId>
   <artifactId>log4j-api</artifactId>
</dependency>
<dependency>
   <groupId>org.apache.logging.log4j</groupId>
   <artifactId>log4j-slf4j2-impl</artifactId>
</dependency>

<!-- The following is needed for Apache HttpClient wire logging -->
<dependency>
   <groupId>org.apache.logging.log4j</groupId>
   <artifactId>log4j-1.2-api</artifactId>
</dependency>
...
```

------
#### [ UrlConnectionHttpClient ]

To log details for service clients that use the `UrlConnectionHttpClient`, first create a `logging.properties` file with the following contents:

```
handlers=java.util.logging.ConsoleHandler
java.util.logging.ConsoleHandler.level=FINEST
sun.net.www.protocol.http.HttpURLConnection.level=ALL
```

Set the following JVM system property with the full path of the `logging.properties`:

```
-Djava.util.logging.config.file=/full/path/to/logging.properties
```

This configuration will log the only the headers of the request and response, for example:

```
<Request>  FINE: sun.net.www.MessageHeader@35a9782c11 pairs: {GET /fileuploadtest HTTP/1.1: null}{amz-sdk-invocation-id: 5f7e707e-4ac5-bef5-ba62-00d71034ffdc}{amz-sdk-request: attempt=1; max=4}{Authorization: AWS4-HMAC-SHA256 Credential=<deleted>/20220927/us-east-1/s3/aws4_request, SignedHeaders=amz-sdk-invocation-id;amz-sdk-request;host;x-amz-content-sha256;x-amz-date;x-amz-te, Signature=e367fa0bc217a6a65675bb743e1280cf12fbe8d566196a816d948fdf0b42ca1a}{User-Agent: aws-sdk-java/2.17.230 Mac_OS_X/12.5 OpenJDK_64-Bit_Server_VM/25.332-b08 Java/1.8.0_332 vendor/Amazon.com_Inc. io/sync http/UrlConnection cfg/retry-mode/legacy}{x-amz-content-sha256: UNSIGNED-PAYLOAD}{X-Amz-Date: 20220927T133955Z}{x-amz-te: append-md5}{Host: tkhill-test1.s3.amazonaws.com}{Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2}{Connection: keep-alive}
<Response> FINE: sun.net.www.MessageHeader@70a36a6611 pairs: {null: HTTP/1.1 200 OK}{x-amz-id-2: sAFeZDOKdUMsBbkdjyDZw7P0oocb4C9KbiuzfJ6TWKQsGXHM/dFuOvr2tUb7Y1wEHGdJ3DSIxq0=}{x-amz-request-id: P9QW9SMZ97FKZ9X7}{Date: Tue, 27 Sep 2022 13:39:57 GMT}{Last-Modified: Tue, 13 Sep 2022 14:38:12 GMT}{ETag: "2cbe5ad4a064cedec33b452bebf48032"}{x-amz-transfer-encoding: append-md5}{Accept-Ranges: bytes}{Content-Type: text/plain}{Server: AmazonS3}{Content-Length: 67}
```

To see the request/response bodies, add `-Djavax.net.debug=all` to the JVM properties\. This additional property logs a great deal of information, including all SSL information\. 

Within the log console or log file, search for `"GET"` or `"POST"` to quickly go to the section of the log containing actual requests and responses\. Search for `"Plaintext before ENCRYPTION"` for requests and `"Plaintext after DECRYPTION"` for responses to see the full text of the headers and bodies\.

------
#### [ NettyNioAsyncHttpClient ]

If your asynchronous service client uses the default `NettyNioAsyncHttpClient`, add two additional loggers to your `log4j2.xml` file to log HTTP headers and request/response bodies\.

```
<Logger name="io.netty.handler.logging" level="DEBUG" />
<Logger name="io.netty.handler.codec.http2.Http2FrameLogger" level="DEBUG" />
```

Here is a complete `log4j2.xml` example:

```
<Configuration status="WARN">
    <Appenders>
        <Console name="ConsoleAppender" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{YYYY-MM-dd HH:mm:ss} [%t] %-5p %c:%L - %m%n" />
        </Console>
    </Appenders>

    <Loggers>
        <Root level="WARN">
            <AppenderRef ref="ConsoleAppender"/>
        </Root>
        <Logger name="software.amazon.awssdk" level="WARN" />
        <Logger name="software.amazon.awssdk.request" level="DEBUG" />
        <Logger name="io.netty.handler.logging" level="DEBUG" />
        <Logger name="io.netty.handler.codec.http2.Http2FrameLogger" level="DEBUG" />
    </Loggers>
</Configuration>
```

These settings log all header details and request/response bodies\.

------
#### [ AwsCrtAsyncHttpClient ]

If you have configured your service client to use an instance of `AwsCrtAsyncHttpClient`, you can log details by setting JVM system properties or programmatically\.


|  | 
| --- |
|  Log to a file at "Debug" level  | 
|  Using system properties: <pre>-Daws.crt.log.level=Trace <br />-Daws.crt.log.destination=File <br />-Daws.crt.log.filename=<path to file></pre>  |  Programmatically: <pre>import import software.amazon.awssdk.crt.Log;<br /><br />// Execute this statement before constructing the SDK service client.<br />Log.initLoggingToFile(Log.LogLevel.Trace, "<path to file>");</pre>  | 
|  Log to the console at "Debug" level  | 
|  Using system properties: <pre>-Daws.crt.log.level=Trace <br />-Daws.crt.log.destination=Stdout</pre>  |  Programmatically: <pre>import import software.amazon.awssdk.crt.Log;<br /><br />// Execute this statement before constructing the SDK service client.<br />Log.initLoggingToStdout(Log.LogLevel.Trace);</pre>  | 

For security reasons, at the "Trace" level the `AwsCrtAsyncHttpClient` logs only response headers\. Request headers, request bodies, and response bodies are not logged\.

------