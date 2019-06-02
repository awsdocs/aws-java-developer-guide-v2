.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.


###############################################
Sending, Receiving, and Deleting |SQS| Messages
###############################################

.. meta::
   :description: How to send, receive and delete Amazon SQS messages.
   :keywords: AWS SDK for Java code examples, Amazon SQS, send message, receive message, delete
              message, SendMessageRequest, ReceiveMessageRequest


A message is a piece of data that can be sent and received by distributed components.
Messages are always delivered using an :doc:`SQS Queue <examples-sqs-message-queues>`.

.. _sqs-message-send:

Send a Message
==============

Add a single message to an |SQS| queue by calling the |sqsclient| client
:methodname:`sendMessage` method. Provide a :aws-java-class:`SendMessageRequest
<services/sqs/model/SendMessageRequest>` object that contains the queue's :ref:`URL
<sqs-get-queue-url>`, message body, and optional delay value (in seconds).

**Imports**

.. literalinclude:: sqs.java2.sqs_example.import.txt
   :language: java

**Code**

.. literalinclude:: sqs.java2.sqs_example.send_message.txt
   :dedent: 8
   :language: java


.. _sqs-messages-send-multiple:

Send Multiple Messages in a Request
===================================

Send more than one message in a single request by using the
|sqsclient| :methodname:`sendMessageBatch` method. This method takes a
:aws-java-class:`SendMessageBatchRequest <services/sqs/model/SendMessageBatchRequest>` that contains
the queue URL and a list of messages to send. (Each message is a :aws-java-class:`SendMessageBatchRequestEntry
<services/sqs/model/SendMessageBatchRequestEntry>`.)  You can also delay sending
a specific message by setting a delay value on the message.

**Imports**

.. literalinclude:: sqs.java2.sqs_example.import.txt
   :language: java

**Code**

.. literalinclude:: sqs.java2.sqs_example.send__multiple_messages.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-sqs:`complete sample <SQSExample.java>` on GitHub.


.. _sqs-messages-receive:

Retrieve Messages
=================

Retrieve any messages that are currently in the queue by calling the |sqsclient|
:methodname:`receiveMessage` method. This method takes a
:aws-java-class:`ReceiveMessageRequest <services/sqs/model/ReceiveMessageRequest>` that contains
the queue URL. You can also specify the maximum number of messages to return. Messages are
returned as a list of :aws-java-class:`Message <services/sqs/model/Message>` objects.

**Imports**

.. literalinclude:: sqs.java2.sqs_example.import.txt
   :language: java

**Code**

.. literalinclude:: sqs.java2.sqs_example.retrieve_messages.txt
   :dedent: 8
   :language: java

.. _sqs-messages-delete:

Delete a Message After Receipt
==============================

After receiving a message and processing its contents, delete the message from the queue by sending
the message's receipt handle and queue URL to the |sqsclient|
:methodname:`deleteMessage` method.

**Imports**

.. literalinclude:: sqs.java2.sqs_example.import.txt
   :language: java

**Code**

.. literalinclude:: sqs.java2.sqs_example.delete_message.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-sqs:`complete sample <SQSExample.java>` on GitHub.


More Info
=========

* :sqs-dg:`How Amazon SQS Queues Work <sqs-how-it-works>` in the |sqs-dg|
* :sqs-api:`SendMessage` in the |sqs-api|
* :sqs-api:`SendMessageBatch` in the |sqs-api|
* :sqs-api:`ReceiveMessage` in the |sqs-api|
* :sqs-api:`DeleteMessage` in the |sqs-api|
