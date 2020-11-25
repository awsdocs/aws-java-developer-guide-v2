.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

##################
Working with |COG|
##################

.. meta::
   :description: How to use the AWS SDK for Java to work with Amazon Cognito.
   :keywords: AWS for Java SDK code examples, cognito, user pool, app, client, identity, IdP,
              provider, credentials


With |COG|, you can quickly add user sign-up or sign-in capability to your web or mobile app. The
examples here demonstrate some of the basic functionality of Cognito.


.. _cognito-create-userpool:

Create a user pool
==================

A user pool is a directory of users that you can configure for your web or mobile app.

To create a user pool, start by building a
:aws-java-class:`CreateUserPoolRequest <services/cognitoidentityprovider/model/CreateUserPoolRequest>`
object, with the name of the user pool as the value of its :methodname:`poolName()`. Call the
:methodname:`createUserPool()` method of your
:aws-java-class:`CreateUserPoolRequest <services/cognitoidentityprovider/CognitoIdentityProviderClient>`,
passing in the :classname:`CreateUserPoolRequest` object. You can capture the result of this
request as a
:aws-java-class:`CreateUserPoolResponse <services/cognitoidentityprovider/model/CreateUserPoolResponse>`
object, as demonstrated in the following code snippet.

**Imports**

.. literalinclude:: cognito.java2.create_user_pool.import.txt
   :language: java

**Code**

.. literalinclude:: cognito.java2.create_user_pool.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-cognito:`complete example <CreateUserPool.java>` on GitHub.


.. _cognito-list-userpool:

List users from a user pool
===========================

To list users from your user pools, start by building a
:aws-java-class:`ListUserPoolsRequest <services/cognitoidentityprovider/model/ListUserPoolsRequest>`
object, with the number of maximum results as the value of its :methodname:`maxResults()`.
Call the :methodname:`listUserPools()` method of your
:classname:`CognitoIdentityProviderClient`, passing in the :classname:`ListUserPoolsRequest`
object. You can capture the result of this request as a
:aws-java-class:`ListUserPoolsResponse <services/cognitoidentityprovider/model/ListUserPoolsResponse>`
object, as demonstrated in the following code snippet. Create a
:aws-java-class:`UserPoolDescriptionType <services/cognitoidentityprovider/model/UserPoolDescriptionType>`
object to easily iterate over the results and pull out the attributes of each user.

**Imports**

.. literalinclude:: cognito.java2.ListUserPools.import.txt
   :language: java

**Code**

.. literalinclude:: cognito.java2.ListUserPools.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-cognito:`complete example <ListUserPools.java>` on GitHub.


.. _cognito-create-identitypool:

Create an identity pool
=======================

An identity pool is a container that organizes the IDs from your external identity provider,
keeping a unique identifier for each user. To create an identity pool, start by building a
:aws-java-class:`CreateIdentityPoolRequest <services/cognitoidentity/model/CreateIdentityPoolRequest>`
with the name of the user pool as the value of its :methodname:`identityPoolName()`. Set
:methodname:`allowUnauthenticatedIdentities()` to :code:`true` or :code:`false`. Call the
:methodname:`createIdentityPool()` method of your :classname:`CognitoIdentityClient` object,
passing in the :classname:`CreateIdentityPoolRequest` object. You can capture the result of this
request as a
:aws-java-class:`CreateIdentityPoolResponse <services/cognitoidentity/model/CreateIdentityPoolResponse>`
object, as demonstrated in the following code snippet.

**Imports**

.. literalinclude:: cognito.java2.create_identity_pool.import.txt
   :language: java

**Code**

.. literalinclude:: cognito.java2.create_identity_pool.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-cognito:`complete example <CreateIdentityPool.java>` on GitHub.


.. _cognito-add-appclient:

Add an app client
=================

To enable the hosted web sign-up or sign-in UI for your app, create an app client. To create an app
client, start by building a
:aws-java-class:`CreateUserPoolClientRequest <services/cognitoidentityprovider/model/CreateUserPoolClientRequest>`
object, with the name of the client as the value of its :methodname:`clientName()`. Set
:methodname:`userPoolId()` to the ID of the user pool to which you want to attach this
app client. Call the :methodname:`createUserPoolClient()` method of your
:classname:`CognitoIdentityProviderClient`, passing in the :classname:`CreateUserPoolClientRequest`
object. You can capture the result of this request as a
:aws-java-class:`CreateUserPoolClientResponse <services/cognitoidentityprovider/model/CreateUserPoolClientResponse>`
object, as demonstrated in the following code snippet.

**Imports**

.. literalinclude:: cognito.java2.user_pool.create_user_pool_client.import.txt
   :language: java

**Code**

.. literalinclude:: cognito.java2.user_pool.create_user_pool_client.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-cognito:`complete example <CreateUserPoolClient.java>` on GitHub.


.. _cognito-thirdparty-idp:

Add a third-party identity provider
===================================

Adding an external identity provider (IdP) enables your users to log into your app using that
service’s login mechanism. To add a third-party IdP, start by building an
:aws-java-class:`UpdateIdentityPoolRequest <services/cognitoidentity/model/UpdateIdentityPoolRequest>`
object, with the name of the identity pool as the value of its :methodname:`identityPoolName()`.
Set :methodname:`allowUnauthenticatedIdentities()` to :code:`true` or :code:`false`, specify the
:methodname:`identityPoolId()`, and define which login providers will be supported with
:methodname:`supportedLoginProviders()`. Call the :methodname:`updateIdentityPool()`
method of your :classname:`CognitoIdentityClient`, passing in the
:classname:`UpdateIdentityPoolRequest` object. You can capture the result of this request as an
:aws-java-class:`UpdateIdentityPoolResponse <services/cognitoidentity/model/UpdateIdentityPoolResponse>`
object, as demonstrated in the following code snippet.

**Imports**

.. literalinclude:: cognito.java2.add_login_provider.import.txt
   :language: java

**Code**

.. literalinclude:: cognito.java2.add_login_provider.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-cognito:`complete example <AddLoginProvider.java>` on GitHub.


.. _cognito-get-credentials:

Get credentials for an ID
=========================

To get the credentials for an identity in an identity pool, first build a
:aws-java-class:`GetCredentialsForIdentityRequest <services/cognitoidentity/model/GetCredentialsForIdentityRequest>`
with the identity ID as the value of its :methodname:`identityId()`. Call the
:methodname:`getCredentialsForIdentity()` method of your :classname:`CognitoIdentityClient`,
passing in the :classname:`GetCredentialsForIdentityRequest`. You can capture the result of this
request as a
:aws-java-class:`GetCredentialsForIdentityResponse <services/cognitoidentity/model/GetCredentialsForIdentityResponse>`
object, as demonstrated in the following code snippet.

**Imports**

.. literalinclude:: cognito.java2.GetIdentityCredentials.import.txt
   :language: java

**Code**

.. literalinclude:: cognito.java2.GetIdentityCredentials.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-cognito:`complete example <GetIdentityCredentials.java>` on GitHub.


For more information, see the |COG-dg|_.
