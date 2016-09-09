package com.myworld.wenwo.utils;

import android.location.Location;

import java.text.DecimalFormat;

/**
 * Created by jianglei on 16/8/31.
 */

public class LocationUtil {
    public static double getDistanceByMeter(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }

    public static String getFormatedDistance(double lat1, double lon1, double lat2, double lon2) {
        double distance = getDistanceByMeter(lat1, lon1, lat2, lon2);
        if (distance >= 1000) {
            DecimalFormat format = new DecimalFormat("0.0");
            String formatedDistance = format.format(distance / 1000);
            return formatedDistance + "km";
        } else {
            DecimalFormat format = new DecimalFormat("#");
            String formatedDistance = format.format(distance);
            return formatedDistance + "m";
        }
    }
}
