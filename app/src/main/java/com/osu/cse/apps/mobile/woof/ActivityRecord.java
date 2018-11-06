package com.osu.cse.apps.mobile.woof;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import android.util.Log;

public class ActivityRecord {

    public final static int WALK = 1;
    public final static int FOOD = 2;
    public final static int WATER = 3;
    public final static int BATHROOM = 4;
    public final static int VETVISIT = 5;

    public final static int CUPS = 0;
    public final static int OUNCES = 1;

    public final static int PEE = 1;
    public final static int POO = 2;
    public final static int BOTH = 3;

    private final static String TAG = "ActivityRecord";

    private String activity_ID;
    private int activity_Type = 0; // Walk/Exercise, Food, Water, Bathroom, Vet Visit
    private Date start_Time;
    private Date end_Time;
    private int bathroom_Type = 0; // 1 or 2 or 3
    private String food_Brand;
    private int food_Amount = -1;
    private int food_Metric = -1; // cups or ounces
    private int calories = 0;
    private String vet_Location;
    private String vet_Visit_Reason;
    private int walk_dist;

    public ActivityRecord() {
        // default constructor for Firebase
    }

    public ActivityRecord(int activity_Type){
        this.activity_Type= activity_Type;
        if (this.activity_Type > 5 || this.activity_Type < 0){
            Log.d(TAG, "ERROR :: INVALID Activity Type");
        }

    }

    public int getactivity_Type() {
        return activity_Type;
    }

    public Date getstart_Time() {
        return start_Time;
    }

    public void setstart_Time(Date start_Time) {
        this.start_Time = start_Time;
    }

    public Date getend_Time() {
        return end_Time;
    }

    public void setend_Time(Date end_Time) {
        this.end_Time = end_Time;
    }

    public int getbathroom_Type() {
        return bathroom_Type;
    }

    public void setbathroom_Type(int bathroom_Type) {
        this.bathroom_Type = bathroom_Type;
    }

    public String getfood_Brand() {
        return food_Brand;
    }

    public void setfood_Brand(String food_Brand) {
        this.food_Brand = food_Brand;
    }

    public int getfood_Amount() {
        return food_Amount;
    }

    public void setfood_Amount(int food_Amount) {
        this.food_Amount = food_Amount;
    }

    public int getfood_Metric() {
        return food_Metric;
    }

    public void setfood_Metric(int food_Metric) {
        this.food_Metric = food_Metric;
    }

    public int getcalories() {
        return calories;
    }

    public void setcalories(int calories) {
        this.calories = calories;
    }

    public String getvet_Location() {
        return vet_Location;
    }

    public void setvet_Location(String vet_Location) {
        this.vet_Location = vet_Location;
    }

    public String getvet_Visit_Reason() {
        return vet_Visit_Reason;
    }

    public void setvet_Visit_Reason(String vet_Visit_Reason) {
        this.vet_Visit_Reason = vet_Visit_Reason;
    }

    public void set_walk_dist(int dist){ this.walk_dist = dist; }
    public int get_walk_dist(){ return this.walk_dist; }

    public String getactivity_ID() {
        return activity_ID;
    }

    public void setactivity_ID(String activity_ID) {
        this.activity_ID = activity_ID;
    }
    // TODO - GPS Paths


    public static List<ActivityRecord> getTestActivityRecords(){

        List<ActivityRecord> activityRecordList = new ArrayList<>();

        // Walk
        ActivityRecord a1 = new ActivityRecord(WALK);
        a1.setstart_Time(new Date());
        a1.setend_Time(new Date());
        a1.setend_Time(new Date());
        a1.setcalories(250);
        a1.set_walk_dist(2000);

        // Food
        ActivityRecord a2 = new ActivityRecord(FOOD);
        a2.setstart_Time(new Date());
        a2.setfood_Amount(5);
        a2.setfood_Brand("McNuggets");
        a2.setfood_Metric(OUNCES);
        a1.setcalories(1000);

        // Water
        ActivityRecord a3 = new ActivityRecord(WATER);
        a3.setstart_Time(new Date());

        // Bathroom
        ActivityRecord a4 = new ActivityRecord(BATHROOM);
        a4.setstart_Time(new Date());
        a4.setbathroom_Type(2);

        // Vet visit
        ActivityRecord a5 = new ActivityRecord(VETVISIT);
        a5.setstart_Time(new Date());
        a5.setvet_Location("250 W. High St. Columbus, OH 43210");
        a5.setvet_Visit_Reason("Child fed him McNuggets");

        activityRecordList.add(a1);
        activityRecordList.add(a2);
        activityRecordList.add(a3);
        activityRecordList.add(a4);
        activityRecordList.add(a5);

        return activityRecordList;
    }


}
