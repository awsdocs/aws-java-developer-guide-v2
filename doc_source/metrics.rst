.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

####################
Enabling SDK metrics
####################

.. meta::
   :description: How to enable and configure metrics collection and publishing for the AWS SDK for
   Java v2
   :keywords: AWS SDK for Java, metrics, configuration, service client, collect, data, CloudWatch

With the AWS SDK for Java 2.x, you can collect metrics about the service clients in your
application, analyze the output in Amazon CloudWatch, and then act on it.

By default, metrics collection is disabled in the SDK. This topic helps you to enable and configure
it.

.. toctree::
   :titlesonly:
   :maxdepth: 1

   metrics-list

Prerequisites
=============

Before you can enable and use metrics, you must complete the following steps:

-  `Sign up for AWS and create an IAM
   user <https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/signup-create-iam-user.html>`_
   
-  `Set up AWS credentials and region for development
   <https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html>`_
   
-  Configure your project dependencies (for example, in your :file:`pom.xml` or :file:`build.gradle`
   file) to use version :code:`2.14.0` or later of the |sdk-java|.
   
   To enabling publishing of metrics to |cw|, also include the artifactId
   :literal:`cloudwatch-metric-publisher` with the version number :code:`2.14.0` or later in your
   project's dependencies.
   
   For example:
   
   .. code-block:: xml

      <project>
         <dependencyManagement>
            <dependencies>
                  <dependency>
                     <groupId>software.amazon.awssdk</groupId>
                     <artifactId>bom</artifactId>
                     <version>2.14.0</version>
                     <type>pom</type>
                     <scope>import</scope>
                  </dependency>
            </dependencies>
         </dependencyManagement>
         <dependencies>
            <dependency>
                  <groupId>software.amazon.awssdk</groupId>
                  <artifactId>cloudwatch-metric-publisher</artifactId>
                  <version>2.14.0</version>
            </dependency>
         </dependencies>
      </project>

.. tip:: To enhance the security of your application, you can use dedicated set of credentials for
   publishing metrics to CloudWatch. Create a separate IAM user with
   `cloudwatch:PutMetricData <https://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_PutMetricData.html>`_
   permissions and then use that user's access key as credentials in the MetricPublisher
   configuration for your application.

   For more information, see the `Amazon CloudWatch Permissions Reference
   <https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/permissions-reference-cw.html#cw-permissions-table>`_
   in the
   `Amazon CloudWatch User Guide <https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring>`_
   and
   `Adding and Removing IAM Identity Permissions <https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage-attach-detach.html>`_
   inthe `AWS IAM User Guide <https://docs.aws.amazon.com/IAM/latest/UserGuide/>`_.

How to enable metrics collection
================================

You can enable metrics in your application for a service client or on individual requests.

Enable metrics for a specific request
-------------------------------------

The following code snippet shows how to enable the CloudWatch metrics publisher for a request to
Amazon DynamoDB. It uses the default metrics publisher configuration.

.. code-block:: java

   MetricPublisher metricsPub = CloudWatchMetricPublisher.create();
   DynamoDbClient ddb = DynamoDbClient.create();
   ddb.listTables(ListTablesRequest.builder()
       .overrideConfiguration(c -> c.addMetricPublisher(metricsPub))
       .build());

Enable metrics for a specific service client
--------------------------------------------

The following code snippet shows how to enable the CloudWatch metrics publisher for a service client.

.. code-block:: java

   MetricPublisher metricsPub = CloudWatchMetricPublisher.create();

   DynamoDbClient ddb = DynamoDbClient.builder()
                       .overrideConfiguration(c -> c.addMetricPublisher(metricsPub))
                       .build();

The following snippet demonstrates how to use a custom configuration for the metrics publisher for
a specific service client. The customizations include loading a separate credentials profile,
specifying a different region than the service client, and customizing how often the publisher
sends metrics to CloudWatch.

.. code-block:: java

   MetricPublisher metricsPub = CloudWatchMetricPublisher.builder()
                       .credentialsProvider(EnvironmentVariableCredentialsProvider.create("cloudwatch"))
                       .region(Region.US_WEST_2)
                       .publishFrequency(5, TimeUnit.MINUTES)
                       .build();

   Region region = Region.US_EAST_1;
   DynamoDbClient ddb = DynamoDbClient.builder()
                       .region(region)
                       .overrideConfiguration(c -> c.addMetricPublisher(metricsPub))
                       .build();

What information is collected?
==============================

Metrics collection includes the following:

-  Number of API requests, including whether they succeed or fail
-  Information about the AWS services you call in your API requests, including exceptions returned
-  The duration for various operations such as Marshalling, Signing, and HTTP requests
-  HTTP client metrics, such as the number of open connections, the number of pending requests, and
   the name of the HTTP client used

.. note:: The metrics available vary by HTTP client.

For a complete list, see :doc:`configuration-metrics-list`.

How can I use this information?
-------------------------------

You can use the metrics the SDK collects to monitor the service clients in your application. You can
look at overall usage trends, identify anomalies, review service client exceptions returned, or to
dig in to understand a particular issue. Using Amazon CloudWatch, you can also create alarms to
notify you as soon as your application reaches a condition that you define.

For more information, see `Using Amazon CloudWatch
Metrics <https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/working_with_metrics.html>`__
and `Using Amazon CloudWatch
Alarms <https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/AlarmThatSendsEmail.html>`__
in the `Amazon CloudWatch User
Guide <https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/>`__.
