package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView position, duration;
    SeekBar seekBar;
    ImageView btRew, btPlay, btPause, btFf;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variable
        position = findViewById(R.id.position);
        duration = findViewById(R.id.duration);
        seekBar = findViewById(R.id.seek_bar);
        btRew = findViewById(R.id.bt_rew);
        btPlay = findViewById(R.id.bt_play);
        btPause = findViewById(R.id.bt_pause);
        btFf = findViewById(R.id.bt_ff);

        // Initialize media player
        mediaPlayer = MediaPlayer.create(this, R.raw.music);

        // Initialize runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                // Set progress on seekbar
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                // Handler post delay for 0.5 second
                handler.postDelayed(this,500);
            }
        };

        // Get duration of music
        int musicDuration = mediaPlayer.getDuration();
        // int  to millisecond
        String sDuration = convertFormat(musicDuration);
        // Set duration on text view
        duration.setText(sDuration);

        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiding play btn
                btPlay.setVisibility(View.GONE);
                // Showing pause btn
                btPause.setVisibility(View.VISIBLE);
                // Start music
                mediaPlayer.start();
                // Set max on seekbar
                seekBar.setMax(mediaPlayer.getDuration());
                // Start handler
                handler.postDelayed(runnable, 0);
            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiding pause btn
                btPause.setVisibility(View.GONE);
                // Show play btn
                btPlay.setVisibility(View.VISIBLE);
                // pause music
                mediaPlayer.pause();
                // Stop handler
                handler.removeCallbacks(runnable);
            }
        });

        btFf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get current position
                int currentPosition = mediaPlayer.getCurrentPosition();
                // Get current duration
                int musicDuration = mediaPlayer.getDuration();
                if (mediaPlayer.isPlaying() && (musicDuration != currentPosition)){
                    // Fast forward 5s
                    currentPosition += 5000;
                    // Set new current position on text view
                    position.setText(convertFormat(currentPosition));
                    // Set progress on seek bar
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
                }
            }
        });

        btRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get current position
                int currentPosition = mediaPlayer.getCurrentPosition();
                if (mediaPlayer.isPlaying() && currentPosition > 5000){
                    // Rewind 5s
                    currentPosition -= 5000;
                    // SEt new current position
                    position.setText(convertFormat(currentPosition));
                    // Set progress on seek bar
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress);
                }
                position.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btPause.setVisibility(View.GONE);
                btPlay.setVisibility(View.VISIBLE);
                mediaPlayer.seekTo(0);
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int musicDuration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(musicDuration),
                TimeUnit.MILLISECONDS.toSeconds(musicDuration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(musicDuration)));
    }
}