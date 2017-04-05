package com.utsg.csc301.team21.models;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.ListFragment;

import com.example.mapwithmarker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.utsg.csc301.team21.servercalls.DemoServer;
import com.utsg.csc301.team21.servercalls.ILotServer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.mapwithmarker.MapsMarkerActivity;

public class SearchResultFragment extends ListFragment {
    ILotServer mServer = new DemoServer();
    List<AbstractParkingLot> mLots = mServer.getLotsFromGeo(0,0);
    ParkingArrayAdapter adp;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Load the layout for this fragment into the right_drawer
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        // Setup adapter for this fragment
        adp = new ParkingArrayAdapter(getActivity(),
                R.layout.fragment_search_result);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(adp);
        adp.addAll(mLots);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        MapsMarkerActivity a = (MapsMarkerActivity) getActivity();
        AbstractParkingLot p = (AbstractParkingLot) getListAdapter().getItem(position);
        a.moveToLocation(p);
        a.closeLeftDrawer();
    }



    // Use this by
    // getSupportFragmentManager().findFragmentById(R.id.result_fragment).updateResult(......)

    public List<AbstractParkingLot> filterResult(List<AbstractParkingLot> parkingLots, int cost, int dist, int height,
                             boolean access, double curr_lat, double curr_lng) {
        List<AbstractParkingLot> lots = new ArrayList<AbstractParkingLot>();
        Log.d("MyActivity", "Starting lot " + parkingLots.toString());

        for (AbstractParkingLot p : parkingLots) {
            // Checks if the ParkingLot has free space
            if ((p.getCapacity() - p.getOccupancy()) < 1) {
                Log.d("MyActivity", "capcity over " + p.toString());
                continue;
            }
            // Check price filter
            if (p.pricePerHour > cost) {
                Log.d("MyActivity", "cost over " + p.toString());
                continue;
            }
            // Check height filter //TODO: we don't have height in ParkingLot
            double p_height = 10;
            if (p_height < height) {
                Log.d("MyActivity", "height over " + p.toString());
                continue;
            }
            // Checks accessibility
            boolean p_access = p.handicapParking;
            if (!p_access && access) {
                Log.d("MyActivity", "access over " + p.toString());
                continue;
            }
            // Check distance filter
            LatLng curr_location = new LatLng(curr_lat, curr_lng);
            LatLng tar_location = new LatLng(p.getLat(), p.getLng());
            if (CalculationByDistance(curr_location, tar_location) > dist) {
                Log.d("MyActivity", "distance over " + CalculationByDistance(curr_location, tar_location));
                continue;
            }

            // ParkingLot is good, add it to result
            lots.add(p);

        }
        // Update this fragment with the latest ParkingLots
        adp.clear();
        adp.addAll(lots);

        Log.d("MyActivity", "test_parkinglotArray: " + lots.toString());
        return lots;
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        return kmInDec;
    }





}