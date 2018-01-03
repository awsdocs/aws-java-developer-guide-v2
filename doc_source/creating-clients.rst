.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

########################
Creating Service Clients
########################

.. meta::
   :description: How to create service clients by using the AWS SDK for Java.
   :keywords:

To make requests to |AWSlong|, you first create a service client object. In version 2.0 of the SDK,
you can create clients only by using service client builders.

Each AWS service has a service interface with methods
for each action in the service API. For example, the service interface for |DDBlong| is named
:aws-java-class:`DynamoDbClient <services/dynamodb/DynamoDBClient>`. Each service interface has a
static factory builder method you can use to construct an implementation of the service interface.

Obtaining a Client Builder
==========================

To obtain an instance of the client, use the static factory method ``builder``.
Then customize it by using the setters in the builder, as shown in the following example.

In the |sdk-java| 2.0, the setters are named without the ``with`` prefix.

.. code-block:: java

    DynamoDBClient client = DynamoDBClient.builder()
                            .region(Region.US_WEST_2)
                            .credentialsProvider(ProfileCredentialsProvider.builder()
                                         .profileName("myProfile")
                                         .build())
                            .build();

.. note:: The fluent setter methods return the ``builder`` object, so that you can chain the
   method calls for convenience and for more readable code. After you configure the properties you
   want, you can call the ``build`` method to create the client. Once a client is created,
   it's immutable. The only way to create a client with different settings is to build a new client.

Using DefaultClient
===================

The client builders have another factory method named :methodname:`create`.
This method creates a service client with the default configuration.
It uses the default provider chain to load credentials and the AWS Region. If credentials or
the region can't be determined from the environment that the application is running in, the call to
:methodname:`create` fails. See :doc:`credentials` and :doc:`java-dg-region-selection`
for more information about how credentials and region are determined.

.. topic:: To create a default client

   .. code-block:: java

      DynamoDBClient client = DynamoDBClient.create();

Client Lifecycle
================

Service clients in the SDK are thread-safe. For best performance, treat them as
long-lived objects. Each client has its own connection pool resource that is released when the
client is garbage collected. The clients in the |sdk-java| 2.0 now extend the AutoClosable
interface. For best practices, explicitly close a client by calling the :methodname:`close` method.

.. topic:: To close a client

   .. code-block:: java

      DynamoDBClient client = DynamoDBClient.create();
      client.close();
