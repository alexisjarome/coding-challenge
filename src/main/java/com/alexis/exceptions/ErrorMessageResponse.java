package com.alexis.exceptions;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
public class ErrorMessageResponse {
    private List<ErrorMessage> errors = new LinkedList<ErrorMessage>();

    public ErrorMessageResponse() {
        // default constructor.
    }

    public List<ErrorMessage> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorMessage> errors) {
        this.errors = errors;
    }

    public void addError(ErrorMessage error) {
        errors.add(error);
    }

    @Override
    public String toString() {
        StringBuffer errorMsg = new StringBuffer("Errors : ");
        for (ErrorMessage s : errors) {
            errorMsg.append(s).append("\n");
        }
        return errorMsg.toString();
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }
}
