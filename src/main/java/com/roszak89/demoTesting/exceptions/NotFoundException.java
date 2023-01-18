package com.roszak89.demoTesting.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String s,long id){
        super(s+" id: "+id);
    }
}
