package com.osu.cse.apps.mobile.woof;

import java.util.ArrayList;
import java.util.UUID;

public class User {

    private UUID mUserId;
    private DogList mDogList;

    public ArrayList<Dog> getDogList() {
        return mDogList.getDogList();
    }

    public Dog getDog(UUID dogId) {
        return mDogList.getDog(dogId);
    }

}
