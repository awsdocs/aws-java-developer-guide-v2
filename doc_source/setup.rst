.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#########################################
Setting up the |sdk-java| version 2 (2.x)
#########################################

The |sdk-java-v2| provides Java APIs for |AWSlong| (|AWS|). Using the SDK, you can build
Java applications that work with |S3|, |EC2|, |DDB|, and more.

This section provides information about how to set up your development environment and projects to
use the latest version (2.x) of the |sdk-java|.

.. _setup-prereq:

Prerequisites
=============

.. :topic:: To make requests to |aws| using the |sdk-java|, you need the following:

   * An active AWS account.
   * An |iamlong| (|iam|) user with:
      * A programmatic access key.
      * Appropriate permissions to the |AWS| resources you'll access using your application.
   * A Java development environment:
      * Configured to use your access key as credentials for AWS.
      * With Java 8 or later and |MVNlong| installed.

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

To use the |sdk-java| to access |AWSlong| (|AWS|), you need an |AWS| account and |AWS| credentials.
To increase the security of your AWS account, we recommend that you use an *IAM user*
to provide access credentials instead of using your AWS account credentials.

.. tip:: For an overview of IAM users and why they are important for the security
   of your account, see :aws-gr:`AWS Security Credentials <aws-security-credentials>`
   in the |AWS-gr|.

For instructions on creating an access key for an existing |iam| user, see
`Programmatic access <https://docs.aws.amazon.com/general/latest/gr/aws-sec-cred-types.html#access-keys-and-secret-access-keys>`_
in the |IAM-ug|_.

.. include:: common/procedure-create-iam-user.txt

.. _setup-javamaven:

Install Java and |mvnlong|
==========================

Your development environment needs to have Java 8 or later and |mvnlong| installed.

* The |sdk-java| works with the
  `Oracle Java SE Development Kit <https://www.oracle.com/java/technologies/javase-downloads.html>`_
  and with distributions of Open Java Development Kit (OpenJDK) such as
  `Amazon Corretto <https://aws.amazon.com/corretto/>`_,
  `Red Hat OpenJDK <https://developers.redhat.com/products/openjdk>`_, and
  `AdoptOpenJDK <https://adoptopenjdk.net/>`_.

* Go to http://maven.apache.org/ for information on how to install and use |MVN|.

.. _setup-credentials:

Configure credentials
=====================

Configure your development environment with your Access Key ID and the Secret Access Key. The
|sdk-java| uses this access key as credentials when your application makes requests to |aws|.



.. topic:: To configure credentials using the shared :file:`credentials` file

.. topic:: To configure credentials using the shared :file:`credentials` file

   #. In a text editor, create a new file. In the file, paste the following code:

      .. code-block:: xml
      
         [default]
         aws_access_key_id = YOUR_AWS_ACCESS_KEY_ID
         aws_secret_access_key = YOUR_AWS_SECRET_ACCESS_KEY

   #. In the text file you just created, replace *YOUR_AWS_ACCESS_KEY* with your unique AWS access
      key ID, and replace *YOUR_AWS_SECRET_ACCESS_KEY* with your unique AWS secret access key.

   #. Save the file. Refer to the following table for the correct location and file name based on
      your operating system.

      +---------------------+----------------------------------------------------------------------+
      | If you're using...  | Save the file as:                                                    |
      +=====================+======================================================================+
      | Windows             | :file:`C:\\Users\\<yourUserName>\\.aws\\credentials`                 |
      +---------------------+----------------------------------------------------------------------+
      | Linux, macOS, Unix  | :file:`~/.aws/credentials`                                           |
      +---------------------+----------------------------------------------------------------------+

      .. note:: Don't include a file extension when saving the credentials file.

.. _setup-projectdependencies:







...............................................................



To make requests to |AWSlong| using the |sdk-java|, you need an active AWS account, an |iamlong| (|iam|) user with a
programmatic access key and appropriate permissions to your |AWS| resources, and a Java development environment configured to
use that |iam| user's access key as credentials for AWS.






Install Java and |MVNlong|
==========================

Your development environment needs to have Java 8 or later and |mvnlong| installed.

* The |sdk-java| works with the
  `Oracle Java SE Development Kit <https://www.oracle.com/java/technologies/javase-downloads.html>`_
  and with distributions of Open Java Development Kit (OpenJDK) such as
  `Amazon Corretto <https://aws.amazon.com/corretto/>`_,
  `Red Hat OpenJDK <https://developers.redhat.com/products/openjdk>`_, and
  `AdoptOpenJDK <https://adoptopenjdk.net/>`_.

