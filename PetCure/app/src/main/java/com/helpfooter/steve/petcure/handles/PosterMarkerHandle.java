package com.helpfooter.steve.petcure.handles;

import android.content.Context;

import com.helpfooter.steve.petcure.dataobjects.PosterObj;
import com.helpfooter.steve.petcure.mgr.MapMgr;
import com.helpfooter.steve.petcure.myviews.MapMarkerView;
import com.tencent.mapsdk.raster.model.BitmapDescriptor;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import java.util.ArrayList;

/**
 * Created by steve on 2016/4/30.
 */
public class PosterMarkerHandle extends AbstractHandles {

    ArrayList<Marker> markers=null;
    Context ctx=null;
    TencentMap map;

    public PosterMarkerHandle(Context ctx,TencentMap map,ArrayList<Marker> markers) {
        this.ctx=ctx;
        this.markers = markers;
        this.map=map;
    }

    ArrayList<PosterObj> posterDOs=null;
    public void setPosterDOs(ArrayList<PosterObj> posterDOs) {
        this.posterDOs = posterDOs;
    }

    @Override
    public void callFunction() {
        for(Marker mk:markers){
            mk.remove();
        }
        markers.clear();
        for (PosterObj obj:posterDOs){
            Marker mark = map.addMarker(new MarkerOptions().draggable(false));
            mark.setVisible(true);
            updateMarkerByPoster(mark,obj);
            markers.add(mark);
        }
    }

    private void updateMarkerByPoster(Marker mk,PosterObj markerPoster) {
        MapMarkerView view=new MapMarkerView(this.ctx);
        view.setData(markerPoster);
        mk.setPosition(new LatLng(markerPoster.getLat(),markerPoster.getLng()));
        mk.setIcon(BitmapDescriptorFactory.fromView(view));
        mk.setTag(markerPoster);
    }
}
