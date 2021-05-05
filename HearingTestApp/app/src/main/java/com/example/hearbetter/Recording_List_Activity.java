package com.example.hearbetter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Recording_List_Activity extends AppCompatActivity {
    ListView listView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_list_);



        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list_records);


        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/HearBetter");
        File[] filelist = dir.listFiles();
        List<String> list_files = new ArrayList<String>();
        for (int i = 0; i < filelist.length; i++) {
            if(filelist[i].getName().contains("HearBetter")){
                list_files.add(filelist[i].getName());
            }
        }

        Collections.sort(list_files, String.CASE_INSENSITIVE_ORDER);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_textview, list_files);
        // Assign adapter to ListView

        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                Intent i = new Intent(Recording_List_Activity.this, ResultRecordActivity.class);
                Bundle args = new Bundle();
                i.putExtra("record_name", itemValue);
                startActivity(i);
            }
        });
    }


}
