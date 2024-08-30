package com.example.backend.Auth;

public class AuthFailException extends Exception{
    @Override
    public String getMessage() {
        return "Authentication Failed";
    }

}
