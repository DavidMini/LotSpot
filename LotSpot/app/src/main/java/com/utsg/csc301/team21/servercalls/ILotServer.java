package com.utsg.csc301.team21.servercalls;

import com.utsg.csc301.team21.models.ParkingLot;

import java.util.List;

/**
 * Created by hugh on 2017-03-07.
 */

public interface ILotServer {
    public List<ParkingLot> getLotsFromGeo(double lat, double lng);
}
