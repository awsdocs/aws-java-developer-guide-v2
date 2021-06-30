.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#############################
Setting up the |sdk-java| 2.x
#############################

The |sdk-java| 2.x provides Java APIs for |AWSlong| (|AWS|). Using the SDK, you can build Java
applications that work with |S3|, |EC2|, |DDB|, and more.

This section provides information about how to set up your development environment and projects to
use the latest version (2.x) of the |sdk-java|.

.. toctree::

   setup-project-maven
   setup-project-gradle

.. _setup-overview:

Overview
========

To make requests to |AWS| using the |sdk-java|, you need the following:

   * An active AWS account

   * An |iamlong| (|iam|) user with:

      * A programmatic access key
      * Permissions to the |AWS| resources you'll access using your application

   * A development environment with:

      * Your access key configured as credentials for |AWS|
      * Java 8 or later
      * A build automation tool

.. _setup-awsaccount:

Create an AWS account
=====================

If you do not have an AWS account, visit
`the Amazon Web Services signup page <https://portal.aws.amazon.com/billing/signup>`_
and follow the on-screen prompts to create and activate a new account.

   For more detailed instructions, see
   `How do I create and activate a new AWS account? <https://aws.amazon.com/premiumsupport/knowledge-center/create-and-activate-aws-account/>`_.
   
   After you activate your new AWS account, follow the instructions in
   `Creating your first IAM admin user and group <https://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html#getting-started_create-admin-group-console>`_
   in the |IAM-ug|. Use this account instead of the root account when accessing the AWS Console.
   For more information, see
   `Security best practices in IAM <https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html#lock-away-credentials>`_
   in the |IAM-ug|_.

.. _setup-iamuser:

Create an |iam| user and programmatic access key
================================================

To use the |sdk-java| to access |AWS|, you need an |AWS| account and |AWS| credentials. To increase
the security of your AWS account, for access credentials, we recommend that you use an IAM user
instead of your AWS account credentials.

.. tip:: For an overview of IAM users and why they are important for the security
   of your account, see :aws-gr:`AWS security credentials <aws-security-credentials>`
   in the |AWS-gr|.

For instructions on creating an access key for an existing |iam| user, see
`Programmatic access <https://docs.aws.amazon.com/general/latest/gr/aws-sec-cred-types.html#access-keys-and-secret-access-keys>`_
in the |IAM-ug|_.

.. include:: common/procedure-create-iam-user.txt

.. _setup-credentials:

Set default credentials and Region
==================================

To make requests to |AWS| using the |sdk-java|, you must use cryptographically-signed
credentials issued by |AWS|. With AWS SDKs and Tools like the |sdk-java|, you use a programmatic
access key, consisting of an Access Key ID and and a Secret Access Key, as credentials. You should
set your credentials as the default credentials for accessing |AWS| with your application.

If you already have an |IAM| account created, see :ref:`setup-iamuser` for instructions on creating
a programmatic access key.

You should also set a default AWS Region for accessing |AWS| with your application. Some
operations require a Region to be set. For the best network performance, you can select a Region
that is geographically near to you or your customers.

The most common way to set the default credentials and |AWS| Region is to use the shared
:file:`config` and :file:`credentials` files. You can also set the default credentials and Region
using environment variables, using Java system properties or, for
your applications running on |EC2|, using
`ContainerCredentialsProvider <https://sdk.amazonaws.com/java/api/latest/index.html?software/amazon/awssdk/auth/credentials/ContainerCredentialsProvider.html>`_
or `InstanceProfileCredentialsProvider <https://sdk.amazonaws.com/java/api/latest/index.html?software/amazon/awssdk/auth/credentials/InstanceProfileCredentialsProvider.html>`_.

Setting the default credentials
-------------------------------

Select one of these options to set the default credentials:

.. include:: common/sdk-shared-credentials.txt

* Set the :code:`aws.accessKeyId` and :code:`aws.secretAccessKey` Java system properties.

   .. code-block:: sh
   
      java app.jar -Daws.accessKeyId=\
      "your_access_key_id" \
      -Daws.secretAccessKey=\
      "your_secret_access_key"

Setting the default |AWS| Region
--------------------------------

Select one of these options to set the default Region:

.. include:: common/sdk-shared-region.txt


