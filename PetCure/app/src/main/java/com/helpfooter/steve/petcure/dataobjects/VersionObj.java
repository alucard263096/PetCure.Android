package com.helpfooter.steve.petcure.dataobjects;

import android.database.Cursor;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by steve on 2016/4/30.
 */
public class VersionObj extends AbstractDO {
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String version;
    String url;

    @Override
    public void parseCursor(Cursor cursor) {

    }


    @Override
    public void parseXmlDataTable(HashMap<String, String> lstRowValue) {

        this.url=lstRowValue.get("url");
        this.version=lstRowValue.get("version");
    }

    @Override
    public void parseJSon(JSONObject json) {
    }
}
