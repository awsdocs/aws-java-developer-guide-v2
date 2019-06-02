.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

##############################
HTTP Transport Configuration
##############################

.. meta::
   :description: How to change max concurrency configuration by using the AWS SDK for Java.

You can use the
:aws-java-class:`NettyNioAsyncHttpClient <http/nio/netty/NettyNioAsyncHttpClient>`
for asynchronous clients or the
:aws-java-class:`ApacheHttpClient <http/apache/ApacheHttpClient>` for synchronous clients
to set HTTP transport settings. For a full list of options you can set with these clients,
see the :aws-java-class-root:`AWS SDK for Java 2.x Reference <>`.

Add a dependency on the :code:`netty-nio-client` in your POM to use the
:aws-java-class:`NettyNioAsyncHttpClient <http/nio/netty/NettyNioAsyncHttpClient>`.

**POM Entry**

.. literalinclude:: example_code/kinesis/pom.xml
   :lines: 20-24
   :language: xml


Maximum Connections
============================

You can set the maximum allowed number of open HTTP connections by using the
:methodname:`maxConcurrency` method. The :methodname:`maxPendingConnectionAcquires`
method enables you to set the maximum requests allowed to queue up once max concurrency
is reached.

* Default for maxConcurrency: 50

* Default for maxPendingConnectionAcquires: 10_000

.. important::
   Set the maximum connections to the number of concurrent transactions to avoid
   connection contentions and poor performance.

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
