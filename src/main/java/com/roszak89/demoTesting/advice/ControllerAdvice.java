package com.roszak89.demoTesting.advice;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;




@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleValidationExceptions(
           Exception ex) {
        return ex.getMessage();
    }


}
