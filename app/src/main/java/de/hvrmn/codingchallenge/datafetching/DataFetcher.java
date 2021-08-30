package de.hvrmn.codingchallenge.datafetching;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import de.hvrmn.codingchallenge.model.Dataset;

public class DataFetcher {

    private final String URL = "http://data.m-tribes.com/locations.json";

    private final RequestQueue requestQueue;

    public DataFetcher(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.start();
    }

    public void fetchData(DataFetcherCallback dataFetcherCallback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                dataFetcherCallback.onDataRetrieved(new Gson().fromJson(response.toString(), Dataset.class));
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dataFetcherCallback.onError();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
