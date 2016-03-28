package zarkorunjevac.mhm.mhm.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import zarkorunjevac.mhm.R;
import zarkorunjevac.mhm.mhm.model.Blog;
import zarkorunjevac.mhm.mhm.service.HypemApiService;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    public List<Blog> blogs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        //////////////
        OkHttpClient okClient = new OkHttpClient();
                okClient.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Log.d("MainActivity", "intercept "+request.url().toString());
                com.squareup.okhttp.Response response=chain.proceed(chain.request());

                //Log.d("MainActivity","intercept "+ response.body().string());
                return response;
            }
        });
        String BASE_URL ="https://api.hypem.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HypemApiService apiService=retrofit.create(HypemApiService.class);

        Call<List<Blog>> call=apiService.getBlogs(true,1,10);
        call.enqueue(new Callback<List<Blog>>() {
            @Override
            public void onResponse(Response<List<Blog>> response) {
                Log.d("MainActivity", "onResponse Satus code " + response.code());
                Log.d("MainActivity", "onResponse " + response.body());


                blogs = response.body();
                Toast.makeText(getApplicationContext(), blogs.get(1).getSitename(), Toast.LENGTH_LONG).show();
                Log.d("MainActivity", "onResponse ");

                // Setting ViewPager for each Tabs
                ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
                setupViewPager(viewPager, blogs);
                // Set Tabs inside Toolbar
                TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
                tabs.setupWithViewPager(viewPager);

            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("MainActivity", "onFailure " + t.getLocalizedMessage());
                Toast.makeText(getApplication(), "Nije uspjelo", Toast.LENGTH_LONG).show();
            }
        });
        /////////////////



        // Create Navigation drawer and inlfate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        // TODO: handle navigation

                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        // Adding Floating Action Button to bottom right of main view
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Hello Snackbar!",
                        Snackbar.LENGTH_LONG).show();
            }
        });

    }
    private void setupViewPager(ViewPager viewPager, List<Blog> blogs) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        LatestTacksFragment latestTacksFragment =new LatestTacksFragment();
        latestTacksFragment.getBlogs(blogs);
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
}
