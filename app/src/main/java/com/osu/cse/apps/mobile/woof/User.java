package com.osu.cse.apps.mobile.woof;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String userId;
    private String fName;
    private String lName;
    private String email;
    private Map<String, Boolean> familyIds;
    private Map<String, Boolean> invitationIds;
    private static final String TAG = "User";

    public User() {
        // default constructor for Firebase
        familyIds = new HashMap();
    }

    public User(String userId, String fName, String lName, String email, Family family) {
        this.userId = userId;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        familyIds = new HashMap();
        familyIds.put(family.getfamilyId(), true);
    }

    public String getuserId() {
        return userId;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getemail() {
        return email;
    }

    public Map<String, Boolean> getfamilyIds() {
        return familyIds;
    }

    public Map<String, Boolean> getinvitationIds() {
        return invitationIds;
    }



    public void changeEmail(String email) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(userId).child("email").setValue(email);
    }

    public void changeFirstName(String firstName) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(userId).child("fName").setValue(firstName);
    }

    public void changeLastName(String lastName) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(userId).child("lName").setValue(lastName);
    }

    // Use this method to generate map of key value pairs that can be stored for user in
    // database.  Better than just storing the user object directly so that extraneous
    // getters such as getfamilyNameList() don't cause extraneous information to be written
    // to the database.
    // source: https://firebase.google.com/docs/database/android/read-and-write
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap();
        result.put("userId", userId);
        result.put("fName", fName);
        result.put("lName", lName);
        result.put("email", email);
        result.put("familyIds", familyIds);
        return result;
    }

}
