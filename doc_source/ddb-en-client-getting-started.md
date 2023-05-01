# Get Started using the DynamoDB Enhanced Client API<a name="ddb-en-client-getting-started"></a>

The following tutorial introduces you to fundamentals that you need to work with the DynamoDB Enhanced Client API\.

## Add dependencies<a name="ddb-en-client-gs-dep"></a>

To begin working with the DynamoDB Enhanced Client API in your project, add a dependency on the `dynamodb-enhanced` Maven artifact\. This is shown in the following examples\. 

------
#### [ Maven ]

```
<project>
  <dependencyManagement>
   <dependencies>
      <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>bom</artifactId>
        <version><VERSION></version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
   </dependencies>
  </dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>dynamodb-enhanced</artifactId>
    </dependency>
  </dependencies>
  ...
</project>
```

Perform a search of the Maven central repository for the [latest version](https://search.maven.org/search?q=g:software.amazon.awssdk%20AND%20a:bom) and replace *<VERSION>* with this value\.

------
#### [ Gradle ]

```
repositories {
    mavenCentral()
}
dependencies {
    implementation(platform("software.amazon.awssdk:bom:<VERSION>"))
    implementation("software.amazon.awssdk:dynamodb-enhanced")
    ...
}
```

Perform a search of the Maven central repository for the [latest version](https://search.maven.org/search?q=g:software.amazon.awssdk%20AND%20a:bom) and replace *<VERSION>* with this value\.

------