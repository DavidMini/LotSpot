package com.utsg.csc301.team21.models;

/**
 * Created by bogdan on 04/04/17.
 */


import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;


public class HttpURLCon {

    public static void httpLotRequest()  {
        Log.v("MyActivity", "crap1");

        HttpURLCon http = new HttpURLCon();

        try {
            // Sending get request
            http.sendingGetRequest();
        }
        catch (Exception e) {
            // Nothing
        }



    }

    // HTTP GET request
    private void sendingGetRequest() throws Exception {

        Log.v("MyActivity", "crap1.25");
        String urlString = "https://lotspot-team21.herokuapp.com/api/lots";

        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // By default it is GET request
        con.setRequestMethod("GET");

        Log.v("MyActivity", "crap1.251");
        //add request header
        con.setRequestProperty("Content-Type", "application/json");

        //System.out.println("Sending get request : "+ url);
        //System.out.println("Response code : "+ responseCode);

        // Reading response from input Stream
        con.setDoOutput(true);

        Log.v("MyActivity", "crap1.252-a");
        //con.connect();



        Log.v("MyActivity", "crap1.252");
        InputStream innn = con.getInputStream();




        Log.v("MyActivity", "crap1.2521");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(innn));
        String output;
        Log.v("MyActivity", "crap1.253");
        StringBuffer response = new StringBuffer();

        Log.v("MyActivity", "crap1.254");
        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        Log.v("MyActivity", "crap1.5");

        //TODO: use response.toString() and make AbstractParkingLot objects
        //printing result from response
        //System.out.println(response.toString());

        JSONObject obj = new JSONObject(response.toString());

        JSONArray arr = obj.getJSONArray("");
        String name = arr.getJSONObject(0).getString("name");
        Log.d("CREATION", name);
        Log.v("MyActivity", "crap2");
//        for (int i = 0; i < arr.length(); i++){
//            String name = arr.getJSONObject(i).getString("name");
//            System.out.println(name);
//        }
    }
}


