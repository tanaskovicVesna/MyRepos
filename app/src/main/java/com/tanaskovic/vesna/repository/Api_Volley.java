package com.tanaskovic.vesna.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tanaskovic.vesna.model.Repo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Api_Volley {

    private static RequestQueue mRequestQueue;
    public static ArrayList<Repo> repos;

    /** MENU Api URL  */

    public static final String API_URL = "https://api.github.com/users/tanaskovicVesna/repos";


    private static Api_Volley mInstance;

    private static Context mCtx;

    private Api_Volley(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

    }

    /**
     * Constructor
     */

    public static synchronized Api_Volley getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Api_Volley(context);
        }
        return mInstance;
    }


    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public static  <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    /** List of all repos */
    public  void reposList(final OnArrayResponse arrayResponse, final Response.ErrorListener onErrorResponse) {

        if(!isNetworkAvailable()) {
            VolleyError error = new VolleyError("No internet");
            onErrorResponse.onErrorResponse(error);
            return;
        }

        StringRequest reposRequest = new StringRequest(Request.Method.GET, API_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                /** Extract venues from JSON */


                try {
                    ArrayList<Repo> repos = new ArrayList<>();
                    JSONArray rootArray  = new JSONArray(response);


                    for(int i = 0; i < rootArray.length(); i++) {
                        JSONObject currentRepo= rootArray.getJSONObject(i);
                        String name = currentRepo.getString("name");

                        Repo repo= new Repo(name);
                        repos.add(repo);
                    }
                    arrayResponse.onResponse(repos);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onErrorResponse.onErrorResponse(error);
            }
        }) {


            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "******************************");
                return params;
            }
        };
        addToRequestQueue(reposRequest);
    }

    public interface OnObjectResponse {
        <T> void onResponse(T object);
    }

    public interface OnErrorResponse {
        void onError(VolleyError error);
    }

    public interface OnArrayResponse {
        <T> void onResponse(List<T> list);
    }

    /**
     * Detect whether there is an Internet connection available on Android
     */
    public static boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    /**
     * Making Toast message
     * @param text is message to be displayed
     */
    public static void makeToast(String text) {
        Toast.makeText(mCtx, text, Toast.LENGTH_SHORT).show();
    }
}
