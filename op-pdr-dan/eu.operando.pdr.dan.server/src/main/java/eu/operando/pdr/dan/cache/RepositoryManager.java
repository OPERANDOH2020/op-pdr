package eu.operando.pdr.dan.cache;

public class RepositoryManager {
	String name;
	
	String schema;
	
	String host;
	
	String serviceRoot;
	
	int port;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getServiceRoot() {
		return serviceRoot;
	}
	public void setServiceRoot(String serviceRoot) {
		this.serviceRoot = serviceRoot;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	
}
