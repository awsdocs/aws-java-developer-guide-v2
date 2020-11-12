.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

###########################
Use the AWS CRT HTTP client
###########################

.. meta::
   :description: How to configure service clients to use the AWS Common Runtime as the HTTP client
   :keywords: AWS SDK for Java, crt, http, client, service client, configure, async

You can use the AWS Common Runtime (CRT) client as the HTTP client for asynchronous requests. You
can use the AWS CRT HTTP client to benefit from features such as improved performance, connection
health checks, and post-quantum TLS support.

For more information about the AWS CRT client, see **SOME COMMON TOPIC**.


Prerequisites
=============

Before you can use use the AWS CRT client, configure your project dependencies (for example, in your
:file:`pom.xml` or :file:`build.gradle` file) to use version :code:`2.14.13` or later (**?????**) of
the |sdk-java| and include version :code:`2.14.13-PREVIEW` of the artifactId
:literal:`aws-crt-client` your project's dependencies.

For example:

.. code-block:: xml

   <project>
      <dependencyManagement>
         <dependencies>
               <dependency>
                  <groupId>software.amazon.awssdk</groupId>
                  <artifactId>bom</artifactId>
                  <version>2.14.13</version>
                  <type>pom</type>
                  <scope>import</scope>
               </dependency>
         </dependencies>
      </dependencyManagement>
      <dependencies>
         <dependency>
               <groupId>software.amazon.awssdk</groupId>
               <artifactId>aws-crt-client</artifactId>
               <version>2.14.13-PREVIEW</version>
         </dependency>
      </dependencies>
   </project>

Use the AWS CRT client
======================

You can use the AWS CRT as the HTTP client for a specific service client, or you can create a single
HTTP client to share across multiple service clients. These options are recommended for most use
cases. Alternatively, you can set the AWS CRT client as the default HTTP client for all service
clients and requests in your application.

Use for a specific service client
---------------------------------

The following code snippet shows an example of how to use the AWS CRT client for a specific service
client.

.. code-block:: java

   S3AsyncClient.builder()
                .httpClientBuilder(AwsCrtAsyncHttpClient.builder()
                                                        .maxConcurrency(50))
                .build();

Use as a shared HTTP client
---------------------------

The following code snippet shows an example of how to use the AWS CRT client as a shared HTTP client.

.. note:: Your application must manage the lifecycle of an HTTP client instantiated outside of a
   service client builder.

.. code-block:: java

   SdkAsyncHttpClient crtClient = AwsCrtAsyncHttpClient.create()
   S3AsyncClient.builder()
                .httpClient(crtClient)
                .build();

Set as the default async HTTP client
------------------------------------

Instead of using Netty as the asynchronous HTTP client, you can set the AWS CRT client to be the
default for your application. You can set this in your project's dependencies (for example,
Maven :file:`pom.xml` file) by explictly excluding Netty. Alternatively, you can set the default
HTTP client via Java system property when you run your app or in your application code.

Remove Netty from the project dependencies
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Refer to the following snippet of a Maven :file:`pom.xml` file.

.. code-block:: xml

   <project>
      <dependencies>
         <dependency>
               <groupId>software.amazon.awssdk</groupId>
               <artifactId>s3</artifactId>
               <version>2.14.13</version>
               <exclusions>
                     <exclusion>
                           <groupId>software.amazon.awssdk</groupId>
                           <artifactId>netty-nio-client</artifactId>
                     </exclusion>
               </exclusions>
         </dependency>
         <dependency>
               <groupId>software.amazon.awssdk</groupId>
               <artifactId>aws-crt-client</artifactId>
               <version>2.14.13-PREVIEW</version>
         </dependency>
      </dependencies>
   </project>

Set via Java system property
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

To use the AWS CRT client as the default HTTP for your application, you can set the Java system property
:literal:`software.amazon.awssdk.http.async.service.impl` to a value of
:literal:`software.amazon.awssdk.http.crt.AwsCrtSdkHttpService`.

To set during application startup, run a command similar to the following.

.. code-block:: sh

   java app.jar -Dsoftware.amazon.awssdk.http.async.service.impl=\
   software.amazon.awssdk.http.crt.AwsCrtSdkHttpService

Use the following code snippet to set in your application code.

.. code-block:: java

   System.setProperty("software.amazon.awssdk.http.async.service.impl",
   "software.amazon.awssdk.http.crt.AwsCrtSdkHttpService");
