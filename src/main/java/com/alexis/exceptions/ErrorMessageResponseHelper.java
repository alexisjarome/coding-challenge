package com.alexis.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

public class ErrorMessageResponseHelper {

    public static Response buildResponse(final Object entity, final int status, final Map<String, Object> headersMap, MediaType mediaType) {

        final Response.ResponseBuilder responseBuilder =
                Response.status(status).entity(entity).type(mediaType);

        if (headersMap != null) {
            // add headers
            for (String name : headersMap.keySet()) {
                responseBuilder.header(name, headersMap.get(name));
            }
        }
        return responseBuilder.build();
    }
}
