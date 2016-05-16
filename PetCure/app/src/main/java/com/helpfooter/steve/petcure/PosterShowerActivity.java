package com.helpfooter.steve.petcure;

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
import android.widget.TextView;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.PosterObj;
import com.helpfooter.steve.petcure.dataobjects.PosterPhotoObj;
import com.helpfooter.steve.petcure.handles.AbstractHandles;
import com.helpfooter.steve.petcure.interfaces.IWebLoaderCallBack;
import com.helpfooter.steve.petcure.loader.PosterPhotoLoader;
import com.helpfooter.steve.petcure.mgr.ActivityMgr;
import com.helpfooter.steve.petcure.mgr.ImageLoaderMgr;
import com.helpfooter.steve.petcure.mgr.WechatMgr;

import java.util.ArrayList;

public class PosterShowerActivity extends AppCompatActivity implements IWebLoaderCallBack {

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

        poster_id=getIntent().getStringExtra("poster_id");
        PosterPhotoLoader loader=new PosterPhotoLoader(this,poster_id);
        loader.setCallBack(this);
        imageFinishedLoad=new ImageFinishedLoad();
        loader.start();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poster_shower, menu);
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
            return true;
        }else if (id == R.id.action_collect) {
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
