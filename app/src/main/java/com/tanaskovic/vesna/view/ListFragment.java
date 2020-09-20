package com.tanaskovic.vesna.view;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tanaskovic.vesna.R;
import com.tanaskovic.vesna.adapter.CustomAdapter;
import com.tanaskovic.vesna.loader.RepoLoader;
import com.tanaskovic.vesna.model.Repo;

import java.util.ArrayList;
import java.util.List;

public class ListFragment  extends Fragment implements  LoaderManager.LoaderCallbacks<List<Repo>>{

    private static final int REPOS_LOADER_ID = 0;

    /** UI references */

    private ArrayList<Repo> repos;
    private CustomAdapter adapter;

    private ListView listView;
    private TextView textView;
    private ProgressBar loadingIndicator;
    private SwipeRefreshLayout pullToRefresh;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initializes the loader

        startLoader(view);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void startLoader(View view) {

        //initialize UI references
        listView = (ListView) view.findViewById(R.id.listItem);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);


        //initialize adapter
        repos = new ArrayList<Repo>();
        adapter = new CustomAdapter(getActivity(), repos);
        //set adapter to the list view
        listView.setAdapter(adapter);
        listView.setEmptyView(textView);
        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);

        //attach listener to the list item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //        listener.onItemSelected((int) position);
                listView.setItemChecked(position, true);
            }
        });


        initializeLoader();
        swipeToRefresh();
    }

    private void  initializeLoader(){
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(REPOS_LOADER_ID, null, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.listfragment, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void swipeToRefresh() {

        if (pullToRefresh != null) {
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                //Counting how many times user have refreshed the layout

                @Override
                public void onRefresh() {

                    Intent intent = new Intent(RepoLoader.ACTION);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

                }
            });
        }
    }

    @Override
    public Loader<List<Repo>> onCreateLoader(int id, Bundle args) {
        return new RepoLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Repo>> loader, List<Repo> data) {
        adapter.clear();

        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }else{
            TextView textView = (TextView) getActivity().findViewById(R.id.no_venues_text);
            listView.setEmptyView(textView);
        }

        loadingIndicator.setVisibility(View.GONE);

        pullToRefresh.setRefreshing(false);
        adapter.setData(data);
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onLoaderReset(Loader<List<Repo>> loader) {
        adapter.clear();
    }


}
