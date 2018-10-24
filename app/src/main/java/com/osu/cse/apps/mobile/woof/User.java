package com.osu.cse.apps.mobile.woof;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.osu.cse.apps.mobile.woof.CurrentUser.removeUserValueEventListener;

public class User {

    private String userId;
    private String fName;
    private String lName;
    private List<String> familyIdList = new ArrayList();
    private Map<String, Family> familyMap = new HashMap();
    private DogList dogList = new DogList();

    public User() {
        // Default constructor for Firebase
    }

    public User(String userId, String fName, String lName, Family family) {
        this.userId = userId;
        this.fName = fName;
        this.lName = lName;
        familyIdList.add(family.getfamilyId());
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

    // return list of keys from familyList map object
    public List<String> getfamilyIdList() {
        return familyIdList;
    }

    // return family map
    public Map<String, Family> getfamilyMap() {
        return familyMap;
    }

    // This method is called whenever a change is made in the database to one of the families
    // to which the current user belongs.  Replaces the old family object in the familyMap with
    // the updated family object from the database.
    public void updateFamily(Family family) {
        String familyId = family.getfamilyId();
        familyMap.put(familyId, family);
    }

    public List<Dog> getdogList() {
        return dogList.getDogList();
    }

    public void deleteDogFromList(String dogId) {
        dogList.deleteDog(dogId);
    }

    public void updateDog(Dog dog) {
        dogList.updateDog(dog);
    }

    public Dog getDog(String dogId) {
        return dogList.getDog(dogId);
    }

    public String getFamilyNameById(String familyId) {
        return familyMap.get(familyId).getfamilyName();
    }


    public void changeFirstName(String firstName) {
        this.fName = firstName;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(userId).setValue(toMap());
    }

    public void changeLastName(String lastName) {
        this.lName = lastName;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(userId).setValue(toMap());
    }

    public void deleteUser(){

        // step 1 remove value event listener
        CurrentUser.removeUserValueEventListener(userId);

        // step 2 delete dogs
        List<Dog> dogs = dogList.getDogList();
        for (int i=0; i < dogs.size(); i++){
            dogs.get(i).deleteDog();
        }

        // step 3 delete family - right now only get the first family because its the only, later
        // this will loop through all families in the family id list.
        Family family = CurrentUser.get().getfamilyMap().get(familyIdList.get(0));
        family.deleteFamily();

        // step 4 delete user from database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap();
        childUpdates.put("/users/" + userId, null);
        ref.updateChildren(childUpdates);

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
        result.put("familyIdList", getfamilyIdList());
        return result;
    }

}
