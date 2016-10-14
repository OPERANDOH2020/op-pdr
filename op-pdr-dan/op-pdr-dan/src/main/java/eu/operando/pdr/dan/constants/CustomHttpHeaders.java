package eu.operando.pdr.dan.constants;

public enum CustomHttpHeaders {
	OSP_IDENTIFIER("osp-identifier"),
	PSP_USER_IDENTIFIER("psp-user-identifier"),
	SERVICE_TICKET("service-ticket");

	private String value;
	
	CustomHttpHeaders(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
}
