package com.roszak89.demoTesting.advice;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class HomeControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String, String> handleValidationExceptions(
            Exception ex) {

        Map<String, String> errorResponseBody = new HashMap<>();

        String timeField = "Time";
        String timeValue = new java.util.Date().toString();

        String statusField = "Status";
        String statusValue = String.valueOf(HttpStatus.BAD_REQUEST);

        String errorField = "Error";
        String errorMessage = ex.getMessage();

        errorResponseBody.put(errorField, errorMessage);
        errorResponseBody.put(statusField, statusValue);
        errorResponseBody.put(timeField, timeValue);

        return errorResponseBody;
    }


}
