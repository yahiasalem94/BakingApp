package com.example.android.bakingapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.Models.RecipeSteps;
import com.example.android.bakingapp.Utils.Constants;
import com.example.android.bakingapp.databinding.FragmentRecipeStepBinding;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeStep extends AppCompatActivity implements ExoPlayer.EventListener, View.OnClickListener {

    private static final String TAG = RecipeStep.class.getSimpleName();

    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private Toolbar toolbar;

    private ArrayList<RecipeSteps> steps;
    private RecipeSteps step;
    private int stepPosition;

    private String videoUrl;
    private String imageUrl;
    private boolean isVideo = false;
    private boolean isImage = false;
    private boolean isInitialized = false;


    FragmentRecipeStepBinding binding;

    public RecipeStep() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        binding = DataBindingUtil.setContentView(this, R.layout.fragment_recipe_step);

        toolbar = findViewById(R.id.step_toolbar);
        setSupportActionBar(toolbar);
        toolbar.bringToFront();

        if (getIntent().getExtras() != null) {
            steps = getIntent().getExtras().getParcelableArrayList(Constants.STEPS_LIST);
            stepPosition = getIntent().getExtras().getInt(Constants.RECIPE_STEP_POSITION);
            step = steps.get(stepPosition);
        }

        toolbar.setTitle("YAHIA");

        videoUrl = step.getVideoUrl();
        imageUrl = step.getImageUrl();

        if (!TextUtils.isEmpty(videoUrl)) isVideo = true;
        if (!TextUtils.isEmpty(imageUrl)) isImage = true;


        if ((!isVideo && isImage) || (!isVideo && !isImage)) {
            Log.d(TAG, "image url is" + " " + imageUrl);
            binding.playerView.setVisibility(View.GONE);
            binding.imageStepRecipe.setVisibility(View.VISIBLE);
        }

        binding.recipeStepName.setText(step.getDescription());
        binding.buttonPrevStep.setOnClickListener(this);
        binding.buttonNextStep.setOnClickListener(this);

        if ( stepPosition == 0 ) {
            binding.buttonPrevStep.setEnabled(false);
        } else if ( stepPosition == steps.size()-1) {
            binding.buttonNextStep.setEnabled(false);
        }

        if (savedInstanceState != null) {
            initialize();
            if (savedInstanceState.containsKey(Constants.VIDEO_POSITION)) {
                mExoPlayer.seekTo(savedInstanceState.getLong(Constants.VIDEO_POSITION));
                mExoPlayer.setPlayWhenReady(true); // start
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (!isInitialized) {
            initialize();
        }
    }

    private void initialize() {
        if (isVideo) {
            Log.d(TAG, "video url is not null");

            // Load the question mark as the background image until the video is loaded
            binding.playerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.question_mark));
            // Initialize the Media Session.
            initializeMediaSession();
            // Initialize the player.
            initializePlayer(Uri.parse(videoUrl));
        } else if (isImage) {
            Log.d(TAG, "image url is not null");
            Picasso.get().load(imageUrl).placeholder(R.drawable.unavailable).into(binding.imageStepRecipe);
        } else {
            binding.imageStepRecipe.setImageResource(R.drawable.unavailable);
        }
        isInitialized = true;
    }

    @Override
    public void onClick(View v) {

        Bundle bundle = new Bundle();
        Intent intent = new Intent(RecipeStep.this, RecipeStep.class);

        switch (v.getId()) {
            case R.id.buttonPrevStep:
                stepPosition--;
                bundle.putInt(Constants.RECIPE_STEP_POSITION, stepPosition);
                bundle.putParcelableArrayList(Constants.STEPS_LIST, steps);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.buttonNextStep:
                stepPosition++;
                bundle.putInt(Constants.RECIPE_STEP_POSITION, stepPosition);
                bundle.putParcelableArrayList(Constants.STEPS_LIST, steps);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSavedInstance");
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false); // pause
            long currentPosition = mExoPlayer.getCurrentPosition();
            outState.putLong(Constants.VIDEO_POSITION, currentPosition);
        }
    }

    /* Local Functions */

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(this, TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            binding.playerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (!videoUrl.isEmpty()) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
            mMediaSession.setActive(false);
        }
    }

    /* ExoPlayer Event Listeners */
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }


    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}
