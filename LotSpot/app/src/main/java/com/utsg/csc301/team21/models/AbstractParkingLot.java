package com.utsg.csc301.team21.models;

import org.json.JSONObject;

import java.math.BigDecimal;

/**
 *  AbstractParkingLot has all the attributes that we require from parking lots.
 */

public abstract class AbstractParkingLot {
    int id; //whatever the server uses as the key
    int capacity;
    int occupancy;
    String name;
    String address;
    double lat;
    double lng;
    BigDecimal pricePerHour; //let's assume all lots provide price per hour for now.

    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lng;
    }

    public int getId() {
        return id;
    }

    public boolean fromJson(JSONObject jsonObject) {
        return false;
    }

    public JSONObject toJson() {
        return null;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getOccupancy() {
        return this.occupancy;
    }

    public int getRemainingSpaces() {
        return getCapacity()-getOccupancy();
    }


    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setPricePerHour(BigDecimal pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public BigDecimal getPricePerHour() {
        return pricePerHour;
    }
}
