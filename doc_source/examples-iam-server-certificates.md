# Working with IAM server certificates<a name="examples-iam-server-certificates"></a>

To enable HTTPS connections to your website or application on AWS, you need an SSL/TLS *server certificate*\. You can use a server certificate provided by AWS Certificate Manager or one that you obtained from an external provider\.

We recommend that you use ACM to provision, manage, and deploy your server certificates\. With ACM you can request a certificate, deploy it to your AWS resources, and let ACM handle certificate renewals for you\. Certificates provided by ACM are free\. For more information about ACM, see the [AWS Certificate Manager User Guide](http://docs.aws.amazon.com/acm/latest/userguide/)\.

## Get a server certificate<a name="get-a-server-certificate"></a>

You can retrieve a server certificate by calling the IamClient’s `getServerCertificate` method, passing it a [GetServerCertificateRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/GetServerCertificateRequest.html) with the certificate’s name\.

 **Imports** 

```
import software.amazon.awssdk.services.iam.model.GetServerCertificateRequest;
import software.amazon.awssdk.services.iam.model.GetServerCertificateResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.IamException;
```

 **Code** 

```
    public static void getCertificate(IamClient iam,String certName ) {

        try {
            GetServerCertificateRequest request = GetServerCertificateRequest.builder()
                    .serverCertificateName(certName)
                    .build();

            GetServerCertificateResponse response = iam.getServerCertificate(request);
            System.out.format("Successfully retrieved certificate with body %s",
                response.serverCertificate().certificateBody());

         } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/GetServerCertificate.java) on GitHub\.

## List server certificates<a name="list-server-certificates"></a>

To list your server certificates, call the IamClient’s `listServerCertificates` method with a [ListServerCertificatesRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/ListServerCertificatesRequest.html)\. It returns a [ListServerCertificatesResponse](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/ListServerCertificatesResponse.html)\.

Call the returned `ListServerCertificateResponse` object’s `serverCertificateMetadataList` method to get a list of [ServerCertificateMetadata](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/ServerCertificateMetadata.html) objects that you can use to get information about each certificate\.

Results may be truncated; if the `ListServerCertificateResponse` object’s `isTruncated` method returns `true`, call the `ListServerCertificatesResponse` object’s `marker` method and use the marker to create a new request\. Use the new request to call `listServerCertificates` again to get the next batch of results\.

 **Imports** 

```
import software.amazon.awssdk.services.iam.model.IamException;
import software.amazon.awssdk.services.iam.model.ListServerCertificatesRequest;
import software.amazon.awssdk.services.iam.model.ListServerCertificatesResponse;
import software.amazon.awssdk.services.iam.model.ServerCertificateMetadata;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
```

 **Code** 

```
    public static void listCertificates(IamClient iam) {

        try {
            boolean done = false;
            String newMarker = null;

            while(!done) {
              ListServerCertificatesResponse response;

            if (newMarker == null) {
                ListServerCertificatesRequest request =
                        ListServerCertificatesRequest.builder().build();
                response = iam.listServerCertificates(request);
            } else {
                ListServerCertificatesRequest request =
                        ListServerCertificatesRequest.builder()
                                .marker(newMarker).build();
                response = iam.listServerCertificates(request);
            }

            for(ServerCertificateMetadata metadata :
                    response.serverCertificateMetadataList()) {
                System.out.printf("Retrieved server certificate %s",
                        metadata.serverCertificateName());
            }

            if(!response.isTruncated()) {
                done = true;
            } else {
                newMarker = response.marker();
            }
        }

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/ListServerCertificates.java) on GitHub\.

## Update a server certificate<a name="update-a-server-certificate"></a>

You can update a server certificate’s name or path by calling the IamClient’s `updateServerCertificate` method\. It takes a [UpdateServerCertificateRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/UpdateServerCertificateRequest.html) object set with the server certificate’s current name and either a new name or new path to use\.

 **Imports** 

```
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.IamException;
import software.amazon.awssdk.services.iam.model.UpdateServerCertificateRequest;
import software.amazon.awssdk.services.iam.model.UpdateServerCertificateResponse;
```

 **Code** 

```
    public static void updateCertificate(IamClient iam, String curName, String newName) {

        try {
            UpdateServerCertificateRequest request =
                UpdateServerCertificateRequest.builder()
                        .serverCertificateName(curName)
                        .newServerCertificateName(newName)
                        .build();

            UpdateServerCertificateResponse response =
                iam.updateServerCertificate(request);


            System.out.printf("Successfully updated server certificate to name %s",
                newName);

        } catch (IamException e) {
             System.err.println(e.awsErrorDetails().errorMessage());
             System.exit(1);
        }
     }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/UpdateServerCertificate.java) on GitHub\.

## Delete a server certificate<a name="delete-a-server-certificate"></a>

To delete a server certificate, call the IamClient’s `deleteServerCertificate` method with a [DeleteServerCertificateRequest](http://docs.aws.amazon.com/sdk-for-java/latest/reference/software/amazon/awssdk/services/iam/model/DeleteServerCertificateRequest.html) containing the certificate’s name\.

 **Imports** 

```
import software.amazon.awssdk.services.iam.model.DeleteServerCertificateRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.IamException;
```

 **Code** 

```
    public static void deleteCert(IamClient iam,String certName ) {

        try {
            DeleteServerCertificateRequest request =
                DeleteServerCertificateRequest.builder()
                        .serverCertificateName(certName)
                        .build();

            iam.deleteServerCertificate(request);
            System.out.println("Successfully deleted server certificate " +
                    certName);

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```

See the [complete example](https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/iam/src/main/java/com/example/iam/DeleteServerCertificate.java) on GitHub\.

## More information<a name="more-information"></a>
+  [Working with Server Certificates](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_server-certs.html) in the IAM User Guide
+  [GetServerCertificate](https://docs.aws.amazon.com/IAM/latest/APIReference/API_GetServerCertificate.html) in the IAM API Reference
+  [ListServerCertificates](https://docs.aws.amazon.com/IAM/latest/APIReference/API_ListServerCertificates.html) in the IAM API Reference
+  [UpdateServerCertificate](https://docs.aws.amazon.com/IAM/latest/APIReference/API_UpdateServerCertificate.html) in the IAM API Reference
+  [DeleteServerCertificate](https://docs.aws.amazon.com/IAM/latest/APIReference/API_DeleteServerCertificate.html) in the IAM API Reference
+  [AWS Certificate Manager User Guide](http://docs.aws.amazon.com/acm/latest/userguide/) 