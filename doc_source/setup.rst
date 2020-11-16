.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#########################################
Setting up the |sdk-java| version 2 (2.x)
#########################################

The |sdk-java-v2| provides Java APIs for |AWSlong| (|AWS|). Using the SDK, you can build
Java applications that work with |S3|, |EC2|, |DDB|, and more.

This section provides information about how to set up your development environment and projects to
use the latest version (2.x) of the |sdk-java|.

.. toctree::

   setup-awsaccount
   setup-iamuser
   setup-javamaven
   Setting up AWS credentials and region <setup-credentials>
   setup-project-maven
   setup-project-gradle
   Downloading the SDK <setup-install>

.. _setup-overview:

Overview
========

To make requests to |aws| using the |sdk-java|, you need the following:

   * An active AWS account.

   * An |iamlong| (|iam|) user with

      * A programmatic access key.
      * Appropriate permissions to the |AWS| resources you'll access using your application.

   * A development environment

      * Configured to use your access key as credentials for |AWS|.
      * With Java 8 or later and |MVNlong| installed.

.. _setup-awsaccount:

Create an AWS account
=====================

If you do not have an AWS account, visit
`the Amazon Web Services signup page <https://portal.aws.amazon.com/billing/signup>`_
and follow the on-screen prompts to create and activate a new account.

   For more detailed instructions, see
   `How do I create and activate a new AWS account? <https://aws.amazon.com/premiumsupport/knowledge-center/create-and-activate-aws-account/>`_.
   
   After you activate your new AWS account, follow the instructions in
   `Creating your first IAM admin user and group <https://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html#getting-started_create-admin-group-console>`_
   in the |IAM-ug|. Use this account instead of the root account when accessing the AWS Console.
   For more information, see
   `Security best practices in IAM <https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html#lock-away-credentials>`_
   in the |IAM-ug|_.

.. _setup-iamuser:

Create an |iam| user and programmatic access key
================================================

To use the |sdk-java| to access |AWSlong| (|AWS|), you need an |AWS| account and |AWS| credentials.
To increase the security of your AWS account, we recommend that you use an *IAM user*
to provide access credentials instead of using your AWS account credentials.

.. tip:: For an overview of IAM users and why they are important for the security
   of your account, see :aws-gr:`AWS Security Credentials <aws-security-credentials>`
   in the |AWS-gr|.

For instructions on creating an access key for an existing |iam| user, see
`Programmatic access <https://docs.aws.amazon.com/general/latest/gr/aws-sec-cred-types.html#access-keys-and-secret-access-keys>`_
in the |IAM-ug|_.

.. include:: common/procedure-create-iam-user.txt

.. _setup-javamaven:

Install Java and |mvnlong|
==========================

Your development environment needs to have Java 8 or later and |mvnlong| installed.

* The |sdk-java| works with the
  `Oracle Java SE Development Kit <https://www.oracle.com/java/technologies/javase-downloads.html>`_
  and with distributions of Open Java Development Kit (OpenJDK) such as
  `Amazon Corretto <https://aws.amazon.com/corretto/>`_,
  `Red Hat OpenJDK <https://developers.redhat.com/products/openjdk>`_, and
  `AdoptOpenJDK <https://adoptopenjdk.net/>`_.

* Go to http://maven.apache.org/ for information on how to install and use |MVN|.

