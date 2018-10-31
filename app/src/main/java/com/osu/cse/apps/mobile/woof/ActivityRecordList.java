package com.osu.cse.apps.mobile.woof;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActivityRecordList {

    private static ActivityRecordList sActivityRecordList;

    private List<ActivityRecord> mActivityRecordList;

    public static ActivityRecordList get(Context context){
        if(sActivityRecordList == null){
            sActivityRecordList = new ActivityRecordList(context);
        }
        return sActivityRecordList;
    }

    private ActivityRecordList(Context context) {

        // Resets the list
        mActivityRecordList = new ArrayList<>();

        // Add test units (temporary)
        getTestActivityRecordList();
    }

    private void setActivityRecordList(UUID dog_id) {
        // code to fetch records associated with a dog from a database
    }

    public ActivityRecord getActivityRecord(UUID recordID) {

        for(ActivityRecord record : mActivityRecordList){
            /*
            if(record.getdogID().equals(recordID)){
                return record;
            }
            */
        }
        return null;
    }

    public void addActivityToList(ActivityRecord activity) {
        mActivityRecordList.add(activity);
    }


    private void getTestActivityRecordList() {
        mActivityRecordList = new ArrayList<>();
        List<ActivityRecord> testActivityRecords = ActivityRecord.getTestActivityRecords();
        for (ActivityRecord  testRecord : testActivityRecords) {
            mActivityRecordList.add(testRecord);
        }
    }

}
