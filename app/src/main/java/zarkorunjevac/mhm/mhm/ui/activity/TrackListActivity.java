package zarkorunjevac.mhm.mhm.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import zarkorunjevac.mhm.R;
import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.common.Config;
import zarkorunjevac.mhm.mhm.common.EndlessRecyclerViewScrollListener;

import zarkorunjevac.mhm.mhm.common.GenericActivity;
import zarkorunjevac.mhm.mhm.common.TrackListType;
import zarkorunjevac.mhm.mhm.common.TypefaceUtils;
import zarkorunjevac.mhm.mhm.model.pojo.Track;
import zarkorunjevac.mhm.mhm.presenter.TrackPresenter;

public class TrackListActivity extends GenericActivity<MVP.RequiredViewOps,
        MVP.ProvidedTrackListPresenterOps,
        TrackPresenter>
        implements MVP.RequiredViewOps,
        MVP.ProvidedMusicListActivityOps {

    protected ProgressBar mLoadingProgressBar;
    List<Track> mTrackList;
    RecyclerView mTrackListRecyclerView;
    String mTrackListName;
    TrackListType mTrackListType;
    int mPage;
    ContentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);

        mPage=0;

        initializeViewFields();

        super.onCreate(TrackPresenter.class, this);

        getDataFromBundle(getIntent().getExtras());

        startDownload();


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

    @Override
    public void displayProgressBar() {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressBar() {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void reportDownloadFailure(String listName) {

    }

    @Override
    public void dispayResults(ConcurrentHashMap<String, List<Track>> trackLists) {

        if(mPage==0) {
            if (mTrackListType == null) mTrackListType = getPresenter().getTrackListType();
            if (mTrackListName == null) mTrackListName = getPresenter().getTrackListName();


            mTrackList = trackLists.get(mTrackListType.toString().toLowerCase() + "." + mTrackListName);
            setupViewFields();
        }else{
            int curSize=mAdapter.getItemCount();
            List<Track> tempList=trackLists.get(mTrackListType.toString().toLowerCase() + "." + mTrackListName);
            if(null!=tempList){
                mTrackList.addAll(tempList);
                mAdapter.notifyItemRangeInserted(curSize,mTrackList.size()-1);
            }

        }

    }


    @Override
    public HashMap<String, List<Track>> loadLatestLists() {
        return null;
    }

    @Override
    public HashMap<String, List<Track>> loadPopularLists() {
        return null;
    }

    @Override
    public void tryToPlayTrack(Track track) {

    }

    @Override
    public Track loadTrack() {
        return null;
    }

    @Override
    public void togglePlayPause() {

    }


    private void getDataFromBundle(Bundle data) {
        if (null != data) {
            mTrackListName = data.getString(Config.LIST_NAME);
            String listType = data.getString(Config.LIST_TYPE);
            if (listType.equals("latest")) mTrackListType = TrackListType.LATEST;
            else mTrackListType = TrackListType.POPULAR;
        }
    }

    private void startDownload() {

        List<String> latestList;
        List<String> popularList;

        if (mTrackListType == TrackListType.LATEST) {
            latestList = Arrays.asList(mTrackListName);
            popularList = new ArrayList<String>();
        } else {
            latestList = new ArrayList<String>();
            popularList = Arrays.asList(mTrackListName);
        }
        if (mTrackListName != null && mTrackListType != null)
            getPresenter().setTrackListParams(mTrackListName, mTrackListType);

        getPresenter().startTrackListDownload(latestList, popularList);

    }


    private void initializeViewFields() {
        mLoadingProgressBar =
                (ProgressBar) findViewById(R.id.progressBar_loading);


        mTrackListRecyclerView = (RecyclerView) findViewById(R.id.rvTrackList);
    }

    private void setupViewFields() {

        setupToolbar();
        mAdapter = new ContentAdapter(mTrackList);
        mTrackListRecyclerView.setAdapter(mAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mTrackListRecyclerView.setLayoutManager(linearLayoutManager);

        mTrackListRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager){
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                getPresenter().takePage(page,mTrackListType,mTrackListName);
                mPage++;
            }
        });
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(mTrackListName);
        toolbarTitle.setTypeface(TypefaceUtils.getTypeFaceHelevticaNeueProMedium(this));
        toolbarTitle.setTextSize(getResources().getDimension(R.dimen.toolbar_title));
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));
    }

    /**
     * Adapter to display recycler view.
     */
    public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {


        private List<Track> mTracks;

        public ContentAdapter(List<Track> tracks) {
            mTracks = tracks;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Track track = mTracks.get(position);
            TextView textView = holder.titleTextView;
            textView.setText(track.getTitle());
            holder.artistTextView.setText(track.getArtist());
            Log.d("ContentAdapter", "onBindViewHolder: " + track.getThumbUrlMedium());
            Picasso.with(TrackListActivity.this)
                    .load(track.getThumbUrlMedium()).
                    into(holder.trackThumbnailImageView);


        }

        @Override
        public int getItemCount() {
            return (mTracks == null) ? 0 : mTracks.size();
            // return 3;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView titleTextView;
            public TextView artistTextView;

            public CircleImageView trackThumbnailImageView;

            public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.item_list, parent, false));
                titleTextView = (TextView) itemView.findViewById(R.id.track_title);
                titleTextView.setTypeface(TypefaceUtils.getTypeFaceHelevticaNeueProMedium(TrackListActivity.this));

                artistTextView = (TextView) itemView.findViewById(R.id.track_artist);
                artistTextView.setTypeface(TypefaceUtils.getTypeFaceHelevticaNeueProMedium(TrackListActivity.this));

                trackThumbnailImageView = (CircleImageView) itemView.findViewById(R.id.track_thumbnail);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Log.d(" ViewHolder", "onClick: ");
                int position = getLayoutPosition();
                Track track = mTracks.get(position);


                Log.d("LatestTracksFragment", "onClick: " + track.getPosturl());
                //mMusicListActivityListener.tryToPlayTrack(track, mTrackListType);
            }
        }
    }
}
