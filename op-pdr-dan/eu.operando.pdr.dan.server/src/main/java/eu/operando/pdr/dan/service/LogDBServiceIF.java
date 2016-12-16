package eu.operando.pdr.dan.service;

import io.swagger.client.model.LogRequest.*;

public interface LogDBServiceIF {
	public void log(LogDataTypeEnum logLevel, String logTitle, String logDescription, LogPriorityEnum logPriority);
}
