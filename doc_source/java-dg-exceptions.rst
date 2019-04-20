.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

##################
Exception Handling
##################

.. meta::
   :description: How to handle exceptions thrown by the AWS SDK for Java.
   :keywords:

Understanding how and when the |sdk-java| throws exceptions is important to building
high-quality applications using the SDK. The following sections describe the different cases of
exceptions that are thrown by the SDK and how to handle them appropriately.

Why Unchecked Exceptions?
=========================

The |sdk-java| uses runtime (or unchecked) exceptions instead of checked exceptions for these
reasons:

* To allow developers fine-grained control over the errors they want to handle without forcing them
  to handle exceptional cases they aren't concerned about (and making their code overly verbose)

* To prevent scalability issues inherent with checked exceptions in large applications

In general, checked exceptions work well on small scales, but can become troublesome as applications
grow and become more complex.


SdkServiceException (and Subclasses)
======================================

:aws-java-class:`SdkServiceException <core/exception/SdkServiceException>` is the most common
exception that you'll experience when using
the |sdk-java|. This exception represents an error response from an AWS service. For example, if you
try to terminate an |EC2| instance that doesn't exist, EC2 will return an error response and all the
details of that error response will be included in the :classname:`SdkServiceException` that's thrown.
For some cases, a subclass of :classname:`SdkServiceException` is thrown to allow developers
fine-grained control over handling error cases through catch blocks.

When you encounter an :classname:`SdkServiceException`, you know that your request was successfully
sent to the AWS service but couldn't be successfully processed. This can be because of errors in
the request's parameters or because of issues on the service side.

:classname:`SdkServiceException` provides you with information such as:

* Returned HTTP status code

* Returned AWS error code

* Detailed error message from the service

* AWS request ID for the failed request


SdkClientException
===================

:aws-java-class:`SdkClientException <core/exception/SdkClientException>` indicates that a
problem occurred inside the Java client code,
either while trying to send a request to AWS or while trying to parse a response from AWS.
An :classname:`SdkClientException` is generally more severe than an
:classname:`SdkServiceException`, and indicates a major problem that is preventing the
client from making service calls to AWS services. For example, the |sdk-java|
throws an :classname:`SdkClientException` if no network connection is available when you try to
call an operation on one of the clients.
