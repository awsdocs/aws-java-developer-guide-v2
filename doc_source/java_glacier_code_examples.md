--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# S3 Glacier examples using SDK for Java 2\.x<a name="java_glacier_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with S3 Glacier\.

*Actions* are code excerpts that show you how to call individual S3 Glacier functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple S3 Glacier functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w591aac15c14b9c59c13)

## Actions<a name="w591aac15c14b9c59c13"></a>

### Create a vault<a name="glacier_CreateVault_java_topic"></a>

The following code example shows how to create an Amazon S3 Glacier vault\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/glacier#readme)\. 
  

```
    public static void createGlacierVault(GlacierClient glacier, String vaultName ) {

        try {
            CreateVaultRequest vaultRequest = CreateVaultRequest.builder()
                .vaultName(vaultName)
                .build();

            CreateVaultResponse createVaultResult = glacier.createVault(vaultRequest);
            System.out.println("The URI of the new vault is " + createVaultResult.location());

        } catch(GlacierException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [CreateVault](https://docs.aws.amazon.com/goto/SdkForJavaV2/glacier-2012-06-01/CreateVault) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a vault<a name="glacier_DeleteVault_java_topic"></a>

The following code example shows how to delete an Amazon S3 Glacier vault\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/glacier#readme)\. 
  

```
    public static void deleteGlacierVault(GlacierClient glacier, String vaultName) {

        try {
            DeleteVaultRequest delVaultRequest = DeleteVaultRequest.builder()
                .vaultName(vaultName)
                .build();

            glacier.deleteVault(delVaultRequest);
            System.out.println("The vault was deleted!");

        } catch(GlacierException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteVault](https://docs.aws.amazon.com/goto/SdkForJavaV2/glacier-2012-06-01/DeleteVault) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete an archive<a name="glacier_DeleteArchive_java_topic"></a>

The following code example shows how to delete an Amazon S3 Glacier archive\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/glacier#readme)\. 
  

```
    public static void deleteGlacierVault(GlacierClient glacier, String vaultName) {

        try {
            DeleteVaultRequest delVaultRequest = DeleteVaultRequest.builder()
                .vaultName(vaultName)
                .build();

            glacier.deleteVault(delVaultRequest);
            System.out.println("The vault was deleted!");

        } catch(GlacierException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteArchive](https://docs.aws.amazon.com/goto/SdkForJavaV2/glacier-2012-06-01/DeleteArchive) in *AWS SDK for Java 2\.x API Reference*\. 

### List vaults<a name="glacier_ListVaults_java_topic"></a>

The following code example shows how to list Amazon S3 Glacier vaults\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/glacier#readme)\. 
  

```
    public static void listAllVault(GlacierClient glacier) {

        boolean listComplete = false;
        String newMarker = null;
        int totalVaults = 0;
        System.out.println("Your Amazon Glacier vaults:");

        try {

            while (!listComplete) {
                ListVaultsResponse response = null;
                if (newMarker != null) {
                    ListVaultsRequest request = ListVaultsRequest.builder()
                        .marker(newMarker)
                        .build();

                    response = glacier.listVaults(request);
                } else {
                    ListVaultsRequest request = ListVaultsRequest.builder()
                        .build();
                    response = glacier.listVaults(request);
                }

                List<DescribeVaultOutput> vaultList = response.vaultList();
                for (DescribeVaultOutput v: vaultList) {
                    totalVaults += 1;
                    System.out.println("* " + v.vaultName());
                }

                // Check for further results.
                newMarker = response.marker();
                if (newMarker == null) {
                    listComplete = true;
                }
            }

            if (totalVaults == 0) {
                System.out.println("No vaults found.");
            }

        } catch(GlacierException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListVaults](https://docs.aws.amazon.com/goto/SdkForJavaV2/glacier-2012-06-01/ListVaults) in *AWS SDK for Java 2\.x API Reference*\. 

### Retrieve a vault inventory<a name="glacier_InitiateJob_InventoryRetrieval_java_topic"></a>

The following code example shows how to retrieve an Amazon S3 Glacier vault inventory\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/glacier#readme)\. 
  

```
    public static String createJob(GlacierClient glacier, String vaultName, String accountId) {

        try {

            JobParameters job = JobParameters.builder()
                .type("inventory-retrieval")
                .build();

            InitiateJobRequest initJob = InitiateJobRequest.builder()
                .jobParameters(job)
                .accountId(accountId)
                .vaultName(vaultName)
                .build();

            InitiateJobResponse response = glacier.initiateJob(initJob);
            System.out.println("The job ID is: " +response.jobId()) ;
            System.out.println("The relative URI path of the job is: " +response.location()) ;
            return response.jobId();

        } catch(GlacierException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);

        }
        return "";
    }

    //  Poll S3 Glacier = Polling a Job may take 4-6 hours according to the Documentation.
    public static void checkJob(GlacierClient glacier, String jobId, String name, String account, String path) {

       try{
           boolean finished = false;
           String jobStatus;
           int yy=0;

           while (!finished) {
               DescribeJobRequest jobRequest = DescribeJobRequest.builder()
                   .jobId(jobId)
                   .accountId(account)
                   .vaultName(name)
                   .build();

                DescribeJobResponse response = glacier.describeJob(jobRequest);
                jobStatus = response.statusCodeAsString();

               if (jobStatus.compareTo("Succeeded") == 0)
                   finished = true;
               else {
                   System.out.println(yy + " status is: " + jobStatus);
                   Thread.sleep(1000);
               }
               yy++;
           }

           System.out.println("Job has Succeeded");
           GetJobOutputRequest jobOutputRequest = GetJobOutputRequest.builder()
               .jobId(jobId)
               .vaultName(name)
               .accountId(account)
               .build();

           ResponseBytes<GetJobOutputResponse> objectBytes = glacier.getJobOutputAsBytes(jobOutputRequest);
           // Write the data to a local file.
           byte[] data = objectBytes.asByteArray();
           File myFile = new File(path);
           OutputStream os = new FileOutputStream(myFile);
           os.write(data);
           System.out.println("Successfully obtained bytes from a Glacier vault");
           os.close();

       } catch(GlacierException | InterruptedException | IOException e) {
           System.out.println(e.getMessage());
           System.exit(1);

       }
    }
```
+  For API details, see [InitiateJob](https://docs.aws.amazon.com/goto/SdkForJavaV2/glacier-2012-06-01/InitiateJob) in *AWS SDK for Java 2\.x API Reference*\. 

### Upload an archive to a vault<a name="glacier_UploadArchive_java_topic"></a>

The following code example shows how to upload an archive to an Amazon S3 Glacier vault\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/glacier#readme)\. 
  

```
    public static String uploadContent(GlacierClient glacier, Path path, String vaultName, File myFile) {

        // Get an SHA-256 tree hash value.
        String checkVal = computeSHA256(myFile);
        try {
            UploadArchiveRequest uploadRequest = UploadArchiveRequest.builder()
                .vaultName(vaultName)
                .checksum(checkVal)
                .build();

            UploadArchiveResponse res = glacier.uploadArchive(uploadRequest, path);
            return res.archiveId();

        } catch(GlacierException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }

    private static String computeSHA256(File inputFile) {

        try {
            byte[] treeHash = computeSHA256TreeHash(inputFile);
            System.out.printf("SHA-256 tree hash = %s\n", toHex(treeHash));
            return toHex(treeHash);

        } catch (IOException ioe) {
            System.err.format("Exception when reading from file %s: %s", inputFile, ioe.getMessage());
            System.exit(-1);

        } catch (NoSuchAlgorithmException nsae) {
            System.err.format("Cannot locate MessageDigest algorithm for SHA-256: %s", nsae.getMessage());
            System.exit(-1);
        }
        return "";
    }

    public static byte[] computeSHA256TreeHash(File inputFile) throws IOException,
            NoSuchAlgorithmException {

        byte[][] chunkSHA256Hashes = getChunkSHA256Hashes(inputFile);
        return computeSHA256TreeHash(chunkSHA256Hashes);
    }

    /**
     * Computes an SHA256 checksum for each 1 MB chunk of the input file. This
     * includes the checksum for the last chunk, even if it's smaller than 1 MB.
     */
    public static byte[][] getChunkSHA256Hashes(File file) throws IOException,
            NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        long numChunks = file.length() / ONE_MB;
        if (file.length() % ONE_MB > 0) {
            numChunks++;
        }

        if (numChunks == 0) {
            return new byte[][] { md.digest() };
        }

        byte[][] chunkSHA256Hashes = new byte[(int) numChunks][];
        FileInputStream fileStream = null;

        try {
            fileStream = new FileInputStream(file);
            byte[] buff = new byte[ONE_MB];

            int bytesRead;
            int idx = 0;

            while ((bytesRead = fileStream.read(buff, 0, ONE_MB)) > 0) {
                md.reset();
                md.update(buff, 0, bytesRead);
                chunkSHA256Hashes[idx++] = md.digest();
            }

            return chunkSHA256Hashes;

        } finally {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException ioe) {
                    System.err.printf("Exception while closing %s.\n %s", file.getName(),
                            ioe.getMessage());
                }
            }
        }
    }

    /**
     * Computes the SHA-256 tree hash for the passed array of 1 MB chunk
     * checksums.
     */
    public static byte[] computeSHA256TreeHash(byte[][] chunkSHA256Hashes)
            throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[][] prevLvlHashes = chunkSHA256Hashes;
        while (prevLvlHashes.length > 1) {
            int len = prevLvlHashes.length / 2;
            if (prevLvlHashes.length % 2 != 0) {
                len++;
            }

            byte[][] currLvlHashes = new byte[len][];
            int j = 0;
            for (int i = 0; i < prevLvlHashes.length; i = i + 2, j++) {

                // If there are at least two elements remaining.
                if (prevLvlHashes.length - i > 1) {

                    // Calculate a digest of the concatenated nodes.
                    md.reset();
                    md.update(prevLvlHashes[i]);
                    md.update(prevLvlHashes[i + 1]);
                    currLvlHashes[j] = md.digest();

                } else { // Take care of the remaining odd chunk
                    currLvlHashes[j] = prevLvlHashes[i];
                }
            }

            prevLvlHashes = currLvlHashes;
        }

        return prevLvlHashes[0];
    }

    /**
     * Returns the hexadecimal representation of the input byte array
     */
    public static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte datum : data) {
            String hex = Integer.toHexString(datum & 0xFF);

            if (hex.length() == 1) {
                // Append leading zero.
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString().toLowerCase();
    }
```
+  For API details, see [UploadArchive](https://docs.aws.amazon.com/goto/SdkForJavaV2/glacier-2012-06-01/UploadArchive) in *AWS SDK for Java 2\.x API Reference*\. 