# Exception class name changes<a name="migration-exception-changes"></a>

This topic contains a mapping of exception class\-related name changes between versions 1\.x and 2\.x\.

This table maps the exception class name changes\.


| 1\.x | 2\.x | 
| --- | --- | 
|   `com.amazonaws.SdkBaseException` `com.amazonaws.AmazonClientException`   |   `software.amazon.awssdk.core.exception.SdkException`   | 
|   `com.amazonaws.SdkClientException`   |   `software.amazon.awssdk.core.exception.SdkClientException`   | 
|   `com.amazonaws.AmazonServiceException`   |   `software.amazon.awssdk.awscore.exception.AwsServiceException`   | 

The following table maps the methods on exception classes between version 1\.x and 2\.x\.


| 1\.x | 2\.x | 
| --- | --- | 
|   `AmazonServiceException.getRequestId`   |   `SdkServiceException.requestId`   | 
|   `AmazonServiceException.getServiceName`   |   `AwsServiceException.awsErrorDetails().serviceName`   | 
|   `AmazonServiceException.getErrorCode`   |   `AwsServiceException.awsErrorDetails().errorCode`   | 
|   `AmazonServiceException.getErrorMessage`   |   `AwsServiceException.awsErrorDetails().errorMessage`   | 
|   `AmazonServiceException.getStatusCode`   |   `AwsServiceException.awsErrorDetails().sdkHttpResponse().statusCode`   | 
|   `AmazonServiceException.getHttpHeaders`   |   `AwsServiceException.awsErrorDetails().sdkHttpResponse().headers`   | 
|   `AmazonServiceException.rawResponse`   |   `AwsServiceException.awsErrorDetails().rawResponse`   | 