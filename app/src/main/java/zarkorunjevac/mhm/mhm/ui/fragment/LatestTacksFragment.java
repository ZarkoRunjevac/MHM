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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import zarkorunjevac.mhm.R;
import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.common.Utils;
import zarkorunjevac.mhm.mhm.model.pojo.Track;
import zarkorunjevac.mhm.mhm.ui.activity.MusicListActivity;

/**
 * Created by zarko.runjevac on 3/21/2016.
 */
public class LatestTacksFragment extends Fragment {

    private MVP.ProvidedMusicListActivityOps mMusicListActivityListener;
    public LatestTacksFragment(){

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

        m_Scroll.addView(layout,layoutParam);
        return m_Scroll;
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
            Log.d("ContentAdapter", "onBindViewHolder: "+track.getThumbUrlMedium());
            Picasso.with(LatestTacksFragment.this.getActivity())
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
                getSoundCloudLink(track.getPosturl());
                Utils.showToast(LatestTacksFragment.this.getActivity(),track.getPosturl());
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

        LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

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

    private void getSoundCloudLink(final String url){



        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Document doc= Jsoup.connect(url).get();
                    Elements media = doc.select("[src]");

                    for (Element src : media) {
                        String link=src.attr("abs:src");
                        if(link.contains("api.soundcloud")){

                            Log.d("LatestTacksFragment", "run: "+ URLDecoder.decode(src.attr("abs:src")));
                        }


                    }
//                    Element iframe=doc.select("iframe").first();
//                    Log.d("LatestTacksFragment", "run: iframe+"+iframe);
//                    String src=iframe.attr("src");
//                    //Log.d("LatestTacksFragment", "run: "+src);
//
//                    //String pattern="/tracks/(\\d+)";
//                    String pattern="/tracks/(\\d+)";
//                    Pattern r = Pattern.compile(pattern);
//                    String link= URLDecoder.decode(src, "UTF-8");
//                    Log.d("LatestTacksFragment", "run: "+link);
//                    Matcher m = r.matcher(link);
//                    while(m.find()){
//                        Log.d("LatestTacksFragment", "run: "+m.group());
//                    }
                }catch (IOException e){

                }

            }
        }).start();
    }


}