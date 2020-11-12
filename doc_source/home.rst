.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.


.. _release notes: https://github.com/aws/aws-sdk-java-v2#release-notes
.. _change log: https://github.com/aws/aws-sdk-java-v2/blob/master/CHANGELOG.md
.. _AWS Blog: https://aws.amazon.com/blogs/developer/aws-sdk-for-java-2-0-developer-preview/
.. _migration guide: https://docs.aws.amazon.com/sdk-for-java/v2/migration-guide/what-is-java-migration.html

################################
Developer guide - |sdk-java| 2.x
################################

.. meta::
    :description:
         Welcome to the Developer Guide for version 2.x of the AWS SDK for Java
    :keywords: aws, sdk, java

The |sdk-java| provides a Java API for |AWSlong|. Using the SDK, you can easily build Java
applications that work with |S3|, |EC2|, |DDB|, and more.

The |sdk-java-v2| is a major rewrite of the version 1.x code base. It's built on top of
Java 8+ and adds several frequently requested features. These include support for non-blocking I/O
and the ability to plug in a different HTTP implementation at run time. For more information see
the `AWS Blog`_. For guidance on migrating your application from 1.11.x to 2.x, see the
`migration guide`_.

We regularly add support for new services to the |sdk-java|. For a list of changes and
features in a particular version, view the `change log`_.

Get started with the |sdk-java-v2|
==================================

If you're ready to get hands-on with the SDK, follow the :doc:`Quick Start <get-started>` tutorial.

To set up your development environment, see :doc:`Setting up <setup>`.

For information on making requests to |s3|, |ddb|, |ec2| and other Amazon Web Services, see :doc:`examples`.

Developing AWS applications for Android
=======================================

If you're an Android developer, |AWSlong| publishes an SDK made specifically for Android
development: the |sdk-android|_. See the |sdk-android-dg|_ for the complete documentation.

Maintenance and support for SDK major versions
==============================================

For information about maintenance and support for SDK major versions and their underlying
dependencies, see the following in the
`AWS SDKs and Tools Shared Configuration and Credentials Reference Guide <https://docs.aws.amazon.com/credref/latest/refdocs/overview.html>`_

* `AWS SDKs and Tools Maintenance Policy <https://docs.aws.amazon.com/credref/latest/refdocs/maint-policy.html>`_
* `AWS SDKs and Tools Version Support Matrix <https://docs.aws.amazon.com/credref/latest/refdocs/version-support-matrix.html>`_

Additional resources
====================

In addition to this guide, the following are valuable online resources for |sdk-java|
developers:

* :aws-java-class-root:`AWS SDK for Java 2.x Reference <>`

* :blog:`Java developer blog <developer/category/java>`

* :forum:`Java developer forums <70>`

* GitHub:

  + :github:`Documentation source <awsdocs/aws-java-developer-guide-v2>`

  + :github:`SDK source <aws/aws-sdk-java-v2>`

* The `AWS Code Sample Catalog <https://docs.aws.amazon.com/code-samples/latest/catalog>`_

* `@awsforjava (Twitter) <https://twitter.com/awsforjava>`_

.. _features_notyet:

Features not yet in the |sdk-java-v2|
-------------------------------------

See the following Github issues for details about additional features not yet in 2.x. Comments and
feedback are also welcome.

* High-level libraries

  + `S3 Transfer manager <https://github.com/aws/aws-sdk-java-v2/issues/37>`_

  + `S3 Encryption Client <https://github.com/aws/aws-sdk-java-v2/issues/34>`_

  + `DynamoDB document APIs <https://github.com/aws/aws-sdk-java-v2/issues/36>`_

  + `DynamoDB Encryption Client <https://github.com/aws/aws-sdk-java-v2/issues/34>`_

  + `SQS Client-side Buffering <https://github.com/aws/aws-sdk-java-v2/issues/848>`_

* `Progress Listeners <https://github.com/aws/aws-sdk-java-v2/issues/25>`_

Contributing to the SDK
-----------------------

Developers can also contribute feedback through the following channels:

* Submit issues on GitHub:

  + :github:`Submit documentation issues <awsdocs/aws-java-developer-guide-v2/issues>`

  + :github:`Submit SDK issues <aws/aws-sdk-java-v2/issues>`

* Join an informal chat about SDK on the AWS SDK for Java 2.x `gitter channel <https://gitter.im/aws/aws-sdk-java-v2>`_

* Submit feedback anonymously to aws-java-sdk-v2-feedback@amazon.com. This email is
  monitored by the AWS SDK for Java team.

* Submit pull requests in the documentation or SDK source GitHub repositories to contribute
  to the SDK development.


