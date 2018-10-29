package com.osu.cse.apps.mobile.woof;

import java.util.HashMap;
import java.util.Map;

public class Dog {

    private String dogId;
    private String dogName;
    private static final String TAG = "Dog";

    public Dog() {
        // default constructor for Firebase
    }

    public Dog(String dogId, String dogName) {
        this.dogId = dogId;
        this.dogName = dogName;
    }

    public String getdogId() {
        return dogId;
    }

    public String getdogName() {
        return dogName;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap();
        result.put("dogId", dogId);
        result.put("dogName", dogName);
        return result;
    }

}
