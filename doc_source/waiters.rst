.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#############
Using waiters
#############

.. meta::
   :description: How to poll for AWS resource states using the waiters utility for the AWS SDK for
                 Java v2
   :keywords: AWS SDK for Java, waiters, polling, state, resource, service client

The waiters utility of the |sdk-java| version 2 (v2) enables you to validate that
AWS resources are in a specified state before performing operations on those resources.

A *waiter* is an abstraction used to poll AWS resources, such as |ddb| tables or |s3| buckets, until a
desired state is reached (or until a determination is made that the resource won't ever reach the
desired state). Instead of writing logic to continuously poll your AWS resources, which can be
cumbersome and error-prone, you can use waiters to poll a resource and have your code continue to run after
the resource is ready.


Prerequisites
=============

Before you can use waiters in a project with the |sdk-java|, you must complete the following steps:

-  :doc:`signup-create-iam-user`
   
-  :doc:`setup-credentials`
   
-  Configure your project dependencies (for example, in your :file:`pom.xml` or :file:`build.gradle`
   file) to use version :code:`2.15.0` or later of the |sdk-java|.
   
   For example:
   
   .. code-block:: xml

      <project>
         <dependencyManagement>
            <dependencies>
                  <dependency>
                     <groupId>software.amazon.awssdk</groupId>
                     <artifactId>bom</artifactId>
                     <version>2.15.0</version>
                     <type>pom</type>
                     <scope>import</scope>
                  </dependency>
            </dependencies>
         </dependencyManagement>
      </project>

Using waiters
=============

To instantiate a waiters object, first create a service client. Set the service client's
:methodname:`waiter()` method as the value of the waiter object. Once the waiter instance exists, set
its response options to execute the appropriate code.

Synchronous programming
-----------------------

The following code snippet shows how to wait for a |ddb| table to exist and be in an **ACTIVE** state.

.. code-block:: java

   DynamoDbClient dynamo = DynamoDbClient.create();
   DynamoDbWaiter waiter = dynamo.waiter();

   WaiterResponse<DescribeTableResponse> waiterResponse = 
      waiter.waitUntilTableExists(r -> r.tableName("myTable"));

   // print out the matched response with a tableStatus of ACTIVE
   waiterResponse.matched().response().ifPresent(System.out::println);

Asynchronous programming
------------------------

The following code snippet shows how to wait for a |ddb| table to no longer exist.

.. code-block:: java

   DynamoDbAsyncClient asyncDynamo = DynamoDbAsyncClient.create();
   DynamoDbAsyncWaiter asyncWaiter = asyncDynamo.waiter();

   CompletableFuture<WaiterResponse<DescribeTableResponse>> waiterResponse =
                       asyncWaiter.waitUntilTableNotExists(r -> r.tableName("myTable"));

   waiterResponse.whenComplete((r, t) -> {
      if (t == null) {
         // print out the matched ResourceNotFoundException
         r.matched().exception().ifPresent(System.out::println);
      }
   }).join();

Configuring waiters
===================

You can customize the configuration for a waiter by using the :methodname:`overrideConfiguration()`
on its builder. For some operations, you can apply a custom configuration when you make the request.

Configure a waiter
------------------

The following code snippet shows how to override the configuration on a waiter.

.. code-block:: java

   // sync
   DynamoDbWaiter waiter =
         DynamoDbWaiter.builder()
                       .overrideConfiguration(b -> b.maxAttempts(10))
                       .client(dynamoDbClient)
                       .build();
   // async
   DynamoDbAsyncWaiter asyncWaiter =
         DynamoDbAsyncWaiter.builder()
                       .client(dynamoDbAsyncClient)
                       .overrideConfiguration(o -> o.backoffStrategy(
                                 FixedDelayBackoffStrategy.create(Duration.ofSeconds(2))))
                       .scheduledExecutorService(Executors.newScheduledThreadPool(3))
                       .build();

Override configuration for a specific request
---------------------------------------------

The following code snippet shows how to override the configuration for a waiter on a per-request
basis. Note that only some operations have customizable configurations.

.. code-block:: java

   waiter.waitUntilTableNotExists(b -> b.tableName("myTable"),
                                 o -> o.maxAttempts(10));

   asyncWaiter.waitUntilTableExists(b -> b.tableName("myTable"), 
                                    o -> o.waitTimeout(Duration.ofMinutes(1)));


