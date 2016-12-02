package com.zarkorunjevac.mhm.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zarkorunjevac.mhm.MVP;
import com.zarkorunjevac.mhm.R;
import com.zarkorunjevac.mhm.common.Config;
import com.zarkorunjevac.mhm.common.TrackListType;
import com.zarkorunjevac.mhm.common.TypefaceUtils;
import com.zarkorunjevac.mhm.model.pojo.Track;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zarko.runjevac on 11/24/2016.
 */

public class TracksFragment extends Fragment{

    private RecyclerView mTracksRecyclerView;
    String mTrackListName;
    TrackListType mTrackListType;
    int mPage;
    ContentAdapter mAdapter;
    private Context mContext;
    private MVP.ProvidedMusicListOps mMusicListActivityListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage=0;
        getDataFromBundle();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_tracks_list,container,false);

        return view;

    }


    public static TracksFragment newInstance(String listName, String listType){
        TracksFragment tracksFragment=new TracksFragment();
        Bundle args=new Bundle();
        args.putString(Config.LIST_NAME,listName);
        args.putString(Config.LIST_TYPE,listType);
        tracksFragment.setArguments(args);

        return tracksFragment;
    }


    private void getDataFromBundle(){
        mTrackListName=getArguments().getString(Config.LIST_NAME);
        String listType = getArguments().getString(Config.LIST_TYPE);
        if (listType.equals("latest")) mTrackListType = TrackListType.LATEST;
        else mTrackListType = TrackListType.POPULAR;
    }


    @Override
    public void onAttach(Context context) {
        mContext=context;
        super.onAttach(mContext);
        if(mContext instanceof MVP.ProvidedMusicListOps)  {
            mMusicListActivityListener=(MVP.ProvidedMusicListOps)mContext;
        }
    }


    public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {


        private List<Track> mTracks;

        public ContentAdapter(List<Track> tracks) {
            mTracks = tracks;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ContentAdapter.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ContentAdapter.ViewHolder holder, int position) {

            Track track = mTracks.get(position);
            TextView textView = holder.titleTextView;
            textView.setText(track.getTitle());
            holder.artistTextView.setText(track.getArtist());
            Log.d("ContentAdapter", "onBindViewHolder: " + track.getThumbUrlMedium());
            Picasso.with(getActivity())
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
                titleTextView.setTypeface(TypefaceUtils.getTypeFaceHelevticaNeueProMedium(getActivity()));

                artistTextView = (TextView) itemView.findViewById(R.id.track_artist);
                artistTextView.setTypeface(TypefaceUtils.getTypeFaceHelevticaNeueProMedium(getActivity()));

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
        if (mTrackListName != null && mTrackListType != null);
           // mMusicListActivityListener.setTrackListParams(mTrackListName, mTrackListType);

       // mMusicListActivityListener.startTrackListDownload(latestList, popularList);

    }
}
