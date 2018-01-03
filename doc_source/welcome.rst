.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

.. meta::
    :description:
         Welcome to the AWS Java Developer Guide

.. _release notes: https://github.com/aws/aws-sdk-java-v2#release-notes
.. _change log: https://github.com/aws/aws-sdk-java-v2/blob/master/CHANGELOG.md
.. _AWS Blog: https://aws.amazon.com/blogs/developer/aws-sdk-for-java-2-0-developer-preview/

#########################################################
|sdk-java-dg-v2| (Developer Preview)
#########################################################

The |sdk-java|_ provides a Java API for |AWSlong|. Using the SDK, you can easily build Java
applications that work with |S3|, |EC2|, |DDB|, and more. We regularly add support for new services
to the |sdk-java|. For a list of changes and features in a particular version,
view the `change log`_.

.. _whats_new:

What's New in Version 2.0
=========================

The |sdk-java-v2| is a major rewrite of the version 1.x code base. It's built on top of
Java 8 and adds several frequently requested features. These include support for non-blocking I/O
and the ability to plug in a different HTTP implementation at run time. For more information see
the `AWS Blog`_.

.. important:: This is a preview release and is not recommended for production environments.

.. _1.0-support:

Support for 1.0
===============

We are not dropping support for the 1.x versions of the AWS SDK for Java currently.
As we get closer to the final production release, we will share a detailed plan for continued
1.x support, similar to how we rolled out major versions of other AWS SDKs.

Additional Resources
====================

In addition to this guide, the following are valuable online resources for |sdk-java|
developers:

* |aws_java_api_v2_url|_

* :blog:`Java developer blog <developer/category/java>`

* :forum:`Java developer forums <70>`

* GitHub:

  + :github:`Documentation source <awsdocs/aws-java-developer-guide-v2>`

  + :github:`SDK source <aws/aws-sdk-java-v2>`

* `@awsforjava (Twitter) <https://twitter.com/awsforjava>`_

Contributing to the Developer Preview
=====================================

Developers can also contribute feedback through the following channels:

* Submit issues on GitHub:

  + :github:`Submit documentation issues <awsdocs/aws-java-developer-guide-v2/issues>`

  + :github:`Submit SDK issues <aws/aws-sdk-java-v2/issues>`

* Join an informal chat about SDK on the |sdk-java-v2| `gitter channel <https://gitter.im/aws/aws-sdk-java-v2>`_

* Submit feedback anonymously to aws-java-sdk-v2-feedback@amazon.com. This email is
  monitored by the AWS SDK for Java team.

* Submit pull requests in the documentation or SDK source GitHub repositories to contribute
  to the SDK development.


.. _eclipse-support:

Eclipse IDE Support
===================

The |tke| doesn't currently support the |sdk-java-v2|. To use the |tke| with
the |sdk-java-v2|, you should use Maven tools in Eclipse to add a dependency on the 2.0 SDK.

.. _android-support:

Developing AWS Applications for Android
=======================================

If you're an Android developer, |AWSlong| publishes an SDK made specifically for Android
development: the |sdk-android|_. See the |sdk-android-dg|_ for the complete documentation.
