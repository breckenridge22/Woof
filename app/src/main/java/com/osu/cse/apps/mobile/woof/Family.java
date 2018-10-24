package com.osu.cse.apps.mobile.woof;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Family {

    private String familyId;
    private String familyName;
    private List<String> userIdList = new ArrayList();
    private List<String> dogIdList = new ArrayList();

    public Family() {
        // default constructor required for Firebase
    }

    public Family(String familyName, String userId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        this.familyId = ref.child("families").push().getKey();
        this.familyName = familyName;
        this.userIdList.add(userId);
    }

    public String getfamilyId() {
        return familyId;
    }

    public String getfamilyName() {
        return familyName;
    }

    public List<String> getuserIdList() {
        return userIdList;
    }

    public List<String> getdogIdList() {
        return dogIdList;
    }

    public void addDogId(String dogId) {
        dogIdList.add(dogId);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap();
        result.put("familyId", familyId);
        result.put("familyName", familyName);
        result.put("userIdList", userIdList);
        result.put("dogIdList", dogIdList);
        return result;
    }

    public void deleteFamily(){
        // step 1
        CurrentUser.removeFamilyValueEventListener(familyId);

        // step 2
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap();
        childUpdates.put("/families/" + familyId, null);
        ref.updateChildren(childUpdates);
    }
}
