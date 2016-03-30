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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zarkorunjevac.mhm.R;
import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.model.pojo.Track;

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

        HashMap<String,List<Track>> r=mMusicListActivityListener.loadLatestLists();
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        for(Map.Entry<String,List<Track>> list:mMusicListActivityListener.loadLatestLists().entrySet()){


            layout.addView(createView(list.getKey(), list.getValue()));
        }
        m_Scroll.addView(layout,layoutParam);
        return m_Scroll;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
            titleTextView=(TextView)itemView.findViewById(R.id.list_title);
        }
    }
    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        private static  int numberOfTracks = 3;
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
            textView.setText(track.getArtist());
        }



        @Override
        public int getItemCount() {
            return (mTracks == null) ? 0 : mTracks.size();
           // return 3;
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
        btn.setText("Click Me");
        btn.setLayoutParams(params);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LatestTacksFragment", "onClick: "+adapter.getItemCount());
                btn.setText(listName);
                int curSize = adapter.getItemCount();
                shortList.addAll(trackList.subList(3,trackList.size()));

                adapter.notifyDataSetChanged();
                Log.d("LatestTacksFragment", "onClick: "+adapter.getItemCount());

            }
        });

        layout.addView(btn);



        return layout;
    }
}