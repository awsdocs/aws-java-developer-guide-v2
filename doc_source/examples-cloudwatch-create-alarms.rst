.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

########################
Working with |CW| Alarms
########################

.. meta::
   :description: How to create, list, and delete alarms in Amazon CloudWatch using the AWS SDK for
                 Java 2.0
   :keywords: create alarm, delete alarm, list alarms, metric alarms, AWS SDK for Java 2.0, example code

Create an Alarm
===============

To create an alarm based on a |cw| metric, call the |cwclient|'s :methodname:`putMetricAlarm` method
with a :aws-java-class-prev:`PutMetricAlarmRequest <services/cloudwatch/model/PutMetricAlarmRequest>`
filled with the alarm conditions.

**Imports**

.. literalinclude:: example_code/cloudwatch/src/main/java/com/example/cloudwatch/PutMetricAlarm.java
   :lines: 16-23
   :language: java

**Code**

.. literalinclude:: example_code/cloudwatch/src/main/java/com/example/cloudwatch/PutMetricAlarm.java
   :lines: 42-66
   :dedent: 8
   :language: java

See the :sdk-examples-java-cloudwatch:`complete example <PutMetricAlarm.java>` on GitHub.

List Alarms
===========

To list the |cw| alarms that you have created, call the |cwclient|'s :methodname:`describeAlarms`
method with a :aws-java-class-prev:`DescribeAlarmsRequest
<services/cloudwatch/model/DescribeAlarmsRequest>` that you can use to set options for the result.

**Imports**

.. literalinclude:: example_code/cloudwatch/src/main/java/com/example/cloudwatch/DescribeAlarms.java
   :lines: 16-20
   :language: java

**Code**

.. literalinclude:: example_code/cloudwatch/src/main/java/com/example/cloudwatch/DescribeAlarms.java
   :lines: 29-58
   :dedent: 8
   :language: java

The list of alarms can be obtained by calling :methodname:`MetricAlarms` on the
:aws-java-class-prev:`DescribeAlarmsResponse <services/cloudwatch/model/DescribeAlarmsResponse>` that is
returned by :methodname:`describeAlarms`.

The results may be *paged*. To retrieve the next batch of results, call :methodname:`nextToken`
on the response object and use the token value to build a new request object. Then
call the :methodname:`describeAlarms` method again with the new request.

.. tip:: You can also retrieve alarms for a specific metric by using the |cwclient|'s
   :methodname:`describeAlarmsForMetric` method. Its use is similar to :methodname:`describeAlarms`.

See the :sdk-examples-java-cloudwatch:`complete example <DescribeAlarms.java>` on GitHub.

Delete Alarms
=============

To delete |cw| alarms, call the |cwclient|'s :methodname:`deleteAlarms` method with a
:aws-java-class-prev:`DeleteAlarmsRequest <services/cloudwatch/model/DeleteAlarmsRequest>` containing one
or more names of alarms that you want to delete.

**Imports**

.. literalinclude:: example_code/cloudwatch/src/main/java/com/example/cloudwatch/DeleteAlarm.java
   :lines: 16-19
   :language: java

**Code**

.. literalinclude:: example_code/cloudwatch/src/main/java/com/example/cloudwatch/DeleteAlarm.java
   :lines: 37-45
   :dedent: 8
   :language: java

See the :sdk-examples-java-cloudwatch:`complete example <DeleteAlarm.java>` on GitHub.

More Information
================

* :cw-ug:`Creating Amazon CloudWatch Alarms <AlarmThatSendsEmail>` in the |cw-ug|
* :cw-api:`PutMetricAlarm <API_PutMetricAlarm>` in the |cw-api|
* :cw-api:`DescribeAlarms <API_DescribeAlarms>` in the |cw-api|
* :cw-api:`DeleteAlarms <API_DeleteAlarms>` in the |cw-api|
