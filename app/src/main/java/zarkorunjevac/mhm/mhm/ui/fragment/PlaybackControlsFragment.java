package zarkorunjevac.mhm.mhm.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import zarkorunjevac.mhm.R;
import zarkorunjevac.mhm.mhm.MVP;
import zarkorunjevac.mhm.mhm.model.pojo.Track;

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
    private ImageView mAlbumArt;
    private String mArtUrl;
    private Track mTrack;
    private MVP.ProvidedMusicListActivityOps mMusicListActivityListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playback_controls, container, false);
        mTrack=mMusicListActivityListener.loadTrack();

        mPlayPause = (ImageButton) rootView.findViewById(R.id.play_pause);
        mPlayPause.setEnabled(true);
        mPlayPause.setOnClickListener(mButtonListener);

        mTitle = (TextView) rootView.findViewById(R.id.title);
        mSubtitle = (TextView) rootView.findViewById(R.id.artist);
        mExtraInfo = (TextView) rootView.findViewById(R.id.extra_info);
        mAlbumArt = (ImageView) rootView.findViewById(R.id.album_art);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /// start FullScreenActivity
            }
        });
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
        super.onAttach(context);
        if(context instanceof MVP.ProvidedMusicListActivityOps){
            mMusicListActivityListener=(MVP.ProvidedMusicListActivityOps)context;
        }
    }

    @Override
    public void displayPlayButton() {
        mPlayPause.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_play_arrow_black_36dp));
    }

    @Override
    public void displayPauseButton() {
        mPlayPause.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_pause_black_36dp));
    }
}
