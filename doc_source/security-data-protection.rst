.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#################################
Data protection in |sdk-java| 2.x
#################################

.. meta::
   :description: Learn how the AWS shared responsibility model applies to data protection in this AWS product or service.
   :keywords:

.. include:: common/_security-includes.txt

The `shared responsibility model <https://aws.amazon.com/compliance/shared-responsibility-model>`_ applies to data protection
in this AWS product or service. As described in this model, AWS is responsible for protecting the global infrastructure that
runs all of the AWS Cloud. You are responsible for maintaining control over your content that is hosted on this infrastructure.
This content includes the security configuration and management tasks for the AWS services that you use. For more information
about data privacy, see the `Data Privacy FAQ <http://aws.amazon.com/compliance/data-privacy-faq>`_. For information about
data protection in Europe, see the
`AWS Shared Responsibility Model and GDPR <http://aws.amazon.com/blogs/security/the-aws-shared-responsibility-model-and-gdpr>`_
blog post on the AWS Security Blog.

For data protection purposes, we recommend that you protect AWS account credentials and set up individual user accounts with
AWS Identity and Access Management (IAM). That way each user is given only the permissions necessary to fulfill their job duties.
We also recommend that you secure your data in the following ways:

* Use multi-factor authentication (MFA) with each account.
* Use SSL/TLS to communicate with AWS resources. We recommend TLS 1.2 or later.
* Set up API and user activity logging with |CTlong|.
* Use |AWS| encryption solutions, with all default security controls within |AWS| services.
* Use advanced managed security services such as |MCElong|, which assists in discovering and securing personal data that
  is stored in |S3|.
* If you require FIPS 140-2 validated cryptographic modules when accessing AWS through a command line interface or an API, use a FIPS endpoint. For more information about the available FIPS endpoints, see `Federal Information Processing Standard (FIPS) 140-2 <http://aws.amazon.com/compliance/fips>`_.

We strongly recommend that you never put sensitive identifying information, such as your customers' account numbers, into
free-form fields such as a **Name** field. This includes when you work with |SERVICENAME| or other |AWS| services
using the console, API, |CLI|, or |AWS| SDKs. Any data that you enter into |SERVICENAME| or other services might get picked up
for inclusion in diagnostic logs. When you provide a URL to an external server, don't include credentials information in the URL
to validate your request to that server.
