package com.zarkorunjevac.mhm.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import com.zarkorunjevac.mhm.R;
import com.zarkorunjevac.mhm.MVP;
import com.zarkorunjevac.mhm.common.Config;
import com.zarkorunjevac.mhm.common.GenericActivity;
import com.zarkorunjevac.mhm.common.TypefaceUtils;
import com.zarkorunjevac.mhm.model.pojo.Track;
import com.zarkorunjevac.mhm.presenter.TrackPresenter;
import com.zarkorunjevac.mhm.ui.fragment.TrackListFragment;

public class MusicListActivity extends GenericActivity<MVP.RequiredViewOps,
        MVP.ProvidedTrackListPresenterOps,
        TrackPresenter>
        implements MVP.RequiredViewOps,
        MVP.ProvidedMusicListActivityOps{

    protected ProgressBar mLoadingProgressBar;
    protected ViewPager mViewPager;
    protected TabLayout mTabs;
    private DrawerLayout mDrawerLayout;

    private TrackListFragment mLatestTracksFragment;
    private TrackListFragment mPopularTracksFragment;


    private HashMap<String,List<Track>> mPopularLists=new HashMap<String,List<Track>>();
    private HashMap<String,List<Track>> mLatestLists=new HashMap<String,List<Track>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);


        initializeViewFields();
        super.onCreate(TrackPresenter.class, this);


        getPresenter().startTrackListDownload(Config.LATEST_LIST_FOR_DOWNLOAD,
                Config.POPULAR_LIST_FOR_DOWNLOAD);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        // Destroy the presenter layer, passing in whether this is
        // triggered by a runtime configuration or not.
        getPresenter().onDestroy(isChangingConfigurations());

        // Always call super class for necessary operations when
        // stopping.
        super.onDestroy();
    }

    private void initializeViewFields() {

        mLoadingProgressBar =
                (ProgressBar) findViewById(R.id.progressBar_loading);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(TypefaceUtils.getTypeFaceHelevticaNeueProMedium(this));
        toolbarTitle.setTextSize(getResources().getDimension(R.dimen.toolbar_title));
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mLatestTracksFragment=new TrackListFragment();
        mLatestTracksFragment.setArguments(makeFragmentBundle("latest"));

        mPopularTracksFragment=new TrackListFragment();
        mPopularTracksFragment.setArguments(makeFragmentBundle("popular"));

        mTabs = (TabLayout) findViewById(R.id.tabs);


    }

    private void setupViewFields(){
        setupViewPager(mViewPager);
        mTabs.setupWithViewPager(mViewPager);
        changeTabsFont(mTabs);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

        adapter.addFragment(mLatestTracksFragment, "Latest");
        adapter.addFragment(mPopularTracksFragment, "Popular");

        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void displayProgressBar() {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressBar() {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void reportDownloadFailure(String listName) {

    }

    @Override
    public void dispayResults(ConcurrentHashMap<String, List<Track>> trackLists) {
        Log.d(TAG, "dispayResults: trackLists.size="+trackLists.size());
        mLatestLists=new HashMap<String,List<Track>>();
        mPopularLists=new HashMap<String,List<Track>>();
        for(Map.Entry<String,List<Track>> list:trackLists.entrySet()){
            String listName=list.getKey();
            String[] parts=listName.split(Pattern.quote("."));
            if(parts[0].equals("latest")){
                mLatestLists.put(parts[1],list.getValue());
            }
            else{
                mPopularLists.put(parts[1],list.getValue());
            }
        }

        setupViewFields();

    }



    @Override
    public HashMap<String, List<Track>> loadLatestLists() {
        Log.d(TAG, "loadLatestLists:mLatestLists.size()= "+mLatestLists.size());
        return mLatestLists;
    }

    @Override
    public HashMap<String, List<Track>> loadPopularLists() {
        return mPopularLists;
    }

    @Override
    public void tryToPlayTrack(Track track) {
        getPresenter().startTrackDownload(track);
    }


    @Override
    public Track loadTrack() {
        return getPresenter().getSelectedTrack();
    }



    @Override
    public void togglePlayPause() {
        getPresenter().togglePlayPause();
    }

//    private void showPlaybackFragment(){
//        Log.d(TAG, "showPlaybackFragment: ");
//        getSupportFragmentManager().beginTransaction()
//
////                .setCustomAnimations(R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom,
////                        R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom)
//                .show(mControlsFragment)
//                .commit();
//    }
//
//    protected void hidePlaybackControls() {
//
//        Log.d(TAG, "hidePlaybackControls");
//        getSupportFragmentManager().beginTransaction()
//                .hide(mControlsFragment)
//                .commit();
//    }

    private Bundle makeFragmentBundle(String listType){
        Bundle args=new Bundle();
        args.putString("listType",listType);
        return args;
    }

    private void changeTabsFont(TabLayout tabLayout) {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(TypefaceUtils.getTypeFaceHelevticaNeueProMedium(this));
                    ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.white));
                }
            }
        }
    }
}


