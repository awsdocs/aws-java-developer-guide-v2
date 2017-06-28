/*
   Copyright 2010-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This file is licensed under the Apache License, Version 2.0 (the "License").
   You may not use this file except in compliance with the License. A copy of
   the License is located at

    http://aws.amazon.com/apache2.0/

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
   CONDITIONS OF ANY KIND, either express or implied. See the License for the
   specific language governing permissions and limitations under the License.
*/

import java.io.*;

import software.amazon.awssdk.auth.*;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.AmazonClientException;
import software.amazon.awssdk.AmazonServiceException;

public class GetS3Object {
  private static String bucketName = "text-content";
  private static String key = "text-object.txt";

  public static void main(String[] args) throws IOException
  {
    S3Client s3Client = S3Client.create();

    try {
      System.out.println("Downloading an object");
      GetObjectResponse responseObj = s3Client.getObject(
          GetObjectRequest.builder()
                          .bucket(bucketName)
                          .key(key)
                          .build());
      displayTextInputStream(responseObj);
    }
    catch(AmazonServiceException ase) {
      System.err.println("Exception was thrown by the service");
    }
    catch(AmazonClientException ace) {
      System.err.println("Exception was thrown by the client");
    }
  }

  private static void displayTextInputStream(GetObjectResponse input) throws IOException
  {
    ByteBuffer body = intput.body();
    while(body.hasRemaining())
    {
      System.out.println((char)input.get());
    }
    System.out.println();
  }
}
