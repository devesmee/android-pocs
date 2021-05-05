package com.example.fhict_companion;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuildingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuildingsFragment extends Fragment {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    View currentview;
    ListView listViewBuildings;
    ImageButton ibToPeopleTab;
    TextView titleTab;
    //ArrayList<String> buildingsList;
    ArrayList<JSONObject> buildingsList;

    public BuildingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BuildingsFragment.
     */
    public static BuildingsFragment newInstance() {
        BuildingsFragment fragment = new BuildingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        // Inflate the layout for this fragment
        currentview = inflater.inflate(R.layout.fragment_buildings, container, false);

        setToolbar();

        setButtonActions();

        listViewBuildings = currentview.findViewById(R.id.buildingsListView);

        /*
        buildingsList = new ArrayList<String>();
        buildingsList.add("Building 1");
        buildingsList.add("Building 2");
        buildingsList.add("Building 3");
        buildingsList.add("Building 4");
        buildingsList.add("Building 5");
        BuildingsAdapter buildingsAdapter = new BuildingsAdapter(getContext(), R.layout.list_item_buildings, buildingsList);
        listViewBuildings.setAdapter(buildingsAdapter);
        */

        RequestBuildings requestbuildingsTask =new RequestBuildings(new AsyncResponse() {

            @Override
            public void processFinish(ArrayList<JSONObject> output) {
                BuildingsAdapter buildingsAdapter = new BuildingsAdapter(getContext(), R.layout.list_item_buildings, output);
                listViewBuildings.setAdapter(buildingsAdapter);
            }
        });

        requestbuildingsTask.execute();

        return currentview;
    }

    public void setToolbar() {
        ibToPeopleTab = getActivity().findViewById(R.id.ibToTab);
        titleTab = getActivity().findViewById(R.id.titleTab);

        ibToPeopleTab.setImageResource(R.drawable.ic_people);
        titleTab.setText(R.string.title_buildings);
    }

    public void setButtonActions() {
        ibToPeopleTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.fragment_container, new PeopleFragment());
                fragmentTransaction.commit();
            }
        });

        ibToPeopleTab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "This icon will redirect you to People.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    // define JSONTask class
    public static class RequestBuildings extends AsyncTask<String, Void, ArrayList<JSONObject>> {

        public AsyncResponse delegate = null;//Call back interface

        public RequestBuildings(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        protected ArrayList<JSONObject> doInBackground(String... params) {
            ArrayList<JSONObject> buildingsList = new ArrayList<JSONObject>();
            //Create URL, in this case for people
            URL url = null;
            try {
                url = new URL("https://api.fhict.nl/buildings");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("URL not correct", "!");
            }
            //Create HttpURLConnection, by opening connection
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Connection cannot open", "!");
            }
            //Set HttpURLConnection properties
            connection.setRequestProperty("Accept", "application/json");
            // Log.e("Token: ", token);
            connection.setRequestProperty("Authorization", "Bearer " + MainActivity.token);
            //Make connection
            try {
                connection.connect();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Connection refused", "!");
            }

            //Get InputStream from URLConnection:
            InputStream is = null;
            try {
                is = connection.getInputStream();
                Scanner scn = new Scanner(is);
                String s = scn.useDelimiter("\\Z").next();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // each array element is an object
                        JSONObject peopleObject = jsonArray.getJSONObject(i);
                        buildingsList.add(peopleObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Get JSON array failed", "!");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Get input stream & scanner failed", "!");
            }
            return buildingsList;
        }

        protected void onPostExecute(ArrayList<JSONObject> result) {
            // update UI using result}
            delegate.processFinish(result);
        }
    }
}