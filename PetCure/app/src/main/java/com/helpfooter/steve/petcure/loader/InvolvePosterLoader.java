package com.helpfooter.steve.petcure.loader;

import android.content.Context;

import com.helpfooter.steve.petcure.common.StaticVar;

/**
 * Created by steve on 2016/5/17.
 */
public class InvolvePosterLoader extends PosterLoader {
    public InvolvePosterLoader(Context ctx) {
        super(ctx);
        this.url= StaticVar.APIUrl.InvolveList;
    }
}
