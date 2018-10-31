package com.osu.cse.apps.mobile.woof;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Family {

    private String familyId;
    private String familyName;
    private Map<String, Boolean> userIds;
    private Map<String, Dog> dogs;

    public Family() {
        // default constructor required for Firebase
        userIds = new HashMap();
        dogs = new HashMap();
    }

    public Family(String familyId, String familyName, String userId) {
        this.familyId = familyId;
        this.familyName = familyName;
        userIds = new HashMap();
        this.userIds.put(userId, true);
        dogs = new HashMap();
    }

    public String getfamilyId() {
        return familyId;
    }

    public String getfamilyName() {
        return familyName;
    }

    public Map<String, Boolean>getuserIds() {
        return userIds;
    }

    public Map<String, Dog> getdogs() {
        return dogs;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap();
        result.put("familyId", familyId);
        result.put("familyName", familyName);
        result.put("userIds", userIds);
        result.put("dogs", dogs);
        return result;
    }

}
