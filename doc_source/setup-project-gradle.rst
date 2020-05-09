.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#######################
Use the SDK with Gradle
#######################

.. meta::
   :description:  How to use Gradle to set up your AWS SDK for Java v2 project
   :keywords: AWS SDK for Java, v2, Gradle, BOM, install, download, setup


You can use Gradle_ to configure and build AWS SDK for Java projects.

To manage SDK dependencies for your Gradle project, import the Maven bill of materials (BOM) for
the |sdk-java| into the :file:`build.gradle` file.

.. note:: In the following examples, replace *2.X.X* in the :file:`build.gradle` file with a valid
          version of the AWS SDK for Java v2. Find the latest version in the
          :aws-java-class-root:`AWS SDK for Java 2.x Reference <>`.

.. topic:: To configure the |sdk-java| for Gradle version 5.0 or later
    
    #. Add the BOM to the `dependencies` section of the file.

       .. code-block:: groovy

          ...
          dependencies {
            implementation platform('software.amazon.awssdk:bom:2.X.X')

            // Declare individual SDK dependencies without version
            ...
          } 
          

    #. Specify the SDK modules to use in the `dependencies` section. For example, the following
       includes a dependency for |AKlong|.

       .. code-block:: groovy

          ...
          dependencies {
            ...
            implementation 'software.amazon.awssdk:kinesis'
            ...
          }
          

Gradle automatically resolves the correct version of your SDK dependencies by using the information
from the BOM.

The following is an example of a complete :file:`build.gradle` file that includes a dependency for
|AK|.

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
   

.. note:: In the previous example, replace the dependency for |AK| with the dependencies of the AWS
          services you will use in your project. The modules (dependencies) that are managed by the
          |sdk-java| BOM are listed on Maven central repository
          (https://mvnrepository.com/artifact/software.amazon.awssdk/bom/latest).

For more information about specifying SDK dependencies by using the BOM, see
:doc:`setup-project-maven`.
