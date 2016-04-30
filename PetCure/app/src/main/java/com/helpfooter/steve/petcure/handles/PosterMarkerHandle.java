package com.helpfooter.steve.petcure.handles;

import com.helpfooter.steve.petcure.dataobjects.PosterDO;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;

import java.util.ArrayList;

/**
 * Created by steve on 2016/4/30.
 */
public class PosterMarkerHandle extends AbstractHandles {

    ArrayList<Marker> markers=null;

    public PosterMarkerHandle(ArrayList<Marker> markers) {
        this.markers = markers;
    }

    ArrayList<PosterDO> posterDOs=null;
    public void setPosterDOs(ArrayList<PosterDO> posterDOs) {
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
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void updateMarkerByPoster(Marker mk,PosterDO markerPoster) {
        mk.setPosition(new LatLng(markerPoster.getRescue_lat(),markerPoster.getRescue_lng()));
    }
}
