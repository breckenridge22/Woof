package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public class FamilyFragment extends Fragment {

    private static final String ARG_FAMILY_ID = "family_id";
    private static final String TAG = "FamilyFragment";
    private String mFamilyId;
    private Family mFamily;
    private FamilyFragment.Callbacks mCallbacks;

    /*
     * Hosting activity must implement this interface
     */
    public interface Callbacks {
        void onMenuButtonSelected(int fragmentId);
        void onFamilyInfoChanged();
    }

    public void setArgs(String familyId) {
        Bundle args = new Bundle();
        args.putString(ARG_FAMILY_ID, familyId);
        setArguments(args);
    }

    /*
     * When fragment is attached to activity, Callbacks object is assigned
     * to member instance variable to allow fragment to call methods in hosting activity
     * (allows fragment to tell activity to add a new activity)
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach() called");
        mCallbacks = (FamilyFragment.Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() called");
        Bundle args = getArguments();
        if (args == null) {
            Log.d(TAG, "Error: Expected family_id but received null");
        }
        else if (!args.containsKey(ARG_FAMILY_ID)) {
            Log.d(TAG,"Error: Expected key family_id not found.");
        }
        else {
            mFamilyId = args.getString(ARG_FAMILY_ID);
            Log.d(TAG, "Calling CurrentUser.getFamilyFromDatabase()");
            CurrentUser.getFamilyFromDatabase(mFamilyId, new FamilyCallback() {
                @Override
                public void onFamilyChange(Family family) {
                    Log.d(TAG, "onFamilyChange()");
                    if (family == null) {
                        Log.d(TAG, "Family is null");
                    }
                    else {
                        mFamily = family;
                        if (mCallbacks != null) {
                            mCallbacks.onFamilyInfoChanged();
                        }
                    }
                }

                @Override
                public void onFailure(String error) {
                    Log.d(TAG, error);
                }
            });
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach() called");
        mCallbacks = null;
    }

    public FamilyFragment.Callbacks getCallbacks() {
        return mCallbacks;
    }

    public String getFamilyId() {
        return mFamilyId;
    }

    public Family getFamily() {
        return mFamily;
    }

    public void updateUI() {}

}
