.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

######################
Service client metrics
######################

.. meta::
   :description: List of service client metrics collected and published for the AWS SDK for Java
   version 2 (v2)
   :keywords: AWS SDK for Java, metrics, configuration, service client, collect, data, list,
   CloudWatch


With the |sdk-java| version 2 (v2), you can collect metrics about the service clients in
your application and then publish (output) those metrics to |cw|.

This topic contains the list and descriptions for the metrics that are collected.

For more information about enabling and configuring metrics for the SDK, see
:doc:`configuration-metrics`.


.. _metrics-perrequest:

Metrics collected with each request
===================================

+--------------------------+------------------------+--------------------+------------------------+
| Metric name              | Description            | Type               | Collected by default?  |
+==========================+========================+====================+========================+
| ServiceId                | Service ID of the AWS  | String             | Yes                    |
|                          | service that the API   |                    |                        |
|                          | request is made        |                    |                        |
|                          | against                |                    |                        |
+--------------------------+------------------------+--------------------+------------------------+
| OperationName            | The name of the AWS    | String             | Yes                    |
|                          | API the request is     |                    |                        |
|                          | made to                |                    |                        |
+--------------------------+------------------------+--------------------+------------------------+
| ApiCallSuccessful        | True if the API call   | Boolean            | Yes                    |
|                          | was successful; false  |                    |                        |
|                          | if not                 |                    |                        |
+--------------------------+------------------------+--------------------+------------------------+
| RetryCount               | Number of times the    | Integer            | Yes                    |
|                          | SDK retried the API    |                    |                        |
|                          | call                   |                    |                        |
+--------------------------+------------------------+--------------------+------------------------+
| ApiCallDuration          | The total time taken   | Duration           | Yes                    |
|                          | to finish a request    |                    |                        |
|                          | (inclusive of all      |                    |                        |
|                          | retries)               |                    |                        |
+--------------------------+------------------------+--------------------+------------------------+
| MarshallingDuration      | The time taken to      | Duration           | Yes                    |
|                          | marshall the request   |                    |                        |
+--------------------------+------------------------+--------------------+------------------------+
| CredentialsFetchDuration | The time taken to      | Duration           | Yes                    |
|                          | fetch signing          |                    |                        |
|                          | credentials for the    |                    |                        |
|                          | request                |                    |                        |
+--------------------------+------------------------+--------------------+------------------------+

.. _metrics-perattempt:

Metrics collected for each request attempt
------------------------------------------

Each API call that your application makes may take multiple attempts before responded with a success
or failure. These metrics are collected for each attempt.

+----------------------------+------------------------+-------------------+-----------------------+
| Metric name                | Description            | Type              | Collected by default? |
+============================+========================+===================+=======================+
| BackoffDelayDuration       | The duration of time   | Duration          | Yes                   |
|                            | the SDK waited before  |                   |                       |
|                            | this API call attempt  |                   |                       |
+----------------------------+------------------------+-------------------+-----------------------+
| MarshallingDuration        | The time it takes to   | Duration          | Yes                   |
|                            | marshall an SDK        |                   |                       |
|                            | request to an HTTP     |                   |                       |
|                            | request                |                   |                       |
+----------------------------+------------------------+-------------------+-----------------------+
| SigningDuration            | The time it takes to   | Duration          | Yes                   |
|                            | sign the HTTP request  |                   |                       |
+----------------------------+------------------------+-------------------+-----------------------+
| ServiceCallDuration        | The time it takes to   | Duration          | Yes                   |
|                            | connect to the         |                   |                       |
|                            | service, send the      |                   |                       |
|                            | request, and receive   |                   |                       |
|                            | the HTTP status code   |                   |                       |
|                            | and header from the    |                   |                       |
|                            | response               |                   |                       |
+----------------------------+------------------------+-------------------+-----------------------+
| UnmarshallingDuration      | The time it takes to   | Duration          | Yes                   |
|                            | unmarshall an HTTP     |                   |                       |
|                            | response to an SDK     |                   |                       |
|                            | response               |                   |                       |
+----------------------------+------------------------+-------------------+-----------------------+
| AwsRequestId               | The request ID of the  | String            | Yes                   |
|                            | service request        |                   |                       |
+----------------------------+------------------------+-------------------+-----------------------+
| AwsExtendedRequestId       | The extended request   | String            | Yes                   |
|                            | ID of the service      |                   |                       |
|                            | request                |                   |                       |
+----------------------------+------------------------+-------------------+-----------------------+
| HttpClientName             | The name of the HTTP   | String            | Yes                   |
|                            | being use for the      |                   |                       |
|                            | request                |                   |                       |
+----------------------------+------------------------+-------------------+-----------------------+
| MaxConcurrency             | The max number of      | Integer           | Yes                   |
|                            | concurrent requests    |                   |                       |
|                            | supported by the HTTP  |                   |                       |
|                            | client                 |                   |                       |
+----------------------------+------------------------+-------------------+-----------------------+
| AvailableConcurrency       | The number of          | Integer           | Yes                   |
|                            | remaining concurrent   |                   |                       |
|                            | requests that can be   |                   |                       |
|                            | supported by the HTTP  |                   |                       |
|                            | client without         |                   |                       |
|                            | needing to establish   |                   |                       |
|                            | another connection     |                   |                       |
+----------------------------+------------------------+-------------------+-----------------------+
| LeasedConcurrency          | The number of request  | Integer           | Yes                   |
|                            | currently being        |                   |                       |
|                            | executed by the HTTP   |                   |                       |
|                            | client                 |                   |                       |
+----------------------------+------------------------+-------------------+-----------------------+
| PendingConcurrencyAcquires | The number of requests | Integer           | Yes                   |
|                            | that are blocked,      |                   |                       |
|                            | waiting for another    |                   |                       |
|                            | TCP connection or a    |                   |                       |
|                            | new stream to be       |                   |                       |
|                            | available from the     |                   |                       |
|                            | connection pool        |                   |                       |
+----------------------------+------------------------+-------------------+-----------------------+
| HttpStatusCode             | The status code        | Integer           | Yes                   |
|                            | returned with the HTTP |                   |                       |
|                            | response               |                   |                       |
+----------------------------+------------------------+-------------------+-----------------------+

