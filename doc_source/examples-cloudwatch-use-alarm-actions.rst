.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#########################
Use alarm actions in |CW|
#########################

.. meta::
   :description: How to enable or disable alarm actions for Amazon Cloudwatch with the AWS SDK for
                 Java 2.x.
   :keywords: cloudwatch alarms, enable alarms, disable alarms, AWS SDK for Java 2.x, code examples

Using |cw| alarm actions, you can create alarms that perform actions such as automatically stopping,
terminating, rebooting, or recovering |ec2| instances.

.. note:: Alarm actions can be added to an alarm by using the
   :aws-java-class:`PutMetricAlarmRequest <services/cloudwatch/model/PutMetricAlarmRequest>`'s
   :methodname:`alarmActions` method when :doc:`creating an alarm <examples-cloudwatch-create-alarms>`.


Enable alarm actions
====================

To enable alarm actions for a |cw| alarm, call the |cwclient|'s :methodname:`enableAlarmActions`
with a :aws-java-class:`EnableAlarmActionsRequest
<services/cloudwatch/model/EnableAlarmActionsRequest>` containing one or more names of alarms whose
actions you want to enable.

**Imports**

.. literalinclude:: cloudwatch.java2.enable_alarm_actions.import.txt
   :language: java

**Code**

.. literalinclude:: cloudwatch.java2.enable_alarm_actions.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-cloudwatch:`complete example <EnableAlarmActions.java>` on GitHub.

Disable alarm actions
=====================

To disable alarm actions for a |cw| alarm, call the |cwclient|'s :methodname:`disableAlarmActions`
with a :aws-java-class:`DisableAlarmActionsRequest
<services/cloudwatch/model/DisableAlarmActionsRequest>` containing one or more names of alarms whose
actions you want to disable.

**Imports**

.. literalinclude:: cloudwatch.java2.disable_alarm_actions.import.txt
   :language: java

**Code**

.. literalinclude:: cloudwatch.java2.disable_alarm_actions.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-cloudwatch:`complete example <DisableAlarmActions.java>` on GitHub.

More information
================

* :cw-ug:`Create Alarms to Stop, Terminate, Reboot, or Recover an Instance <UsingAlarmActions>` in
  the |cw-ug|
* :cw-api:`PutMetricAlarm <API_PutMetricAlarm>` in the |cw-api|
* :cw-api:`EnableAlarmActions <API_EnableAlarmActions>` in the |cw-api|
* :cw-api:`DisableAlarmActions <API_DisableAlarmActions>` in the |cw-api|
