package com.helpfooter.steve.petcure.myviews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.helpfooter.steve.petcure.R;
import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.PosterObj;
import com.helpfooter.steve.petcure.utils.ImageUtil;
import com.helpfooter.steve.petcure.utils.Util;
import com.tencent.mapsdk.raster.model.Marker;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by steve on 2016/5/9.
 */
public class MapMarkerView  extends LinearLayout {
    public MapMarkerView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.map_marker,this,true);
    }

    public MapMarkerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.map_marker,this,true);
    }

    public MapMarkerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.map_marker,this,true);
    }

    public void setData(PosterObj obj) {
//        Uri uri = Uri.parse(StaticVar.PetImageUrl+obj.getPhoto());
//        Log.i("MapMarker",StaticVar.PetImageUrl+obj.getPhoto());
//        SimpleDraweeView imageView= ((SimpleDraweeView) findViewById(R.id.image_view));
//        DraweeController dr= Fresco.newDraweeControllerBuilder().setUri(uri).setAutoPlayAnimations(true).build();
//        imageView.setController(dr);
        ImageView imageView = ((ImageView) findViewById(R.id.image_view));
//        Bitmap bitmap = ImageUtil.GetHttpBitmap(StaticVar.PetImageUrl + obj.getPhoto());
//        imageView.setImageBitmap(bitmap);

        class DisplayImage extends AsyncTask<String,Void,Bitmap> {
            private ImageView imageView;

            public DisplayImage(ImageView imageView) {
                this.imageView = imageView;
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                String url = params[0];
                Bitmap bitmap = null;
                try {
                    InputStream is = new URL(url).openStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }
            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                imageView.setImageBitmap(result);
            }
        }
        new DisplayImage(imageView).execute(StaticVar.PetImageUrl + obj.getPhoto());

    }

}
