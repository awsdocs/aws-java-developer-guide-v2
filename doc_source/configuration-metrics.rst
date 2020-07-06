.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#########################
Enable metrics collection
#########################

.. meta::
   :description: How to enable metrics collection for the AWS SDK for Java v2
   :keywords: AWS SDK for Java, metrics, configuration, service client, collect, data, CloudWatch

With the AWS SDK for Java version 2 (v2), you can collect metrics about the service clients in your
application, analyze the output in Amazon CloudWatch, and then act on it.

By default, metrics collection is disabled in the SDK. This topic helps you to enable and configure
it.

.. note:: You can output metrics to destinations other than CloudWatch by developing a custom
   metrics publisher. For more information, see Output metrics to a custom publisher.

Prerequisites
=============

Before you can enable and use metrics, you must complete the following steps:

-  `Sign up for AWS and create an IAM
   user <https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/signup-create-iam-user.html>`_
-  `Set up AWS credentials and region for development
   <https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html>`_
-  Configure the SDK for Java v2 dependency in your project configuration (e.g. :file:`pom.xml` or
   :file:`build.gradle` file) to version [2.14.x] or later.

.. tip:: To enhance the security of your application, you can use dedicated set of credentials for
   publishing metrics to CloudWatch. Create a separate IAM user with
   `cloudwatch:PutMetricData <https://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_PutMetricData.html>`_
   permissions and then use that user's access key as credentials in the MetricPublisher
   configuration for your application.

   For more information, see the `Amazon CloudWatch Permissions Reference
   <https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/permissions-reference-cw.html#cw-permissions-table>`_
   in the `Amazon CloudWatch User Guide <http://monitoring/>`__ and `Adding and Removing IAM
   Identity Permissions
   <https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage-attach-detach.html>`_ in
   the `AWS IAM User Guide <https://docs.aws.amazon.com/IAM/latest/UserGuide/>`_.

How to enable metrics collection
================================

You can enable metrics per request or per service client in your code, or globally using a Java
System Property or an Environment Variable.

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

Enable metrics globally
-----------------------

To enable the metrics publisher by default for all requests via service clients, use one of the
following options:

-  Set the environment variable :code:`AWS_METRIC_PUBLISHING_ENABLED` to :literal:`true`

To set these variables on Linux/Unix or macOS, use the :code:`export` command in a terminal.

-  :literal:`export AWS_METRIC_PUBLISHING_ENABLED=true`

To set these variables on Windows, use the :code:`set` command in a command prompt.

-  :literal:`set AWS_METRIC_PUBLISHING_ENABLED=true`

-  Set the Java system property :code:`aws.metricPublishingEnable` to :literal:`true`

To set this outside of your application code, you can add
:code:`-Daws.metricPublishingEnabled=true` to the Java command you use to run your app. For
example:

-  :literal:`java myapp.jar -Daws.metricPublishingEnabled=true`

To set this within the code for your application, add the following:

-  :literal:`System.setProperty("aws.metricPublishingEnabled", "true");`

What information is collected?
==============================

Metrics collection includes the following:

-  Number of API requests, including whether they succeed or fail
-  Information about the AWS services you call in your API requests, including exceptions returned
-  The duration for various operations such as Marshalling, Signing, and HTTP requests
-  HTTP client metrics, such as the number of open connections, the number of pending requests, and
   the name of the HTTP client used.

.. note:: The metrics available vary by HTTP client.

For a complete list, see Service client metrics.

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

Additional information
======================

To publish (output) the SDK metrics data collected to somewhere other than Amazon CloudWatch, see
Output SDK metrics to a custom publisher.
