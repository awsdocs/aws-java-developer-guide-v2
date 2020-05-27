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
   :description: How to use the AWS SDK for Java to work with |PINlong|.
   :keywords: AWS for Java SDK code examples, pinpoint, project, application, app, segment,
              dynamic, static, campaign, send, message


You can use |PINlong| to send relevant, personalized messages to your customers via multiple
communication channels, such as push notifications, SMS, and email.


.. _pinpoint-create-project:

Create a project
================

A project (or application) in |PIN| is a collection of settings, customer data, segments, and
campaigns.

To create a project, start by building a 
:aws-java-class:`CreateApplicationRequest <services/pinpoint/model/CreateApplicationRequest>`
object with the name of the project as the value of its :method:`name()`. Then build a
:aws-java-class:`CreateAppRequest <services/pinpoint/model/CreateAppRequest>`
object, passing in the :classname:`CreateApplicationRequest` object as the value of its
:methodname:`createApplicationRequest()`. Call the :methodname:`createApp()` method of your
:aws-java-class:`PinpointClient <services/pinpoint/PinpointClient>`, passing in the
:classname:`CreateAppRequest` object. You can capture the result of this request as a
:aws-java-class:`CreateAppResponse <services/pinpoint/model/CreateAppResponse>` object, as
demonstrated in the code snippet below.

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
last 30 days. You can do this by first building a
:aws-java-class:`RecencyDimension <services/pinpoint/model/RecencyDimension>` object with the
desired :methodname:`duration()` and :methodname:`recencyType()` (i.e. :code:`ACTIVE` or
:code:`INACTIVE`), and then passing this object to a
:aws-java-class:`SegmentBehaviors <services/pinpoint/model/SegmentBehaviors>` builder object as the
value of :methodname:`recency()`.

Once you have your segment attributes defined, build them into a
:aws-java-class:`SegmentDimensions <services/pinpoint/model/SegmentDimensions>` object. Then build
a :aws-java-class:`WriteSegmentRequest <services/pinpoint/model/WriteSegmentRequest>` object,
passing in the :classname:`SegmentDimensions` object as the value of its
:methodname:`dimensions()`. Next, build a
:aws-java-class:`CreateSegmentRequest <services/pinpoint/model/CreateSegmentRequest>` object,
passing in the :classname:`WriteSegmentRequest` object as the value of its
:methodname:`writeSegmentRequest()`. Finally, call the :methodname:`createSegment()` method of
your :classname:`PinpointClient`, passing in the :classname:`CreateSegmentRequest` object. Capture
the result of this request as a
:aws-java-class:`CreateSegmentResponse <services/pinpoint/model/CreateSegmentResponse>` object.

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

A static segment is one created and imported from outside of |PIN|. The example code below shows
how to create a static segment by importing it from |S3|.

Prerequisite
------------

Before you can complete this example, you need to create an |IAM| role that grants |PINlong|
access to |S3|. For more information, see
`IAM role for importing endpoints or segments <https://docs.aws.amazon.com/pinpoint/latest/developerguide/permissions-import-segment.html>`_
in the |PIN-dg|.

To import a static segment, start by building an 
:aws-java-class:`ImportJobRequest <services/pinpoint/model/ImportJobRequest>` object. In the
builder, specify the :methodname:`s3Url()`, :methodname:`roleArn()`, and :methodname:`format()`.

.. note:: For more information about the properties of an :classname:`ImportJobRequest`, see
          `the ImportJobRequest section of Import Jobs <https://docs.aws.amazon.com/pinpoint/latest/apireference/apps-application-id-jobs-import.html#apps-application-id-jobs-import-properties>`_
          in the |PIN-api|.

Then build a 
:aws-java-class:`CreateImportJobRequest <services/pinpoint/model/CreateImportJobRequest>` object,
passing in the :classname:`ImportJobRequest` object as the value of its
:methodname:`importJobRequest()` as well as the :code:`ID` of your project as the
:methodname:`applicationId()`. Call the :methodname:`createImportJob()` method of your
:classname:`PinpointClient`, passing in the :classname:`CreateImportJobRequest` object. You can
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

