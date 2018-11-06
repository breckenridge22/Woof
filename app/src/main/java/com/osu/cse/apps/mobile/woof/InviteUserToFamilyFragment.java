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
                        }
                        else {
                            mUser = user;
                            mFindUserButton.setVisibility(View.INVISIBLE);
                            mUserNameTextView.setText(mUser.getfName() + " " + mUser.getlName());
                            mUserResultLayout.setVisibility(View.VISIBLE);
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

        Button inviteButton = v.findViewById(R.id.invite_button);
        inviteButton.setOnClickListener(new View.OnClickListener() {
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
                ref.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        });

        mUserNotFoundTextView = v.findViewById(R.id.user_not_found_text_view);
        mUserNotFoundTextView.setVisibility(View.INVISIBLE);

        return v;
    }

}
