package com.google.testapi_fpt.model;

public class ProgramModel {
    public String Address;
    public String IDUSER;
    public Double Latitude;
    public String LinkAvatar;
    public Double Longitude;
    public String Name;
    public String Note;
    public String NumberPhone;

    public ProgramModel() {
    }

    public ProgramModel(String address, String IDUSER, Double latitude, String linkAvatar, Double longitude, String name, String note, String numberPhone) {
        Address = address;
        this.IDUSER = IDUSER;
        Latitude = latitude;
        LinkAvatar = linkAvatar;
        Longitude = longitude;
        Name = name;
        Note = note;
        NumberPhone = numberPhone;
    }
 
}