* Go to http://maven.apache.org/ for information on how to install and use |MVN|.

Set |AWS| credentials and region for development
================================================



Configure your development environment with your Access Key ID and the Secret Access Key. The
|sdk-java| uses this access key as credentials when your application makes requests to |AWS|.




.. topic:: To configure the shared credentials file

   #. In a text editor, create a new file. In the file, paste the following code:

      .. code-block:: xml
      
         [default]
         aws_access_key_id = YOUR_AWS_ACCESS_KEY_ID
         aws_secret_access_key = YOUR_AWS_SECRET_ACCESS_KEY

   #. In the text file you just created, replace *YOUR_AWS_ACCESS_KEY* with your unique AWS access
      key ID, and replace *YOUR_AWS_SECRET_ACCESS_KEY* with your unique AWS secret access key.

   #. Save the file. Refer to the following table for the correct location and file name based on
      your operating system.

      +---------------------+----------------------------------------------------------------------+
      | If you're using...  | Save the file as:                                                    |
      +=====================+======================================================================+
      | Windows             | :file:`C:\\Users\\<yourUserName>\\.aws\\credentials`                 |
      +---------------------+----------------------------------------------------------------------+
      | Linux, macOS, Unix  | :file:`~/.aws/credentials`                                           |
      +---------------------+----------------------------------------------------------------------+

      .. note:: Don't include a file extension when saving the credentials file.

Create a project and configure dependencies
===========================================

To 


...............................................................

To create the project for this tutorial, you first generate a |MVN| project. Next, you configure
your project with a dependency on |sdk-java| and for any AWS service you use, for example |s3|.
Then you configure the |MVN| compiler to use Java 1.8.

.. topic:: To create the |MVN| project

   #. Open a terminal or command prompt window and navigate to a directory of your choice, for
      example, your Desktop or Home folder.

   #. Use the following command to create a new directory called :file:`myapp` with a project
      configuration file (:file:`pom.xml`) and a basic Java class.

      .. code-block:: sh

         mvn -B archetype:generate \
           -DarchetypeGroupId=org.apache.maven.archetypes \
           -DgroupId=com.example.myapp \
           -DartifactId=myapp

**To configure your project with dependencies for the AWS SDK for Java and Amazon S3, and to use
Java 1.8**

In the folder :file:`myapp` that you created in the previous procedure, open the :file:`pom.xml`
file. Replace its contents with the following code, and then save your changes.

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
           <version>2.15.15</version>
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

..

   The :code:`dependencyManagement` section contains a dependency to the |sdk-java| and the
   :code:`dependencies` section has a dependency for |s3|. The |mvnlong| Compiler Plugin is
   configured in the :code:`build` section to use Java 1.8.

.. _get-started-code2:

Write the code
==============

After the project has been created and configured, edit the project's default class
(:classname:`App`) to use the example code below.

The example class below creates a service client for |s3| and then uses it to upload a text file. To
create a service client for |s3|, instantiate an :aws-java-class:`S3Client <services/s3/S3Client>`
object using the static factory method :methodname:`builder`. To upload a file to |s3|, first build
a :aws-java-class:`PutObjectRequest <services/s3/model/PutObjectRequest>` object, supplying a bucket
name and a key name. Then, call the |s3client|'s :methodname:`putObject` method, with a
:aws-java-class:`RequestBody <core/sync/RequestBody>` that contains the object content and the
:classname:`PutObjectRequest` object.

