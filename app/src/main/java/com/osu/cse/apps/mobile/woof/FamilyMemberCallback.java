package com.osu.cse.apps.mobile.woof;

public interface FamilyMemberCallback {
    void onFamilyMemberRetrieved(User familyMember);
    void onFailure(String error);
}
