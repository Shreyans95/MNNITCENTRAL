package com.example.android.mnnitcentral;

/**
 * Created by LENOVO on 3/30/2018.
 */

public class Map_Coordinates {
    private double lat,lon;
    float col;

    public float getCol() {
        return col;
    }

    private String name;
    public Map_Coordinates(){}

    public Map_Coordinates(double lat, double lon, String name,float col) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.col = col;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }
}
