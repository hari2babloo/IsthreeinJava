package com.example.hari.isthreeinjava.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class JobOrder {

    @SerializedName("jobid")
    @Expose
    private String jobid;
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("shift")
    @Expose
    private Object shift;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("category")
    @Expose
    private List<String> category = null;
    @SerializedName("price")
    @Expose
    private List<String> price = null;
    @SerializedName("quantity")
    @Expose
    private List<String> quantity = null;
    @SerializedName("subTotal")
    @Expose
    private List<String> subTotal = null;
    @SerializedName("GSTPercentage")
    @Expose
    private String gSTPercentage;
    @SerializedName("grandTotal")
    @Expose
    private String grandTotal;
    @SerializedName("jobStatus")
    @Expose
    private Object jobStatus;

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Object getShift() {
        return shift;
    }

    public void setShift(Object shift) {
        this.shift = shift;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public List<String> getPrice() {
        return price;
    }

    public void setPrice(List<String> price) {
        this.price = price;
    }

    public List<String> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<String> quantity) {
        this.quantity = quantity;
    }

    public List<String> getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(List<String> subTotal) {
        this.subTotal = subTotal;
    }

    public String getGSTPercentage() {
        return gSTPercentage;
    }

    public void setGSTPercentage(String gSTPercentage) {
        this.gSTPercentage = gSTPercentage;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Object getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Object jobStatus) {
        this.jobStatus = jobStatus;
    }



}
