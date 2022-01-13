package com.example.travelbyplane.Exceptions;

public class ClientBadActionException extends RuntimeException{
    public ClientBadActionException(String message){
        super(message);
    }
}
