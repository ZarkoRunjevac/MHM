package zarkorunjevac.mhm.mhm.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import zarkorunjevac.mhm.R;
import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.model.pojo.Track;
import zarkorunjevac.mhm.mhm.ui.activity.FullScreenPlayerActivity;

/**
 * Created by zarko.runjevac on 4/4/2016.
 */
public class PlaybackControlsFragment extends Fragment
        implements MVP.ProvidedPlaybackControlsFragmentOps{

    private static String TAG=PlaybackControlsFragment.class.getSimpleName();

    private ImageButton mPlayPause;
    private TextView mTitle;
    private TextView mSubtitle;
    private TextView mExtraInfo;
    private CircleImageView mAlbumArt;
    private String mArtUrl;
    private Track mTrack;

    private Context mContext;
    private MVP.ProvidedMusicListActivityOps mMusicListActivityListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playback_controls, container, false);
        Log.d(TAG, "onCreateView: start ");

        mPlayPause = (ImageButton) rootView.findViewById(R.id.play_pause);
        mPlayPause.setEnabled(true);
        mPlayPause.setOnClickListener(mButtonListener);
        mPlayPause.setBackgroundResource(0);

        mTitle = (TextView) rootView.findViewById(R.id.title);
        mSubtitle = (TextView) rootView.findViewById(R.id.artist);
        mExtraInfo = (TextView) rootView.findViewById(R.id.extra_info);
        mAlbumArt = (CircleImageView) rootView.findViewById(R.id.album_art);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /// start FullScreenActivity
                Intent intent=new Intent(mContext, FullScreenPlayerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        Log.d(TAG, "onCreateView: end  ");
        return rootView;
    }

    private final View.OnClickListener mButtonListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mMusicListActivityListener.togglePlayPause();
        }
    };

    @Override
    public void onAttach(Context context) {
        mContext=context;
        super.onAttach(mContext);
        if(context instanceof MVP.ProvidedMusicListActivityOps){
            mMusicListActivityListener=(MVP.ProvidedMusicListActivityOps)mContext;
        }
    }

    @Override
    public void displayPlayButton() {
        if(mPlayPause!=null)
            mPlayPause.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_play));
    }

    @Override
    public void displayPauseButton() {
        if(mPlayPause!=null)
         mPlayPause.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pause));
    }

    @Override
    public void initializeViewFields(Track track) {

            Log.d(TAG, "initializeViewFields: mTitle="+track.getTitle());
            mTitle.setText(track.getTitle());
            Log.d(TAG, "initializeViewFields: mSubTitle="+track.getArtist());
            mSubtitle.setText(track.getArtist());

            Picasso.with(mContext)
                    .load(track.getThumbUrlMedium()).
                    into(mAlbumArt);


    }
}
