package com.helpfooter.steve.petcure.dataobjects;

import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by steve on 2016/4/29.
 */
public class PosterObj extends AbstractObj {
    String type;
    String needs;
    String photo;
    String address;
    double lat,lng;
    String contact;
    String created_date;
    int created_id;
    String updated_date;
    int updated_id;

    public int getFollowcount() {
        return followcount;
    }

    public void setFollowcount(int followcount) {
        this.followcount = followcount;
    }

    public int getCollectcount() {
        return collectcount;
    }

    public void setCollectcount(int collectcount) {
        this.collectcount = collectcount;
    }

    int followcount=0,collectcount=0;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNeeds() {
        return needs;
    }

    public void setNeeds(String needs) {
        this.needs = needs;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public int getCreated_id() {
        return created_id;
    }

    public void setCreated_id(int created_id) {
        this.created_id = created_id;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public int getUpdated_id() {
        return updated_id;
    }

    public void setUpdated_id(int updated_id) {
        this.updated_id = updated_id;
    }

    @Override
    public void parseCursor(Cursor cursor) {

    }

    @Override
    public void parseXmlDataTable(HashMap<String, String> lstRowValue) {
        id=Integer.valueOf(lstRowValue.get("id"));
        type = lstRowValue.get("type");
        needs = lstRowValue.get("needs");
        photo = lstRowValue.get("photo");
        address = lstRowValue.get("address");
        lat = Double.valueOf(lstRowValue.get("lat"));
        lng = Double.valueOf(lstRowValue.get("lng"));
        contact = lstRowValue.get("contact");
        created_date = lstRowValue.get("created_date");
        created_id = Integer.valueOf(lstRowValue.get("created_id"));
        updated_date = lstRowValue.get("updated_date");
        updated_id = Integer.valueOf(lstRowValue.get("updated_id"));
        followcount = Integer.valueOf(lstRowValue.get("followcount"));
        collectcount = Integer.valueOf(lstRowValue.get("collectcount"));
    }

    @Override
    public void parseJSon(JSONObject json) {

    }
}
