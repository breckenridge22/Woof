package com.osu.cse.apps.mobile.woof;

public interface DogCallback {
    void onDogChange(Dog dog);
    void onFailure(String error);
}
