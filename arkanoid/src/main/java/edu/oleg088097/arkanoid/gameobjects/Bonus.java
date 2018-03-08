package edu.oleg088097.arkanoid.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import java.util.ArrayList;

import edu.oleg088097.arkanoid.util.Utils;

public class Bonus {
    public enum BonusType
    {
        PLAYER,
        DIVIDE,
        EXPAND,
        SLOW,
        FIRE
    }

    private static final int colors[] = new int[]{Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.GREEN, Color.RED};
    private static final float[] gradPositions = new float[]{0, 0.5f, 1};
    private static final TextPaint textPaint = new TextPaint();

    private final Paint paint;
    private float letterPosX;
    private float letterPosY;
    private final BonusType effect;
    private RectF rect;
    private RectF drawedRect;
    private final float speed;
    private final int[] gradColors;

    public Bonus(float side, float x, float y, float bonusSpeed){
        float side_2 = side/2;
        rect = new RectF(x-side_2, y-side_2, x+side_2, y+side_2);
        drawedRect = new RectF(x-side_2, y-side_2, x+side_2, y+side_2);
        speed = bonusSpeed;

        int num = Utils.random(5);
        effect = BonusType.values()[num];
        int color = colors[num];
        textPaint.setTextSize(side_2);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);

        gradColors = new int[3];
        gradColors[0] = color;
        gradColors[1] = gradColors[0];
        gradColors[2] = 0;

        letterPosX = rect.left + side*0.1f;
        letterPosY = rect.bottom - side*0.1f;
    }

    public static void setFont(Typeface typeface){
        textPaint.setTypeface(typeface);
    }

    public BonusType getEffect()
    {
        return effect;
    }

    public RectF getRect()
    {
        return rect;
    }

    public void move()
    {
        rect.offset(0, speed);
        letterPosY += speed;
    }

    public void drawBonus(final Canvas canvas, float interpolation)
    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        drawedRect.set(rect);
        drawedRect.offset(0, speed*interpolation);
        canvas.drawRect(drawedRect, paint);

        LinearGradient bonusGrad = new LinearGradient(drawedRect.left, drawedRect.top, drawedRect.left, drawedRect.bottom,
                gradColors, gradPositions, Shader.TileMode.MIRROR);
        paint.setShader(bonusGrad);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(drawedRect, paint);

        char letter[] = new char[1];
        switch (effect)
        {
            case PLAYER:
                letter[0] = 'P';
                break;
            case SLOW:
                letter[0] = 'S';
                break;
            case EXPAND:
                letter[0] = 'E';
                break;
            case DIVIDE:
                letter[0] = 'D';
                break;
            case FIRE:
                letter[0] = 'F';
                break;
            default:
                break;
        }

        canvas.drawText(letter, 0, 1, letterPosX, letterPosY + speed*interpolation, textPaint);
    }

    public static void divideBonus(ArrayList<Ball> balls) {
        int size = balls.size();
        for (int cur = 0; cur < size; cur++) {
            for (int i = 0; i < 2; i++) {
                int incTimes = Utils.random(7);
                int decTimes = Utils.random(7);
                if (incTimes == decTimes) {
                    decTimes += Utils.random(7);
                }

                Ball newBall = new Ball(balls.get(cur));

                for (int j = 0; j < incTimes; j++) {
                    newBall.increaseCos();
                }
                for (int j = 0; j < decTimes; j++) {
                    newBall.decreaseCos();
                }
                balls.add(newBall);
            }
        }
    }
}
