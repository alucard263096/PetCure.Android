package com.helpfooter.steve.petcure.myviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by steve on 2016/4/30.
 */
public class FontAwesomeView extends TextView {
    public FontAwesomeView(Context context) {
        super(context);
        init(context);
    }

    public FontAwesomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontAwesomeView(Context context, AttributeSet attrs, int defSyle) {
        super(context, attrs, defSyle);
        init(context);
    }

    public void init(Context context) {
        try {
            Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
            //http://fortawesome.github.io/Font-Awesome/cheatsheet/
            setTypeface(font);
        }catch (Exception ex){
            Log.e("LoadFontError",ex.getMessage());
        }
    }
}
