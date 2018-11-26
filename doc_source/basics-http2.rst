.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

##################
HTTP/2 Programming
##################

.. meta::
   :description: Basic information about the HTTP/2 protocol and how it's implemented
                 in the AWS SDK for Java 2.x.

HTTP/2 is a major revision of the HTTP protocol. This new version has several
enhancements to improve performance:

* Binary data encoding provides more efficient data transfer.
* Header compression reduces the overhead bytes downloaded by the client,
  helping get the content to the client sooner. This is especially useful
  for mobile clients that are already constrained on bandwidth.
* Bidirectional asynchronous communication (multiplexing) allows multiple
  requests and response messages between the client and AWS to be in flight
  at the same time over a single connection, instead of over multiple connections,
  which improves performance.

Developers upgrading to the latest SDKs will automatically use HTTP/2 when
it's supported by the service they're working with. New programming interfaces
seamlessly take advantage of HTTP/2 features and provide new ways to build
applications.

The |sdk-java-v2| features new APIs for event streaming that implement
the HTTP/2 protocol. For examples of how to use these new APIs,
see :doc:`examples-kinesis`.
