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


With the AWS SDK for Java version 2 (v2), you can collect metrics about the service clients in
your application and then publish (output) those metrics to Amazon CloudWatch.

This topic contains the list and descriptions for the metrics that are collected.

For more information about enabling and configuring metrics for the SDK, see Enable metrics
collection.


.. _metrics-perrequest:

Metrics collected with each request
===================================

+------------------------+------------------------+----------------------+------------------------+
| Metric name            | Description            | Type                 | Collected by default?  |
+========================+========================+======================+========================+
| ServiceId              | Service ID of the AWS  | String               | Yes                    |
|                        | service that the API   |                      |                        |
|                        | request is made        |                      |                        |
|                        | against                |                      |                        |
+------------------------+------------------------+----------------------+------------------------+
| OperationName          | The name of the AWS    | String               | Yes                    |
|                        | API the request is     |                      |                        |
|                        | made to                |                      |                        |
+------------------------+------------------------+----------------------+------------------------+
| ApiCallDuration        | The total time taken   | Duration             | Yes                    |
|                        | to finish a request    |                      |                        |
|                        | (inclusive of all      |                      |                        |
|                        | retries),              |                      |                        |
|                        | ApiCallEndTime -       |                      |                        |
|                        | ApiCallStartTime       |                      |                        |
+------------------------+------------------------+----------------------+------------------------+
| MarshallingDuration    | The time taken to      | Duration             | Yes                    |
|                        | marshall the request   |                      |                        |
+------------------------+------------------------+----------------------+------------------------+
| Cr                     | The time taken to      | Duration             | Yes                    |
| edentialsFetchDuration | fetch signing          |                      |                        |
|                        | credentials for the    |                      |                        |
|                        | request                |                      |                        |
+------------------------+------------------------+----------------------+------------------------+

.. _metrics-perattempt:

Metrics collected for each request attempt
------------------------------------------

Each API call that your application makes may take multiple attempts before responded with a success
or failure. These metrics are collected for each attempt.

+------------------------+------------------------+----------------------+------------------------+
| Metric name            | Description            | Type                 | Collected by default?  |
+========================+========================+======================+========================+
| SigningDuration        | The time it takes to   | Duration             | Yes                    |
|                        | sign the HTTP request  |                      |                        |
+------------------------+------------------------+----------------------+------------------------+
| Ht                     | The total time it      | Duration             | Yes                    |
| tpRequestRoundTripTime | takes to send an HTTP  |                      |                        |
|                        | request and receive    |                      |                        |
|                        | the response           |                      |                        |
+------------------------+------------------------+----------------------+------------------------+
| HttpStatusCode         | The status code        | Integer              | Yes                    |
|                        | returned with the HTTP |                      |                        |
|                        | response               |                      |                        |
+------------------------+------------------------+----------------------+------------------------+
| AwsRequestId           | The request ID of the  | String               | Yes                    |
|                        | service request        |                      |                        |
+------------------------+------------------------+----------------------+------------------------+
| AwsExtendedRequestId   | The extended request   | String               | Yes                    |
|                        | ID of the service      |                      |                        |
|                        | request                |                      |                        |
+------------------------+------------------------+----------------------+------------------------+
| Exception              | The total time taken   | Throwable            | Yes                    |
|                        | for an Api call        |                      |                        |
|                        | attempt (exclusive of  |                      |                        |
|                        | retries),              |                      |                        |
|                        | ApiCallAttemptEndTime  |                      |                        |
|                        | -                      |                      |                        |
|                        | A                      |                      |                        |
|                        | piCallAttemptStartTime |                      |                        |
+------------------------+------------------------+----------------------+------------------------+
