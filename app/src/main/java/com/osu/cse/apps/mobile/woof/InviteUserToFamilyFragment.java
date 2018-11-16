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

public class InviteUserToFamilyFragment extends FamilyFragment {

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

        mFindUserButton = v.findViewById(R.id.find_user_button);
        mFindUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick() called");
                mUserNotFoundTextView.setVisibility(View.INVISIBLE);
                CurrentUser.searchDatabaseForUserByEmail(mUserEmail, new UserCallback() {
                    @Override
                    public void onUserRetrieved(User user) {
                        Log.d(TAG, "onUserRetrieved() called");
                        if (user == null) {
                            Log.d(TAG, "User object from database is null");
                            mUserNotFoundTextView.setVisibility(View.VISIBLE);
                        } else {
                            mUserResultLayout.setVisibility(View.VISIBLE);
                            mFindUserButton.setVisibility(View.INVISIBLE);
                            mUser = user;
                            if (checkUserInFamily()) {
                                mUserExceptionTextView.setVisibility(View.VISIBLE);
                                mUserExceptionTextView.setText("User already in family");
                            }
                            else if (mUser.getinvitationIds() == null) {
                                setViewsOnUserInvitable();
                            }
                            else {
                                checkInvitationAlreadySent(new InvitationAlreadySentCallback() {
                                    @Override
                                    public void onInvitationAlreadySent() {
                                        mUserExceptionTextView.setVisibility(View.VISIBLE);
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

            }
        });

        mUserResultLayout = v.findViewById(R.id.user_result_layout);
        mUserResultLayout.setVisibility(View.INVISIBLE);

        mUserNameTextView = v.findViewById(R.id.user_name_text_view);

        mInviteButton = v.findViewById(R.id.invite_button);
        mInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                }
                else {
                    Toast.makeText(getActivity(), "Invitation sent", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        });
        mInviteButton.setVisibility(View.INVISIBLE);

        mUserExceptionTextView = v.findViewById(R.id.user_exception_text_view);
        mUserExceptionTextView.setVisibility(View.INVISIBLE);

        mUserNotFoundTextView = v.findViewById(R.id.user_not_found_text_view);
        mUserNotFoundTextView.setVisibility(View.INVISIBLE);

        return v;
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
                }
                else if (mUserInvitationCount >= mUserInvitationTotal && !mUserInvitationSent) {
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
        mInviteButton.setVisibility(View.VISIBLE);
    }

}
