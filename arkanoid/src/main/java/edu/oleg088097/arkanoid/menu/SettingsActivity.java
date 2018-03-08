package edu.oleg088097.arkanoid.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.Locale;

import edu.oleg088097.arkanoid.R;
import edu.oleg088097.arkanoid.util.MusicService;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private MusicService mServ;
    private Button changeStateButton;

    @Override
    public void onClick(View v) {
        mServ.changePlayingState();
        setButtonText();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mServ.setVolumeLevel(seekBar.getProgress());
    }

    private final ServiceConnection sCon = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    private void doBindService() {
        bindService(new Intent(this, MusicService.class), sCon, Context.BIND_AUTO_CREATE);
    }

    private void setButtonText(){
        if (mServ.isPlaying()){
            changeStateButton.setText(getString(R.string.turnOff));
        } else {
            changeStateButton.setText(getString(R.string.turnOn));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(edu.oleg088097.arkanoid.R.layout.activity_settings);

        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                String.format(Locale.US, "fonts/%s", "angled_mont.ttf"));

        changeStateButton = findViewById(edu.oleg088097.arkanoid.R.id.button);
        changeStateButton.setOnClickListener(this);
        changeStateButton.setTypeface(typeface);

        SeekBar seekBar = findViewById(edu.oleg088097.arkanoid.R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);

        ((TextView)findViewById(edu.oleg088097.arkanoid.R.id.tuneTextView)).setTypeface(typeface);

        doBindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setButtonText();
    }
}
