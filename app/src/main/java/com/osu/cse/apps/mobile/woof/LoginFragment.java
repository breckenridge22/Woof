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
import android.text.TextUtils;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mFNameEditText;
    private EditText mLNameEditText;
    private TextView mFNameTextView;
    private TextView mLNameTextView;
    private TextView mFamilyNameTextView;
    private EditText mFamilyNameEditText;
    private Button mLoginButton;
    private Button mNewUserButton;
    private Button mCancelButton;
    private static final String TAG = "LoginFragment";

    private boolean mNewU = true;

    public static FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    private final static String OPT_NAME = "name";

    /*
     * Hosting activity must implement this interface
     */
    public interface Callbacks {
        void initFireBase();
    }

    private Callbacks mCallbacks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance();

        // Ensure that setPersistenceEnabled has not been called before (if called after any other
        // usage of the FirebaseDatabase instance, app will crash).  Otherwise, set database persistence.
        if (!CurrentUser.isDatabasePersistenceEnabled()) {
            databaseInstance.setPersistenceEnabled(true);
            CurrentUser.setDatabasePersistenceEnabled(true);
        }

        mDatabase = databaseInstance.getReference();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        mCallbacks.initFireBase();
        v = inflater.inflate(R.layout.fragment_login, container, false);

        mUsernameEditText = v.findViewById(R.id.username_text);
        mPasswordEditText = v.findViewById(R.id.password_text);
        mFNameEditText = v.findViewById(R.id.fName_text);
        mLNameEditText = v.findViewById(R.id.lName_text);
        mFNameTextView = v.findViewById(R.id.fName_textV);
        mLNameTextView = v.findViewById(R.id.lName_textV);
        mFamilyNameTextView = v.findViewById(R.id.family_name_text_view);
        mFamilyNameEditText = v.findViewById(R.id.family_name_edit_text);
        mLoginButton = v.findViewById(R.id.login_button);
        mNewUserButton = v.findViewById(R.id.new_user_button);
        mCancelButton = v.findViewById(R.id.cancel_button);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        if (mLoginButton != null) {
            mLoginButton.setOnClickListener(this);
        }
        if (mNewUserButton != null) {
            mNewUserButton.setOnClickListener(this);
        }
        if (mCancelButton != null) {
            mCancelButton.setOnClickListener(this);
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        //mCallbacks.initFireBase();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Log.d(TAG, "OnStart Login");
            Toast.makeText(getActivity(), "Signed in", Toast.LENGTH_SHORT).show();
            Intent intent = HomeScreenActivity.newIntent(getActivity());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    // Skeleton from https://github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/java/EmailPasswordActivity.java
    private void createAccount(final String email, String password) {
        Log.d(TAG, "createAccount: " + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getActivity(), "Account Created", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "Created User ID: " + user.getUid());
                            writeNewUser(user.getUid(), mFNameEditText.getText().toString(), mLNameEditText.getText().toString(), email, mFamilyNameEditText.getText().toString());
                            signIn(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Invalid Account Creation", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Skeleton from https://github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/java/EmailPasswordActivity.java
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
                            Toast.makeText(getActivity(), "Signed in", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = HomeScreenActivity.newIntent(getActivity());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // [END sign_in_with_email]
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.new_user_button) {
            if (mNewU) {
                mFNameTextView.setVisibility(View.VISIBLE);
                mLNameTextView.setVisibility(View.VISIBLE);
                mFNameEditText.setVisibility(View.VISIBLE);
                mLNameEditText.setVisibility(View.VISIBLE);
                mFamilyNameTextView.setVisibility(View.VISIBLE);
                mFamilyNameEditText.setVisibility(View.VISIBLE);
                mLoginButton.setVisibility(View.GONE);
                mCancelButton.setVisibility(View.VISIBLE);
                mNewU = false;
            } else {
                if (validateSignUpForm()) {
                    createAccount(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Please fill in all details.", Toast.LENGTH_LONG).show();
                }
            }
        } else if (i == R.id.login_button) {
            if (validateSignInForm()) {
                signIn(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
            } else {
                Toast.makeText(getActivity(), "Please Enter an email and password.", Toast.LENGTH_LONG).show();
            }
        } else if (i == R.id.cancel_button) {
            mFNameTextView.setVisibility(View.GONE);
            mLNameTextView.setVisibility(View.GONE);
            mFNameEditText.setVisibility(View.GONE);
            mLNameEditText.setVisibility(View.GONE);
            mFamilyNameTextView.setVisibility(View.GONE);
            mFamilyNameEditText.setVisibility(View.GONE);
            mLoginButton.setVisibility(View.VISIBLE);
            mCancelButton.setVisibility(View.GONE);
            mNewU = true;
        }
    }

    public static void signOut() {
        mAuth.signOut();
        Log.d(TAG, "Current User:" + mAuth.getCurrentUser());
    }

    // Skeleton from https://github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/java/EmailPasswordActivity.java
    private boolean validateSignInForm() {
        boolean valid = true;

        String userName = mUsernameEditText.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            mUsernameEditText.setError("Required.");
            valid = false;
        } else {
            mUsernameEditText.setError(null);
        }

        String password = mPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordEditText.setError("Required.");
            valid = false;
        } else {
            mPasswordEditText.setError(null);
        }

        return valid;
    }

    // Skeleton from https://github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/java/EmailPasswordActivity.java
    private boolean validateSignUpForm() {
        boolean valid = validateSignInForm();
        if (valid) {
            String email = mFNameEditText.getText().toString();
            if (TextUtils.isEmpty(email)) {
                mFNameEditText.setError("Required.");
                valid = false;
            } else {
                mFNameEditText.setError(null);
            }

            String password = mLNameEditText.getText().toString();
            if (TextUtils.isEmpty(password)) {
                mLNameEditText.setError("Required.");
                valid = false;
            } else {
                mLNameEditText.setError(null);
            }
            String familyName = mFamilyNameEditText.getText().toString();
            if (TextUtils.isEmpty(familyName)) {
                mFamilyNameEditText.setError("Required.");
                valid = false;
            } else {
                mFamilyNameEditText.setError(null);
            }
        }
        return valid;
    }

    private void writeNewUser(String userId, String fName, String lName, String email, String familyName) {

        // TODO: Get family name from user during user registration and/or ask to join existing family on login
        // Temporarily using this line to generate a random 5-character String for the family name
        Log.d(TAG, "writeNewUser() called");

        String familyId = mDatabase.child("families").push().getKey();
        Family family = new Family(familyId, familyName, userId);
        User user = new User(userId, fName, lName, email, family);

        Log.d(TAG, "User Id: " + userId);
        Log.d(TAG, "Family Id: " + familyId);
        Log.d(TAG, "User email: " + email);

        // bundle updates together into one map object so that "users" and "families"
        // can be updated atomically (either both will be written to the database
        // or neither will be written to the database)
        // souce: https://firebase.google.com/docs/database/android/read-and-write
        Map<String, Object> childUpdates = new HashMap();
        childUpdates.put("/users/" + userId, user.toMap());
        childUpdates.put("/families/" + familyId, family.toMap());
        //childUpdates.put("/emails/" + email, userId);
        mDatabase.updateChildren(childUpdates) // update database atomically
                // print toast and finish activity if database successfully updated
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Successfully added new user!",
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Successfully added new user to database");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Database update failed",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Failed to add new user to database");
            }
        });

    }


}

