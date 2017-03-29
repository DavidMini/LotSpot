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
    String name = "";
    String address;
    String phoneNumber;
    String paymentType;
    double lat;
    double lng;
    BigDecimal pricePerHour; //TODO: Change this to just price payment type tells us the hour/moth/year rate
    boolean handicapParking;

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

    public String getPhoneNumber(){return this.phoneNumber;}

    public String getPaymentType(){return  this.paymentType;}


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

    public void setPhoneNumber (String phoneNumber) {this.phoneNumber = phoneNumber;}

    public void setHandicapParking (boolean hParking){this.handicapParking = hParking;}

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
