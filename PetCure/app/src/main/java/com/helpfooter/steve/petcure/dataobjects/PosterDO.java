package com.helpfooter.steve.petcure.dataobjects;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by steve on 2016/4/29.
 */
public class PosterDO extends AbstractDO {
    String pet_type;
    String pet_size;
    String pet_photo;
    String pet_detail;
    String rescue_type;
    String rescue_level;
    String rescue_address;
    String rescue_detail;
    String rescue_need;
    String contact_name;
    String contact_mobile;
    String contact_qq;
    String contact_wechat;
    Double rescue_lat;
    Double rescue_lng;
    String verify;
    String status;
    String created_date;
    String updated_date;


    public String getPet_type() {
        return pet_type;
    }

    public void setPet_type(String pet_type) {
        this.pet_type = pet_type;
    }

    public String getPet_size() {
        return pet_size;
    }

    public void setPet_size(String pet_size) {
        this.pet_size = pet_size;
    }

    public String getPet_photo() {
        return pet_photo;
    }

    public void setPet_photo(String pet_photo) {
        this.pet_photo = pet_photo;
    }

    public String getPet_detail() {
        return pet_detail;
    }

    public void setPet_detail(String pet_detail) {
        this.pet_detail = pet_detail;
    }

    public String getRescue_type() {
        return rescue_type;
    }

    public void setRescue_type(String rescue_type) {
        this.rescue_type = rescue_type;
    }

    public String getRescue_level() {
        return rescue_level;
    }

    public void setRescue_level(String rescue_level) {
        this.rescue_level = rescue_level;
    }

    public String getRescue_address() {
        return rescue_address;
    }

    public void setRescue_address(String rescue_address) {
        this.rescue_address = rescue_address;
    }

    public String getRescue_detail() {
        return rescue_detail;
    }

    public void setRescue_detail(String rescue_detail) {
        this.rescue_detail = rescue_detail;
    }

    public String getRescue_need() {
        return rescue_need;
    }

    public void setRescue_need(String rescue_need) {
        this.rescue_need = rescue_need;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }

    public String getContact_qq() {
        return contact_qq;
    }

    public void setContact_qq(String contact_qq) {
        this.contact_qq = contact_qq;
    }

    public String getContact_wechat() {
        return contact_wechat;
    }

    public void setContact_wechat(String contact_wechat) {
        this.contact_wechat = contact_wechat;
    }

    public Double getRescue_lat() {
        return rescue_lat;
    }

    public void setRescue_lat(Double rescue_lat) {
        this.rescue_lat = rescue_lat;
    }

    public Double getRescue_lng() {
        return rescue_lng;
    }

    public void setRescue_lng(Double rescue_lng) {
        this.rescue_lng = rescue_lng;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    @Override
    public void parseCursor(Cursor cursor) {

    }

    @Override
    public void parseXmlDataTable(HashMap<String, String> lstRowValue) {

    }

    public void parseJSon(JSONObject json) {
        try {
            id = json.getInt("id");
            pet_type = json.getString("pet_type");
            pet_size = json.getString("pet_size");
            pet_photo = json.getString("pet_photo");
            pet_detail = json.getString("pet_detail");
            rescue_type = json.getString("rescue_type");
            rescue_level = json.getString("rescue_level");
            rescue_address = json.getString("rescue_address");
            rescue_detail = json.getString("rescue_detail");
            rescue_need = json.getString("rescue_need");
            contact_name = json.getString("contact_name");
            contact_mobile = json.getString("contact_mobile");
            contact_qq = json.getString("contact_qq");
            contact_wechat = json.getString("contact_wechat");
            rescue_lat = json.getDouble("rescue_lat");
            rescue_lng = json.getDouble("rescue_lng");
            verify = json.getString("verify");
            status = json.getString("status");
            created_date = json.getString("created_date");
            updated_date = json.getString("updated_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
