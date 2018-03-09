package edu.oleg088097.arkanoid.menu;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import java.util.Locale;

import edu.oleg088097.arkanoid.R;
import edu.oleg088097.arkanoid.util.MusicService;

public class PauseMenuActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case edu.oleg088097.arkanoid.R.id.resumeButton:{
                finish();
                break;
            }
            case edu.oleg088097.arkanoid.R.id.helpButton:{
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
            }
            case edu.oleg088097.arkanoid.R.id.exitButton:{
                getBaseContext().stopService(new Intent(getBaseContext(),
                        MusicService.class));
                finishAffinity();
            }
            case R.id.settingsButton:{
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(edu.oleg088097.arkanoid.R.layout.activity_pause_menu);

        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                String.format(Locale.US, "fonts/%s", "angled_mont.ttf"));

        Button resumeButton = findViewById(edu.oleg088097.arkanoid.R.id.resumeButton);
        resumeButton.setOnClickListener(this);
        resumeButton.setTypeface(typeface);
        Button helpButton = findViewById(edu.oleg088097.arkanoid.R.id.helpButton);
        helpButton.setOnClickListener(this);
        helpButton.setTypeface(typeface);
        Button exitButton = findViewById(edu.oleg088097.arkanoid.R.id.exitButton);
        exitButton.setOnClickListener(this);
        exitButton.setTypeface(typeface);
        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(this);
        settingsButton.setTypeface(typeface);
    }

}
