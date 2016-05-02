package zarkorunjevac.mhm.mhm.ui.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import zarkorunjevac.mhm.R;
import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.common.Config;
import zarkorunjevac.mhm.mhm.common.TrackListType;
import zarkorunjevac.mhm.mhm.common.TypefaceUtils;
import zarkorunjevac.mhm.mhm.model.pojo.Track;
import zarkorunjevac.mhm.mhm.ui.activity.MusicListActivity;

/**
 * Created by zarkorunjevac on 02/05/16.
 */
public class TrackListFragment extends Fragment
        implements MVP.ProvidedLatestTracksPresenterOps{
    protected final static String TAG =
            TrackListFragment.class.getSimpleName();

    private MVP.ProvidedMusicListActivityOps mMusicListActivityListener;

    private TrackListType mTrackListType;

    private Context mContext;

    private LinearLayout mLayout;


    public TrackListFragment(){
        //no-op

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        HashMap<String,List<Track>> trackList=loadTrackList();


        RelativeLayout.LayoutParams layoutParam = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT );
        layoutParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ScrollView m_Scroll = new ScrollView(getActivity());

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        List<String> avaliableList=null;
        Bundle data=getArguments();
        setTrackListType(data);
        if(TrackListType.LATEST==mTrackListType) avaliableList=Config.LATEST_LIST_FOR_DOWNLOAD;
        if(TrackListType.POPLUAR==mTrackListType) avaliableList=Config.POPULAR_LIST_FOR_DOWNLOAD;

        for(String listName: avaliableList){
            layout.addView(createView(listName, trackList.get(listName)));
        }

        m_Scroll.addView(layout, layoutParam);


        return m_Scroll;
    }

    @Override
    public void onAttach(Context context) {
        mContext=context;
        super.onAttach(mContext);
        if(mContext instanceof MVP.ProvidedMusicListActivityOps)  {
            mMusicListActivityListener=(MVP.ProvidedMusicListActivityOps)mContext;
        }
    }

    @Override
    public void onStreamLinkFound(Track track) {
        //no-op
    }


    private  LinearLayout createView(final String listName, final List<Track> trackList){
        LinearLayout layout = new LinearLayout(getActivity());

        layout.setOrientation(LinearLayout.VERTICAL);
        if(null!=trackList){
            LinearLayout.LayoutParams params = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            TextView listNameTextView = makeTextView(listName);
            layout.addView(listNameTextView);

            final List<Track> shortList=new ArrayList<Track>(trackList.subList(0,6));

            final ContentAdapter adapter=new ContentAdapter(shortList);
            RecyclerView recyclerView=makeRecyclerView(adapter);

            layout.addView(recyclerView);

            final Button showMoreButton = makeButton();

            showMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("LatestTacksFragment", "onClick: " + adapter.getItemCount());

                    if (showMoreButton.getText().equals("More...")) {
                        showMoreButton.setText("SHOW ALL");
                        int curSize = adapter.getItemCount();
                        shortList.addAll(trackList.subList(3, trackList.size()));

                        adapter.notifyDataSetChanged();
                        Log.d("LatestTacksFragment", "onClick: " + adapter.getItemCount());
                    } else {
                        //strartnewactivity
                    }
                }
            });

            layout.addView(showMoreButton);
            return layout;
        }
        return layout;
    }



    private Button makeButton(){

        LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Button moreButton=new Button(getActivity());
        params.gravity=Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
        // params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        moreButton.setText("More...");
        moreButton.setTypeface(TypefaceUtils.getTypeFaceHelevticaNeueProMedium(getActivity()));

        moreButton.setLayoutParams(params);
        moreButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        // moreButton.setBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
        moreButton.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.green_button));
        moreButton.setTextColor(getActivity().getResources().getColor(R.color.white));
        // moreButton.setBackgroundResource(0);
        return moreButton;
    }

    private RecyclerView makeRecyclerView(ContentAdapter adapter){
        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerView=new RecyclerView(getActivity());
        recyclerView.setLayoutParams(params);

        recyclerView.setHasFixedSize(true);
//       OverlapDecoration overlapDecoration=new OverlapDecoration();
//        recyclerView.addItemDecoration(overlapDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    private TextView makeTextView(String listName){
        LinearLayout.LayoutParams textViewParams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView listNameTextView = new TextView(getActivity());
        listNameTextView.setTypeface(TypefaceUtils.getTypeFaceHelevticaNeueProMedium(getActivity()));
        listNameTextView.setText(listName);
        listNameTextView.setLayoutParams(textViewParams);
        listNameTextView.setTextSize(getActivity().getResources().getDimension(R.dimen.body_heading));
        listNameTextView.setTextColor(getActivity().getResources().getColor(R.color.white));
        textViewParams.setMargins(32, 16, 16, 16);

        return listNameTextView;
    }


    /**
     * Adapter to display recycler view.
     */
    public  class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {


        private List<Track> mTracks;
        public ContentAdapter( List<Track> tracks) {
            mTracks=tracks;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Track track=mTracks.get(position);
            TextView textView=holder.titleTextView;
            textView.setText(track.getTitle());
            holder.artistTextView.setText(track.getArtist());
            Log.d("ContentAdapter", "onBindViewHolder: " + track.getThumbUrlMedium());
            Picasso.with(TrackListFragment.this.getActivity())
                    .load(track.getThumbUrlMedium()).
                    into(holder.trackThumbnailImageView);



        }
        @Override
        public int getItemCount() {
            return (mTracks == null) ? 0 : mTracks.size();
            // return 3;
        }

        public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView titleTextView;
            public TextView artistTextView;

            public CircleImageView trackThumbnailImageView;
            public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.item_list, parent, false));
                titleTextView=(TextView)itemView.findViewById(R.id.track_title);
                titleTextView.setTypeface(TypefaceUtils.getTypeFaceHelevticaNeueProMedium(getActivity()));

                artistTextView=(TextView)itemView.findViewById(R.id.track_artist);
                artistTextView.setTypeface(TypefaceUtils.getTypeFaceHelevticaNeueProMedium(getActivity()));

                trackThumbnailImageView=(CircleImageView)itemView.findViewById(R.id.track_thumbnail);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Log.d(" ViewHolder", "onClick: ");
                int position=getLayoutPosition();
                Track track=mTracks.get(position);


                Log.d("LatestTracksFragment", "onClick: "+track.getPosturl());
                mMusicListActivityListener.tryToPlayTrack(track, mTrackListType);
            }
        }
    }

    private HashMap<String,List<Track>> loadTrackList(){
        if(TrackListType.LATEST==mTrackListType){
            return mMusicListActivityListener.loadLatestLists();
        }else{
            return mMusicListActivityListener.loadPopularLists();
        }
    }

    public void setTrackListType(TrackListType trackListType) {
        mTrackListType = trackListType;
    }

    private void setTrackListType(Bundle data){
        String listType=data.getString("listType");
        if(listType.equals("latest")) mTrackListType=TrackListType.LATEST;
        else mTrackListType=TrackListType.POPLUAR;
    }
}
