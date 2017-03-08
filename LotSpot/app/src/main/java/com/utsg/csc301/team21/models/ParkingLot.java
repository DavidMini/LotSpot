package com.utsg.csc301.team21.models;

import java.math.BigDecimal;

/**
 * Created by hugh on 2017-03-07.
 */

public class ParkingLot {
    int capacity;
    int occupancy;
    BigDecimal pricePerHour; //let's assume all lots provide price per hour for now.
    String name;
    String address;
    double lat;
    double lng;
}
