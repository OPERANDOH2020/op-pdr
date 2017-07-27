# op-pdr
For the modules in the Private Data Repository (PDR) container:
 * Gatekeeper (GK)
 * Data Access Node (DAN)
 * Repository Manager (RPM)

You can find a description of OPERANDO's architecture on the [project website](https://www.operando.eu). You can find detailed specifications and descriptions of each of the modules in this repository in D5.5 (to be relased in October 2017) of [OPERANDO's public deliverables](https://www.operando.eu/servizi/moduli/moduli_fase01.aspx?mp=1&fn=6&Campo_78=&Campo_126=68&AggiornaDB=search&moduli1379178994=&__VIEWSTATEGENERATOR=D6660DC7&__EVENTVALIDATION=/wEWCAKInYjvBwK46/eoCgLW6PifAQLM6NSfAQLP6LicAQLM6NifAQLPm7uVCQKtvouLDQGIwuPU0XcXVk7W8FmpEwz15iKL).

## Reporting an issue
To report an issue, please use GitHub's built-in issue-tracking system for this repository, or send an email to bug-report@operando.eu.

## Contributing code
If you'd like to contribute code, we'd be happy to have your support, so please contact us at os-contributions@operando.eu! You can find some examples of how you might help us on the [contributions page](https://www.operando.eu) of our website.

N.B. The copyright for any code will be owned by the OPERANDOH2020 organisation, and can therefore be used by any of the partner organisations that make up the consortium in other applications.

## Functional Description of Modules
### GK
The Gatekeeper (GK) facilitates controlled release of personal user data. It is the service which will receive requests to access user data and return the requested data, delegating tasks to other modules in the platform as necessary.

GK is involved in just one workflow: retrieving user data.
Upon receipt of a request for user data, the GK checks the authentication of the caller with the Authentication Service (AS). If the caller is authenticated, the request is forwarded to the Rights Management module, which retrieves the requested data from the Data Access Node (DAN), ‘sanitises’ it according to the wishes of the affected data subjects, and returns the (sanitised) results to the GK. The GK then returns the results to the caller.

### DAN
### RPM

## Installation Instructions
### GK
To compile and package the source code into a .war file:
 * Download and install Java from https://java.com/en/download/
 * Download and install Maven from https://maven.apache.org/download.cgi
 * Navigate to a directory containing both the “op-pdr and the “op-other” repositories. This top-level directory should also contain a “pom.xml” file (see https://maven.apache.org/pom.html for reference), whose modules tag contains the content below .
 * Run “mvn clean package” – Maven will then download any dependencies it needs, and then compile the dependencies in ./op-other, before compiling the .war file from the code in ./op-pdr/op-pdr-gk
 * Once this is complete, you can find the .war file at ./op-pdr/op-pdr-gk/target/operando#pdr#gk.war

```xml
<modules>
	<module>op-other/test-dependencies</module>
	<module>op-other/op-other-common-java</module>
	<module>op-pdr/op-pdr-gk</module>
</modules>
```

### DAN
### RPM

## Usage Instructions
### GK
Once GK and the modules it depends on are up and running, you can send requests to **`[hostname]:[port]/operando/pdr/gatekeeper/[OData query]`** to get data, as specified in the OData query. You should include:
 * a “service-ticket” header, requested from the AAPI for the service ID "/gatekeeper".
 * an “osp-id” header, identifying the OSP whose records you want to get data from.
The data will be returned in the format delivered from RM. At the moment this will be JSON.

### DAN
### RPM

## Future Plans
### GK
Interactive Swagger API documentation will be added to the module, to make understanding the interfaces easier.

### DAN
### RPM

## Dependencies
Dependency Name|Description|Link|Module|Test-only?
---------------|-----------|----|------|----------
Java|A general-purpose computer programming language|https://java.com/en/download/|GK|
Maven|Tool for managing software dependencies and packaging software|https://maven.apache.org/|GK|
Apache Tomcat|Web Container for Java Servlets|http://tomcat.apache.org/|GK|
Jersey|Java library for writing REST-based web applications|https://jersey.java.net/|GK|
Swagger Core|Java library for producing interactive API documentation directly from source code|https://github.com/swagger-api/swagger-core|GK|
Gson|Java library for converting between Java objects and JSON|https://github.com/google/gson|GK|
JUnit|Java library for writing and executing automated unit tests|http://junit.org/junit4/|GK|Y
Hamcrest|Java library for wording unit tests more naturally|http://hamcrest.org/JavaHamcrest/|GK|Y
Wiremock|Java library for testing interactions via HTTP|http://wiremock.org/|GK|Y
Mockito|Java library for mocking and stubbing out software components depended upon by the system under test|https://github.com/mockito/mockito|GK|Y
