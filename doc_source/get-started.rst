.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

###################################
Get started with the |sdk-java| 2.x
###################################

.. meta::
   :description: Quickly build your first Java application that uses the latest version of the AWS
                 SDK for Java
   :keywords: start, setup, install, get started, java, aws, sdk for java, fast, first, running

The |sdk-java-v2| provides Java APIs for Amazon Web Services (AWS). Using the SDK, you can build
Java applications that work with |S3|, |EC2|, |DDB|, and more.

This tutorial shows you how you can use |mvnlong|_ to define dependencies for the |sdk-java| and
then write code that connects to |S3| to upload a file.

Follow these steps to complete this tutorial:

* :ref:`get-started-setup`
* :ref:`get-started-projectsetup`
* :ref:`get-started-code`
* :ref:`get-started-run`

.. _get-started-setup:

Step 1: Set up for this tutorial
================================

Before you begin this tutorial, you need an active AWS account, an |iamlong| (|iam|) user with a
programmatic access key and permissions to |s3|, and a Java development environment configured to
use that access key as credentials for AWS.

Follow these steps to set up for this tutorial:

   * :ref:`get-started-setup-account`
   * :ref:`get-started-setup-user`
   * :ref:`get-started-setup-javamaven`
   * :ref:`get-started-setup-credentials`

.. _get-started-setup-account:

Create an AWS account
---------------------

If you do not have an AWS account, visit
`the Amazon Web Services signup page <https://portal.aws.amazon.com/billing/signup>`_
and follow the on-screen prompts to create and activate a new account. For detailed instructions,
see `How do I create and activate a new AWS account? <https://aws.amazon.com/premiumsupport/knowledge-center/create-and-activate-aws-account/>`_.
   
   After you activate your new AWS account, follow the instructions in
   `Creating your first IAM admin user and group <https://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html#getting-started_create-admin-group-console>`_
   in the |IAM-ug|. Use this account instead of the root account when accessing the AWS Console.
   For more information, see
   `Security best practices in IAM <https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html>`_
   in the |IAM-ug|_.

.. _get-started-setup-user:

Create an |iam| user
--------------------

To complete this tutorial, you need to use credentials for an |iam| user that has read and write access to |s3|.
To make requests to |aws| using the |sdk-java|, create an access key to use as credentials.

.. topic:: To create an |iam| user with a programmatic access key and the required permissions for this tutorial

   #.	Sign in to `the IAM console <https://console.aws.amazon.com/iam/>`_

   #.	In the navigation pane on the left, choose :guilabel:`Users`. Then choose :guilabel:`Add user`. 

   #.	Enter *TestSDK* as the :guilabel:`User name` and select the :guilabel:`Programmatic access` checkbox. Choose :guilabel:`Next: Permissions`.

   #. Under **Set permissions**, select :guilabel:`Attach existing policies directly`.

   #. In the list of policies, select the checkbox for the **AmazonS3FullAccess** policy. Choose :guilabel:`Next: Tags`.

   #. Choose :guilabel:`Next: Review`. Then choose :guilabel:`Create user`.

   #. On the *Success* screen, choose **Download .csv**.

      The downloaded file contains the Access Key ID and the Secret Access Key for this tutorial.
      Treat your Secret Access Key as a password; save in a trusted location and do not share it.

      .. note:: You will **not** have another opportunity to download or copy the Secret Access Key.

.. _get-started-setup-javamaven:

Install Java and |mvnlong|
--------------------------

Your development environment needs to have Java 8 or later and |mvnlong| installed.

* For Java, use 
  `Oracle Java SE Development Kit <https://www.oracle.com/java/technologies/javase-downloads.html>`_
  , `Amazon Corretto <https://aws.amazon.com/corretto/>`_,
  `Red Hat OpenJDK <https://developers.redhat.com/products/openjdk>`_, or
  `AdoptOpenJDK <https://adoptopenjdk.net/>`_.

* For |MVN|, go to `https://maven.apache.org/ <https://maven.apache.org/>`_.

.. _get-started-setup-credentials:

