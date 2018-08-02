.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

##########################
Managing |IAM| Access Keys
##########################

.. meta::
   :description: How to manage IAM access keys with the AWS SDK for Java 2.0.
   :keywords: AWS SDK for Java 2.0, code examples, IAM access keys, creating, listing, updating,
              deleting, getting last access time, CreateAccessKeyRequest, ListAccessKeysRequest,
              GetAccessKeyLastUsedRequest

.. include:: includes/dev-preview-note.txt

Creating an Access Key
======================

To create an |IAM| access key, call the |iamclient|'s :methodname:`createAccessKey` method with a
:aws-java-class:`CreateAccessKeyRequest <services/iam/model/CreateAccessKeyRequest>`
object.

.. note:: You must set the region to :guilabel:`AWS_GLOBAL` for |iamclient| calls to work because
   |IAM| is a global service.

**Imports**

.. literalinclude:: example_code/iam/src/main/java/com/example/iam/CreateAccessKey.java
   :lines: 16-20
   :language: java

**Code**

.. literalinclude:: example_code/iam/src/main/java/com/example/iam/CreateAccessKey.java
   :lines: 39-46
   :dedent: 8
   :language: java

See the :sdk-examples-java-iam:`complete example <CreateAccessKey.java>` on GitHub.


Listing Access Keys
===================

To list the access keys for a given user, create a :aws-java-class:`ListAccessKeysRequest
<services/iam/model/ListAccessKeysRequest>` object that contains the user name to
list keys for, and pass it to the |iamclient|'s :methodname:`listAccessKeys` method.

.. note:: If you do not supply a user name to :methodname:`listAccessKeys`, it will attempt to list
   access keys associated with the AWS account that signed the request.

**Imports**

.. literalinclude:: example_code/iam/src/main/java/com/example/iam/ListAccessKeys.java
   :lines: 16-21
   :language: java

**Code**

.. literalinclude:: example_code/iam/src/main/java/com/example/iam/ListAccessKeys.java
   :lines: 40-73
   :dedent: 8
   :language: java

The results of :methodname:`listAccessKeys` are paged (with a default maximum of 100 records per
call). You can call :methodname:`isTruncated` on the returned
:aws-java-class:`ListAccessKeysResponse <services/iam/model/ListAccessKeysResponse>`
object to see if the query returned fewer results then are available. If so, then call
:methodname:`marker` on the :classname:`ListAccessKeysResponse` and use it when creating
a new request. Use that new request in the next
invocation of :methodname:`listAccessKeys`.

See the :sdk-examples-java-iam:`complete example <ListAccessKeys.java>` on GitHub.


Retrieving an Access Key's Last Used Time
=========================================

To get the time an access key was last used, call the |iamclient|'s
:methodname:`getAccessKeyLastUsed` method with the access key's ID (which can be passed in using a
:aws-java-class:`GetAccessKeyLastUsedRequest
<services/iam/model/GetAccessKeyLastUsedRequest>` object.

You can then use the returned :aws-java-class:`GetAccessKeyLastUsedResponse
<services/iam/model/GetAccessKeyLastUsedResponse>` object to retrieve the key's last
used time.

**Imports**

.. literalinclude:: example_code/iam/src/main/java/com/example/iam/AccessKeyLastUsed.java
   :lines: 17-20
   :language: java

**Code**

.. literalinclude:: example_code/iam/src/main/java/com/example/iam/AccessKeyLastUsed.java
   :lines: 38-48
   :dedent: 8
   :language: java

See the :sdk-examples-java-iam:`complete example <AccessKeyLastUsed.java>` on GitHub.


.. _iam-access-keys-update:

Activating or Deactivating Access Keys
======================================

You can activate or deactivate an access key by creating an :aws-java-class:`UpdateAccessKeyRequest
<services/iam/model/UpdateAccessKeyRequest>` object, providing the access key ID,
optionally the user name, and the desired :aws-java-class:`status <services/iam/model/StatusType>`,
then passing the request object to the
|iamclient|'s :methodname:`updateAccessKey` method.

**Imports**

.. literalinclude:: example_code/iam/src/main/java/com/example/iam/UpdateAccessKey.java
   :lines: 16-20
   :language: java

**Code**

.. literalinclude:: example_code/iam/src/main/java/com/example/iam/UpdateAccessKey.java
   :lines: 39-64
   :dedent: 8
   :language: java

See the :sdk-examples-java-iam:`complete example <UpdateAccessKey.java>` on GitHub.


Deleting an Access Key
======================

To permanently delete an access key, call the |iamclient|'s :methodname:`deleteKey` method,
providing it with a :aws-java-class:`DeleteAccessKeyRequest
<services/iam/model/DeleteAccessKeyRequest>` containing the access key's ID and
username.

.. note:: Once deleted, a key can no longer be retrieved or used. To temporarily deactivate a key so
   that it can be activated again later, use :ref:`updateAccessKey <iam-access-keys-update>` method
   instead.

**Imports**

.. literalinclude:: example_code/iam/src/main/java/com/example/iam/DeleteAccessKey.java
   :lines: 16-19
   :language: java

**Code**

.. literalinclude:: example_code/iam/src/main/java/com/example/iam/DeleteAccessKey.java
   :lines: 39-46
   :dedent: 8
   :language: java

See the :sdk-examples-java-iam:`complete example <DeleteAccessKey.java>` on GitHub.


More Information
================

* :iam-api:`CreateAccessKey` in the |iam-api|
* :iam-api:`ListAccessKeys` in the |iam-api|
* :iam-api:`GetAccessKeyLastUsed` in the |iam-api|
* :iam-api:`UpdateAccessKey` in the |iam-api|
* :iam-api:`DeleteAccessKey` in the |iam-api|
