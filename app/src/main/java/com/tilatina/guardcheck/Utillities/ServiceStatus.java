package com.tilatina.guardcheck.Utillities;

/**
 * Created by jaime on 7/04/16.
 */
public class ServiceStatus {
    private int id;
    private String name, statusDate, nextTo;

    public ServiceStatus(){}

    public ServiceStatus(String name, String statusDate, String nextTo) {
        this.name = name;
        this.statusDate = statusDate;
        this.nextTo = nextTo;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getNextTo() {
        return nextTo;
    }

    public void setNextTo(String nextTo) {
        this.nextTo = nextTo;
    }

}
