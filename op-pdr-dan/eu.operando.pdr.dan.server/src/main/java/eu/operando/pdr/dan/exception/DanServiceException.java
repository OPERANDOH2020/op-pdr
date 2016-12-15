package eu.operando.pdr.dan.exception;

@SuppressWarnings("serial")
public class DanServiceException extends Exception {	
	int code;
	String message;

	public DanServiceException(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
