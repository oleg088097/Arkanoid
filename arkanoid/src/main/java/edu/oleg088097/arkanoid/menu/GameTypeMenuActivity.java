package edu.oleg088097.arkanoid.menu;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import java.util.Locale;

import edu.oleg088097.arkanoid.gamelogic.GameActivity;

public class GameTypeMenuActivity extends AppCompatActivity implements View.OnClickListener{
    public enum GameType
    {
        CLASSIC,
        FAST,
        AGGRESSIVE
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        switch (v.getId()){
            case edu.oleg088097.arkanoid.R.id.classicGameButton:{
                intent.putExtra("gameType", GameType.CLASSIC);
                break;
            }
            case edu.oleg088097.arkanoid.R.id.fastGameButton:{
                intent.putExtra("gameType", GameType.FAST);
                break;
            }
            case edu.oleg088097.arkanoid.R.id.aggressiveGameButton:{
                intent.putExtra("gameType", GameType.AGGRESSIVE);
                break;
            }
        }
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(edu.oleg088097.arkanoid.R.layout.activity_game_type_menu);

        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                String.format(Locale.US, "fonts/%s", "angled_mont.ttf"));

        Button classicGameButton = findViewById(edu.oleg088097.arkanoid.R.id.classicGameButton);
        classicGameButton.setOnClickListener(this);
        classicGameButton.setTypeface(typeface);
        Button fastGameButton = findViewById(edu.oleg088097.arkanoid.R.id.fastGameButton);
        fastGameButton.setOnClickListener(this);
        fastGameButton.setTypeface(typeface);
        Button aggressiveGameButton = findViewById(edu.oleg088097.arkanoid.R.id.aggressiveGameButton);
        aggressiveGameButton.setOnClickListener(this);
        aggressiveGameButton.setTypeface(typeface);
    }

}
