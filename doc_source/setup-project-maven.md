# Set up an Apache Maven project<a name="setup-project-maven"></a>

You can use [Apache Maven](https://maven.apache.org/) to set up and build AWS SDK for Java projects, or to build the SDK itself\.

## Prerequisites<a name="prerequisitesmaven"></a>

To use the AWS SDK for Java with Maven, you need the following:
+ Java *8\.0 or later*\. You can download the latest Java SE Development Kit software from [http://www\.oracle\.com/technetwork/java/javase/downloads/](http://www.oracle.com/technetwork/java/javase/downloads/)\. The AWS SDK for Java also works with [OpenJDK](https://openjdk.java.net/) and Amazon Corretto, a distribution of the Open Java Development Kit \(OpenJDK\)\. Download the latest OpenJDK version from [https://openjdk\.java\.net/install/index\.html](https://openjdk.java.net/install/index.html)\. Download the latest Amazon Corretto 8 or Amazon Corretto 11 version from [the Corretto page](http://aws.amazon.com/corretto/)\.
+  *Apache Maven*\. If you need to install Maven, go to [http://maven\.apache\.org/](http://maven.apache.org/) to download and install it\.

## Create a Maven project<a name="create-maven-project"></a>

To create a Maven project from the command line, run the following command from a terminal or command prompt window````\.

```
mvn -B archetype:generate \
 -DarchetypeGroupId=software.amazon.awssdk \
 -DarchetypeArtifactId=archetype-lambda -Dservice=s3 -Dregion=US_WEST_2 \
 -DgroupId=com.example.myapp \
 -DartifactId=myapp
```

**Note**  
Replace *com\.example\.myapp* with the full package namespace of your application\. Also replace *myapp* with your project name\. This becomes the name of the directory for your project\.

This command creates a Maven project using the archetype templating toolkit\. The archetype generates the scaffolding for an AWS Lambda function handler project \. This project archetype is preconfigured to compile with Java SE 8 and includes a dependency to the SDK for Java 2\.x\.

For more information about creating and configuring Maven projects, see the [Maven Getting Started Guide](https://maven.apache.org/guides/getting-started/)\.

## Configure the Java compiler for Maven<a name="configure-maven-compiler"></a>

If you created your project using the AWS Lambda project archetype as described earlier, this is already done for you\.

To verify that this configuration is present, start by opening the `pom.xml` file from the project folder you created \(for example, `myapp`\) when you executed the previous command\. Look on lines 11 and 12 to see the Java compiler version setting for this Maven project, and the required inclusion of the Maven compiler plugin on lines 71\-75\.

```
<project>
  <properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
  </properties>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>${maven.compiler.plugin.version}</version>
      </plugin>
    </plugins>
  </build>
</project>
```

If you create your project with a different archetype or by using another method, you must ensure that the Maven compiler plugin is part of the build and that its source and target properties are both set to **1\.8** in the `pom.xml` file\.

See the previous snippet for one way to configure these required settings\.

Alternatively, you can configure the compiler configuration inline with the plugin declaration, as follows\.

```
<project>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>1.8</source>
          <target>1.8</target>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

## Declare the SDK as a dependency<a name="sdk-as-dependency"></a>

To use the AWS SDK for Java in your project, you need to declare it as a dependency in your project’s `pom.xml` file\.

If you created your project using the project archetype as described earlier, the SDK is already configured as a dependency in your project\. We recommend that you update this configuration to reference the latest version of the AWS SDK for Java\. To do so, open the `pom.xml` file and change the `aws.java.sdk.version` property \(on line 16\) to the latest version\. The following is an example\.

```
<project>
  <properties>
    <aws.java.sdk.version>2.20.20</aws.java.sdk.version>
  </properties>
</project>
```

Find the latest version of the AWS SDK for Java in the [AWS SDK for Java API Reference version 2\.x](http://docs.aws.amazon.com/sdk-for-java/latest/reference/)\. The version is listed in the title of the page\.

If you created your Maven project in a different way, configure the latest version of the SDK for your project by ensuring that the `pom.xml` file contains the following\.

```
<project>
  <properties>
    <aws.java.sdk.version>2.20.20</aws.java.sdk.version>
  </properties>
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
</project>
```

**Note**  
Replace *2\.X\.X* in the `pom.xml` file with a valid version of the AWS SDK for Java version 2\.

## Set dependencies for SDK modules<a name="modules-dependencies"></a>

Now that you have configured the SDK, you can add dependencies for one or more of the AWS SDK for Java modules to use in your project\.

Although you can specify the version number for each component, you don’t need to because you already declared the SDK version in the `dependencyManagement` section\. To load a custom version of a given module, specify a version number for its dependency\.

If you created your project using the project archetype as described earlier, your project is already configured with multiple dependencies\. These include dependences for Lambda function handlers and Amazon S3, as follows\.

```
<project>
  <dependencies>
    <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>s3</artifactId>
    </dependency>
    <dependency>
      <groupId>com.amazonaws</groupId>
      <artifactId>aws-lambda-java-core</artifactId>
      <version>1.2.0</version>
    </dependency>
  </dependencies>
</project>
```

**Note**  
In the `pom.xml` example above, the dependencies are from different `groupId`s\. The `s3` dependency is from `software.amazon.awssdk`, whereas the `aws-lambda-java-core` dependency is from `com.amazonaws`\. The `bom` dependency management configuration affects artifacts for `software.amazon.awssdk`, so a version is needed for the `aws-lambda-java-core` artifact\.  
For the development of *Lambda function handlers* using the SDK for Java 2\.x, `aws-lambda-java-core` is the correct dependency\. However, if your application needs to manage Lambda resources, using operations such as `listFunctions`, `deleteFunction`, `invokeFunction`, and `createFunction`, your application requires the following dependency\.   

```
<groupId>software.amazon.awssdk</groupId>
<artifactId>lambda</artifactId>
```

Add the modules to your project for the AWS service and features you need for your project\. The modules \(dependencies\) that are managed by the AWS SDK for Java BOM are listed on the Maven central repository \([https://mvnrepository\.com/artifact/software\.amazon\.awssdk/bom/latest](https://mvnrepository.com/artifact/software.amazon.awssdk/bom/latest)\)\.

**Note**  
You can look at the `pom.xml` file from a code example to determine which dependencies you need for your project\. For example, if you’re interested in the dependencies for the Amazon S3 service, see [this example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java) from the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2) on GitHub\. \(Look for the `pom.xml` file under [/java2/example\_code/s3](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/s3/pom.xml)\.\)

### Build the entire SDK into your project<a name="build-the-entire-sdk-into-your-project"></a>

To optimize your application, we strongly recommend that you pull in only the components you need instead of the entire SDK\. However, to build the entire AWS SDK for Java into your project, declare it in your `pom.xml` file, as follows\.

```
<project>
  <dependencies>
    <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>aws-sdk-java</artifactId>
      <version>2.X.X</version>
    </dependency>
  </dependencies>
</project>
```

## Build your project<a name="build-project"></a>

After you configure the `pom.xml` file, you can use Maven to build your project\.

To build your Maven project from the command line, open a terminal or command prompt window, navigate to your project directory \(for example, `myapp`\), enter or paste the following command, then press Enter or Return\.

```
mvn package
```

This creates a single `.jar` file \(JAR\) in the `target` directory \(for example, `myapp/target`\)\. This JAR contains all of the SDK modules you specified as dependencies in your `pom.xml` file\.