.. topic:: To create the Java class for this tutorial

   #. In your project folder (:file:`myapp`), navigate to the directory
      :file:`src/main/java/com/example/myapp`. Open the :file:`App.java` file.

   #. Replace its contents with the following code and save the file.

      .. code-block:: java

         package com.example.myapp;
         
         import java.io.IOException;
         import software.amazon.awssdk.core.sync.RequestBody;
         import software.amazon.awssdk.regions.Region;
         import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
         import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
         import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
         import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
         import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
         import software.amazon.awssdk.services.s3.model.PutObjectRequest;
         import software.amazon.awssdk.services.s3.model.S3Exception;
         import software.amazon.awssdk.services.s3.S3Client;
         
         
         public class App {
         
             public static void main(String[] args) throws IOException {
         
                 Region region = Region.US_WEST_2;
                 S3Client s3 = S3Client.builder().region(region).build();
         
                 String bucket = "bucket" + System.currentTimeMillis();
                 String key = "key";
         
                 tutorialSetup(s3, bucket, region);
         
                 System.out.println("Uploading object...");
         
                 s3.putObject(PutObjectRequest.builder().bucket(bucket).key(key)
                                 .build(),
                         RequestBody.fromString("Testing with the AWS SDK for Java"));
         
                 System.out.println("Upload complete");
                 System.out.printf("%n");
         
                 cleanUp(s3, bucket, key);
         
                 System.out.println("Closing the connection to Amazon S3");
                 s3.close();
                 System.out.println("Connection closed");
                 System.out.println("Exiting...");
             }
         
             public static void tutorialSetup(S3Client s3Client, String bucketName, Region region) {
                 try {
                     s3Client.createBucket(CreateBucketRequest
                             .builder()
                             .bucket(bucketName)
                             .createBucketConfiguration(
                                     CreateBucketConfiguration.builder()
                                             .locationConstraint(region.id())
                                             .build())
                             .build());
                     System.out.println("Creating bucket: " + bucketName);
                     s3Client.waiter().waitUntilBucketExists(HeadBucketRequest.builder()
                             .bucket(bucketName)
                             .build()).matched().response().isPresent();
                     System.out.println(bucketName +" is ready.");
                     System.out.printf("%n");
                 } catch (S3Exception e) {
                     System.err.println(e.awsErrorDetails().errorMessage());
                     System.exit(1);
                 }
             }
         
             public static void cleanUp(S3Client s3Client, String bucketName, String keyName) {
                 System.out.println("Cleaning up...");
                 try {
                     System.out.println("Deleting object: " + keyName);
                     DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(keyName).build();
                     s3Client.deleteObject(deleteObjectRequest);
                     System.out.println(keyName +" has been deleted.");
                     System.out.println("Deleting bucket: " + bucketName);
                     DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucketName).build();
                     s3Client.deleteBucket(deleteBucketRequest);
                     System.out.println(bucketName +" has been deleted.");
                     System.out.printf("%n");
                 } catch (S3Exception e) {
                     System.err.println(e.awsErrorDetails().errorMessage());
                     System.exit(1);
                 }
                 System.out.println("Cleanup complete");
                 System.out.printf("%n");
             }
         }

.. _get-started-run2:

Build and run the application
=============================

After the project is created and contains the example class, build and run the application. To view
the uploaded file in the |s3| console, edit the code to remove the cleanup steps and then rebuild the
project.

.. topic:: To build the project using |MVN|

   #. Open a terminal or command prompt window and navigate to your project directory (:file:`myapp`).

   #. Use the following command to build your project:

      .. code-block:: sh

         mvn package

.. topic:: To run the application

   #. Open a terminal or command prompt window and navigate to your project directory (:file:`myapp`).

   #. Use the following command to run the application.

      .. code-block:: sh

         mvn exec:java -Dexec.mainClass="com.example.myapp.App"

When you run the application, it uploads a new a text file to a new bucket in |s3|. If you copied
the code above exactly, it will also delete the file and bucket afterward.
      
.. topic:: To see the file in the |s3| console after it uploads

   #. In the :file:`App.java`, comment out :code:`s3.deleteObject(deleteObjectRequest);` and
      :code:`deleteBucket(s3, bucket);` and save the file.

   #. Rebuild the project by running :code:`mvn package` again.

   #. Upload the file by running :code:`mvn exec:java -Dexec.mainClass="com.example.myapp.App"` again.

   #. Sign in to `the S3 console <https://console.aws.amazon.com/s3/>`_ to view the new file in the
      newly-created bucket.
..

   After you view the file, clean up test resources by deleting the object and then deleting the
   bucket.

.. _get-started-success2:

Success!
--------

If your |MVN| project built and ran without error, then congratulations! You have successfully built
your first Java application using the |sdk-java|.

Cleanup
-------

To clean up the resources you created during this tutorial:

* In `the S3 console <https://console.aws.amazon.com/s3/>`_, delete any objects and any buckets
  created when you ran the application.

* In `the IAM console <https://console.aws.amazon.com/iam/home#/users>`_, delete the *TestSDK*
  user.

     If you delete this user, also remove the contents of the :file:`credentials` file you
     created during setup.

* Delete the project folder (:file:`myapp`).

.. _get-started-next2:

Next steps
==========

Now that you have the basics down, you can learn how to:

   * Program more with |s3|
   * Program with other Amazon Web Services, such as |DDB|, |EC2|, and |IAM|
   * Use and configure features of the |sdk-java|
   * Configure service client, credentials, regions, and more




.. toctree::
   :maxdepth: 1
   :titlesonly:

   signup-create-iam-user
   Downloading the SDK <setup-install>
   Setting up AWS credentials and region <setup-credentials>
   setup-project-maven
   setup-project-gradle
