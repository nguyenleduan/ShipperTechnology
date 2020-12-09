package com.google.testapi_fpt.route.model;

public class RouteModel {
    private String Phone;
    public Double Latitude;
    public Double Longitude;

    public RouteModel() {
    }

    public RouteModel(String phone, Double latitude, Double longitude) {
        Phone = phone;
        Latitude = latitude;
        Longitude = longitude;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }
}
