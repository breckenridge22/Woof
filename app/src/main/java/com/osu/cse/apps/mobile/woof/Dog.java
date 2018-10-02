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

    public static Dog[] getTestDogs() {
        Dog testDog1 = new Dog(UUID.randomUUID());
        testDog1.setName("Sparky");

        Dog testDog2 = new Dog(UUID.randomUUID());
        testDog2.setName("Hazel");

        return new Dog[] {testDog1, testDog2};
    }
}
