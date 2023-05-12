# Route 53 domain registration examples using SDK for Java 2\.x<a name="java_route53-domains_code_examples"></a>

The following code examples show you how to perform actions and implement common scenarios by using the AWS SDK for Java 2\.x with Route 53 domain registration\.

*Actions* are code excerpts that show you how to call individual service functions\.

*Scenarios* are code examples that show you how to accomplish a specific task by calling multiple functions within the same service\.

Each example includes a link to GitHub, where you can find instructions on how to set up and run the code in context\.

**Get started**

## Hello Route 53 domain registration<a name="example_route-53_Hello_section"></a>

The following code examples show how to get started using Route 53 domain registration\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/route53#readme)\. 
  

```
/**
 * Before running this Java V2 code example, set up your development environment, including your credentials.
 *
 * For more information, see the following documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 *
 * This Java code example performs the following operation:
 *
 * 1. Invokes ListPrices for at least one domain type, such as the “com” type, and displays prices for registration and renewal.
 *
 */
public class Route53Hello {
    public static final String DASHES = new String(new char[80]).replace("\0", "-");
    public static void main(String[] args) {
        final String usage = "\n" +
            "Usage:\n" +
            "    <domainType> \n\n" +
            "Where:\n" +
            "    domainType - The domain type (for example, com). \n";

        if (args.length != 1) {
            System.out.println(usage);
            System.exit(1);
        }

        String domainType = args[0];
        Region region = Region.US_EAST_1;
        Route53DomainsClient route53DomainsClient = Route53DomainsClient.builder()
            .region(region)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

        System.out.println(DASHES);
        System.out.println("Invokes ListPrices for at least one domain type.");
        listPrices(route53DomainsClient, domainType);
        System.out.println(DASHES);
    }

    public static void listPrices(Route53DomainsClient route53DomainsClient, String domainType) {
        try {
            ListPricesRequest pricesRequest = ListPricesRequest.builder()
                .maxItems(10)
                .tld(domainType)
                .build();

            ListPricesResponse response = route53DomainsClient.listPrices(pricesRequest);
            List<DomainPrice> prices = response.prices();
            for (DomainPrice pr: prices) {
                System.out.println("Name: "+pr.name());
                System.out.println("Registration: "+pr.registrationPrice().price() + " " +pr.registrationPrice().currency());
                System.out.println("Renewal: "+pr.renewalPrice().price() + " " +pr.renewalPrice().currency());
                System.out.println("Transfer: "+pr.transferPrice().price() + " " +pr.transferPrice().currency());
                System.out.println("Change Ownership: "+pr.changeOwnershipPrice().price() + " " +pr.changeOwnershipPrice().currency());
                System.out.println("Restoration: "+pr.restorationPrice().price() + " " +pr.restorationPrice().currency());
                System.out.println(" ");
            }

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
```
+  For API details, see [ListPrices](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/ListPrices) in *AWS SDK for Java 2\.x API Reference*\. 

