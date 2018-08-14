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
   :description: How asynchronous programming works in the AWS SDK for Java 2.0

|sdk-java-v2| features truly non-blocking asychronous clients that implements high
concurrency across a few threads. |sdk-java| 1.11.x has
asynchronous clients that are wrappers around a thread pool and blocking synchronous clients
which do not provide the full benefit of non-blocking I/O.

Synchronous methods block your thread's execution until the client receives a response from the
service. Asynchronous methods return immediately, giving control back to the calling thread without
waiting for a response.

Because an asynchronous method returns before a response is available, you need a way to get the
response when it's ready. The |sdk-java-v2| asynchronous client methods return *CompletableFuture objects*
that allows you to access the response when it's ready.

.. _basics-async-non-streaming:

Non-streaming Operations
========================

For non-streaming operations, asychronous method calls are similar to synchronous methods
except that the asynchronous methods in the |sdk-java| return a
:javase-ref:`CompletableFuture <java/util/concurrent/CompletableFuture>`
object that contains the results of the asynchronous operation *in the future*.

Call the :code-java:`CompletableFuture` :methodname:`whenComplete()` method with an action to complete when
the result is available. :code-java:`CompletableFuture` implements the :code-java:`Future` interface
so you can get the response object by calling the :methodname:`get()` method as well.

Here is an example of an asynchronous operation that calls a |DDB| function to get a list of tables, receiving a
:code-java:`CompletableFuture` that can hold a :aws-java-class:`ListTablesResponse
<services/dynamodb/model/ListTablesResponse>` object. The action defined in the call to :methodname:`whenComplete()`
is only done when the asynchronous call is complete.

.. literalinclude:: example_code/dynamodbasync/src/main/java/com/example/dynamodbasync/DynamoDBAsync.java
   :language: java
   :lines: 26-

.. _basics-async-streaming:

Streaming Operations
======================

For streaming operations, you must provide an :aws-java-class:`AsyncRequestBody
<core/async/AsyncRequestBody>` to provide the content incrementally or
an :aws-java-class:`AsyncResponseTransformer <core/async/AsyncResponseTransformer>`
to receive and process the response.

Here is an example that uploads a file to |S3| asynchronously with the
:methodname:`PutObject` operation.

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3AsyncOps.java
   :language: java
   :lines: 26-

Here is an example that gets a file from |S3| asynchronously with the
:methodname:`GetObject` operation.

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3AsyncStreamOps.java
   :language: java
   :lines: 25-
