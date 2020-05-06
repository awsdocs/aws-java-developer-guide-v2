.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#################################
Working with |SQS| Message Queues
#################################

.. meta::
   :description: How to create, list, delete, and get an Amazon SQS queue's URL.
   :keywords: AWS SDK for Java code example, Amazon SQS, queue operations, CreateQueueRequest
              ListQueuesRequest, GetQueueUrlRequest, DeleteMessageRequest


A *message queue* is the logical container used for sending messages reliably in |sqs|. There are
two types of queues: *standard* and *first-in, first-out* (FIFO). To learn more about queues and the
differences between these types, see the |sqs-dg|_.

This topic describes how to create, list, delete, and get the URL of an |sqs| queue by using the
|sdk-java|.


.. _sqs-create-queue:

Create a Queue
==============

Use the |sqsclient|'s :methodname:`createQueue` method, and provide a
:aws-java-class:`CreateQueueRequest <services/sqs/model/CreateQueueRequest>` object that describes
the queue parameters.

**Imports**

.. literalinclude:: sqs.java2.sqs_example.import.txt
   :language: java

**Code**

.. literalinclude:: sqs.java2.sqs_example.create_queue.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-sqs:`complete sample <SQSExample.java>` on GitHub.


.. _sqs-list-queues:

List Queues
===========

To list the |SQS| queues for your account, call the |sqsclient|'s :methodname:`listQueues`
method with a :aws-java-class:`ListQueuesRequest <services/sqs/model/ListQueuesRequest>` object.

Using the :methodname:`listQueues` overload without any parameters returns *all queues*, up to
1,000 queues. You can supply a queue name prefix to the :code-java:`ListQueuesRequest` object to limit
the results to queues that match that prefix.

**Imports**

.. literalinclude:: sqs.java2.sqs_example.import.txt
   :language: java

**Code**

.. literalinclude:: sqs.java2.sqs_example.list_queues.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-sqs:`complete sample <SQSExample.java>` on GitHub.


.. _sqs-get-queue-url:

Get the URL for a Queue
=======================

Call the |sqsclient|'s :methodname:`getQueueUrl` method.
with a :aws-java-class:`GetQueueUrlRequest <services/sqs/model/GetQueueUrlRequest>` object.

**Imports**

.. literalinclude:: sqs.java2.sqs_example.import.txt
   :language: java

**Code**

.. literalinclude:: sqs.java2.sqs_example.get_queue.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-sqs:`complete sample <SQSExample.java>` on GitHub.


.. _sqs-delete-queue:

Delete a Queue
==============

Provide the queue's :ref:`URL <sqs-get-queue-url>` to the
:aws-java-class:`DeleteMessageRequest <services/sqs/model/DeleteMessageRequest>` object.
Then call the |sqsclient|'s :methodname:`deleteQueue` method.

**Imports**

.. literalinclude:: sqs.java2.sqs_example.delete_queue.import.txt
   :language: java

**Code**

.. literalinclude:: sqs.java2.sqs_example.delete_queue.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-sqs:`complete sample <SQSExample.java>` on GitHub.

More Info
=========

* :sqs-dg:`How Amazon SQS Queues Work <sqs-how-it-works>` in the |sqs-dg|
* :sqs-api:`CreateQueue` in the |sqs-api|
* :sqs-api:`GetQueueUrl` in the |sqs-api|
* :sqs-api:`ListQueues` in the |sqs-api|
* :sqs-api:`DeleteQueues` in the |sqs-api|
