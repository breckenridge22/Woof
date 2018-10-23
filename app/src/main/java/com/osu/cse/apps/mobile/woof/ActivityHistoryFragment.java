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

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ActivityHistoryFragment extends Fragment {

    private final String TAG = "ActivityHistoryFragment";

    private RecyclerView mActivityRecyclerView;
    private ActivityAdapter mAdapter;

    public static ActivityHistoryFragment newInstance() {
        return new ActivityHistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.d(TAG, "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_activity_list, container, false);
        mActivityRecyclerView = (RecyclerView) view.findViewById(R.id.activity_recycler_view);
        mActivityRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        UUID dog_id = UUID.randomUUID();
        // Need to get it passed from DogSelection
        updateUI(dog_id);

        return view;
    }

    private void updateUI(UUID dog_id) {
        Log.d(TAG, "updateUI() called");
        // Need Dog UUID

        // Using test activities for now

        List<ActivityRecord> activityRecordsList = ActivityRecord.getTestActivityRecords();
        mAdapter = new ActivityAdapter(activityRecordsList);

        mActivityRecyclerView.setAdapter(mAdapter);
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

        private String getActivityDetails(ActivityRecord record){
            String details = "";
            switch(record.getActivity_Type()){
                case 1:
                    if(record.getCalories() >= 0){
                        details = details.concat("Calories Burned: " + record.getCalories() + "\n");
                    }
                    break;
                case 2:
                    String brand = "";
                    String amt = "";
                    String metric = "";

                    if(!record.getFood_Brand().isEmpty()){
                        brand = record.getFood_Brand();
                    }
                    if(record.getFood_Amount() > 0){
                        amt = String.valueOf(record.getFood_Amount());
                    }
                    if(record.getFood_Metric() == 0){
                        metric = "Cups";
                    }
                    else if(record.getFood_Metric() == 1) {
                        metric = "Oz";
                    }
                    details = details + brand + ": " + amt + " " + metric + "\n";
                    if (record.getCalories() > 0){
                        details = details.concat("Calories: " + record.getCalories());
                    }
                    break;
                case 3:
                    // No additional details to present
                    break;
                case 4:
                    if(record.getBathroom_Type() == 1){
                        details = details.concat("Pee");
                    }
                    else if (record.getBathroom_Type() == 2){
                        details = details.concat("Poo");
                    }
                    break;
                case 5:
                    if(!record.getVet_Location().isEmpty()){
                        details = details.concat(record.getVet_Location() + "\n");
                    }
                    if(!record.getVet_Visit_Reason().isEmpty()){
                        details = details.concat("Reason: ");
                        details = details.concat(record.getVet_Visit_Reason());
                    }
                    break;
            }
            return details.trim();
        }

        private String getActivityDateTime(Date date){
            return date.toString();
        }

        private String getActivityTypeString(int type){
            switch(type){
                case 1:
                    return "WALK/EXERCISE";
                case 2:
                    return "FOOD";
                case 3:
                    return "WATER";
                case 4:
                    return "BATHROOM";
                case 5:
                    return "VET VISIT";
                default:
                    return "Activity not found";
            }
        }

        public void bind(ActivityRecord activityRecord) {
            mActivityRecord = activityRecord;

            // Retrieve activity type (int) and convert to corresponding string message
            String str_type = getActivityTypeString(mActivityRecord.getActivity_Type());
            mActivityType.setText(str_type);

            // Retrieve start/end times and convert to string
            // If end time exists, append to start time
            String str_time = getActivityDateTime(mActivityRecord.getStart_Time());
            if(mActivityRecord.getEnd_Time() != null){
                str_time.concat(" - ");
                str_time.concat(mActivityRecord.getEnd_Time().toString());
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
        public int getItemCount() { return mActivityRecords.size(); }

    }
}