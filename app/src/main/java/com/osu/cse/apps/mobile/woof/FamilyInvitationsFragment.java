package com.osu.cse.apps.mobile.woof;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
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

        mInvitationList = new ArrayList();

        // populate invitation list from database
        /*
        CurrentUser.getInvitationsFromDatabase(new InvitationCallback() {
            @Override
            public void onDogInfoRetrieved(DogInfo dogInfo) {
                mDogInfoList.add(dogInfo);
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, error);
            }
        });
        */

        // TODO: delete test code below
        // ********
        FamilyInfo familyInfo1 = new FamilyInfo("54321", "The Simpsons");
        Invitation invitation1 = new Invitation("12345", familyInfo1, "Marge Simpson");
        mInvitationList.add(invitation1);
        FamilyInfo familyInfo2 = new FamilyInfo("87", "The Kardashians");
        Invitation invitation2 = new Invitation("78", familyInfo2, "Kim Kardashian");
        mInvitationList.add(invitation2);
        // ********
        // TODO: delete test code above
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
        if (mAdapter == null) {
            mAdapter = new FamilyInvitationsFragment.InvitationAdapter(mInvitationList);
            mInvitationRecyclerView.setAdapter(mAdapter);
        }
        else {
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

            mFamilyNameTextView.setText(mInvitation.getfamilyInfo().getfamilyName());

            String inviterString = mInvitation.getinviterName() + " invited you";
            mInviterTextView.setText(inviterString);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.accept_button:
                    // TODO: implement accept button functionality
                    break;
                case R.id.decline_button:
                    // TODO: implement decline button functionality
                    break;
            }
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

    }

}
