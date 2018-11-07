package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NewActivityRecordActivity extends SingleFragmentActivity{

    private static final String TAG = "NewActivityRecordActivity";
    private static final String EXTRA_DOG_ID = "DOG_ID_LIST";
    private static final String EXTRA_DOG_ACTS = "DOG_ACTS_LIST";
    private static List<String> mDogIdList;
    private static List<String> mDogActsList;

    public static Intent newIntent(Context packageContext, List<String> DogIdList,
                                   List<DogInfo> dogInfoList) {
        Log.d(TAG, "newIntent() called.");

        Intent intent = new Intent(packageContext, NewActivityRecordActivity.class);
        mDogIdList = new ArrayList<String>();
        for(String s:DogIdList){
            mDogIdList.add(s);
        }

        mDogActsList = new ArrayList<>();
        for (DogInfo d: dogInfoList){
            mDogActsList.add(d.getactivitiesId());
        }


        // Pass string into intent
        intent.putStringArrayListExtra(EXTRA_DOG_ID, (ArrayList<String>) mDogIdList);
        intent.putStringArrayListExtra(EXTRA_DOG_ACTS, (ArrayList<String>) mDogActsList);
        return intent;
    }

    protected Fragment createFragment() {
        Log.d(TAG, "createFragment() called");
        return NewActivityRecordFragment.newInstance();
    }


}
