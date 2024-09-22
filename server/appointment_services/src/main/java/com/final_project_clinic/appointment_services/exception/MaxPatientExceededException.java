package com.final_project_clinic.appointment_services.exception;

public class MaxPatientExceededException extends RuntimeException {
    public MaxPatientExceededException(String message) {
        super(message);
    }
}

