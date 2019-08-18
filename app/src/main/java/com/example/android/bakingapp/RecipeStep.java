package com.example.android.bakingapp;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.Models.RecipeSteps;
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

public class RecipeStep extends Fragment implements ExoPlayer.EventListener, View.OnClickListener {

    private static final String TAG = RecipeStep.class.getSimpleName();

    private static final String TAG_RECIPE_STEP_FRAGMENT = "RecipeStep";

    private static final String RECIPE_STEPS = "recipeStep";
    private static final String RECIPE_STEP_POSITION = "recipeStepPosition";

    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private ArrayList<RecipeSteps> steps;
    private RecipeSteps step;
    private int stepPosition;

    private String videoUrl;
    private String imageUrl;
    private boolean isVideo = false;
    private boolean isImage = false;


    FragmentRecipeStepBinding binding;

    public RecipeStep() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
        if (getArguments() != null) {
            steps = getArguments().getParcelableArrayList(RECIPE_STEPS);
            stepPosition = getArguments().getInt(RECIPE_STEP_POSITION);
            step = steps.get(stepPosition);
        }

        videoUrl = step.getVideoUrl();
        imageUrl = step.getImageUrl();
        if (!videoUrl.isEmpty()) isVideo = true;
        if (!imageUrl.isEmpty()) isImage = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        // Inflate the fragment's layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_step, container, false);

        Log.d(TAG, "video is " + videoUrl);
        Log.d(TAG, "image is " + imageUrl);

        if ((!isVideo && isImage) || (!isVideo && !isImage)) {
            Log.d(TAG, "image url is" + " " + imageUrl);
            binding.playerView.setVisibility(View.GONE);
            binding.imageStepRecipe.setVisibility(View.VISIBLE);
        }

        binding.buttonPrevStep.setOnClickListener(this);
        binding.buttonNextStep.setOnClickListener(this);

        if ( stepPosition == 0 ) {
            binding.buttonPrevStep.setEnabled(false);
        } else if ( stepPosition == steps.size()-1) {
            binding.buttonNextStep.setEnabled(false);
        }

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onViewCreated");

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
    }

    @Override
    public void onClick(View v) {

        RecipeStep fragment = new RecipeStep();
        Bundle bundle = new Bundle();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch (v.getId()) {
            case R.id.buttonPrevStep:
                stepPosition--;
                bundle.putInt(RECIPE_STEP_POSITION, stepPosition);
                bundle.putParcelableArrayList(RECIPE_STEPS, steps);
                fragment.setArguments(bundle);
                transaction.replace(R.id.placeholder, fragment, TAG_RECIPE_STEP_FRAGMENT);
                transaction.commit();
                break;

            case R.id.buttonNextStep:
                stepPosition++;
                bundle.putInt(RECIPE_STEP_POSITION, stepPosition);
                bundle.putParcelableArrayList(RECIPE_STEPS, steps);
                fragment.setArguments(bundle);
                transaction.replace(R.id.placeholder, fragment, TAG_RECIPE_STEP_FRAGMENT);
                transaction.commit();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    /* Local Functions */

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getActivity(), TAG);

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
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            binding.playerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
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
