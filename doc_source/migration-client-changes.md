# Additional client changes<a name="migration-client-changes"></a>

This topic describes additional changes to the default client in the AWS SDK for Java 2\.x\.

## Default client changes<a name="default-client-change-list"></a>
+ The default credential provider chain for Amazon S3 no longer includes anonymous credentials\. You must specify anonymous access to Amazon S3 manually by using the `AnonymousCredentialsProvider`\.
+ The following environment variables related to default client creation have been changed\.    
[\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/migration-client-changes.html)
+ The following system properties related to default client creation have been changed\.    
[\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/migration-client-changes.html)
+ The following system properties are no longer supported in 2\.x\.    
[\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/migration-client-changes.html)
+ Loading Region configuration from a custom `endpoints.json` file is no longer supported\.