package com.example.fhict_companion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LocalJsonActivity extends AppCompatActivity {

    ListView listViewClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_json);

        listViewClasses = findViewById(R.id.localJsonListView);

        // Create InputStream, by passing file name
        InputStream is = null;
        try {
            is = getApplicationContext().getAssets().open("json.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create Scanner, by passing InputStream (Scanner is used to read text from a file):
        Scanner scn = new Scanner(is);
        // Read the whole file, and put into String:
        String s = scn. useDelimiter("\\Z").next();
        ArrayList<JSONObject> classesList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++) {
                // each array element is an object
                JSONObject classObject = jsonArray.getJSONObject(i);
                // from each object, get field "subject", which is a string
                // add each "subject" string to list
                classesList.add(classObject);}

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LocalJsonAdapter peopleAdapter = new LocalJsonAdapter(this, R.layout.list_item_local_json, classesList);
        listViewClasses.setAdapter(peopleAdapter);

    }
}