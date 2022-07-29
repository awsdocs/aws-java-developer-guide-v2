--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Amazon Textract examples using SDK for Java 2\.x<a name="java_textract_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Amazon Textract\.

*Actions* are code excerpts that show you how to call individual Amazon Textract functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple Amazon Textract functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w591aac15c14b9c69c13)

## Actions<a name="w591aac15c14b9c69c13"></a>

### Analyze a document<a name="textract_AnalyzeDocument_java_topic"></a>

The following code example shows how to analyze a document using Amazon Textract\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/textract#readme)\. 
  

```
    public static void analyzeDoc(TextractClient textractClient, String sourceDoc) {

        try {
            InputStream sourceStream = new FileInputStream(new File(sourceDoc));
            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);

            // Get the input Document object as bytes
            Document myDoc = Document.builder()
                    .bytes(sourceBytes)
                    .build();

            List<FeatureType> featureTypes = new ArrayList<FeatureType>();
            featureTypes.add(FeatureType.FORMS);
            featureTypes.add(FeatureType.TABLES);

            AnalyzeDocumentRequest analyzeDocumentRequest = AnalyzeDocumentRequest.builder()
                    .featureTypes(featureTypes)
                    .document(myDoc)
                    .build();

            AnalyzeDocumentResponse analyzeDocument = textractClient.analyzeDocument(analyzeDocumentRequest);
            List<Block> docInfo = analyzeDocument.blocks();
            Iterator<Block> blockIterator = docInfo.iterator();

            while(blockIterator.hasNext()) {
                Block block = blockIterator.next();
                System.out.println("The block type is " +block.blockType().toString());
            }

        } catch (TextractException | FileNotFoundException e) {

            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [AnalyzeDocument](https://docs.aws.amazon.com/goto/SdkForJavaV2/textract-2018-06-27/AnalyzeDocument) in *AWS SDK for Java 2\.x API Reference*\. 

### Detect text in a document<a name="textract_DetectDocumentText_java_topic"></a>

The following code example shows how to detect text in a document using Amazon Textract\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/textract#readme)\. 
Detect text from an input document\.  

```
    public static void detectDocText(TextractClient textractClient,String sourceDoc) {

        try {
            InputStream sourceStream = new FileInputStream(new File(sourceDoc));
            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);

            // Get the input Document object as bytes
            Document myDoc = Document.builder()
                .bytes(sourceBytes)
                .build();

            DetectDocumentTextRequest detectDocumentTextRequest = DetectDocumentTextRequest.builder()
                .document(myDoc)
                .build();

            // Invoke the Detect operation
            DetectDocumentTextResponse textResponse = textractClient.detectDocumentText(detectDocumentTextRequest);
            List<Block> docInfo = textResponse.blocks();
            for (Block block : docInfo) {
                System.out.println("The block type is " + block.blockType().toString());
            }

            DocumentMetadata documentMetadata = textResponse.documentMetadata();
            System.out.println("The number of pages in the document is " +documentMetadata.pages());

        } catch (TextractException | FileNotFoundException e) {

            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
Detect text from a document located in an Amazon S3 bucket\.  

```
    public static void detectDocTextS3 (TextractClient textractClient, String bucketName, String docName) {

        try {
            S3Object s3Object = S3Object.builder()
                .bucket(bucketName)
                .name(docName)
                .build();

            // Create a Document object and reference the s3Object instance
            Document myDoc = Document.builder()
                .s3Object(s3Object)
                .build();

            DetectDocumentTextRequest detectDocumentTextRequest = DetectDocumentTextRequest.builder()
                .document(myDoc)
                .build();

            DetectDocumentTextResponse textResponse = textractClient.detectDocumentText(detectDocumentTextRequest);
            for (Block block: textResponse.blocks()) {
                System.out.println("The block type is " +block.blockType().toString());
            }

            DocumentMetadata documentMetadata = textResponse.documentMetadata();
            System.out.println("The number of pages in the document is " +documentMetadata.pages());

        } catch (TextractException e) {

            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DetectDocumentText](https://docs.aws.amazon.com/goto/SdkForJavaV2/textract-2018-06-27/DetectDocumentText) in *AWS SDK for Java 2\.x API Reference*\. 

### Start asynchronous analysis of a document<a name="textract_StartDocumentAnalysis_java_topic"></a>

The following code example shows how to start asynchronous analysis of a document using Amazon Textract\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/textract#readme)\. 
  

```
    public static String startDocAnalysisS3 (TextractClient textractClient, String bucketName, String docName) {

        try {
            List<FeatureType> myList = new ArrayList<>();
            myList.add(FeatureType.TABLES);
            myList.add(FeatureType.FORMS);

            S3Object s3Object = S3Object.builder()
                .bucket(bucketName)
                .name(docName)
                .build();

            DocumentLocation location = DocumentLocation.builder()
                .s3Object(s3Object)
                .build();

            StartDocumentAnalysisRequest documentAnalysisRequest = StartDocumentAnalysisRequest.builder()
                .documentLocation(location)
                .featureTypes(myList)
                .build();

            StartDocumentAnalysisResponse response = textractClient.startDocumentAnalysis(documentAnalysisRequest);

            // Get the job ID
            String jobId = response.jobId();
            return jobId;

        } catch (TextractException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "" ;
    }

    private static String getJobResults(TextractClient textractClient, String jobId) {

        boolean finished = false;
        int index = 0 ;
        String status = "" ;

       try {
           while (!finished) {
               GetDocumentAnalysisRequest analysisRequest = GetDocumentAnalysisRequest.builder()
                   .jobId(jobId)
                   .maxResults(1000)
                   .build();

               GetDocumentAnalysisResponse response = textractClient.getDocumentAnalysis(analysisRequest);
               status = response.jobStatus().toString();

               if (status.compareTo("SUCCEEDED") == 0)
                   finished = true;
               else {
                   System.out.println(index + " status is: " + status);
                   Thread.sleep(1000);
               }
               index++ ;
           }

           return status;

       } catch( InterruptedException e) {
           System.out.println(e.getMessage());
           System.exit(1);
       }
       return "";
    }
```
+  For API details, see [StartDocumentAnalysis](https://docs.aws.amazon.com/goto/SdkForJavaV2/textract-2018-06-27/StartDocumentAnalysis) in *AWS SDK for Java 2\.x API Reference*\. 