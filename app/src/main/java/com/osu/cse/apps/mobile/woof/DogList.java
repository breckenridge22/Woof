package com.osu.cse.apps.mobile.woof;

/*
 * Contains a list of all of the dogs associated with a User.  There is a one-to-one
 * relationship between User and DogList and a one-to-many relationship between DogList
 * and Dog.  This class sits between User and Dog in the UML diagram.
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DogList {

    private Map<UUID, Dog> mDogList;

    public DogList() {
        mDogList = new HashMap<>();
    }

    private void setDogList(UUID userId) {
        // code to fetch dogs associated with a user from a database
    }

    public void addDogToList(Dog dog) {
        mDogList.put(dog.getDogId(), dog);
        // code to add user to dog in dog database
    }

    public ArrayList<Dog> getDogList() {
        return (ArrayList) mDogList.values();
    }

}
