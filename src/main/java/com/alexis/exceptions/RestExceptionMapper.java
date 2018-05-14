package com.alexis.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestExceptionMapper implements ExceptionMapper<RestException> {

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(RestException e) {
        ErrorMessageResponse errorMessage = new ErrorMessageResponse();
        errorMessage.addError(new ErrorMessage(e.getMessage()));
        //default to json
        MediaType mediaType = headers.getAcceptableMediaTypes().get(0).toString().equals("*/*") ? MediaType.APPLICATION_JSON_TYPE : headers.getAcceptableMediaTypes().get(0);
        return ErrorMessageResponseHelper.buildResponse(errorMessage, e.getStatus(), e.getHeadersMap(), mediaType);
    }

}