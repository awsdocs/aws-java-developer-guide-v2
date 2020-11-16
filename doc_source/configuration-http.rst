.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

##################
HTTP configuration
##################

.. meta::
   :description: How to configure HTTP clients in the AWS SDK for Java.

You can change the default configuration for HTTP clients in applications you build with the
|sdk-java|. This section discusses how to configure HTTP clients and settings for the
|sdk-java-v2|. Some of the available settings including specifying which HTTP client to use,
as well as setting max concurrency, connection timeout, and maximum retries.

You can use the
:aws-java-class:`NettyNioAsyncHttpClient <http/nio/netty/NettyNioAsyncHttpClient>` or
:aws-java-class:`AwsCrtAsyncHttpClient <http/crt/AwsCrtAsyncHttpClient>` for 
asynchronous clients. For more information, see :doc:`configuration-http-netty`
or :doc:`configuration-http-crt`.

For synchronous clients, you can use
:aws-java-class:`ApacheHttpClient <http/apache/ApacheHttpClient>`. For more information about
Apache HTTPClient, see
`HttpClient Overview <https://hc.apache.org/httpcomponents-client-4.5.x/index.html>`_.

For a full list of options you can set with these clients, see the
:aws-java-class-root:`AWS SDK for Java 2.x API Reference<>`.

.. toctree::
   :maxdepth: 1
   :titlesonly:

   configuration-http-crt
   configuration-http-netty


Setting maximum connections
---------------------------

You can set the maximum allowed number of open HTTP connections by using the
:methodname:`maxConcurrency` method. The :methodname:`maxPendingConnectionAcquires`
method enables you to set the maximum requests allowed to queue up once max concurrency
is reached.

* Default for maxConcurrency: 50

* Default for maxPendingConnectionAcquires: 10_000

.. note::
   Set the maximum connections to the number of concurrent transactions to avoid
   connection contentions and poor performance.

Timeouts and error handling
---------------------------

You can set options related to timeouts and handling errors with HTTP connections.

* :strong:`Connection Timeout`

  The connection timeout is the amount of time (in milliseconds) that the HTTP connection will wait
  to establish a connection before giving up. The default is 10,000 ms.

  To set this value yourself, use the :aws-java-ref:`ClientConfiguration.setConnectionTimeout
  <ClientConfiguration.html#setConnectionTimeout-int->` method.

* :strong:`Connection Time to Live (TTL)`

  By default, the SDK will attempt to reuse HTTP connections as long as possible. In failure
  situations where a connection is established to a server that has been brought out of service,
  having a finite TTL can help with application recovery. For example, setting a 15 minute TTL will
  ensure that even if you have a connection established to a server that is experiencing issues,
  you'll reestablish a connection to a new server within 15 minutes.

  To set the HTTP connection TTL, use the :aws-java-ref:`ClientConfiguration.setConnectionTTL
  <ClientConfiguration.html#setConnectionTTL-long->` method.

* :strong:`Maximum Error Retries`

  The default maximum retry count for retriable errors is 3. You can set a different value
  by using the :aws-java-ref:`ClientConfiguration.setMaxErrorRetry
  <ClientConfiguration.html#setMaxErrorRetry-int->` method.

Local address
-------------

To set the local address that the HTTP client will bind to, use
:aws-java-ref:`ClientConfiguration.setLocalAddress
<ClientConfiguration.html#setLocalAddress-java.net.InetAddress->`.

