.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#########################################
Configuring the AWS CRT-based HTTP client
#########################################

.. meta::
   :description: How to configure service clients to use the AWS Common Runtime as the HTTP client
   :keywords: AWS SDK for Java, crt, http, client, service client, configure, async

The AWS Common Runtime (CRT) HTTP client is a new HTTP client you can use with the |sdk-java-v2|.
The CRT-based HTTP client is an asynchronous, non-blocking HTTP client built on top of the Java
bindings of the `AWS Common Runtime <https://github.com/awslabs/aws-crt-java>`_. You can use the
CRT-based HTTP client to benefit from features such as improved performance, connection health
checks, and post-quantum TLS support.

For asynchronous operations in the |sdk-java-v2|, you can use Netty
(:aws-java-class:`NettyNioAsyncHttpClient <http/nio/netty/NettyNioAsyncHttpClient>`) as the HTTP
client or you can use the new AWS Common Runtime (CRT) HTTP client
:aws-java-class:`AwsCrtAsyncHttpClient <http/crt/AwsCrtAsyncHttpClient>`. This topics shows you
how to configure the AWS CRT-based HTTP client.

Prerequisites
-------------

Before you can use use the AWS CRT client, you need to configure your project dependencies in your
:file:`pom.xml` or :file:`build.gradle` file to do the following:
   * Use version :code:`2.14.13` or later of the |sdk-java|.
   * Include version :code:`2.14.13-PREVIEW` of the :code:`artifactId` :literal:`aws-crt-client`.

The following code example shows how to configure your project dependencies.

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

Using the CRT-based HTTP client
-------------------------------

You can use the CRT-based HTTP client for a specific service client, or you can create a single
HTTP client to share across multiple service clients. These options are recommended for most use
cases. Alternatively, you can set the CRT-based client as the default HTTP client for all
asynchronous service clients and requests in your application.

The following code example shows how to use the CRT-based HTTP client for a specific
service client.

.. code-block:: java

   S3AsyncClient.builder()
                .httpClientBuilder(AwsCrtAsyncHttpClient.builder()
                                                        .maxConcurrency(50))
                .build();

The following code example shows how to use the CRT-based HTTP client as a shared
HTTP client.

.. code-block:: java

   SdkAsyncHttpClient crtClient = AwsCrtAsyncHttpClient.create()
   S3AsyncClient.builder()
                .httpClient(crtClient)
                .build();

.. note:: Your application must manage the lifecycle of an HTTP client instantiated outside of a
          service client builder. (A :methodname:`builder` is static factory method used by the
          |sdk-java| to connect to |aws| such as |s3| and |kms|. For more information, see
          :doc:`creating-clients`.)

Setting the CRT-based HTTP client as the default
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

For asynchronous operations in the |sdk-java-v2|, you can use Netty
(:aws-java-class:`NettyNioAsyncHttpClient <http/nio/netty/NettyNioAsyncHttpClient>`) or
the new AWS CRT-based HTTP client
(:aws-java-class:`AwsCrtAsyncHttpClient <http/crt/AwsCrtAsyncHttpClient>`) as the default
asynchronous HTTP client in the |sdk-java-v2|.

Instead of using Netty as the asynchronous HTTP client, you can set the CRT-based HTTP client to be
the default for your application. You can set this in your project's dependencies (for example,
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

Setting via Java system property
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

To use the CRT-based HTTP client as the default HTTP for your application, you can set the Java
system property :literal:`software.amazon.awssdk.http.async.service.impl` to a value of
:literal:`software.amazon.awssdk.http.crt.AwsCrtSdkHttpService`.

To set during application startup, run a command similar to the following.

.. code-block:: sh

   java app.jar -Dsoftware.amazon.awssdk.http.async.service.impl=\
   software.amazon.awssdk.http.crt.AwsCrtSdkHttpService

Use the following code snippet to set in your application code.

.. code-block:: java

   System.setProperty("software.amazon.awssdk.http.async.service.impl",
   "software.amazon.awssdk.http.crt.AwsCrtSdkHttpService");
   
Configuring the CRT-based HTTP client
=====================================

With the CRT-based HTTP client with in the |sdk-java|, you can configure various
settings including connection health checks and maximum idle time. You can also
configure post-quantum TLS support when you make requests to |kmslong| (|kms|).

Connection health checks
------------------------

You can configure connection health checks for the CRT-based HTTP client using the
:methodname:`connectionHealthChecks` method on the HTTP client builder. Refer to the
following example code snippet and the
:aws-java-class:`API documentation <http/crt/AwsCrtAsyncHttpClient>`.

.. code-block:: java

   AwsCrtAsyncHttpClient.builder()                           
                        .connectionHealthChecksConfiguration(
                               b -> b.minThroughputInBytesPerSecond(32000L)
                                     .allowableThroughputFailureInterval(Duration.ofSeconds(3)))
                        .build();

Post-quantum TLS support
------------------------

You can configure the CRT-based HTTP client to use post-quantum TLS when your application makes
requests to |kms|. Use the :methodname:`tlsCipherPreference` method on the HTTP client builder. Refer
to the following example code snippet and the
:aws-java-class:`API documentation <http/crtAwsCrtAsyncHttpClient.Builder>`.

.. code-block:: java

   SdkAsyncHttpClient awsCrtHttpClient = AwsCrtAsyncHttpClient.builder()
                                                                 .tlsCipherPreference(TlsCipherPreference.TLS_CIPHER_KMS_PQ_TLSv1_0_2019_06)
                                                                 .build();
   KmsAsyncClient kms = KmsAsyncClient.builder()
                                      .httpClient(awsCrtHttpClient)
                                      .build();
