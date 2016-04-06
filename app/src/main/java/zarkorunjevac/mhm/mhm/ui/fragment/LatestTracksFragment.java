package zarkorunjevac.mhm.mhm.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import zarkorunjevac.mhm.R;
import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.common.TrackListType;
import zarkorunjevac.mhm.mhm.common.Utils;
import zarkorunjevac.mhm.mhm.model.pojo.Track;
import zarkorunjevac.mhm.mhm.ui.activity.MusicListActivity;

/**
 * Created by zarko.runjevac on 3/21/2016.
 */
public class LatestTracksFragment extends Fragment
       implements MVP.ProvidedLatestTracksPresenterOps{

    private MVP.ProvidedMusicListActivityOps mMusicListActivityListener;
    public LatestTracksFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        LinearLayout.LayoutParams layoutParam = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );

        ScrollView m_Scroll = new ScrollView(getActivity());

        HashMap<String,List<Track>> trackList=mMusicListActivityListener.loadLatestLists();
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        for(String listName: MusicListActivity.LATEST_LIST_FOR_DOWNLOAD){
            layout.addView(createView(listName, trackList.get(listName)));
        }
//        List<Track> all=trackList.get("all");
//        for(Track track :all){
//            getSoundCloudLink(track);
//        }
        m_Scroll.addView(layout, layoutParam);
        return m_Scroll;
    }

    @Override
    public void onStreamLinkFound(String link) {
        Utils.showToast(getActivity(), link);
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
            Picasso.with(LatestTracksFragment.this.getActivity())
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
                artistTextView=(TextView)itemView.findViewById(R.id.track_artist);
                trackThumbnailImageView=(CircleImageView)itemView.findViewById(R.id.track_thumbnail);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Log.d(" ViewHolder", "onClick: ");
                int position=getLayoutPosition();
                Track track=mTracks.get(position);
               // getSoundCloudLink(track);
                //Utils.showToast(LatestTracksFragment.this.getActivity(),track.getPosturl());
                Log.d("LatestTracksFragment", "onClick: "+track.getPosturl());
                mMusicListActivityListener.getStreamUrl(track, TrackListType.LATEST);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MVP.ProvidedMusicListActivityOps)  {
            mMusicListActivityListener=(MVP.ProvidedMusicListActivityOps)context;
        }
    }

    private  LinearLayout createView(final String listName, final List<Track> trackList){
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        if(null!=trackList){
            LinearLayout.LayoutParams params = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);



            LinearLayout.LayoutParams textViewParams = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            TextView listNameTextView = new TextView(getActivity());
            listNameTextView.setText(listName);
            listNameTextView.setLayoutParams(textViewParams);
            listNameTextView.setTextSize(getActivity().getResources().getDimension(R.dimen.body_heading));
            textViewParams.setMargins(16, 0, 0, 0);
            layout.addView(listNameTextView);

            final List<Track> shortList=new ArrayList<Track>(trackList.subList(0,3));

            final ContentAdapter adapter=new ContentAdapter(shortList);
            RecyclerView recyclerView=new RecyclerView(getActivity());
            recyclerView.setLayoutParams(params);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            layout.addView(recyclerView);

            final Button btn = new Button(getActivity());
            btn.setText("v");
            btn.setLayoutParams(params);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("LatestTacksFragment", "onClick: " + adapter.getItemCount());

                    if (btn.getText().equals("v")) {
                        btn.setText("SHOW ALL");
                        int curSize = adapter.getItemCount();
                        shortList.addAll(trackList.subList(3, trackList.size()));

                        adapter.notifyDataSetChanged();
                        Log.d("LatestTacksFragment", "onClick: " + adapter.getItemCount());
                    } else {
                        //strartnewactivity
                    }
                }
            });

            layout.addView(btn);
            return layout;
        }
        return layout;
    }
}