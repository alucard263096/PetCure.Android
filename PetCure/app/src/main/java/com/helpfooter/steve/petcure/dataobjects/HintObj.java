package com.helpfooter.steve.petcure.dataobjects;

import android.database.Cursor;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by scai on 2016/5/20.
 */
public class HintObj extends AbstractObj {

    String needs,address,lat,lng,contact,created_date,created_id;
    String photo1;
    String photo2;
    String photo3;
    String photo4;
    String photo5;
    String photo6;
    String photo7;

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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
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

    public String getCreated_id() {
        return created_id;
    }

    public void setCreated_id(String created_id) {
        this.created_id = created_id;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public String getPhoto5() {
        return photo5;
    }

    public void setPhoto5(String photo5) {
        this.photo5 = photo5;
    }

    public String getPhoto6() {
        return photo6;
    }

    public void setPhoto6(String photo6) {
        this.photo6 = photo6;
    }

    public String getPhoto7() {
        return photo7;
    }

    public void setPhoto7(String photo7) {
        this.photo7 = photo7;
    }

    public String getPhoto8() {
        return photo8;
    }

    public void setPhoto8(String photo8) {
        this.photo8 = photo8;
    }

    String photo8;




    @Override
    public void parseCursor(Cursor cursor) {

    }

    @Override
    public void parseXmlDataTable(HashMap<String, String> lstRowValue) {

        id = Integer.valueOf(lstRowValue.get("id"));
        needs = lstRowValue.get("needs");
        photo1 = lstRowValue.get("photo1");
        photo2 = lstRowValue.get("photo2");
        photo3 = lstRowValue.get("photo3");
        photo4 = lstRowValue.get("photo4");
        photo5 = lstRowValue.get("photo5");
        photo6 = lstRowValue.get("photo6");
        photo7 = lstRowValue.get("photo7");
        photo8 = lstRowValue.get("photo8");
        address = lstRowValue.get("address");
        contact = lstRowValue.get("contact");
        created_date = lstRowValue.get("created_date");
        created_id = lstRowValue.get("created_id");
    }

    @Override
    public void parseJSon(JSONObject json) {

    }
}
