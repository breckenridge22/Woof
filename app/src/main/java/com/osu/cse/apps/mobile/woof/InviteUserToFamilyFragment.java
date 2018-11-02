package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class InviteUserToFamilyFragment extends FamilyFragment {

    private static final String TAG = "InviteUserToFamilyFragment";
    private String mUserEmail;
    private ConstraintLayout mUserResultLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_invite_user_to_family, container, false);

        EditText userEmailEditText = v.findViewById(R.id.user_email_edit_text);
        userEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // required
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "onTextChanged() called");
                mUserEmail = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // required
            }
        });

        Button findUserButton = v.findViewById(R.id.find_user_button);
        findUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserResultLayout.setVisibility(View.VISIBLE);
            }
        });

        mUserResultLayout = v.findViewById(R.id.user_result_layout);
        mUserResultLayout.setVisibility(View.INVISIBLE);

        return v;
    }

}
