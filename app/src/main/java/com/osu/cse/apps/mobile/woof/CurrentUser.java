package com.osu.cse.apps.mobile.woof;

/*
 * Singleton class for storing all information pertinent to current user.  sCurrentUser is instantiated
 * on user login or user account creation.  When sCurrentUser is instantiated, valueEventListeners
 * are created for all relevant data objects in database (i.e., families and dogs).
 * sCurrentUser is nullified on user logout.
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

public class CurrentUser extends User {

    private static User sCurrentUser;
    private static Map<String, ValueEventListener> sUserValueEventListenerMap;
    private static Map<String, ValueEventListener> sFamilyValueEventListenerMap;
    private static Map<String, ValueEventListener> sDogValueEventListenerMap;
    private static final String TAG = "CurrentUser";

    public static User get() {

        if (sCurrentUser == null) {

            // Get data from database by adding value event listeners.  This is done in a
            // cascading fashion by first adding a value event listener to the child of
            // "users" corresponding to the user's ID.  Then, value event listeners are added to
            // the children of "families" corresponding to the family IDs for each of the families
            // of which the user is a member.  Then, value event listeners are added to the children
            // of "dogs" corresponding to the dog IDs for each of the dogs associated with the
            // user's families.

            sUserValueEventListenerMap = new HashMap();
            sFamilyValueEventListenerMap = new HashMap();
            sDogValueEventListenerMap = new HashMap();

            FirebaseAuth auth = FirebaseAuth.getInstance();
            String userId = auth.getCurrentUser().getUid();
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            // source: https://firebase.google.com/docs/database/admin/retrieve-data
            // source: https://firebase.google.com/docs/database/android/read-and-write
            class DogValueEventListener implements ValueEventListener {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Dog dog = dataSnapshot.getValue(Dog.class);
                    sCurrentUser.updateDog(dog);
                    Log.d(TAG, "Value event listener called for dog " + dog.getdogName() +
                        " triggered");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            }

            class FamilyValueEventListener implements ValueEventListener {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Family family = dataSnapshot.getValue(Family.class);
                    sCurrentUser.updateFamily(family);
                    Log.d(TAG, "Value event listener for family " + family.getfamilyName() +
                        " triggered");

                    // add value event listener for each dog that belongs to the family
                    List<String> dogIdList = family.getdogIdList();
                    dogIdList.removeAll(Collections.singleton(null));
                    for (String dogId : dogIdList) {
                        addDogValueEventListener(dogId, new DogValueEventListener());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            }

            class UserValueEventListener implements ValueEventListener {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    sCurrentUser = dataSnapshot.getValue(User.class);
                    Log.d(TAG, "Value event listener for user " +
                            sCurrentUser.getfName() + " " + sCurrentUser.getlName() + " triggered");

                    // add value event listener for each family of which the user is a member
                    List<String> familyIdList = sCurrentUser.getfamilyIdList();
                    familyIdList.removeAll(Collections.singleton(null));
                    for (String familyId : familyIdList) {
                        addFamilyValueEventListener(familyId, new FamilyValueEventListener());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            }

            // start cascade of adding value event listeners in database for all information
            // pertinent to current user
            addUserValueEventListener(userId, new UserValueEventListener());

        }

        return sCurrentUser;
    }

    /*
     * Removes all value event listeners associated with CurrentUser in the database and nullifies
     * all static member variables of CurrentUser class
     */
    public static void setNull() {

        Map<String, ValueEventListener>[] listenerMaps = new Map[]{sUserValueEventListenerMap,
            sFamilyValueEventListenerMap, sDogValueEventListenerMap};
        String[] mapTypes = new String[]{"users", "families", "dogs"};

        // remove event listeners in each of the maps associated with CurrentUser and set map
        // to null
        for (int i = 0; i < listenerMaps.length; i++) {
            Map<String, ValueEventListener> listenerMap = listenerMaps[i];
            String mapType = mapTypes[i];

            // remove value event listeners for current map
            for (Map.Entry<String, ValueEventListener> mapEntry : listenerMap.entrySet()) {
                String id = mapEntry.getKey();
                switch (mapType) {
                    case "users":
                        removeUserValueEventListener(id);
                        break;
                    case "families":
                        removeFamilyValueEventListener(id);
                        break;
                    case "dogs":
                        removeDogValueEventListener(id);
                    default:
                        Log.d(TAG, "Requesting to remove invalid listener type");
                }
            }

            listenerMap = null;
        }

        sCurrentUser = null;

    }

    public static void removeUserValueEventListener(String userId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users")
                .child(userId);
        removeValueEventListener(sUserValueEventListenerMap, userId, ref);
    }

    public static void removeFamilyValueEventListener(String familyId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("families")
                .child(familyId);
        removeValueEventListener(sFamilyValueEventListenerMap, familyId, ref);
    }

    public static void removeDogValueEventListener(String dogId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("dogs")
                .child(dogId);
        CurrentUser.removeValueEventListener(sDogValueEventListenerMap, dogId, ref);
    }

    /*
     * Removes value event listener from data object in database, then removes entry from
     * specified map in this singleton.  Should be called BEFORE deleting a data object in the
     * database (e.g., User, Family, Dog).
     */
    private static void removeValueEventListener(Map<String, ValueEventListener> listenerMap,
                                                String id, DatabaseReference ref) {
        ValueEventListener listener = listenerMap.get(id);
        ref.removeEventListener(listener);
        Log.d(TAG, "removeEventListener() called for " + id);
        listenerMap.remove(id);
    }

    private static void addUserValueEventListener(String userId, ValueEventListener newListener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users")
                .child(userId);
        addValueEventListener(sUserValueEventListenerMap, userId, ref, newListener);
    }

    private static void addFamilyValueEventListener(String familyId, ValueEventListener newListener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("families")
                .child(familyId);
        addValueEventListener(sFamilyValueEventListenerMap, familyId, ref, newListener);

    }

    private static void addDogValueEventListener(String dogId, ValueEventListener newListener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("dogs")
                .child(dogId);
        addValueEventListener(sDogValueEventListenerMap, dogId, ref, newListener);
    }

    private static void addValueEventListener(Map<String, ValueEventListener> listenerMap, String id,
                                             DatabaseReference ref,
                                             ValueEventListener newListener) {
        if (listenerMap.get(id) != null) {
            removeValueEventListener(listenerMap, id, ref);
        }
        listenerMap.put(id, newListener);
        ref.addValueEventListener(newListener);
    }



}
