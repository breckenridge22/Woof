package com.osu.cse.apps.mobile.woof;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteFragment extends Fragment implements View.OnClickListener {


    private Button mYesButton;
    private Button mNoButton;
    private Button mReauthenticateButton;
    private EditText mEmailText;
    private EditText mPasswordText;
    private LinearLayout mReauthLayout;
    private LinearLayout mDeleteLayout;
    private static final String TAG = "DeleteFragment";
    private FirebaseUser mUser;
    private int mFamilyCount = 0;
    private int mCountFamiliesChecked = 0;
    private boolean mFamilyCountFailure = false;
    private List<DatabaseReference> mFamiliesToDelete = new ArrayList();
    private List<DatabaseReference> mUserIdListsToDeleteUserFrom = new ArrayList();


    public static FirebaseAuth mAuth;

    public static DeleteFragment newInstance() {
        return new DeleteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View v;
        v = inflater.inflate(R.layout.fragment_delete_confirm, container, false);

        mAuth = FirebaseAuth.getInstance();

        mYesButton = v.findViewById(R.id.yes_button);
        mNoButton = v.findViewById(R.id.no_button);
        mReauthenticateButton = v.findViewById(R.id.reauthenticate_delete);
        mEmailText = v.findViewById(R.id.re_email);
        mPasswordText = v.findViewById(R.id.re_password);
        mReauthLayout = v.findViewById(R.id.reauth_layout);
        mDeleteLayout = v.findViewById(R.id.delete_layout);
        mUser = FirebaseAuth.getInstance().getCurrentUser();


        if (mYesButton != null) {
            mYesButton.setOnClickListener(this);
        }
        if (mNoButton != null) {
            mNoButton.setOnClickListener(this);
        }
        if (mReauthenticateButton != null) {
            mReauthenticateButton.setOnClickListener(this);
        }
        return v;
    }

    // Skeleton from https://firebase.google.com/docs/auth/android/manage-users
    public void reauthenticate() {
        if (validateSignInForm()) {
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
                                mDeleteLayout.setVisibility(View.VISIBLE);
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
        Intent intent;
        FirebaseUser aUser = mAuth.getCurrentUser();
        switch (i) {
            case R.id.reauthenticate_delete:
                Log.d(TAG, "Reauth button pressed");
                reauthenticate();
                break;
            case R.id.yes_button:
                aUser.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User account deleted.");
                                    deleteUserFromDatabase(new DeleteUserCallback() {
                                        @Override
                                        public void onUserDeleted() {
                                            Log.d(TAG, "onUserDeleted() called");
                                            CurrentUser.setNull();
                                            Intent intent = LoginActivity.newIntent(getActivity());
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            Toast.makeText(getActivity(), "Deleted Account", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(String error) {
                                            Log.d(TAG, "onFailure() called::" + error);
                                            Toast.makeText(getActivity(), "Failed to delete account", Toast.LENGTH_SHORT).show();
                                            getActivity().finish();
                                        }
                                    });
                                }
                            }
                        });
                break;
            case R.id.no_button:
                intent = SettingsActivity.newIntent(getActivity());
                startActivity(intent);
                break;
        }
    }

    private interface DeleteUserCallback {
        void onUserDeleted();

        void onFailure(String error);
    }

    /*
     * Deletes user, deletes all families of which user is the sole member, and deletes userId
     * from families of which user is a member but not the sole member. Verifies that all families of which user is a
     * member have been checked before making deletions.  All deletions are done atomically.
     */
    private void deleteUserFromDatabase(final DeleteUserCallback callback) {
        CurrentUser.getFamilyIdsFromDatabase(new FamilyIdsCallback() {
            @Override
            public void onFamilyIdsRetrieved(List<String> familyIdList) {
                mFamilyCount = familyIdList.size();
                for (String familyId : familyIdList) {
                    checkNumFamilyMembersInDatabase(familyId, new FamilyMemberNumberCallback() {
                        @Override
                        public void onFamilyMembersCounted() {
                            mCountFamiliesChecked++;
                            if (mCountFamiliesChecked == mFamilyCount && !mFamilyCountFailure) {
                                Map<String, Object> childUpdates = new HashMap();
                                String path;

                                // user to be deleted
                                path = CurrentUser.convertDatabaseURLtoPath(CurrentUser
                                        .getUserDatabaseRef().toString());
                                childUpdates.put(path, null);
                                Log.d(TAG, "Attempting to delete user at: " + path);

                                // families to be deleted
                                for (DatabaseReference ref : mFamiliesToDelete) {
                                    path = CurrentUser.convertDatabaseURLtoPath(ref.toString());
                                    childUpdates.put(path, null);
                                    Log.d(TAG, "Attempting to delete family at: " + path);
                                }

                                // references to userId to be deleted
                                String userId = CurrentUser.getUserId();
                                for (DatabaseReference ref : mUserIdListsToDeleteUserFrom) {
                                    path = CurrentUser.convertDatabaseURLtoPath(ref.child(userId).toString());
                                    childUpdates.put(path, null);
                                    Log.d(TAG, "Attempting to remove user from family Id list at: " + path);
                                }

                                // atomic database delete
                                Task updateTask = FirebaseDatabase.getInstance().getReference()
                                        .updateChildren(childUpdates);
                                if (CurrentUser.isConnectedToDatabase()) {
                                    updateTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            callback.onUserDeleted();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            callback.onFailure("Failed to atomically " +
                                                    "delete user and family information");
                                        }
                                    });
                                }
                                else {
                                    callback.onUserDeleted();
                                }
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                            mFamilyCountFailure = true;
                            mCountFamiliesChecked++;
                            callback.onFailure(error);
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    private interface FamilyMemberNumberCallback {
        void onFamilyMembersCounted();

        void onFailure(String error);
    }

    private void checkNumFamilyMembersInDatabase(String familyId, final FamilyMemberNumberCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("families")
                .child(familyId).child("userIds");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Boolean> userIds = (Map) dataSnapshot.getValue();
                int numUsers = userIds.size();
                if (numUsers >= 1) {
                    DatabaseReference ref = dataSnapshot.getRef();
                    if (numUsers == 1) {
                        mFamiliesToDelete.add(ref.getParent());
                    } else {
                        mUserIdListsToDeleteUserFrom.add(ref);
                    }
                    callback.onFamilyMembersCounted();
                } else {
                    callback.onFailure("Number of users counted is less than one");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure("Unable to get user IDs");
            }
        });

    }
}
