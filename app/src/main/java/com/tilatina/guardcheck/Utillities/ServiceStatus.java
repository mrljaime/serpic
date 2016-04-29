package com.tilatina.guardcheck.Utillities;

import android.app.Service;

/**
 * Created by jaime on 7/04/16.
 */
public class ServiceStatus {
    private int id, canModify;
    private double lat;
    private double lng;
    private String name, statusColor, nextTo;

    public ServiceStatus(){}

    public ServiceStatus(int id, String name, String statusColor, String nextTo, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.statusColor = statusColor;
        this.nextTo = nextTo;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public ServiceStatus setId(int id) {
        this.id = id;

        return this;
    }

    public String getName() {
        return name;
    }

    public ServiceStatus setName(String name) {
        this.name = name;

        return this;
    }

    public String getstatusColor() {
        return statusColor;
    }

    public ServiceStatus setstatusColor(String statusColor) {
        this.statusColor = statusColor;

        return this;
    }

    public String getNextTo() {
        return nextTo;
    }

    public ServiceStatus setNextTo(String nextTo) {
        this.nextTo = nextTo;

        return this;
    }

    public double getLat() {
        return lat;
    }

    public ServiceStatus setLat(double lat) {
        this.lat = lat;

        return this;
    }

    public ServiceStatus setLng(double lng) {
        this.lng = lng;

        return this;
    }

    public double getLng() {
        return lng;
    }

    public ServiceStatus setCanModify(int canModify) {
        this.canModify = canModify;

        return this;
    }

    public int getCanModify() {
        return canModify;
    }

}
