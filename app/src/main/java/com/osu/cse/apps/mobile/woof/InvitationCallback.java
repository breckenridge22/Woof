package com.osu.cse.apps.mobile.woof;

public interface InvitationCallback {
    void onInvitationRetrieved(Invitation invitation);
    void onFailure(String error);
}
