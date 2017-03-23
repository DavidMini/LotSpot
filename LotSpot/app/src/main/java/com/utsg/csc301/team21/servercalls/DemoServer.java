package com.utsg.csc301.team21.servercalls;

import com.utsg.csc301.team21.models.AbstractParkingLot;
import com.utsg.csc301.team21.models.ParkingLot;

import java.util.Arrays;
import java.util.List;

/**
 * DemoServer implementation that has 3 parking lots near UTSG hard-coded.
 */

public class DemoServer implements ILotServer {
    private AbstractParkingLot a = new ParkingLot(1, 30, 3, "DynaPark", "Bedford Rd Toronto, ON M5S 1V4",
            43.666705, -79.405147, 8d);
    private AbstractParkingLot b = new ParkingLot(1, 30, 7, "O.I.S.I.E Parking Garage", "71 Prince Arthur Ave, Toronto, ON M5R 1B3",
            43.657563, -79.403436, 6.5d);
    private AbstractParkingLot c = new ParkingLot(1, 30, 29, "Graduate House Garage", "17 Glen Morris St, Toronto, ON M5S 1H9",
            43.669710, -79.391218, 4.57d);

    @Override
    public List<AbstractParkingLot> getLotsFromGeo(double lat, double lng) {
        return Arrays.asList(a,b,c);
    }

}
