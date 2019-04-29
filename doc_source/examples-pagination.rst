.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

############################
Retrieving Paginated Results
############################

.. meta::
    :description: How to use stream to get automatic pagination in AWS SDK for Java 2.x.
    :keywords: Pagination, AWS SDK for Java 2.x, S3 code examples, async pagination,
               listObjectsV2Paginator, listTablesPaginator, paginate

Many AWS operations return paginated results when the response object is too large
to return in a single response. In the |sdk-java| 1.0, the response contained a token
you had to use to retrieve the next page of results. New in the |sdk-java-v2| are
autopagination methods that make multiple
service calls to get the next page of results for you automatically.
You only have to write code that processes the results. Additionally both types of methods
have synchronous and asynchronous versions. See :doc:`basics-async` for more detail about
asynchronous clients.

The following examples use |S3| and |DDBlong| operations to demonstrate the
various methods of retrieving your data from paginated responses.

.. include:: includes/examples-note.txt

======================
Synchronous Pagination
======================

These examples use the synchronous pagination methods for listing objects in an
|S3| bucket.


.. _iterate-pages:

Iterate over Pages
==================

Build a :aws-java-class:`ListObjectsV2Request <services/s3/model/ListObjectsV2Request>`
and provide a bucket name. Optionally you can provide the maximum number of keys to
retrieve at one time.
Pass it to the |s3client|'s :methodname:`listObjectsV2Paginator` method. This method
returns a :aws-java-class:`ListObjectsV2Iterable <services/s3/paginators/ListObjectsV2Iterable>`
object, which is an ``Iterable`` of the
:aws-java-class:`ListObjectsV2Response <services/s3/model/ListObjectsV2Response>` class.

The first example demonstrates using the paginator object to iterate through all the
response pages with the :methodname:`stream` method. You can directly stream over
the response pages, convert the response stream to a stream of
:aws-java-class:`S3Object <services/s3/model/S3Object>` content, and then process
the content of the |S3| object.

**Imports**

.. literalinclude:: s3.java.s3_object_operations.import.txt
   :language: java

**Code**

.. literalinclude:: s3.java.s3_object_operations.iterative.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-s3:`complete example <S3ObjectOperations.java>` on GitHub.


.. _iterate-objects:

Iterate over Objects
====================

The following examples show ways to iterate over the objects returned in the response
instead of the pages of the response.

Use a Stream
------------

Use the :methodname:`stream` method on the response content
to iterate over the paginated item collection.

**Code**

.. literalinclude:: s3.java.s3_object_operations.stream.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-s3:`complete example <S3ObjectOperations.java>` on GitHub.


.. _for-loop:

Use a For Loop
--------------

Use a standard ``for`` loop to iterate through the contents of the response.

**Code**

.. literalinclude:: s3.java.s3_object_operations.forloop.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-s3:`complete example <S3ObjectOperations.java>` on GitHub.

Manual Pagination
=================

If your use case requires it, manual pagination is still available. Use the next token
in the response object for the subsequent requests. Here's an example using a ``while`` loop.

**Code**

.. literalinclude:: s3.java.s3_object_operations.pagination.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-s3:`complete example <S3ObjectOperations.java>` on GitHub.

=======================
Asynchronous Pagination
=======================

These examples use the asynchronous pagination methods for listing tables in
|DDB|. A manual pagination example is available in the :doc:`basics-async` topic.

.. _iterate-pages-async:

Iterate over Pages of Table Names
=================================

First, create an asynchronous |DDB| client. Then, call the
:methodname:`listTablesPaginator` method to get a
:aws-java-class:`ListTablesPublisher <services/dynamodb/paginators/ListTablesPublisher>`.
This is an implementation of the reactive streams Publisher interface. To learn more
about the reactive streams model, see
the `Reactive Streams Github repo <https://github.com/reactive-streams/reactive-streams-jvm/blob/v1.0.2/README.md>`_.

