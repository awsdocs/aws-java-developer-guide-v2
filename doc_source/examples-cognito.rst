.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

##############
|COG| examples
##############

.. meta::
   :description: How to use the AWS SDK for Java to work with |COG|.
   :keywords: AWS for Java SDK code examples, cognito, user, pool, app, client, identity, IdP,
              provider, credentials


With |COG|, you can quickly add user sign up/sign in to your web or mobile app. The
examples here demonstrate some of the basic functionality of Cognito.


.. _cognito-create-userpool:

Create a user pool
==================

A user pool is a directory of users that you can configure for your web or mobile app. To create a
user pool, start by building a CreateUserPoolRequest with the name of the user pool as the value of
its poolName(). Call the createUserPool() method of your CognitoIdentityProviderClient, passing in
the CreateUserPoolRequest. You can capture the result of this request as a CreateUserPoolResponse
object, as demonstrated in the code snippet below.

**Imports**

.. literalinclude:: dynamodb.java2.mapping.putitem.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.mapping.putitem.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-cognito:`complete example <CreateUserPool.java>` on GitHub.


.. _cognito-list-userpool:

List users from a user pool
===========================

To list users from your user pools, start by building a ListUserPoolsRequest with the number of
maximum results as the value of its maxResults(). Call the listUserPools() method of your
CognitoIdentityProviderClient, passing in the ListUserPoolsRequest. You can capture the result of
this request as a ListUserPoolsResponse object, as demonstrated in the code snippet below. Create a
UserPoolDescriptionType object to easily iterate over the results and pull out the attributes of
each user.

**Imports**

.. literalinclude:: dynamodb.java2.mapping.putitem.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.mapping.putitem.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-cognito:`complete example <ListUserPools.java>` on GitHub.


.. _cognito-create-identitypool:

Create an identity pool
=======================

An identity pool is a container that organizes the IDs from your external identity provider, keeping
a unique identifier for each user. To create an identity pool, start by building a
CreateIdentityPoolRequest with the name of the user pool as the value of its identityPoolName() and
set allowUnauthenticatedIdentities() to true or false. Call the createIdentityPool() method of your
CognitoIdentityClient, passing in the CreateIdentityPoolRequest. You can capture the result of this
request as a CreateIdentityPoolResponse object, as demonstrated in the code snippet below.

**Imports**

.. literalinclude:: dynamodb.java2.mapping.putitem.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.mapping.putitem.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-cognito:`complete example <CreateIdentityPool.java>` on GitHub.


.. _cognito-add-appclient:

Add an app client
=================

To enable the hosted web sign up/sign in UI for your app, create an app client. To create an app
client, start by building a CreateUserPoolClientRequest with the name of the client as the value of
its clientName() and set userPoolId() to the ID of the user pool to which you want to attach this
app client. Call the createUserPoolClient() method of your CognitoIdentityProviderClient, passing in
the CreateUserPoolClientRequest. You can capture the result of this request as a
CreateUserPoolClientResponse object, as demonstrated in the code snippet below.

**Imports**

.. literalinclude:: dynamodb.java2.mapping.putitem.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.mapping.putitem.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-cognito:`complete example <CreateUserPoolClient.java>` on GitHub.


.. _cognito-thirdparty-idp:

Add a third-party identity provider
===================================

Adding an external identity provider (IdP) enables your users to log into your app using that
service’s login mechanism. To add a third-party IdP, start by building a UpdateIdentityPoolRequest
with the name of the identity pool as the value of its identityPoolName(), set
allowUnauthenticatedIdentities() to true or false, specify the identiyPooolId(), and define which
login providers will be supported with supportedLoginProviders(). Call the updateIdentityPool()
method of your CognitoIdentityClient, passing in the UpdateIdentityPoolRequest. You can capture the
result of this request as a UpdateIdentityPoolResponse object, as demonstrated in the code snippet
below.

**Imports**

.. literalinclude:: dynamodb.java2.mapping.putitem.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.mapping.putitem.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-cognito:`complete example <AddLoginProvider.java>` on GitHub.


.. _cognito-get-credentials:

Get credentials for an ID
=========================

To get the credentials for an identity in an identity pool, first build a
GetCredentialsForIdentityRequest with the identity pool ID as the value of its identityId(). Call
the getCredentialsForIdentity() method of your CognitoIdentityClient, passing in the
GetCredentialsForIdentityRequest. You can capture the result of this request as a
GetCredentialsForIdentityResponse object, as demonstrated in the code snippet below.

**Imports**

.. literalinclude:: dynamodb.java2.mapping.putitem.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.mapping.putitem.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-cognito:`complete example <GetIdentityCredentials.java>` on GitHub.


For more information, see the |COG-dg|_.
