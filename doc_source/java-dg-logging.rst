.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

########################
Logging |sdk-java| Calls
########################

.. meta::
   :description: How to use Slf4j with the AWS SDK for Java.
   :keywords:

The |sdk-java| is instrumented with `Slf4j
<https://www.slf4j.org/manual.html>`_, which is an abstraction layer that
enables the use of any one of several logging systems at runtime.

Supported logging systems include the Java Logging Framework and Apache Log4j, among others. This
topic shows you how to use Log4j. You can use the SDK's logging functionality without
making any changes to your application code.

To learn more about `Log4j <http://logging.apache.org/log4j/1.2/>`_,
see the `Apache website <http://www.apache.org/>`_.

Download the Log4J JAR
======================

To use Log4j with the SDK, you need to download the Log4j JAR from the Apache website or use Maven. The SDK doesn't
include the JAR.

Log4j uses a configuration file, log4j.properties. Example configuration files are shown below. Copy
this configuration file to a directory on your classpath. The Log4j JAR and the log4j.properties
file don't have to be in the same directory.

The log4j.properties configuration file specifies properties such as logging level, where logging output is
sent (for example, to a file or to the console), and the format of the output. See the Log4j `manual <https://logging.apache.org/log4j/1.2/manual.html>`_ for more information on configuration. 

The logging level is the granularity of output that the logger generates. Log4j supports the concept of multiple logging
:emphasis:`hierarchies`. The logging level is set independently for each hierarchy. The following
two logging hierarchies are available in the |sdk-java|:

*   log4j.logger.software.amazon.awssdk

*   log4j.logger.org.apache.http.wire

.. _sdk-net-logging-classpath:

Setting the Classpath
=====================

Both the Log4j JAR and the log4j.properties file must be located on your classpath. To configure the log4j binding for Sl4j in Maven you can add the following to your pom.xml:

.. code-block:: xml

    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
        <version>1.7.25</version>
    </dependency>

If you're using the Eclipse IDE, you can set the classpath by opening the menu and navigating to
:guilabel:`Project` | :guilabel:`Properties` | :guilabel:`Java Build Path`.


.. _sdk-net-logging-service:

Service-Specific Errors and Warnings
====================================

We recommend that you always leave the "software.amazon.awssdk" logger hierarchy set to "WARN" to
catch any important messages from the client libraries. For example, if the |S3| client detects
that your application hasn't properly closed an :code:`InputStream` and could be leaking resources,
the S3 client reports it through a warning message to the logs. This also ensures that messages
are logged if the client has any problems handling requests or responses.

The following log4j.properties file sets the :code:`rootLogger` to WARN, which causes warning
and error messages from all loggers in the "software.amazon.awssdk" hierarchy to be included. Alternatively,
you can explicitly set the software.amazon.awssdk logger to WARN.

.. code-block:: properties

    log4j.rootLogger=WARN, A1
    log4j.appender.A1=org.apache.log4j.ConsoleAppender
    log4j.appender.A1.layout=org.apache.log4j.PatternLayout
    log4j.appender.A1.layout.ConversionPattern=%d [%t] %-5p %c -  %m%n
    # Or you can explicitly enable WARN and ERROR messages for the AWS Java clients
    log4j.logger.software.amazon.awssdk=WARN


.. _sdk-net-logging-request-response:

Request/Response Summary Logging
================================

Every request to an AWS service generates a unique AWS request ID that is useful if you run into an
issue with how an AWS service is handling a request. AWS request IDs are accessible programmatically
through Exception objects in the SDK for any failed service call, and can also be reported through
the DEBUG log level in the "software.amazon.awssdk.request" logger.

The following log4j.properties file enables a summary of requests and responses, including AWS
request IDs.

.. code-block:: properties

    log4j.rootLogger=WARN, A1
    log4j.appender.A1=org.apache.log4j.ConsoleAppender
    log4j.appender.A1.layout=org.apache.log4j.PatternLayout
    log4j.appender.A1.layout.ConversionPattern=%d [%t] %-5p %c -  %m%n
    # Turn on DEBUG logging in software.amazon.awssdk.request to log
    # a summary of requests/responses with AWS request IDs
    log4j.logger.software.amazon.awssdk.request=DEBUG

Here is an example of the log output.

.. code-block:: none

    2009-12-17 09:53:04,269 [main] DEBUG software.amazon.awssdk.request - Sending
    Request: POST https://rds.amazonaws.com / Parameters: (MaxRecords: 20,
    Action: DescribeEngineDefaultParameters, SignatureMethod: HmacSHA256,
    AWSAccessKeyId: ACCESSKEYID, Version: 2009-10-16, SignatureVersion: 2,
    Engine: mysql5.1, Timestamp: 2009-12-17T17:53:04.267Z, Signature:
    q963XH63Lcovl5Rr71APlzlye99rmWwT9DfuQaNznkD, ) 2009-12-17 09:53:04,464
    [main] DEBUG software.amazon.awssdk.request - Received successful response: 200, AWS
    Request ID: 694d1242-cee0-c85e-f31f-5dab1ea18bc6 2009-12-17 09:53:04,469
    [main] DEBUG software.amazon.awssdk.request - Sending Request: POST
    https://rds.amazonaws.com / Parameters: (ResetAllParameters: true, Action:
    ResetDBParameterGroup, SignatureMethod: HmacSHA256, DBParameterGroupName:
    java-integ-test-param-group-0000000000000, AWSAccessKeyId: ACCESSKEYID,
    Version: 2009-10-16, SignatureVersion: 2, Timestamp:
    2009-12-17T17:53:04.467Z, Signature:
    9WcgfPwTobvLVcpyhbrdN7P7l3uH0oviYQ4yZ+TQjsQ=, )

    2009-12-17 09:53:04,646 [main] DEBUG software.amazon.awssdk.request - Received
    successful response: 200, AWS Request ID:
    694d1242-cee0-c85e-f31f-5dab1ea18bc6


.. _sdk-net-logging-verbose:

Verbose Wire Logging
====================

In some cases, it can be useful to see the exact requests and responses that the |sdk-java|
sends and receives. If you really need access to this information, you can temporarily enable it through
the Apache HttpClient 4 logger. Enabling the DEBUG level on the :code:`apache.http.wire` logger
enables logging for all request and response data.

.. warning:: We recommend you only use wire logging for debugging purposes.
   Disable it in your production environments because it can log sensitive data.
   It logs the full request or response without encryption, even for an HTTPS call.
   For large requests (e.g., to upload a file to |S3|) or responses,
   verbose wire logging can also significantly impact your application's performance.

The following log4j.properties file turns on full wire logging in Apache HttpClient 4.

.. code-block:: properties

    log4j.rootLogger=WARN, A1
    log4j.appender.A1=org.apache.log4j.ConsoleAppender
    log4j.appender.A1.layout=org.apache.log4j.PatternLayout
    log4j.appender.A1.layout.ConversionPattern=%d [%t] %-5p %c -  %m%n
    # Log all HTTP content (headers, parameters, content, etc)  for
    # all requests and responses. Use caution with this since it can
    # be very expensive to log such verbose data!
    log4j.logger.org.apache.http.wire=DEBUG
