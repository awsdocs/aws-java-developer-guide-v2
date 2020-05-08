.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#############################
Use the SDK with Apache Maven
#############################

You can use |mvnlong|_ to configure and build |sdk-java| projects, or to build the SDK itself.

.. note:: The |sdk-java| requires Java *8.0 or later*. You can download the latest Java SE
          Development Kit software from http://www.oracle.com/technetwork/java/javase/downloads/.
          The |sdk-java| also works with `OpenJDK <https://openjdk.java.net/>`_ and |JDKlong| a
          distribution of the Open Java Development Kit (OpenJDK). Download the latest OpenJDK
          version from https://openjdk.java.net/install/index.html. Download the latest |JDKlong8|
          or |JDKlong11| version from https://aws.amazon.com/corretto/.

.. note:: You must have Maven installed to use the guidance in this topic. If it isn't already
          installed, visit http://maven.apache.org/ to download and install it.


.. _create-maven-project:

Create a new Maven project
==========================

To create a new Maven project from the command line, open a terminal or command prompt window,
type or paste the following command, then press Enter or Return:

.. code-block:: sh

   mvn -B archetype:generate \
     -DarchetypeGroupId=software.amazon.awssdk \
     -DarchetypeArtifactId=archetype-lambda -Dservice=S3 -Dregion=US_WEST_2 \
     -DgroupId=com.example.myapp \
     -DartifactId=myapp

.. note:: Replace *com.example.myapp* with the full package namespace of your application. Also
          replace *myapp* with your project name - this becomes the name of the directory for your
          project.

This command creates a new Maven project using the AWS Lambda project archetype. This project
archetype is preconfigured to compile with Java SE 8 and includes a dependency to the Java SDK.

For more information about creating and configuring Maven projects, see the
`Maven Getting Started Guide <https://maven.apache.org/guides/getting-started/>`_.



.. _configure-maven-compiler:

Configure the Java compiler for Maven
=====================================

If you created your project using the AWS Lambda project archetype as detailed above, this is
already done for you.

To verify that this configuration is present, start by opening the :file:`pom.xml` file from the
project folder you created (e.g. "myapp") when you executed the above command. Look on lines 11 and
12 to see the Java compiler version setting for this Maven project, as well as the required
inclusion of the Maven compiler plugin on lines 71-75.

.. code-block:: xml

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


If you create your project with a different archetype or using another method, you must ensure that
the Maven compiler plugin is part of the build and that its source and target properties are both
set to "1.8" in the :file:`pom.xml` file.

See the above snippet for one way to configure these required settings.

Alternatively, you can configure the compiler configuration inline with the plugin declaration, as
below:

.. code-block:: xml

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




.. _sdk-as-dependency:

Declare the SDK as a dependency
===============================

To use the |sdk-java| in your project, you need to declare it as a dependency in your project's
:file:`pom.xml` file.

If you created your project using the AWS Lambda project archetype as detailed above, the SDK is
already configured as a dependency in your project. It is recommended that you update this
configuration to reference the latest version of the AWS SDK for Java. To do so, open the
:file:`pom.xml` file and change the 'aws.java.sdk.version' property (on line 16) to the latest
version. See this example for a reference:

.. code-block:: xml

   <project>
       <properties>
           <aws.java.sdk.version>2.13.7</aws.java.sdk.version>
       </properties>
   </project>

Find the latest version of the |sdk-java| in the
:aws-java-class-root:`AWS SDK for Java 2.x Reference <>`.

If you created your Maven project a different way, configure the latest version of the SDK for
your project by ensuring that the :file:`pom.xml` file contains the following:

.. code-block:: xml

   <project>
       <dependencyManagement>
           <dependencies>
               <dependency>
                   <groupId>software.amazon.awssdk</groupId>
                   <artifactId>bom</artifactId>
                   <version>2.X.X</version>
                   <type>pom</type>
                   <scope>import</scope>
               </dependency>
           </dependencies>
       </dependencyManagement>
   </project>

.. note:: Replace *2.X.X* in the :file:`pom.xml` file with a valid version of the AWS SDK for
          Java v2. 


.. _modules-dependencies:

Set dependencies for SDK modules
================================

Now that you have the SDK configured, you can add dependencies for one or more of the |sdk-java|
modules to use in your project.

Although you can specify the version number for each component, you don't need to because you
already declared the SDK version in the dependencyManagement section. To load a custom version of
a given module, specify a version number for its dependency.

If you created your project using the AWS Lambda project archetype as detailed above, your project
is already configured with multiple dependencies, including for AWS Lambda and Amazon DynamoDB as
seen below.

.. code-block:: xml

   <project>
       <dependencies>
           <dependency>
               <groupId>software.amazon.awssdk</groupId>
               <artifactId>dynamodb</artifactId>
           </dependency>
           <dependency>
               <groupId>com.amazonaws</groupId>
               <artifactId>aws-lambda-java-core</artifactId>
               <version>1.2.0</version>
           </dependency>
       </dependencies>
   </project>

Add the modules to your project for the AWS service and features you need for your project. The
modules (dependencies) that are managed by the |sdk-java| BOM are listed on Maven central
repository (https://mvnrepository.com/artifact/software.amazon.awssdk/bom/latest).

.. tip:: You can look at the :file:`pom.xml` file from a code example to determine which
         dependencies you need for your project. For example, if you are interested in the
         dependencies for the |S3| service, see
         :sdk-examples-java-s3:`this example <S3ObjectOperations.java>` from the `AWS Code Examples
         Repository <https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2>`_ on
         GitHub. (Look for the :file:`pom.xml` file file under |javav2s3pom|_.)

 
Build the entire SDK into your project
--------------------------------------

To optimize your application, we strongly recommend that you pull in only the components you need
instead of the entire SDK. However, if you want to build the entire |sdk-java| into your project,
declare it in your :file:`pom.xml` file as follows:

.. code-block:: xml

   <project>
       <dependencies>
           <dependency>
               <groupId>software.amazon.awssdk</groupId>
               <artifactId>aws-sdk-java</artifactId>
               <version>2.X.X</version>
           </dependency>
       </dependencies>
   </project>
 


.. _build-project:

Build your project
=======================================

Once you have the :file:`pom.xml` file configured, you can use Maven to build your project.

To build your Maven project from the command line, open a terminal or command prompt window,
navigate to your project directory (e.g. :file:`myapp`), type or paste the following command, then
press Enter or Return:

.. code-block:: sh

   mvn package

This creates a single :file:`.jar` file (JAR) in the :file:`target` directory (e.g.
:file:`myapp/target`). This JAR contains all of the SDK modules you specified as dependencies in
your :file:`pom.xml` file.

