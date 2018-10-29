package com.osu.cse.apps.mobile.woof;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import android.util.Log;

public class ActivityRecord {

    private final static int WALK = 1;
    private final static int FOOD = 2;
    private final static int WATER = 3;
    private final static int BATHROOM = 4;
    private final static int VETVISIT = 5;

    private final static int CUPS = 0;
    private final static int OUNCES = 1;

    private final static String TAG = "ActivityRecord";

    private UUID mActivity_ID;
    private int mActivity_Type = 0; // Walk/Exercise, Food, Water, Bathroom, Vet Visit
    private Date mStart_Time;
    private Date mEnd_Time;
    private int mBathroom_Type = 0; // 1 or 2
    private String mFood_Brand;
    private int mFood_Amount = -1;
    private int mFood_Metric = -1; // cups or ounces
    private int mCalories = 0;
    private String mVet_Location;
    private String mVet_Visit_Reason;
    private UUID mDogID;

    public ActivityRecord(UUID id, int activity_Type){
        this.mActivity_ID = id;
        this.mActivity_Type= activity_Type;
        if (this.mActivity_Type > 5 || this.mActivity_Type < 0){
            Log.d(TAG, "ERROR :: INVALID Activity Type");
        }

    }

    public int getActivity_Type() {
        return mActivity_Type;
    }

    public Date getStart_Time() {
        return mStart_Time;
    }

    public void setStart_Time(Date start_Time) {
        mStart_Time = start_Time;
    }

    public Date getEnd_Time() {
        return mEnd_Time;
    }

    public void setEnd_Time(Date end_Time) {
        mEnd_Time = end_Time;
    }

    public int getBathroom_Type() {
        return mBathroom_Type;
    }

    public void setBathroom_Type(int bathroom_Type) {
        mBathroom_Type = bathroom_Type;
    }

    public String getFood_Brand() {
        return mFood_Brand;
    }

    public void setFood_Brand(String food_Brand) {
        mFood_Brand = food_Brand;
    }

    public int getFood_Amount() {
        return mFood_Amount;
    }

    public void setFood_Amount(int food_Amount) {
        mFood_Amount = food_Amount;
    }

    public int getFood_Metric() {
        return mFood_Metric;
    }

    public void setFood_Metric(int food_Metric) {
        mFood_Metric = food_Metric;
    }

    public int getCalories() {
        return mCalories;
    }

    public void setCalories(int calories) {
        mCalories = calories;
    }

    public String getVet_Location() {
        return mVet_Location;
    }

    public void setVet_Location(String vet_Location) {
        mVet_Location = vet_Location;
    }

    public String getVet_Visit_Reason() {
        return mVet_Visit_Reason;
    }

    public void setVet_Visit_Reason(String vet_Visit_Reason) {
        mVet_Visit_Reason = vet_Visit_Reason;
    }

    public UUID getDogID() {
        return mDogID;
    }

    public void setDogID(UUID dogID) {
        mDogID = dogID;
    }

    public UUID getActivity_ID() {
        return mActivity_ID;
    }

    public void setActivity_ID(UUID activity_ID) {
        mActivity_ID = activity_ID;
    }
    // TODO - GPS Paths


    public static List<ActivityRecord> getTestActivityRecords(){

        UUID dog_id = UUID.randomUUID();
        List<ActivityRecord> activityRecordList = new ArrayList<>();

        // Walk
        ActivityRecord a1 = new ActivityRecord(UUID.randomUUID(), WALK);
        a1.setStart_Time(new Date());
        a1.setEnd_Time(new Date());
        a1.setEnd_Time(new Date());
        a1.setCalories(250);
        a1.setDogID(dog_id);

        // Food
        ActivityRecord a2 = new ActivityRecord(UUID.randomUUID(), FOOD);
        a2.setStart_Time(new Date());
        a2.setFood_Amount(5);
        a2.setFood_Brand("McNuggets");
        a2.setFood_Metric(OUNCES);
        a1.setCalories(1000);
        a2.setDogID(dog_id);

        // Water
        ActivityRecord a3 = new ActivityRecord(UUID.randomUUID(), WATER);
        a3.setStart_Time(new Date());
        a3.setDogID(dog_id);

        // Bathroom
        ActivityRecord a4 = new ActivityRecord(UUID.randomUUID(), BATHROOM);
        a4.setStart_Time(new Date());
        a4.setBathroom_Type(2);
        a4.setDogID(dog_id);

        // Vet visit
        ActivityRecord a5 = new ActivityRecord(UUID.randomUUID(), VETVISIT);
        a5.setStart_Time(new Date());
        a5.setVet_Location("250 W. High St. Columbus, OH 43210");
        a5.setVet_Visit_Reason("Child fed him McNuggets");
        a5.setDogID(dog_id);

        activityRecordList.add(a1);
        activityRecordList.add(a2);
        activityRecordList.add(a3);
        activityRecordList.add(a4);
        activityRecordList.add(a5);


        return activityRecordList;
    }


}
