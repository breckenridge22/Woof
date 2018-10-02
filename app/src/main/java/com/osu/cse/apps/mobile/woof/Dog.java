package com.osu.cse.apps.mobile.woof;

import java.util.UUID;

public class Dog {

    private UUID mDogId;
    private String mName;

    public Dog(UUID dogId) {
        mDogId = dogId;
    }

    public UUID getDogId() {
        return mDogId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
