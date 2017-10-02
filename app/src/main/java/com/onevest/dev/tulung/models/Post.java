package com.onevest.dev.tulung.models;

/**
 * Created by Shoddiq Jati Premono on 16/09/2017.
 */

public class Post {
    private long id;
    private String uuid;
    private String name;
    private String category;
    private String desc;
    private String people;
    private String status;
    private String address;
    private String latitude;
    private String longitude;

    public Post() {
    }

    public Post(String uuid, String name, String category, String desc, String people, String status, String address, String latitude, String longitude) {
        this.uuid = uuid;
        this.name = name;
        this.category = category;
        this.desc = desc;
        this.people = people;
        this.status = status;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
