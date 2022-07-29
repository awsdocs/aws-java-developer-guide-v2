--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# HTTP configuration<a name="http-configuration"></a>

You can change the default configuration for HTTP clients in applications you build with the AWS SDK for Java\. This section discusses HTTP clients and settings for the AWS SDK for Java 2\.x\.

For asynchronous clients, you can use the [NettyNioAsyncHttpClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/http/nio/netty/NettyNioAsyncHttpClient.html) or [AwsCrtAsyncHttpClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/http/crt/AwsCrtAsyncHttpClient.html)\. For more information, see [Configuring the Netty\-based HTTP client](http-configuration-netty.md) or [Configuring the AWS CRT\-based HTTP client](http-configuration-crt.md)\.

For synchronous clients, you can use [UrlConnectionHttpClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/http/urlconnection/UrlConnectionHttpClient.html) or [ApacheHttpClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/http/apache/ApacheHttpClient.html)\. For more information about Apache HTTPClient, see [HttpClient Overview](https://hc.apache.org/httpcomponents-client-4.5.x/index.html)\.

For a full list of options you can set with these clients, see the [AWS SDK for Java 2\.x API Reference](http://docs.aws.amazon.com/sdk-for-java/latest/reference/)\.

**Topics**
+ [Configuring the AWS CRT\-based HTTP client](http-configuration-crt.md)
+ [Configuring the Netty\-based HTTP client](http-configuration-netty.md)