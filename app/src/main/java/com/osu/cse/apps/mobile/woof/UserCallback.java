package com.osu.cse.apps.mobile.woof;

public interface UserCallback {
    void onUserRetrieved(User user);
    void onFailure(String error);
}
