package com.utsg.csc301.team21.servercalls;

import com.utsg.csc301.team21.models.AbstractParkingLot;

import java.util.List;

/**
 * ILotServer defines a facade for server calls.
 * Implementation should be hidden.
 */

public interface ILotServer {
    List<AbstractParkingLot> getLotsFromGeo(double lat, double lng);
}
