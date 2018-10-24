package com.osu.cse.apps.mobile.woof;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dog {

    private String dogId;
    private String dogName;
    private String familyId;
    private static final String TAG = "Dog";

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

    /*
     * This method carries out the following steps:
     *
     * 1. Removes the value event listener for the dog object in the database (may not be necessary
     *    given step 2) and deletes record of event listener in CurrentUser singleton.
     * 2. Removes dog ID from associated family's dogIdList object
     * 3. ATOMICALLY removes the dog object from the database AND updates associated family object
     *    in database (effectively removing the dogId from family's dogIdList in database)
     *    (ATOMIC = either both changes will be made or neither will be made.)
     * 4. Removes dog from current user's dogList object
     */
    public void deleteDog() {

        // step 1
        CurrentUser.removeDogValueEventListener(dogId);

        // step 2
        Family family = CurrentUser.get().getfamilyMap().get(familyId);
        List<String> dogIdList = family.getdogIdList();
        int dogListIndex = 0;
        Log.d(TAG, "Looking for dog ID" + dogId);
        for (int i = 0; i < dogIdList.size(); i++) {
            if (dogId.equals(dogIdList.get(i))) {
                Log.d(TAG, "Found dog ID" + dogId);
                dogListIndex = i;
                break;
            }
        }
        if (dogListIndex != 0) {
            dogIdList.remove(dogListIndex);
        }

        // step 3
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap();
        childUpdates.put("/dogs/" + dogId, null); // pass null to delete object from database
        childUpdates.put("/families/" + familyId, family.toMap());
        ref.updateChildren(childUpdates);

        // step 4
        CurrentUser.get().getdogList().remove(dogId);

    }

}
