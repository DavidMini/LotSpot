package com.utsg.csc301.team21.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.math.BigDecimal;

/**
 * Parking Lot
 *  - Basic implementation of Abstract Parking Lot.
 */

public class ParkingLot extends AbstractParkingLot implements ClusterItem{


    public ParkingLot(int id, int capacity, int occupancy, String name, String address, double lat, double lng, Double pricePerHour) {
        this.id = id;
        this.capacity = capacity;
        this.occupancy = occupancy;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.pricePerHour = pricePerHour;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(lat, lng);
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return "Vacancy: " + (capacity - occupancy) + " / " + capacity;
    }
}
