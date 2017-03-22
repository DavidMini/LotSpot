package com.utsg.csc301.team21.models;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.ListFragment;

import com.example.mapwithmarker.R;
import com.utsg.csc301.team21.servercalls.DemoServer;
import com.utsg.csc301.team21.servercalls.ILotServer;

import java.util.ArrayList;

public class SearchResultFragment extends ListFragment {
    ILotServer mServer = new DemoServer();
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Load the layout for this fragment into the right_drawer
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ParkingArrayAdapter adp = new ParkingArrayAdapter(getActivity(),
                R.layout.fragment_search_result);
        setListAdapter(adp);
        adp.addAll(mServer.getLotsFromGeo(0,0));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO implement some logic
        
    }
}