.. Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

########################################
Supplying and Retrieving AWS Credentials
########################################

.. meta::
   :description: How to load credentials for AWS using the AWS SDK for Java.
   :keywords:

To make requests to |AWSlong| (AWS), you must supply AWS credentials to the |sdk-java|.
You can do this by using the following methods:

* Use the default credential provider chain :emphasis:`(recommended)`.

* Use a specific credential provider or provider chain.

* Supply credentials explicitly.

Each of these methods is discussed in the following sections.

.. _credentials-default:

Use the Default Credential Provider Chain
=========================================

When you initialize a new service client without supplying any arguments, the |sdk-java|
attempts to find AWS credentials. It uses the :emphasis:`default credential provider chain` implemented
by the :aws-java-class:`DefaultCredentialsProvider <auth/credentials/DefaultCredentialsProvider>`
class. 

The following example creates a new service client that uses the default credential provider chain:

.. code-block:: java

   S3Client s3 = S3Client.builder()
                         .region(Region.US_WEST_2)
                         .build();

Credential Retrieval Order
--------------------------

When the default credential provider chain attempts to retrieve credentials. For example, the following Java code shows how to create a **AmazonDynamoDB** object using Environment variables.

.. code-block:: java

   AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.DEFAULT_REGION)
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build();

THe following list shows the order:

#. **Java system properties** |ndash| :code:`aws.accessKeyId` and :code:`aws.secretAccessKey`.
   The |sdk-java| uses the :aws-java-class:`SystemPropertyCredentialsProvider <auth/credentials/SystemPropertyCredentialsProvider>`
   to load these credentials.

#. **Environment variables** |ndash| :envvar:`AWS_ACCESS_KEY_ID` and :envvar:`AWS_SECRET_ACCESS_KEY`.
   The |sdk-java| uses the :aws-java-class:`EnvironmentVariableCredentialsProvider <auth/credentials/EnvironmentVariableCredentialsProvider>`
   class to load these credentials.  
   
#. **The default credential profiles file** |ndash| The specific location of this file can vary per platform, but is 
   typically located at :file:`~/.aws/credentials`. 
   This file is shared by many of the AWS SDKs and by the AWS CLI. The
   |sdk-java| uses the :aws-java-class:`ProfileCredentialsProvider <auth/credentials/ProfileCredentialsProvider>` to load these credentials.

   You can create a credentials file by using the :code:`aws configure` command provided by the AWS
   CLI. You can also create it by editing the file with a text editor. For information about the
   credentials file format, see :ref:`credentials-file-format`.

#. **Amazon ECS container credentials** |ndash| This is loaded from |ECS| if the environment
   variable :envvar:`AWS_CONTAINER_CREDENTIALS_RELATIVE_URI` is set. The |sdk-java| uses the
   :aws-java-class:`ContainerCredentialsProvider <auth/credentials/ContainerCredentialsProvider>` to load these
   credentials.

#. **Instance profile credentials** |ndash| This is used on |EC2| instances, and delivered through the |EC2|
   metadata service. The |sdk-java| uses the :aws-java-class:`InstanceProfileCredentialsProvider
   <auth/credentials/InstanceProfileCredentialsProvider>` to load these credentials.

Setting Credentials
-------------------

To use AWS credentials, supply them in at least one of the
preceding locations. For information about setting credentials, see the following topics:

* To supply credentials in the :emphasis:`environment` or in the default
  :emphasis:`credential profiles file`, see :doc:`setup-credentials`.

* To set Java :emphasis:`system properties`, see the
  `System Properties <http://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html>`_
  tutorial on the official :title:`Java Tutorials` website.

* To set up and use :emphasis:`instance profile credentials` with
  your EC2 instances, see :doc:`java-dg-roles`.

Setting an Alternate Credentials Profile
----------------------------------------
The |sdk-java| uses the default profile, but there are ways to customize
which profile is sourced from the credentials file.

You can use the :code:`AWS_PROFILE` environment variable to change the profile loaded by the SDK.

For example, in |unixes|, you run the following command to change the profile to `myProfile`.

.. code-block:: sh

    export AWS_PROFILE="myProfile"

In Windows, run the following command.

.. code-block:: bat

    set AWS_PROFILE="myProfile"

