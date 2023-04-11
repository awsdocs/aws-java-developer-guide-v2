# Load temporary credentials from an external process<a name="credentials-process"></a>

**Warning**  
The following describes a method of sourcing temporary credentials from an external process\. This can potentially be dangerous, so proceed with caution\. Other credential providers should be preferred if at all possible\. If using this option, you should make sure that the `config` file is as locked down as possible using security best practices for your operating system\.   
Make sure that your custom credentials tool does not write any secret information to `StdErr`\. SDKs and AWS CLI can capture and log such information, potentially exposing it to unauthorized users\.

With the SDK for Java 2\.x, you can acquire temporary credentials from an external process for custom use cases\. There are two ways to configure this functionality\.

## Use the `credential_process` setting<a name="credentials-credential_process"></a>

If you have a method that provides temporary credentials, you can integrate it by adding the `credential_process` setting as part of a profile definition in the `config` file\. The value you specify must use the full path to the command file\. If the file path contains any spaces, you must surround it with quotation marks\.

The SDK calls the command exactly as given and then reads JSON data from `stdout`\. 

The following examples show the use of this setting for file paths without spaces and file paths with spaces\.

------
#### [ Linux/macOS ]

**No spaces in file path**  

```
[profile process-credential-profile]
credential_process = /path/to/credential/file/credential_file.sh --custom-command custom_parameter
```

**Spaces in file path**  

```
[profile process-credential-profile]
credential_process = "/path/with/space to/credential/file/credential_file.sh" --custom-command custom_parameter
```

------
#### [ Windows ]

**No spaces in file path**  

```
[profile process-credential-profile]
credential_process = C:\Path\To\credentials.cmd --custom_command custom_parameter
```

**Spaces in file path**  

```
[profile process-credential-profile]
credential_process = "C:\Path\With Space To\credentials.cmd" --custom_command custom_parameter
```

------

The following code snippet demonstrates how to build a service client that uses the temporary credentials defined as part of the profile named `process-credential-profile`\.

```
Region region = Region.US_WEST_2;
S3Client s3Client = S3Client.builder()
      .region(region)
      .credentialsProvider(ProfileCredentialsProvider.create("process-credential-profile"))
      .build();
```

For detailed information about using an external process as a source of temporary credentials, refer to the [process credentials section](https://docs.aws.amazon.com/sdkref/latest/guide/feature-process-credentials.html) in the AWS SDKs and Tools Reference Guide\.

## Use the `ProcessCredentialsProvider`<a name="credentials-procredprovider"></a>

As an alternative to using settings in the `config` file, you can use the SDK's `[ProcessCredentialsProvider](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/auth/credentials/ProcessCredentialsProvider.html)` to load temporary credentials using Java\. 

The following examples show various versions of how to specify an external process using the `ProcessCredentialsProvider` and configuring a service client that uses the temporary credentials\.

------
#### [ Linux/macOS ]

**No spaces in file path**  

```
ProcessCredentialsProvider credentials = 
    ProcessCredentialsProvider
        .builder()
        .command("/path/to/credentials.sh optional_param1 optional_param2")
        .build();

S3Client s3 = S3Client.builder()
                      .region(Region.US_WEST_2)
                      .credentialsProvider(credentials)
                      .build();
```

**Spaces in file path**  

```
ProcessCredentialsProvider credentials = 
    ProcessCredentialsProvider
        .builder()
        .command("/path\\ with\\ spaces\\ to/credentials.sh optional_param1 optional_param2")
        .build();

S3Client s3 = S3Client.builder()
                      .region(Region.US_WEST_2)
                      .credentialsProvider(credentials)
                      .build();
```

------
#### [ Windows ]

**No spaces in file path**  

```
ProcessCredentialsProvider credentials = 
    ProcessCredentialsProvider
        .builder()
        .command("C:\\Path\\To\\credentials.exe optional_param1 optional_param2")
        .build();

S3Client s3 = S3Client.builder()
                      .region(Region.US_WEST_2)
                      .credentialsProvider(credentials)
                      .build();
```

**Spaces in file path**  

```
ProcessCredentialsProvider credentials = 
    ProcessCredentialsProvider
        .builder()
        .command("\"C:\\Path\\With Spaces To\\credentials.exe\" optional_param1 optional_param2")
        .build();

S3Client s3 = S3Client.builder()
                      .region(Region.US_WEST_2)
                      .credentialsProvider(credentials)
                      .build();
```

------