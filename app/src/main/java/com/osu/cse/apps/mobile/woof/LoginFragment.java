package com.osu.cse.apps.mobile.woof;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private Button mNewUserButton;
    private static final String TAG = "LoginFragment";
    private boolean logged = false;

    // Choose an arbitrary request code value
    private FirebaseAuth mAuth;

    private final static String OPT_NAME = "name";

    /*
     * Hosting activity must extend this interface
     */
    public interface Callbacks {
        void initFireBase();
    }

    private Callbacks mCallbacks;

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
        mLoginButton = v.findViewById(R.id.login_button);
        mNewUserButton = v.findViewById(R.id.new_user_button);
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

            if (mLoginButton != null) {
                mLoginButton.setOnClickListener(this);
            }
            if (mNewUserButton != null) {
                mNewUserButton.setOnClickListener(this);
            }

        return v;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mCallbacks = (Callbacks) context ;
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        mCallbacks.initFireBase();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getActivity(),"Account Created",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            logged = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(),"Invalid Account Creation",Toast.LENGTH_SHORT).show();
                            logged = false;
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getActivity(),"Signed in",Toast.LENGTH_SHORT).show();
                            logged = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(),"Wrong Email or Password",Toast.LENGTH_SHORT).show();
                            logged = false;
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.new_user_button) {
            createAccount(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
            if(logged){
                Intent intent = HomeScreenActivity.newIntent(getActivity());
                startActivity(intent);
            }
        } else if (i == R.id.login_button) {
            signIn(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
            if(logged){
                Intent intent = HomeScreenActivity.newIntent(getActivity());
                startActivity(intent);
            }
        }
    }

}

