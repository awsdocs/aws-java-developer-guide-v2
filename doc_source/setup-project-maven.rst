.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

###############################
Using the SDK with Apache Maven
###############################

You can use |mvnlong|_ to configure and build |sdk-java| projects or to build the SDK itself.

.. note:: You must have Maven installed to use the guidance in this topic. If it isn't already
   installed, visit http://maven.apache.org/ to download and install it.


Create a New Maven Package
==========================

To create a basic Maven package, open a terminal (command line) window and run the following.

.. code-block:: sh

   mvn -B archetype:generate \
     -DarchetypeGroupId=org.apache.maven.archetypes \
     -DgroupId=org.example.basicapp \
     -DartifactId=myapp

Replace *org.example.basicapp* with the full package namespace of your application. Replace *myapp*
with your project name (this becomes the name of the directory for your project).

By default, |mvn| creates a project template for you using the `quickstart
<http://maven.apache.org/archetypes/maven-archetype-quickstart/>`_ archetype. This creates a Java 1.5 project.
You must update your application to Java 1.8 to be compatible with |sdk-java| 2.0. To update to Java 1.8, add
the following to your :file:`pom.xml` file.

.. code-block:: xml

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


You can choose a particular archetype to use by adding the ``-DarchetypeArtifactId`` argument
to the ``archetype:generate`` command. To skip step to update the :file:`pom.xml` file, you can use the
following archetype that creates a Java 1.8 project from the start.

.. code-block:: sh
   :emphasize-lines: 3

   mvn archetype:generate -B \
      -DarchetypeGroupId=pl.org.miki \
      -DarchetypeArtifactId=java8-quickstart-archetype \
      -DarchetypeVersion=1.0.0 \
      -DgroupId=com.example \
      -DartifactId=sdk-sandbox \
      -Dversion=1.0 \
      -Dpackage=com.example

There are more archetypes available. See `Maven Archetypes
<https://maven.apache.org/archetypes/index.html>`_ for a list of archetypes packaged with
|mvn|.

.. tip:: For much more information about creating and configuring |mvn| projects, see the
   `Maven Getting Started Guide <https://maven.apache.org/guides/getting-started/>`_.


.. _configuring-maven:

Configure the SDK as a Maven Dependency
=======================================

To use the |sdk-java| in your project, you need to declare it as a dependency in your project's
:file:`pom.xml` file. You can import :ref:`individual components <configuring-maven-individual-components>`
or the :ref:`entire SDK <configuring-maven-entire-sdk>`. We strongly recommend
that you pull in only the components you need instead of the entire SDK.

.. _configuring-maven-individual-components:

Specifying Individual SDK Modules (Recommended)
-----------------------------------------------

To select individual SDK modules, use the |sdk-java| bill of materials (BOM) for Maven. This
ensures that the modules you specify use the same version of the SDK, and that they're compatible with
each other.

To use the BOM, add a :code-xml:`<dependencyManagement>` section to your application's
:file:`pom.xml` file. Add ``bom`` as a dependency and specify the version of the
SDK to use.

.. code-block:: xml

    <dependencyManagement>
      <dependencies>
        <dependency>
          <groupId>software.amazon.awssdk</groupId>
          <artifactId>bom</artifactId>
          <version>2.0.0-preview-1</version>
          <type>pom</type>
          <scope>import</scope>
        </dependency>
      </dependencies>
    </dependencyManagement>

To view the latest version of the |sdk-java| BOM that is available on Maven Central, see
https://mvnrepository.com/artifact/software.amazon.awssdk/bom. This page also shows
the modules (dependencies) that are managed by the BOM that you can include within the
:code-xml:`<dependencies>` section of your project's :file:`pom.xml` file.

You can now select individual modules from the SDK that you use to your application. Because you
already declared the SDK version in the BOM, you don't need to specify the version number for each
component.

.. code-block:: xml

    <dependencies>
      <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>s3</artifactId>
      </dependency>
      <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>dynamodb</artifactId>
      </dependency>
    </dependencies>


.. _configuring-maven-entire-sdk:

Importing All SDK Modules (Not Recommended)
-------------------------------------------

To pull in the *entire* SDK as a dependency, don't use the BOM method. Simply
declare it in your :file:`pom.xml` as follows.

.. code-block:: xml

  <dependencies>
    <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>aws-java-sdk</artifactId>
      <version>2.0.0-preview-1</version>
    </dependency>
  </dependencies>


Build Your Project
==================

Once you set up your project, you can build it using Maven's ``package`` command.

::

 mvn package

This creates your :file:`.jar` file in the :file:`target` directory.
