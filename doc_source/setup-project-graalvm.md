--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Setting up a GraalVM Native Image project for the AWS SDK for Java<a name="setup-project-graalvm"></a>

With versions 2\.16\.1 and later, the AWS SDK for Java provides out\-of\-the\-box support for GraalVM Native Image applications\. Use the `archetype-app-quickstart` Maven archetype to set up a project with built\-in native image support\.

## Prerequisites<a name="setup-graalvmnativeimage-prereq"></a>
+ Complete the steps in [Setting up the AWS SDK for Java 2\.x](setup.md)\.
+ Install [GraalVM Native Image](https://www.graalvm.org/reference-manual/native-image/#install-native-image)\.

## Create a project using the archetype<a name="setup-graalvmnativeimage-project"></a>

To create a Maven project with built\-in native image support, in a terminal or command prompt window, use the following command\.

**Note**  
Replace *com\.example\.mynativeimageapp* with the full package namespace of your application\. Also replace *mynativeimageapp* with your project name\. This becomes the name of the directory for your project\.

```
mvn archetype:generate \
    -DarchetypeGroupId=software.amazon.awssdk \
    -DarchetypeArtifactId=archetype-app-quickstart \
    -DarchetypeVersion=2.16.1 \
    -DnativeImage=true \
    -DhttpClient=apache-client \
    -Dservice=s3 \
    -DgroupId=com.example.mynativeimageapp \
    -DartifactId=mynativeimageapp \
    -DinteractiveMode=false
```

This command creates a Maven project configured with dependencies for the AWS SDK for Java, Amazon S3, and the `ApacheHttpClient` HTTP client\. It also includes a dependency for the [GraalVM Native Image Maven plugin](https://www.graalvm.org/reference-manual/native-image/NativeImageMavenPlugin/), so that you can build native images using Maven\.

To include dependencies for a different Amazon Web Services, set the value of the `-Dservice` parameter to the artifict ID of that service\. For example, *dynamodb*, *iam*, *pinpoint*, etc\. For a complete list of artifact IDs, see the list of managed dependencies for [software\.amazon\.awssdk on Maven Central](https://mvnrepository.com/artifact/software.amazon.awssdk/bom/latest)\.

To use an asynchronous HTTP client, set the `-DhttpClient` parameter to `netty-nio-client`\. To use `UrlConnectionHttpClient` as the synchronous HTTP client instead of `apache-client`, set the `-DhttpClient` parameter to `url-connection-client`\.

## Build a native image<a name="build-graalvmnativeimage-project"></a>

After you create the project, run the following command from your project directory, for example, `mynativeimageapp`:

```
mvn package -P native-image
```

This creates a native image application in the `target` directory, for example, `target/mynativeimageapp`\.