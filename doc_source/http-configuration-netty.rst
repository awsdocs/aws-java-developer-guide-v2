.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#######################################
Configuring the Netty-based HTTP client 
#######################################

.. meta::
   :description: How to configure HTTP clients in the AWS SDK for Java.

For asynchronous operations in the |sdk-java-v2|, you can use Netty
(:aws-java-class:`NettyNioAsyncHttpClient <http/nio/netty/NettyNioAsyncHttpClient>`) as the HTTP
client or you can use the new AWS Common Runtime (CRT) HTTP client
:aws-java-class:`AwsCrtAsyncHttpClient <http/crt/AwsCrtAsyncHttpClient>`. This topics shows you
how to configure the Netty-based HTTP client.

For a full list of options you can set with these clients, see the
:aws-java-class-root:`AWS SDK for Java API Reference version 2.x<>`.


Prerequisite
============

Before you can use use the Netty client, you need to configure your project dependencies in your
:file:`pom.xml` or :file:`build.gradle` file to include version :code:`2.0.0` or later of the
:code:`artifactId` :literal:`netty-nio-client`.

The following code example shows how to configure your project dependencies.

.. code-block:: xml

   <dependency>
      <artifactId>netty-nio-client</artifactId>
      <groupId>software.amazon.awssdk</groupId>
      <version>2.0.0</version>
   </dependency>

Configuring service clients
===========================

Use the HTTP client builder to have the SDK manage its lifecycle.
The HTTP client will be closed for you when the service client is shut down.

**Imports**

.. literalinclude:: kinesis.java2.client_configuration.import.txt
  :language: java

**Code**

.. literalinclude:: kinesis.java2.client_configuration.client.txt
  :dedent: 8
  :language: java

You can also pass the HTTP client directly to the service client if you want to manage
the lifecycle yourself.

**Code**

.. literalinclude:: kinesis.java2.client_configuration.httpclient.txt
  :dedent: 8
  :language: java
