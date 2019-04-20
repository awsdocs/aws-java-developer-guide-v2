.. Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

#####################
Working with |TSC|
#####################

.. meta::
   :description: How to set up bidirectional streaming using Amazon Transcribe.
   :keywords: AWS SDK for Java 2.x code examples, Transcribe streaming


The following example shows how bidirectional streaming works using |TSC|.
Bidirectional streaming implies that there's both a stream of data going to the service
and being received back in real time. The example uses |TSC| streaming transcription
to send an audio stream and receive a stream of transcribed text back in real time.

See :TSC-dg:`Streaming Transcription <streaming>` in the |TSC-dg| to learn more about
this feature.

See :TSC-dg:`Getting Started <getting-started>` in the |TSC-dg| to get started using
|TSC|.

Set up the Microphone
=====================

This code uses the javax.sound.sampled package to stream audio from an input device.

**Code**

.. literalinclude:: transcribe.java.bidir_streaming_microphone.complete.txt
   :language: java

See the :sdk-examples-java-transcribe:`complete example <Microphone.java>` on GitHub.

Create a Publisher
==================

This code implements a publisher that publishes audio data from the |TSC| audio stream.

**Code**

.. literalinclude:: transcribe.java.bidir_streaming_audiopublisher.complete.txt
   :language: java

See the :sdk-examples-java-transcribe:`complete example <AudioStreamPublisher.java>` on GitHub.


Create the Client and Start the Stream
======================================

In the main method, create a request object, start the audio input stream and instantiate
the publisher with the audio input.

You must also create a
:aws-java-class:`StartStreamTranscriptionResponseHandler <services/transcribestreaming/model/StartStreamTranscriptionResponseHandler>`
to specify how to handle the response from |TSC|.

Then, use the |tscstreamasyncclient|'s
:methodname:`startStreamTranscription` method to start the bidirectional streaming.

**Imports**

.. literalinclude:: transcribe.java.bidir_streaming.import.txt
   :language: java

**Code**

.. literalinclude:: transcribe.java.bidir_streaming.main.txt
   :dedent: 2
   :language: java

See the :sdk-examples-java-transcribe:`complete example <BidirectionalStreaming.java>` on GitHub.

More Info
=========

* :TSC-dg:`How It Works <how-it-works>` in the |TSC-dg|.
* :TSC-dg:`Getting Started With Streaming Audio <getting-started-streaming>` in the |TSC-dg|.
* :TSC-dg:`Guidelines and Limits <limits-guidelines>` in the |TSC-dg|.
