package zarkorunjevac.mhm.mhm.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
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
import zarkorunjevac.mhm.mhm.common.Utils;
import zarkorunjevac.mhm.mhm.model.pojo.Music;
import zarkorunjevac.mhm.mhm.model.pojo.Track;
import zarkorunjevac.mhm.mhm.presenter.MusicPresenter;
import zarkorunjevac.mhm.mhm.ui.fragment.LatestTacksFragment;
import zarkorunjevac.mhm.mhm.ui.fragment.PopularTracksFragment;

public class MusicListActivity extends GenericActivity<MVP.RequiredViewOps,
        MVP.ProvidedMusicPresenterOps,
        MusicPresenter>
        implements MVP.RequiredViewOps,
        MVP.ProvidedMusicListActivityOps{

    protected ProgressBar mLoadingProgressBar;
    protected ViewPager mViewPager;
    protected TabLayout mTabs;
    private DrawerLayout mDrawerLayout;
    public static final List<String> LATEST_LIST_FOR_DOWNLOAD=Arrays.asList("all", "fresh", "remix", "noremix");
    public static final List<String> POPULAR_LIST_FOR_DOWNLOAD=Arrays.asList("now", "remix", "noremix");

    private HashMap<String,List<Track>> mPopularLists=new HashMap<String,List<Track>>();
    private HashMap<String,List<Track>> mLatestLists=new HashMap<String,List<Track>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);


        initializeViewFields();
        super.onCreate(MusicPresenter.class, this);
        getPresenter().startProcessing(LATEST_LIST_FOR_DOWNLOAD,
                POPULAR_LIST_FOR_DOWNLOAD);

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

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        // Set Tabs inside Toolbar
        //1. getPresenter().startProcessing();

         mTabs = (TabLayout) findViewById(R.id.tabs);


    }

    private void setupViewFields(){
        setupViewPager(mViewPager);
        mTabs.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        LatestTacksFragment latestTacksFragment =new LatestTacksFragment();
        //latestBlogsFragment.getBlogs(blogs);
        adapter.addFragment(latestTacksFragment, "Latest");
        adapter.addFragment(new PopularTracksFragment(), "Popular");

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

    /**
     * Make the ProgressBar invisible.
     */
    @Override
    public void dismissProgressBar() {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void reportDownloadFailure(Uri url,
                                      boolean downloadsComplete) {
        Utils.showToast(this,
                "image at "
                        + url.toString()
                        + " failed to download!");

        // Remove the URL that failed from the UI.
        removeUrl(url,
                downloadsComplete);

        if (downloadsComplete)
            // Dismiss the progress bar.
            mLoadingProgressBar.setVisibility(View.INVISIBLE);
    }
    /**
     * Remove a URL that couldn't be downloaded.
     */
    private void removeUrl(Uri url,
                           boolean downloadsComplete) {

    }

    /**
     * Display the URLs provided by the user thus far.
     */
    @Override
    public void displayUrls() {

    }

    /**
     * Start the DisplayImagesActivity to display the results of the
     * download to the user.
     */
    @Override
    public void displayResults(Uri directoryPathname) {

    }

    @Override
    public void loadTracks(Music music) {

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
        return mLatestLists;
    }

    @Override
    public HashMap<String, List<Track>> loadPopularLists() {
        return mPopularLists;
    }
}


