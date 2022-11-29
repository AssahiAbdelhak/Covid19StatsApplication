package com.example.covidapplication;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataFromApi {
    Context context;
    public DataFromApi(Context context) {
        this.context = context;
    }

    void listAllCountries(MainActivity.GetInfos sendInfos){
        String url = "https://restcountries.com/v3.1/all";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string
                        List<CountryModule> list = new ArrayList<>();
                        for(int i = 0; i < response.length();i++){
                            CountryModule cm = new CountryModule();
                            try {
                                cm.setCountryName(response.getJSONObject(i).getJSONObject("name").getString("common"));
                                cm.setCountryFlag(response.getJSONObject(i).getJSONObject("flags").getString("png"));
                                list.add(cm);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        sendInfos.sendCountries(list);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    void getSlugFromCountry(String countryName, MainActivity.GetInfos getInfos){
        String url = "https://api.covid19api.com/countries";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
                        try {
                            for (int i = 0 ; i < response.length();i++){
                                if(response.getJSONObject(i).getString("Country").equals(countryName)){
                                    getInfos.sendSlug(response.getJSONObject(i).getString("Slug"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    void getStats(String countryName,MainActivity.GetInfos getInfos){
        String url = "https://api.covid19api.com/total/country/"+countryName;
        System.out.println(url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = response.getJSONObject(response.length()-1);
                            getInfos.sendStats(jsonObject.get("Deaths").toString(),jsonObject.get("Recovered").toString(),jsonObject.get("Active").toString(),jsonObject.get("Confirmed").toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    void getGlobalStats(MainActivity.GetInfos getInfos){
        String url = "https://api.covid19api.com/summary";
        System.out.println(url);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject obj = response.getJSONObject("Global");
                            getInfos.sendStats(obj.getString("TotalDeaths"), obj.getString("TotalRecovered"),"0", obj.getString("TotalConfirmed") );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

}
