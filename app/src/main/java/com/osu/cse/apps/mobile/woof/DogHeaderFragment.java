package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DogHeaderFragment extends DogFragment {

    private TextView mDogNameTextView;
    private static final String TAG = "DogFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_dog_header, container, false);
        mDogNameTextView = v.findViewById(R.id.dog_name_header);
        updateUI();

        return v;
    }

    public void updateUI() {
        mDogNameTextView.setText(getDog().getName());
    }

}
