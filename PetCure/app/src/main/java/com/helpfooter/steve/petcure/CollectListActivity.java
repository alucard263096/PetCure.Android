package com.helpfooter.steve.petcure;

import com.helpfooter.steve.petcure.loader.CollectPosterLoader;
import com.helpfooter.steve.petcure.loader.FollowPosterLoader;
import com.helpfooter.steve.petcure.mgr.MemberMgr;

import java.util.HashMap;

/**
 * Created by steve on 2016/5/17.
 */
public class CollectListActivity extends PosterListActivity {
    public CollectListActivity() {
        this.needLogin=true;
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
        CollectPosterLoader posterLoader = new CollectPosterLoader(this);
        posterLoader.setUrlDynamicParam(hmLocation);
        posterLoader.setCallBack(this);
        posterLoader.start();
    }
}
