package com.gigaspaces.cloudify.restclient;

import com.gigaspaces.cloudify.restclient.RestException;

/**
 * @author uri
 */
public class ErrorStatusException extends RestException {

	private static final long serialVersionUID = -399277091070772297L;
	private String reasonCode;
    private Object[] args;


    public ErrorStatusException(String reasonCode, Throwable cause, Object... args) {
        super("reasonCode: " + reasonCode, cause);
        this.args = args;
        this.reasonCode = reasonCode; 
    }

    public ErrorStatusException(String reasonCode, Object... args) {
        super("reasonCode: " + reasonCode);
        this.reasonCode = reasonCode;
        this.args = args;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public Object[] getArgs() {
        return args;
    }
}