package edu.oleg088097.arkanoid.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import java.util.ArrayList;

public class Brick {
    private RectF rect;
    private boolean destroyed;
    private int health;
    private int[] gradColors;
    private final Paint paint;
    private LinearGradient brickGrad;
    private final ArrayList<Integer> colors;
    private final static int blueColor = Color.parseColor("#00ccff");

    public Brick(ArrayList<Integer> colorsSet) {
        rect = new RectF();
        destroyed = false;
        health = 1;
        gradColors = new int[2];
        paint = new Paint();
        colors = colorsSet;
        setGradient();
    }

    public void set(float width, float heigth, float x, float y, int ... hp) {
        rect.set(x, y, x + width, y + heigth);
        if (hp.length == 1) {
            health = hp[0];
        }

        setGradient();
    }

    public RectF getRect() {
        return rect;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void hit() {
        health--;
        if (health == 0) {
            destroyed = true;
            return;
        }

        setGradient();
    }

    public void defend() {
        health++;

        setGradient();
    }

    public void resetState() {
        health = 1;
        destroyed = false;

        setGradient();
    }

    private void setGradient(){
        if (health > colors.size()) {
            gradColors[0] = Color.WHITE;
        } else {
            gradColors[0] = colors.get(health - 1);
        }
        gradColors[1] = 0;

        brickGrad = new LinearGradient(rect.left, rect.top, rect.left, rect.bottom, new int[]{colors.get(0), 0},
                new float[]{0, 1}, Shader.TileMode.MIRROR);
    }

    public void drawBrick(Canvas canvas)
    {
        if (destroyed) {
            return;
        }

        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(blueColor);
        paint.setStrokeWidth(2);
        canvas.drawRect(rect, paint);

        paint.setShader(brickGrad);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(rect.left+2, rect.top+2, rect.right-2, rect.bottom-2, paint);
    }
}
