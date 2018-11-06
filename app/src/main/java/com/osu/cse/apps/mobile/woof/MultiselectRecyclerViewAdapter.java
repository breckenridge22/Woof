package com.osu.cse.apps.mobile.woof;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MultiselectRecyclerViewAdapter extends RecyclerView.Adapter        <MultiselectRecyclerViewAdapter.MyViewHolder> {

    private List<DogInfo> mDogInfoList;
    private Map<String, Boolean> mDogSelected;

    private MultiSelectClick mCallback;

    private static final String TAG = "MultiselectRecyclerViewAdaptor";

    public MultiselectRecyclerViewAdapter(List<DogInfo> dogInfoList, Map<String, Boolean> dogSelected, MultiSelectClick listener) {
        Log.d(TAG, "Called MultiselectRecyclerViewAdapter()");
        mDogInfoList = dogInfoList;
        mDogSelected = dogSelected;
        mCallback = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "Called onCreateViewHolder()");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_multi_item_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Log.d(TAG, "Called onBindViewHolder()");
        final DogInfo dogInfo = mDogInfoList.get(position);
        holder.textView.setText(dogInfo.getdogName());

        // Check if this dog has been selected
        Boolean b = mDogSelected.get(dogInfo.getdogId());
        holder.view.setBackgroundColor(b ? Color.CYAN : Color.WHITE);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Setting text in setOnClickListener()");
                Boolean dogSelected = mDogSelected.get(dogInfo.getdogId());
                mDogSelected.put(dogInfo.getdogId(), !dogSelected);
                holder.view.setBackgroundColor(mDogSelected.get(dogInfo.getdogId()) ? Color.CYAN : Color.WHITE);
                mCallback.onClick(mDogSelected);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDogInfoList == null ? 0 : mDogInfoList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView textView;

        private MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}