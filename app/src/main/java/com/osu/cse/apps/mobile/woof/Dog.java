package com.osu.cse.apps.mobile.woof;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Dog {

    private String dogId;
    private String dogName;
    private String familyId;

    public Dog() {
        // default constructor for Firebase
    }

    public Dog(String dogId, String dogName, String familyId) {
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

    // Note: If the name of this method is changed to "setdogName", it screws something
    // up with Firebase when the app is launched
    public void changedogName(String dogName) {
        this.dogName = dogName;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("dogs").child(dogId).setValue(toMap());
    }

    public String getfamilyId() {
        return familyId;
    }

    public String getFamilyName(User user, String familyId) {
        return user.getFamilyNameById(familyId);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap();
        result.put("dogId", dogId);
        result.put("dogName", dogName);
        result.put("familyId", familyId);
        return result;
    }

}
