.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

##################
|PINlong| examples
##################

.. meta::
   :description: How to use the AWS SDK for Java to work with Amazon Pinpoint.
   :keywords: AWS for Java SDK code examples, pinpoint, project, application, app, segment,
              dynamic, static, campaign, send, message


You can use |PINlong| to send relevant, personalized messages to your customers via multiple
communication channels, such as push notifications, SMS, and email.


.. _pinpoint-create-project:

Create a project
================

A project (or application) in |PIN| is a collection of settings, customer data, segments, and
campaigns.

To create a project, start by building a CreateApplicationRequest object with the name of
the project as the value of its .name(). Then build a CreateAppRequest object, passing in the
CreateApplicationRequest as the value of its .createApplicationRequest(). Call the createApp()
method of your PinpointClient, passing in the CreateAppRequest. You can capture the result of this
request as a CreateAppResponse object, as demonstrated in the code snippet below.

**Imports**

.. literalinclude:: pinpoint.java2.createapp.import.txt
   :language: java

**Code**

.. literalinclude:: pinpoint.java2.createapp.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-pinpoint:`complete example <CreateApp.java>` on GitHub.


.. _pinpoint-create-dynamic:

Create a dynamic segment
========================

A segment is a set of customers that share specific attributes, such as the city they live in or how
frequently they visit your website. A dynamic segment is one that is based on attributes that you
define, and can change over time.

To create a dynamic segment, first build all of the dimensions you want for this segment. For
example, the following code snippet is set to include customers who were active on the site in the
last 30 days. You can do this by first building a RecencyDimension object with the desired
.duration() and recencyType() (i.e. ACTIVE or INACTIVE), and then passing this object to a
SegmentBehaviors builder as the value of .recency().

Once you have your segment attributes defined, build them into a SegmentDimensions object. Then
build a WriteSegmentRequest, passing in the SegmentDimensions as the value of its .dimensions().
Next, build a CreateSegmentRequest, passing in the WriteSegmentRequest object as the value of its
.writeSegmentRequest(). Finally, call the createSegment() method of your PinpointClient, passing in
the CreateSegmentRequest. Capture the result of this request as a CreateSegmentResponse object.

**Imports**

.. literalinclude:: pinpoint.java2.createsegment.import.txt
   :language: java

**Code**

.. literalinclude:: pinpoint.java2.createsegment.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-pinpoint:`complete example <CreateSegment.java>` on GitHub.


.. _pinpoint-import-static:

Import a static segment
=======================

A static segment is one created and imported from outside of Pinpoint. The example code below shows
how to create a static segment by importing it from Amazon S3.

.. note:: Before you can complete this example, you need to create an IAM role that grants Amazon
          Pinpoint access to Amazon S3. For more information, see: https://docs.aws.amazon.com/pinpoint/latest/developerguide/permissions-import-segment.html

To import a static segment, start by building an ImportJobRequest object. In the builder, specify
the s3Url(), roleArn(), and format().
(For more information about the properties of an ImportJobRequest, see: https://docs.aws.amazon.com/pinpoint/latest/apireference/apps-application-id-jobs-import.html#apps-application-id-jobs-import-properties)

Then build a CreateImportJobRequest object, passing in the ImportJobRequest as the value of its
.importJobRequest() as well as the ID of your project as teh .applicationId(). Call the
createImportJob() method of your PinpointClient, passing in the CreateImportJobRequest. You can
capture the result of this request as a CreateImportJobResponse object, as demonstrated in the code
snippet below.

**Imports**

.. literalinclude:: pinpoint.java2.importsegment.import.txt
   :language: java

**Code**

.. literalinclude:: pinpoint.java2.importsegment.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-pinpoint:`complete example <ImportSegment.java>` on GitHub.


.. _pinpoint-list-segments:

List segments for your project
==============================

To list the segments associated with a particular project, start by building a GetSegmentsRequest
object with the ID of the project as the value of its .applicationId(). Next, call the getSegments()
method of your PinpointClient, passing in the GetSegmentsRequest. Capture the result of this request
as a GetSegmentsResponse object. Finally, instantiate a List object upcasted to the SegmentResponse
class, then call GetSegmentsResponseâ€™s .segmentsResponse().item() as demonstrated in the code
snippet below. From there, you can iterate through the results.

**Imports**

.. literalinclude:: pinpoint.java2.listsegments.import.txt
   :language: java

**Code**

.. literalinclude:: pinpoint.java2.listsegments.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-pinpoint:`complete example <ListSegments.java>` on GitHub.


.. _pinpoint-create-campaign:

Create a campaign
=================

A campaign is an initiative meant to engage a particular audience segment by sending messages to
those customers.

To create a campaign, first build all of the settings you want for this campaign. In the following
code snippet, for example, the campaign will start start immediately because the Scheduleâ€™s
.startTime() is to â€œIMMEDIATEâ€. (To set it to start at a specific time in the future instead,
specify a time in ISO 8601 format.) For more information about the settings available for campaigns,
see:
https://docs.aws.amazon.com/pinpoint/latest/apireference/apps-application-id-campaigns.html#apps-application-id-campaigns-model-schedule

Once you have your campaign configuration defined, build them into a WriteCampaignRequest object.
None of the methods of the WriteCampaignRequest.builder() are required; however, you do need to
include any of the configuration settings you set for the campaign. It is also recommended to
include a name and description for your campaign so you can easily distinguish it from other
campaigns. Call the createCampaign() method of your PinpointClient, passing in the
WriteCampaignRequest. Capture the result of this request as a CreateCampaignResponse object.

**Imports**

.. literalinclude:: pinpoint.java2.createcampaign.import.txt
   :language: java

**Code**

.. literalinclude:: pinpoint.java2.createcampaign.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-pinpoint:`complete example <CreateCampaign.java>` on GitHub.


.. _pinpoint-send-message:

Send a message
==============

To send an SMS text message through Pinpoint, first build an AddressConfiguration object to specify
the .channelType(). In this case, it is set to â€˜ChannelType.SMSâ€™ to indicate the message will be
sent via SMS. Initialize a HashMap to store the destination phone number and the
AddressConfiguration object. Next, build an SMSMessage object containing the relevant values,
including the .originationNumber(), the type of message, and the body of the message itself.

Once you have created the message, build the SMSMessage object into DirectMessageConfiguration
object. Build your map object and DirectMessageConfiguration object into a new MessageRequest
object. Build a SendMessagesRequest object, including your project ID and your MessageRequest
object. Call the sendMessages() method of your PinpointClient, passing in the SendMessagesRequest
object. Capture the result of this request as a SendMessagesResponse object.

**Imports**

.. literalinclude:: pinpoint.java2.sendmsg.import.txt
   :language: java

**Code**

.. literalinclude:: pinpoint.java2.sendmsg.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-pinpoint:`complete example <SendMessage.java>` on GitHub.
