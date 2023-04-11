# Working with TLS in the SDK for Java<a name="security-java-tls"></a>

The AWS SDK for Java uses the TLS capabilities of its underlying Java platform\. In this topic, we show examples using the OpenJDK implementation used by [Amazon Corretto 18](https://alpha.www.docs.aws.a2z.com/corretto/latest/corretto-18-ug/what-is-corretto-18.html)\. 

Users should consult the documentation of the the Java platform they are using with the SDK to find out which TLS versions are enabled by default as well as how to enable and disable specific TLS versions\.

## How to check TLS version information<a name="how-to-check-the-tls-version"></a>

Using OpenJDK, the following code shows the use of [SSLContext](https://devdocs.io/openjdk%7E18/java.base/javax/net/ssl/sslcontext#getSupportedSSLParameters()) to print which TLS/SSL versions are supported\.

```
System.out.println(Arrays.toString(SSLContext.getDefault().getSupportedSSLParameters().getProtocols()));
```

For example, Amazon Corretto 18 \(OpenJDK\) produces the following output\.

```
[TLSv1.3, TLSv1.2, TLSv1.1, TLSv1, SSLv3, SSLv2Hello]
```

To see the SSL handshake in action and what version of TLS is used, you can use the system property **javax\.net\.debug**\.

For example, run a Java applications that uses TLS\.

```
java app.jar -Djavax.net.debug=ssl:handshake
```

The application logs the SSL handshake similar to the following\.

```
...
javax.net.ssl|DEBUG|10|main|2022-12-23 13:53:12.221 EST|ClientHello.java:641|Produced ClientHello handshake message (
"ClientHello": {
  "client version"      : "TLSv1.2",

...
javax.net.ssl|DEBUG|10|main|2022-12-23 13:53:12.295 EST|ServerHello.java:888|Consuming ServerHello handshake message (
"ServerHello": {
  "server version"      : "TLSv1.2",
...
```

## Enforce a minimum TLS version<a name="enforce-minimum-tls-version"></a>

The SDK for Java always prefers the latest TLS version supported by the platform and service\. If you wish to enforce a specific minimum TLS version, consult your Java platform’s documentation\.

For OpenJDK\-based JVMs, you can use the system property `jdk.tls.client.protocols`\.

For example, if you want SDK service clients in your application to use TLS 1\.2, even though TLS 1\.3 is available, provide the following system property\. 

```
java app.jar -Djdk.tls.client.protocols=TLSv1.2
```

## AWS API endpoints upgrade to TLS 1\.2<a name="tls-more-info"></a>

See this [blog post](http://aws.amazon.com/blogs/security/tls-1-2-required-for-aws-endpoints/) for information about AWS API endpoints moving to TLS 1\.2 for the minimum version\.

AWS Open Source Blog has [related information](http://aws.amazon.com/blogs/opensource/tls-1-0-1-1-changes-in-openjdk-and-amazon-corretto/) about continued support for TLS 1\.0 and TLS 1\.1 with OpenJDK and Amazon Corretto\.