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

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newGameButton:{
                Intent intent = new Intent(this, GameTypeMenuActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.helpButton:{
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.exitButton:{
                getBaseContext().stopService(new Intent(getBaseContext(),
                        MusicService.class));
                finishAffinity();
                break;
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

        setContentView(R.layout.activity_main_menu);

        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                String.format(Locale.US, "fonts/%s", "angled_mont.ttf"));

        Button newGameButton = findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(this);
        newGameButton.setTypeface(typeface);
        Button helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(this);
        helpButton.setTypeface(typeface);
        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
        exitButton.setTypeface(typeface);
        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(this);
        settingsButton.setTypeface(typeface);

        Intent music = new Intent(this, MusicService.class);
        startService(music);
    }
}