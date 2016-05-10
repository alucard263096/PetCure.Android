package com.helpfooter.steve.petcure.mgr;

import android.content.Context;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.tencent.lbssearch.TencentSearch;

/**
 * Created by scai on 2016/5/10.
 */
public class MapSearchMgr {
    static TencentSearch search;

    public static void InitSearch(Context ctx){
        search=new TencentSearch(ctx);
    }

    public static TencentSearch getSearch() {
        return search;
    }
}
