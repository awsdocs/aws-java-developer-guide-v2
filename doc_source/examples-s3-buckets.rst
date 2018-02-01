.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

############################################
Creating, Listing, and Deleting |S3| Buckets
############################################

.. meta::
    :description: How to create, list, or delete a bucket using the AWS SDK for Java 2.0.
    :keywords: Amazon Simple Storage Service, Amazon S3, AWS SDK for Java 2.0, S3 code examples

Every object (file) in |S3| must reside within a *bucket*. A bucket represents a collection (container)
of objects. Each bucket must have a unique *key* (name). For detailed information
about buckets and their configuration, see :s3-dg:`Working with Amazon S3 Buckets <UsingBucket>` in
the |s3-dg|.

.. include:: common/s3-note-incomplete-upload-policy.txt

.. include:: includes/examples-note.txt

.. _create-bucket:

Create a Bucket
===============

Build a :aws-java-class:`CreateBucketRequest <services/s3/model/CreateBucketRequest>`
and provide a bucket name. Pass it to the |s3client|
:methodname:`createBucket` method. Use the |s3client| to do additional operations
such as listing or deleting buckets as shown in later examples.

**Imports**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketOps.java
   :lines: 17-20
   :language: java

**Code**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketOps.java
   :lines: 33-41
   :dedent: 8
   :language: java

See the :sdk-examples-java-s3:`complete example <S3BucketOps.java>` on GitHub.


.. _list-buckets:

List the Buckets
================

Build a :aws-java-class:`ListBucketRequest <services/s3/model/ListBucketRequest>`.
Use the |s3client| :methodname:`listBuckets` method to retrieve the list of buckets.
If the request succeeds a :aws-java-class:`ListBucketsResponse <services/s3/model/ListBucketsResponse>`
is returned. Use this response object to retrieve the list of buckets.

**Imports**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketOps.java
   :lines: 17-23
   :language: java

**Code**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketOps.java
   :lines: 44-46
   :dedent: 8
   :language: java

See the :sdk-examples-java-s3:`complete example <S3BucketOps.java>` on GitHub.


.. _delete-bucket:

Delete a Bucket
===============

Before you can delete an |S3| bucket, you must ensure that the bucket is empty or
the service will return an error. If you have a :S3-dg:`versioned bucket <Versioning>`,
you must also delete any versioned objects that are in the bucket.

.. contents::
   :local:

Delete Objects in a Bucket
--------------------------

Build a :aws-java-class:`ListObjectsV2Request <services/s3/model/ListObjectsV2Request>`
and use the |s3client| :methodname:`listObjects`
method to retrieve the list of objects in the bucket. Then use the :methodname:`deleteObject`
method on each object to delete it.

**Imports**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketDeletion.java
   :lines: 22-28
   :language: java

**Code**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketDeletion.java
   :lines: 54-66
   :dedent: 8
   :language: java

See the :sdk-examples-java-s3:`complete example <S3BucketDeletion.java>` on GitHub.

Delete an Empty Bucket
----------------------

Build a :aws-java-class:`DeleteBucketRequest <services/s3/model/DeleteBucketRequest>`
with a bucket name and pass it to the |s3client| :methodname:`deleteBucket` method.

**Imports**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketOps.java
   :lines: 17-21
   :language: java

**Code**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketOps.java
   :lines: 49-50
   :dedent: 8
   :language: java

See the :sdk-examples-java-s3:`complete example <S3BucketOps.java>` on GitHub.
