--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# Step Functions examples using SDK for Java 2\.x<a name="java_sfn_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Step Functions\.

*Actions* are code excerpts that show you how to call individual Step Functions functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple Step Functions functions\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Topics**
+ [Actions](#w620aac15c13b9c75c13)

## Actions<a name="w620aac15c13b9c75c13"></a>

### Create a state machine<a name="sfn_CreateStateMachine_java_topic"></a>

The following code example shows how to create a Step Functions state machine\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

```
    public static String createMachine( SfnClient sfnClient, String roleARN, String stateMachineName, String jsonFile) {

        String json = getJSONString(jsonFile);
        try {
           CreateStateMachineRequest machineRequest = CreateStateMachineRequest.builder()
               .definition(json)
               .name(stateMachineName)
               .roleArn(roleARN)
               .type(StateMachineType.STANDARD)
               .build();

           CreateStateMachineResponse response = sfnClient.createStateMachine(machineRequest);
           return response.stateMachineArn();

        } catch (SfnException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }

    private static String getJSONString(String path) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(new FileReader(path));//path to the JSON file.
            return data.toJSONString();

        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
```
+  For API details, see [CreateStateMachine](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/CreateStateMachine) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a state machine<a name="sfn_DeleteStateMachine_java_topic"></a>

The following code example shows how to delete a Step Functions state machine\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

```
    public static void deleteMachine(SfnClient sfnClient, String stateMachineArn) {

        try {
            DeleteStateMachineRequest deleteStateMachineRequest = DeleteStateMachineRequest.builder()
                .stateMachineArn(stateMachineArn)
                .build();

            sfnClient.deleteStateMachine(deleteStateMachineRequest);
            System.out.println(stateMachineArn +" was successfully deleted.");

        } catch (SfnException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteStateMachine](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/DeleteStateMachine) in *AWS SDK for Java 2\.x API Reference*\. 

### List state machine runs<a name="sfn_ListExecutions_java_topic"></a>

The following code example shows how to list Step Functions state machine runs\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

```
    public static void getExeHistory(SfnClient sfnClient, String exeARN) {
        try {
            GetExecutionHistoryRequest historyRequest = GetExecutionHistoryRequest.builder()
                .executionArn(exeARN)
                .maxResults(10)
                .build();

            GetExecutionHistoryResponse historyResponse = sfnClient.getExecutionHistory(historyRequest);
            List<HistoryEvent> events = historyResponse.events();
            for (HistoryEvent event: events) {
                System.out.println("The event type is "+event.type().toString());
            }

        } catch (SfnException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListExecutions](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/ListExecutions) in *AWS SDK for Java 2\.x API Reference*\. 

### List state machines<a name="sfn_ListStateMachines_java_topic"></a>

The following code example shows how to list Step Functions state machines\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

```
    public static void listMachines(SfnClient sfnClient) {

        try {
            ListStateMachinesResponse response = sfnClient.listStateMachines();
            List<StateMachineListItem> machines = response.stateMachines();
            for (StateMachineListItem machine :machines) {
                System.out.println("The name of the state machine is: "+machine.name());
                System.out.println("The ARN value is : "+machine.stateMachineArn());
            }

        } catch (SfnException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListStateMachines](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/ListStateMachines) in *AWS SDK for Java 2\.x API Reference*\. 

### Start a state machine run<a name="sfn_StartExecution_java_topic"></a>

The following code example shows how to start a Step Functions state machine run\.

**SDK for Java 2\.x**  
 To learn how to set up and run this example, see [GitHub](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

```
    public static String startWorkflow(SfnClient sfnClient, String stateMachineArn, String jsonFile) {

        String json = getJSONString(jsonFile);
        UUID uuid = UUID.randomUUID();
        String uuidValue = uuid.toString();
        try {

            StartExecutionRequest executionRequest = StartExecutionRequest.builder()
                .input(json)
                .stateMachineArn(stateMachineArn)
                .name(uuidValue)
                .build();

            StartExecutionResponse response = sfnClient.startExecution(executionRequest);
            return response.executionArn();

        } catch (SfnException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }

    private static String getJSONString(String path) {

        try {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(new FileReader(path));//path to the JSON file.
            String json = data.toJSONString();
            return json;

        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
```
+  For API details, see [StartExecution](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/StartExecution) in *AWS SDK for Java 2\.x API Reference*\. 