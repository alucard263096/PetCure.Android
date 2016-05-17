package com.helpfooter.steve.petcure;

import com.helpfooter.steve.petcure.loader.InvolvePosterLoader;
import com.helpfooter.steve.petcure.mgr.MemberMgr;

import java.util.HashMap;

/**
 * Created by steve on 2016/5/17.
 */
public class InvolveListActivity extends PosterListActivity {
    public InvolveListActivity() {
        this.needLogin=true;
        arr2=new String[]{"最近参与","参与最久","最新发布","发布最久"};
        strings=new String[]{"全部救助","最近参与"};
    }

    @Override
    public void RefreshData() {
        swipe_container.setRefreshing(true);
        HashMap<String, String> hmLocation = new HashMap<String, String>();

        if(MemberMgr.Member!=null){
            hmLocation.put("member_id",String.valueOf( MemberMgr.Member.getId()));
        }

        hmLocation.put("page", String.valueOf(page));
        hmLocation.put("count", "20");
        hmLocation.put("type", String.valueOf(type_index));
        hmLocation.put("order",String.valueOf(order_index));
        InvolvePosterLoader posterLoader = new InvolvePosterLoader(this);
        posterLoader.setUrlDynamicParam(hmLocation);
        posterLoader.setCallBack(this);
        posterLoader.start();
    }
}
