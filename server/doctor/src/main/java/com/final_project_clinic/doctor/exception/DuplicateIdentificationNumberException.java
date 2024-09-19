package com.final_project_clinic.doctor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateIdentificationNumberException extends RuntimeException {

    public DuplicateIdentificationNumberException(String message) {
        super(message);
    }
}
