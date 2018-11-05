package com.osu.cse.apps.mobile.woof;

import java.util.HashMap;
import java.util.Map;

public class Invitation {

    private String invitationId;
    private FamilyInfo familyInfo;
    private String familyId;
    private String inviteeId;
    private String inviterId;

    public Invitation() {
        // default constructor for Firebase (actually, not sure if this will be used directly
        // to get or set data from Firebase)
    }

    public Invitation(String invitationId, String familyId, String inviteeId, String inviterId) {
        this.invitationId = invitationId;
        this.familyId = familyId;
        this.inviteeId = inviteeId;
        this.inviterId = inviterId;
    }

    public Invitation(String invitationId, FamilyInfo familyInfo, String inviterId) {
        this.invitationId = invitationId;
        this.familyInfo = familyInfo;
        this.inviterId = inviterId;
    }

    public String getinvitationId() {
        return invitationId;
    }

    public FamilyInfo getfamilyInfo() {
        return familyInfo;
    }

    public String getfamilyId() {
        return familyId;
    }

    public String getinviteeId() {
        return inviteeId;
    }

    public String getinviterId() {
        return inviterId;
    }

    public Map<String, String> toMap() {
        Map<String, String> result = new HashMap();
        result.put("invitationId", invitationId);
        result.put("familyId", familyId);
        result.put("inviteeId", inviteeId);
        result.put("inviterId", inviterId);
        return result;
    }

}
