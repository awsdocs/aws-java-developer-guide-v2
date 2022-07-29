--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Region class name changes<a name="migration-client-region"></a>

This section describes the changes implemented in the AWS SDK for Java 2\.x for using the `Region` and `Regions` classes\.

## Region configuration<a name="region-configuration"></a>
+ Some AWS services don’t have Region specific endpoints\. When using those services, you must set the Region as `Region.AWS_GLOBAL` or `Region.AWS_CN_GLOBAL`\.  
**Example**  

  ```
  Region region = Region.AWS_GLOBAL;
  ```
+  `com.amazonaws.regions.Regions` and `com.amazonaws.regions.Region` classes are now combined into one class, `software.amazon.awssdk.regions.Region`\.

## Method and Class Name Mappings<a name="region-method-mapping"></a>

The following tables map Region related classes between versions 1\.x and 2\.x of the AWS SDK for Java\. You can create an instance of these classes using the `of()` method\.

**Example**  

```
RegionMetadata regionMetadata = RegionMetadata.of(Region.US_EAST_1);
```


**Regions class method changes**  

| 1\.x | 2\.x | 
| --- | --- | 
|  Regions\.fromName  |  Region\.of  | 
|  Regions\.getName  |  Region\.id  | 
|  Regions\.getDescription  |  Not Supported  | 
|  Regions\.getCurrentRegion  |  Not Supported  | 
|  Regions\.DEFAULT\_REGION  |  Not Supported  | 
|  Regions\.name  |  Not Supported  | 


**Region class method changes**  

| 1\.x | 2\.x | 
| --- | --- | 
|  Region\.getName  |  Region\.id  | 
|  Region\.hasHttpsEndpoint  |  Not Supported  | 
|  Region\.hasHttpEndpoint  |  Not Supported  | 
|  Region\.getAvailableEndpoints  |  Not Supported  | 
|  Region\.createClient  |  Not Supported  | 


**RegionMetadata class method changes**  

| 1\.x | 2\.x | 
| --- | --- | 
|  RegionMetadata\.getName  |  RegionMetadata\.name  | 
|  RegionMetadata\.getDomain  |  RegionMetadata\.domain  | 
|  RegionMetadata\.getPartition  |  RegionMetadata\.partition  | 


**ServiceMetadata class method changes**  

| 1\.x | 2\.x | 
| --- | --- | 
|  Region\.getServiceEndpoint  |  ServiceMetadata\.endpointFor\(Region\)  | 
|  Region\.isServiceSupported  |  ServiceMetadata\.regions\(\)\.contains\(Region\)  | 