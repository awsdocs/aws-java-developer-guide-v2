# Supply temporary credentials in code<a name="credentials-explicit"></a>

If the default credential chain or a specific or custom provider or provider chain doesn't work for your application, you can supply temporary credentials directly in code\. These can be [IAM role credentials](https://docs.aws.amazon.com/singlesignon/latest/userguide/howtogetcredentials.html) as[ described above](credentials-temporary.md#credentials-temporary-from-portal) or temporary credentials retrieved from AWS Security Token Service \(AWS STS\)\. If you retrieved temporary credentials using AWS STS, provide them to an AWS service client as shown in the following code example\.

1. Instantiate a class that provides the [AwsCredentials](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/AwsCredentials.html) interface, such as [AwsSessionCredentials](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/AwsSessionCredentials.html)\. Supply it with the AWS access keys and session token to use for the connection\.

1. Create a [StaticCredentialsProvider](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/auth/credentials/StaticCredentialsProvider.html) object and supply it with the `AwsSessionCredentials` object\.

1. Configure the service client builder with the `StaticCredentialsProvider` and build the client\.

The following example creates an Amazon S3 service client using temporary credentials that you supply\.

```
public static void assumeRole(String roleArn, String roleSessionName) {
    // The IAM role represented by the roleArn parameter can be assumed by any user in the same account
    // and has read-only permissions when it accesses Amazon S3.

    // The default credentials provider chain will find the single sign-on settings in the [default] profile.
    StsClient stsClient = StsClient.builder()
            .region(Region.US_EAST_1)
            .build();
    try {
        AssumeRoleRequest roleRequest = AssumeRoleRequest.builder()
                .roleArn(roleArn)
                .roleSessionName(roleSessionName)
                .build();

        AssumeRoleResponse roleResponse = stsClient.assumeRole(roleRequest);
        Credentials tempRoleCredentials = roleResponse.credentials();
        // The following temporary credential items are used when Amazon S3 is called
        String key = tempRoleCredentials.accessKeyId();
        String secKey = tempRoleCredentials.secretAccessKey();
        String secToken = tempRoleCredentials.sessionToken();

        // List all buckets in the account using the temporary credentials retrieved by invoking assumeRole.
        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsSessionCredentials.create(key, secKey, secToken)))
                .region(region)
                .build();

        List<Bucket> buckets = s3.listBuckets().buckets();
        for (Bucket bucket : buckets) {
            System.out.println("bucket name: " + bucket.name());
        }
    } catch (StsException e) {
        System.err.println(e.getMessage());
        System.exit(1);
    }
}
```