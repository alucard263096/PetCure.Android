package com.helpfooter.steve.petcure;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.PosterObj;
import com.helpfooter.steve.petcure.dataobjects.PosterPhotoObj;
import com.helpfooter.steve.petcure.dataobjects.ResultObj;
import com.helpfooter.steve.petcure.handles.AbstractHandles;
import com.helpfooter.steve.petcure.interfaces.IWebLoaderCallBack;
import com.helpfooter.steve.petcure.loader.PosterPhotoLoader;
import com.helpfooter.steve.petcure.loader.ResultLoader;
import com.helpfooter.steve.petcure.loader.WebXmlLoader;
import com.helpfooter.steve.petcure.mgr.ActivityMgr;
import com.helpfooter.steve.petcure.mgr.ImageLoaderMgr;
import com.helpfooter.steve.petcure.mgr.MemberMgr;
import com.helpfooter.steve.petcure.mgr.WechatMgr;
import com.helpfooter.steve.petcure.utils.ImageUtil;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.util.ArrayList;
import java.util.HashMap;

public class PosterShowerActivity extends AppCompatActivity implements IWebLoaderCallBack {


    public static class RequestCode{
        public static int LoginActivity=1;
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    String poster_id;

    ArrayList<PosterPhotoObj> photos=new ArrayList<PosterPhotoObj>();
    View progress;
    PosterInfoHandle posterInfoHandle;
    class PosterInfoHandle extends AbstractHandles{

        boolean collect=false;
        boolean follow=false;


        public void setCollect(boolean collect) {
            this.collect = collect;
        }


        public void setFollow(boolean follow) {
            this.follow = follow;
        }

        @Override
        public void callFunction() {
            menuCollect.setTitle(collect?"取消收藏":"收藏");
            menuFollow.setTitle(follow?"取消关注":"关注");
        }
    }

    class FollowHandle extends AbstractHandles{

        boolean ret;

        public void setRet(boolean ret) {
            this.ret = ret;
        }

        @Override
        public void callFunction() {
            Toast.makeText(PosterShowerActivity.this,menuFollow.getTitle()+(ret?"成功":"失败"),Toast.LENGTH_LONG).show();
            if(ret) {
                if (menuFollow.getTitle().toString().contains("取消")) {
                    menuFollow.setTitle("关注");
                } else {
                    menuFollow.setTitle("取消关注");
                }
            }
            menuFollow.setEnabled(true);
        }
    }
    FollowHandle followHandle;


    class CollectHandle extends AbstractHandles{

        boolean ret;

        public void setRet(boolean ret) {
            this.ret = ret;
        }

        @Override
        public void callFunction() {
            Toast.makeText(PosterShowerActivity.this,menuCollect.getTitle()+(ret?"成功":"失败"),Toast.LENGTH_LONG).show();
            if(ret) {
                if (menuCollect.getTitle().toString().contains("取消")) {
                    menuCollect.setTitle("关注");
                } else {
                    menuCollect.setTitle("取消关注");
                }
            }
            menuCollect.setEnabled(true);
        }
    }
    CollectHandle collectHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_shower);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        progress = (View) findViewById(R.id.progress);

        poster_id = getIntent().getStringExtra("poster_id");
        PosterPhotoLoader loader = new PosterPhotoLoader(this, poster_id);
        loader.setCallBack(this);
        imageFinishedLoad = new ImageFinishedLoad();
        loader.start();

        posterInfoHandle = new PosterInfoHandle();
        followHandle = new FollowHandle();
        collectHandle = new CollectHandle();


