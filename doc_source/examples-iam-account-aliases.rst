.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#########################
Use |IAM| account aliases
#########################

.. meta::
   :description: How to create, list and delete AWS account aliases.
   :keywords: AWS SDK for Java 2.x code examples, IAM account aliases,
              CreateAccountAliasRequest, ListAccountAliasesResponse, DeleteAccountAliasRequest


If you want the URL for your sign-in page to contain your company name or other friendly identifier
instead of your AWS account ID, you can create an alias for your AWS account.

.. note:: AWS supports exactly one account alias per account.


Create an account alias
=======================

To create an account alias, call the |iamclient|'s :methodname:`createAccountAlias` method with a
:aws-java-class:`CreateAccountAliasRequest
<services/iam/model/CreateAccountAliasRequest>` object that contains the alias name.

**Imports**

.. literalinclude:: iam.java2.create_account_alias.import.txt
   :language: java

**Code**

.. literalinclude:: iam.java2.create_account_alias.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-iam:`complete example <CreateAccountAlias.java>` on GitHub.


List account aliases
====================

To list your account's alias, if any, call the |iamclient|'s :methodname:`listAccountAliases`
method.

.. note:: The returned
   :aws-java-class:`ListAccountAliasesResponse
   <services/iam/model/ListAccountAliasesResponse>`
   supports the same
   :methodname:`isTruncated` and :methodname:`marker` methods as other |sdk-java| *list*
   methods, but an AWS account can have only *one* account alias.

**Imports**

.. literalinclude:: iam.java2.list_account_aliases.import.txt
   :language: java

**Code**

.. literalinclude:: iam.java2.list_account_aliases.main.txt
   :dedent: 4
   :language: java

see the :sdk-examples-java-iam:`complete example <ListAccountAliases.java>` on GitHub.


Delete an account alias
=======================

To delete your account's alias, call the |iamclient|'s :methodname:`deleteAccountAlias` method. When
deleting an account alias, you must supply its name using a
:aws-java-class:`DeleteAccountAliasRequest
<services/iam/model/DeleteAccountAliasRequest>` object.

**Imports**

.. literalinclude:: iam.java2.delete_account_alias.import.txt
   :language: java

**Code**

.. literalinclude:: iam.java2.delete_account_alias.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-iam:`complete example <DeleteAccountAlias.java>` on GitHub.

More information
================

* :iam-ug:`Your AWS Account ID and Its Alias <console_account-alias>` in the |iam-ug|
* :iam-api:`CreateAccountAlias` in the |iam-api|
* :iam-api:`ListAccountAliases` in the |iam-api|
* :iam-api:`DeleteAccountAlias` in the |iam-api|
