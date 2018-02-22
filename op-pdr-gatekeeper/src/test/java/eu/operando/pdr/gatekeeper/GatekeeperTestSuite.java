package eu.operando.pdr.gatekeeper;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import eu.operando.moduleclients.ClientAuthenticationApiOperandoClientTests;
import eu.operando.moduleclients.ClientAuthenticationApiOperandoServiceTests;
import eu.operando.moduleclients.http.HttpRequestBuilderAuthenticationApiTests;

@RunWith(Suite.class)
@Suite.SuiteClasses
({
	GatekeeperApiTests.class,
	GatekeeperServiceImplTests.class,
	ClientAuthenticationApiOperandoClientTests.class,
	ClientAuthenticationApiOperandoServiceTests.class,
	HttpRequestBuilderAuthenticationApiTests.class
})
public class GatekeeperTestSuite
{
}