Setting the :code:`AWS_PROFILE` environment variable affects credential loading for all officially
supported AWS SDKs and tools, for example the AWS CLI and the AWS Tools for PowerShell.
To change only the profile for a Java application, use the system property `aws.profile` instead.

Setting an Alternate Credentials File Location
----------------------------------------------

The |sdk-java| loads AWS credentials automatically from the default credentials file
location. However, you can also specify the location by setting the |aws-credfile-var| environment
variable with the full path to the credentials file.

You can use this feature to temporarily change the location where the |sdk-java| looks for
your credentials file. For example, set this variable with the command line. You
can also set the environment variable in your user or system environment to change it for the user specifically 
or across the system.

.. include:: common/procedure-override-shared-credfile-location.txt

.. _credentials-file-format:

AWS Credentials File Format
---------------------------

When you use the :code:`aws configure` command to create an AWS credentials file, the command creates
a file with the following format.

.. code-block:: ini

    [default]
    aws_access_key_id={YOUR_ACCESS_KEY_ID}
    aws_secret_access_key={YOUR_SECRET_ACCESS_KEY}

    [profile2]
    aws_access_key_id={YOUR_ACCESS_KEY_ID}
    aws_secret_access_key={YOUR_SECRET_ACCESS_KEY}

The profile name is specified in square brackets (for example, :code:`[default]`), followed by the
configurable fields in that profile as key-value pairs. You can have multiple profiles in your
credentials file. You can add or edit them using :samp:`aws configure --profile {PROFILE_NAME}`
to select the profile to configure. In addition to the access key and secret access keys, you can
specify a session token using the :code:`aws_session_token` field.

.. _credentials-specify-provider:

Use a Specific Credential Provider or Provider Chain
====================================================

You can use a credential provider that is different from the :emphasis:`default` credential
provider chain by using the client builder.

You provide an instance of a credentials provider or provider chain to a client builder that
takes an :aws-java-class:`AwsCredentialsProvider <auth/credentials/AwsCredentialsProvider>` interface as input.

The following example creates a new service client that uses the :emphasis:`environment` credentials provided, 
called `EnvironmentVariableCredentialsProvider`:

.. code-block:: java

   S3Client s3 = S3Client.builder()
                         .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                         .build();

For the full list of |sdk-java|-supplied credential providers and provider chains,
see **All Known Implementing Classes** in :aws-java-class:`AwsCredentialsProvider <auth/credentials/AwsCredentialsProvider>`.

.. tip:: You supply credential providers or provider chains that you
   create by using your own credential provider that implements the
   :code-java:`AwsCredentialsProvider` interface.

.. _credentials-explicit:

Supply Credentials Explicitly
=============================

If the default credential chain or a specific or custom provider or provider chain doesn't work for
your code, you can supply the credentials that you want. These can be AWS account credentials, |IAM| credentials, or 
temporary credentials retrieved from |STSlong| (|STS|). If you've retrieved temporary
credentials using |STS|, use this method to specify the credentials for AWS access.

.. important:: For security, we :emphasis:`strongly recommend` that you
   :emphasis:`use IAM account credentials` instead of the AWS account credentials
   for AWS access. For more information, see
   :aws-gr:`AWS Security Credentials <aws-security-credentials>` in the |AWS-gr|.

.. topic:: To explicitly supply credentials to an AWS client

   #. Instantiate a class that provides the :aws-java-class:`AwsCredentials <auth/credentials/AwsCredentials>`
      interface, such as :aws-java-class:`AwsSessionCredentials <auth/credentials/AwsSessionCredentials>`. Supply
      it with the AWS access key and secret key to use for the connection.

   #. Create an :aws-java-class:`StaticCredentialsProvider <auth/credentials/StaticCredentialsProvider>` with
      the :code-java:`AwsCredentials` object.

   #. Configure the client builder with the :code-java:`StaticCredentialsProvider` and build the client.

The following example creates a new service client that uses credentials that you supplied:

.. code-block:: java

   AwsSessionCredentials awsCreds = AwsSessionCredentials.create(
       "your_access_key_id_here",
       "your_secret_key_id_here",
       "your_session_token_here");

   S3Client s32 = S3Client.builder()
                          .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                          .build();
