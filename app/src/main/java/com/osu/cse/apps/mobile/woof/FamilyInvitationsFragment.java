package com.osu.cse.apps.mobile.woof;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FamilyInvitationsFragment extends Fragment {

    private static final String TAG = "FamilyInvitationsFragment";

    private Map<String, Invitation> mInvitationMap;
    private List<Invitation> mInvitationList;
    private RecyclerView mInvitationRecyclerView;
    private InvitationAdapter mAdapter;

    public static FamilyInvitationsFragment newInstance() {
        return new FamilyInvitationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_family_invitations, container, false);

        mInvitationRecyclerView = v.findViewById(R.id.invitation_recycler_view);
        mInvitationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    private void updateUI() {
        Log.i(TAG, "on updateUI() called");

        mInvitationList = new ArrayList();

        // populate invitation list from database
        CurrentUser.getUserInvitationsFromDatabase(CurrentUser.getUserId(), new InvitationCallback() {
            @Override
            public void onInvitationRetrieved(Invitation invitation) {
                mInvitationList.add(invitation);
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, error);
            }
        });

        if (mAdapter == null) {
            mAdapter = new FamilyInvitationsFragment.InvitationAdapter(mInvitationList);
            mInvitationRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setInvitationList(mInvitationList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class InvitationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Invitation mInvitation;

        private TextView mFamilyNameTextView;
        private TextView mInviterTextView;
        private Button mAcceptButton;
        private Button mDeclineButton;

        public InvitationHolder(LayoutInflater inflater, ViewGroup parent)                                                                                                                                                                 {
            super(inflater.inflate(R.layout.list_item_family_invitation, parent, false));

            mFamilyNameTextView = itemView.findViewById(R.id.family_name_text_view);

            mInviterTextView = itemView.findViewById(R.id.inviter_text_view);

            mAcceptButton = itemView.findViewById(R.id.accept_button);
            mAcceptButton.setOnClickListener(this);

            mDeclineButton = itemView.findViewById(R.id.decline_button);
            mDeclineButton.setOnClickListener(this);
        }

        public void bind(Invitation invitation) {
            mInvitation = invitation;

            CurrentUser.getFamilyInfoFromDatabaseById(mInvitation.getfamilyId(), new FamilyInfoCallback() {
                @Override
                public void onFamilyInfoRetrieved(FamilyInfo familyInfo) {
                    mFamilyNameTextView.setText(familyInfo.getfamilyName());
                }

                @Override
                public void onFailure(String error) {
                    Log.d(TAG, error);
                }
            });

            CurrentUser.getUserFromDatabaseById(mInvitation.getinviterId(), new UserCallback() {
                @Override
                public void onUserRetrieved(User user) {
                    String userName = user.getfName() + " " + user.getlName();
                    String inviterString = userName + " invited you";
                    mInviterTextView.setText(inviterString);
                }

                @Override
                public void onFailure(String error) {
                    Log.d(TAG, error);
                }
            });

        }

        @Override
        public void onClick(View v) {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            String invitationId = mInvitation.getinvitationId();
            String familyId = mInvitation.getfamilyId();
            String userId = CurrentUser.getUserId();
            Map<String, Object> childUpdates = new HashMap();
            addChildUpdatesRemoveInvitation(childUpdates, userId, familyId, invitationId);

            switch(v.getId()) {

                case R.id.accept_button:
                    // atomically perform the following database updates:
                    // 1. delete invitation object
                    // 2. remove invitation id from user invitation id list
                    // 3. remove invitation id from family invitation id list
                    // 4. add family id to user family id list
                    // 5. add user id to family user id list
                    childUpdates.put("/users/" + userId + "/familyIds/" + familyId, true);
                    childUpdates.put("/families/" + familyId + "/userIds/" + userId, true);
                    ref.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Joined family", Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Database error.  Failed to join family.");
                        }
                    });
                    break;

                case R.id.decline_button:
                    // atomically perform the following database updates:
                    // 1. delete invitation object
                    // 2. remove invitation id from user invitation id list
                    // 3. remove invitation id from family invitation id list
                    ref.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Declined invitation", Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Database error.  Failed to join family.");
                        }
                    });
                    break;

            }
        }

        private void addChildUpdatesRemoveInvitation(Map<String, Object> childUpdates,
                                                     String userId, String familyId, String invitationId) {
            childUpdates.put("/users/" + userId + "/invitationIds/" +
                    invitationId, null);
            childUpdates.put("/families/" + familyId + "/invitationIds/" +
                    invitationId, null);
            childUpdates.put("/invitations/" + invitationId, null);
        }
    }

    private class InvitationAdapter extends RecyclerView.Adapter<FamilyInvitationsFragment.InvitationHolder> {

        private List<Invitation> mInvitationList;

        public InvitationAdapter(List<Invitation> invitationList) {
            mInvitationList = invitationList;
        }

        @Override
        public FamilyInvitationsFragment.InvitationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FamilyInvitationsFragment.InvitationHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(FamilyInvitationsFragment.InvitationHolder holder, int position) {
            Invitation invitation = mInvitationList.get(position);
            holder.bind(invitation);
        }

        @Override
        public int getItemCount() { return mInvitationList.size(); }

        public void setInvitationList(List<Invitation> invitationList) {
            mInvitationList = invitationList;
        }

    }

}
