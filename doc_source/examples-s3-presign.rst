.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

############################################
Working with Amazon S3 Presigned URLs
############################################

.. meta::
    :description: How to work with Presigned buckets using the AWS SDK for Java 2.x.
    :keywords: Amazon Simple Storage Service, Amazon S3, AWS SDK for Java 2.x, S3 code examples,
               CreateBucketRequest, ListBucketRequest, ListObjectsV2Request

You can use a :aws-java-class:`S3Presigner <services/s3/presigner/S3Presigner>` object to sign an S3 *SdkRequest* so that it can
be executed without requiring any additional authentication on the part of the caller.
For example, assume Alice has access to an S3 object, and she wants to temporarily share access to that object with Bob.
Alice can generate a pre-signed :aws-java-class:`GetObjectRequest <services/s3/model/GetObjectRequest>` object
to secure share with Bob so that he can download the object without requiring
access to Alice's credentials.

.. contents::
    :local:
    :depth: 1

.. _generate-presignedurl:

Generate a presigned URL and upload an object
=============================================

Build a :aws-java-class:`S3Presigner <services/s3/presigner/S3Presigner>` object that represents the client object.
Next create a :aws-java-class:`PresignedPutObjectRequest <services/s3/presigner/model/PresignedPutObjectRequest>` object
that can be executed at a later time without requiring additional signing or authentication. When you create this object,
you can specify the bucket name and the key name. In addition,
you can also specify the time in minutes that the bucket can be accessed without using AWS credentials by invoking the
*signatureDuration* method (as shown in the following code example).

You can use the :aws-java-class:`PresignedPutObjectRequest <services/s3/presigner/model/PresignedPutObjectRequest>` object to
obtain the URL by invoking its *url* method.

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


.. _get-presignedobject:

Get a presigned object
======================

Build a :aws-java-class:`S3Presigner <services/s3/presigner/S3Presigner>` object that represents the client object.
Next create a :aws-java-class:`GetObjectRequest <services/s3/model/GetObjectRequest>` object
and specify the bucket name and key name. In addition, create a
:aws-java-class:`GetObjectPresignRequest <services/s3/presigner/model/GetObjectPresignRequest>` object that
can be executed at a later time without requiring additional signing or authentication.

Invoke the aws-java-class:`S3Presigner <services/s3/presigner/S3Presigner>` object's *presignGetObject* method to create a
:aws-java-class:`PresignedPutObjectRequest <services/s3/presigner/model/PresignedPutObjectRequest>` object. You can invoke this object's *url* method to obtain the URL to use. Once you have the URL, you can use standard HTTP Java logic to read the contents of the bucket, as shown
in the following Java code example.

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


