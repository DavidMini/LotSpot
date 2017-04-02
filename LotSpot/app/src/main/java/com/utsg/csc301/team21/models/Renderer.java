package com.utsg.csc301.team21.models;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class Renderer extends DefaultClusterRenderer<ParkingLot> {
    public Renderer(Context context, GoogleMap map, ClusterManager<ParkingLot> clusterManager) {
        super(context, map, clusterManager);
    }

    protected void onBeforeClusterItemRendered(ParkingLot pl, MarkerOptions markerOptions) {
        markerOptions.icon(BitmapDescriptorFactory.fromAsset("solidAndFullMarkers/green_solid_marker.png"));
    }
}