To list the segments associated with a particular project, start by building a
:aws-java-class:`GetSegmentsRequest <services/pinpoint/model/GetSegmentsRequest>` object with the
:code:`ID` of the project as the value of its :methodname:`applicationId()`. Next, call the
:methodname:`getSegments()` method of your :classname:`PinpointClient`, passing in the
:classname:`GetSegmentsRequest` object. Capture the result of this request as a
:aws-java-class:`GetSegmentsResponse <services/pinpoint/model/GetSegmentsResponse>` object.
Finally, instantiate a :javase-ref:`List <java/util/List>` object upcasted to the
:aws-java-class:`SegmentResponse <services/pinpoint/model/SegmentResponse>` class, then call
:classname:`GetSegmentsResponse`'s :methodname:`segmentsResponse().item()` as demonstrated in the
code snippet below. From there, you can iterate through the results.

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
code snippet, for example, the campaign will start start immediately because the
:aws-java-class:`Schedule <services/pinpoint/model/Schedule>`'s :methodname:`startTime()` is to
:code:`IMMEDIATE`. To set it to start at a specific time in the future instead, specify a time in
ISO 8601 format.

.. note:: For more information about the settings available for campaigns, see
          `the Schedule section of Campaigns <https://docs.aws.amazon.com/pinpoint/latest/apireference/apps-application-id-campaigns.html#apps-application-id-campaigns-model-schedule>`_
          in the |PIN-api|.

Once you have your campaign configuration defined, build them into a
:aws-java-class:`WriteCampaignRequest <services/pinpoint/model/WriteCampaignRequest>` object. None
of the methods of the :classname:`WriteCampaignRequest`'s :methodname:`builder()` are required;
however, you do need to include any of the configuration settings
(:aws-java-class:`MessageConfiguration <services/pinpoint/model/MessageConfiguration>`) that you
set for the campaign. It is also recommended to include a :methodname:`name` and a
:methodname:`description` for your campaign so you can easily distinguish it from other campaigns.
Call the :methodname:`createCampaign()` method of your :classname:`PinpointClient`, passing in the
:classname:`WriteCampaignRequest` object. Capture the result of this request as a
:aws-java-class:`CreateCampaignResponse <services/pinpoint/model/CreateCampaignResponse>` object.

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

To send an SMS text message through |PIN|, first build an
:aws-java-class:`AddressConfiguration <services/pinpoint/model/AddressConfiguration>` object to
specify the :methodname:`channelType()`. (In the example below, it is set to
:code:`ChannelType.SMS` to indicate the message will be sent via SMS.) Initialize a
:javase-ref:`HashMap <java/util/HashMap>` to store the destination phone number and the
:classname:`AddressConfiguration` object. Next, build an
:aws-java-class:`SMSMessage <services/pinpoint/model/SMSMessage>` object containing the relevant
values, including the :methodname:`originationNumber`, the type of message
(:methodname:`messageType`), and the :methodname:`body` of the message itself.

Once you have created the message, build the :classname:`SMSMessage` object into a
:aws-java-class:`DirectMessageConfiguration <services/pinpoint/model/DirectMessageConfiguration>`
object. Build your :javase-ref:`Map <java/util/Map>` object and
:classname:`DirectMessageConfiguration` object into a
:aws-java-class:`MessageRequest <services/pinpoint/model/MessageRequest>` object. Build a
:aws-java-class:`SendMessagesRequest <services/pinpoint/model/SendMessagesRequest>` object,
including your project ID (:methodname:`applicationId`) and your :classname:`MessageRequest`
object. Call the :methodname:`sendMessages()` method of your :classname:`PinpointClient`, passing
in the :classname:`SendMessagesRequest` object. Capture the result of this request as a
:aws-java-class:`SendMessagesResponse <services/pinpoint/model/SendMessagesResponse>` object.

**Imports**

.. literalinclude:: pinpoint.java2.sendmsg.import.txt
   :language: java

**Code**

.. literalinclude:: pinpoint.java2.sendmsg.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-pinpoint:`complete example <SendMessage.java>` on GitHub.


For more information, see the |PIN-dg|_.
