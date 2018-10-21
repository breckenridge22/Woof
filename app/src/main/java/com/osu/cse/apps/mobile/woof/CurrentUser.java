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

import java.util.List;

public class CurrentUser extends User {

    private static User sCurrentUser;
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

                    // add value event listener for each dog that belongs to the family
                    List<String> dogIdList = family.getdogIdList();
                    for (String dogId : dogIdList) {
                        ref.child("dogs").child(dogId).
                                addValueEventListener(new DogValueEventListener());
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

                    // add value event listener for each family of which the user is a member
                    List<String> familyIdList = sCurrentUser.getfamilyIdList();
                    for (String familyId : familyIdList) {
                        ref.child("families").child(familyId).
                                addValueEventListener(new FamilyValueEventListener());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            }

            // start cascade of adding value event listeners in database for all information
            // pertinent to current user
            Log.d(TAG, "userId = " + userId);
            ref.child("users").child(userId).addValueEventListener(new UserValueEventListener());

        }

        return sCurrentUser;
    }

    public static void setNull() {
        sCurrentUser = null;
        // TODO: remove value event listeners
    }


}
