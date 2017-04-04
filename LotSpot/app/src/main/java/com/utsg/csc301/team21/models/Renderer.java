package com.utsg.csc301.team21.models;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Renderer extends DefaultClusterRenderer<ParkingLot> {
    private Context context;

    public Renderer(Context context, GoogleMap map, ClusterManager<ParkingLot> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    protected void onBeforeClusterItemRendered(ParkingLot pl, MarkerOptions markerOptions) {
        int occupancy = pl.getOccupancy();
        int capacity = pl.getCapacity();
        int vacancy = capacity - occupancy;
        String vacancyString = Integer.toString(vacancy);

        double r = (1.0 * occupancy) / (1.0 * capacity);
        String color, assetPath;

        if (r >= 0.75) { color = "red"; }
        else if (r >= 0.5 && r < 0.75) { color = "orange"; }
        else { color = "green"; }

        assetPath = "solidAndFullMarkers/" + color + "_solid_marker.png";
        //
        Bitmap bitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;

        String filename = new File(assetPath).getAbsolutePath();
        bitmap = BitmapFactory.decodeFile(filename, options);

        if (bitmap == null) {
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            bitmap = Bitmap.createBitmap(200, 50, conf);
        }

        Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);

        Paint paint = new Paint();

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);

        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(vacancyString, 100, 50, paint);

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        markerOptions.icon(BitmapDescriptorFactory.fromAsset(assetPath));
    }
}
