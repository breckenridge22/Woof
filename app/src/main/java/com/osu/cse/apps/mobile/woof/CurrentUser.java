package com.osu.cse.apps.mobile.woof;

/*
 * Singleton class for storing all information pertinent to current user.  Static members are
 * instantiated on user login or user account creation.
 */

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentUser {

    private static User sCurrentUser;
    private static Map<String, Family> sFamilyMap;
    private static Map<String, Dog> sDogMap;
    private static UserValueEventListener sUserValueEventListener;
    private static FamilyValueEventListener sFamilyValueEventListener;
    private static DogValueEventListener sDogValueEventListener;

    private static final String TAG = "CurrentUser";

    // source: https://firebase.google.com/docs/database/admin/retrieve-data
    // source: https://firebase.google.com/docs/database/android/read-and-write
    static class UserValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            User user = dataSnapshot.getValue(User.class);
            sCurrentUser = user;
            Log.d(TAG, "Value event listener for user " +
                    user.getfName() + " " + user.getlName() + " triggered");

            // add value event listener for each family of which the user is a member
            List<String> familyIdList = user.getfamilyIdList();
            familyIdList.removeAll(Collections.singleton(null));
            for (String familyId : familyIdList) {
                addFamilyValueEventListener(familyId);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
    }

    static class FamilyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Family family = dataSnapshot.getValue(Family.class);
            sFamilyMap.put(family.getfamilyId(), family);
            Log.d(TAG, "Value event listener for family " + family.getfamilyName() +
                    " triggered");

            // add value event listener for each dog that belongs to the family
            List<String> dogIdList = family.getdogIdList();
            dogIdList.removeAll(Collections.singleton(null));
            for (String dogId : dogIdList) {
                addDogValueEventListener(dogId);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
    }

    static class DogValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Dog dog = dataSnapshot.getValue(Dog.class);
            sDogMap.put(dog.getdogId(), dog);
            Log.d(TAG, "Value event listener for dog " + dog.getdogName() +
                    " triggered");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
    }

    /*
     * Get data from database by adding value event listeners.  This is done in a cascading fashion.
     * (First, user value event listener is created.  Then, family listeners.  Then, dog listeners.)
     */
    public static void start() {

        if (sCurrentUser == null) {

            sFamilyMap = new HashMap();
            sDogMap = new HashMap();
            sUserValueEventListener = new UserValueEventListener();
            sFamilyValueEventListener = new FamilyValueEventListener();
            sDogValueEventListener = new DogValueEventListener();


            FirebaseAuth auth = FirebaseAuth.getInstance();
            String userId = auth.getCurrentUser().getUid();

            // start cascade of adding value event listeners in database for all information
            // pertinent to current user
            addUserValueEventListener(userId);

        }

    }

    public static User getCurrentUser() {
        if (sCurrentUser == null) {
            start();
        }
        return sCurrentUser;
    }

    public static Map<String, Family> getFamilyMap() {
        if (sFamilyMap == null) {
            start();
        }
        return sFamilyMap;
    }

    public static Map<String, Dog> getDogMap() {
        if (sDogMap == null) {
            start();
        }
        return sDogMap;
    }


    /*
     * Nullifies all static members of CurrentUser class
     */
    public static void setNull() {
        sCurrentUser = null;
        sFamilyMap = null;
        sDogMap = null;
        sUserValueEventListener = null;
        sFamilyValueEventListener = null;
        sDogValueEventListener = null;
    }

    public static void removeUserValueEventListener(String userId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users")
                .child(userId);
        removeValueEventListener(sUserValueEventListener, ref);
    }

    public static void removeFamilyValueEventListener(String familyId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("families")
                .child(familyId);
        removeValueEventListener(sFamilyValueEventListener, ref);
    }

    public static void removeDogValueEventListener(String dogId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("dogs")
                .child(dogId);
        CurrentUser.removeValueEventListener(sDogValueEventListener, ref);
    }

    /*
     * Detaches value event listener from database reference.  Should be called BEFORE deleting
     * a data object in the database (e.g., User, Family, Dog).
     */
    private static void removeValueEventListener(ValueEventListener listener,
                                                DatabaseReference ref) {
        ref.removeEventListener(listener);
        Log.d(TAG, "removeEventListener() called for " + ref.toString());
    }

    private static void addUserValueEventListener(String userId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users")
                .child(userId);
        addValueEventListener(sUserValueEventListener, ref);
    }

    private static void addFamilyValueEventListener(String familyId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("families")
                .child(familyId);
        addValueEventListener(sFamilyValueEventListener, ref);

    }

    private static void addDogValueEventListener(String dogId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("dogs")
                .child(dogId);
        addValueEventListener(sDogValueEventListener, ref);
    }

    private static void addValueEventListener(ValueEventListener listener, DatabaseReference ref) {

        // Detaches value event listener from database reference for safety before adding.
        // Per Firebase documentation: "If a listener has been added multiple times to a data
        // location, it is called multiple times for each event, and you must detach it the same
        // number of times to remove it completely."
        // source: https://firebase.google.com/docs/database/android/read-and-write?authuser=0#detach_listeners
        removeValueEventListener(listener, ref);

        ref.addValueEventListener(listener);
    }

}
