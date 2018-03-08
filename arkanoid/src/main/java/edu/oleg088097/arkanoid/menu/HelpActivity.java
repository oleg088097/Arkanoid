package edu.oleg088097.arkanoid.menu;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import java.util.Locale;

public class HelpActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(edu.oleg088097.arkanoid.R.layout.activity_help);

        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                String.format(Locale.US, "fonts/%s", "angled_mont.ttf"));


        TextView helpTextView = findViewById(edu.oleg088097.arkanoid.R.id.helpTextView);
        helpTextView.setTypeface(typeface);
        helpTextView.append("Arkanoid\n");
        helpTextView.append("Use right and left third of the screen to move paddle and central third to pause\n");
        helpTextView.append("Crash all blocks and don't let ball fall down\n");
        helpTextView.append("Notice, that blocks have different strength\n");
        helpTextView.append("The color of the block serves as an indicator of its strength\n");
        helpTextView.append("Black blocks are weakest\n");
        helpTextView.append("Sometimes when the block breaks, the bonus drops from it\n");
        helpTextView.append("There are 4 types of bonuses - 1) extra life, 2) triplication the number of balls,\n");
        helpTextView.append("3) doubling the width of the paddle, 4) slowing the flight of balls.\n");
        helpTextView.append("Each type marked with his own color and letter\n");
        helpTextView.append("1) P \n");
        helpTextView.append("2) D \n");
        helpTextView.append("3) E \n");
        helpTextView.append("4) S \n");
        helpTextView.append("Good luck\n");

        TextView gratitudeTextView = findViewById(edu.oleg088097.arkanoid.R.id.gratitudeTextView);
        gratitudeTextView.setTypeface(typeface);

    }
}


