package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

public class DogInformationFragment extends DogFragment {

    private static final String TAG = "DogInformationFragment";
    private TextView mDogIdTextView;
    private EditText mDogNameEditText;
    private TextView mFamilyNameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_dog_information, container, false);

        mDogIdTextView = v.findViewById(R.id.dog_id);

        mDogNameEditText = v.findViewById(R.id.dog_name);
        mDogNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // required
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "onTextChanged() called");
                getDog().changedogName(s.toString());
                getCallbacks().onDogNameChanged(); // update header
            }

            @Override
            public void afterTextChanged(Editable s) {
                // required
            }
        });
        mDogNameEditText.setText(getDog().getdogName());

        mFamilyNameTextView = v.findViewById(R.id.family);

        updateUI();

        return v;
    }

    public void updateUI() {
        mDogIdTextView.setText(getDog().getdogId());
        mFamilyNameTextView.setText(getDog().getFamilyName(CurrentUser.get(),
                getDog().getfamilyId()));
    }

}
