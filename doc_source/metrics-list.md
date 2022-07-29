--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Service client metrics<a name="metrics-list"></a>

With the AWS SDK for Java version 2 \(v2\), you can collect metrics about the service clients in your application and then publish \(output\) those metrics to CloudWatch\.

This topic contains the list and descriptions for the metrics that are collected\.

For more information about enabling and configuring metrics for the SDK, see [Enabling SDK metrics](metrics.md)\.

## Metrics collected with each request<a name="metrics-perrequest"></a>


**​**  

| Metric name | Description | Type | Collected by default? | 
| --- | --- | --- | --- | 
|  ServiceId  |  Service ID of the AWS service that the API request is made against  |  String  |  Yes  | 
|  OperationName  |  The name of the AWS API the request is made to  |  String  |  Yes  | 
|  ApiCallSuccessful  |  True if the API call was successful; false if not  |  Boolean  |  Yes  | 
|  RetryCount  |  Number of times the SDK retried the API call  |  Integer  |  Yes  | 
|  ApiCallDuration  |  The total time taken to finish a request \(inclusive of all retries\)  |  Duration  |  Yes  | 
|  MarshallingDuration  |  The time taken to marshall the request  |  Duration  |  Yes  | 
|  CredentialsFetchDuration  |  The time taken to fetch signing credentials for the request  |  Duration  |  Yes  | 

### Metrics collected for each request attempt<a name="metrics-perattempt"></a>

Each API call that your application makes may take multiple attempts before responded with a success or failure\. These metrics are collected for each attempt\.


**​**  

| Metric name | Description | Type | Collected by default? | 
| --- | --- | --- | --- | 
|  BackoffDelayDuration  |  The duration of time the SDK waited before this API call attempt  |  Duration  |  Yes  | 
|  MarshallingDuration  |  The time it takes to marshall an SDK request to an HTTP request  |  Duration  |  Yes  | 
|  SigningDuration  |  The time it takes to sign the HTTP request  |  Duration  |  Yes  | 
|  ServiceCallDuration  |  The time it takes to connect to the service, send the request, and receive the HTTP status code and header from the response  |  Duration  |  Yes  | 
|  UnmarshallingDuration  |  The time it takes to unmarshall an HTTP response to an SDK response  |  Duration  |  Yes  | 
|  AwsRequestId  |  The request ID of the service request  |  String  |  Yes  | 
|  AwsExtendedRequestId  |  The extended request ID of the service request  |  String  |  Yes  | 
|  HttpClientName  |  The name of the HTTP being use for the request  |  String  |  Yes  | 
|  MaxConcurrency  |  The max number of concurrent requests supported by the HTTP client  |  Integer  |  Yes  | 
|  AvailableConcurrency  |  The number of remaining concurrent requests that can be supported by the HTTP client without needing to establish another connection  |  Integer  |  Yes  | 
|  LeasedConcurrency  |  The number of request currently being executed by the HTTP client  |  Integer  |  Yes  | 
|  PendingConcurrencyAcquires  |  The number of requests that are blocked, waiting for another TCP connection or a new stream to be available from the connection pool  |  Integer  |  Yes  | 
|  HttpStatusCode  |  The status code returned with the HTTP response  |  Integer  |  Yes  | 
|  LocalStreamWindowSize  |  The local HTTP/2 window size in bytes this request’s stream  |  Integer  |  Yes  | 
|  RemoteStreamWindowSize  |  The remote HTTP/2 window size in bytes this request’s stream  |  Integer  |  Yes  | 