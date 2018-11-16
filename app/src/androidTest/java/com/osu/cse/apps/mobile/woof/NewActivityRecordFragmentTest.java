package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NewActivityRecordFragmentTest {
    private NewActivityRecordActivity mNewActivityRecordActivity;
    private NewActivityRecordFragment mNewActivityRecordFragment;
    private ActivityRecord mActivityRecord;

    DogInfo dog1 = new DogInfo("1234", "Doggo", "family123", "activity123");
    DogInfo dog2 = new DogInfo("2345", "Daggo", "family234", "activity234");
    ArrayList<DogInfo> mDogInfos;

//    public NewActivityRecordFragmentTest(){
//        super(NewActivityRecordActivity.class);
//    }
}
