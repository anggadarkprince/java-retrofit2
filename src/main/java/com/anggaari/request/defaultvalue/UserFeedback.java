package com.anggaari.request.defaultvalue;

public class UserFeedback {
    public String osName = "Android";
    public int osVersion = Constants.VERSION;
    public String device = Constants.MODEL;
    public String message;
    public boolean userIsATalker;

    public UserFeedback(String message) {
        this.message = message;
        this.userIsATalker = (message.length() > 200);
    }
}
