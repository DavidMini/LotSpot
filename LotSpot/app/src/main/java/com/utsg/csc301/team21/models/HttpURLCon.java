package com.utsg.csc301.team21.models;

/**
 * Created by bogdan on 04/04/17.
 */


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpURLCon {

    public static void main(String[] args) throws Exception {

        HttpURLCon http = new HttpURLCon();

        // Sending get request
        http.sendingGetRequest();

    }

    // HTTP GET request
    private void sendingGetRequest() throws Exception {

        String urlString = "https://lotspot-team21.herokuapp.com/api/lots";

        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // By default it is GET request
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("Content-Type", "application/json");

        int responseCode = con.getResponseCode();
        //System.out.println("Sending get request : "+ url);
        //System.out.println("Response code : "+ responseCode);

        // Reading response from input Stream
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();


        //TODO: use response.toString() and make AbstractParkingLot objects
        //printing result from response
        //System.out.println(response.toString());

    }
}


