package example.tutorial.githubupload.mvvm.githubrepo1;


import android.annotation.SuppressLint;
import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class ViewHolder extends RecyclerView.ViewHolder {

    SimpleExoPlayer simpleExoPlayer;
    PlayerView playerView;
    private BandwidthMeter bandwidthMeter;
    private TrackSelector trackSelector;
    private TrackSelection.Factory trackSelectionFactory;
    private SimpleExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private ExtractorsFactory extractorsFactory;
    private DefaultBandwidthMeter defaultBandwidthMeter;
    private MediaSource mediaSource;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setData(Application application, String title, String videoUrl) {
        TextView textView = itemView.findViewById(R.id.item_name);
        playerView = itemView.findViewById(R.id.exoplayerView);
        textView.setText(title);
        try {

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(application).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(application);
            Uri videoUri = Uri.parse(videoUrl);
            DefaultHttpDataSourceFactory defaultHttpDataSourceFactory = new DefaultHttpDataSourceFactory("Videos");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoUri, defaultHttpDataSourceFactory, extractorsFactory, null, null);
            playerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(false);
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
