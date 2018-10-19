package com.osu.cse.apps.mobile.woof;

import java.util.ArrayList;
import java.util.UUID;

public class User {

    private String mUserId;
    private String fName;
    private String lName;
    private DogList mDogList;

    public User(){
        //Default constructor for firebase
    }

    public User(String UUID, String fName, String lName){
        this.mUserId = UUID;
        this.fName = fName;
        this.lName = lName;
        this.mDogList = new DogList();
    }

    public String getUUID(){
        return this.mUserId;
    }

    public String getfName(){
        return this.fName;
    }

    public String getlName(){
        return this.lName;
    }

    public ArrayList<Dog> getDogList() {
        return mDogList.getDogList();
    }

    public Dog getDog(UUID dogId) {
        return mDogList.getDog(dogId);
    }

    public static User getTestUser() {
        User testUser = new User();
        testUser.mUserId = "";
        //testUser.mUserId = UUID.randomUUID();
        testUser.mDogList = DogList.getTestDogList();
        return testUser;
    }

}
