--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Working with HTTP/2 in the AWS SDK for Java<a name="http2"></a>

HTTP/2 is a major revision of the HTTP protocol\. This new version has several enhancements to improve performance:
+ Binary data encoding provides more efficient data transfer\.
+ Header compression reduces the overhead bytes downloaded by the client, helping get the content to the client sooner\. This is especially useful for mobile clients that are already constrained on bandwidth\.
+ Bidirectional asynchronous communication \(multiplexing\) allows multiple requests and response messages between the client and AWS to be in flight at the same time over a single connection, instead of over multiple connections, which improves performance\.

Developers upgrading to the latest SDKs will automatically use HTTP/2 when it’s supported by the service they’re working with\. New programming interfaces seamlessly take advantage of HTTP/2 features and provide new ways to build applications\.

The AWS SDK for Java 2\.x features new APIs for event streaming that implement the HTTP/2 protocol\. For examples of how to use these new APIs, see [Working with Kinesis](examples-kinesis.md)\.