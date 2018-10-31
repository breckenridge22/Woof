package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ActivityHistoryFragment extends DogFragment {

    private final String TAG = "ActivityHistoryFragment";

    private RecyclerView mActivityRecyclerView;
    private ActivityAdapter mAdapter;
    private List<ActivityRecord> mActivityRecordsList;
    private static final int MAX_ACTIVITIES = 15;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityRecordsList = new ArrayList();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("activities")
                .child(getFamilyId() + ":" + getDogId());
        Query activitiesQuery = ref.limitToLast(MAX_ACTIVITIES); // limit number of activities to display
        activitiesQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded() called");
                ActivityRecord record = dataSnapshot.getValue(ActivityRecord.class);
                mActivityRecordsList.add(0, record);
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged() called");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved() called");
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved() called");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled() called");
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_activity_list, container, false);
        mActivityRecyclerView = (RecyclerView) view.findViewById(R.id.activity_recycler_view);
        mActivityRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateActivityHistoryUI(); // TODO: Does this need to be called from onCreateView?
        return view;
    }

    // TODO: Get this working properly
    public void updateActivityHistoryUI() {
        Log.d(TAG, "updateActivityHistoryUI() called");

        if (mAdapter == null) {
            mAdapter = new ActivityAdapter(mActivityRecordsList);
            mActivityRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.notifyDataSetChanged();
        }
    }


    private class ActivityHolder extends RecyclerView.ViewHolder  {

        private ActivityRecord mActivityRecord;
        private TextView mActivityDetails;
        private TextView mActivityType;
        private TextView mActivityTime;

        public ActivityHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_activity, parent, false));

            mActivityType = itemView.findViewById(R.id.activity_type);
            mActivityTime= itemView.findViewById(R.id.activity_time);
            mActivityDetails = itemView.findViewById(R.id.activity_details);
        }

        private String getActivityDetails(ActivityRecord record) {
            String details = "";
            switch(record.getactivity_Type()) {
                case ActivityRecord.WALK:
                    if(record.getcalories() >= 0) {
                        details = details.concat("Calories Burned: " + record.getcalories() + "\n");
                    }
                    break;
                case ActivityRecord.FOOD:
                    String brand = "";
                    String amt = "";
                    String metric = "";

                    if(!record.getfood_Brand().isEmpty()) {
                        brand = record.getfood_Brand();
                    }
                    if(record.getfood_Amount() > 0) {
                        amt = String.valueOf(record.getfood_Amount());
                    }
                    if(record.getfood_Metric() == ActivityRecord.CUPS) {
                        metric = "Cups";
                    }
                    else if(record.getfood_Metric() == ActivityRecord.OUNCES) {
                        metric = "Oz";
                    }
                    details = details + brand + ": " + amt + " " + metric + "\n";
                    if (record.getcalories() > 0) {
                        details = details.concat("Calories: " + record.getcalories());
                    }
                    break;
                case ActivityRecord.WATER:
                    // No additional details to present
                    break;
                case ActivityRecord.BATHROOM:
                    if(record.getbathroom_Type() == ActivityRecord.PEE) {
                        details = details.concat("Pee");
                    }
                    else if (record.getbathroom_Type() == ActivityRecord.POO) {
                        details = details.concat("Poo");
                    }
                    break;
                case ActivityRecord.VETVISIT:
                    if(!record.getvet_Location().isEmpty()) {
                        details = details.concat(record.getvet_Location() + "\n");
                    }
                    if(!record.getvet_Visit_Reason().isEmpty()) {
                        details = details.concat("Reason: ");
                        details = details.concat(record.getvet_Visit_Reason());
                    }
                    break;
            }
            return details.trim();
        }

        private String getActivityDateTime(Date date){
            return date.toString();
        }

        private String getActivityTypeString(int type) {
            switch(type) {
                case ActivityRecord.WALK:
                    return "WALK/EXERCISE";
                case ActivityRecord.FOOD:
                    return "FOOD";
                case ActivityRecord.WATER:
                    return "WATER";
                case ActivityRecord.BATHROOM:
                    return "BATHROOM";
                case ActivityRecord.VETVISIT:
                    return "VET VISIT";
                default:
                    return "Activity not found";
            }
        }

        public void bind(ActivityRecord activityRecord) {
            mActivityRecord = activityRecord;

            // Retrieve activity type (int) and convert to corresponding string message
            String str_type = getActivityTypeString(mActivityRecord.getactivity_Type());
            mActivityType.setText(str_type);

            // Retrieve start/end times and convert to string
            // If end time exists, append to start time
            String str_time = getActivityDateTime(mActivityRecord.getstart_Time());
            if(mActivityRecord.getend_Time() != null) {
                str_time.concat(" - ");
                str_time.concat(mActivityRecord.getend_Time().toString());
            }
            mActivityTime.setText(str_time);

            // Retrieve necessary details depending on activity type and format to string
            String details = getActivityDetails(mActivityRecord);
            mActivityDetails.setText(details);
        }
    }


    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {

        private List<ActivityRecord> mActivityRecords;

        public ActivityAdapter (List<ActivityRecord> activityRecords) {
            mActivityRecords = activityRecords;
        }

        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ActivityHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ActivityHistoryFragment.ActivityHolder holder, int position) {
            ActivityRecord activityRecord = mActivityRecords.get(position);
            holder.bind(activityRecord);
        }

        @Override
        public int getItemCount() {
            if (mActivityRecords == null) {
                return 0;
            }
            else return mActivityRecords.size(); }

    }
}