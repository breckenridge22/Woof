package com.osu.cse.apps.mobile.woof;

import java.util.List;

public interface FamilyIdsCallback {
    void onFamilyIdsRetrieved(List<String> familyIdList);
    void onError(String errorMessage);
}
