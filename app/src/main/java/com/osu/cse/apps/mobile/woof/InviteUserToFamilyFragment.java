package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class InviteUserToFamilyFragment extends FamilyFragment implements View.OnClickListener {

    private static final String TAG = "InviteUserToFamilyFragment";
    private String mUserEmail;
    private User mUser;
    private ConstraintLayout mUserResultLayout;
    private Button mFindUserButton;
    private TextView mUserNameTextView;
    private TextView mUserNotFoundTextView;
    private Button mInviteButton;
    private TextView mUserExceptionTextView;
    private int mUserInvitationCount;
    private int mUserInvitationTotal;
    private boolean mUserInvitationSent;

    // int values to keep track of visibility of view elements should the fragment be destroyed
    private int mFindUserButtonVisibility;
    private int mUserResultLayoutVisibility;
    private int mInviteButtonVisibility;
    private int mUserExceptionTextViewVisibility;
    private int mUserNotFoundTextViewVisibility;

    private static final String KEY_FIND_USER = "find_user";
    private static final String KEY_USER_RESULT = "user_result";
    private static final String KEY_INVITE = "invite";
    private static final String KEY_USER_EXCEPTION = "user_exception";
    private static final String KEY_USER_NOT_FOUND = "user_not_found";

    private static final int FIND_USER_DEFAULT = View.VISIBLE;
    private static final int USER_RESULT_DEFAULT = View.INVISIBLE;
    private static final int INVITE_DEFAULT = View.INVISIBLE;
    private static final int USER_EXCEPTION_DEFAULT = View.INVISIBLE;
    private static final int USER_NOT_FOUND_DEFAULT = View.INVISIBLE;

    private boolean mFindUserButtonClicked;
    private static final String KEY_FIND_USER_CLICKED = "find_user_clicked";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() called");

        mFindUserButtonVisibility = FIND_USER_DEFAULT;
        mUserResultLayoutVisibility = USER_RESULT_DEFAULT;
        mInviteButtonVisibility = INVITE_DEFAULT;
        mUserExceptionTextViewVisibility = USER_EXCEPTION_DEFAULT;
        mUserNotFoundTextViewVisibility = USER_NOT_FOUND_DEFAULT;
        if (savedInstanceState != null) {
            mFindUserButtonVisibility = savedInstanceState.getInt(KEY_FIND_USER, FIND_USER_DEFAULT);
            mUserResultLayoutVisibility = savedInstanceState.getInt(KEY_USER_RESULT, USER_RESULT_DEFAULT);
            mInviteButtonVisibility = savedInstanceState.getInt(KEY_INVITE, INVITE_DEFAULT);
            mUserExceptionTextViewVisibility = savedInstanceState.getInt(KEY_USER_EXCEPTION, USER_EXCEPTION_DEFAULT);
            mUserNotFoundTextViewVisibility = savedInstanceState.getInt(KEY_USER_NOT_FOUND, USER_NOT_FOUND_DEFAULT);
            mFindUserButtonClicked = savedInstanceState.getBoolean(KEY_FIND_USER_CLICKED, false);
        }
    }

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

        mUserResultLayout = v.findViewById(R.id.user_result_layout);
        mUserResultLayout.setVisibility(mUserResultLayoutVisibility);

        mUserNameTextView = v.findViewById(R.id.user_name_text_view);

        mInviteButton = v.findViewById(R.id.invite_button);
        mInviteButton.setOnClickListener(this);
        mInviteButton.setVisibility(mInviteButtonVisibility);

        mUserExceptionTextView = v.findViewById(R.id.user_exception_text_view);
        mUserExceptionTextView.setVisibility(mUserExceptionTextViewVisibility);

        mUserNotFoundTextView = v.findViewById(R.id.user_not_found_text_view);
        mUserNotFoundTextView.setVisibility(mUserNotFoundTextViewVisibility);

        mFindUserButton = v.findViewById(R.id.find_user_button);
        mFindUserButton.setOnClickListener(this);
        mFindUserButton.setVisibility(mFindUserButtonVisibility);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume() called");
        if (mFindUserButtonClicked) {
            onClick(mFindUserButton);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState() called");
        savedInstanceState.putInt(KEY_FIND_USER, mFindUserButtonVisibility);
        savedInstanceState.putInt(KEY_USER_RESULT, mUserResultLayoutVisibility);
        savedInstanceState.putInt(KEY_INVITE, mInviteButtonVisibility);
        savedInstanceState.putInt(KEY_USER_EXCEPTION, mUserExceptionTextViewVisibility);
        savedInstanceState.putInt(KEY_USER_NOT_FOUND, mUserNotFoundTextViewVisibility);
        savedInstanceState.putBoolean(KEY_FIND_USER_CLICKED, mFindUserButtonClicked);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.find_user_button:
                Log.d(TAG, "onClick() called");
                mFindUserButtonClicked = true;
                CurrentUser.searchDatabaseForUserByEmail(mUserEmail, new UserCallback() {
                    @Override
                    public void onUserRetrieved(User user) {
                        mUserNotFoundTextViewVisibility = View.INVISIBLE;
                        mUserNotFoundTextView.setVisibility(mUserNotFoundTextViewVisibility);
                        Log.d(TAG, "onUserRetrieved() called");
                        if (user == null) {
                            Log.d(TAG, "User object from database is null");
                            mUserNotFoundTextViewVisibility = View.VISIBLE;
                            mUserNotFoundTextView.setVisibility(mUserNotFoundTextViewVisibility);
                        } else {
                            mUserResultLayoutVisibility = View.VISIBLE;
                            mUserResultLayout.setVisibility(mUserResultLayoutVisibility);
                            mFindUserButtonVisibility = View.INVISIBLE;
                            mFindUserButton.setVisibility(mFindUserButtonVisibility);
                            mUser = user;
                            if (checkUserInFamily()) {
                                mUserNameTextView.setText(mUser.getfName() + " " + mUser.getlName());
                                mUserExceptionTextViewVisibility = View.VISIBLE;
                                mUserExceptionTextView.setVisibility(mUserExceptionTextViewVisibility);
                                mUserExceptionTextView.setText("User already in family");
                            } else if (mUser.getinvitationIds() == null) {
                                setViewsOnUserInvitable();
                            } else {
                                checkInvitationAlreadySent(new InvitationAlreadySentCallback() {
                                    @Override
                                    public void onInvitationAlreadySent() {
                                        mUserNameTextView.setText(mUser.getfName() + " " + mUser.getlName());
                                        mUserExceptionTextViewVisibility = View.VISIBLE;
                                        mUserExceptionTextView.setVisibility(mUserExceptionTextViewVisibility);
                                        mUserExceptionTextView.setText("Invitation already sent");
                                    }

                                    @Override
                                    public void onInvitationNotSent() {
                                        setViewsOnUserInvitable();
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        Log.d(TAG, error);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.d(TAG, error);
                    }
                });
                break;

            case R.id.invite_button:
                Log.d(TAG, "onClick() called");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                String invitationId = ref.child("invitations").push().getKey();
                Invitation invitation = new Invitation(invitationId, getFamilyId(),
                        mUser.getuserId(), CurrentUser.getUserId());

                Map<String, Object> childUpdates = new HashMap();
                childUpdates.put("/users/" + mUser.getuserId() + "/invitationIds/" + invitationId, true);
                childUpdates.put("/families/" + getFamilyId() + "/invitationIds/" + invitationId, true);
                childUpdates.put("/invitations/" + invitationId, invitation.toMap());
                Task updateTask = ref.updateChildren(childUpdates);
                if (CurrentUser.isConnectedToDatabase()) {
                    updateTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Invitation sent", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Failed sending invitation");
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Invitation sent", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;

        }
    }

    private boolean checkUserInFamily() {
        boolean userInFamily = false;
        Family family = getFamily();
        String userId = mUser.getuserId();
        for (Map.Entry<String, Boolean> entry : family.getuserIds().entrySet()) {
            if (entry.getKey().equals(userId)) {
                userInFamily = true;
                break;
            }
        }
        return userInFamily;
    }

    private void checkInvitationAlreadySent(final InvitationAlreadySentCallback callback) {
        mUserInvitationCount = 0;
        mUserInvitationTotal = mUser.getinvitationIds().size();
        mUserInvitationSent = false;
        String userId = mUser.getuserId();
        CurrentUser.getUserInvitationsFromDatabase(userId, new InvitationCallback() {
            @Override
            public void onInvitationRetrieved(Invitation invitation) {
                mUserInvitationCount++;
                if (invitation.getfamilyId().equals(getFamilyId())) {
                    mUserInvitationSent = true;
                    callback.onInvitationAlreadySent();
                } else if (mUserInvitationCount >= mUserInvitationTotal && !mUserInvitationSent) {
                    callback.onInvitationNotSent();
                }
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure("Failed getting invitation from database");
            }
        });
    }

    private interface InvitationAlreadySentCallback {
        void onInvitationAlreadySent();

        void onInvitationNotSent();

        void onFailure(String error);
    }

    private void setViewsOnUserInvitable() {
        mUserNameTextView.setText(mUser.getfName() + " " + mUser.getlName());
        mInviteButtonVisibility = View.VISIBLE;
        mInviteButton.setVisibility(mInviteButtonVisibility);
    }

}
