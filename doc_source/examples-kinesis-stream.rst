.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

##########################################
Subscribing to Amazon Kinesis Data Streams
##########################################

.. meta::
    :description: How to use Amazon Kinesis data streams to get results from Amazon Kinesis using the AWS SDK for Java 2.x.
    :keywords: Kinesis, AWS SDK for Java 2.x, async streaming

The following examples show you how to retrieve and process data from |AKSlong|
using the :methodname:`subscribeToShard` method.
|AKS| now employs the enhanced fanout feature and a
low-latency HTTP/2 data retrieval API, making it
easier for developers to run multiple low-latency, high-performance applications
on the same |AK| Data Stream.


Set Up
======

First, create an asynchronous |AK| client and a
:aws-java-class:`SubscribeToShardRequest <services/kinesis/model/SubscribeToShardRequest>` object.
These objects are used in each of the following examples to subscribe to |AK| events.

**Imports**

.. literalinclude:: kinesis.java2.stream_example.import.txt
   :language: java

**Code**

.. literalinclude:: kinesis.java2.stream_example.setup.txt
   :dedent: 8
   :language: java


Use the Builder Interface
=========================

You can use the :methodname:`builder` method to simplify the creation of the
:aws-java-class:`SubscribeToShardResponseHandler <services/kinesis/model/SubscribeToShardResponseHandler>`.

Using the builder, you can set each lifecycle callback with a method call instead of implementing
the full interface.

**Code**

.. literalinclude:: kinesis.java2.stream_example.lifecycle_callback.txt
  :dedent: 4
  :language: java

For more control of the publisher, you can use the :methodname:`publisherTransformer`
method to customize the publisher.

**Code**

.. literalinclude:: kinesis.java2.stream_example.publish_transformer.txt
  :dedent: 4
  :language: java

See the :sdk-examples-java-kinesis:`complete example <KinesisStreamEx.java>` on GitHub.

Use a Custom Response Handler
=============================

For full control of the subscriber and publisher, implement the
:aws-java-class:`SubscribeToShardResponseHandler <services/kinesis/model/SubscribeToShardResponseHandler>` interface.

In this example, you implement the :methodname:`onEventStream` method, which allows
you full access to the publisher. This demonstrates how to transform the publisher to
event records for printing by the subscriber.

**Code**

.. literalinclude:: kinesis.java2.stream_example.custom_handler.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-kinesis:`complete example <KinesisStreamEx.java>` on GitHub.

Use the Visitor Interface
=========================

You can use a
:aws-java-class:`Visitor <services/kinesis/model/SubscribeToShardResponseHandler\Visitor>` object
to subscribe to specific events you're interested in watching.

**Code**

.. literalinclude:: kinesis.java2.stream_example.visitor.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-kinesis:`complete example <KinesisStreamEx.java>` on GitHub.

Use a Custom Subscriber
=======================

You can also implement your own custom subscriber to subscribe to the stream.

This code snippet shows an example subscriber.

**Code**

.. literalinclude:: kinesis.java2.stream_example.custom_subscriber.txt
   :dedent: 4
   :language: java

You can pass that custom subscriber to the :methodname:`subscribe` method, similarly
to preview examples. The following code snippet shows this example.

**Code**

.. literalinclude:: kinesis.java2.stream_example.subscribe.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-kinesis:`complete example <KinesisStreamEx.java>` on GitHub.

Write data records into a Kinesis data stream
=============================================
You can use the :aws-java-class:`KinesisClient <services/kinesis/KinesisClient>` object to write data records into a Kinesis data stream by using the
:methodname:`putRecords` method. To successfully invoke this method, create a :aws-java-class:`PutRecordsRequest <services/kinesis/model/PutRecordsRequest>` object. You
pass the name of the data steam to the :methodname:`streamName` method. Also you must pass the data by using the :data:`putRecords` method (as shown in the following code example).

**Imports**

.. literalinclude:: kinesis.java2.stream_rx_example.import.txt
  :language: java

In the following Java code example, notice that **StockTrade** object is used as the data to write to the Kinesis data stream. Before running this example, ensure that you have created the data steam. 
**Code**

.. literalinclude:: kinesis.java2.stream_example.subscribe.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-kinesis:`complete example <KinesisStreamEx.java>` on GitHub.


Use a Third-Party Library
==========================

You can use other third-party libraries instead of implementing a custom subscriber.
This example demonstrates using the RxJava implementation,
but you can use any library that implements the Reactive Streams interfaces.
See the `RxJava wiki page on Github <https://github.com/ReactiveX/RxJava/wiki>`_
for more information on that library.

To use the library, add it as a dependency. If you're using Maven, the example shows the
POM snippet to use.

**POM Entry**

.. literalinclude:: example_code/kinesis/pom.xml
   :lines: 13-17
   :language: xml

**Imports**

.. literalinclude:: kinesis.java2.stream_rx_example.import.txt
  :language: java

This example uses RxJava in the :methodname:`onEventStream` lifecycle method. This gives you
full access to the publisher, which can be used to create an Rx Flowable.

**Code**

.. literalinclude:: kinesis.java2.stream_rx_example.event_stream.txt
  :dedent: 8
  :language: java

You can also use the :methodname:`publisherTransformer` method with the :methodname:`Flowable`
publisher. You must adapt the :methodname:`Flowable` publisher to an `SdkPublisher`, as shown in
the following example.

**Code**

.. literalinclude:: kinesis.java2.stream_rx_example.publish_transform.txt
  :dedent: 8
  :language: java

See the :sdk-examples-java-kinesis:`complete example <KinesisStreamRxJavaEx.java>` on GitHub.

More Information
================

* :AK-api:`SubscribeToShardEvent <API_SubscribeToShardEvent>` in the |AK-api|
* :AK-api:`SubscribeToShard <API_SubscribeToShard>` in the |AK-api|
