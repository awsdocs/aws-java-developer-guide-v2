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

The |sdk-java| is instrumented with `Slf4j <https://www.slf4j.org/manual.html>`_,
which is an abstraction layer that
enables the use of any one of several logging systems at runtime.

Supported logging systems include the Java Logging Framework and Apache Log4j,
among others. This topic shows you how to use Log4j. You can use the SDK's
logging functionality without making any changes to your application code.

To learn more about `Log4j <http://logging.apache.org/log4j/2.x/>`_,
see the `Apache website <http://www.apache.org/>`_.

Add the Log4J JAR
=================

To use Log4j with the SDK, you need to download the Log4j JAR from the
`Apache website <https://logging.apache.org/log4j/2.0/download.html/>`_ or
use Maven by adding a dependency on Log4j in your pom.xml file.
The SDK doesn't include the JAR.

.. _log4j-configuration-file:

Log4j Configuration file
========================
Log4j uses a configuration file, log4j2.xml. Example configuration files are shown below.
To learn more about the values used in the configuration file, see the
`manual for Log4j configuration <https://logging.apache.org/log4j/2.x/manual/configuration.html/>`_.

Place your configuration file in a directory on your classpath. The Log4j JAR
and the log4j2.xml file do not have to be in the same directory.

The log4j2.xml configuration file specifies properties such as `logging level
<http://logging.apache.org/log4j/2.x/manual/configuration.html#Loggers>`_, where logging output is
sent (for example, `to a file or to the console
<http://logging.apache.org/log4j/2.x/manual/appenders.html>`_), and the `format of the output
<http://logging.apache.org/log4j/2.x/manual/layouts.html>`_. The logging level is the granularity of
output that the logger generates. Log4j supports the concept of multiple logging
:emphasis:`hierarchies`. The logging level is set independently for each hierarchy. The following
two logging hierarchies are available in the |sdk-java|:

*   software.amazon.awssdk

*   org.apache.http.wire

.. _sdk-net-logging-classpath:

Setting the Classpath
=====================

Both the Log4j JAR and the log4j2.xml file must be located on your classpath.
To configure the log4j binding for Sl4j in Maven you can add the following to your pom.xml:

.. code-block:: xml
   <dependency>
     <groupId>org.apache.logging.log4j</groupId>
     <artifactId>log4j-core</artifactId>
   </dependency>
   <dependency>
     <groupId>org.apache.logging.log4j</groupId>
     <artifactId>log4j-api</artifactId>
   </dependency>
   <dependency>
     <groupId>org.apache.logging.log4j</groupId>
     <artifactId>log4j-slf4j-impl</artifactId>
   </dependency>

If you're using the Eclipse IDE, you can set the classpath by opening the menu and navigating to
:guilabel:`Project` | :guilabel:`Properties` | :guilabel:`Java Build Path`.

.. _sdk-net-logging-service:

Service-Specific Errors and Warnings
====================================

We recommend that you always leave the "software.amazon.awssdk" logger hierarchy
set to "WARN" to catch any important messages from the client libraries. For
example, if the |S3| client detects that your application hasn't properly closed
an :code:`InputStream` and could be leaking resources, the S3 client reports it
through a warning message to the logs. This also ensures that messages
are logged if the client has any problems handling requests or responses.

The following log4j2.xml file sets the :code:`rootLogger` to WARN, which causes warning
and error messages from all loggers in the "software.amazon.awssdk" hierarchy to be included. Alternatively,
you can explicitly set the software.amazon.awssdk logger to WARN.

.. code-block:: xml

   <Configuration status="WARN">
     <Appenders>
       <Console name="ConsoleAppender" target="SYSTEM_OUT">
         <PatternLayout pattern="%d{YYYY-MM-dd HH:mm:ss} [%t] %-5p %c:%L - %m%n" />
       </Console>
     </Appenders>

     <Loggers>
       <Root level="WARN">
         <AppenderRef ref="ConsoleAppender"/>
       </Root>
       <Logger name="software.amazon.awssdk" level="WARN" />
     </Loggers>
   </Configuration>


.. _sdk-net-logging-request-response:

Request/Response Summary Logging
================================

Every request to an AWS service generates a unique AWS request ID that is useful if you run into an
issue with how an AWS service is handling a request. AWS request IDs are accessible programmatically
through Exception objects in the SDK for any failed service call, and can also be reported through
the DEBUG log level in the "software.amazon.awssdk.request" logger.

The following log4j2.xml file enables a summary of requests and responses.

.. code-block:: xml

   <Configuration status="WARN">
     <Appenders>
       <Console name="ConsoleAppender" target="SYSTEM_OUT">
         <PatternLayout pattern="%d{YYYY-MM-dd HH:mm:ss} [%t] %-5p %c:%L - %m%n" />
       </Console>
     </Appenders>

     <Loggers>
       <Root level="WARN">
         <AppenderRef ref="ConsoleAppender"/>
       </Root>
       <Logger name="software.amazon.awssdk" level="WARN" />
       <Logger name="software.amazon.awssdk.request" level="DEBUG" />
     </Loggers>
   </Configuration>

Here is an example of the log output:

.. code-block:: none

   2018-01-28 19:31:56 [main] DEBUG software.amazon.awssdk.request:Logger.java:78 - Sending Request: software.amazon.awssdk.http.DefaultSdkHttpFullRequest@3a80515c


.. _sdk-net-logging-verbose:

Verbose Wire Logging
====================

In some cases, it can be useful to see the exact requests and responses that the |sdk-java|
sends and receives. If you really need access to this information, you can temporarily enable it through
the Apache HttpClient logger. Enabling the DEBUG level on the :code:`apache.http.wire` logger
enables logging for all request and response data.

.. warning:: We recommend you only use wire logging for debugging purposes.
   Disable it in your production environments because it can log sensitive data.
   It logs the full request or response without encryption, even for an HTTPS call.
   For large requests (e.g., to upload a file to |S3|) or responses,
   verbose wire logging can also significantly impact your application's performance.

The following log4j2.xml file turns on full wire logging in Apache HttpClient.

.. code-block:: xml

   <Configuration status="WARN">
     <Appenders>
       <Console name="ConsoleAppender" target="SYSTEM_OUT">
         <PatternLayout pattern="%d{YYYY-MM-dd HH:mm:ss} [%t] %-5p %c:%L - %m%n" />
       </Console>
     </Appenders>

     <Loggers>
       <Root level="WARN">
        <AppenderRef ref="ConsoleAppender"/>
       </Root>
       <Logger name="software.amazon.awssdk" level="WARN" />
       <Logger name="software.amazon.awssdk.request" level="DEBUG" />
       <Logger name="org.apache.http.wire" level="DEBUG" />
     </Loggers>
   </Configuration>

Additional Maven dependency on log4j-1.2-api is required for wire-logging with Apache as
it uses 1.2 under  the hood. Add the following to the pom.xml file if you enable wire logging.

.. code-block:: xml

   <dependency>
     <groupId>org.apache.logging.log4j</groupId>
     <artifactId>log4j-1.2-api</artifactId>
   </dependency>
