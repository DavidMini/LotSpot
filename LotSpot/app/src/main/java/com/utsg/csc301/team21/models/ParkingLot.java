package com.utsg.csc301.team21.models;

import java.math.BigDecimal;

/**
 * Parking Lot
 *  - Basic implementation of Abstract Parking Lot.
 */

public class ParkingLot extends AbstractParkingLot{


    public ParkingLot(int id, int capacity, int occupancy, String name, String address, double lat, double lng, Double pricePerHour) {
        this.id = id;
        this.capacity = capacity;
        this.occupancy = occupancy;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.pricePerHour = BigDecimal.valueOf( pricePerHour);
    }

}
