.. Copyright 2010-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

####################
AWS Region Selection
####################

.. meta::
   :description: How to check service availability and choose an AWS Region and specific endpoints.
   :keywords:

Regions enable you to access AWS services that physically reside in a specific geographic area. This
can be useful both for redundancy and to keep your data and applications running close to where you
and your users will access them.

In |sdk-java| version 2.0, all the different region related classes from version 1.x have been collapsed
into one Region class.
You can use this class for all region-related actions such as retrieving metadata about a region
or
checking whether a service is available in a region.


.. _region-selection-choose-region:

Choosing a Region
=================

You can specify a region name and the SDK will automatically choose an appropriate endpoint for you.

To explicitly set a region, we recommend that you use the constants defined in the
:aws-java-class:`Region <regions/Region>` class. This is an enumeration of all publicly available
regions. To create a client with a region from the class, use the following code.

.. code-block:: java

    EC2Client ec2 = EC2Client.builder()
                        .region(Region.US_WEST_2)
                        .build();

If the region you are attempting to use isn't one of the constants in the :classname:`Region`
class, you can create a new region using the :methodname:`of` method. This feature allows you
access to new Regions without upgrading the SDK. 

.. code-block:: java

    Region newRegion = Region.of("us-east-42");
    EC2Client ec2 = EC2Client.builder()
                        .region(newRegion)
                        .build();

.. note:: After you build a client with the builder, it's *immutable* and the region *cannot
   be changed*. If you are working with multiple AWS Regions for the same service, you should
   create multiple clients |mdash| one per region.

Automatically Determine the AWS Region from the Environment
=============================================================

When running on |EC2| or |LAMlong|, you might want to configure clients to use the same region
that your code is running on. This decouples your code from the environment it's running in and
makes it easier to deploy your application to multiple regions for lower latency or redundancy.

To use the default credential/region provider chain to determine the region from the environment,
use the client builder's :methodname:`create` method.

.. code-block:: java

   EC2Client ec2 = EC2Client.create();

If you don't explicitly set a region using the :methodname:`region` method, the SDK
consults the default region provider chain to try and determine the region to use.


Default Region Provider Chain
-----------------------------

**The following is the region lookup process:**

#. Any explicit region set by using :methodname:`region` on the builder
   itself takes precedence over anything else.

#. The :envvar:`AWS_REGION` environment variable is checked. If it's set, that region is
   used to configure the client.

   .. note:: This environment variable is set by the |LAM| container.

#. The SDK checks the AWS shared configuration file (usually located at :file:`~/.aws/config`). If
   the :paramname:`region` property is present, the SDK uses it.

   * The :envvar:`AWS_CONFIG_FILE` environment variable can be used to customize the location of the
     shared config file.

   * The :envvar:`AWS_PROFILE` environment variable or the :paramname:`aws.profile` system property
     can be used to customize the profile that the SDK loads.

#. The SDK attempts to use the |EC2| instance metadata service to determine the region of the
   currently running |EC2| instance.

#. If the SDK still hasn't found a region by this point, client creation fails with an
   exception.

When developing AWS applications, a common approach is to use the *shared configuration file*
(described in :ref:`credentials-default`) to set the region for local development, and rely on the default region
provider chain to determine the region when running on AWS infrastructure. This greatly simplifies
client creation and keeps your application portable.

.. _region-selection-query-service:

Checking for Service Availability in an AWS Region
==================================================

To see if a particular AWS service is available in a region, use the
:methodname:`serviceMetadata` and :methodname:`region` method on the service
that you'd like to check.

.. code-block:: java

    DynamoDBClient.serviceMetadata().regions().forEach(System.out::println);

See the :aws-java-class:`Region <regions/Region>` class documentation for the regions you can specify,
and use the endpoint prefix of the service to query.
