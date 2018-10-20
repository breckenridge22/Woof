package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class DogFragment extends Fragment {

    private static final String ARG_DOG_ID = "dog_id";
    private static final String TAG = "DogFragment";
    private Dog mDog;
    private Callbacks mCallbacks;

    /*
     * Hosting activity must implement this interface
     */
    public interface Callbacks {
        void onMenuButtonSelected(int fragmentId);
        void onDogNameChanged();
    }

    public void setArgs(String dogId) {
        Bundle args = new Bundle();
        args.putString(ARG_DOG_ID, dogId);
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
        String dogId = getArguments().getString(ARG_DOG_ID);
        mDog = CurrentUser.get().getDog(dogId);
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

    public Dog getDog() {
        return mDog;
    }
}
