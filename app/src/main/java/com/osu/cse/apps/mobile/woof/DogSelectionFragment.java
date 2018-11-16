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
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DogSelectionFragment extends Fragment {

    private final String TAG = "DogSelectionFragment";

    private RecyclerView mDogRecyclerView;
    private List<DogInfo> mDogInfoList;
    private DogAdapter mAdapter;

    private String activity_type;

    public static DogSelectionFragment newInstance() {
        return new DogSelectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() called");

        // Retrieve values stored in intent
        Bundle bundle = this.getArguments();
        if(bundle != null){
            activity_type = bundle.getString("activity_type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_dog_selection, container, false);

        mDogRecyclerView = v.findViewById(R.id.dog_recycler_view);
        mDogRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //updateUI();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume() called");
        updateUI();
        mAdapter.setClickable(true);
    }

    private void updateUI() {
        Log.i(TAG, "on updateUI() called");

        mDogInfoList = new ArrayList();

        // populate dog info list from database
        CurrentUser.getDogInfoFromDatabase(new DogInfoCallback() {
            @Override
            public void onDogInfoRetrieved(DogInfo dogInfo) {
                Log.d(TAG, "onDogInfoRetrieved() called");
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

        if (mAdapter == null) {
            mAdapter = new DogAdapter(mDogInfoList);
            mDogRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setDogInfoList(mDogInfoList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class DogHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private DogInfo mDogInfo;

        private ImageView mDogPictureImageView;
        private TextView mDogNameTextView;

        public DogHolder(LayoutInflater inflater, ViewGroup parent)                                                                                                                                                                 {
            super(inflater.inflate(R.layout.list_item_dog, parent, false));
            itemView.setOnClickListener(this);

            mDogPictureImageView = itemView.findViewById(R.id.dog_picture);
            mDogNameTextView = itemView.findViewById(R.id.dog_name);
        }

        public void bind(DogInfo dogInfo) {
            mDogInfo = dogInfo;
            // TODO - set dog picture on bind
            mDogNameTextView.setText(dogInfo.getdogName());
        }

        @Override
        public void onClick(View v) {
            if(mAdapter.isClickable()) {
                mAdapter.setClickable(false);

                if (activity_type.equals("manage_dogs")) {
                    Log.d(TAG, "Starting DogHomeFragment via DogManagementActivity.");
                    Intent intent = DogManagementActivity.newIntent(getActivity(), mDogInfo.getdogId(),
                            mDogInfo.getfamilyId(), DogManagementActivity.DOG_HOME);
                    startActivity(intent);

                } else if (activity_type.equals("activity_history")) {
                    Log.d(TAG, "Starting ActivityHistoryFragment via DogManagementActivity.");
                    Intent intent = DogManagementActivity.newIntent(getActivity(), mDogInfo.getdogId(),
                            mDogInfo.getfamilyId(), DogManagementActivity.ACTIVITY_HISTORY);
                    startActivity(intent);

                }
            }
        }
    }

    private class DogAdapter extends RecyclerView.Adapter<DogHolder> {

        private List<DogInfo> mDogInfoList;
        private Boolean isClickable = true;

        public DogAdapter(List<DogInfo> dogInfoList) {
            mDogInfoList = dogInfoList;
        }

        @Override
        public DogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new DogHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(DogHolder holder, int position) {
            DogInfo dogInfo = mDogInfoList.get(position);
            holder.bind(dogInfo);
        }

        @Override
        public int getItemCount() { return mDogInfoList.size(); }

        public void setDogInfoList(List<DogInfo> dogInfoList) {
            mDogInfoList = dogInfoList;
        }

        public boolean isClickable(){
            return isClickable;
        }

        public void setClickable(Boolean click){
            isClickable = click;
        }

    }

}
