# Set up a Gradle project<a name="setup-project-gradle"></a>

You can use [Gradle](https://gradle.org/) to set up and build AWS SDK for Java projects\.

To manage SDK dependencies for your Gradle project, import the Maven bill of materials \(BOM\) for the AWS SDK for Java into the `build.gradle` file\.

**Note**  
In the following examples, replace *2\.20\.56* in the Gradle build file with the latest version of the AWS SDK for Java v2\. Find the latest version in the [AWS SDK for Java API Reference version 2\.x](http://docs.aws.amazon.com/sdk-for-java/latest/reference/)\.

1. Add the BOM \(Bill of Materials\) to the *dependencies* section of the file\.

   ```
   ...
   dependencies {
       implementation platform('software.amazon.awssdk:bom:2.20.56')
   
   
    // Declare individual SDK dependencies without version.
    ...
   }
   ```

1. Specify the SDK modules to use in the *dependencies* section\. For example, the following includes a dependency for Amazon Simple Storage Service\.

   ```
   ...
   dependencies {
    ...
    implementation 'software.amazon.awssdk:s3'
    ...
   }
   ```

Gradle automatically resolves the correct version of your SDK dependencies by using the information from the BOM\.

The following shows a complete Gradle build file using the Groovy and Kotlin DSLs\. The build file contains dependencies for Amazon S3, authentication, logging, and testing\. The source and target version of Java is version 11\.

------
#### [ Groovy DSL \(build\.gradle\) ]

```
plugins {
    id 'java'
}

group = 'aws.test'
version = '1.0-SNAPSHOT'

sourceCompatibility JavaVersion.VERSION_11
targetCompatibility JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation platform('software.amazon.awssdk:bom:2.20.56')
    implementation 'software.amazon.awssdk:s3:'
    implementation 'software.amazon.awssdk:sso'
    implementation 'software.amazon.awssdk:ssooidc'
    implementation 'org.slf4j:slf4j-simple:2.0.5'
    testImplementation platform('org.junit:junit-bom:5.9.1')
    testImplementation 'org.junit.jupiter:junit-jupiter'
}

test {
    useJUnitPlatform()
}
```

------
#### [ Kotlin DSL \(build\.gradle\.kts\) ]

```
plugins {
    `java-library`
}

group = "aws.test"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("software.amazon.awssdk:bom:2.20.56"))
    implementation("software.amazon.awssdk:s3")
    implementation("software.amazon.awssdk:sso")
    implementation("software.amazon.awssdk:ssooidc")
    implementation("org.slf4j:slf4j-simple:2.0.5")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
```

------

**Note**  
In the previous example, replace the dependency for Amazon S3 with the dependencies of the AWS services you will use in your project\. The modules \(dependencies\) that are managed by the AWS SDK for Java BOM are listed on [the Maven central repository](https://mvnrepository.com/artifact/software.amazon.awssdk/bom/latest)\.

For more information about specifying SDK dependencies by using the BOM, see [Setting up an Apache Maven project](setup-project-maven.md)\.