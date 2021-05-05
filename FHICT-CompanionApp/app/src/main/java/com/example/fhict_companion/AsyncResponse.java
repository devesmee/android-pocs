package com.example.fhict_companion;

import org.json.JSONObject;

import java.util.ArrayList;

interface AsyncResponse {
    void processFinish(ArrayList<JSONObject> output);
}
