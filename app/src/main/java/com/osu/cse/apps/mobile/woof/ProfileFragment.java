package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment implements View.OnClickListener {


    private Button mReauthenticateButton;
    private Button mEmailUpdateButton;
    private Button mPasswordUpdateButton;
    private Button mFNameUpdateButton;
    private Button mLNameUpdateButton;
    private EditText mEmailText;
    private EditText mPasswordText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mFNameEditText;
    private EditText mLNameEditText;
    private LinearLayout mReauthLayout;
    private LinearLayout mUpdateLayout;
    private FirebaseUser mUser;
    private User mCurrentUser;
    private static final String TAG = "ProfileFragment";


    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    /*
     * Hosting activity must extend this interface
     */
    public interface Callbacks {
        LayoutInflater getInflater();
    }

    private Callbacks mCallbacks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CurrentUser.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onUserRetrieved(User user) {
                mCurrentUser = user;
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, "Failed to retrieve current user object from database");
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        mReauthenticateButton = v.findViewById(R.id.reauthenticate);
        mEmailUpdateButton = v.findViewById(R.id.update_email_button);
        mPasswordUpdateButton = v.findViewById(R.id.update_password_button);
        mFNameUpdateButton = v.findViewById(R.id.update_fName_button);
        mLNameUpdateButton = v.findViewById(R.id.update_lName_button);
        mEmailText = v.findViewById(R.id.re_email);
        mPasswordText = v.findViewById(R.id.re_password);
        mEmailEditText = v.findViewById(R.id.update_email_edit);
        mPasswordEditText = v.findViewById(R.id.update_pass_edit);
        mFNameEditText = v.findViewById(R.id.update_fName_edit);
        mLNameEditText = v.findViewById(R.id.update_lName_edit);
        mReauthLayout = v.findViewById(R.id.reauth_layout);
        mUpdateLayout = v.findViewById(R.id.update_layout);

        if (mReauthenticateButton != null) {
            mReauthenticateButton.setOnClickListener(this);
        }
        if (mEmailUpdateButton != null) {
            mEmailUpdateButton.setOnClickListener(this);
        }
        if (mPasswordUpdateButton != null) {
            mPasswordUpdateButton.setOnClickListener(this);
        }
        if (mFNameUpdateButton != null) {
            mFNameUpdateButton.setOnClickListener(this);
        }
        if (mLNameUpdateButton != null) {
            mLNameUpdateButton.setOnClickListener(this);
        }

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (ProfileFragment.Callbacks) context;
    }

    // Skeleton from https://firebase.google.com/docs/auth/android/manage-users
    public void reauthenticate() {
        if(validateSignInForm()) {
            AuthCredential credential = EmailAuthProvider
                    .getCredential(mEmailText.getText().toString(), mPasswordText.getText().toString());

            // Prompt the user to re-provide their sign-in credentials
            mUser.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User re-authenticated.");
                                mReauthLayout.setVisibility(View.GONE);
                                mUpdateLayout.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "Successfully re-authenticated", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "Re-authentication failed.");
                                Toast.makeText(getActivity(), "Re-authentication failed.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Please fill in the form.", Toast.LENGTH_SHORT).show();
        }
    }

    // Skeleton from https://github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/java/EmailPasswordActivity.java
    private boolean validateSignInForm() {
        boolean valid = true;

        String userName = mEmailText.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            mEmailText.setError("Required.");
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        String password = mPasswordText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordText.setError("Required.");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }

        return valid;
    }

    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.reauthenticate:
                reauthenticate();
                break;
            case R.id.update_email_button:
                Log.d(TAG, "Email Update button pressed.");
                String email = mEmailEditText.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    mEmailEditText.setError("Required.");
                } else {
                    mEmailEditText.setError(null);
                    mUser.updateEmail(mEmailEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        String email = mEmailEditText.getText().toString();
                                        String userId = mCurrentUser.getuserId();
                                        Task updateTask = FirebaseDatabase.getInstance().getReference()
                                                .child("users").child(userId).child("email").setValue(email);
                                        if (CurrentUser.isConnectedToDatabase()) {
                                            updateTask.addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    Toast.makeText(getActivity(), "Updated Email", Toast.LENGTH_SHORT).show();
                                                    Log.d(TAG, "User email address updated.");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "Failed to change email");
                                                }
                                            });
                                        }
                                        else {
                                            Toast.makeText(getActivity(), "Updated Email", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "User email address updated.");
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "Invalid new email", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "User email address not updated.");
                                    }
                                }
                            });
                }
                break;
            case R.id.update_password_button:
                Log.d(TAG, "Password Update button pressed.");
                String pass = mPasswordEditText.getText().toString();
                if (TextUtils.isEmpty(pass)) {
                    mPasswordEditText.setError("Required.");
                } else {
                    mPasswordEditText.setError(null);
                    mUser.updatePassword(mPasswordEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Updated Password", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "User password address updated.");
                                    } else {
                                        Toast.makeText(getActivity(), "Invalid new password", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "User password not updated.");
                                    }
                                }
                            });
                }
                break;
            case R.id.update_fName_button:
                Log.d(TAG, "First name Update button pressed.");
                String fName = mFNameEditText.getText().toString();
                if (TextUtils.isEmpty(fName)) {
                    mFNameEditText.setError("Required.");
                } else {
                    mFNameEditText.setError(null);
                    String userId = mCurrentUser.getuserId();
                    String firstName = mFNameEditText.getText().toString();
                    Task updateTask = FirebaseDatabase.getInstance().getReference().child("users")
                            .child(userId).child("fName").setValue(firstName);
                    if (CurrentUser.isConnectedToDatabase()) {
                        updateTask.addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(getActivity(), "Updated First Name", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Failed to change first name");
                            }
                        });
                    }
                    else {
                        Toast.makeText(getActivity(), "Updated First Name", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.update_lName_button:
                Log.d(TAG, "Last name Update button pressed.");
                String lName = mLNameEditText.getText().toString();
                if (TextUtils.isEmpty(lName)) {
                    mLNameEditText.setError("Required.");
                } else {
                    mLNameEditText.setError(null);
                    String userId = mCurrentUser.getuserId();
                    String lastName = mLNameEditText.getText().toString();
                    Task updateTask = FirebaseDatabase.getInstance().getReference().child("users")
                            .child(userId).child("lName").setValue(lastName);
                    if (CurrentUser.isConnectedToDatabase()) {
                        updateTask.addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(getActivity(), "Updated Last Name", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Failed to change last name");
                            }
                        });
                    }
                    else {
                        Toast.makeText(getActivity(), "Updated Last Name", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


}
