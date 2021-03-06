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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FamilySelectionFragment extends Fragment {

    private static final String TAG = "FamilySelectionFragment";

    private TextView mNoFamiliesTextView;
    private RecyclerView mFamilyRecyclerView;
    private List<FamilyInfo> mFamilyInfoList;
    private FamilyAdapter mAdapter;

    public static FamilySelectionFragment newInstance() {
        return new FamilySelectionFragment();
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

        View v = inflater.inflate(R.layout.fragment_family_selection, container, false);

        mNoFamiliesTextView = v.findViewById(R.id.no_families_text_view);

        mFamilyRecyclerView = v.findViewById(R.id.family_recycler_view);
        mFamilyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // updateUI();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume() called");
        updateUI();
        mAdapter.setIsClickable(true);
    }

    private void updateUI() {
        Log.i(TAG, "updateUI() called");

        mNoFamiliesTextView.setVisibility(View.VISIBLE);
        mFamilyInfoList = new ArrayList();

        // populate family list from database
        CurrentUser.getFamilyInfoFromDatabase(new FamilyInfoCallback() {
            @Override
            public void onFamilyInfoRetrieved(FamilyInfo familyInfo) {
                Log.d(TAG, "onFamilyInfoRetrieved() called");
                mNoFamiliesTextView.setVisibility(View.INVISIBLE);
                mFamilyInfoList.add(familyInfo);
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
            mAdapter = new FamilyAdapter(mFamilyInfoList);
            mFamilyRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setFamilyInfoList(mFamilyInfoList);
            mAdapter.notifyDataSetChanged();
        }

    }

    private class FamilyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private FamilyInfo mFamilyInfo;

        private TextView mFamilyNameTextView;
        private TextView mCoordinatorTextView;

        public FamilyHolder(LayoutInflater inflater, ViewGroup parent)                                                                                                                                                                 {
            super(inflater.inflate(R.layout.list_item_family, parent, false));
            itemView.setOnClickListener(this);
            mFamilyNameTextView = itemView.findViewById(R.id.family_name_text_view);
            mCoordinatorTextView = itemView.findViewById(R.id.coordinator_text_view);
        }

        public void bind(FamilyInfo familyInfo) {
            Log.d(TAG, "FamilyHolder.bind() called");
            mFamilyInfo = familyInfo;
            String familyName = familyInfo.getfamilyName();
            mFamilyNameTextView.setText(familyName);

            // make coordinator text view invisible if current user is not the coordinator for this family
            if (!mFamilyInfo.getcoordinatorUserId().equals(CurrentUser.getUserId())) {
                mCoordinatorTextView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            if(mAdapter.isClickable){
                mAdapter.setIsClickable(false);
                Intent intent = FamilyManagementActivity.newIntent(getActivity(), mFamilyInfo.getfamilyId(), FamilyManagementActivity.FAMILY_HOME);
                startActivity(intent);
            }
        }
    }

    private class FamilyAdapter extends RecyclerView.Adapter<FamilyHolder> {

        private List<FamilyInfo> mFamilyInfoList;
        private Boolean isClickable = true;

        public FamilyAdapter(List<FamilyInfo> familyInfoList) {
            mFamilyInfoList = familyInfoList;
        }

        @Override
        public FamilyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FamilyHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(FamilyHolder holder, int position) {
            FamilyInfo familyInfo = mFamilyInfoList.get(position);
            holder.bind(familyInfo);
        }

        @Override
        public int getItemCount() { return mFamilyInfoList.size(); }

        public void setFamilyInfoList(List<FamilyInfo> familyInfoList) {
            mFamilyInfoList = familyInfoList;
        }

        public void setIsClickable(Boolean click){
            isClickable = click;
        }

        public Boolean getClickable(){
            return isClickable;
        }

    }

}