For additional information about setting credentials and Region, see
`The .aws/credentials and .aws/config files <https://docs.aws.amazon.com/credref/latest/refdocs/creds-config-files.html>`_,
`AWS Region <https://docs.aws.amazon.com/credref/latest/refdocs/setting-global-region.html>`_,
and `Using environment variables <https://docs.aws.amazon.com/credref/latest/refdocs/environment-variables.html>`_
in the `AWS SDKs and Tools Reference Guide <https://docs.aws.amazon.com/credref/latest/refdocs>`_.

.. _setup-envtools:

Install Java and a build tool
=============================

Your development environment needs the following:

* Java 8 or later. The |sdk-java| works with the
  `Oracle Java SE Development Kit <https://www.oracle.com/java/technologies/javase-downloads.html>`_
  and with distributions of Open Java Development Kit (OpenJDK) such as
  `Amazon Corretto <https://aws.amazon.com/corretto/>`_,
  `Red Hat OpenJDK <https://developers.redhat.com/products/openjdk>`_, and
  `AdoptOpenJDK <https://adoptopenjdk.net/>`_.

* A build tool or IDE that supports Maven Central such as |mvnlong|, Gradle, or IntelliJ.

    * For information about how to install and use |MVN|, see http://maven.apache.org/.
    
    * For information about how to install and use Gradle, see https://gradle.org/.
    
    * For information about how to install and use IntelliJ IDEA, see https://www.jetbrains.com/idea/.

.. _setup-nextsteps:

Next steps
----------

Once you have your AWS account and development environment set up, create a Java project using your
preferred build tool. Import
`the Maven bill of materials (BOM) for the AWS SDK for Java 2.x from Maven Central <https://mvnrepository.com/artifact/software.amazon.awssdk/bom/latest>`_,
:code:`software.amazon.awssdk'. Then add dependencies for the services you'll use in your
application.

Example Maven :file:`pom.xml` file:

   .. code-block:: xml

        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
          <modelVersion>4.0.0</modelVersion>
          <properties>
            <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
          </properties>
          <groupId>com.example.myapp</groupId>
          <artifactId>myapp</artifactId>
          <packaging>jar</packaging>
          <version>1.0-SNAPSHOT</version>
          <name>myapp</name>
          <dependencyManagement>
            <dependencies>
              <dependency>
                <groupId>software.amazon.awssdk</groupId>
                <artifactId>bom</artifactId>
                <version>2.15.0</version>
                <type>pom</type>
                <scope>import</scope>
              </dependency>
            </dependencies>
          </dependencyManagement>
          <dependencies>
            <dependency>
              <groupId>junit</groupId>
              <artifactId>junit</artifactId>
              <version>3.8.1</version>
              <scope>test</scope>
            </dependency>
            <dependency>
              <groupId>software.amazon.awssdk</groupId>
              <artifactId>dynamodb</artifactId>
            </dependency>
            <dependency>
              <groupId>software.amazon.awssdk</groupId>
              <artifactId>iam</artifactId>
            </dependency>
            <dependency>
              <groupId>software.amazon.awssdk</groupId>
              <artifactId>kinesis</artifactId>
            </dependency>
            <dependency>
              <groupId>software.amazon.awssdk</groupId>
              <artifactId>s3</artifactId>
            </dependency>
          </dependencies>
          <build>
            <plugins>
              <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                  <source>8</source>
                  <target>8</target>
                </configuration>
              </plugin>
            </plugins>
          </build>
        </project>

Example :file:`build.gradle` file:

   .. code-block:: groovy
   
      group 'com.example.myapp'
      version '1.0'
   
      apply plugin: 'java'
   
      sourceCompatibility = 1.8
   
      repositories {
        mavenCentral()
      }
   
      dependencies {
        implementation platform('software.amazon.awssdk:bom:2.15.0')
        implementation 'software.amazon.awssdk:dynamodb'
        implementation 'software.amazon.awssdk:iam'
        implementation 'software.amazon.awssdk:kinesis'
        implementation 'software.amazon.awssdk:s3'
        testImplementation group: 'junit', name: 'junit', version: '4.11'
      }

For more information, see :doc:`setup-project-maven` or :doc:`setup-project-gradle`.
