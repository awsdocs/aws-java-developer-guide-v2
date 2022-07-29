--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

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
+ Loading Region configuration from a custom endpoints\.json file is no longer supported\.