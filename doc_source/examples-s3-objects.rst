.. Copyright 2010-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#####################################
Performing Operations on |S3| Objects
#####################################

.. meta::
    :description: How to list, upload, download or delete objects in an Amazon
                  S3 bucket using the AWS SDK for Java 2.0.
    :keywords: AWS SDK for Java 2.0 code example


An |S3| object represents a *file* or collection of data. Every object must reside within a
:doc:`bucket <examples-s3-buckets>`.

.. include:: includes/examples-note.txt

.. contents::
    :local:
    :depth: 1


.. _upload-object:

Upload an Object
================

Build a :classname:`PutObjectRequest`, supplying bucket name and key name. Then use the |s3client|'s
:methodname:`putObject` method with a :classname:`RequestBody` that contains the object content
and the PutObjectRequest object. *The bucket must exist, or an error will result*.

**Imports**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java
   :lines: 33-35

**Code**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java
   :lines: 51-54
   :dedent: 8

See the :sdk-examples-java-s3:`complete example <S3ObjectOperations.java>`.

.. _list-objects:

Upload Objects in Multiparts
============================

To upload an object in multiple parts, use the |s3client|'s :methodname:`createMultipartUpload`
method to get an upload ID. Then use the |s3client|'s :methodname:`uploadPart` method for each part.
Finally use the |s3client|'s :methodname:`completeMultipartUpload` method to tell S3 to
merge all the uploaded parts and finish the upload operation.

**Imports**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java
   :lines: 23-34

**Code**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java
   :lines: 102-130
   :dedent: 8

See the :sdk-examples-java-s3:`complete example <S3ObjectOperations.java>`.

.. _download-object:

Download an Object
==================

Build a :classname:`GetObjectRequest`, supplying a bucket name and key name.
Use the |s3client|'s :methodname:`getObject` method, passing it the
:classname:`GetObjectRequest` object and a :classname:`StreamingResponseHandler` object.
The :classname:`StreamingResponseHandler` creates a response handler that will write
the response content to the specified file or stream.

The following example specifies a file name to write the object content to.

**Imports**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java
   :lines: 17-23

**Code**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java
   :lines: 62-64
   :dedent: 8

See the :sdk-examples-java-s3:`complete example <S3ObjectOperations.java>`.

.. _delete-object:

Delete an Object
================

Build a :classname:`DeleteObjectRequest`, supplying a bucket name and key name.
Use the |s3client| client's :methodname:`deleteObject` method, passing it the name of a bucket and
object to delete. *The specified bucket and object key must exist, or an error will result*.

**Imports**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java
   :lines: 31-32

**Code**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3ObjectOperations.java
   :lines: 67-69
   :dedent: 8

See the :sdk-examples-java-s3:`complete example <S3ObjectOperations.java>`.
