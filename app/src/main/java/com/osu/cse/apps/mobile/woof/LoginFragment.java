package com.osu.cse.apps.mobile.woof;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class LoginFragment extends Fragment {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    // Choose an arbitrary request code value
    private FirebaseAuth mAuth;

    private final static String OPT_NAME = "name";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        Activity activity = getActivity();

        //if (activity != null) {
            int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
            //if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            //  v = inflater.inflate(R.layout.fragment_login_land, container, false);
            //} else {
            //gv = inflater.inflate(R.layout.fragment_login, container, false);
            //}
        //} else {
            v = inflater.inflate(R.layout.fragment_login, container, false);
        //}

        mUsernameEditText = v.findViewById(R.id.username_text);
        mPasswordEditText = v.findViewById(R.id.password_text);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

            /*Button loginButton = v.findViewById(R.id.login_button);
            if (loginButton != null) {
                loginButton.setOnClickListener(this);
            }
            Button newUserButton = v.findViewById(R.id.new_user_button);
            if (newUserButton != null) {
                newUserButton.setOnClickListener(this);
            }*/

        return v;
    }

}

