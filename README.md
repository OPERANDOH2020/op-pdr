# op-pdr
For the modules in the Private Data Repository (PDR) container:
 * Gatekeeper (GK)
 * Data Access Node (DAN)
 * Repository Manager (RPM)

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
