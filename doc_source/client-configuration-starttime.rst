.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#############################################
Optimize cold start performance for |LAMlong|
#############################################

.. meta::
   :description: How to minimize SDK startup time when using AWS SDK for Java with AWS Lambda.
   :keywords: AWS for Java SDK, lambda, startup, coldstart, functions, HTTP, client, performance

Among the improvements in the |sdk-java-v2| is the SDK cold startup time for Java functions in
|lam|. This is the time it takes for a Java |lam| function to start up and respond to its
first request.

Version 2.x includes three primary changes that contribute to this improvement:

* Use of `jackson-jr <https://github.com/FasterXML/jackson-jr>`_, which is a serialization library
  that improves initialization time.

* Use of the :javase-ref:`java.time <java/time>` libraries for date and time objects.

* Use of `Slf4j <https://www.slf4j.org/>`_ for a logging facade.

You can gain additional SDK startup time improvement by setting specific configuration values on
the client builder. They each save some time at startup by reducing the amount of information
your application needs to find for initialization.

In your client builder, specify a region, use Environment Variable credentials provider, and
specify UrlConnectionClient as the httpClient. See the code snippet below for an example.

* `The region lookup process <https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/java-dg-region-selection.html#default-region-provider-chain>`_
  for the SDK takes time. By specifying a region, you can save up to 80ms of initialization time.

  .. note:: By specifying an |AWS| region, the code will not run in other regions without modification.

* The process the SDK uses to look for credentials can take up to 90ms. By using the
  :aws-java-class:`EnvironmentVariableCredentialsProvider <auth/credentials/EnvironmentVariableCredentialsProvider>`

  .. note:: Using this credentials provider enables the code to be used in |LAM| functions, but may
            not work on |EC2| or other systems.
	
* Instantiation time for JDK's :javase-ref:`URLConnection <java/net/URLConnection>` library is much
  lower than Apache HTTP Client or Netty. You can save up to 1 second by using this HTTP client.



Example client configuration
============================

.. code-block:: Java

   S3Client client = S3Client.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .httpClient(UrlConnectionHttpClient.builder().build())
                .build();
