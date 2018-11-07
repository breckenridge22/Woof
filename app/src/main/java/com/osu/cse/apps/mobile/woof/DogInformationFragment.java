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
    private EditText mDogNameEditText;
    private TextView mFamilyNameTextView;
    private Button mSaveChangesButton;
    private String mDogName;
    private String mFamilyName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_dog_information, container, false);

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
        String familyId = getFamilyId();
        if (familyId != null) {
            CurrentUser.getFamilyInfoFromDatabaseById(familyId, new FamilyInfoCallback() {
                @Override
                public void onFamilyInfoRetrieved(FamilyInfo familyInfo) {
                    if (familyInfo == null) {
                        Log.d(TAG, "Null family info object obtained from database");
                        return;
                    }
                    mFamilyName = familyInfo.getfamilyName();
                    if (mFamilyName == null) {
                        Log.d(TAG, "Null family string in family info object from database");
                        return;
                    }
                    mFamilyNameTextView.setText(mFamilyName);
                }

                @Override
                public void onFailure(String error) {
                    Log.d(TAG, error);
                }
            });
        }

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
        mDogNameEditText.setText(dog.getdogName());
    }

}
