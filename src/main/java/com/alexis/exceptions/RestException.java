package com.alexis.exceptions;

import javax.ws.rs.WebApplicationException;
import java.util.Map;

public class RestException extends WebApplicationException {
    private static final long serialVersionUID = 1L;
    protected int status;
    protected String message;
    private Object object;
    private Map<String, Object> headersMap;
    private boolean logIt = true;

    public RestException(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public RestException(int status, String message, boolean logIt) {
        this(status, message);
        this.logIt = logIt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setHeadersMap(Map<String, Object> headersMap) {
        this.headersMap = headersMap;
    }

    public Map<String, Object> getHeadersMap() {
        return headersMap;
    }

    public boolean isLogEnabled() {
        return logIt;
    }

    public void setLogEnabled(boolean logIt) {
        this.logIt = logIt;
    }

}
