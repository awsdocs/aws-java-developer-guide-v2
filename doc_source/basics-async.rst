.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

########################
Asynchronous Programming
########################

.. meta::
   :description: How asynchronous programming works in the AWS SDK for Java 2.x

The |sdk-java-v2| features truly nonblocking asychronous clients that implement high
concurrency across a few threads. The |sdk-java| 1.11.x has
asynchronous clients that are wrappers around a thread pool and blocking synchronous clients
that don't provide the full benefit of nonblocking I/O.

Synchronous methods block your thread's execution until the client receives a response from the
service. Asynchronous methods return immediately, giving control back to the calling thread without
waiting for a response.

Because an asynchronous method returns before a response is available, you need a way to get the
response when it's ready. The |sdk-java-v2| asynchronous client methods return *CompletableFuture objects*
that allow you to access the response when it's ready.

.. _basics-async-non-streaming:

Non-Streaming Operations
========================

For non-streaming operations, asychronous method calls are similar to synchronous methods.
However, the asynchronous methods in the |sdk-java| return a
:javase-ref:`CompletableFuture <java/util/concurrent/CompletableFuture>`
object that contains the results of the asynchronous operation *in the future*.

Call the :code-java:`CompletableFuture` :methodname:`whenComplete()` method with an action to complete when
the result is available. :code-java:`CompletableFuture` implements the :code-java:`Future` interface
so you can also get the response object by calling the :methodname:`get()` method as well.

The following is an example of an asynchronous operation that calls a |DDBlong| function to get a list of tables, receiving a
:code-java:`CompletableFuture` that can hold a :aws-java-class:`ListTablesResponse
<services/dynamodb/model/ListTablesResponse>` object. The action defined in the call to :methodname:`whenComplete()`
is done only when the asynchronous call is complete.

**Imports**

.. literalinclude:: dynamodb.java2.dbasync.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.dbasync.main.txt
   :language: java

The following code example shows you how to retrieve an Item from a table by using
the Asynchronous client. Invoke the :methodname:`getItem` method of the |ddbasyncclient| and pass it a
:aws-java-class:`GetItemRequest <services/dynamodb/model/GetItemRequest>` object with the table
name and primary key value of the item you want. This is typically how you pass data that the operation requires.
In this example, notice that a String value is passed.

**Imports**

.. literalinclude:: dynamoasyn.java2.get_item.import.txt
   :language: java

**Code**

.. literalinclude:: dynamoasyc.java2.get_item.main.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-dynamodbasync:`complete example <DynamoDBAsyncGetItem.java>` on GitHub.


.. _basics-async-streaming:

Streaming Operations
======================

For streaming operations, you must provide an :aws-java-class:`AsyncRequestBody
<core/async/AsyncRequestBody>` to provide the content incrementally, or
an :aws-java-class:`AsyncResponseTransformer <core/async/AsyncResponseTransformer>`
to receive and process the response.

The following example uploads a file to |S3| asynchronously by using the
:methodname:`PutObject` operation.

**Imports**

.. literalinclude:: s3.java2.async_ops.import.txt
   :language: java

**Code**

.. literalinclude:: s3.java2.async_ops.main.txt
   :language: java
   
The following example gets a file from |S3| asynchronously by using the
:methodname:`GetObject` operation.

**Imports**

.. literalinclude:: s3.java2.async_stream_ops.import.txt
   :language: java

**Code**

.. literalinclude:: s3.java2.async_stream_ops.main.txt
   :language: java
   
Advanced Operations
===================

The |sdk-java-v2| uses `Netty <https://netty.io>`_ an asynchronous event-driven network application framework, to 
handle I/O threads. The |sdk-java-v2| creates an :methodname:`ExecutorService` behind Netty, to complete the futures returned 
from the HTTP client request through  to the Netty client. This abstraction reduces the risk of an application breaking the async
process if developers choose to stop or sleep threads. By default, 50 Threads are generated for each asychronous client, 
and managed in a queue within the :methodname:`ExecutorService`.

Advanced users can specify their thread pool size when creating an asychronous client using the following option when building.

**Code**

.. code-block:: java

        S3AsyncClient clientThread = S3AsyncClient.builder()
            .asyncConfiguration(
                b -> b.advancedOption(SdkAdvancedAsyncClientOption
                    .FUTURE_COMPLETION_EXECUTOR,
                    Executors.newFixedThreadPool(10)
                )
            )
            .build();

To optimize performance, you can manage your own thread pool executor, and include it when configuring your client.  

.. code-block:: java

        ThreadPoolExecutor executor = new ThreadPoolExecutor(50, 50,
                10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10_000),
                new ThreadFactoryBuilder()
                    .threadNamePrefix("sdk-async-response").build());
                
        // Allow idle core threads to time out
        executor.allowCoreThreadTimeOut(true);
        
        S3AsyncClient clientThread = S3AsyncClient.builder()
            .asyncConfiguration(
                b -> b.advancedOption(SdkAdvancedAsyncClientOption
                    .FUTURE_COMPLETION_EXECUTOR,
                    executor
                )
            )
            .build();

If you prefer to not use a thread pool, at all, use `Runnable::run` instead of using a thread pool executor. 

.. code-block:: java

    S3AsyncClient clientThread = S3AsyncClient.builder() 
        .asyncConfiguration( 
            b -> b.advancedOption(SdkAdvancedAsyncClientOption 
                    .FUTURE_COMPLETION_EXECUTOR, 
                    Runnable::run 
                )
            )
            .build();
