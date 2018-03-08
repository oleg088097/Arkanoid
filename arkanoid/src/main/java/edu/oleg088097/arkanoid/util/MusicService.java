package edu.oleg088097.arkanoid.util;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import edu.oleg088097.arkanoid.R;

public class MusicService extends Service  implements MediaPlayer.OnErrorListener {
    private final IBinder mBinder = new ServiceBinder();

    private int volumeLevel;
    private int maxVolume;
    private int length;
    private MediaPlayer mPlayer;

    public class ServiceBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        volumeLevel = 100;
        maxVolume = 101;
        length = 0;

        mPlayer = MediaPlayer.create(this, R.raw.fonemusic);
        mPlayer.setOnErrorListener(this);

        if (mPlayer != null) {
            mPlayer.setLooping(true);
            mPlayer.setVolume(volumeLevel, volumeLevel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPlayer.start();
        return START_STICKY;
    }

    public void changePlayingState() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            length = mPlayer.getCurrentPosition();
        }
        else if (!mPlayer.isPlaying()) {
            mPlayer.seekTo(length);
            mPlayer.start();
        }
    }

    public boolean isPlaying(){
        return mPlayer.isPlaying();
    }

    public void setVolumeLevel(int newLevel) {
        volumeLevel = newLevel;
        float log1=(float)(Math.log(maxVolume-volumeLevel)/Math.log(maxVolume));
        mPlayer.setVolume(1-log1, 1-log1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        return false;
    }
}