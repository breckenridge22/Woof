package com.osu.cse.apps.mobile.woof;

public class DogInfo {

    private String dogId;
    private String dogName;
    private String familyId;

    public DogInfo() {
        // default constructor for Firebase
    }

    public DogInfo(String dogId, String dogName, String familyId) {
        this.dogId = dogId;
        this.dogName = dogName;
        this.familyId = familyId;
    }

    public String getdogId() {
        return dogId;
    }

    public String getdogName() {
        return dogName;
    }

    public String getfamilyId() {
        return familyId;
    }

}
