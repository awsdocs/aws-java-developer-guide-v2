.. Copyright 2010-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

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
    :keywords: Amazon S3, AWS SDK for Java 2.0, create bucket, list bucket, delete
               bucket, delete versioned bucket

Every object (file) in |S3| must reside within a *bucket*, which represents a collection (container)
of objects. Each bucket is known by a *key* (name), which must be unique. For detailed information
about buckets and their configuration, see :s3-dg:`Working with Amazon S3 Buckets <UsingBucket>` in
the |s3-dg|.

.. include:: common/s3-note-incomplete-upload-policy.txt

.. include:: includes/examples-note.txt

.. _create-bucket:

Create a Bucket
===============

Build a :classname:`CreateBucketRequest` with a bucket name and
pass it to the |s3client|'s
:methodname:`createBucket` method. Use the |s3client| to do additional operations.

**Imports**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketOps.java
   :lines: 17-20

**Code**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketOps.java
   :lines: 33-41
   :dedent: 8

See the :sdk-examples-java-s3:`complete example <S3BucketOps.java>`.


.. _list-buckets:

List Buckets
============

Build a :classname:`CreateBucketRequest`. Use the |s3client|'s :methodname:`listBuckets`
method to retrieve the list of buckets. If successful,
a :aws-java-class:`ListBucketsResponse <services/s3/model/ListBucketsResponse>` is returned.
Use this to retrieve the list of buckets.

**Imports**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketOps.java
   :lines: 17-23

**Code**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketOps.java
   :lines: 44-46
   :dedent: 8

See the :sdk-examples-java-s3:`complete example <S3BucketOps.java>`.


.. _delete-bucket:

Delete a Bucket
===============

Before you can delete an |S3| bucket, you must ensure that the bucket is empty or an error
will result. If you have a :S3-dg:`versioned bucket <Versioning>`, you must also delete any
versioned objects associated with the bucket.

.. contents::
   :local:

Delete Objects in Bucket
------------------------
Build a :classname:`ListObjectsV2Request` and use the |s3client|'s :methodname:`listObjects`
method to retrieve the list of objects in a bucket. Then use the :methodname:`deleteObject`
method on each object.

**Imports**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketDeletion.java
   :lines: 22-28

**Code**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketDeletion.java
   :lines: 54-66
   :dedent: 8

See the :sdk-examples-java-s3:`complete example <S3BucketDeletion.java>`.

Delete an Empty Bucket
----------------------

To delete an empty bucket, build a :methodname:`CreateBucketRequest` with a bucket name
and pass it to the |s3client|'s :methodname:`deleteBucket` method.

**Imports**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketOps.java
   :lines: 17-21

**Code**

.. literalinclude:: example_code/s3/src/main/java/com/example/s3/S3BucketOps.java
   :lines: 49-50
   :dedent: 8

See the :sdk-examples-java-s3:`complete example <S3BucketOps.java>`.
