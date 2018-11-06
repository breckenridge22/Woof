package com.osu.cse.apps.mobile.woof;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class FamilyMembersFragment extends FamilyFragment {

    private static final String TAG = "FamilyMembersFragment";

    private RecyclerView mFamilyMemberRecyclerView;
    private List<User> mFamilyMemberList;
    private FamilyMembersFragment.FamilyMemberAdapter mAdapter;

    public static FamilyMembersFragment newInstance() {
        return new FamilyMembersFragment();
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

        View v = inflater.inflate(R.layout.fragment_family_members, container, false);

        mFamilyMemberRecyclerView = v.findViewById(R.id.family_member_recycler_view);
        mFamilyMemberRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //updateFragmentUI();

        return v;
    }

    @Override
    public void updateUI() {
        super.onResume();
        Log.i(TAG, "updateUI() called");

        mFamilyMemberList = new ArrayList();

        // populate family list from database
        CurrentUser.getFamilyMembersFromDatabase(getFamily().getfamilyId(), new FamilyMemberCallback() {
            @Override
            public void onFamilyMemberRetrieved(User familyMember) {
                Log.d(TAG, "onFamilyMemberRetrieved() called");
                mFamilyMemberList.add(familyMember);
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, error);
            }
        });

        updateFragmentUI();
    }

    private void updateFragmentUI() {
        Log.i(TAG, "updateFragmentUI() called");

        if (mAdapter == null) {
            mAdapter = new FamilyMembersFragment.FamilyMemberAdapter(mFamilyMemberList);
            mFamilyMemberRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setFamilyMemberList(mFamilyMemberList);
            mAdapter.notifyDataSetChanged();
        }

    }

    private class FamilyMemberHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private User mFamilyMember;

        private TextView mFamilyMemberNameTextView;
        private TextView mCoordinatorTextView;
        private Button mRemoveFamilyMemberButton;

        public FamilyMemberHolder(LayoutInflater inflater, ViewGroup parent)                                                                                                                                                                 {
            super(inflater.inflate(R.layout.list_item_family_member, parent, false));

            mFamilyMemberNameTextView = itemView.findViewById(R.id.family_member_name_text_view);

            // TODO: dynamically set visibility for coordinator text view
            mCoordinatorTextView = itemView.findViewById(R.id.coordinator_text_view);

            // TODO: dynamically set visibility for remove button (coordinator only)
            mRemoveFamilyMemberButton = itemView.findViewById(R.id.remove_button);
            mRemoveFamilyMemberButton.setOnClickListener(this);
        }

        public void bind(User familyMember) {
            Log.d(TAG, "FamilyMemberHolder.bind() called");
            mFamilyMember = familyMember;
            String familyMemberName = mFamilyMember.getfName() + " " + mFamilyMember.getlName();
            mFamilyMemberNameTextView.setText(familyMemberName);

            // hide coordinator indicator if family is not coordinator
            if (!getFamily().getcoordinatorUserId().equals(familyMember.getuserId())) {
                mCoordinatorTextView.setVisibility(View.INVISIBLE);
            }

            // hide remove button if current user is not coordinator
            if(!getFamily().getcoordinatorUserId().equals(CurrentUser.getUserId())) {
                mRemoveFamilyMemberButton.setVisibility(View.INVISIBLE);
            }

            // hide remove button if family member and current user are same
            if (familyMember.getuserId().equals(CurrentUser.getUserId())) {
                mRemoveFamilyMemberButton.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.remove_button:
                    // TODO: may not remove user from family if user does not belong to any other families
                    // TODO: if user has pending invitations to other users for this family, delete those invitations
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    String familyId = getFamilyId();
                    String userId = mFamilyMember.getuserId();
                    Map<String, Object> childUpdates = new HashMap();
                    childUpdates.put("/families/" + familyId + "/userIds/" + userId, null);
                    childUpdates.put("/users/" + userId + "/familyIds/" + familyId, null);
                    ref.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Successfully removed user from family",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error removing user from family");
                        }
                    });
                    break;
            }
        }
    }

    private class FamilyMemberAdapter extends RecyclerView.Adapter<FamilyMembersFragment.FamilyMemberHolder> {

        private List<User> mFamilyMemberList;

        public FamilyMemberAdapter(List<User> familyMemberList) {
            mFamilyMemberList = familyMemberList;
        }

        @Override
        public FamilyMembersFragment.FamilyMemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FamilyMembersFragment.FamilyMemberHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(FamilyMembersFragment.FamilyMemberHolder holder, int position) {
            User familyMember = mFamilyMemberList.get(position);
            holder.bind(familyMember);
        }

        @Override
        public int getItemCount() { return mFamilyMemberList.size(); }

        public void setFamilyMemberList(List<User> familyMemberList) {
            mFamilyMemberList = familyMemberList;
        }

    }

}
