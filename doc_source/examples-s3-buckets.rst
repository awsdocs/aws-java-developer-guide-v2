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
    :description: How to create, list, or delete a bucket using the AWS SDK for Java 2.x.
    :keywords: Amazon Simple Storage Service, Amazon S3, AWS SDK for Java 2.x, S3 code examples,
               CreateBucketRequest, ListBucketRequest, ListObjectsV2Request


Every object (file) in |S3| must reside within a *bucket*. A bucket represents a collection (container)
of objects. Each bucket must have a unique *key* (name). For detailed information
about buckets and their configuration, see :s3-dg:`Working with Amazon S3 Buckets <UsingBucket>` in
the |s3-dg|.

.. include:: common/s3-note-incomplete-upload-policy.txt

.. include:: includes/examples-note.txt

.. contents::
    :local:
    :depth: 1

.. _create-bucket:

Create a Bucket
===============

Build a :aws-java-class:`CreateBucketRequest <services/s3/model/CreateBucketRequest>`
and provide a bucket name. Pass it to the |s3client|'s
:methodname:`createBucket` method. Use the |s3client| to do additional operations
such as listing or deleting buckets as shown in later examples.

**Imports**

.. literalinclude:: s3.java2.s3_bucket_ops.import.txt
   :language: java

**Code**

First create an |s3client|.

.. literalinclude:: s3.java2.s3_bucket_ops.region.txt
   :language: java

Make a Create Bucket Request. 

.. literalinclude:: s3.java2.s3_bucket_ops.create_bucket.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-s3:`complete example <S3BucketOps.java>` on GitHub.


.. _list-buckets:

List the Buckets
================

Build a :aws-java-class:`ListBucketsRequest <services/s3/model/ListBucketsRequest>`.
Use the |s3client|'s :methodname:`listBuckets` method to retrieve the list of buckets.
If the request succeeds a :aws-java-class:`ListBucketsResponse <services/s3/model/ListBucketsResponse>`
is returned. Use this response object to retrieve the list of buckets.

**Imports**

.. literalinclude:: s3.java2.s3_bucket_ops.import.txt
   :language: java

**Code**

First create an |s3client|.

.. literalinclude:: s3.java2.s3_bucket_ops.region.txt
   :language: java

Make a List Buckets Request. 

.. literalinclude:: s3.java2.s3_bucket_ops.list_bucket.txt
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
and use the |s3client|'s :methodname:`listObjects`
method to retrieve the list of objects in the bucket. Then use the :methodname:`deleteObject`
method on each object to delete it.

**Imports**

.. literalinclude:: s3.java2.s3_bucket_ops.import.txt
   :language: java

**Code**

First create an |s3client|.

.. literalinclude:: s3.java2.s3_bucket_ops.region.txt
   :language: java


.. literalinclude:: s3.java2.s3_bucket_ops.delete_bucket.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-s3:`complete example <S3BucketDeletion.java>` on GitHub.

Delete an Empty Bucket
----------------------

Build a :aws-java-class:`DeleteBucketRequest <services/s3/model/DeleteBucketRequest>`
with a bucket name and pass it to the |s3client|'s :methodname:`deleteBucket` method.

**Imports**

.. literalinclude:: s3.java2.s3_bucket_ops.import.txt
   :language: java

**Code**

First create an |s3client|.

.. literalinclude:: s3.java2.s3_bucket_ops.delete_bucket.txt
   :language: java


Delete all objects in the bucket. 

.. literalinclude:: s3.java2.s3_bucket_ops.delete_bucket.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-s3:`complete example <S3BucketOps.java>` on GitHub.
