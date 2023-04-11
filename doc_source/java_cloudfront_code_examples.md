# CloudFront examples using SDK for Java 2\.x<a name="java_cloudfront_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with CloudFront\.

*Actions* are code excerpts that show you how to call individual service functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple functions within the same service\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#actions)
+ [Scenarios](#scenarios)

## Actions<a name="actions"></a>

### Create a distribution<a name="cloudfront_CreateDistribution_java_topic"></a>

The following code example shows how to create a CloudFront distribution\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudfront#readme)\. 
The following example uses an Amazon Simple Storage Service \(Amazon S3\) bucket as a content origin\.   
After creating the distribution, the code creates a [CloudFrontWaiter](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/cloudfront/waiters/CloudFrontWaiter.html) to wait until the distribution is deployed before returning the distribution  
\.  

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.services.cloudfront.CloudFrontClient;
import software.amazon.awssdk.services.cloudfront.model.CreateDistributionResponse;
import software.amazon.awssdk.services.cloudfront.model.Distribution;
import software.amazon.awssdk.services.cloudfront.model.GetDistributionResponse;
import software.amazon.awssdk.services.cloudfront.model.ItemSelection;
import software.amazon.awssdk.services.cloudfront.model.Method;
import software.amazon.awssdk.services.cloudfront.model.ViewerProtocolPolicy;
import software.amazon.awssdk.services.cloudfront.waiters.CloudFrontWaiter;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.Instant;

public class CreateDistribution {

    private static final Logger logger = LoggerFactory.getLogger(CreateDistribution.class);

    public static Distribution createDistribution(CloudFrontClient cloudFrontClient, S3Client s3Client,
                                                  final String bucketName, final String keyGroupId, final String originAccessControlId) {

        final String region = s3Client.headBucket(b -> b.bucket(bucketName)).sdkHttpResponse().headers().get("x-amz-bucket-region").get(0);
        final String originDomain = bucketName + ".s3." + region + ".amazonaws.com";
        String originId = originDomain; // Use the originDomain value for the originId.

        // The service API requires some deprecated methods, such as DefaultCacheBehavior.Builder#minTTL and #forwardedValue.
        CreateDistributionResponse createDistResponse = cloudFrontClient.createDistribution(builder -> builder
                .distributionConfig(b1 -> b1
                        .origins(b2 -> b2
                                .quantity(1)
                                .items(b3 -> b3
                                        .domainName(originDomain)
                                        .id(originId)
                                        .s3OriginConfig(builder4 -> builder4.originAccessIdentity(""))
                                        .originAccessControlId(originAccessControlId)))
                        .defaultCacheBehavior(b2 -> b2
                                .viewerProtocolPolicy(ViewerProtocolPolicy.ALLOW_ALL)
                                .targetOriginId(originId)
                                .minTTL(200L)
                                .forwardedValues(b5 -> b5
                                        .cookies(cp -> cp
                                                .forward(ItemSelection.NONE))
                                        .queryString(true))
                                .trustedKeyGroups(b3 -> b3
                                        .quantity(1)
                                        .items(keyGroupId)
                                        .enabled(true))
                                .allowedMethods(b4 -> b4
                                        .quantity(2)
                                        .items(Method.HEAD, Method.GET)
                                        .cachedMethods(b5 -> b5
                                                .quantity(2)
                                                .items(Method.HEAD, Method.GET))))
                        .cacheBehaviors(b -> b
                                .quantity(1)
                                .items(b2 -> b2
                                        .pathPattern("/index.html")
                                        .viewerProtocolPolicy(ViewerProtocolPolicy.ALLOW_ALL)
                                        .targetOriginId(originId)
                                        .trustedKeyGroups(b3 -> b3
                                                .quantity(1)
                                                .items(keyGroupId)
                                                .enabled(true))
                                        .minTTL(200L)
                                        .forwardedValues(b4 -> b4
                                                .cookies(cp -> cp
                                                        .forward(ItemSelection.NONE))
                                                .queryString(true))
                                        .allowedMethods(b5 -> b5.
                                                quantity(2).
                                                items(Method.HEAD, Method.GET)
                                                .cachedMethods(b6 -> b6
                                                        .quantity(2)
                                                        .items(Method.HEAD, Method.GET)))))
                        .enabled(true)
                        .comment("Distribution built with java")
                        .callerReference(Instant.now().toString())
                ));

        final Distribution distribution = createDistResponse.distribution();
        logger.info("Distribution created. DomainName: [{}]  Id: [{}]", distribution.domainName(), distribution.id());
        logger.info("Waiting for distribution to be deployed ...");
        try (CloudFrontWaiter cfWaiter = CloudFrontWaiter.builder().client(cloudFrontClient).build()) {
            ResponseOrException<GetDistributionResponse> responseOrException =
                    cfWaiter.waitUntilDistributionDeployed(builder -> builder.id(distribution.id())).matched();
            responseOrException.response().orElseThrow(() -> new RuntimeException("Distribution not created"));
            logger.info("Distribution deployed. DomainName: [{}]  Id: [{}]", distribution.domainName(), distribution.id());
        }
        return distribution;
    }
}
```
+  For API details, see [CreateDistribution](https://docs.aws.amazon.com/goto/SdkForJavaV2/cloudfront-2020-05-31/CreateDistribution) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a function<a name="cloudfront_CreateFunction_java_topic"></a>

The following code example shows how to create an Amazon CloudFront function\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudfront#readme)\. 
  

```
    public static String createNewFunction(CloudFrontClient cloudFrontClient, String functionName, String filePath) {

        try {

            InputStream fileIs = CreateFunction.class.getClassLoader().getResourceAsStream(filePath);
            SdkBytes functionCode = SdkBytes.fromInputStream(fileIs);

            FunctionConfig config = FunctionConfig.builder()
                .comment("Created by using the CloudFront Java API")
                .runtime(FunctionRuntime.CLOUDFRONT_JS_1_0)
                .build();

            CreateFunctionRequest functionRequest = CreateFunctionRequest.builder()
                .name(functionName)
                .functionCode(functionCode)
                .functionConfig(config)
                .build();

            CreateFunctionResponse response = cloudFrontClient.createFunction(functionRequest);
            return response.functionSummary().functionMetadata().functionARN();

        } catch (CloudFrontException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateFunction](https://docs.aws.amazon.com/goto/SdkForJavaV2/cloudfront-2020-05-31/CreateFunction) in *AWS SDK for Java 2\.x API Reference*\. 

### Create a key group<a name="cloudfront_CreateKeyGroup_java_topic"></a>

The following code example shows how to create a key group that you can use with signed URLs and signed cookies\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudfront#readme)\. 
A key group requires at least one public key that is used to verify signed URLs or cookies\.  

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.cloudfront.CloudFrontClient;

import java.util.UUID;

public class CreateKeyGroup {
    private static final Logger logger = LoggerFactory.getLogger(CreateKeyGroup.class);

    public static String createKeyGroup(CloudFrontClient cloudFrontClient, String publicKeyId) {
        String keyGroupId = cloudFrontClient.createKeyGroup(b -> b.
                        keyGroupConfig(c -> c
                                .items(publicKeyId)
                                .name("JavaKeyGroup"+ UUID.randomUUID())))
                .keyGroup().id();
        logger.info("KeyGroup created with ID: [{}]", keyGroupId);
        return keyGroupId;
    }
}
```
+  For API details, see [CreateKeyGroup](https://docs.aws.amazon.com/goto/SdkForJavaV2/cloudfront-2020-05-31/CreateKeyGroup) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a distribution<a name="cloudfront_DeleteDistribution_java_topic"></a>

The following code example shows how to delete a CloudFront distribution\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudfront#readme)\. 
The following code example updates a distribution to *disabled*, uses a waiter that waits for the change to be deployed, then deletes the distribution\.  

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.services.cloudfront.CloudFrontClient;
import software.amazon.awssdk.services.cloudfront.model.DeleteDistributionResponse;
import software.amazon.awssdk.services.cloudfront.model.DistributionConfig;
import software.amazon.awssdk.services.cloudfront.model.GetDistributionResponse;
import software.amazon.awssdk.services.cloudfront.waiters.CloudFrontWaiter;

public class DeleteDistribution {
    private static final Logger logger = LoggerFactory.getLogger(DeleteDistribution.class);

    public static void deleteDistribution(final CloudFrontClient cloudFrontClient, final String distributionId) {
        // First, disable the distribution by updating it.
        GetDistributionResponse response = cloudFrontClient.
                getDistribution(b -> b
                        .id(distributionId));
        String etag = response.eTag();
        DistributionConfig distConfig = response.distribution().distributionConfig();

        cloudFrontClient.updateDistribution(builder -> builder
                .id(distributionId)
                .distributionConfig(builder1 -> builder1
                        .cacheBehaviors(distConfig.cacheBehaviors())
                        .defaultCacheBehavior(distConfig.defaultCacheBehavior())
                        .enabled(false)
                        .origins(distConfig.origins())
                        .comment(distConfig.comment())
                        .callerReference(distConfig.callerReference())
                        .defaultCacheBehavior(distConfig.defaultCacheBehavior())
                        .priceClass(distConfig.priceClass())
                        .aliases(distConfig.aliases())
                        .logging(distConfig.logging())
                        .defaultRootObject(distConfig.defaultRootObject())
                        .customErrorResponses(distConfig.customErrorResponses())
                        .httpVersion(distConfig.httpVersion())
                        .isIPV6Enabled(distConfig.isIPV6Enabled())
                        .restrictions(distConfig.restrictions())
                        .viewerCertificate(distConfig.viewerCertificate())
                        .webACLId(distConfig.webACLId())
                        .originGroups(distConfig.originGroups()))
                .ifMatch(etag));

        logger.info("Distribution [{}] is DISABLED, waiting for deployment before deleting ...", distributionId);
        GetDistributionResponse distributionResponse;
        try (CloudFrontWaiter cfWaiter = CloudFrontWaiter.builder().client(cloudFrontClient).build()) {
            ResponseOrException<GetDistributionResponse> responseOrException =
                    cfWaiter.waitUntilDistributionDeployed(builder -> builder.id(distributionId)).matched();
            distributionResponse = responseOrException.response().orElseThrow(() -> new RuntimeException("Could not disable distribution"));
        }

        DeleteDistributionResponse deleteDistributionResponse = cloudFrontClient.deleteDistribution(builder -> builder
                .id(distributionId)
                .ifMatch(distributionResponse.eTag()));
        if ( deleteDistributionResponse.sdkHttpResponse().isSuccessful() ){
            logger.info("Distribution [{}] DELETED", distributionId);
        }
    }
}
```
+ For API details, see the following topics in *AWS SDK for Java 2\.x API Reference*\.
  + [DeleteDistribution](https://docs.aws.amazon.com/goto/SdkForJavaV2/cloudfront-2020-05-31/DeleteDistribution)
  + [UpdateDistribution](https://docs.aws.amazon.com/goto/SdkForJavaV2/cloudfront-2020-05-31/UpdateDistribution)

### Delete signing resources<a name="cloudfront_DeleteSigningResources_java_topic"></a>

The following code example shows how to delete resources that are used to gain access to restricted content in an Amazon Simple Storage Service \(Amazon S3\) bucket\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudfront#readme)\. 
  

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.cloudfront.CloudFrontClient;
import software.amazon.awssdk.services.cloudfront.model.DeleteKeyGroupResponse;
import software.amazon.awssdk.services.cloudfront.model.DeleteOriginAccessControlResponse;
import software.amazon.awssdk.services.cloudfront.model.DeletePublicKeyResponse;
import software.amazon.awssdk.services.cloudfront.model.GetKeyGroupResponse;
import software.amazon.awssdk.services.cloudfront.model.GetOriginAccessControlResponse;
import software.amazon.awssdk.services.cloudfront.model.GetPublicKeyResponse;

public class DeleteSigningResources {
    private static final Logger logger = LoggerFactory.getLogger(DeleteSigningResources.class);

    public static void deleteOriginAccessControl(final CloudFrontClient cloudFrontClient, final String originAccessControlId){
        GetOriginAccessControlResponse getResponse = cloudFrontClient.getOriginAccessControl(b -> b.id(originAccessControlId));
        DeleteOriginAccessControlResponse deleteResponse = cloudFrontClient.deleteOriginAccessControl(builder -> builder
                .id(originAccessControlId)
                .ifMatch(getResponse.eTag()));
        if ( deleteResponse.sdkHttpResponse().isSuccessful() ){
            logger.info("Successfully deleted Origin Access Control [{}]", originAccessControlId);
        }
    }

    public static void deleteKeyGroup(final CloudFrontClient cloudFrontClient, final String keyGroupId){

        GetKeyGroupResponse getResponse = cloudFrontClient.getKeyGroup(b -> b.id(keyGroupId));
        DeleteKeyGroupResponse deleteResponse = cloudFrontClient.deleteKeyGroup(builder -> builder
                .id(keyGroupId)
                .ifMatch(getResponse.eTag()));
        if ( deleteResponse.sdkHttpResponse().isSuccessful() ){
            logger.info("Successfully deleted Key Group [{}]", keyGroupId);
        }
    }

    public static void deletePublicKey(final CloudFrontClient cloudFrontClient, final String publicKeyId){
        GetPublicKeyResponse getResponse = cloudFrontClient.getPublicKey(b -> b.id(publicKeyId));

        DeletePublicKeyResponse deleteResponse = cloudFrontClient.deletePublicKey(builder -> builder
                .id(publicKeyId)
                .ifMatch(getResponse.eTag()));

        if (deleteResponse.sdkHttpResponse().isSuccessful()){
            logger.info("Successfully deleted Public Key [{}]", publicKeyId);
        }
    }
}
```
+ For API details, see the following topics in *AWS SDK for Java 2\.x API Reference*\.
  + [DeleteKeyGroup](https://docs.aws.amazon.com/goto/SdkForJavaV2/cloudfront-2020-05-31/DeleteKeyGroup)
  + [DeleteOriginAccessControl](https://docs.aws.amazon.com/goto/SdkForJavaV2/cloudfront-2020-05-31/DeleteOriginAccessControl)
  + [DeletePublicKey](https://docs.aws.amazon.com/goto/SdkForJavaV2/cloudfront-2020-05-31/DeletePublicKey)

### Update a distribution<a name="cloudfront_UpdateDistribution_java_topic"></a>

The following code example shows how to update an Amazon CloudFront distribution\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudfront#readme)\. 
  

```
    public static void modDistribution(CloudFrontClient cloudFrontClient, String idVal) {

        try {
            // Get the Distribution to modify.
            GetDistributionRequest disRequest = GetDistributionRequest.builder()
                 .id(idVal)
                 .build();

            GetDistributionResponse response = cloudFrontClient.getDistribution(disRequest);
            Distribution disObject = response.distribution();
            DistributionConfig config = disObject.distributionConfig();

            // Create a new  DistributionConfig object and add new values to comment and aliases
            DistributionConfig config1 = DistributionConfig.builder()
                .aliases(config.aliases()) // You can pass in new values here
                .comment("New Comment")
                .cacheBehaviors(config.cacheBehaviors())
                .priceClass(config.priceClass())
                .defaultCacheBehavior(config.defaultCacheBehavior())
                .enabled(config.enabled())
                .callerReference(config.callerReference())
                .logging(config.logging())
                .originGroups(config.originGroups())
                .origins(config.origins())
                .restrictions(config.restrictions())
                .defaultRootObject(config.defaultRootObject())
                .webACLId(config.webACLId())
                .httpVersion(config.httpVersion())
                .viewerCertificate(config.viewerCertificate())
                .customErrorResponses(config.customErrorResponses())
                .build();

            UpdateDistributionRequest updateDistributionRequest = UpdateDistributionRequest.builder()
                .distributionConfig(config1)
                .id(disObject.id())
                .ifMatch(response.eTag())
                .build();

            cloudFrontClient.updateDistribution(updateDistributionRequest);

        } catch (CloudFrontException e){
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [UpdateDistribution](https://docs.aws.amazon.com/goto/SdkForJavaV2/cloudfront-2020-05-31/UpdateDistribution) in *AWS SDK for Java 2\.x API Reference*\. 

### Upload a public key<a name="cloudfront_CreatePublicKey_java_topic"></a>

The following code example shows how to upload a public key\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudfront#readme)\. 
The following code example reads in a public key and uploads it to Amazon CloudFront\.  

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.cloudfront.CloudFrontClient;
import software.amazon.awssdk.services.cloudfront.model.CreatePublicKeyResponse;
import software.amazon.awssdk.utils.IoUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class CreatePublicKey {
    private static final Logger logger = LoggerFactory.getLogger(CreatePublicKey.class);

    public static String createPublicKey(CloudFrontClient cloudFrontClient, String publicKeyFileName) {
        try (InputStream is = CreatePublicKey.class.getClassLoader().getResourceAsStream(publicKeyFileName)) {
            String publicKeyString = IoUtils.toUtf8String(is);
            CreatePublicKeyResponse createPublicKeyResponse = cloudFrontClient.createPublicKey(b -> b.
                    publicKeyConfig(c -> c
                            .name("JavaCreatedPublicKey" + UUID.randomUUID())
                            .encodedKey(publicKeyString)
                            .callerReference(UUID.randomUUID().toString())));
            String createdPublicKeyId = createPublicKeyResponse.publicKey().id();
            logger.info("Public key created with id: [{}]", createdPublicKeyId);
            return createdPublicKeyId;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```
+  For API details, see [CreatePublicKey](https://docs.aws.amazon.com/goto/SdkForJavaV2/cloudfront-2020-05-31/CreatePublicKey) in *AWS SDK for Java 2\.x API Reference*\. 

## Scenarios<a name="scenarios"></a>

### Sign URLs and cookies<a name="cloudfront_CloudFrontUtilities_java_topic"></a>

The following code example shows how to create signed URLs and cookies that allow access to restricted resources\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/cloudfront#readme)\. 
Use the [CannedSignerRequest](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/cloudfront/model/CannedSignerRequest.html) class to sign URLs or cookies with a *canned* policy\.  

```
import software.amazon.awssdk.services.cloudfront.model.CannedSignerRequest;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CreateCannedPolicyRequest {

    public static CannedSignerRequest createRequestForCannedPolicy(String distributionDomainName, String fileNameToUpload,
                                                                   String privateKeyFullPath, String publicKeyId) throws Exception{
        String protocol = "https";
        String resourcePath = "/" + fileNameToUpload;

        String cloudFrontUrl = new URL(protocol, distributionDomainName, resourcePath).toString();
        Instant expirationDate = Instant.now().plus(7, ChronoUnit.DAYS);
        Path path = Paths.get(privateKeyFullPath);

        return CannedSignerRequest.builder()
                .resourceUrl(cloudFrontUrl)
                .privateKey(path)
                .keyPairId(publicKeyId)
                .expirationDate(expirationDate)
                .build();
    }
}
```
Use the [CustomSignerRequest](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/cloudfront/model/CustomSignerRequest.html) class to sign URLs or cookies with a *custom* policy\. The `activeDate` and `ipRange` are optional methods\.  

```
import software.amazon.awssdk.services.cloudfront.model.CustomSignerRequest;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CreateCustomPolicyRequest {

    public static CustomSignerRequest createRequestForCustomPolicy(String distributionDomainName, String fileNameToUpload,
                                                                   String privateKeyFullPath, String publicKeyId) throws Exception {
        String protocol = "https";
        String resourcePath = "/" + fileNameToUpload;

        String cloudFrontUrl = new URL(protocol, distributionDomainName, resourcePath).toString();
        Instant expireDate = Instant.now().plus(7, ChronoUnit.DAYS);
        // URL will be accessible tomorrow using the signed URL.
        Instant activeDate = Instant.now().plus(1, ChronoUnit.DAYS);
        Path path = Paths.get(privateKeyFullPath);

        return CustomSignerRequest.builder()
                .resourceUrl(cloudFrontUrl)
                .privateKey(path)
                .keyPairId(publicKeyId)
                .expirationDate(expireDate)
                .activeDate(activeDate)      // Optional.
                //.ipRange("192.168.0.1/24") // Optional.
                .build();
    }
}
```
The following example demonstrates the use of the [CloudFrontUtilities](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/cloudfront/CloudFrontUtilities.html) class to produce signed cookies and URLs\. [View](https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/cloudfront/src/main/java/com/example/cloudfront/SigningUtilities.java) this code example on GitHub\.  

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.cloudfront.cookie.CookiesForCannedPolicy;
import software.amazon.awssdk.services.cloudfront.cookie.CookiesForCustomPolicy;
import software.amazon.awssdk.services.cloudfront.model.CannedSignerRequest;
import software.amazon.awssdk.services.cloudfront.model.CustomSignerRequest;
import software.amazon.awssdk.services.cloudfront.url.SignedUrl;

public class SigningUtilities {
    private static final Logger logger = LoggerFactory.getLogger(SigningUtilities.class);
    private static final CloudFrontUtilities cloudFrontUtilities = CloudFrontUtilities.create();

    public static SignedUrl signUrlForCannedPolicy(CannedSignerRequest cannedSignerRequest){
        SignedUrl signedUrl = cloudFrontUtilities.getSignedUrlWithCannedPolicy(cannedSignerRequest);
        logger.info("Signed URL: [{}]", signedUrl.url());
        return signedUrl;
    }

    public static SignedUrl signUrlForCustomPolicy(CustomSignerRequest customSignerRequest){
        SignedUrl signedUrl = cloudFrontUtilities.getSignedUrlWithCustomPolicy(customSignerRequest);
        logger.info("Signed URL: [{}]", signedUrl.url());
        return signedUrl;
    }

    public static CookiesForCannedPolicy getCookiesForCannedPolicy(CannedSignerRequest cannedSignerRequest){
        CookiesForCannedPolicy cookiesForCannedPolicy = cloudFrontUtilities.getCookiesForCannedPolicy(cannedSignerRequest);
        logger.info("Cookie EXPIRES header [{}]", cookiesForCannedPolicy.expiresHeaderValue());
        logger.info("Cookie KEYPAIR header [{}]", cookiesForCannedPolicy.keyPairIdHeaderValue());
        logger.info("Cookie SIGNATURE header [{}]", cookiesForCannedPolicy.signatureHeaderValue());
        return cookiesForCannedPolicy;
    }

    public static CookiesForCustomPolicy getCookiesForCustomPolicy(CustomSignerRequest customSignerRequest) {
        CookiesForCustomPolicy cookiesForCustomPolicy = cloudFrontUtilities.getCookiesForCustomPolicy(customSignerRequest);
        logger.info("Cookie POLICY header [{}]", cookiesForCustomPolicy.policyHeaderValue());
        logger.info("Cookie KEYPAIR header [{}]", cookiesForCustomPolicy.keyPairIdHeaderValue());
        logger.info("Cookie SIGNATURE header [{}]", cookiesForCustomPolicy.signatureHeaderValue());
        return cookiesForCustomPolicy;
    }
}
```
+  For API details, see [CloudFrontUtilities](https://docs.aws.amazon.com/goto/SdkForJavaV2/cloudfront-2020-05-31/CloudFrontUtilities) in *AWS SDK for Java 2\.x API Reference*\. 