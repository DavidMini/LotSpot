package com.example.mapwithmarker;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback, OnMarkerClickListener {

    // Global Variables
    private GoogleMap mGoogleMap;
    private Map<Marker, View> markers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Set Map
        mGoogleMap = googleMap;
        // Populate map with parking lot markers
        addMarkers();
        // Set a listener for Marker click.
        mGoogleMap.setOnMarkerClickListener(this);
        //TODO: Cluster Markers
    }

    public void onSearch(View view)
    {
        EditText location_tf = (EditText)findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if(location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
            mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        }
    }
    public void addMarkers(){
        // Initial focus on UofT TODO:Set to current location
        LatLng uoft = new LatLng(43.662892, -79.395656);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uoft, 14));

        // List of Parking Lots
        LatLng latLngLot01 = new LatLng(43.666705, -79.405147);
        Marker lot01 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot01)
                .title("Lot 01")
                .icon(BitmapDescriptorFactory.fromAsset("marker_orange3.png")));


        LatLng latLngLot02 = new LatLng(43.665385, -79.403477);
        Marker lot02 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot02)
                .title("Lot 02")
                .icon(BitmapDescriptorFactory.fromAsset("marker_green14.png")));

        LatLng latLngLot03 = new LatLng(43.657563, -79.403436);
        Marker lot03 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot03)
                .title("Lot 03")
                .icon(BitmapDescriptorFactory.fromAsset("marker_green30.png")));

        LatLng latLngLot04 = new LatLng(43.655946, -79.408577);
        Marker lot04 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot04)
                .title("Lot 04")
                .icon(BitmapDescriptorFactory.fromAsset("marker_orange4.png")));

        LatLng latLngLot05 = new LatLng(43.652175, -79.405963);
        Marker lot05 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot05)
                .title("Lot 05")
                .icon(BitmapDescriptorFactory.fromAsset("marker_green26.png")));

        LatLng latLngLot06 = new LatLng(43.652586, -79.398445);
        Marker lot06 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot06)
                .title("Lot 06")
                .icon(BitmapDescriptorFactory.fromAsset("marker_red1.png")));

        LatLng latLngLot07 = new LatLng(43.659353, -79.389422);
        Marker lot07 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot07)
                .title("Lot 07")
                .icon(BitmapDescriptorFactory.fromAsset("marker_red2.png")));

        LatLng latLngLot08 = new LatLng(43.659891, -79.388625);
        Marker lot08 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot08)
                .title("Lot 08")
                .icon(BitmapDescriptorFactory.fromAsset("marker_green11.png")));

        LatLng latLngLot09 = new LatLng(43.667672, -79.389450);
        Marker lot09 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot09)
                .title("Lot 09")
                .icon(BitmapDescriptorFactory.fromAsset("marker_orange5.png")));

        LatLng latLngLot10 = new LatLng(43.669112, -79.388623);
        Marker lot10 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot10)
                .title("Lot 10")
                .icon(BitmapDescriptorFactory.fromAsset("marker_green56.png")));

        LatLng latLngLot11 = new LatLng(43.669710, -79.391218);
        Marker lot11 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot11)
                .title("Lot 11")
                .icon(BitmapDescriptorFactory.fromAsset("marker_green38.png")));

        LatLng latLngLot12 = new LatLng( 43.669447, -79.392248);
        Marker lot12 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot12)
                .title("Lot 12")
                .icon(BitmapDescriptorFactory.fromAsset("marker_green19.png")));

        LatLng latLngLot13 = new LatLng( 43.671654, -79.394603);
        Marker lot13 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot13)
                .title("Lot 13")
                .icon(BitmapDescriptorFactory.fromAsset("marker_red2.png")));

        LatLng latLngLot14 = new LatLng( 43.674856, -79.398259);
        Marker lot14 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot14)
                .title("Lot 14")
                .icon(BitmapDescriptorFactory.fromAsset("marker_green23.png")));

        LatLng latLngLot15 = new LatLng( 43.670676, -79.382509);
        Marker lot15 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot15)
                .title("Lot 15")
                .icon(BitmapDescriptorFactory.fromAsset("marker_green15.png")));

        LatLng latLngLot16 = new LatLng( 43.669883, -79.382455);
        Marker lot16 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot16)
                .title("Lot 16")
                .icon(BitmapDescriptorFactory.fromAsset("marker_orange6.png")));

        LatLng latLngLot17 = new LatLng( 43.659829, -79.380369);
        Marker lot17 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot17)
                .title("Lot 17")
                .icon(BitmapDescriptorFactory.fromAsset("marker_red2.png")));

        LatLng latLngLot18 = new LatLng( 43.657764, -79.376211);
        Marker lot18 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot18)
                .title("Lot 18")
                .icon(BitmapDescriptorFactory.fromAsset("marker_green9.png")));

        LatLng latLngLot19 = new LatLng( 43.658145, -79.385359);
        Marker lot19 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot19)
                .title("Lot 19")
                .icon(BitmapDescriptorFactory.fromAsset("marker_green39.png")));

        LatLng latLngLot20 = new LatLng( 43.656254, -79.388198);
        Marker lot20 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot20)
                .title("Lot 20")
                .icon(BitmapDescriptorFactory.fromAsset("marker_green64.png")));

        LatLng latLngLot21 = new LatLng( 43.654792, -79.389622);
        Marker lot21 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot21)
                .title("Lot 21")
                .icon(BitmapDescriptorFactory.fromAsset("marker_red3.png")));

        LatLng latLngLot22 = new LatLng( 43.654358, -79.388712);
        Marker lot22 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot22)
                .title("Lot 22")
                .icon(BitmapDescriptorFactory.fromAsset("marker_green31.png")));

        LatLng latLngLot23 = new LatLng( 43.654010, -79.387221);
        Marker lot23 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot23)
                .title("Lot 23")
                .icon(BitmapDescriptorFactory.fromAsset("marker_orange7.png")));

        LatLng latLngLot24 = new LatLng( 43.654806, -79.386678);
        Marker lot24 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot24)
                .title("Lot 24")
                .icon(BitmapDescriptorFactory.fromAsset("marker_orange4.png")));
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        // Check if a click count was set, then resolve.TODO

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

}
