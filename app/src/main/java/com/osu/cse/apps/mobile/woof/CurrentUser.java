package com.osu.cse.apps.mobile.woof;

/*
 * Singleton class for storing basic user information and methods for database queries
 */

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CurrentUser {

    private static String sUserId;
    private static DatabaseReference sUserDatabaseRef;

    private static final String TAG = "CurrentUser";

    public static void start() {
        if (sUserId == null) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            sUserId = auth.getCurrentUser().getUid();
            sUserDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users")
                    .child(sUserId);
        }
    }

    /*
     * Nullifies all static members of CurrentUser class
     */
    public static void setNull() {
        sUserId = null;
        sUserDatabaseRef = null;
    }

    public static String getUserId() {
        return sUserId;
    }

    public static DatabaseReference getUserDatabaseRef() {
        return sUserDatabaseRef;
    }

    public static void getCurrentUserFromDatabase(final UserCallback callback) {
        sUserDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                callback.onUserRetrieved(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /*
     * Asynchronously get family IDs associated with current user from database.  IDs are
     * "returned" via callback.
     */
    public static void getFamilyIdsFromDatabase(final FamilyIdsCallback callback) {
        sUserDatabaseRef.child("familyIds")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null) {
                            Log.d(TAG, "family Ids data snapshot null");
                            callback.onError("familyIds data snapshot null");
                        } else {
                            Map<String, Boolean> familyIds = (Map) dataSnapshot.getValue();
                            List<String> familyIdList = new ArrayList();
                            if (familyIds.entrySet() == null) {
                                Log.d(TAG, "family Ids entry set null");
                                callback.onError("familyIds entry set null");
                            }
                            else {
                                for (Map.Entry<String, Boolean> entry : familyIds.entrySet()) {
                                    if (entry != null) {
                                        familyIdList.add(entry.getKey());
                                        Log.d(TAG, "Key " + entry.getKey() + " retrieved " +
                                                "from database");
                                    }
                                    else {
                                        Log.d(TAG, "familyID entry null");
                                    }
                                }
                                callback.onFamilyIdsRetrieved(familyIdList);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onError("Unable to get familyIds");
                    }
                });
    }

    public static void getFamilyNamesFromDatabase(List<String> familyIdList,
                                                  final FamilyInfoCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("families");
        for (String familyId : familyIdList) {
            if (familyId == null) {
                continue;
            }
            Log.d(TAG, "Getting familyInfo for familyId " + familyId);
            ref.child(familyId).addListenerForSingleValueEvent(new ValueEventListener() {

                        // Successfully fetched family name from database/  "Return" family
                        // name and family ID to calling function via callback
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot == null) {
                                Log.d(TAG, "No entry found");
                                return;
                            }
                            FamilyInfo familyInfo = dataSnapshot.getValue(FamilyInfo.class);
                            callback.onFamilyInfoRetrieved(familyInfo);
                        }

                        // error on fetching family name from database
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG, "Error getting data from database");
                        }
                    });
        }
    }

    /*
     *
     */
    public static void getFamilyInfoFromDatabase(final FamilyInfoCallback callback) {

        // fetch family IDs associated with user from database as ArrayList
        getFamilyIdsFromDatabase(new FamilyIdsCallback() {

            // Successfully fetched family IDs from database.  Now, get family name
            // associated with each family ID and "return" to calling function via callback
            @Override
            public void onFamilyIdsRetrieved(List<String> familyIdList) {
                for (String familyId : familyIdList) {
                    Log.d(TAG, "familyIdList entry: " + familyId);
                }
                getFamilyNamesFromDatabase(familyIdList, callback);
            }

            // error on fetching family IDs associated with user from database
            @Override
            public void onError(String errorMessage) {
                Log.d(TAG, errorMessage);
            }
        });

    }

    public static String convertDatabaseURLtoPath(String databaseURL) {
        int index = databaseURL.indexOf(".com") + 4;
        String path = databaseURL.substring(index);
        return path;
    }

    public static void getDogInfoFromDatabase(final DogInfoCallback callback) {
        getFamilyIdsFromDatabase(new FamilyIdsCallback() {
            @Override
            public void onFamilyIdsRetrieved(List<String> familyIdList) {
                for (String familyId : familyIdList) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                            .child("families").child(familyId).child("dogs");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, Map<String, String>> dogMap = (Map) dataSnapshot.getValue();
                            String familyId = dataSnapshot.getRef().getParent().getKey();
                            if (dogMap == null) {
                                Log.d(TAG, "Dogs lookup returned null");
                                return;
                            }
                            for (Map<String, String> dogInfoMap : dogMap.values()) {
                                DogInfo dogInfo = new DogInfo(dogInfoMap.get("dogId"), dogInfoMap.get("dogName"), familyId);
                                callback.onDogInfoRetrieved(dogInfo);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            callback.onFailure("Unable to get dogInfo for family");
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    public static void getDogFromDatabase(String dogId, String familyId, final DogCallback callback) {
        FirebaseDatabase.getInstance().getReference().child("families").child(familyId)
                .child("dogs").child(dogId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Dog dog = dataSnapshot.getValue(Dog.class);
                        if (dataSnapshot == null) {
                            callback.onFailure("Null reference in database");
                        }
                        else {
                            callback.onDogChange(dog);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onFailure("Failed getting dog in database");
                    }
        });
    }

    public static void changeDogName(String familyId, String dogId, String newDogName) {
        FirebaseDatabase.getInstance().getReference().child("families").child(familyId)
                .child("dogs").child(dogId).child("dogName").setValue(newDogName);
    }


}
