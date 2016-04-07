package com.tilatina.guardcheck.Utillities;

/**
 * Created by jaime on 7/04/16.
 */
public class ServiceStatus {
    private int id, entitiesId;
    private String name, statusDate, nextTo;

    public ServiceStatus(){}

    public ServiceStatus(int id, int entitiesId, String name, String statusDate, String nextTo) {
        this.id = id;
        this.entitiesId = entitiesId;
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

    public int getEntitiesId() {
        return entitiesId;
    }

    public void setEntitiesId(int entitiesId) {
        this.entitiesId = entitiesId;
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
