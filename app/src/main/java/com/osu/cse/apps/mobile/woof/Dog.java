package com.osu.cse.apps.mobile.woof;

import java.util.HashMap;
import java.util.Map;

public class Dog {

    private static final String TAG = "Dog";

    private String dogId;
    private String dogName;
    private String activitiesId;

    public Dog() {
        // default constructor for Firebase
    }

    public Dog(String dogId, String dogName, String activitiesId) {
        this.dogId = dogId;
        this.dogName = dogName;
        this.activitiesId = activitiesId;
    }

    public String getdogId() {
        return dogId;
    }

    public String getdogName() {
        return dogName;
    }

    public String getactivitiesId() {
        return activitiesId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap();
        result.put("dogId", dogId);
        result.put("dogName", dogName);
        result.put("activitiesId", activitiesId);
        return result;
    }

}
