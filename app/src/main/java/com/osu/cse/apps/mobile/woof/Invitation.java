package com.osu.cse.apps.mobile.woof;

public class Invitation {

    private String invitationId;
    private FamilyInfo familyInfo;
    private String inviterName;

    public Invitation() {
        // default constructor for Firebase (actually, not sure if this will be used directly
        // to get or set data from Firebase)
    }

    public Invitation(String invitationId, FamilyInfo familyInfo, String inviterName) {
        this.invitationId = invitationId;
        this.familyInfo = familyInfo;
        this.inviterName = inviterName;
    }

    public String inviationId() {
        return invitationId;
    }

    public FamilyInfo getfamilyInfo() {
        return familyInfo;
    }

    public String getinviterName() {
        return inviterName;
    }

}