        if (MemberMgr.Member != null) {
            loadPosterInfo();
        }
    }

    private void loadPosterInfo() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("poster_id", poster_id);
        param.put("member_id", String.valueOf(MemberMgr.Member.getId()));
        final WebXmlLoader posterInfoLoader = new WebXmlLoader(PosterShowerActivity.this, StaticVar.APIUrl.PosterInfo,param);

        posterInfoLoader.setCallBack(new IWebLoaderCallBack() {
            @Override
            public void CallBack(Object result) {
                ArrayList<HashMap<String,String>> xmlArray=(ArrayList<HashMap<String,String>>)result;
                if(xmlArray.size()>0) {
                    HashMap<String, String> rs = xmlArray.get(0);
                    posterInfoHandle.setCollect(rs.get("collect").equals("Y"));
                    posterInfoHandle.setFollow(rs.get("follow").equals("Y"));
                    posterInfoHandle.sendHandle();
                }
            }
        });

        posterInfoLoader.start();

    }



    ImageFinishedLoad imageFinishedLoad;
    class ImageFinishedLoad extends AbstractHandles{

        @Override
        public void callFunction() {
            ActivityMgr.ShowProgress(false,PosterShowerActivity.this,mViewPager,progress);
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }
    }

    MenuItem menuCollect,menuFollow;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poster_shower, menu);
        menuCollect=menu.findItem(R.id.action_collect);
        menuFollow=menu.findItem(R.id.action_follow);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home)
        {
            finish();
            return true;
        }else if (id == R.id.action_follow) {
            if(MemberMgr.CheckIsLogin(PosterShowerActivity.this, RequestCode.LoginActivity)){

                HashMap<String, String> param = new HashMap<String, String>();
                param.put("poster_id", poster_id);
                param.put("member_id", String.valueOf(MemberMgr.Member.getId()));
                param.put("follow", menuFollow.getTitle().toString().contains("取消")?"N":"Y");
                ResultLoader resultLoader=new ResultLoader(this,StaticVar.APIUrl.PosterFollow,param);
                resultLoader.setCallBack(new IWebLoaderCallBack() {
                    @Override
                    public void CallBack(Object result) {
                        ArrayList<ResultObj> objs=(ArrayList<ResultObj>)result;
                        if(objs.size()>0&&objs.get(0).getId()==0){
                            followHandle.setRet(true);
                        }else {
                            followHandle.setRet(false);
                        }
                        followHandle.sendHandle();
                    }
                });
                menuFollow.setEnabled(false);
                resultLoader.start();
            }
            return true;
        }else if (id == R.id.action_collect) {
            if(MemberMgr.CheckIsLogin(PosterShowerActivity.this, RequestCode.LoginActivity)){

                HashMap<String, String> param = new HashMap<String, String>();
                param.put("poster_id", poster_id);
                param.put("member_id", String.valueOf(MemberMgr.Member.getId()));
                param.put("collect", menuCollect.getTitle().toString().contains("取消")?"N":"Y");
                ResultLoader resultLoader=new ResultLoader(this,StaticVar.APIUrl.PosterCollect,param);
                resultLoader.setCallBack(new IWebLoaderCallBack() {
                    @Override
                    public void CallBack(Object result) {
                        ArrayList<ResultObj> objs=(ArrayList<ResultObj>)result;
                        if(objs.size()>0&&objs.get(0).getId()==0){
                            collectHandle.setRet(true);
                        }else {
                            collectHandle.setRet(false);
                        }
                        collectHandle.sendHandle();
                    }
                });
                menuCollect.setEnabled(false);
                resultLoader.start();
            }
            return true;
        }else if (id == R.id.action_sharetimeline) {
            PosterPhotoObj photo=photos.get(0);
            Bitmap bm=ImageLoaderMgr.GetImageLoader().loadImageSync(StaticVar.PetImageUrl+photo.getPhoto(),ImageLoaderMgr.GetDefaultDisplayImageOptions());
            WechatMgr.SendUrlToFriendGroup(StaticVar.PosterShowerUrl+poster_id, photo.getNeeds(),photo.getAddress(),bm);
            return true;
        }else if (id == R.id.action_sharewechat) {
            PosterPhotoObj photo=photos.get(0);
            Bitmap bm=ImageLoaderMgr.GetImageLoader().loadImageSync(StaticVar.PetImageUrl+photo.getPhoto(),ImageLoaderMgr.GetDefaultDisplayImageOptions());
            WechatMgr.SendUrlToFriend(StaticVar.PosterShowerUrl+poster_id, photo.getNeeds(),photo.getAddress(),bm);
            return true;
        }else if (id == R.id.action_help) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if(requestCode==RequestCode.LoginActivity){
            if(resultCode == RESULT_OK){
                loadPosterInfo();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void CallBack(Object result) {
        photos=(ArrayList<PosterPhotoObj>)result;
//        for(PosterPhotoObj photo:photos){
//            Bitmap bitmap= ImageUtil.GetHttpBitmap(StaticVar.PetImageUrl+photo.getPhoto());
//            photo.setTag(bitmap);
//        }
        imageFinishedLoad.sendHandle();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public  class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private  final String ARG_SECTION_NUMBER = "section_number";
        private  final String ARG_TOTAL_NUMBER = "total_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_poster_shower, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.txtDescription);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            int index=getArguments().getInt(ARG_SECTION_NUMBER);
            int total=photos.size();
            PosterPhotoObj sphoto=photos.get(index);
            StringBuilder sb=new StringBuilder();
            sb.append("(").append(index+1).append("/").append(total).append(") ");
            sb.append(sphoto.getNeeds()).append("\r\n").append("联系方式:").append(sphoto.getContact());
            sb.append("\r\n").append(sphoto.getAddress());
            textView.setText(sb.toString());
            textView.setBackgroundColor(Color.argb(70,0,0,0));

            ImageView img=(ImageView) rootView.findViewById(R.id.imgPhoto);
            //img.setImageBitmap((Bitmap) sphoto.getTag());
            ImageLoaderMgr.GetImageLoader().displayImage(StaticVar.PetImageUrl+sphoto.getPhoto(),img,ImageLoaderMgr.GetDefaultDisplayImageOptions());
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return new PlaceholderFragment().newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return photos.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
