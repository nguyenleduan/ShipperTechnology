package com.google.testapi_fpt.model;

public class ProfileModel {
    public int CountProgram;
    public String IDUSER;
    public String GROUP;
    public Double Latiude;
    public String LinkAvatar;
    public Double Longitude;
    public String NumberPhone;
    public String UserName;

    public ProfileModel() {
    }

    public ProfileModel(int countProgram, String IDUSER, String GROUP, Double latiude, String linkAvatar, Double longitude, String numberPhone, String userName) {
        CountProgram = countProgram;
        this.IDUSER = IDUSER;
        this.GROUP = GROUP;
        Latiude = latiude;
        LinkAvatar = linkAvatar;
        Longitude = longitude;
        NumberPhone = numberPhone;
        UserName = userName;
    }
}
