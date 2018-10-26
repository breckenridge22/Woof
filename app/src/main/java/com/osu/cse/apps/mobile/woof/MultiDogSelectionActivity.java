package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.osu.cse.apps.mobile.woof.CurrentUser.getDogMap;

public class MultiDogSelectionActivity extends AppCompatActivity {

    private List<Dog> mDogList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, DogSelectionActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve list of dogs
        List<Dog> dogList = new ArrayList(CurrentUser.getDogMap().values());
        Map<String, Boolean> dogSelected = new HashMap<>();
        // Default select values = false; Set in map w/ DogId keys
        for (Dog dog:dogList){
            String dogID = dog.getdogId();
            dogSelected.put(dogID, false);
        }

        setContentView(R.layout.multi_select_dog);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MultiselectRecyclerViewAdapter(mDogList, dogSelected);
        LinearLayoutManager manager = new LinearLayoutManager(MultiDogSelectionActivity.this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }
}

