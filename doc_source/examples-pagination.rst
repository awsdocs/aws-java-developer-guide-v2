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
    :description: How to use stream to get automatic pagination in AWS SDK for Java 2.0.
    :keywords: Pagination, AWS SDK for Java 2.0, S3 code examples

Many AWS operations return paginated results when the response object is too large
to return in a single response. In the |sdk-java|_ 1.0, the response contained a token
you had to use to retrieve the next page of results. With the |sdk-java|_ 2.0,
multiple service calls to get the next page of results are made for you automatically.
You only have to code what to do with the results.

The examples on this page use |S3| object operations to demonstrate the
various methods of retrieving your data from paginated responses.

.. include:: includes/examples-note.txt

.. _iterate-pages:

Iterate over Pages
==================

Build a :aws-java-class:`ListObjectsV2Request <services/s3/model/ListObjectsV2Request>`
and provide a bucket name. Optionally you can provide the maximum number of keys to
retrieve at one time.
Pass it to the |s3client|'s :methodname:`listObjectsV2Iterable` method. This method
returns a :aws-java-class:`ListObjectsV2Paginator <services/s3/paginators/ListObjectsV2Paginator>`
object, which is an iterable of the
:aws-java-class:`ListObjectsV2Response <services/s3/model/ListObjectsV2Response>` class.

The first example demonstrates using the paginator object to iterate through all the
response pages with the :methodname:`stream` method. You can directly stream over
the response pages, convert the response stream to a stream of
:aws-java-class:`S3Object <services/s3/model/S3Object>` content, and then process
the content of the |S3| object.

**Imports**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java
   :lines: 21-22,33,37
   :language: java

**Code**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java
   :lines: 64-74
   :dedent: 8
   :language: java

See the :sdk-examples-java-s3:`complete example <S3ObjectOperations.java>` on GitHub.


.. _iterate-objects:

Iterate over Objects
====================

The following examples show ways to iterate over the objects returned in the response
instead of in the pages of the response.

Use a Stream
------------

Use the :methodname:`stream` method on the response content
to iterate over the paginated item collection.

**Code**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java
   :lines: 76-78
   :dedent: 8
   :language: java

See the :sdk-examples-java-s3:`complete example <S3ObjectOperations.java>` on GitHub.


.. _for-loop:

Use a For Loop
--------------

Use a standard for loop to iterate through the contents of the response.

**Code**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java
   :lines: 80-83
   :dedent: 8
   :language: java

See the :sdk-examples-java-s3:`complete example <S3ObjectOperations.java>` on GitHub.
