# Step Functions examples using SDK for Java 2\.x<a name="java_sfn_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Step Functions\.

*Actions* are code excerpts that show you how to call individual service functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple functions within the same service\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Get started**

## Hello Step Functions<a name="example_sfn_Hello_section"></a>

The following code examples show how to get started using Step Functions\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
Java version of Hello\.  

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

**Topics**
+ [Actions](#actions)
+ [Scenarios](#scenarios)

## Actions<a name="actions"></a>

### Create a state machine<a name="sfn_CreateStateMachine_java_topic"></a>

The following code example shows how to create a Step Functions state machine\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

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

### Create an activity<a name="sfn_CreateActivity_java_topic"></a>

The following code example shows how to create a Step Functions activity\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

```
    public static String createActivity(SfnClient sfnClient, String activityName) {
        try {
            CreateActivityRequest activityRequest = CreateActivityRequest.builder()
                .name(activityName)
                .build();

            CreateActivityResponse response = sfnClient.createActivity(activityRequest);
            return response.activityArn();

        } catch (SfnException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [CreateActivity](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/CreateActivity) in *AWS SDK for Java 2\.x API Reference*\. 

### Delete a state machine<a name="sfn_DeleteStateMachine_java_topic"></a>

The following code example shows how to delete a Step Functions state machine\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

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

### Delete an activity<a name="sfn_DeleteActivity_java_topic"></a>

The following code example shows how to delete a Step Functions activity\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

```
    public static void deleteActivity(SfnClient sfnClient, String actArn) {
        try {
            DeleteActivityRequest activityRequest = DeleteActivityRequest.builder()
                .activityArn(actArn)
                .build();

            sfnClient.deleteActivity(activityRequest);
            System.out.println("You have deleted "+actArn);

        } catch (SfnException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DeleteActivity](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/DeleteActivity) in *AWS SDK for Java 2\.x API Reference*\. 

### Describe a state machine<a name="sfn_DescribeStateMachine_java_topic"></a>

The following code example shows how to describe a Step Functions state machine\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

```
    public static void describeStateMachine(SfnClient sfnClient, String stateMachineArn) {
        try {
            DescribeStateMachineRequest stateMachineRequest = DescribeStateMachineRequest.builder()
                .stateMachineArn(stateMachineArn)
                .build();

            DescribeStateMachineResponse response = sfnClient.describeStateMachine(stateMachineRequest);
            System.out.println("The name of the State machine is "+ response.name());
            System.out.println("The status of the State machine is "+ response.status());
            System.out.println("The ARN value of the State machine is "+ response.stateMachineArn());
            System.out.println("The role ARN value is "+ response.roleArn());

        } catch (SfnException e) {
            System.err.println(e.getMessage());
        }
    }
```
+  For API details, see [DescribeStateMachine](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/DescribeStateMachine) in *AWS SDK for Java 2\.x API Reference*\. 

### Describe a state machine run<a name="sfn_DescribeExecution_java_topic"></a>

The following code example shows how to describe a Step Functions state machine run\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

```
    public static void describeExe(SfnClient sfnClient, String executionArn) {
        try {
            DescribeExecutionRequest executionRequest = DescribeExecutionRequest.builder()
                .executionArn(executionArn)
                .build();

            String status = "";
            boolean hasSucceeded = false;
            while (!hasSucceeded) {
                DescribeExecutionResponse response = sfnClient.describeExecution(executionRequest);
                status = response.statusAsString();
                if (status.compareTo("RUNNING") ==0) {
                    System.out.println("The state machine is still running, let's wait for it to finish.");
                    Thread.sleep(2000);
                } else if (status.compareTo("SUCCEEDED") ==0) {
                    System.out.println("The Step Function workflow has succeeded");
                    hasSucceeded = true;
                } else {
                    System.out.println("The Status is neither running or succeeded");
                }
            }
            System.out.println("The Status is "+status);

        } catch (SfnException | InterruptedException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [DescribeExecution](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/DescribeExecution) in *AWS SDK for Java 2\.x API Reference*\. 

### Get task data for an activity<a name="sfn_GetActivityTask_java_topic"></a>

The following code example shows how to get task data for a Step Functions activity\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

```
    public static List<String> getActivityTask(SfnClient sfnClient, String actArn){
        List<String> myList = new ArrayList<>();
        GetActivityTaskRequest getActivityTaskRequest = GetActivityTaskRequest.builder()
            .activityArn(actArn)
            .build();

        GetActivityTaskResponse response = sfnClient.getActivityTask(getActivityTaskRequest);
        myList.add(response.taskToken());
        myList.add(response.input());
        return myList;
    }

    /// <summary>
    /// Stop execution of a Step Functions workflow.
    /// </summary>
    /// <param name="executionArn">The Amazon Resource Name (ARN) of
    /// the Step Functions execution to stop.</param>
    /// <returns>A Boolean value indicating the success of the action.</returns>
    public async Task<bool> StopExecution(string executionArn)
    {
        var response =
            await _amazonStepFunctions.StopExecutionAsync(new StopExecutionRequest { ExecutionArn = executionArn });
        return response.HttpStatusCode == System.Net.HttpStatusCode.OK;
    }
```
+  For API details, see [GetActivityTask](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/GetActivityTask) in *AWS SDK for Java 2\.x API Reference*\. 

### List activities<a name="sfn_ListActivities_java_topic"></a>

The following code example shows how to list Step Functions activities\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

```
    public static void listAllActivites(SfnClient sfnClient) {

        try {
            ListActivitiesRequest activitiesRequest = ListActivitiesRequest.builder()
                .maxResults(10)
                .build();

            ListActivitiesResponse response = sfnClient.listActivities(activitiesRequest);
            List<ActivityListItem> items = response.activities();
            for (ActivityListItem item: items) {
                System.out.println("The activity ARN is "+item.activityArn());
                System.out.println("The activity name is "+item.name());
            }

        } catch (SfnException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListActivities](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/ListActivities) in *AWS SDK for Java 2\.x API Reference*\. 

### List state machine runs<a name="sfn_ListExecutions_java_topic"></a>

The following code example shows how to list Step Functions state machine runs\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

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
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

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

### Send a success response to a task<a name="sfn_SendTaskSuccess_java_topic"></a>

The following code example shows how to send a success response to a Step Functions task\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

```
    public static void sendTaskSuccess(SfnClient sfnClient, String token, String json) {
        try {
            SendTaskSuccessRequest successRequest = SendTaskSuccessRequest.builder()
            .taskToken(token)
            .output(json)
            .build();

           sfnClient.sendTaskSuccess(successRequest);

        } catch (SfnException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [SendTaskSuccess](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/SendTaskSuccess) in *AWS SDK for Java 2\.x API Reference*\. 

### Start a state machine run<a name="sfn_StartExecution_java_topic"></a>

The following code example shows how to start a Step Functions state machine run\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

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

## Scenarios<a name="scenarios"></a>

### Get started with state machines<a name="sfn_Scenario_GetStartedStateMachines_java_topic"></a>

The following code example shows how to:
+ Create an activity\.
+ Create a state machine from an Amazon States Language definition that contains the previously created activity as a step\.
+ Run the state machine and respond to the activity with user input\.
+ Get the final status and output after the run completes, then clean up resources\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/stepfunctions#readme)\. 
  

```
/**
 * You can obtain the JSON file to create a state machine in the following GitHub location.
 *
 * https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/resources/sample_files
 *
 * To run this code example, place the chat_sfn_state_machine.json file into your project's resources folder.
 *
 * Also, set up your development environment, including your credentials.
 *
 * For information, see this documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 *
 * This Java code example performs the following tasks:
 *
 * 1. Creates an activity.
 * 2. Creates a state machine.
 * 3. Describes the state machine.
 * 4. Starts execution of the state machine and interacts with it.
 * 5. Describes the execution.
 * 6. Delete the activity.
 * 7. Deletes the state machine.
 */
public class StepFunctionsScenario {
    public static final String DASHES = new String(new char[80]).replace("\0", "-");
    public static void main(String[]args) throws Exception {
        final String usage = "\n" +
            "Usage:\n" +
            "    <roleARN> <activityName> <stateMachineName>\n\n" +
            "Where:\n" +
            "    roleName - The name of the IAM role to create for this state machine.\n" +
            "    activityName - The name of an activity to create." +
            "    stateMachineName - The name of the state machine to create.\n";

       if (args.length != 3) {
           System.out.println(usage);
           System.exit(1);
       }

        String roleName = args[0];
        String activityName = args[1];
        String stateMachineName = args[2];
        String polJSON = "{\n" +
            "    \"Version\": \"2012-10-17\",\n" +
            "    \"Statement\": [\n" +
            "        {\n" +
            "            \"Sid\": \"\",\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": {\n" +
            "                \"Service\": \"states.amazonaws.com\"\n" +
            "            },\n" +
            "            \"Action\": \"sts:AssumeRole\"\n" +
            "        }\n" +
            "    ]\n" +
            "}" ;

        Scanner sc = new Scanner(System.in);
        boolean action = false ;

        Region region = Region.US_EAST_1;
        SfnClient sfnClient = SfnClient.builder()
            .region(region)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

        Region regionGl = Region.AWS_GLOBAL;
        IamClient iam = IamClient.builder()
            .region(regionGl)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

        System.out.println(DASHES);
        System.out.println("Welcome to the AWS Step Functions example scenario.");
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("1. Create an activity.");
        String activityArn = createActivity(sfnClient, activityName);
        System.out.println("The ARN of the activity is "+activityArn);
        System.out.println(DASHES);

        // Get JSON to use for the state machine and place the activityArn value into it.
        InputStream input = StepFunctionsScenario.class.getClassLoader().getResourceAsStream("chat_sfn_state_machine.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readValue(input, JsonNode.class);
        String jsonString = mapper.writeValueAsString(jsonNode);

        // Modify the Resource node.
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonString);
        ((ObjectNode) root.path("States").path("GetInput")).put("Resource", activityArn);

        // Convert the modified Java object back to a JSON string.
        String stateDefinition = objectMapper.writeValueAsString(root);
        System.out.println(stateDefinition);

        System.out.println(DASHES);
        System.out.println("2. Create a state machine.");
        String roleARN = createIAMRole(iam, roleName, polJSON );
        String stateMachineArn = createMachine(sfnClient, roleARN, stateMachineName, stateDefinition);
        System.out.println("The ARN of the state machine is "+stateMachineArn);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("3. Describe the state machine.");
        describeStateMachine(sfnClient, stateMachineArn);
        System.out.println("What should ChatSFN call you?");
        String userName = sc.nextLine();
        System.out.println("Hello "+userName);
        System.out.println(DASHES);

        System.out.println(DASHES);
        // The JSON to pass to the StartExecution call.
        String executionJson = "{ \"name\" : \""+userName +"\" }";
        System.out.println(executionJson);
        System.out.println("4. Start execution of the state machine and interact with it.");
        String runArn = startWorkflow(sfnClient, stateMachineArn, executionJson);
        System.out.println("The ARN of the state machine execution is "+runArn);
        List<String> myList ;
        while (!action) {
            myList = getActivityTask(sfnClient, activityArn);
            System.out.println("ChatSFN: " + myList.get(1));
            System.out.println(userName + " please specify a value.");
            String myAction = sc.nextLine();
            if (myAction.compareTo("done") == 0)
                action = true;

            System.out.println("You have selected " + myAction);
            String taskJson = "{ \"action\" : \"" + myAction + "\" }";
            System.out.println(taskJson);
            sendTaskSuccess(sfnClient, myList.get(0), taskJson);
        }
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("5. Describe the execution.");
        describeExe(sfnClient, runArn);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("6. Delete the activity.");
        deleteActivity(sfnClient, activityArn);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("7. Delete the state machines.");
        deleteMachine(sfnClient, stateMachineArn);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("The AWS Step Functions example scenario is complete.");
        System.out.println(DASHES);
    }

    public static String createIAMRole(IamClient iam, String rolename, String polJSON ) {
        try {
            CreateRoleRequest request = CreateRoleRequest.builder()
                .roleName(rolename)
                .assumeRolePolicyDocument(polJSON)
                .description("Created using the AWS SDK for Java")
                .build();

            CreateRoleResponse response = iam.createRole(request);
            return response.role().arn();

        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }

    public static void describeExe(SfnClient sfnClient, String executionArn) {
        try {
            DescribeExecutionRequest executionRequest = DescribeExecutionRequest.builder()
                .executionArn(executionArn)
                .build();

            String status = "";
            boolean hasSucceeded = false;
            while (!hasSucceeded) {
                DescribeExecutionResponse response = sfnClient.describeExecution(executionRequest);
                status = response.statusAsString();
                if (status.compareTo("RUNNING") ==0) {
                    System.out.println("The state machine is still running, let's wait for it to finish.");
                    Thread.sleep(2000);
                } else if (status.compareTo("SUCCEEDED") ==0) {
                    System.out.println("The Step Function workflow has succeeded");
                    hasSucceeded = true;
                } else {
                    System.out.println("The Status is neither running or succeeded");
                }
            }
            System.out.println("The Status is "+status);

        } catch (SfnException | InterruptedException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void sendTaskSuccess(SfnClient sfnClient, String token, String json) {
        try {
            SendTaskSuccessRequest successRequest = SendTaskSuccessRequest.builder()
            .taskToken(token)
            .output(json)
            .build();

           sfnClient.sendTaskSuccess(successRequest);

        } catch (SfnException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static List<String> getActivityTask(SfnClient sfnClient, String actArn){
        List<String> myList = new ArrayList<>();
        GetActivityTaskRequest getActivityTaskRequest = GetActivityTaskRequest.builder()
            .activityArn(actArn)
            .build();

        GetActivityTaskResponse response = sfnClient.getActivityTask(getActivityTaskRequest);
        myList.add(response.taskToken());
        myList.add(response.input());
        return myList;
    }

    public static void deleteActivity(SfnClient sfnClient, String actArn) {
        try {
            DeleteActivityRequest activityRequest = DeleteActivityRequest.builder()
                .activityArn(actArn)
                .build();

            sfnClient.deleteActivity(activityRequest);
            System.out.println("You have deleted "+actArn);

        } catch (SfnException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void describeStateMachine(SfnClient sfnClient, String stateMachineArn) {
        try {
            DescribeStateMachineRequest stateMachineRequest = DescribeStateMachineRequest.builder()
                .stateMachineArn(stateMachineArn)
                .build();

            DescribeStateMachineResponse response = sfnClient.describeStateMachine(stateMachineRequest);
            System.out.println("The name of the State machine is "+ response.name());
            System.out.println("The status of the State machine is "+ response.status());
            System.out.println("The ARN value of the State machine is "+ response.stateMachineArn());
            System.out.println("The role ARN value is "+ response.roleArn());

        } catch (SfnException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void deleteMachine(SfnClient sfnClient, String stateMachineArn) {
        try {
            DeleteStateMachineRequest deleteStateMachineRequest = DeleteStateMachineRequest.builder()
                .stateMachineArn(stateMachineArn)
                .build();

            sfnClient.deleteStateMachine(deleteStateMachineRequest);
            DescribeStateMachineRequest describeStateMachine = DescribeStateMachineRequest.builder()
                .stateMachineArn(stateMachineArn)
                .build();

            while (true) {
                DescribeStateMachineResponse response = sfnClient.describeStateMachine(describeStateMachine);
                System.out.println("The state machine is not deleted yet. The status is "+response.status());
                Thread.sleep(3000);
            }

        } catch (SfnException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
        System.out.println(stateMachineArn +" was successfully deleted.");
    }

    public static String startWorkflow(SfnClient sfnClient, String stateMachineArn, String jsonEx) {
        UUID uuid = UUID.randomUUID();
        String uuidValue = uuid.toString();
        try {
            StartExecutionRequest executionRequest = StartExecutionRequest.builder()
                .input(jsonEx)
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

    public static String createMachine( SfnClient sfnClient, String roleARN, String stateMachineName, String json) {
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

    public static String createActivity(SfnClient sfnClient, String activityName) {
        try {
            CreateActivityRequest activityRequest = CreateActivityRequest.builder()
                .name(activityName)
                .build();

            CreateActivityResponse response = sfnClient.createActivity(activityRequest);
            return response.activityArn();

        } catch (SfnException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
}
```
+ For API details, see the following topics in *AWS SDK for Java 2\.x API Reference*\.
  + [CreateActivity](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/CreateActivity)
  + [CreateStateMachine](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/CreateStateMachine)
  + [DeleteActivity](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/DeleteActivity)
  + [DeleteStateMachine](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/DeleteStateMachine)
  + [DescribeExecution](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/DescribeExecution)
  + [DescribeStateMachine](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/DescribeStateMachine)
  + [GetActivityTask](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/GetActivityTask)
  + [ListActivities](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/ListActivities)
  + [ListStateMachines](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/ListStateMachines)
  + [SendTaskSuccess](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/SendTaskSuccess)
  + [StartExecution](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/StartExecution)
  + [StopExecution](https://docs.aws.amazon.com/goto/SdkForJavaV2/states-2016-11-23/StopExecution)