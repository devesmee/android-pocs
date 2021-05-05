package com.example.fhict_companion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BuildingsAdapter extends BaseAdapter {
    LayoutInflater inflater;
    int resourceLayout;
    ArrayList<JSONObject> buildingsList;

    public BuildingsAdapter(Context context, int resource, ArrayList<JSONObject> buildingsList) {
        this.resourceLayout = resource;
        this.buildingsList = buildingsList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return buildingsList.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return buildingsList.get(position);
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

        TextView idTextView = convertView.findViewById(R.id.idBuilding);
        TextView nameTextView = convertView.findViewById(R.id.nameBuilding);
        TextView cityTextView = convertView.findViewById(R.id.cityBuilding);

        try {
            idTextView.setText(getId(getItem(position)));
            nameTextView.setText(getName(getItem(position)));
            cityTextView.setText(getCity(getItem(position)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public String getId(JSONObject buildingObject) throws JSONException {
        return buildingObject.getString("id");
    }

    public String getName(JSONObject buildingObject) throws JSONException {
        return buildingObject.getString("name");
    }

    public String getCity(JSONObject buildingObject) throws JSONException {
        return buildingObject.getString("city");
    }
}
