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
 * Use the {@link PeopleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeopleFragment extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    View currentView;
    ListView listViewPeople;
    ImageButton ibToBuildingsTab;
    TextView titleTab;
   // ArrayList<String> peopleList;
    ArrayList<JSONObject> peopleList;

    public PeopleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PeopleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PeopleFragment newInstance() {
        PeopleFragment fragment = new PeopleFragment();
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
        currentView = inflater.inflate(R.layout.fragment_people, container, false);

        setToolbar();

        setButtonActions();

        listViewPeople = currentView.findViewById(R.id.peopleListView);
        /*
        peopleList = new ArrayList<String>();
        peopleList.add("Person one");
        peopleList.add("Person two");
        peopleList.add("Person three");
        peopleList.add("Person four");
        peopleList.add("Person five");

        PeopleAdapter peopleAdapter = new PeopleAdapter(getContext(), R.layout.list_item_people, peopleList);
        listViewPeople.setAdapter(peopleAdapter);
        */
        RequestPeople requestPeopleTask =new RequestPeople(new AsyncResponse() {

            @Override
            public void processFinish(ArrayList<JSONObject> output) {
                PeopleFontysAdapter peopleFontysAdapter = new PeopleFontysAdapter(getContext(), R.layout.list_item_people_fontys, output);
                listViewPeople.setAdapter(peopleFontysAdapter);
            }
        });

        requestPeopleTask.execute();
        return currentView;
    }

    public void setToolbar() {
        ibToBuildingsTab = getActivity().findViewById(R.id.ibToTab);
        titleTab = getActivity().findViewById(R.id.titleTab);

        ibToBuildingsTab.setImageResource(R.drawable.ic_building);
        titleTab.setText(R.string.title_people);
    }

    public void setButtonActions() {
        ibToBuildingsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.fragment_container, new BuildingsFragment());
                fragmentTransaction.commit();
            }
        });

        ibToBuildingsTab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "This icon will redirect you to Buildings.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    // define JSONTask class
    public static class RequestPeople extends AsyncTask<String, Void, ArrayList<JSONObject>> {

        public AsyncResponse delegate = null;//Call back interface

        public RequestPeople(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interface through constructor
        }

        protected ArrayList<JSONObject> doInBackground(String... params) {
            ArrayList<JSONObject> peopleList = new ArrayList<JSONObject>();
            //Create URL, in this case for people
            URL url = null;
            try {
                url = new URL("https://api.fhict.nl/people");
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
                    //Log.e("JSON OBJECT: ", jsonObject.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // each array element is an object
                        JSONObject peopleObject = jsonArray.getJSONObject(i);
                        peopleList.add(peopleObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Get JSON array failed", "!");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Get input stream & scanner failed", "!");
            }

            //Log.e("peopleList: ", peopleList.toString());
            return peopleList;
        }

        protected void onPostExecute(ArrayList<JSONObject> result) {
            // update UI using result}
            delegate.processFinish(result);
        }
    }
}