Call the :methodname:`subscribe` method on the :aws-java-class:`ListTablesPublisher <services/dynamodb/paginators/ListTablesPublisher>`
and pass a subscriber implementation. In this example, the subscriber has an :methodname:`onNext` method
that requests one item at a time from the publisher. This is the method that is called
repeatedly until all pages are retrieved. The :methodname:`onSubscribe` method
calls the :methodname:`Subscription.request` method to initiate
requests for data from the publisher. This method must be called to start getting data
from the publisher. The :methodname:`onError` method is triggered
if an error occurs while retrieving data. Finally,
the :methodname:`onComplete` method is called when all pages have been requested.


Use a Subscriber
----------------

**Imports**

.. literalinclude:: dynamodb.java.async_pagination.import.txt
   :language: java

**Code**

First create a asyc client

.. literalinclude:: dynamodb.java.async_pagination.pagesclient.txt
   :dedent: 8
   :language: java

Then use Subscriber to get results. 
 
.. literalinclude:: dynamodb.java.async_pagination.pagessubscribe.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-dynamodbasync:`complete example <AsyncPagination.java>` on GitHub.

Use a For Loop
--------------

Use a ``for`` loop to iterate through the pages for simple use cases when creating a new subscriber
might be too much overhead. The response publisher object has a :methodname:`forEach` helper method
for this purpose.

**Code**

.. literalinclude:: dynamodb.java.async_pagination.pagesforeach.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-dynamodbasync:`complete example <AsyncPagination.java>` on GitHub.


.. _iterate-objects-async:

Iterate over Table Names
========================

The following examples show ways to iterate over the objects returned in the response
instead of the pages of the response. Similar to the synchronous result,
the asynchronous result class has a method to interact with the underlying item
collection. The return type of the convenience method is a publisher that can be
used to request items across all pages.

Use a Subscriber
----------------

**Code**

First create a asyc client

.. literalinclude:: dynamodb.java.async_pagination.client.txt
   :dedent: 8
   :language: java

Then use Subscriber to get results. 
 
.. literalinclude:: dynamodb.java.async_pagination.subscriber.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-dynamodbasync:`complete example <AsyncPagination.java>` on GitHub.


.. _for-loop-async:

Use a For Loop
--------------

Use the :methodname:`forEach` convenience method to iterate through the results.

**Code**

.. literalinclude:: dynamodb.java.async_pagination.foreach.txt
  :dedent: 8
  :language: java

See the :sdk-examples-java-dynamodbasync:`complete example <AsyncPagination.java>` on GitHub.


Use Third-party Library
==========================

You can use other third party libraries instead of implementing a custom subscriber.
This example demonstrates using the RxJava implementation
but any library that implements the reactive stream interfaces can be used.
See the `RxJava wiki page on Github <https://github.com/ReactiveX/RxJava/wiki>`_
for more information on that library.

To use the library, add it as a dependency. If using Maven, the example shows the
POM snippet to use.

**POM Entry**

.. literalinclude:: example_code/dynamodb/pom.xml
   :lines: 46-50
   :language: xml

**Imports**

.. literalinclude:: dynamodb.java.async_pagination.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java.async_pagination.async.txt
   :dedent: 8
   :language: java


..  ====================
    Resume after Failure
    ====================

    Use the :methodname:`resume` method on the response object to resume pagination after an error.
    Multiple calls are made to retrieve paginated results. If a transient error occurs during
    those calls, you can use the :methodname:`resume` method to resume the iteration from
    the last successful call. This method is available in both the asynchronous and synchronous
    APIs.

    **Imports**

    .. literalinclude:: example_code/dynamodb/src/main/java/com/example/dynamodb/SyncPagination.java
       :language: java

    **Code**

    .. literalinclude:: example_code/dynamodb/src/main/java/com/example/dynamodb/SyncPagination.java
       :dedent: 8
       :language: java

    See the :sdk-examples-java-dynamodb:`complete example <SyncPagination.java>` on GitHub.
