.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#######################################################
SDK Startup Time Performance Improvement Configuration
#######################################################

.. meta::
   :description: How to minimize SDK Startup Time using the AWS SDK for Java.

Among the improvements in the |sdk-java-v2| is the SDK startup time for Java functions in
|lam|. This is the time it takes for a Java |lam| function to start up and respond to its
first request.

Version 2.x includes three changes that contribute to this improvement:

* Use of `jackson-jr <https://github.com/FasterXML/jackson-jr>`_, which is
  a serialization library that improves initialization time.

* Use of the java.time.libraries for date and time objects.

* Switch to `Slf4j <https://www.slf4j.org/>`_ for a logging facade.

You can gain additional SDK startup time improvement by setting specific configuration values on
the client builder. They each save some time at startup by reducing the amount of information
your application needs to find for initialization.

.. note::
  By specifying these values, you are losing some portability of your code.
  For example, by specifying an |AWS| Region, the code will not run in other Regions without modification.

Example: Minimal SDK Startup Time Client Configuration
======================================================

.. code-block:: Java

 Region region = Region.US_WEST_2;
 3Client S3 = S3Client.builder()
                .region(region)
                .build();
