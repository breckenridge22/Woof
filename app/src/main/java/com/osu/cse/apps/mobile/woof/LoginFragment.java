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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mFNameEditText;
    private EditText mLNameEditText;
    private TextView mFNameTextView;
    private TextView mLNameTextView;
    private Button mLoginButton;
    private Button mNewUserButton;
    private static final String TAG = "LoginFragment";

    private boolean newU = true;

    public static FirebaseAuth mAuth;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

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
        mCallbacks.initFireBase();

        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        v = inflater.inflate(R.layout.fragment_login, container, false);

        mUsernameEditText = v.findViewById(R.id.username_text);
        mPasswordEditText = v.findViewById(R.id.password_text);
        mFNameEditText = v.findViewById(R.id.fName_text);
        mLNameEditText = v.findViewById(R.id.lName_text);
        mFNameTextView = v.findViewById(R.id.fName_textV);
        mLNameTextView = v.findViewById(R.id.lName_textV);
        mLoginButton = v.findViewById(R.id.login_button);
        mNewUserButton = v.findViewById(R.id.new_user_button);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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
        //mCallbacks.initFireBase();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            Log.d(TAG, "OnStart Login");
            Toast.makeText(getActivity(),"Signed in",Toast.LENGTH_SHORT).show();
            Intent intent = HomeScreenActivity.newIntent(getActivity());
            startActivity(intent);
        }
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getActivity(),"Account Created",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "Created User ID: " + user.getUid());
                            writeNewUser(user.getUid(), mFNameEditText.getText().toString(), mLNameEditText.getText().toString());
                            signIn(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(),"Invalid Account Creation",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                            Toast.makeText(getActivity(),"Signed in",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = HomeScreenActivity.newIntent(getActivity());
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(),"Wrong Email or Password",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // [END sign_in_with_email]
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.new_user_button) {
            if(newU){
                mFNameTextView.setVisibility(View.VISIBLE);
                mLNameTextView.setVisibility(View.VISIBLE);
                mFNameEditText.setVisibility(View.VISIBLE);
                mLNameEditText.setVisibility(View.VISIBLE);
                mLoginButton.setVisibility(View.GONE);
                newU = false;
            } else {
                createAccount(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
            }
        } else if (i == R.id.login_button) {
            signIn(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
        }
    }

    public static void signOut() {
        mAuth.signOut();
        Log.d(TAG, "Current User:" + mAuth.getCurrentUser());
    }

    private void writeNewUser(String userId, String fName, String lName) {
        User user = new User(userId, fName, lName);

        mDatabase.child("users").child(userId).setValue(user);
    }


}

