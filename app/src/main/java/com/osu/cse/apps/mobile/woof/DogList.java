package com.osu.cse.apps.mobile.woof;

/*
 * Contains a list of all of the dogs associated with a User.  There is a one-to-one
 * relationship between User and DogList and a one-to-many relationship between DogList
 * and Dog.  This class sits between User and Dog in the UML diagram.
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DogList {

    private Map<String, Dog> mDogList = new HashMap();

    public DogList() {
        // default constructor
    }

    private void setDogList(String userId) {
        // code to fetch dogs associated with a user from a database
    }

    public List<Dog> getDogList() {
        return new ArrayList(mDogList.values());
    }

    public Dog getDog(String dogId) {
        return mDogList.get(dogId);
    }

    public void updateDog(Dog dog) {
        String dogId = dog.getdogId();
        mDogList.put(dogId, dog);
    }

    public void deleteDog(String dogId) {
        mDogList.remove(dogId);
    }

}