Configure credentials
---------------------

Configure your development environment with your Access Key ID and the Secret Access Key. The
|sdk-java| uses this access key as credentials when your application makes requests to |aws|.

.. topic:: To configure credentials

   #. In a text editor, create a new file with the following code:

      .. code-block:: xml
      
         [default]
         aws_access_key_id = YOUR_AWS_ACCESS_KEY_ID
         aws_secret_access_key = YOUR_AWS_SECRET_ACCESS_KEY

   #. In the text file you just created, replace *YOUR_AWS_ACCESS_KEY* with your unique AWS access
      key ID, and replace *YOUR_AWS_SECRET_ACCESS_KEY* with your unique AWS secret access key.

   #. Save the file without a file extension. Refer to the following table for the correct location and file name based on
      your operating system.

      +---------------------+----------------------------------------------------------------------+
      | Operating system    | File name                                                             |
      +=====================+======================================================================+
      | Windows             | :file:`C:\\Users\\<yourUserName>\\.aws\\credentials`                 |
      +---------------------+----------------------------------------------------------------------+
      | Linux, macOS, Unix  | :file:`~/.aws/credentials`                                           |
      +---------------------+----------------------------------------------------------------------+

.. _get-started-projectsetup:

Step 2: Create the project
==========================

To create the project for this tutorial, you first create a |MVN| project. Next, you configure
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

   * In the folder :file:`myapp` that you created in the previous procedure, open the :file:`pom.xml` file. Replace its contents with the following code, and then save your changes.

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

.. _get-started-code:

Step 3: Write the code
======================

After the project has been created and configured, edit the project's default class
:classname:`App` to use the example code below.

The example class below creates a service client for |s3| and then uses it to upload a text file. To
create a service client for |s3|, instantiate an :aws-java-class:`S3Client <services/s3/S3Client>`
object using the static factory method :methodname:`builder`. To upload a file to |s3|, first build
a :aws-java-class:`PutObjectRequest <services/s3/model/PutObjectRequest>` object, supplying a bucket
name and a key name. Then, call the |s3client|'s :methodname:`putObject` method, with a
:aws-java-class:`RequestBody <core/sync/RequestBody>` that contains the object content and the
:classname:`PutObjectRequest` object.

.. topic:: To create the Java class for this tutorial

   #. In your project folder :file:`myapp`, navigate to the directory
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
                             .build());
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

.. _get-started-run:

Step 4: Build and run the application
=====================================

After the project is created and contains the example class, build and run the application. To view
the uploaded file in the |s3| console, edit the code to remove the cleanup steps and then rebuild the
project.

.. topic:: To build the project using |MVN|

   #. Open a terminal or command prompt window and navigate to your project directory :file:`myapp`.

   #. Use the following command to build your project:

      .. code-block:: sh

         mvn package

.. topic:: To run the application

   #. Open a terminal or command prompt window and navigate to your project directory :file:`myapp`.

   #. Use the following command to run the application.

      .. code-block:: sh

         mvn exec:java -Dexec.mainClass="com.example.myapp.App"

When you run the application, it uploads a new a text file to a new bucket in |s3|. Afterward, it
will also delete the file and bucket.
      
.. topic:: To see the file in the |s3| console after it uploads

   #. In :file:`App.java`, comment out the line :code:`cleanUp(s3, bucket, key);` and save the file.

   #. Rebuild the project by running :code:`mvn package`.

   #. Upload the file by running :code:`mvn exec:java -Dexec.mainClass="com.example.myapp.App"` again.

   #. Sign in to `the S3 console <https://console.aws.amazon.com/s3/>`_ to view the new file in the
      newly-created bucket.
..

   After you view the file, clean up test resources by deleting the object and then deleting the
   bucket.

.. _get-started-success:

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

.. _get-started-next:

Next steps
==========

Now that you have the basics down, you can learn how to:

   * Program more with |s3|
   * Program with other Amazon Web Services, such as |DDB|, |EC2|, and |IAM|
   * Use and configure features of the |sdk-java|
   * Configure service client, credentials, regions, and more
 