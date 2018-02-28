package com.example.hari.isthreeinjava.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hari on 28/2/18.
 */

public class Sigin {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userType")
    @Expose
    private String userType;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("pic")
    @Expose
    private Object pic;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("landMark")
    @Expose
    private String landMark;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("longi")
    @Expose
    private String longi;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("AltPhone")
    @Expose
    private String altPhone;
    @SerializedName("adharno")
    @Expose
    private Object adharno;
    @SerializedName("licenceno")
    @Expose
    private Object licenceno;
    @SerializedName("adharproof")
    @Expose
    private Object adharproof;
    @SerializedName("licenceproof")
    @Expose
    private Object licenceproof;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getPic() {
        return pic;
    }

    public void setPic(Object pic) {
        this.pic = pic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAltPhone() {
        return altPhone;
    }

    public void setAltPhone(String altPhone) {
        this.altPhone = altPhone;
    }

    public Object getAdharno() {
        return adharno;
    }

    public void setAdharno(Object adharno) {
        this.adharno = adharno;
    }

    public Object getLicenceno() {
        return licenceno;
    }

    public void setLicenceno(Object licenceno) {
        this.licenceno = licenceno;
    }

    public Object getAdharproof() {
        return adharproof;
    }

    public void setAdharproof(Object adharproof) {
        this.adharproof = adharproof;
    }

    public Object getLicenceproof() {
        return licenceproof;
    }

    public void setLicenceproof(Object licenceproof) {
        this.licenceproof = licenceproof;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
