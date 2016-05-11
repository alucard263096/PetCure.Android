package com.helpfooter.steve.petcure.dataobjects;

import android.database.Cursor;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by scai on 2016/5/11.
 */
public class PosterPhotoObj extends AbstractObj {

    int record_id;
    String type;
    String needs;
    String address;
    String contact;
    String created_date;
    String photo;

    @Override
    public void parseCursor(Cursor cursor) {

    }

    public int getRecord_id() {
        return record_id;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public void parseXmlDataTable(HashMap<String, String> lstRowValue) {
        id = Integer.valueOf(lstRowValue.get("photo_id"));
        record_id = Integer.valueOf(lstRowValue.get("record_id"));
        type = lstRowValue.get("type");
        needs = lstRowValue.get("needs");
        photo = lstRowValue.get("photo");
        address = lstRowValue.get("address");
        contact = lstRowValue.get("contact");
        created_date = lstRowValue.get("created_date");

    }

    @Override
    public void parseJSon(JSONObject json) {

    }
}
