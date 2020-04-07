.. Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#########################
Using the SDK with Gradle
#########################

To use the |sdk-java| in your Gradle_ project, import the SDK's Maven Bill of Materials (BOM) into the build file to manage SDK dependencies for your project.

Be sure to replace *2.X.X* in the build file examples below with a valid version of the AWS SDK for Java v2. Find the latest version in the 
:aws-java-class-root:`AWS SDK for Java 2.x Reference <>`.


.. topic:: To configure the SDK for Gradle 5.0 or later

    
    #. Add the BOM to the *dependencies* section of the file.

       .. code-block:: groovy

          dependencies {
            implementation platform('software.amazon.awssdk:bom:2.X.X')

            // Declare individual SDK dependencies without version
            ...
          } 


    #. Specify the SDK modules you want to use in the *dependencies* section. For example, the following includes a dependency for Amazon Kinesis.

       .. code-block:: groovy

          dependencies {
            implementation 'software.amazon.awssdk:kinesis'
            testCompile group: 'junit', name: 'junit', version: '4.11'
          }

Gradle automatically resolves the correct version of your SDK dependencies using the information
from the BOM.

Here's the complete :file:`build.gradle` file for this example:

.. code-block:: groovy

   group 'aws.test'
   version '1.0'

   apply plugin: 'java'

   sourceCompatibility = 1.8

   repositories {
     mavenCentral()
   }

   dependencies {
     implementation platform('software.amazon.awssdk:bom:2.X.X')
     implementation 'software.amazon.awssdk:kinesis'
     testImplementation group: 'junit', name: 'junit', version: '4.11'

   }

.. note:: Replace the dependency for Kinesis above with the dependency or dependencies of the service SDK(s) you will be using in your project. The modules (dependencies) that are managed by the |sdk-java| BOM are listed on Maven Central (https://mvnrepository.com/artifact/software.amazon.awssdk/bom).

For more detail about specifying SDK dependencies using the BOM, see
:doc:`setup-project-maven`.
