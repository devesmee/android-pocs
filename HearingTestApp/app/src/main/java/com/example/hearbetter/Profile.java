package com.example.hearbetter;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Profile {
    static JSONObject profile = new JSONObject();
    static JSONArray array = new JSONArray();

    public static void AddData(String var, String value){
        try {
            profile.put(var, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void SaveToFolder() {
        array.put("Profile");
        array.put(profile);
        File root = android.os.Environment.getExternalStorageDirectory();
        try {
            Writer output;
            File dir = new File (root.getAbsolutePath() + "/HearBetter");
            dir.mkdirs();
            File file = new File(dir, "Profile.txt");
            output = new BufferedWriter(new FileWriter(file));
            output.write(array.toString());
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("Error Dir", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
