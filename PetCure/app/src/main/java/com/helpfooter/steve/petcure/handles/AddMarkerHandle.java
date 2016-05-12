package com.helpfooter.steve.petcure.handles;

import android.content.Context;
import android.graphics.Bitmap;

import com.helpfooter.steve.petcure.dataobjects.PosterObj;
import com.helpfooter.steve.petcure.mgr.MapMgr;
import com.helpfooter.steve.petcure.myviews.MapMarkerView;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

/**
 * Created by steve on 2016/5/12.
 */
public class AddMarkerHandle extends AbstractHandles {
    public void setObj(PosterObj obj) {
        this.obj = obj;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public AddMarkerHandle(MapMgr mapMgr, Context ctx) {
        this.mapMgr = mapMgr;
        this.ctx = ctx;
    }

    MapMgr mapMgr;
    Context ctx;
    PosterObj obj;
    Bitmap bitmap;

    @Override
    public void callFunction() {

        Marker mark = mapMgr.getTencentMap().addMarker(new MarkerOptions().draggable(false));
        mark.setVisible(true);
        updateMarkerByPoster(mark,obj);
        MapMarkerView mkv=new MapMarkerView(ctx);
        mkv.setImage(bitmap);
        mkv.setType(obj.getType());
        //mark.setVisible(false);
        mark.setIcon(BitmapDescriptorFactory.fromView(mkv));
        mapMgr.getPosterMarker().add(mark);

    }


    private void updateMarkerByPoster(Marker mk, PosterObj markerPoster) {
        mk.setPosition(new LatLng(markerPoster.getLat(),markerPoster.getLng()));
        mk.setTag(markerPoster);
    }
}
