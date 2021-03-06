package com.example.android.bakemaster.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakemaster.R;
import com.example.android.bakemaster.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

/**
 * This is the class responsible for the creation of the Fragment that contains
 * the list recipe step details.
 */
public class RecipeStepDetailFragment extends Fragment {

    private static final String LOG_TAG = RecipeStepDetailFragment.class.getSimpleName();
    public static final String PLAY_WHEN_READY_KEY = "play";
    public static final String CURRENT_POSITION_KEY = "position";
    public static final String CURRENT_WINDOW_KEY = "window";
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private ImageView playerThumbnail;
    private static boolean playWhenReady;
    private static int currentWindow;
    private static long playbackPosition;
    private String mediaUrl;
    private String thumbnailUrl;
    private static int nextWasClicked;
    private static int previousWasClicked;
    public static int upWasClicked;

    public RecipeStepDetailFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail,
                container, false);

        // Here we restore the position, window, and play when ready state of the player
        // if the screen was rotated.
        if (savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
            playbackPosition = savedInstanceState.getLong(CURRENT_POSITION_KEY);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_KEY);
        }

        // Here we get the step passed by the RecipeStepDetailActivity and we set the
        // data to each view.
        Bundle bundle = getArguments();
        if (bundle != null) {
            Step step = bundle.getParcelable(MainActivity.STEP_KEY);

            // Here we check to see if it is still the same fragment, we only get
            // the values if the fragment is different.
            if (savedInstanceState == null) {
                nextWasClicked = bundle.getInt(RecipeStepDetailActivity.NEXT_KEY);
                previousWasClicked = bundle.getInt(RecipeStepDetailActivity.PREVIOUS_KEY);
            }

            TextView recipeStepInstruction = rootView.findViewById(R.id.recipe_step_instruction_tv);
            recipeStepInstruction.setText(step.getDescription());

            playerView = rootView.findViewById(R.id.player_view);
            mediaUrl = step.getVideoURL();
            thumbnailUrl = step.getThumbnailURL();
            playerThumbnail = rootView.findViewById(R.id.exo_artwork);

            // Here we reset the position, window and play when ready state of the player
            // if the next, previous or up buttons were clicked.
            if (nextWasClicked == RecipeStepDetailActivity.NEXT_VALUE
                    || previousWasClicked == RecipeStepDetailActivity.PREVIOUS_VALUE
                    || upWasClicked == RecipeStepDetailActivity.NO_VALUE) {
                playbackPosition = 0;
                playWhenReady = false;
                currentWindow = 0;
                nextWasClicked = RecipeStepDetailActivity.NO_VALUE;
                previousWasClicked = RecipeStepDetailActivity.NO_VALUE;
                upWasClicked = RecipeStepDetailActivity.DEFAULT_VALUE;
            } else {
                nextWasClicked = RecipeStepDetailActivity.NO_VALUE;
                previousWasClicked = RecipeStepDetailActivity.NO_VALUE;
            }

            // Here we handle the case when the phone is in landscape and we
            // want the video to be fullscreen.
            if (getScreenWidth() < 600f && getActivity().getResources().getConfiguration()
                    .orientation == Configuration.ORIENTATION_LANDSCAPE
                    && !(mediaUrl.isEmpty())) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                        playerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = params.MATCH_PARENT;
                playerView.setLayoutParams(params);
                hideSystemUi();
            } else {
                showSystemUi();
            }
        }

        return rootView;
    }

    /**
     * Here we initialize the player only if the mediaUrl is not empty.
     * If it is we set the player visibility to GONE.
     */
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23 && !(mediaUrl.isEmpty())) {
            initializePlayer(mediaUrl, thumbnailUrl);
            playerView.setVisibility(View.VISIBLE);
        } else {
            playerView.setVisibility(View.GONE);
        }
    }

    /**
     * Here we reinitialize the player only if the mediaUrl is not empty.
     * If it is we set the player visibility to GONE.
     */
    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            if (!(mediaUrl.isEmpty())) {
                initializePlayer(mediaUrl, thumbnailUrl);
                playerView.setVisibility(View.VISIBLE);
            } else {
                playerView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Here we release the player.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    /**
     * Here we release the player.
     */
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    /**
     * Here we save the position, window and play when ready state of the player.
     * @param outState the bundle containing the saved player variables.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Boolean pwr = player.getPlayWhenReady();
        outState.putBoolean(PLAY_WHEN_READY_KEY, pwr);
        long pp = player.getCurrentPosition();
        outState.putLong(CURRENT_POSITION_KEY, pp);
        int cw = player.getCurrentWindowIndex();
        outState.putInt(CURRENT_WINDOW_KEY, cw);
    }



    /**
     * Method used to initialize the player.
     * @param mediaUrl the URL of the video.
     * @param thumbnailUrl the URL of the thumbnail.
     */
    private void initializePlayer(String mediaUrl, String thumbnailUrl) {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            playerView.setPlayer(player);

            if (!(thumbnailUrl.isEmpty())) {
                Picasso.get().load(thumbnailUrl).into(playerThumbnail);
            }

            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);

            Uri uri = Uri.parse(mediaUrl);
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, false, false);
    }

    /**
     * This method is used to build the Media Source for the preparation of the player.
     * @param uri the uri of the source.
     * @return the Media Source in order to prepare the player.
     */
    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("bake-master"))
                .createMediaSource(uri);
    }

    /**
     * Method used to hide the system UI when the phone is in landscape orientation.
     */
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    /**
     * Method used to show the system UI when the phone is again in portrait orientation.
     */
    private void showSystemUi() {
        playerView.setSystemUiVisibility(0);
    }

    /**
     * This method releases the player when it is no longer needed.
     */
    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    /**
     * This method is used to return the shortest screen side density in dp.
     * @return the screen density.
     */
    private float getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;
        float dpHeight = outMetrics.heightPixels / density;
        return Math.min(dpWidth, dpHeight);
    }
}
