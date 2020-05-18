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

Prerequisites
=============

To use the |sdk-java| with Maven, you need the following:

* Java *8.0 or later*. You can download the latest Java SE
  Development Kit software from http://www.oracle.com/technetwork/java/javase/downloads/.
  The |sdk-java| also works with `OpenJDK <https://openjdk.java.net/>`_ and |JDKlong|, a
  distribution of the Open Java Development Kit (OpenJDK). Download the latest OpenJDK
  version from https://openjdk.java.net/install/index.html. Download the latest |JDKlong8|
  or |JDKlong11| version from https://aws.amazon.com/corretto/.
* *Apache Maven*. If you need to install Maven,
  go to http://maven.apache.org/ to download and install it.


.. _create-maven-project:

Create a Maven project
======================

To create a Maven project from the command line, open a terminal or command prompt window,
enter or paste the following command, and then press Enter or Return.

.. code-block:: sh

   mvn -B archetype:generate \
     -DarchetypeGroupId=software.amazon.awssdk \
     -DarchetypeArtifactId=archetype-lambda -Dservice=s3 -Dregion=US_WEST_2 \
     -DgroupId=com.example.myapp \
     -DartifactId=myapp

.. note:: Replace *com.example.myapp* with the full package namespace of your application. Also
          replace *myapp* with your project name. This becomes the name of the directory for your
          project.

This command creates a Maven project using the |LAMlong| project archetype. This project
archetype is preconfigured to compile with Java SE 8 and includes a dependency to the |sdk-java|.

For more information about creating and configuring Maven projects, see the
`Maven Getting Started Guide <https://maven.apache.org/guides/getting-started/>`_.



.. _configure-maven-compiler:

Configure the Java compiler for Maven
=====================================

If you created your project using the |LAMlong| project archetype as described earlier, this is
already done for you.

To verify that this configuration is present, start by opening the :file:`pom.xml` file from the
project folder you created (for example, :file:`myapp`) when you executed the previous command. Look
on lines 11 and 12 to see the Java compiler version setting for this Maven project, and the required
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


If you create your project with a different archetype or by using another method, you must ensure
that the Maven compiler plugin is part of the build and that its source and target properties are
both set to **1.8** in the :file:`pom.xml` file.

See the previous snippet for one way to configure these required settings.

Alternatively, you can configure the compiler configuration inline with the plugin declaration, as
follows.

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

If you created your project using the project archetype as described earlier, the SDK is already
configured as a dependency in your project. We recommend that you update this configuration to
reference the latest version of the |sdk-java|. To do so, open the :file:`pom.xml` file and change
the :code:`aws.java.sdk.version` property (on line 16) to the latest version. The following is an
example.

.. code-block:: xml

   <project>
       <properties>
           <aws.java.sdk.version>2.13.7</aws.java.sdk.version>
       </properties>
   </project>

Find the latest version of the |sdk-java| in the
:aws-java-class-root:`AWS SDK for Java API Reference version 2.x<>`.

If you created your Maven project in a different way, configure the latest version of the SDK for
your project by ensuring that the :file:`pom.xml` file contains the following.

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

.. note:: Replace *2.X.X* in the :file:`pom.xml` file with a valid version of the |sdk-java|
          version 2. 


.. _modules-dependencies:

Set dependencies for SDK modules
================================

Now that you have configured the SDK, you can add dependencies for one or more of the |sdk-java|
modules to use in your project.

Although you can specify the version number for each component, you don't need to because you
already declared the SDK version in the :code:`dependencyManagement` section. To load a custom
version of a given module, specify a version number for its dependency.

If you created your project using the project archetype as described earlier, your project is
already configured with multiple dependencies. These include dependences for |LAM| and |DDBlong|,
as follows.

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
modules (dependencies) that are managed by the |sdk-java| BOM are listed on the Maven central
repository (https://mvnrepository.com/artifact/software.amazon.awssdk/bom/latest).

.. tip:: You can look at the :file:`pom.xml` file from a code example to determine which
         dependencies you need for your project. For example, if you're interested in the
         dependencies for the |S3| service, see
         :sdk-examples-java-s3:`this example <S3ObjectOperations.java>` from the `AWS Code Examples
         Repository <https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2>`_ on
         GitHub. (Look for the :file:`pom.xml` file file under |javav2s3pom|_.)

 
Build the entire SDK into your project
--------------------------------------

To optimize your application, we strongly recommend that you pull in only the components you need
instead of the entire SDK. However, to build the entire |sdk-java| into your project,
declare it in your :file:`pom.xml` file, as follows.

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
==================

After you configure the :file:`pom.xml` file, you can use Maven to build your project.

To build your Maven project from the command line, open a terminal or command prompt window,
navigate to your project directory (for example, :file:`myapp`), enter or paste the following
command, then press Enter or Return.

.. code-block:: sh

   mvn package

This creates a single :file:`.jar` file (JAR) in the :file:`target` directory (for example,
:file:`myapp/target`). This JAR contains all of the SDK modules you specified as dependencies in
your :file:`pom.xml` file.

