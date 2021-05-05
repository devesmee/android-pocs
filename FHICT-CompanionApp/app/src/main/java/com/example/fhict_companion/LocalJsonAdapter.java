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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LocalJsonAdapter extends BaseAdapter {
    LayoutInflater inflater;
    int resourceLayout;
    ArrayList<JSONObject> classesList;
    TextView nameClasses;
    TextView roomClass;
    TextView subjectClass;
    TextView teacherClass;

    public LocalJsonAdapter(Context context, int resource, ArrayList<JSONObject> classesList) {
        this.resourceLayout = resource;
        this.classesList = classesList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return classesList.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return classesList.get(position);
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

        nameClasses = convertView.findViewById(R.id.nameClasses);
        roomClass = convertView.findViewById(R.id.roomClass);
        subjectClass = convertView.findViewById(R.id.subjectClass);
        teacherClass = convertView.findViewById(R.id.teacherClass);


        try {
            Log.e("LENGTH: ", String.valueOf(getClass(getItem(position)).length()));
            if((getClass(getItem(position)).length()) > 1) {
                StringBuilder classes = new StringBuilder();
                for (int i = 0; i < getClass(getItem(position)).length(); i++)
                {
                    if(i == 0 )
                    {
                        classes.append(getClass(getItem(position)).getString(i));
                    } else {
                        classes.append("-");
                        classes.append(getClass(getItem(position)).getString(i));
                    }
                }
                nameClasses.setText(classes.toString());
            } else {
                nameClasses.setText(getClass(getItem(position)).getString(0));
            }


            //nameClasses.setText(getClass(getItem(position)));
            roomClass.setText(getRoom(getItem(position)));
            subjectClass.setText(getSubject(getItem(position)));
            teacherClass.setText(getTeacher(getItem(position)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public JSONArray getClass(JSONObject classObject) throws JSONException {
        return classObject.getJSONArray("classes");
    }

    public String getRoom(JSONObject classObject) throws JSONException {
        return classObject.getString("room");
    }

    public String getSubject(JSONObject classObject) throws JSONException {
        return classObject.getString("subject");
    }

    public String getTeacher(JSONObject classObject) throws JSONException {
        return classObject.getString("teacherAbbreviation");
    }
}
