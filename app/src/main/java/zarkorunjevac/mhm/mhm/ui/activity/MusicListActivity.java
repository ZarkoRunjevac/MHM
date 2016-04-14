package zarkorunjevac.mhm.mhm.ui.activity;

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
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import zarkorunjevac.mhm.R;
import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.common.GenericActivity;
import zarkorunjevac.mhm.mhm.common.TrackListType;
import zarkorunjevac.mhm.mhm.model.pojo.Track;
import zarkorunjevac.mhm.mhm.presenter.TrackPresenter;
import zarkorunjevac.mhm.mhm.ui.fragment.LatestTracksFragment;
import zarkorunjevac.mhm.mhm.ui.fragment.PlaybackControlsFragment;
import zarkorunjevac.mhm.mhm.ui.fragment.PopularTracksFragment;

public class MusicListActivity extends GenericActivity<MVP.RequiredViewOps,
        MVP.ProvidedTrackListPresenterOps,
        TrackPresenter>
        implements MVP.RequiredViewOps,
        MVP.ProvidedMusicListActivityOps{

    protected ProgressBar mLoadingProgressBar;
    protected ViewPager mViewPager;
    protected TabLayout mTabs;
    private DrawerLayout mDrawerLayout;
    private PlaybackControlsFragment mControlsFragment;
    private LatestTracksFragment mLatestTracksFragment;
    private PopularTracksFragment mPopularTracksFragment;


    public static final List<String> LATEST_LIST_FOR_DOWNLOAD=Arrays.asList("all", "fresh", "remix", "noremix");
    public static final List<String> POPULAR_LIST_FOR_DOWNLOAD=Arrays.asList("now", "remix", "noremix");

    private HashMap<String,List<Track>> mPopularLists=new HashMap<String,List<Track>>();
    private HashMap<String,List<Track>> mLatestLists=new HashMap<String,List<Track>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);


        initializeViewFields();
        super.onCreate(TrackPresenter.class, this);


        getPresenter().startTrackListDownload(LATEST_LIST_FOR_DOWNLOAD,
                POPULAR_LIST_FOR_DOWNLOAD);

//        mControlsFragment = (PlaybackControlsFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.fragment_playback_controls);
//
//        if (mControlsFragment == null) {
//            throw new IllegalStateException("Mising fragment with id 'controls'. Cannot continue.");
//        }
//
//        hidePlaybackControls();

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mLatestTracksFragment=new LatestTracksFragment();
        mPopularTracksFragment=new PopularTracksFragment();

         mTabs = (TabLayout) findViewById(R.id.tabs);


    }

    private void setupViewFields(){
        setupViewPager(mViewPager);
        mTabs.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

        adapter.addFragment(mLatestTracksFragment, "Latest");
        adapter.addFragment(mPopularTracksFragment, "Popular");

        viewPager.setAdapter(adapter);
//        getSupportFragmentManager().beginTransaction()
//                .show(mControlsFragment)
//                .commit();
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


    @Override
    public void displayPlaybackFragment() {

        getSupportFragmentManager().beginTransaction()
                .show(mControlsFragment)
                .commit();

    }

    @Override
    public void dismissPlaybackFragment() {

        getSupportFragmentManager().beginTransaction()
                .hide(mControlsFragment)
                .commit();

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
    public void onStreamLinkFound(Track track, TrackListType trackListType) {
        if(trackListType.equals(TrackListType.LATEST)){
            MVP.ProvidedLatestTracksPresenterOps latestTracksPresenterOps=(MVP.ProvidedLatestTracksPresenterOps)mLatestTracksFragment;
//            MVP.ProvidedPlaybackControlsFragmentOps providedPlaybackControlsFragmentOps=(MVP.ProvidedPlaybackControlsFragmentOps)mControlsFragment;
//            mControlsFragment.initializeViewFields(track);
//           mControlsFragment.displayPlayButton();
//            showPlaybackFragment();
            latestTracksPresenterOps.onStreamLinkFound(track);

            Log.d(TAG, "onStreamLinkFound: link="+track.getStreamUrl());

            getPresenter().playMedia(track);
            //show playback fragment
           // showPlaybackFragment();
        }else{
            MVP.ProvidedPopularTracksPresenterOps popularTracksPresenterOps=(MVP.ProvidedPopularTracksPresenterOps)mPopularTracksFragment;
            popularTracksPresenterOps.onStreamLinkFound(track);
        }



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
    public void tryToPlayTrack(Track track, TrackListType trackListType) {
        getPresenter().startTrackDownload(track,trackListType);
    }

    @Override
    public void displayPlayButton() {
//        MVP.ProvidedPlaybackControlsFragmentOps providedPlaybackControlsFragmentOps=(MVP.ProvidedPlaybackControlsFragmentOps)mControlsFragment;
//        providedPlaybackControlsFragmentOps.displayPauseButton();
    }

    @Override
    public Track loadTrack() {
        return getPresenter().getSelectedTrack();
    }

    @Override
    public void displayPauseButton() {
//        MVP.ProvidedPlaybackControlsFragmentOps providedPlaybackControlsFragmentOps=(MVP.ProvidedPlaybackControlsFragmentOps)mControlsFragment;
//        providedPlaybackControlsFragmentOps.displayPlayButton();
    }

    @Override
    public void togglePlayPause() {
        getPresenter().togglePlayPause();
    }

    private void showPlaybackFragment(){
        Log.d(TAG, "showPlaybackFragment: ");
        getSupportFragmentManager().beginTransaction()

//                .setCustomAnimations(R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom,
//                        R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom)
                .show(mControlsFragment)
                .commit();
    }

    protected void hidePlaybackControls() {

        Log.d(TAG, "hidePlaybackControls");
        getSupportFragmentManager().beginTransaction()
                .hide(mControlsFragment)
                .commit();
    }
}


