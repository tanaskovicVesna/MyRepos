package com.tanaskovic.vesna.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.tanaskovic.vesna.model.Repo;
import com.tanaskovic.vesna.repository.Api_Retrofit;
import com.tanaskovic.vesna.repository.Api_Volley;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepoLoader extends Loader<List<Repo>> {

    public static final String TAG = "repoloader";
    public static final String ACTION = "com.reposloader.FORCE";
    //generated GitHub personal access token
    public static String  authkey = "**************************";

    private List<Repo> cachedRepos;

   private static RequestQueue queue;

    /**
     * @param context used to retrieve the application context.
     */
    public RepoLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter filter = new IntentFilter(ACTION);
        manager.registerReceiver(broadcastReceiver,filter);
        if(cachedRepos==null){
            forceLoad();
        }else{
            super.deliverResult(cachedRepos);
        }

    }



    @Override
    public void forceLoad() {
        //start request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api_Retrofit.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api_Retrofit api = retrofit.create(Api_Retrofit.class);

        //make network call
        Call<List<Repo>> call = api.getRepos(authkey);
        call.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
              if(response.isSuccessful()){
                  cachedRepos = response.body();
                  deliverResult(cachedRepos);
              }else{
                  Toast.makeText(getContext(),"An error occurred",Toast.LENGTH_LONG).show();
              }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                Toast.makeText(getContext(),"An error occurred"+t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }


    /** if Volley library is used instead of Retrofit
     *
     *     @Override
     *     public void forceLoad() {
     *         //start request
     *         Api_Volley apiVolley = Api_Volley.getInstance(getContext());
     *         apiVolley.reposList(new Api_Volley.OnArrayResponse() {
     *             @Override
     *             public <T> void onResponse(List<T> list) {
     *                 deliverResult((List<Repo>) list);
     *             }
     *         }, new com.android.volley.Response.ErrorListener() {
     *             @Override
     *             public void onErrorResponse(VolleyError error) {
     *
     *             }
     *         });
     *     }
     *
     *
     *
     * */

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        //cancel request
        onStopLoading();

        // The Loader is being reset, so we should stop monitoring for changes.
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            forceLoad();
        }
    };

    @Override
    public void deliverResult(@Nullable List<Repo> data) {
        cachedRepos = data;
        super.deliverResult(data);

    }

    public void loadNewRepos(){
        forceLoad();
    }

}
