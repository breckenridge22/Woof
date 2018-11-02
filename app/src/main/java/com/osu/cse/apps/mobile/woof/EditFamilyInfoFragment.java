package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class EditFamilyInfoFragment extends FamilyFragment {

    private static final String TAG = "EditFamilyInfoFragment";
    private String mFamilyName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() called");

        View v = inflater.inflate(R.layout.fragment_edit_family_info, container, false);

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

        Button saveChangesButton = v.findViewById(R.id.save_changes_button);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: implement logic to update family name in database
            }
        });

        return v;
    }

}
