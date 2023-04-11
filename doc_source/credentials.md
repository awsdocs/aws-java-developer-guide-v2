# Provide temporary credentials to the SDK<a name="credentials"></a>

Before making a request to Amazon Web Services using the AWS SDK for Java 2\.x, the SDK cryptographically signs temporary credentials issued by AWS\. To access temporary credentials, the SDK retrieves configuration values by checking several locations\.

This topic discusses several ways that you enable to SDK to access temporary credentials\.

**Topics**
+ [Use temporary credentials](credentials-temporary.md)
+ [Default credentials provider chain](credentials-chain.md)
+ [Use a specific credentials provider or provider chain](credentials-specify.md)
+ [Use profiles](credentials-profiles.md)
+ [Load temporary credentials from an external process](credentials-process.md)
+ [Supply temporary credentials in code](credentials-explicit.md)
+ [Configuring IAM roles for Amazon EC2](ec2-iam-roles.md)