package com.example.fhict_companion;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PeopleFontysAdapter extends BaseAdapter {

    LayoutInflater inflater;
    int resourceLayout;
    ArrayList<JSONObject> peopleList;

    public PeopleFontysAdapter(Context context, int resource, ArrayList<JSONObject> peopleList) {
        this.resourceLayout = resource;
        this.peopleList = peopleList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return peopleList.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return peopleList.get(position);
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

        TextView firstNameTextView = convertView.findViewById(R.id.firstNamePerson);
        TextView lastNameTextView = convertView.findViewById(R.id.lastnamePerson);
        TextView departmentTextView = convertView.findViewById(R.id.departmentPerson);
        TextView emailTextView = convertView.findViewById(R.id.emailPerson);

        try {
            firstNameTextView.setText(getFirstName(getItem(position)));
            lastNameTextView.setText(getLastName(getItem(position)));
            departmentTextView.setText(getDepartment(getItem(position)));
            emailTextView.setText(getEmail(getItem(position)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public String getFirstName(JSONObject personObject) throws JSONException {
        return personObject.getString("givenName");
    }

    public String getLastName(JSONObject personObject) throws JSONException {
        return personObject.getString("surName");
    }

    public String getDepartment(JSONObject personObject) throws JSONException {
        return personObject.getString("department");
    }

    public String getEmail(JSONObject personObject) throws JSONException {
        return personObject.getString("mail");
    }
}
