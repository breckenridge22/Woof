package com.osu.cse.apps.mobile.woof;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MultiselectRecyclerViewAdapter extends RecyclerView.Adapter<MultiselectRecyclerViewAdapter.MyViewHolder> {

    private List<Dog> mDogList;
    private Map<String, Boolean> mDogSelected;

    public MultiselectRecyclerViewAdapter(List<Dog> dogList, Map<String, Boolean> dogSelected) {
        mDogList = dogList;
        mDogSelected = dogSelected;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_multi_item_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Dog dog = mDogList.get(position);
        holder.textView.setText(dog.getdogName());

        // Check if this dog has been selected
        Boolean b = mDogSelected.get(dog.getdogId());
        holder.view.setBackgroundColor(b ? Color.CYAN : Color.WHITE);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean dogSelected = mDogSelected.get(dog.getdogId());
                mDogSelected.put(dog.getdogId(), !dogSelected);
                holder.view.setBackgroundColor(mDogSelected.get(dog.getdogId()) ? Color.CYAN : Color.WHITE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDogList == null ? 0 : mDogList.size();
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