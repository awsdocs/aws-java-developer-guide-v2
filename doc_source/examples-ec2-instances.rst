.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

######################
Manage |EC2| instances
######################

.. meta::
   :description: How to create, start, stop, reboot, list and monitor EC2 instances using the AWS
                 SDK for Java 2.x.
   :keywords: AWS SDK for Java 2.x, code examples, EC2 instances, create instance, start instance, stop
              instance, reboot instance, monitor instance, list instances, describe instances


Create an instance
==================

Create a new |EC2| instance by calling the |ec2client|'s :methodname:`runInstances` method,
providing it with a :aws-java-class:`RunInstancesRequest <services/ec2/model/RunInstancesRequest>`
containing the :ec2-ug:`Amazon Machine Image (AMI) <AMIs>` to use and an :ec2-ug:`instance type
<instance-types>`.

**Imports**

.. literalinclude:: ec2.java2.create_instance.import.txt
   :language: java

**Code**

.. literalinclude:: ec2.java2.create_instance.main.txt
   :dedent: 3
   :language: java

See the :sdk-examples-java-ec2:`complete example <CreateInstance.java>` on GitHub.


Start an instance
=================

To start an |EC2| instance, call the |ec2client|'s :methodname:`startInstances` method, providing it
with a :aws-java-class:`StartInstancesRequest <services/ec2/model/StartInstancesRequest>` containing
the ID of the instance to start.

**Imports**

.. literalinclude:: ec2.java2.start_stop_instance.import.txt
   :language: java

**Code**

.. literalinclude:: ec2.java2.start_stop_instance.start.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-ec2:`complete example <StartStopInstance.java>` on GitHub.


Stop an instance
================

To stop an |EC2| instance, call the |ec2client|'s :methodname:`stopInstances` method, providing it
with a :aws-java-class:`StopInstancesRequest <services/ec2/model/StopInstancesRequest>` containing
the ID of the instance to stop.

**Imports**

.. literalinclude:: ec2.java2.start_stop_instance.import.txt
   :language: java

**Code**

.. literalinclude:: ec2.java2.start_stop_instance.stop.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-ec2:`complete example <StartStopInstance.java>` on GitHub.


Reboot an instance
==================

To reboot an |EC2| instance, call the |ec2client|'s :methodname:`rebootInstances` method, providing it
with a :aws-java-class:`RebootInstancesRequest <services/ec2/model/RebootInstancesRequest>` containing
the ID of the instance to reboot.

**Imports**

.. literalinclude:: ec2.java2.reboot_instance.import.txt
   :language: java

**Code**

.. literalinclude:: ec2.java2.reboot_instance.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-ec2:`complete example <RebootInstance.java>` on GitHub.


Describe instances
==================

To list your instances, create a :aws-java-class:`DescribeInstancesRequest
<services/ec2/model/DescribeInstancesRequest>` and call the |ec2client|'s
:methodname:`describeInstances` method. It will return a :aws-java-class:`DescribeInstancesResponse
<services/ec2/model/DescribeInstancesResponse>` object that you can use to list the |EC2| instances
for your account and region.

Instances are grouped by *reservation*. Each reservation corresponds to the call to
:methodname:`startInstances` that launched the instance. To list your instances, you must first call
the :classname:`DescribeInstancesResponse` class' :methodname:`reservations` method, and then call
:methodname:`instances` on each returned
:aws-java-class:`Reservation <services/ec2/model/Reservation>` object.

**Imports**

.. literalinclude:: ec2.java2.describe_instances.import.txt
   :language: java

**Code**

.. literalinclude:: ec2.java2.describe_instances.main.txt
   :dedent: 4
   :language: java

Results are paged; you can get further results by passing the value returned from the result
object's :methodname:`nextToken` method to a new request object's
:methodname:`nextToken` method, then using the new request object in your next call to
:methodname:`describeInstances`.

See the :sdk-examples-java-ec2:`complete example <DescribeInstances.java>` on GitHub.


Monitor an instance
===================

You can monitor various aspects of your |EC2| instances, such as CPU and network utilization,
available memory, and disk space remaining. To learn more about instance monitoring, see
:ec2-ug:`Monitoring Amazon EC2 <monitoring_ec2>` in the |ec2-ug|.

To start monitoring an instance, you must create a :aws-java-class:`MonitorInstancesRequest
<services/ec2/model/MonitorInstancesRequest>` with the ID of the instance to monitor, and pass it to
the |ec2client|'s :methodname:`monitorInstances` method.

**Imports**

.. literalinclude:: ec2.java2.monitor_instance.import.txt
   :language: java

**Code**

.. literalinclude:: ec2.java2.monitor_instance.main.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-ec2:`complete example <MonitorInstance.java>` on GitHub.


Stop instance monitoring
========================

To stop monitoring an instance, create an :aws-java-class:`UnmonitorInstancesRequest
<services/ec2/model/UnmonitorInstancesRequest>` with the ID of the instance to stop monitoring, and
pass it to the |ec2client|'s :methodname:`unmonitorInstances` method.

**Imports**

.. literalinclude:: ec2.java2.monitor_instance.import.txt
   :language: java

**Code**

.. literalinclude:: ec2.java2.monitor_instance.stop.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-ec2:`complete example <MonitorInstance.java>` on GitHub.


More information
================

* :ec2-api:`RunInstances` in the |ec2-api|
* :ec2-api:`DescribeInstances` in the |ec2-api|
* :ec2-api:`StartInstances` in the |ec2-api|
* :ec2-api:`StopInstances` in the |ec2-api|
* :ec2-api:`RebootInstances` in the |ec2-api|
* :ec2-api:`DescribeInstances` in the |ec2-api|
* :ec2-api:`MonitorInstances` in the |ec2-api|
* :ec2-api:`UnmonitorInstances` in the |ec2-api|
