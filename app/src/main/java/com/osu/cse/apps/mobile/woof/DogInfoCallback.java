package com.osu.cse.apps.mobile.woof;

public interface DogInfoCallback {
    void onDogInfoRetrieved(DogInfo dogInfo);
    void onFailure (String error);
}
