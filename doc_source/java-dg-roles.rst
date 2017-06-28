.. Copyright 2010-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

###########################################################
Using |IAM| Roles to Grant Access to AWS Resources on |EC2|
###########################################################

All requests to AWS services must be cryptographically signed using credentials issued
by AWS. You can use :emphasis:`IAM roles` to conveniently grant secure access to AWS resources from
your |EC2| instances.

This topic provides information about how to use |IAM| roles with |sdk-java| applications running
on |EC2|. For more information about |IAM| instances, see :ec2-ug:`IAM Roles for Amazon EC2
<iam-roles-for-amazon-ec2>` in the |EC2-ug|.


.. _default-provider-chain:

Default Provider Chain and |EC2| Instance Profiles
==================================================

If your application creates an AWS client using the default constructor, the client searches
for credentials using the :emphasis:`default credentials provider chain`, in the following order:

1. In the Java system properties: :code:`aws.accessKeyId` and :code:`aws.secretKey`.

2. In system environment variables: :code:`AWS_ACCESS_KEY_ID` and :code:`AWS_SECRET_ACCESS_KEY`.

3. In the default credentials file (the location of this file varies by platform).

4. In the |ECS| environment variable: :code:`AWS_CONTAINER_CREDENTIALS_RELATIVE_URI`.

5. In the :emphasis:`instance profile credentials`, which exist within the instance metadata
   associated with the IAM role for the EC2 instance.

The final step in the default provider chain is available only when running your application on an
|EC2| instance. However, it provides the greatest ease of use and best security when working with |EC2|
instances. You can also pass an :aws-java-class:`InstanceProfileCredentialsProvider
<auth/InstanceProfileCredentialsProvider>` instance directly to the client constructor to get
instance profile credentials without proceeding through the entire default provider chain.

For example:

.. code-block:: java

   S3Client s3 = S3Client.builder()
                 .credentialsProvider(InstanceProfileCredentialsProvider.builder().build())
                 .build();

When you use this approach, the SDK retrieves temporary AWS credentials that have the same
permissions as those associated with the |IAM| role that is associated with the |EC2| instance in its
instance profile. Although these credentials are temporary and would eventually expire,
:classname:`InstanceProfileCredentialsProvider` periodically refreshes them for you so that the
obtained credentials continue to allow access to AWS.

.. _roles-walkthrough:

Walkthrough: Using IAM roles for |EC2| Instances
================================================

This walkthrough shows you how to retrieve an object from |S3| using an |IAM| role to
manage access.


.. _java-dg-create-the-role:

Create an |IAM| Role
--------------------

Create an IAM role that grants read-only access to |S3|.

.. topic:: To create the IAM role

    #. Open the :console:`IAM console <iam>`.

    #. In the navigation pane, choose :guilabel:`Roles`, then :guilabel:`Create New Role`.

    #. On the :guilabel:`Select Role Type` page, under :guilabel:`AWS Service Roles`, choose
       :guilabel:`Amazon EC2`.

    #. On the :guilabel:`Attach Policy` page, choose
       :guilabel:`Amazon S3 Read Only Access` from the policy list, then choose :guilabel:`Next Step`.

    #. Enter a name for the role, then select :guilabel:`Next Step`. Remember this name
          because you'll need it when you launch your |EC2| instance.

    #. On the :guilabel:`Review` page, choose :guilabel:`Create Role`.



.. _java-dg-launch-ec2-instance-with-instance-profile:

Launch an EC2 Instance and Specify Your IAM Role
------------------------------------------------

You can launch an |EC2| instance with an |IAM| role using the |EC2| console.

To launch an |EC2| instance using the console, follow the directions in :ec2-ug:`Getting Started
with Amazon EC2 Linux Instances <EC2_GetStarted>` in the |EC2-ug|.

When you reach the :guilabel:`Review Instance Launch` page, select :guilabel:`Edit instance
details`. In :guilabel:`IAM role`, choose the |IAM| role that you created previously. Complete the
procedure as directed.

.. note:: You need to create or use an existing security group and key pair to connect to the
   instance.

With this |IAM| and |EC2| setup, you can deploy your application to the EC2 instance and it will have read access
to the Amazon S3 service.
