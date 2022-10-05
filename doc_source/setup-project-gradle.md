--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Setting up a Gradle project<a name="setup-project-gradle"></a>

You can use [Gradle](https://gradle.org/) to set up and build AWS SDK for Java projects\.

To manage SDK dependencies for your Gradle project, import the Maven bill of materials \(BOM\) for the AWS SDK for Java into the `build.gradle` file\.

**Note**  
In the following examples, replace *2\.15\.0* in the `build.gradle` file with the latest version of the AWS SDK for Java v2\. Find the latest version in the [AWS SDK for Java API Reference version 2\.x](http://docs.aws.amazon.com/sdk-for-java/latest/reference/)\.

1. Add the BOM to the *dependencies* section of the file\.

   ```
   ...
   dependencies {
    implementation platform('software.amazon.awssdk:bom:2.15.0')
   
    // Declare individual SDK dependencies without version
    ...
   }
   ```

1. Specify the SDK modules to use in the *dependencies* section\. For example, the following includes a dependency for Amazon Kinesis\.

   ```
   ...
   dependencies {
    ...
    implementation 'software.amazon.awssdk:kinesis'
    ...
   }
   ```

Gradle automatically resolves the correct version of your SDK dependencies by using the information from the BOM\.

The following is an example of a complete `build.gradle` file that includes a dependency for Kinesis\.

```
group 'aws.test'
version '1.0'

apply plugin: 'java'

sourceCompatibility = 1.8

repositories {
 mavenCentral()
}

dependencies {
 implementation platform('software.amazon.awssdk:bom:2.15.0')
 implementation 'software.amazon.awssdk:kinesis'
 testImplementation group: 'junit', name: 'junit', version: '4.11'
}
```

**Note**  
In the previous example, replace the dependency for Kinesis with the dependencies of the AWS services you will use in your project\. The modules \(dependencies\) that are managed by the AWS SDK for Java BOM are listed on Maven central repository \([https://mvnrepository\.com/artifact/software\.amazon\.awssdk/bom/latest](https://mvnrepository.com/artifact/software.amazon.awssdk/bom/latest)\)\.

For more information about specifying SDK dependencies by using the BOM, see [Setting up an Apache Maven project](setup-project-maven.md)\.