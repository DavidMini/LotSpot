package com.utsg.csc301.team21.models;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class Renderer extends DefaultClusterRenderer<ParkingLot> {
    public Renderer(Context context, GoogleMap map, ClusterManager<ParkingLot> clusterManager) {
        super(context, map, clusterManager);
    }

    protected void onBeforeClusterItemRendered(ParkingLot pl, MarkerOptions markerOptions) {
        int occupancy = pl.getOccupancy();
        int capacity = pl.getCapacity();
        double r = (1.0 * occupancy) / (1.0 * capacity);
        String color;

        if (r >= 0.75) {
            color = "red";
        }
        else if (r >= 0.5 && r < 0.75) {
            color = "orange";
        }
        else {
            color = "green";
        }

        markerOptions.icon(BitmapDescriptorFactory.
                fromAsset("solidAndFullMarkers/" + color + "_solid_marker.png"));
    }


}
