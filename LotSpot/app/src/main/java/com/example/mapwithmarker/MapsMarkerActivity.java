package com.example.mapwithmarker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.maps.android.clustering.ClusterManager;
import com.utsg.csc301.team21.models.AbstractParkingLot;
import com.utsg.csc301.team21.models.LotInfoBoxFragment;
import com.utsg.csc301.team21.models.ParkingLot;
import com.utsg.csc301.team21.models.Renderer;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback, OnMarkerClickListener, GoogleMap.OnMapClickListener,
        LotInfoBoxFragment.OnFragmentInteractionListener,
        ActivityCompat.OnRequestPermissionsResultCallback{

    // Global Variables
    private GoogleMap mGoogleMap;
    private Map<Marker, View> markers = new HashMap<>();
    private Set<ParkingLot> parkingLotsArray = new HashSet<>();

    // Drawer Variables
    private DrawerLayout mDrawerLayout;
    private View mLeftDrawerView;
    private View mRightDrawerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private Button mOptionButton;
    private PopupWindow mPopupWindow;
    private Marker searched = null;
    private ClusterManager<ParkingLot> mClusterManager;

    // Used in displaying the lot info box
    private Fragment infoBox = null;

    // Persistence value in popup option
    private int oCost = 30;
    private int oDistance = 10;
    private int oHeight = 2;
    private boolean oAccess = true;

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

        // Adding button for opening drawer
        mTitle = mDrawerTitle = getTitle();
        final android.support.v7.app.ActionBar mActionBar = getSupportActionBar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawerView = findViewById(R.id.left_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                if (view.equals(mLeftDrawerView)) {
                    super.onDrawerClosed(view);
                    mActionBar.setTitle(mTitle);
                }
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                if (drawerView.equals(mLeftDrawerView)) {
                    super.onDrawerOpened(drawerView);
                    mActionBar.setTitle(mDrawerTitle);
                }
            }

            // Only move the toggle icon when the left_drawer is moved.
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (drawerView.equals(mLeftDrawerView)) {
                    super.onDrawerSlide(drawerView, slideOffset);
                }
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        //Get the My Location button in order to change it's position
        View locationButton = ((View) mapFragment.getView().findViewById(
          Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        //reposition the My Location Button
         repositionMyLocationButton(locationButton);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Configure the search info and add any event listeners
         SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        public boolean onQueryTextChange(String newText) {
            // this is your adapter that will be filtered
            return true;
        }

        public boolean onQueryTextSubmit(String query) {
            //Here u can get the value "query" which is entered in the search box.
            onSearch(query);
            return true;
        }

    };
    searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }
    public void repositionMyLocationButton(View locationButton) {

        RelativeLayout.LayoutParams locbuttonlayout = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        locbuttonlayout.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        locbuttonlayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        locbuttonlayout.setMargins(0, 0, 100, 250);
    }


    public void disableParentMovement(View seekbar) {
        seekbar.setOnTouchListener(new RelativeLayout.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        // Disallow Drawer to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow Drawer to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle seekbar touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
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

        // Attaches click listener for deselecting a marker
        mGoogleMap.setOnMapClickListener(this);

        // Set a listener for Marker click.
        mGoogleMap.setOnMarkerClickListener(this);

        // Set map style
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

        // Set map location
        if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted

                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
            requestPermissions ();

            }
        //TODO: Cluster Markers
        setUpCluster();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        switch (item.getItemId()) {

            case R.id.action_option:
                initiazePopupWindow();


            default:
                //invoke superclass to handle not recognized actions
                return super.onOptionsItemSelected(item);

        }
    }

    public void onSearch(String search)
    {
        //EditText location_tf = (EditText)findViewById(R.id.TFaddress);
        String location = search;
        List<Address> addressList = null;
        if( !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            // Checks if Google actually found a location
            if (addressList.size() > 0) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
                if(searched!= null){
                    searched.remove();
                }
                searched = mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Updates markers on the map, removes previous markers and updates with list
     */
    public void updateMarkers(){
        // TODO: Controller function call
        // populateLotArray();

        //For each parking lot create a new marker
        Iterator<ParkingLot> lots = parkingLotsArray.iterator();
        String iconColor= "";
        while (lots.hasNext()){
            ParkingLot lot = lots.next();

            // Set which color icon to use
            if (lot.getOccupancy()/4 < lot.getCapacity() && lot.getOccupancy()/2 > lot.getCapacity()){
                iconColor = "orange";
            }else if(lot.getOccupancy()/2 < lot.getCapacity()){
                iconColor = "green";
            }else{
                iconColor = "red";
            }

            // Create marker
            LatLng tempLatLng = new LatLng(lot.getLat(), lot.getLng());
            mGoogleMap.addMarker(new MarkerOptions().position(tempLatLng)
                    .title(lot.getName())
                    .icon(BitmapDescriptorFactory.fromAsset("newMarkers/"+ iconColor +"_3_marker.png")));
                    // TODO: Draw on asset number of available lots
                    // According to capacity choose color
        }
    }

    private void setUpCluster() {
        mClusterManager = new ClusterManager<ParkingLot>(this, mGoogleMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mGoogleMap.setOnCameraIdleListener(mClusterManager);
        // We dont need this yet ⬇️
        // mGoogleMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setRenderer(new Renderer(this, mGoogleMap, mClusterManager));

        // Hardcoded testing ParkingLot instances
        ParkingLot pl1 = new ParkingLot(1, 100, 50, "Parking Lot 01",
                null, 43.662892, -79.395656, 5.0);
        ParkingLot pl2 = new ParkingLot(2, 100, 50, "Parking Lot 02",
                null, 43.665385, -79.403477, 5.0);
        ParkingLot pl3 = new ParkingLot(2, 100, 50, "Parking Lot 03",
                null, 43.657563, -79.403436, 5.0);
        ParkingLot pl4 = new ParkingLot(2, 100, 50, "Parking Lot 04",
                null, 43.655946, -79.408577, 5.0);
        ParkingLot pl5 = new ParkingLot(2, 100, 50, "Parking Lot 05",
                null, 43.652175, -79.405963, 5.0);
        ParkingLot pl6 = new ParkingLot(2, 100, 50, "Parking Lot 06",
                null, 43.652586, -79.398445, 5.0);
        ParkingLot pl7 = new ParkingLot(2, 100, 50, "Parking Lot 07",
                null, 43.659353, -79.389422, 5.0);
        ParkingLot pl8 = new ParkingLot(2, 100, 50, "Parking Lot 08",
                null, 43.659891, -79.388625, 5.0);
        ParkingLot pl9 = new ParkingLot(2, 100, 50, "Parking Lot 09",
                null, 43.667672, -79.389450, 5.0);
        ParkingLot pl10 = new ParkingLot(2, 100, 50, "Parking Lot 10",
                null, 43.669112, -79.388623, 5.0);

        mClusterManager.addItem(pl1);
        mClusterManager.addItem(pl2);
        mClusterManager.addItem(pl4);
        mClusterManager.addItem(pl5);
        mClusterManager.addItem(pl6);
        mClusterManager.addItem(pl7);
        mClusterManager.addItem(pl8);
        mClusterManager.addItem(pl9);
        mClusterManager.addItem(pl10);
    }



    public void addMarkers(){
        // Initial focus on UofT TODO:Set to current location
        LatLng uoft = new LatLng(43.662892, -79.395656);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uoft, 14));

        // List of Parking Lots
        LatLng latLngLot01 = new LatLng(43.666705, -79.405147);
        Marker lot01 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot01)
                .title("Parking Lot 01")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/orange_3_marker.png")));


        LatLng latLngLot02 = new LatLng(43.665385, -79.403477);
        Marker lot02 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot02)
                .title("Parking Lot 02")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/green_14_marker.png")));

        LatLng latLngLot03 = new LatLng(43.657563, -79.403436);
        Marker lot03 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot03)
                .title("Parking Lot 03")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/green_30_marker.png")));

        LatLng latLngLot04 = new LatLng(43.655946, -79.408577);
        Marker lot04 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot04)
                .title("Parking Lot 04")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/orange_4_marker.png")));

        LatLng latLngLot05 = new LatLng(43.652175, -79.405963);
        Marker lot05 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot05)
                .title("Parking Lot 05")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/green_26_marker.png")));

        LatLng latLngLot06 = new LatLng(43.652586, -79.398445);
        Marker lot06 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot06)
                .title("Parking Lot 06")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/red_1_marker.png")));

        LatLng latLngLot07 = new LatLng(43.659353, -79.389422);
        Marker lot07 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot07)
                .title("Parking Lot 07")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/red_2_marker.png")));

        LatLng latLngLot08 = new LatLng(43.659891, -79.388625);
        Marker lot08 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot08)
                .title("Parking Lot 08")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/green_11_marker.png")));

        LatLng latLngLot09 = new LatLng(43.667672, -79.389450);
        Marker lot09 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot09)
                .title("Parking Lot 09")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/orange_5_marker.png")));

        LatLng latLngLot10 = new LatLng(43.669112, -79.388623);
        Marker lot10 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot10)
                .title("Parking Lot 10")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/green_56_marker.png")));

        LatLng latLngLot11 = new LatLng(43.669710, -79.391218);
        Marker lot11 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot11)
                .title("Parking Lot 11")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/green_38_marker.png")));

        LatLng latLngLot12 = new LatLng( 43.669447, -79.392248);
        Marker lot12 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot12)
                .title("Parking Lot 12")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/green_19_marker.png")));

        LatLng latLngLot13 = new LatLng( 43.671654, -79.394603);
        Marker lot13 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot13)
                .title("Parking Lot 13")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/red_2_marker.png")));

        LatLng latLngLot14 = new LatLng( 43.674856, -79.398259);
        Marker lot14 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot14)
                .title("Parking Lot 14")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/green_23_marker.png")));

        LatLng latLngLot15 = new LatLng( 43.670676, -79.382509);
        Marker lot15 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot15)
                .title("Parking Lot 15")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/green_15_marker.png")));

        LatLng latLngLot16 = new LatLng( 43.669883, -79.382455);
        Marker lot16 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot16)
                .title("Parking Lot 16")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/orange_6_marker.png")));

        LatLng latLngLot17 = new LatLng( 43.659829, -79.380369);
        Marker lot17 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot17)
                .title("Parking Lot 17")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/red_2_marker.png")));

        LatLng latLngLot18 = new LatLng( 43.657764, -79.376211);
        Marker lot18 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot18)
                .title("Parking Lot 18")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/green_9_marker.png")));

        LatLng latLngLot19 = new LatLng( 43.658145, -79.385359);
        Marker lot19 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot19)
                .title("Parking Lot 19")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/green_39_marker.png")));

        LatLng latLngLot20 = new LatLng( 43.656254, -79.388198);
        Marker lot20 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot20)
                .title("Parking Lot 20")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/green_64_marker.png")));

        LatLng latLngLot21 = new LatLng( 43.654792, -79.389622);
        Marker lot21 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot21)
                .title("Lot 21")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/red_3_marker.png")));

        LatLng latLngLot22 = new LatLng( 43.654358, -79.388712);
        Marker lot22 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot22)
                .title("Parking Lot 22")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/green_31_marker.png")));

        LatLng latLngLot23 = new LatLng( 43.654010, -79.387221);
        Marker lot23 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot23)
                .title("Parking Lot 23")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/orange_7_marker.png")));

        LatLng latLngLot24 = new LatLng( 43.654806, -79.386678);
        Marker lot24 = mGoogleMap.addMarker(new MarkerOptions().position(latLngLot24)
                .title("Parking Lot 24")
                .icon(BitmapDescriptorFactory.fromAsset("newMarkers/orange_4_marker.png")));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        destroyInfoBox();
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Animates the map and displays the marker's banner
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        marker.showInfoWindow();

        // Prepares fragment managers
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        destroyInfoBox();

        // Build new fragment
        infoBox = LotInfoBoxFragment.newInstance(marker.getTitle(), 50, 39, 40,
                                                 marker.getPosition().latitude,
                                                 marker.getPosition().longitude);

        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.add(R.id.mainLayout, infoBox);
        fragmentTransaction.commit();

        // Tell API to ignore default marker functionality
        return true;
    }

    // Helper function that handles the removal of the info box fragment
    private void destroyInfoBox(){
        if(infoBox != null){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            fragmentTransaction.remove(infoBox);
            fragmentTransaction.commit();
            infoBox = null;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Leave this empty - needed for fragments used by this activity
    }

    public void moveToLocation(AbstractParkingLot p) {

        // Move to that location with transition animation
        double lat = p.getLat();
        double lng = p.getLng();
        LatLng coordinate = new LatLng(lat, lng);
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(
                coordinate, 15);
        mGoogleMap.animateCamera(camera);

        // Simulate a Infobox
        destroyInfoBox();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        destroyInfoBox();
        infoBox = LotInfoBoxFragment.newInstance(p.getName(), p.getCapacity(), p.getOccupancy(), p.getPricePerHour(), lat, lng);
        fragmentTransaction.add(R.id.mainLayout, infoBox);
        fragmentTransaction.commit();

    }

    public void closeLeftDrawer() {
        mDrawerLayout.closeDrawer(mLeftDrawerView);
    }



    public void initiazePopupWindow() {
        try {
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) MapsMarkerActivity.this.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_option,
                    (ViewGroup) findViewById(R.id.options));
            PopupWindow pw;
            pw = new PopupWindow(layout, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

            // Restore initial value of each object in popup
            // Restore the text
            // Initialize each listener for all the objects in the popup
            View seekbar = layout.findViewById(R.id.seekBarPrice);
            ((DiscreteSeekBar)seekbar).setProgress(oCost);
            TextView textview = (TextView) layout.findViewById(R.id.price_slider);
            initPopupObj(seekbar, 1, textview);
            textview.setText("Price ($" + ((DiscreteSeekBar)seekbar).getProgress() + "/h)");

            seekbar = layout.findViewById(R.id.seekBarDistance);
            ((DiscreteSeekBar)seekbar).setProgress(oDistance);
            textview = (TextView) layout.findViewById(R.id.distance_slider);
            initPopupObj(seekbar, 2, textview);
            textview.setText("Distance (" + ((DiscreteSeekBar)seekbar).getProgress() + "km)");

            seekbar = layout.findViewById(R.id.seekBarHeight);
            ((DiscreteSeekBar)seekbar).setProgress(oHeight);
            textview = (TextView) layout.findViewById(R.id.height_slider);
            initPopupObj(seekbar, 3, textview);
            textview.setText("Height Restriction (" + ((DiscreteSeekBar)seekbar).getProgress() + "m)");

            View button = layout.findViewById(R.id.switchDisabled);
            ((Switch)button).setChecked(oAccess);
            initPopupObj(button, 4, textview);

            // Setup popup window open and closing animation
            pw.setAnimationStyle(R.style.popupAnimation);

            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            // Dim background, must be after showing the pw
            dimBehind(pw);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initPopupObj(final View seekbar, int flag, final TextView textview) {

        if (flag == 1) {
            seekbar.setOnTouchListener(new RelativeLayout.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    DiscreteSeekBar ds = (DiscreteSeekBar) seekbar;
                    int progress = ds.getProgress();
                    oCost = progress;
                    textview.setText("Price ($" + progress + "/h)");
                    // Handle seekbar touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });
        } else if (flag == 2) {
            seekbar.setOnTouchListener(new RelativeLayout.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    DiscreteSeekBar ds = (DiscreteSeekBar) seekbar;
                    int progress = ds.getProgress();
                    oDistance = progress;
                    textview.setText("Distance (" + progress + "km)");
                    // Handle seekbar touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });
        } else if (flag == 3) {
            seekbar.setOnTouchListener(new RelativeLayout.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    DiscreteSeekBar ds = (DiscreteSeekBar) seekbar;
                    int progress = ds.getProgress();
                    oHeight = progress;
                    textview.setText("Height Restriction (" + progress + "m)");
                    // Handle seekbar touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });
        } else if (flag == 4) {
            Switch s = (Switch) seekbar;
            s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        oAccess = true;
                    } else {
                        oAccess = false;
                    }
                }
            });
        }



    }

    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.5f;
        wm.updateViewLayout(container, p);
    }


}


