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
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.utsg.csc301.team21.models.HttpURLCon;
import com.utsg.csc301.team21.models.LotInfoBoxFragment;
import com.utsg.csc301.team21.models.ParkingLot;
import com.utsg.csc301.team21.models.Renderer;
import com.utsg.csc301.team21.models.SearchResultFragment;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private Marker searched = null;
    private ClusterManager<ParkingLot> mClusterManager;
    private SearchView searchView;

    // Used in displaying the lot info box
    private Fragment infoBox = null;

    // Persistence value in popup option
    private int oCost = 30;
    private int oDistance = 10;
    private int oHeight = 2;
    private boolean oAccess = true;

    // Holds the system start time
    long startTime = 0;

    // Holds the interval (seconds) in which the map refreshes
    int interval = 15;

    // Result parkinglot from HTTPS call
    ArrayList<AbstractParkingLot> lots = new ArrayList<AbstractParkingLot>();

    // Timer used to refresh the map with new data
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);

            if(seconds >= interval){
                startTime = System.currentTimeMillis();

                // Call controller code
            }

        }
    };

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

        // Start the timer
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
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
        // Cluster Markers
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

        addItems();
    }



    public void addItems(){
        // Initial focus on UofT TODO:Set to current location
        LatLng uoft = new LatLng(43.662892, -79.395656);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uoft, 14));

        Random r = new Random();

        // ParkingLot(int id, int capacity, int occupancy, String name,
        // String address, double lat, double lng, Double pricePerHour)

        // List of Parking Lots
        ParkingLot pl01 = new ParkingLot(1, 100, r.nextInt(100), "Parking Lot 1", "", 43.666705, -79.405147, r.nextDouble()*10);
        ParkingLot pl02 = new ParkingLot(2, 100, r.nextInt(100), "Parking Lot 2", "", 43.665385, -79.403477, r.nextDouble()*10);
        ParkingLot pl03 = new ParkingLot(3, 100, r.nextInt(100), "Parking Lot 3", "", 43.657563, -79.403436, r.nextDouble()*10);
        ParkingLot pl04 = new ParkingLot(4, 100, r.nextInt(100), "Parking Lot 4", "", 43.655946, -79.408577, r.nextDouble()*10);
        ParkingLot pl05 = new ParkingLot(5, 100, r.nextInt(100), "Parking Lot 5", "", 43.652175, -79.405963, r.nextDouble()*10);

        ParkingLot pl06 = new ParkingLot(6, 100, r.nextInt(100), "Parking Lot 6", "", 43.652586, -79.398445, r.nextDouble()*10);
        ParkingLot pl07 = new ParkingLot(7, 100, r.nextInt(100), "Parking Lot 7", "", 43.659353, -79.389422, r.nextDouble()*10);
        ParkingLot pl08 = new ParkingLot(8, 100, r.nextInt(100), "Parking Lot 8", "", 43.659891, -79.388625, r.nextDouble()*10);
        ParkingLot pl09 = new ParkingLot(9, 100, r.nextInt(100), "Parking Lot 9", "", 43.667672, -79.389450, r.nextDouble()*10);
        ParkingLot pl10 = new ParkingLot(10, 100, r.nextInt(100), "Parking Lot 10", "", 43.669112, -79.388623, r.nextDouble()*10);

        ParkingLot pl11 = new ParkingLot(11, 100, r.nextInt(100), "Parking Lot 11", "", 43.669710, -79.391218, r.nextDouble()*10);
        ParkingLot pl12 = new ParkingLot(12, 100, r.nextInt(100), "Parking Lot 12", "", 43.669447, -79.392248, r.nextDouble()*10);
        ParkingLot pl13 = new ParkingLot(13, 100, r.nextInt(100), "Parking Lot 13", "", 43.671654, -79.394603, r.nextDouble()*10);
        ParkingLot pl14 = new ParkingLot(14, 100, r.nextInt(100), "Parking Lot 14", "", 43.674856, -79.398259, r.nextDouble()*10);
        ParkingLot pl15 = new ParkingLot(15, 100, r.nextInt(100), "Parking Lot 15", "", 43.670676, -79.382509, r.nextDouble()*10);

        ParkingLot pl16 = new ParkingLot(16, 100, r.nextInt(100), "Parking Lot 16", "", 43.669883, -79.382455, r.nextDouble()*10);
        ParkingLot pl17 = new ParkingLot(17, 100, r.nextInt(100), "Parking Lot 17", "", 43.659829, -79.380369, r.nextDouble()*10);
        ParkingLot pl18 = new ParkingLot(18, 100, r.nextInt(100), "Parking Lot 18", "", 43.657764, -79.376211, r.nextDouble()*10);
        ParkingLot pl19 = new ParkingLot(19, 100, r.nextInt(100), "Parking Lot 19", "", 43.658145, -79.385359, r.nextDouble()*10);
        ParkingLot pl20 = new ParkingLot(20, 100, r.nextInt(100), "Parking Lot 20", "", 43.656254, -79.388198, r.nextDouble()*10);

        ParkingLot pl21 = new ParkingLot(21, 100, r.nextInt(100), "Parking Lot 21", "", 43.654792, -79.389622, r.nextDouble()*10);
        ParkingLot pl22 = new ParkingLot(22, 100, r.nextInt(100), "Parking Lot 22", "", 43.654358, -79.388712, r.nextDouble()*10);
        ParkingLot pl23 = new ParkingLot(23, 100, r.nextInt(100), "Parking Lot 23", "", 43.654010, -79.387221, r.nextDouble()*10);
        ParkingLot pl24 = new ParkingLot(24, 100, r.nextInt(100), "Parking Lot 24", "", 43.654806, -79.386678, r.nextDouble()*10);

        mClusterManager.addItem(pl01);
        mClusterManager.addItem(pl02);
        mClusterManager.addItem(pl03);
        mClusterManager.addItem(pl04);
        mClusterManager.addItem(pl05);

        mClusterManager.addItem(pl06);
        mClusterManager.addItem(pl07);
        mClusterManager.addItem(pl08);
        mClusterManager.addItem(pl09);
        mClusterManager.addItem(pl10);

        mClusterManager.addItem(pl11);
        mClusterManager.addItem(pl12);
        mClusterManager.addItem(pl13);
        mClusterManager.addItem(pl14);
        mClusterManager.addItem(pl15);

        mClusterManager.addItem(pl16);
        mClusterManager.addItem(pl17);
        mClusterManager.addItem(pl18);
        mClusterManager.addItem(pl19);
        mClusterManager.addItem(pl20);

        mClusterManager.addItem(pl21);
        mClusterManager.addItem(pl22);
        mClusterManager.addItem(pl23);
        mClusterManager.addItem(pl24);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        destroyInfoBox();
        getSupportActionBar().collapseActionView();
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Animates the map and displays the marker's banner
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        marker.showInfoWindow();

        // Prepares fragment managers only on non cluster marker clicks
        if(marker.getTitle() != null){
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
        }

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



    // DESCRIPTION!!!!!! READDDDD

    // lat, lng is either the current loaction or the current search location,
    // this function gets all the parklots from server, filters it with the current filters
    // then update on itself the drawer search view, AND it returns a list of filters
    // List of filtered parklots.
    // USE THE parklots returned to populate map markers, and other stuff that needs to be updated!!!!
    private List<AbstractParkingLot> getLotsFromServer(double lat, double lng) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://lotspot-team21.herokuapp.com/api/lots";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            // Make ParkingLot ArrayList
                            JSONObject jObj = new JSONObject("{'lots':" + response +"}");
                            JSONArray arr = jObj.getJSONArray("lots");

                            //Log.d("MyActivity", "test: " + jObj.toString());

                            // Clear list
                            lots.clear();

                            for (int i = 0; i < arr.length(); i++){
                                JSONObject objj = arr.getJSONObject(i);
                                //(int id, int capacity, int occupancy, String name, String address, double lat, double lng, Double pricePerHour)
                                AbstractParkingLot p = new ParkingLot(i, objj.getInt("capacity"),
                                        objj.getInt("occupancy"), objj.getString("name"), objj.getString("address"),
                                        objj.getDouble("lat"), objj.getDouble("lng"), objj.getDouble("price"));
                                lots.add(p);

                            }


                        } catch (Exception e) {
                            Log.d("MyActivity", "test2: " + e.toString());
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("MyActivity", "That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


        //ilterResult(List<AbstractParkingLot> parkingLots, int cost, int dist, int height,
        //boolean access, double curr_lat, double curr_lng)

        return SearchResultFragment.filterResult(lots, oCost, oDistance, oHeight, oAccess, lat, lng);

    }
}


