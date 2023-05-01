# Service client metrics<a name="metrics-list"></a>

With the AWS SDK for Java 2\.x, you can collect metrics from the service clients in your application and then publish \(output\) those metrics to [Amazon CloudWatch](https://docs.aws.amazon.com/cloudwatch/index.html)\.

These tables list the metrics that you can collect and any HTTP client usage requirement\.

For more information about enabling and configuring metrics for the SDK, see [Enabling SDK metrics](metrics.md)\.

The terms used in the tables mean:
+ Apache: the Apache\-based HTTP client \(`[ApacheHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/apache/ApacheHttpClient.html)`\)
+ Netty: the Netty\-based HTTP client \(`[NettyNioAsyncHttpClient](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/http/nio/netty/NettyNioAsyncHttpClient.html)`\)
+ CRT: the AWS CRT\-based HTTP client \(`[AwsCrtAsyncHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/crt/AwsCrtAsyncHttpClient.html)`\)
+ Any: the collection of metric data does not depend on the HTTP client; this includes use of the URLConnection\-based HTTP client \(`[UrlConnectionHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/urlconnection/UrlConnectionHttpClient.html)`\)

## Metrics collected with each request<a name="metrics-perrequest"></a>


**​**  

| Metric name | Description | Type | HTTP client required | 
| --- | --- | --- | --- | 
|  ApiCallDuration  |  The total time taken to finish a request \(inclusive of all retries\)  |  Duration  |  Any  | 
|  ApiCallSuccessful  |  True if the API call was successful; false if not  |  Boolean  |  Any  | 
|  CredentialsFetchDuration  |  The time taken to fetch AWS signing credentials for the request  |  Duration  |  Any  | 
|  MarshallingDuration  |  The time taken to marshall the request  |  Duration  |  Any  | 
|  OperationName  |  The name of the AWS API the request is made to  |  String  |  Any  | 
|  RetryCount  |  Number of times the SDK retried the API call  |  Integer  |  Any  | 
|  ServiceId  |  Service ID of the AWS service that the API request is made against  |  String  |  Any  | 
|  TokenFetchDuration  | The time taken to fetch token signing credentials for the request | Duration | Any | 

## Metrics collected for each request attempt<a name="metrics-perattempt"></a>

Each API call that your application makes may take multiple attempts before responded with a success or failure\. These metrics are collected for each attempt\.


**​**  

| Metric name | Description | Type | HTTP client required | 
| --- | --- | --- | --- | 
|  AvailableConcurrency  |  The number of remaining concurrent requests that can be supported by the HTTP client without needing to establish another connection  |  Integer  | Apache, Netty, CRT | 
|  AwsExtendedRequestId  |  The extended request ID of the service request  |  String  |  Any  | 
|  AwsRequestId  |  The request ID of the service request  |  String  | Any | 
|  BackoffDelayDuration  |  The duration of time the SDK waited before this API call attempt  |  Duration  |  Any  | 
|  ConcurrencyAcquireDuration  |  The time taken to acquire a channel from the connection pool  |  Duration  |  Apache, Netty, CRT  | 
|  HttpClientName  |  The name of the HTTP being use for the request  |  String  |  Apache, Netty, CRT  | 
|  HttpStatusCode  |  The status code returned with the HTTP response  |  Integer  |  Any  | 
|  LeasedConcurrency  |  The number of request currently being executed by the HTTP client  |  Integer  |  Apache, Netty, CRT  | 
|  LocalStreamWindowSize  |  The local HTTP/2 window size in bytes for the stream that this request was executed on  |  Integer  |  Netty  | 
|  MarshallingDuration  |  The time it takes to marshall an SDK request to an HTTP request  |  Duration  |  Any  | 
|  MaxConcurrency  |  The max number of concurrent requests supported by the HTTP client  |  Integer  |  Apache, Netty, CRT  | 
|  PendingConcurrencyAcquires  |  The number of requests that are blocked, waiting for another TCP connection or a new stream to be available from the connection pool  |  Integer  |  Apache, Netty, CRT  | 
|  RemoteStreamWindowSize  |  The remote HTTP/2 window size in bytes for the stream that this request was executed on  |  Integer  |  Netty  | 
|  ServiceCallDuration  |  The time it takes to connect to the service, send the request, and receive the HTTP status code and header from the response  |  Duration  |  Any  | 
|  SigningDuration  |  The time it takes to sign the HTTP request  |  Duration  |  Any  | 
|  UnmarshallingDuration  |  The time it takes to unmarshall an HTTP response to an SDK response  |  Duration  |  Any  | 