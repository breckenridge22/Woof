package com.osu.cse.apps.mobile.woof;

public interface FamilyCallback {
    void onFamilyChange(Family family);
    void onFailure(String error);
}
