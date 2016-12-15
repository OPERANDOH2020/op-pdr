package eu.operando.pdr.dan.service;

import io.swagger.client.model.LogRequest;

public interface LogDBServiceIF {
	public void log(LogRequest.LogDataTypeEnum logLevel, String logTitle, String logDescription, String logPriority);
}
