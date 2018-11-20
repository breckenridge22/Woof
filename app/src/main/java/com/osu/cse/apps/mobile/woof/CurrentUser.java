package com.osu.cse.apps.mobile.woof;

/*
 * Singleton class for storing basic user information and methods for database queries
 */

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CurrentUser {

    private static String sUserId;
    private static DatabaseReference sUserDatabaseRef;
    private static boolean sConnectedToDatabase;
    private static ValueEventListener sConnectedListener;
    private static List<DatabaseReference> sSyncedList;

    private static boolean sDatabasePersistenceEnabled;
    // Map for activities currently in progress, key is the activity ID for a dog and the value is
    // the activity in progress
    private static Map<String, ActivityRecord> mCurrentActivities;

    private static final String TAG = "CurrentUser";

    public static void start() {
        if (sUserId == null) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            sUserId = auth.getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            sUserDatabaseRef = ref.child("users")
                    .child(sUserId);
            sConnectedListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    sConnectedToDatabase = dataSnapshot.getValue(Boolean.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "Error fetching connection status of user to database");
                }
            };
            ref.child(".info/connected").addValueEventListener(sConnectedListener);
            mCurrentActivities = new HashMap<>();

            // synchronize all database references relevant to users (i.e., direct user data,
            // families of which user is a part, and activities for dogs belonging to those
            // families)
            sSyncedList = new ArrayList();
            DatabaseReference userRef = ref.child("users").child(sUserId);
            userRef.keepSynced(true);
            sSyncedList.add(userRef);
            getFamilyIdsFromDatabase(new FamilyIdsCallback() {
                @Override
                public void onFamilyIdsRetrieved(List<String> familyIdList) {
                    for (String familyId : familyIdList) {
                        DatabaseReference familyRef = FirebaseDatabase.getInstance().getReference()
                                .child("families").child(familyId);
                        familyRef.keepSynced(true);
                        sSyncedList.add(familyRef);

                        getFamilyFromDatabase(familyId, new FamilyCallback() {
                            @Override
                            public void onFamilyChange(Family family) {
                                Map<String, Dog> dogMap = family.getdogs();
                                if (dogMap == null) {
                                    return;
                                }
                                for (Dog dog : dogMap.values()) {
                                    String activitiesId = dog.getactivitiesId();
                                    DatabaseReference activitiesRef = FirebaseDatabase.getInstance()
                                            .getReference().child("activities").child(activitiesId);
                                    activitiesRef.keepSynced(true);
                                    sSyncedList.add(activitiesRef);
                                }
                            }

                            @Override
                            public void onFailure(String error) {
                                Log.d(TAG, error);
                            }
                        });
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    Log.d(TAG, errorMessage);
                }
            });
        }
    }

    /*
     * Nullifies all static members of CurrentUser class
     */
    public static void setNull() {
        sUserId = null;
        sUserDatabaseRef = null;
        mCurrentActivities = null;

        FirebaseDatabase.getInstance().getReference().child(".info/connected")
                .removeEventListener(sConnectedListener);
        sConnectedListener = null;

        // remove all synchronized database references
        for (DatabaseReference ref : sSyncedList) {
            ref.keepSynced(false);
        }
        sSyncedList = null;
    }

    public static Map<String, ActivityRecord> getmCurrentActivities() {
        return mCurrentActivities;
    }

    public static void setmCurrentActivities(List<String> dogs) {
        for (String s : dogs) {
            mCurrentActivities.put(s, null);
        }
    }

    public static void clearmCurrentActivities() {
        mCurrentActivities.clear();
    }

    public static void addActivity(ActivityRecord act) {
        Set<String> keys = mCurrentActivities.keySet();
        for (String s : keys) {
            mCurrentActivities.replace(s, act);
        }
    }

    public static void saveActivity() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref;
        for (Map.Entry<String, ActivityRecord> e : mCurrentActivities.entrySet()) {
            ref = mDatabase.child("activities").child(e.getKey());
            if (e.getValue().getactivity_Type() == ActivityRecord.WALK) {
                e.getValue().setend_Time(new Date());
            }
            String key = ref.push().getKey();
            ref.child(key).setValue(e.getValue());
        }
        mCurrentActivities.clear();
    }

    public static String getUserId() {
        return sUserId;
    }

    public static DatabaseReference getUserDatabaseRef() {
        return sUserDatabaseRef;
    }

    public static Boolean isConnectedToDatabase() {
        return sConnectedToDatabase;
    }

    public static boolean isDatabasePersistenceEnabled() {
        return sDatabasePersistenceEnabled;
    }

    public static void setDatabasePersistenceEnabled(boolean enabled) {
        sDatabasePersistenceEnabled = enabled;
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
        Log.d(TAG, "getFamilyIdsFromDatabase() called");
        sUserDatabaseRef.child("familyIds")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange() called");
                        if (dataSnapshot == null) {
                            Log.d(TAG, "family Ids data snapshot null");
                            callback.onError("familyIds data snapshot null");
                        } else {
                            Map<String, Boolean> familyIds = (Map) dataSnapshot.getValue();
                            List<String> familyIdList = new ArrayList();
                            if (familyIds.entrySet() == null) {
                                Log.d(TAG, "family Ids entry set null");
                                callback.onError("familyIds entry set null");
                            } else {
                                for (Map.Entry<String, Boolean> entry : familyIds.entrySet()) {
                                    if (entry != null) {
                                        familyIdList.add(entry.getKey());
                                        Log.d(TAG, "Key " + entry.getKey() + " retrieved " +
                                                "from database");
                                    } else {
                                        Log.d(TAG, "familyID entry null");
                                    }
                                }
                                callback.onFamilyIdsRetrieved(familyIdList);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled() called");
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

                // Successfully fetched family name from database.  "Return" family
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
                    callback.onFailure("Error getting data from database");
                }
            });
        }
    }

    public static void getFamilyInfoFromDatabaseById(String familyId, final FamilyInfoCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("families")
                .child(familyId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FamilyInfo familyInfo = dataSnapshot.getValue(FamilyInfo.class);
                if (familyInfo == null) {
                    callback.onFailure("Retrieved FamilyInfo object null");
                    return;
                }
                callback.onFamilyInfoRetrieved(familyInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailure("Failed to retrieved FamilyInfo from database");
            }
        });
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
                callback.onFailure(errorMessage);
            }
        });

    }

    public static void getFamilyFromDatabase(String familyId, final FamilyCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("families")
                .child(familyId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Family family = dataSnapshot.getValue(Family.class);
                callback.onFamilyChange(family);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure("ValueEventListener.onCancelled called");
            }
        });
    }

    public static String convertDatabaseURLtoPath(String databaseURL) {
        int index = databaseURL.indexOf(".com") + 4;
        String path = databaseURL.substring(index);
        return path;
    }

    public static void getDogInfoFromDatabase(final DogInfoCallback callback) {
        Log.d(TAG, "getDogInfoFromDatabase() called");
        getFamilyIdsFromDatabase(new FamilyIdsCallback() {
            @Override
            public void onFamilyIdsRetrieved(List<String> familyIdList) {
                Log.d(TAG, "onFamilyIdsRetrieved() called");
                for (String familyId : familyIdList) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                            .child("families").child(familyId).child("dogs");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "onDataChange() called");
                            Map<String, Map<String, String>> dogMap = (Map) dataSnapshot.getValue();
                            String familyId = dataSnapshot.getRef().getParent().getKey();
                            if (dogMap == null) {
                                Log.d(TAG, "Dogs lookup returned null");
                                return;
                            }
                            for (Map<String, String> dogInfoMap : dogMap.values()) {
                                DogInfo dogInfo = new DogInfo(dogInfoMap.get("dogId"),
                                        dogInfoMap.get("dogName"), familyId, dogInfoMap.get("activitiesId"));
                                callback.onDogInfoRetrieved(dogInfo);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG, "onCancelled() called");
                            callback.onFailure("Unable to get dogInfo for family");
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.d(TAG, "oneError() called");
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
                } else {
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

    public static void getFamilyMembersFromDatabase(String familyId, final FamilyMemberCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("families")
                .child(familyId).child("userIds");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Boolean> userIds = (Map) dataSnapshot.getValue();
                if (userIds == null) {
                    callback.onFailure("User ID map null");
                    return;
                }
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
                for (String userId : userIds.keySet()) {
                    ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User familyMember = dataSnapshot.getValue(User.class);
                            callback.onFamilyMemberRetrieved(familyMember);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            callback.onFailure("Error retrieving user from database");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure("SingleValueEventListener event cancelled");
            }
        });
    }

    /*
     * Note: addValueEventListener is used here instead of addSingleValueEventListener because
     * of an issue with Firebase persistence where if the locally cached datasnapshot does not have any
     * children, subsequent calls with a single value event listener will not check the database for an updated value.
     * This may be related to the fact that a query is used.
     */
    public static void searchDatabaseForUserByEmail(String email, final UserCallback callback) {
        Log.d(TAG, "searchDatabaseForUserByEmail() called");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        Query userQuery = ref.orderByChild("email").equalTo(email);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange called");
                if (dataSnapshot.getChildrenCount() > 1) {
                    callback.onFailure("Found more than one user with matching email");
                    return;
                }
                User user = null;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    user = userSnapshot.getValue(User.class);
                    if (user == null) {
                        callback.onFailure("User object obtained from database null");
                        return;
                    }
                }
                callback.onUserRetrieved(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled() called");
                callback.onFailure("Failed getting user from database");
            }
        });
    }

    private interface InvitationIdsCallback {
        void onInvitationIdsRetrieved(List<String> invitationIds);

        void onFailure(String error);
    }

    public static void getUserInvitationIdsFromDatabase(String userId, final InvitationIdsCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users")
                .child(userId).child("invitationIds");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Boolean> invitationIds = (Map) dataSnapshot.getValue();
                if (invitationIds == null) {
                    callback.onFailure("Empty user id list retrieved from database");
                    return;
                }
                List<String> invitationIdList = new ArrayList();
                for (Map.Entry<String, Boolean> entry : invitationIds.entrySet()) {
                    invitationIdList.add(entry.getKey());
                }
                callback.onInvitationIdsRetrieved(invitationIdList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure("Failed to get user invitation Ids from database");
            }
        });
    }

    public static void getInvitationFromDatabase(String invitationId, final InvitationCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("invitations")
                .child(invitationId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Invitation invitation = dataSnapshot.getValue(Invitation.class);
                callback.onInvitationRetrieved(invitation);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure("Failed getting invitation from database");
            }
        });

    }

    public static void getUserInvitationsFromDatabase(String userId, final InvitationCallback callback) {
        getUserInvitationIdsFromDatabase(userId, new InvitationIdsCallback() {
            @Override
            public void onInvitationIdsRetrieved(List<String> invitationIds) {
                for (String invitationId : invitationIds) {
                    getInvitationFromDatabase(invitationId, callback);
                }
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, error);
            }
        });

    }

    public static void getUserFromDatabaseById(String userId, final UserCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users")
                .child(userId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    callback.onFailure("No user object in database");
                }
                callback.onUserRetrieved(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure("Failed getting user from database");
            }
        });
    }

}
