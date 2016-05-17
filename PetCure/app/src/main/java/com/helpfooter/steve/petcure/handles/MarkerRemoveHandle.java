package com.helpfooter.steve.petcure.handles;

import com.tencent.mapsdk.raster.model.Marker;

import java.util.ArrayList;

/**
 * Created by steve on 2016/5/18.
 */
public class MarkerRemoveHandle extends AbstractHandles {
    ArrayList<Marker> markers=new ArrayList<Marker>();
    public void addMarker(Marker mk){
        markers.add(mk);
    }
    @Override
    public void callFunction() {
        for(Marker mk:markers){
            mk.remove();
        }
        markers.clear();
    }
}
