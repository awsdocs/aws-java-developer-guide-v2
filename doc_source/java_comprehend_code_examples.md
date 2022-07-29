--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\! By the way, the AWS SDK for Java team is hiring [software development engineers](https://github.com/aws/aws-sdk-java-v2/issues/3156)\!

--------

# Amazon Comprehend examples using SDK for Java 2\.x<a name="java_comprehend_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Amazon Comprehend\.

*Actions* are code excerpts that show you how to call individual Amazon Comprehend functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple Amazon Comprehend functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w591aac15c14b9c25c13)

## Actions<a name="w591aac15c14b9c25c13"></a>

### Create a document classifier<a name="comprehend_CreateDocumentClassifier_java_topic"></a>

The following code example shows how to create an Amazon Comprehend document classifier\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/comprehend#readme)\. 
  

```
    public static void createDocumentClassifier(ComprehendClient comClient, String dataAccessRoleArn, String s3Uri, String documentClassifierName){

         try {
             DocumentClassifierInputDataConfig config = DocumentClassifierInputDataConfig.builder()
                 .s3Uri(s3Uri)
                 .build();

             CreateDocumentClassifierRequest createDocumentClassifierRequest = CreateDocumentClassifierRequest.builder()
                 .documentClassifierName(documentClassifierName)
                 .dataAccessRoleArn(dataAccessRoleArn)
                 .languageCode("en")
                 .inputDataConfig(config)
                 .build();

             CreateDocumentClassifierResponse createDocumentClassifierResult = comClient.createDocumentClassifier(createDocumentClassifierRequest);
             String documentClassifierArn = createDocumentClassifierResult.documentClassifierArn();
             System.out.println("Document Classifier ARN: " + documentClassifierArn);

         } catch (ComprehendException e) {
             System.err.println(e.awsErrorDetails().errorMessage());
             System.exit(1);
         }
     }
```
+  For API details, see [CreateDocumentClassifier](https://docs.aws.amazon.com/goto/SdkForJavaV2/comprehend-2017-11-27/CreateDocumentClassifier) in *AWS SDK for Java 2\.x API Reference*\. 

### Detect entities in a document<a name="comprehend_DetectEntities_java_topic"></a>

The following code example shows how to detect entities in a document with Amazon Comprehend\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/comprehend#readme)\. 
  

```
    public static void detectAllEntities(ComprehendClient comClient,String text ) {

        try {
            DetectEntitiesRequest detectEntitiesRequest = DetectEntitiesRequest.builder()
                .text(text)
                .languageCode("en")
                .build();

            DetectEntitiesResponse detectEntitiesResult = comClient.detectEntities(detectEntitiesRequest);
            List<Entity> entList = detectEntitiesResult.entities();
            for (Entity entity : entList) {
                System.out.println("Entity text is " + entity.text());
            }

        } catch (ComprehendException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DetectEntities](https://docs.aws.amazon.com/goto/SdkForJavaV2/comprehend-2017-11-27/DetectEntities) in *AWS SDK for Java 2\.x API Reference*\. 

### Detect key phrases in a document<a name="comprehend_DetectKeyPhrases_java_topic"></a>

The following code example shows how to detect key phrases in a document with Amazon Comprehend\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/comprehend#readme)\. 
  

```
    public static void detectAllKeyPhrases(ComprehendClient comClient, String text) {

        try {
            DetectKeyPhrasesRequest detectKeyPhrasesRequest = DetectKeyPhrasesRequest.builder()
                .text(text)
                .languageCode("en")
                .build();

            DetectKeyPhrasesResponse detectKeyPhrasesResult = comClient.detectKeyPhrases(detectKeyPhrasesRequest);
            List<KeyPhrase> phraseList = detectKeyPhrasesResult.keyPhrases();
            for (KeyPhrase keyPhrase : phraseList) {
                System.out.println("Key phrase text is " + keyPhrase.text());
            }

        } catch (ComprehendException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DetectKeyPhrases](https://docs.aws.amazon.com/goto/SdkForJavaV2/comprehend-2017-11-27/DetectKeyPhrases) in *AWS SDK for Java 2\.x API Reference*\. 

### Detect syntactical elements of a document<a name="comprehend_DetectSyntax_java_topic"></a>

The following code example shows how to detect syntactial elements of a document with Amazon Comprehend\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/comprehend#readme)\. 
  

```
    public static void detectAllSyntax(ComprehendClient comClient, String text){

        try {
            DetectSyntaxRequest detectSyntaxRequest = DetectSyntaxRequest.builder()
                .text(text)
                .languageCode("en")
                .build();

            DetectSyntaxResponse detectSyntaxResult = comClient.detectSyntax(detectSyntaxRequest);
            List<SyntaxToken> syntaxTokens = detectSyntaxResult.syntaxTokens();
            for (SyntaxToken token : syntaxTokens) {
                System.out.println("Language is " + token.text());
                System.out.println("Part of speech is " + token.partOfSpeech().tagAsString());
            }

        } catch (ComprehendException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DetectSyntax](https://docs.aws.amazon.com/goto/SdkForJavaV2/comprehend-2017-11-27/DetectSyntax) in *AWS SDK for Java 2\.x API Reference*\. 

### Detect the dominant language in a document<a name="comprehend_DetectDominantLanguage_java_topic"></a>

The following code example shows how to detect the dominant language in a document with Amazon Comprehend\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/comprehend#readme)\. 
  

```
    public static void detectTheDominantLanguage(ComprehendClient comClient, String text){

        try {
            DetectDominantLanguageRequest request = DetectDominantLanguageRequest.builder()
                .text(text)
                .build();

            DetectDominantLanguageResponse resp = comClient.detectDominantLanguage(request);
            List<DominantLanguage> allLanList = resp.languages();
            for (DominantLanguage lang : allLanList) {
                System.out.println("Language is " + lang.languageCode());
            }

        } catch (ComprehendException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DetectDominantLanguage](https://docs.aws.amazon.com/goto/SdkForJavaV2/comprehend-2017-11-27/DetectDominantLanguage) in *AWS SDK for Java 2\.x API Reference*\. 

### Detect the sentiment of a document<a name="comprehend_DetectSentiment_java_topic"></a>

The following code example shows how to detect the sentiment of a document with Amazon Comprehend\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/comprehend#readme)\. 
  

```
    public static void detectSentiments(ComprehendClient comClient, String text){

        try {
            DetectSentimentRequest detectSentimentRequest = DetectSentimentRequest.builder()
                .text(text)
                .languageCode("en")
                .build();

            DetectSentimentResponse detectSentimentResult = comClient.detectSentiment(detectSentimentRequest);
            System.out.println("The Neutral value is " +detectSentimentResult.sentimentScore().neutral() );

        } catch (ComprehendException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DetectSentiment](https://docs.aws.amazon.com/goto/SdkForJavaV2/comprehend-2017-11-27/DetectSentiment) in *AWS SDK for Java 2\.x API Reference*\. 