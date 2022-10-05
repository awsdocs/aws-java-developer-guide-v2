--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Migrating from version 1\.x to 2\.x of the AWS SDK for Java<a name="migration"></a>

The AWS SDK for Java 2\.x is a major rewrite of the 1\.x code base built on top of Java 8\+\. It includes many updates, such as improved consistency, ease of use, and strongly enforced immutability\. This section describes the major features that are new in version 2\.x, and provides guidance on how to migrate your code to version 2\.x from 1\.x\.

**Topics**
+ [What’s new](#migration-whats-new)
+ [What's different between 1\.x and 2\.x](migration-whats-different.md)
+ [Using the SDK for Java 1\.x and 2\.x side\-by\-side](migration-side-by-side.md)

## What’s new<a name="migration-whats-new"></a>
+ You can configure your own HTTP clients\. See [HTTP Transport Configuration](http-configuration.md)\.
+ Async clients are now truly nonblocking and return `CompletableFuture` objects\. See [Asynchronous programming](asynchronous.md)\.
+ Operations that return multiple pages have autopaginated responses\. This enables you to focus your code on what to do with the response, without the need to check for and get subsequent pages\. See the [Pagination](pagination.md) 
+ SDK start time performance for AWS Lambda functions is improved\. See [SDK Start Time Performance Improvements](lambda-optimize-starttime.md) 
+ Version 2\.x supports a new shorthand method for creating requests\.  
**Example**  

  ```
  dynamoDbClient.putItem(request -> request.tableName(TABLE))
  ```

For more details about the new features and to see specific code examples, refer to the other sections of this guide\.
+  [Quick Start](get-started.md) 
+  [Setting up](setup.md) 
+  [Code examples for the AWS SDK for Java 2\.x](examples.md) 
+  [Using the SDK](using.md) 
+  [Security for the AWS SDK for Java](security.md) 