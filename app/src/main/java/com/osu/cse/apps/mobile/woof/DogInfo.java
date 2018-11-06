package com.osu.cse.apps.mobile.woof;

public class DogInfo {

    private String dogId;
    private String dogName;
    private String familyId;
    private String activitiesId;

    public DogInfo() {
        // default constructor for Firebase
    }

    public DogInfo(String dogId, String dogName, String familyId, String activitiesId) {
        this.dogId = dogId;
        this.dogName = dogName;
        this.familyId = familyId;
        this.activitiesId = activitiesId;
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

    public String getactivitiesId() {
        return activitiesId;
    }

}
