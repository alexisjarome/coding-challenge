package com.alexis.resources.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SuccessPostResponse {

    String message;

    public SuccessPostResponse() {

    }

    public SuccessPostResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
