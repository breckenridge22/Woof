package com.osu.cse.apps.mobile.woof;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
     * 1. Detaches the value event listener for the dog object in the database (may not be necessary
     *    given step 2).
     * 2. Locally removes dog ID from associated family's dogIdList object (no effect on database)
     * 3. ATOMICALLY removes the dog object from the database AND updates associated family object
     *    in database (effectively removing the dogId from family's dogIdList in database)
     *    (ATOMIC = either both changes will be made or neither will be made.)
     * 4. Locally removes dog from CurrentUser.sDogMap (no effect on database)
     */
    public void deleteDog() {

        // step 1
        // TODO: Add checking for completion of listener removal--how?
        CurrentUser.removeDogValueEventListener(dogId);

        // step 2
        Family family = CurrentUser.getFamilyMap().get(familyId);
        List<String> dogIdList = family.getdogIdList();
        for (int i = 0; i < dogIdList.size(); i++) {
            if (dogId.equals(dogIdList.get(i))) {
                dogIdList.remove(i);
                break;
            }
        }

        Log.d(TAG, "About to delete dog and dogId in database");

        // step 3
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap();
        childUpdates.put("/dogs/" + dogId, null); // pass null to delete object from database
        childUpdates.put("/families/" + familyId, family.toMap());
        ref.updateChildren(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully deleted dog and dogId in database");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Database update failed");
                    }
                });

        // step 4
        CurrentUser.getDogMap().remove(dogId);

    }

}
