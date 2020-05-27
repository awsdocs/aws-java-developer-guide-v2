.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

##############
|SNS| examples
##############

.. meta::
   :description: How to use the AWS SDK for Java to work with Amazon Simple Notification Service (SNS).
   :keywords: AWS for Java SDK code examples, SNS, topic, create, list, endpoint, subscribe,
              publish, message, unsubscribe

With |SNS|, you can easily push real-time notification messages from your applications to subscribers
over multiple communication channels. This article describes how to perform some of the basic
functions of SNS.


.. _sns-create-topic:

Create a new topic
==================

A **topic** is a logical grouping of communication channels that defines to which systems a message
is sent. For example, fanning out a message to |LAMlong| and an HTTP webhook. You send messages to
|SNS| and then they are distributed to the channels defined in the topic, making the messages
available to subscribers.

To create a topic, first build a
:aws-java-class:`CreateTopicRequest <services/sns/model/CreateTopicRequest>` object with the name
of the topic set using the :methodname:`name()` method in the builder. Then, send the request
object to |SNS| using :aws-java-class:`SnsClient <services/sns/SnsClient>`â€™s
:methodname:`createTopic()` method. You can capture the result of this request as a
:aws-java-class:`CreateTopicResponse <services/sns/model/CreateTopicResponse>` object, as
demonstrated in the code snippet below.

**Imports**

.. literalinclude:: sns.java2.CreateTopic.import.txt
   :language: java

**Code**

.. literalinclude:: sns.java2.CreateTopic.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-sns:`complete example <CreateTopic.java>` on GitHub.


.. _sns-crelistate-topics:

List your SNS topics
====================

To retrieve a list of your existing |SNS| topics, build a
:aws-java-class:`ListTopicsRequest <services/sns/model/ListTopicsRequest>` object. Then, send the
request object to |SNS| using :classname:`SnsClient`’s :methodname:`listTopics()` method. You can
capture the result of this request as a
:aws-java-class:`ListTopicsResponse <services/sns/model/ListTopicsResponse>` object.

The code snippet below will print out the HTTP status code of the request along with a list of
:code:`ARN`s for your |SNS| topics.

**Imports**

.. literalinclude:: sns.java2.ListTopics.import.txt
   :language: java

**Code**

.. literalinclude:: sns.java2.ListTopics.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-sns:`complete example <ListTopics.java>` on GitHub.


.. _sns-subscribe-endpoint-topic:

Subscribe an endpoint to a topic
================================

Once you have a topic created, you can configure which communication channels will be endpoints for
that topic. Messages are distributed to these endpoints after being received by |SNS|.

To configure a communication channel as an endpoint for a topic, subscribe that endpoint to the
topic. To start, build a:aws-java-class:`SubscribeRequest <services/sns/model/SubscribeRequest>`
object. Specify the communication channel (for example, :code:`lambda` or :code:`email`) as the
:methodname:`protocol()`, set the :method:`endpoint()` to the relevant output location (for
example, the :code:`ARN` of a |LAM| function or an email address), and set the :code:`ARN` of the
topic to which you wish to subscribe as the :methodname:`topicArn()`. Then, send the request object
to SNS using :classname:`SnsClient`’s :methodname:`subscribe()` method. You can capture the result
of this request as a :aws-java-class:`SubscribeResponse <services/sns/model/SubscribeResponse>`
object.

The following code snippet shows how to subscribe an email address to a topic.

**Imports**

.. literalinclude:: sns.java2.SubscribeEmail.import.txt
   :language: java

**Code**

.. literalinclude:: sns.java2.SubscribeEmail.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-sns:`complete example <SubscribeEmail.java>` on GitHub.


.. _sns-publish-message-topic:

Publish a message to a topic
============================

Once you have a topic and one or more endpoints configured for it, you can publish a message to the
topic. To start, build a :aws-java-class:`PublishRequest <services/sns/model/PublishRequest>`
object. Specify the :methodname:`message()` to send and the :code:`ARN` of the topic
to which to send (:methodname:`topicArn()`). Then, send the request object to |SNS| using
:classname:`SnsClient`’s :methodname:`publish()` method. You can capture the result of this request
as a :aws-java-class:`PublishResponse <services/sns/model/PublishResponse>` object.

**Imports**

.. literalinclude:: sns.java2.PublishTopic.import.txt
   :language: java

**Code**

.. literalinclude:: sns.java2.PublishTopic.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-sns:`complete example <PublishTopic.java>` on GitHub.


.. _sns-unsubscribe-endpoint-topic:

Unsubscribe an endpoint from a topic
====================================

You can remove the communication channels configured as endpoints for a topic. After doing so, the
topic itself will continue to exist and distribute messages to any other endpoints configured for
that topic.

To remove a communication channel as an endpoint for a topic, unsubscribe that endpoint from the
topic. To start, build an
:aws-java-class:`UnsubscribeRequest <services/sns/model/UnsubscribeRequest>` object and set the
:code:`ARN` of the topic you wish to unsubscribe from as the :methodname:`subscriptionArn()`. Then,
send the request object to SNS using :classname:`SnsClient`’s :methodname:`unsubscribe()` method.
You can capture the result of this request as an
:aws-java-class:`UnsubscribeResponse <services/sns/model/UnsubscribeResponse>` object.

**Imports**

.. literalinclude:: sns.java2.Unsubscribe.import.txt
   :language: java

**Code**

.. literalinclude:: sns.java2.Unsubscribe.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-sns:`complete example <Unsubscribe.java>` on GitHub.


.. _sns-delete-topic:

Delete a topic
==============

To delete an |SNS| topic, first build a
:aws-java-class:`DeleteTopicRequest <services/sns/model/DeleteTopicRequest>` object with the
:code:`ARN` of the topic set as the :methodname:`topicArn()` method in the builder. Then, send the
request object to |SNS| using :classname:`SnsClient`’s :methodname:`deleteTopic()` method. You can
capture the result of this request as a
:aws-java-class:`DeleteTopicResponse <services/sns/model/DeleteTopicResponse>` object, as
demonstrated in the code snippet below.

**Imports**

.. literalinclude:: sns.java2.DeleteTopic.import.txt
   :language: java

**Code**

.. literalinclude:: sns.java2.DeleteTopic.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-sns:`complete example <DeleteTopic.java>` on GitHub.

For more information, see the |SNS-dg|_.
