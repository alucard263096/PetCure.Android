package com.helpfooter.steve.petcure.handles;

import android.content.Context;

import com.helpfooter.steve.petcure.dataobjects.PosterObj;
import com.helpfooter.steve.petcure.myviews.MapMarkerView;
import com.tencent.mapsdk.raster.model.BitmapDescriptor;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;

import java.util.ArrayList;

/**
 * Created by steve on 2016/4/30.
 */
public class PosterMarkerHandle extends AbstractHandles {

    ArrayList<Marker> markers=null;
    Context ctx=null;

    public PosterMarkerHandle(Context ctx,ArrayList<Marker> markers) {
        this.ctx=ctx;
        this.markers = markers;
    }

    ArrayList<PosterObj> posterDOs=null;
    public void setPosterDOs(ArrayList<PosterObj> posterDOs) {
        this.posterDOs = posterDOs;
    }

    @Override
    public void callFunction() {
        for(int i=0;i<100;i++){
            Marker marker=markers.get(i);
            try {
                if(posterDOs!=null&&i<posterDOs.size()){
                    updateMarkerByPoster(marker,posterDOs.get(i));
                    marker.setVisible(true);
                }else {
                    marker.setVisible(false);
                    marker.setTag(null);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
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