**Topics**
+ [Actions](#actions)
+ [Scenarios](#scenarios)

## Actions<a name="actions"></a>

### Check domain availability<a name="route-53_CheckDomainAvailability_java_topic"></a>

The following code example shows how to check the availability of a domain\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/route53#readme)\. 
  

```
    public static void checkDomainAvailability(Route53DomainsClient route53DomainsClient, String domainSuggestion) {
        try {
            CheckDomainAvailabilityRequest availabilityRequest = CheckDomainAvailabilityRequest.builder()
                .domainName(domainSuggestion)
                .build();

            CheckDomainAvailabilityResponse response = route53DomainsClient.checkDomainAvailability(availabilityRequest);
            System.out.println(domainSuggestion +" is "+response.availability().toString());

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [CheckDomainAvailability](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/CheckDomainAvailability) in *AWS SDK for Java 2\.x API Reference*\. 

### Check domain transferability<a name="route-53_CheckDomainTransferability_java_topic"></a>

The following code example shows how to check the transferability of a domain\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/route53#readme)\. 
  

```
    public static void checkDomainTransferability(Route53DomainsClient route53DomainsClient, String domainSuggestion){
        try {
            CheckDomainTransferabilityRequest transferabilityRequest = CheckDomainTransferabilityRequest.builder()
                .domainName(domainSuggestion)
                .build();

            CheckDomainTransferabilityResponse response = route53DomainsClient.checkDomainTransferability(transferabilityRequest);
            System.out.println("Transferability: "+response.transferability().transferable().toString());

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [CheckDomainTransferability](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/CheckDomainTransferability) in *AWS SDK for Java 2\.x API Reference*\. 

### Get domain details<a name="route-53_GetDomainDetails_java_topic"></a>

The following code example shows how to get the details for a domain\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/route53#readme)\. 
  

```
    public static void getDomainDetails(Route53DomainsClient route53DomainsClient, String domainSuggestion){
        try {
            GetDomainDetailRequest detailRequest = GetDomainDetailRequest.builder()
                .domainName(domainSuggestion)
                .build();

            GetDomainDetailResponse response = route53DomainsClient.getDomainDetail(detailRequest);
            System.out.println("The contact first name is " + response.registrantContact().firstName());
            System.out.println("The contact last name is " + response.registrantContact().lastName());
            System.out.println("The contact org name is " + response.registrantContact().organizationName());

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [GetDomainDetail](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/GetDomainDetail) in *AWS SDK for Java 2\.x API Reference*\. 

### Get operation details<a name="route-53_GetOperationDetails_java_topic"></a>

The following code example shows how to get details on an operation\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/route53#readme)\. 
  

```
    public static void getOperationalDetail(Route53DomainsClient route53DomainsClient, String operationId) {
        try {
            GetOperationDetailRequest detailRequest = GetOperationDetailRequest.builder()
                .operationId(operationId)
                .build();

            GetOperationDetailResponse response = route53DomainsClient.getOperationDetail(detailRequest);
            System.out.println("Operation detail message is "+response.message());

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [GetOperationDetail](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/GetOperationDetail) in *AWS SDK for Java 2\.x API Reference*\. 

### Get suggested domain names<a name="route-53_GetDomainSuggestions_java_topic"></a>

The following code example shows how to get domain name suggestions\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/route53#readme)\. 
  

```
    public static void listDomainSuggestions(Route53DomainsClient route53DomainsClient, String domainSuggestion) {
        try {
            GetDomainSuggestionsRequest suggestionsRequest = GetDomainSuggestionsRequest.builder()
                .domainName(domainSuggestion)
                .suggestionCount(5)
                .onlyAvailable(true)
                .build();

            GetDomainSuggestionsResponse response = route53DomainsClient.getDomainSuggestions(suggestionsRequest);
            List<DomainSuggestion> suggestions = response.suggestionsList();
            for (DomainSuggestion suggestion: suggestions) {
                System.out.println("Suggestion Name: "+suggestion.domainName());
                System.out.println("Availability: "+suggestion.availability());
                System.out.println(" ");
            }

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [GetDomainSuggestions](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/GetDomainSuggestions) in *AWS SDK for Java 2\.x API Reference*\. 

### List domain prices<a name="route-53_ListPrices_java_topic"></a>

The following code example shows how to list domain prices\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/route53#readme)\. 
  

```
    public static void listPrices(Route53DomainsClient route53DomainsClient, String domainType) {
        try {
            ListPricesRequest pricesRequest = ListPricesRequest.builder()
                .tld(domainType)
                .build();

            ListPricesIterable listRes = route53DomainsClient.listPricesPaginator(pricesRequest);
            listRes.stream()
                .flatMap(r -> r.prices().stream())
                .forEach(content -> System.out.println(" Name: " + content.name() +
                    " Registration: " + content.registrationPrice().price() + " " + content.registrationPrice().currency() +
                    " Renewal: "+ content.renewalPrice().price() + " " + content.renewalPrice().currency() ));

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListPrices](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/ListPrices) in *AWS SDK for Java 2\.x API Reference*\. 

### List domains<a name="route-53_ListDomains_java_topic"></a>

The following code example shows how to list the registered domains\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/route53#readme)\. 
  

```
    public static void listDomains(Route53DomainsClient route53DomainsClient) {
        try {
            ListDomainsIterable listRes = route53DomainsClient.listDomainsPaginator();
            listRes.stream()
                .flatMap(r -> r.domains().stream())
                .forEach(content -> System.out.println("The domain name is " + content.domainName()));

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListDomains](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/ListDomains) in *AWS SDK for Java 2\.x API Reference*\. 

### List operations<a name="route-53_ListOperations_java_topic"></a>

The following code example shows how to list operations\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/route53#readme)\. 
  

```
    public static void listOperations(Route53DomainsClient route53DomainsClient) {
        try {
            Date currentDate = new Date();
            LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            ZoneOffset zoneOffset = ZoneOffset.of("+01:00");
            localDateTime = localDateTime.minusYears(1);
            Instant myTime = localDateTime.toInstant(zoneOffset);

            ListOperationsRequest operationsRequest = ListOperationsRequest.builder()
                .submittedSince(myTime)
                .build();

            ListOperationsIterable listRes = route53DomainsClient.listOperationsPaginator(operationsRequest);
            listRes.stream()
                .flatMap(r -> r.operations().stream())
                .forEach(content -> System.out.println(" Operation Id: " + content.operationId() +
                    " Status: " + content.statusAsString() +
                    " Date: "+content.submittedDate()));


        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ListOperations](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/ListOperations) in *AWS SDK for Java 2\.x API Reference*\. 

### Register a domain<a name="route-53_RegisterDomain_java_topic"></a>

The following code example shows how to register a domain\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/route53#readme)\. 
  

```
    public static String requestDomainRegistration(Route53DomainsClient route53DomainsClient,
                                                   String domainSuggestion,
                                                   String phoneNumber,
                                                   String email,
                                                   String firstName,
                                                   String lastName,
                                                   String city) {

        try {
            ContactDetail contactDetail = ContactDetail.builder()
                .contactType(ContactType.COMPANY)
                .state("LA")
                .countryCode(CountryCode.IN)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .city(city)
                .phoneNumber(phoneNumber)
                .organizationName("My Org")
                .addressLine1("My Address")
                .zipCode("123 123")
                .build();

            RegisterDomainRequest domainRequest = RegisterDomainRequest.builder()
                .adminContact(contactDetail)
                .registrantContact(contactDetail)
                .techContact(contactDetail)
                .domainName(domainSuggestion)
                .autoRenew(true)
                .durationInYears(1)
                .build();

            RegisterDomainResponse response = route53DomainsClient.registerDomain(domainRequest);
            System.out.println("Registration requested. Operation Id: " +response.operationId());
            return response.operationId();

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }
```
+  For API details, see [RegisterDomain](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/RegisterDomain) in *AWS SDK for Java 2\.x API Reference*\. 

### View billing<a name="route-53_ViewBilling_java_topic"></a>

The following code example shows how to view billing records\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/route53#readme)\. 
  

```
    public static void listBillingRecords(Route53DomainsClient route53DomainsClient) {
        try {
            Date currentDate = new Date();
            LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            ZoneOffset zoneOffset = ZoneOffset.of("+01:00");
            LocalDateTime localDateTime2 = localDateTime.minusYears(1);
            Instant myStartTime = localDateTime2.toInstant(zoneOffset);
            Instant myEndTime = localDateTime.toInstant(zoneOffset);

            ViewBillingRequest viewBillingRequest = ViewBillingRequest.builder()
                .start(myStartTime)
                .end(myEndTime)
                .build();

            ViewBillingIterable listRes = route53DomainsClient.viewBillingPaginator(viewBillingRequest);
            listRes.stream()
                .flatMap(r -> r.billingRecords().stream())
                .forEach(content -> System.out.println(" Bill Date:: " + content.billDate() +
                    " Operation: " + content.operationAsString() +
                    " Price: "+content.price()));

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
```
+  For API details, see [ViewBilling](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/ViewBilling) in *AWS SDK for Java 2\.x API Reference*\. 

## Scenarios<a name="scenarios"></a>

### Get started with domains<a name="route-53_Scenario_GetStartedRoute53Domains_java_topic"></a>

The following code example shows how to:
+ List current domains, and list operations in the past year\.
+ View billing for the past year, and view prices for domain types\.
+ Get domain suggestions\.
+ Check domain availability and transferability\.
+ Optionally, request a domain registration\.
+ Get an operation detail\.
+ Optionally, get a domain detail\.

**SDK for Java 2\.x**  
 There's more on GitHub\. Find the complete example and learn how to set up and run in the [AWS Code Examples Repository](https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/route53#readme)\. 
  

```
/**
 * Before running this Java V2 code example, set up your development environment, including your credentials.
 *
 * For more information, see the following documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 *
 * This example uses pagination methods where applicable. For example, to list domains, the
 * listDomainsPaginator method is used. For more information about pagination,
 * see the following documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/pagination.html
 *
 * This Java code example performs the following operations:
 *
 * 1. List current domains.
 * 2. List operations in the past year.
 * 3. View billing for the account in the past year.
 * 4. View prices for domain types.
 * 5. Get domain suggestions.
 * 6. Check domain availability.
 * 7. Check domain transferability.
 * 8. Request a domain registration.
 * 9. Get operation details.
 * 10. Optionally, get domain details.
 */

public class Route53Scenario {
    public static final String DASHES = new String(new char[80]).replace("\0", "-");
    public static void main(String[] args) {
        final String usage = "\n" +
            "Usage:\n" +
            "    <domainType> <phoneNumber> <email> <domainSuggestion> <firstName> <lastName> <city>\n\n" +
            "Where:\n" +
            "    domainType - The domain type (for example, com). \n" +
            "    phoneNumber - The phone number to use (for example, +91.9966564xxx)  "+
            "    email - The email address to use.  "+
            "    domainSuggestion - The domain suggestion (for example, findmy.accountants). \n" +
            "    firstName - The first name to use to register a domain. \n" +
            "    lastName -  The last name to use to register a domain. \n" +
            "    city - the city to use to register a domain. ";

        if (args.length != 7) {
            System.out.println(usage);
            System.exit(1);
        }

        String domainType = args[0];
        String phoneNumber = args[1];
        String email = args[2] ;
        String domainSuggestion = args[3] ;
        String firstName = args[4] ;
        String lastName = args[5] ;
        String city = args[6] ;
        Region region = Region.US_EAST_1;
        Route53DomainsClient route53DomainsClient = Route53DomainsClient.builder()
            .region(region)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

        System.out.println(DASHES);
        System.out.println("Welcome to the Amazon Route 53 domains example scenario.");
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("1. List current domains.");
        listDomains(route53DomainsClient);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("2. List operations in the past year.");
        listOperations(route53DomainsClient);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("3. View billing for the account in the past year.");
        listBillingRecords(route53DomainsClient);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("4. View prices for domain types.");
        listPrices(route53DomainsClient, domainType);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("5. Get domain suggestions.");
        listDomainSuggestions(route53DomainsClient, domainSuggestion);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("6. Check domain availability.");
        checkDomainAvailability(route53DomainsClient, domainSuggestion);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("7. Check domain transferability.");
        checkDomainTransferability(route53DomainsClient, domainSuggestion);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("8. Request a domain registration.");
        String opId = requestDomainRegistration(route53DomainsClient, domainSuggestion, phoneNumber, email, firstName, lastName, city);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("9. Get operation details.");
        getOperationalDetail(route53DomainsClient, opId);
        System.out.println(DASHES);

        System.out.println(DASHES);
        System.out.println("10. Get domain details.");
        System.out.println("Note: You must have a registered domain to get details.");
        System.out.println("Otherwise, an exception is thrown that states " );
        System.out.println("Domain xxxxxxx not found in xxxxxxx account.");
        getDomainDetails(route53DomainsClient, domainSuggestion);
        System.out.println(DASHES);
    }

    public static void getDomainDetails(Route53DomainsClient route53DomainsClient, String domainSuggestion){
        try {
            GetDomainDetailRequest detailRequest = GetDomainDetailRequest.builder()
                .domainName(domainSuggestion)
                .build();

            GetDomainDetailResponse response = route53DomainsClient.getDomainDetail(detailRequest);
            System.out.println("The contact first name is " + response.registrantContact().firstName());
            System.out.println("The contact last name is " + response.registrantContact().lastName());
            System.out.println("The contact org name is " + response.registrantContact().organizationName());

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void getOperationalDetail(Route53DomainsClient route53DomainsClient, String operationId) {
        try {
            GetOperationDetailRequest detailRequest = GetOperationDetailRequest.builder()
                .operationId(operationId)
                .build();

            GetOperationDetailResponse response = route53DomainsClient.getOperationDetail(detailRequest);
            System.out.println("Operation detail message is "+response.message());

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static String requestDomainRegistration(Route53DomainsClient route53DomainsClient,
                                                   String domainSuggestion,
                                                   String phoneNumber,
                                                   String email,
                                                   String firstName,
                                                   String lastName,
                                                   String city) {

        try {
            ContactDetail contactDetail = ContactDetail.builder()
                .contactType(ContactType.COMPANY)
                .state("LA")
                .countryCode(CountryCode.IN)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .city(city)
                .phoneNumber(phoneNumber)
                .organizationName("My Org")
                .addressLine1("My Address")
                .zipCode("123 123")
                .build();

            RegisterDomainRequest domainRequest = RegisterDomainRequest.builder()
                .adminContact(contactDetail)
                .registrantContact(contactDetail)
                .techContact(contactDetail)
                .domainName(domainSuggestion)
                .autoRenew(true)
                .durationInYears(1)
                .build();

            RegisterDomainResponse response = route53DomainsClient.registerDomain(domainRequest);
            System.out.println("Registration requested. Operation Id: " +response.operationId());
            return response.operationId();

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }

    public static void checkDomainTransferability(Route53DomainsClient route53DomainsClient, String domainSuggestion){
        try {
            CheckDomainTransferabilityRequest transferabilityRequest = CheckDomainTransferabilityRequest.builder()
                .domainName(domainSuggestion)
                .build();

            CheckDomainTransferabilityResponse response = route53DomainsClient.checkDomainTransferability(transferabilityRequest);
            System.out.println("Transferability: "+response.transferability().transferable().toString());

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void checkDomainAvailability(Route53DomainsClient route53DomainsClient, String domainSuggestion) {
        try {
            CheckDomainAvailabilityRequest availabilityRequest = CheckDomainAvailabilityRequest.builder()
                .domainName(domainSuggestion)
                .build();

            CheckDomainAvailabilityResponse response = route53DomainsClient.checkDomainAvailability(availabilityRequest);
            System.out.println(domainSuggestion +" is "+response.availability().toString());

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void listDomainSuggestions(Route53DomainsClient route53DomainsClient, String domainSuggestion) {
        try {
            GetDomainSuggestionsRequest suggestionsRequest = GetDomainSuggestionsRequest.builder()
                .domainName(domainSuggestion)
                .suggestionCount(5)
                .onlyAvailable(true)
                .build();

            GetDomainSuggestionsResponse response = route53DomainsClient.getDomainSuggestions(suggestionsRequest);
            List<DomainSuggestion> suggestions = response.suggestionsList();
            for (DomainSuggestion suggestion: suggestions) {
                System.out.println("Suggestion Name: "+suggestion.domainName());
                System.out.println("Availability: "+suggestion.availability());
                System.out.println(" ");
            }

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void listPrices(Route53DomainsClient route53DomainsClient, String domainType) {
        try {
            ListPricesRequest pricesRequest = ListPricesRequest.builder()
                .tld(domainType)
                .build();

            ListPricesIterable listRes = route53DomainsClient.listPricesPaginator(pricesRequest);
            listRes.stream()
                .flatMap(r -> r.prices().stream())
                .forEach(content -> System.out.println(" Name: " + content.name() +
                    " Registration: " + content.registrationPrice().price() + " " + content.registrationPrice().currency() +
                    " Renewal: "+ content.renewalPrice().price() + " " + content.renewalPrice().currency() ));

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void listBillingRecords(Route53DomainsClient route53DomainsClient) {
        try {
            Date currentDate = new Date();
            LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            ZoneOffset zoneOffset = ZoneOffset.of("+01:00");
            LocalDateTime localDateTime2 = localDateTime.minusYears(1);
            Instant myStartTime = localDateTime2.toInstant(zoneOffset);
            Instant myEndTime = localDateTime.toInstant(zoneOffset);

            ViewBillingRequest viewBillingRequest = ViewBillingRequest.builder()
                .start(myStartTime)
                .end(myEndTime)
                .build();

            ViewBillingIterable listRes = route53DomainsClient.viewBillingPaginator(viewBillingRequest);
            listRes.stream()
                .flatMap(r -> r.billingRecords().stream())
                .forEach(content -> System.out.println(" Bill Date:: " + content.billDate() +
                    " Operation: " + content.operationAsString() +
                    " Price: "+content.price()));

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void listOperations(Route53DomainsClient route53DomainsClient) {
        try {
            Date currentDate = new Date();
            LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            ZoneOffset zoneOffset = ZoneOffset.of("+01:00");
            localDateTime = localDateTime.minusYears(1);
            Instant myTime = localDateTime.toInstant(zoneOffset);

            ListOperationsRequest operationsRequest = ListOperationsRequest.builder()
                .submittedSince(myTime)
                .build();

            ListOperationsIterable listRes = route53DomainsClient.listOperationsPaginator(operationsRequest);
            listRes.stream()
                .flatMap(r -> r.operations().stream())
                .forEach(content -> System.out.println(" Operation Id: " + content.operationId() +
                    " Status: " + content.statusAsString() +
                    " Date: "+content.submittedDate()));


        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void listDomains(Route53DomainsClient route53DomainsClient) {
        try {
            ListDomainsIterable listRes = route53DomainsClient.listDomainsPaginator();
            listRes.stream()
                .flatMap(r -> r.domains().stream())
                .forEach(content -> System.out.println("The domain name is " + content.domainName()));

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
```
+ For API details, see the following topics in *AWS SDK for Java 2\.x API Reference*\.
  + [CheckDomainAvailability](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/CheckDomainAvailability)
  + [CheckDomainTransferability](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/CheckDomainTransferability)
  + [GetDomainDetail](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/GetDomainDetail)
  + [GetDomainSuggestions](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/GetDomainSuggestions)
  + [GetOperationDetail](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/GetOperationDetail)
  + [ListDomains](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/ListDomains)
  + [ListOperations](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/ListOperations)
  + [ListPrices](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/ListPrices)
  + [RegisterDomain](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/RegisterDomain)
  + [ViewBilling](https://docs.aws.amazon.com/goto/SdkForJavaV2/route53domains-2014-05-15/ViewBilling)