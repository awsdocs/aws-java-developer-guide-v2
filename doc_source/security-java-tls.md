--------

You can now use the [Amazon S3 Transfer Manager \(Developer Preview\)](https://bit.ly/2WQebiP) in the AWS SDK for Java 2\.x for accelerated file transfers\. Give it a try and [let us know what you think](https://bit.ly/3zT1YYM)\!

--------

# AWS SDK for Java support for TLS<a name="security-java-tls"></a>

The following information applies only to Java SSL implementation \(the default SSL implementation in the AWS SDK for Java\)\. If you’re using a different SSL implementation, see your specific SSL implementation to learn how to enforce TLS versions\.

## How to check the TLS version<a name="how-to-check-the-tls-version"></a>

Consult your Java virtual machine \(JVM\) provider's documentation to determine which TLS versions are supported on your platform\. For some JVMs, the following code will print which SSL versions are supported\.

```
System.out.println(Arrays.toString(SSLContext.getDefault().getSupportedSSLParameters().getProtocols()));
```

To see the SSL handshake in action and what version of TLS is used, you can use the system property **javax\.net\.debug**\.

```
java app.jar -Djavax.net.debug=ssl
```

## Enforcing a minimum TLS version<a name="enforce-minimum-tls-version"></a>

 The SDK always prefers the latest TLS version supported by the platform and service\. If you wish to enforce a specific minimum TLS version, consult your JVM’s documentation\.

 For OpenJDK\-based JVMs, you can use the system property `jdk.tls.client.protocols`\.

```
java app.jar -Djdk.tls.client.protocols=PROTOCOLS
```

 Consult your JVM's documentation for the supported values of PROTOCOLS\. 