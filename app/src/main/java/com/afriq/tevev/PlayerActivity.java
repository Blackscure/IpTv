package com.afriq.tevev;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.chip.Chip;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PlayerActivity extends AppCompatActivity {
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
    }

 */

    com.google.android.exoplayer2.ui.PlayerView playerView;
    SimpleExoPlayer player;
    boolean shouldAutoPlay;
    RelativeLayout playerBar;
    boolean playerBarShown;
    TextView nameText;
    TextView actionText;
    de.hdodenhof.circleimageview.CircleImageView circleImageView;
    ProgressBar progressBar;

    Boolean extened;
    Boolean issound;
    LinearLayout nameofchannel;
    Animation slidetoleft;
    Animation fromDown;
    Animation slidetoRight;
    Animation fromDownFast;
    ImageView sound;
    SeekBar seekBar;

    ImageView tv;
    ImageView movies;
    private AudioManager audioManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        tv = findViewById(R.id.tv);
        movies = findViewById(R.id.movie);
        nameText = findViewById(R.id.item_name);
        actionText = findViewById(R.id.action_name);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        circleImageView = findViewById(R.id.channelLogo);
        playerView = findViewById(R.id.player_view);
        playerBar = findViewById(R.id.player_bar);
        seekBar = findViewById(R.id.seekBar);
        nameofchannel = findViewById(R.id.name_of_channel);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        seekBar.setMax(audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBar.setProgress(audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC));
        sound = findViewById(R.id.sound);

        extened = false;
        playerBarShown = true;
        issound = true;


        slidetoleft = AnimationUtils.loadAnimation(this, R.anim.righttoleft);
        fromDown = AnimationUtils.loadAnimation(this, R.anim.downgoingup);
        slidetoRight = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        fromDownFast = AnimationUtils.loadAnimation(this, R.anim.downgoingup_fast);
        shouldAutoPlay = true;

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        String actionName = intent.getStringExtra("actionName");
        String iconurl = intent.getStringExtra("iconUrl");
        nameText.setText(name);
        actionText.setText(actionName);

        if (iconurl.isEmpty()) {
            circleImageView.setImageResource(R.drawable.live_tv);
        } else {
            Glide.with(getApplicationContext()).load(iconurl).into(circleImageView);
        }


        movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MovieList.class));
                finish();
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PlaylistAdapter mAdapter;
                final AlertDialog dialogBuilder = new AlertDialog.Builder(PlayerActivity.this).create();
                LayoutInflater inflater = PlayerActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_recycler, null);
                dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                RecyclerView recyclerViewDialog = dialogView.findViewById(R.id.dialogRecycler);
                recyclerViewDialog.setHasFixedSize(true);
                recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerViewDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                    }
                });
                mAdapter = new PlaylistAdapter(getApplicationContext());
                recyclerViewDialog.setAdapter(mAdapter);
                SharedPreferences pref = getSharedPreferences("tv", MODE_PRIVATE);
                String channelString = pref.getString("channelList", "");

                try {
                    InputStream is = new ByteArrayInputStream(channelString.getBytes("UTF-8"));
                    M3UParser parser = new M3UParser();
                    M3UPlaylist playlist = parser.parseFile(is);
                    mAdapter.update(playlist.getPlaylistItems());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PlayerActivity.this, "Sorry we could not get your channel list because: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
                dialogBuilder.setView(dialogView);
                WindowManager.LayoutParams wlp = dialogBuilder.getWindow().getAttributes();
                wlp.gravity = Gravity.TOP | Gravity.LEFT;
                wlp.x = 0;
                wlp.y = 0;
                dialogBuilder.show();
            }
        });

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setVisibility(View.VISIBLE);
                seekBar.requestFocus();
                issound = true;
                //GET THE CURRENT VOLUME PROGRAMMATICALLY
                //MAP THE CURRENT VOLUME TO A 0 TO 100 RANGE OF INTERGER
                //SET THE MAPPED SOUND VALUE TO THE SEEK BAR............
                //SET THE SEEK BAR ON SEEK LISTENER
                //FIRE THE SEEK BAR ON SEEK LISTENER


            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                //AFTER THE PROGRESS HAS CHANGED...CHECK WHICH IS ACTIVE...SOUND OR LIGHT
                if (issound) {//the sound button was clicked
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                } else if (!issound) {//the light button was clicked
                    Toast.makeText(PlayerActivity.this, "Still in development", Toast.LENGTH_SHORT).show();
                   /* Settings.System.putInt(getApplicationContext().getContentResolver(),
                            Settings.System.SCREEN_BRIGHTNESS, progress);*/
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        try {
            initializePlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.primaryColor));
        }

        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                // Note that system bars will only be "visible" if none of the
                // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // TODO: The system bars are visible. Make any desired
                    // adjustments to your UI, such as showing the action bar or
                    // other navigational controls.
                    playerBar.setVisibility(View.VISIBLE);
                    playerBar.startAnimation(fromDownFast);
                } else {
                    // TODO: The system bars are NOT visible. Make any desired
                    // adjustments to your UI, such as hiding the action bar or
                    // other navigational controls.
                    playerBar.setVisibility(View.GONE);

                }
            }
        });


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                //View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    //implement a touch listener
    boolean isTouched = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("TOUCHED:", "onTouchEvent: ");
        if (isTouched == false) {
            showSystemUI();
            isTouched = true;
        } else {
            hideSystemUI();
            isTouched = false;
        }
        isTouched = true;
        return true;


    }

    private void initializePlayer() throws IOException {
        final Intent intent = getIntent();
        String itemUrl = intent.getStringExtra("itemUrl"); // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory =
                new DefaultHttpDataSourceFactory(Util.getUserAgent(getApplicationContext(), "app-name"));
        // Create a progressive media source pointing to a stream uri.
        Uri progressiveUri = Uri.parse(itemUrl);
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(progressiveUri);
// Create a player instance.
        player = new SimpleExoPlayer.Builder(getApplicationContext()).build();
        //here i can check for the intent if it contains movie info....if it does...set use of controller to be true
        if (intent.hasExtra("tvChannel")) {
            //is playing a tv stream....so make the play button only visible
            playerView.hideController();
            playerView.setUseController(false);
            playerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
                @Override
                public void onVisibilityChange(int visibility) {
                    if (visibility == 0) {
                        playerView.hideController();
                    }
                }
            });
        } else if (!intent.hasExtra("newTask")) {//movie mode....but is not a new task....so can load the recent movie
            //load the period of the movie
            //is playing a movie stream....so save movie on pause
            SharedPreferences lastViewed = getSharedPreferences("lastViewed", MODE_PRIVATE);
            if (lastViewed.contains("last")) {
                final long pos = Long.parseLong(lastViewed.getString("last", ""));
                final AlertDialog dialogBuilder = new AlertDialog.Builder(PlayerActivity.this).create();
                LayoutInflater inflater = PlayerActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_resume_movie, null);
                dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView mov = dialogView.findViewById(R.id.movie_text);
                Chip resume = dialogView.findViewById(R.id.resume);
                Chip restart = dialogView.findViewById(R.id.start);
                String name = intent.getStringExtra("name");
                String actionName = intent.getStringExtra("actionName");
                String iconurl = intent.getStringExtra("iconUrl");
                mov.setText("You were watching " + name);
                resume.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(String.valueOf(pos), "onClick: ");
                        player.seekTo(pos);
                        dialogBuilder.dismiss();
                    }
                });
                restart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                    }
                });
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(true);
                dialogBuilder.show();


            }


        }


        player.addListener(new Player.EventListener() {
            private static final String TAG = "PLAYER LISTENER";

            @Override
            public void onTimelineChanged(Timeline timeline, int reason) {

            }

            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == PlaybackStateCompat.STATE_PAUSED) {
                    Log.d(TAG, "Pause clicked");

                }

                if (playbackState == PlaybackStateCompat.STATE_SKIPPING_TO_NEXT) {
                    Log.d(TAG, "Next clicked");
                }

                if (playbackState == PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS) {
                    Log.d(TAG, "Previous clicked");
                }

                switch (playbackState) {
                    case ExoPlayer.STATE_READY:
                        progressBar.setVisibility(View.GONE);
                        break;
                    case ExoPlayer.STATE_BUFFERING:
                        progressBar.setVisibility(View.VISIBLE);

                        break;

                    case Player.STATE_ENDED:
                        startActivity(new Intent(getApplicationContext(),MovieList.class));
                        finish();
                        Log.i("PLAYER ENDED", "onPlayerStateChanged: ");
                        break;
                    case Player.STATE_IDLE:
                        break;
                }
                if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED ||
                        !playWhenReady) {

                    playerView.setKeepScreenOn(false);
                } else { // STATE_IDLE, STATE_ENDED
                    // This prevents the screen from getting dim/lock
                    playerView.setKeepScreenOn(true);
                }
            }

            @Override
            public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {

            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
// Prepare the player with the media source.
        player.prepare(mediaSource);
        player.setPlayWhenReady(shouldAutoPlay);
        playerView.setPlayer(player);

    }

    private void releasePlayer() {
        if (player != null) {
            Intent intent = getIntent();
            String itemUrl = intent.getStringExtra("itemUrl");
            String name = intent.getStringExtra("name");
            String actionName = intent.getStringExtra("actionName");
            String iconurl = intent.getStringExtra("iconUrl");
            SharedPreferences preferences2 = getSharedPreferences("lastViewed", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences2.edit();
            editor.putString("itemName", name);
            editor.putString("iconUrl", iconurl);
            editor.putString("itemUrl", itemUrl);
            editor.putString("actionName", actionName);
            if (!getIntent().hasExtra("tvChannel")) {
                editor.putString("last", String.valueOf(player.getCurrentPosition()));
                Log.i(String.valueOf(player.getCurrentPosition()), "releasePlayer: ");
            }
            editor.apply();
            player.setPlayWhenReady(false);
            player.clearVideoSurface();
            player.release();
            player = null;

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MovieList.class));
        finish();

    }


    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            try {
                initializePlayer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}