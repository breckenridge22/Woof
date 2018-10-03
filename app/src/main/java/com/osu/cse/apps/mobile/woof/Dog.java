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

        Dog testDog3 = new Dog(UUID.randomUUID());
        testDog3.setName("Woofy");

        Dog testDog4 = new Dog(UUID.randomUUID());
        testDog4.setName("Chubs");

        Dog testDog5 = new Dog(UUID.randomUUID());
        testDog5.setName("Gus");

        Dog testDog6 = new Dog(UUID.randomUUID());
        testDog6.setName("Fluffy");

        Dog testDog7 = new Dog(UUID.randomUUID());
        testDog7.setName("Fido");

        Dog testDog8 = new Dog(UUID.randomUUID());
        testDog8.setName("Ivy");

        Dog testDog9 = new Dog(UUID.randomUUID());
        testDog9.setName("Mr. Jingles");

        Dog testDog10 = new Dog(UUID.randomUUID());
        testDog10.setName("Rascal");

        Dog testDog11 = new Dog(UUID.randomUUID());
        testDog11.setName("Guinness");

        Dog testDog12 = new Dog(UUID.randomUUID());
        testDog12.setName("Beth");

        Dog testDog13 = new Dog(UUID.randomUUID());
        testDog13.setName("Doug the Pug");

        Dog testDog14 = new Dog(UUID.randomUUID());
        testDog14.setName("Porkchop");

        Dog testDog15 = new Dog(UUID.randomUUID());
        testDog15.setName("Ren");

        return new Dog[] {testDog1, testDog2, testDog3, testDog4, testDog5, testDog6, testDog7,
            testDog8, testDog9, testDog10, testDog11, testDog12, testDog13, testDog14, testDog15};
    }
}
