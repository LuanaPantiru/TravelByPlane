package com.example.travelbyplane.Exceptions;

public class ForeignKeyException extends RuntimeException{
    public ForeignKeyException(String message){
        super(message);
    }
}
