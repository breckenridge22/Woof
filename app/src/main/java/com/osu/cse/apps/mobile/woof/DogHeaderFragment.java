package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

public class DogHeaderFragment extends DogFragment {

    private TextView mDogNameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dog_header, container, false);
        mDogNameTextView = v.findViewById(R.id.dog_name_header);
        updateUI();

        return v;
    }

    public void updateUI() {
        mDogNameTextView.setText(getDog().getName());
    }

}
