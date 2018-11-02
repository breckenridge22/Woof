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

public class CreateNewFamilyFragment extends Fragment {

    private static final String TAG = "CreateNewFamilyFragment";
    private String mFamilyName;

    public static CreateNewFamilyFragment newInstance() {
        return new CreateNewFamilyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_new_family, container, false);

        EditText familyNameEditText = v.findViewById(R.id.family_name_edit_text);
        familyNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // required
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "onTextChanged() called");
                mFamilyName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // required
            }
        });


        Button createButton = v.findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: implement on-click functionality for family create button
            }
        });

        return v;
    }

}
