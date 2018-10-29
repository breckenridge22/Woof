/*
 * Used to wrap familyId and familyName as results from database queries
 */

package com.osu.cse.apps.mobile.woof;

public class FamilyInfo {

    private String familyId;
    private String familyName;

    public FamilyInfo() {
        // default constructor for Firebase
    }

    public String getfamilyId() {
        return familyId;
    }

    public String getfamilyName() {
        return familyName;
    }

}
