package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

public class DogInformationFragment extends DogFragment {

    private static final String TAG = "DogInformationFragment";
    private TextView mDogIdTextView;
    private EditText mDogNameEditText;
    private TextView mFamilyNameTextView;
    private Button mSaveChangesButton;
    private String mDogName;

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
                mDogName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // required
            }
        });

        mFamilyNameTextView = v.findViewById(R.id.family);

        mSaveChangesButton = v.findViewById(R.id.save_changes_button);
        mSaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentUser.changeDogName(getFamilyId(), getDogId(), mDogName);
            }
        });

        return v;
    }

    @Override
    public void updateUI() {
        Dog dog = getDog();
        if (dog == null) {
            return;
        }
        mDogIdTextView.setText(getDogId());
        mDogNameEditText.setText(dog.getdogName());
        mFamilyNameTextView.setText(getFamilyId());
    }

}
