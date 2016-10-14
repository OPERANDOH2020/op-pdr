package eu.operando.pdr.gatekeeper;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import eu.operando.moduleclients.ClientAuthenticationApiOperandoClientTests;
import eu.operando.moduleclients.ClientAuthenticationApiOperandoServiceTests;
import eu.operando.moduleclients.ClientDataAccessNodeTests;
import eu.operando.moduleclients.http.HttpRequestBuilderAuthenticationApiTests;

@RunWith(Suite.class)
@Suite.SuiteClasses
({
		GkWebServiceImplTests.class,
		ClientAuthenticationApiOperandoClientTests.class,
		ClientAuthenticationApiOperandoServiceTests.class,
		ClientDataAccessNodeTests.class,
		HttpRequestBuilderAuthenticationApiTests.class
})
public class GkTestSuite
{
}
