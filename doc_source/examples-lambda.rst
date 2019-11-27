.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#################################################
Invoking, Listing, and Deleting Lambda Functions
#################################################

.. meta::
    :description: How to invoke, list, and delete a Lambda function by using the AWS SDK for Java 2.x.
    :keywords: Amazon Lambda, AWS SDK for Java 2.x, Lambda code examples,
               deleteFunction, invoke, listFunctions


This section provides examples of programming with the Lambda service client by using the AWS SDK for Java 2.0.

Invoke a Lambda function
========================

You can invoke a Lambda function by creating a :aws-java-class:`LambdaClient <services/lambda/LambdaClient>`
object and invoking its :methodname:`invoke` method. Create an :aws-java-class:`InvokeRequest <services/lambda/model/InvokeRequest>`
object to specify additional information such as the function name and the payload to pass to the Lambda function.
To pass payload data to a function, create a :aws-java-class:`SdkBytes <core/SdkBytes>`
object that contains information. For example, in the following code example, notice the JSON data passed to the Lambda function.

**Imports**

.. literalinclude:: s3.java2.s3_bucket_ops.import.txt
   :language: java

**Code**

The following code example demonstrates how to invoke a Lambda function.

.. literalinclude:: s3.java2.s3_bucket_ops.region.txt
   :language: java

See the :sdk-examples-java-s3:`complete example <S3BucketOps.java>` on GitHub.


.. _invoke-lambda:

List the Lambda functions
=========================

Build a :aws-java-class:`LambdaClient <services/lambda/LambdaClient>`
object and invoke its :methodname:`listFunctions` method.
This method returns a :aws-java-class:`ListFunctionsResponse <services/lambda/model/ListFunctionsResponse>` object.
You can invoke this object's :methodname:`functions` method to return a list of :aws-java-class:`FunctionConfiguration <services/lambda/model/FunctionConfiguration>` objects.
You can iterate through the list to retrieve information about the functions. For example, the following code example shows you how you can get the function names.


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
