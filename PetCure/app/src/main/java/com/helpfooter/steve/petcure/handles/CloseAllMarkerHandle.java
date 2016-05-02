package com.helpfooter.steve.petcure.handles;

import com.tencent.mapsdk.raster.model.Marker;

import java.util.ArrayList;

/**
 * Created by steve on 2016/5/2.
 */
public class CloseAllMarkerHandle extends AbstractHandles {
    ArrayList<Marker> markers=null;

    public void setNotcloseMarker(Marker notcloseMarker) {
        this.notcloseMarker = notcloseMarker;
    }

    Marker notcloseMarker=null;

    public CloseAllMarkerHandle(ArrayList<Marker> markers) {
        this.markers = markers;
    }

    @Override
    public void callFunction() {
        for(Marker marker:markers){
            if(marker.getTag()!=notcloseMarker.getTag()){
                marker.hideInfoWindow();
            }
        }
        //notcloseMarker.showInfoWindow();
    }
}
