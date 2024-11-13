package com.example.exceptions;

public class UsernameAlreadyExists extends RuntimeException{

    public UsernameAlreadyExists(){
        super("Chosen username already exists!");
    }
}
