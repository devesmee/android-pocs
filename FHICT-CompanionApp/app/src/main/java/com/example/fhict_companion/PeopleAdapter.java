package com.example.fhict_companion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PeopleAdapter extends BaseAdapter {

    LayoutInflater inflater;
    int resourceLayout;
    ArrayList<String> peopleArray;

    public PeopleAdapter(Context context, int resource, ArrayList<String> peopleArray) {
        this.resourceLayout = resource;
        this.peopleArray = peopleArray;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return peopleArray.size();
    }

    @Override
    public String getItem(int position) {
        return peopleArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(resourceLayout, parent, false);
        }

        TextView personName = convertView.findViewById(R.id.namePerson);

        personName.setText(getItem(position));

        return convertView;
    }
}
