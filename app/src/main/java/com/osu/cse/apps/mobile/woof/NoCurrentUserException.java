package com.osu.cse.apps.mobile.woof;

public class NoCurrentUserException extends RuntimeException {

    public NoCurrentUserException(String message) {
        super(message);
    }

}