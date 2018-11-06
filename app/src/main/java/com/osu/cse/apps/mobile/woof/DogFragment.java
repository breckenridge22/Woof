package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class DogFragment extends Fragment {

    private static final String ARG_DOG_ID = "dog_id";
    private static final String ARG_FAMILY_ID = "family_id";
    private static final String TAG = "DogFragment";
    private String mFamilyId;
    private String mDogId;
    private Dog mDog;
    private Callbacks mCallbacks;

    /*
     * Hosting activity must implement this interface
     */
    public interface Callbacks {
        void onMenuButtonSelected(int fragmentId);
        void onDogInfoChanged();
    }

    public void setArgs(String dogId, String familyId) {
        Bundle args = new Bundle();
        args.putString(ARG_DOG_ID, dogId);
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
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() called");
        Bundle args = getArguments();
        if (args == null) {
            Log.d(TAG, "Error: Expected ids but received null");
        }
        else if (!args.containsKey(ARG_DOG_ID)) {
            Log.d(TAG, "Error: Expected key dog_id not found.");
        }
        else if (!args.containsKey(ARG_FAMILY_ID)) {
            Log.d(TAG,"Error: Expected key family_id not found.");
        }
        else {
            mDogId = args.getString(ARG_DOG_ID);
            mFamilyId = args.getString(ARG_FAMILY_ID);
            CurrentUser.getDogFromDatabase(mDogId, mFamilyId, new DogCallback() {
                @Override
                public void onDogChange(Dog dog) {
                    if (dog == null) {
                        Log.d(TAG, "Dog is null");
                    }
                    else {
                        mDog = dog;
                        if (mCallbacks != null) {
                            mCallbacks.onDogInfoChanged();
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

    public Callbacks getCallbacks() {
        return mCallbacks;
    }

    public String getDogId() {
        return mDogId;
    }

    public String getFamilyId() {
        return mFamilyId;
    }

    public Dog getDog() {
        return mDog;
    }

    public void updateUI() {
        Log.d(TAG, "updateUI() called");
    }
}
