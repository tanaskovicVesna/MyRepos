package com.tanaskovic.vesna.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tanaskovic.vesna.R;
import com.tanaskovic.vesna.model.Repo;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Repo> {
    private LayoutInflater inflater;
    private List<Repo> repos;

    public CustomAdapter(@NonNull Context context, @NonNull List<Repo> objects) {
        super(context, 0, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView name = (TextView) listItemView.findViewById(R.id.text1);
        name.setText(getItem(position).getName());

        return listItemView;

    }
    public void setData(List<Repo> data) {
        clear();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                add(data.get(i));
            }
        }
    }

    public int getSize(List<Repo> data){
        return data.size();
    }

    public void swapData(List<Repo> data){
        this.repos.clear();
        this.repos.addAll(data);
        notifyDataSetChanged();
    }


}

