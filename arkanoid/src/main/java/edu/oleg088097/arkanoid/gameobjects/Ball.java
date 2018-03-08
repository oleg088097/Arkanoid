package edu.oleg088097.arkanoid.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;

public class Ball {
    private final static float COS_PI_4 = (float) Math.cos(Math.PI / 4);
    private final static float SIN_PI_4 = (float) Math.sin(Math.PI / 4);
    private final static int blueColor = Color.parseColor("#00ccff");

    private float rightEdge;
    private float cos;
    private float sin;
    private int speed;
    private float x;
    private float y;
    private int radius;
    private int color;
    private final Paint paint;

    public Ball(float ... rEdge) {
        if (rEdge.length == 1) {
            rightEdge = rEdge[0];
        }
        cos = COS_PI_4;
        sin = SIN_PI_4;
        x = -100;
        y = -100;
        color = Color.BLACK;

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    Ball(final Ball original) {
        rightEdge = original.rightEdge;
        cos = original.cos;
        sin = original.sin;
        speed = original.speed;
        x = original.x;
        y = original.y;
        radius = original.radius;
        color = original.color;

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    public void set(int newSpeed, int newRad, float rEdge) {
        rightEdge = rEdge;
        speed = newSpeed;
        x = -100;
        y = -100;
        radius = newRad;
        color = Color.BLACK;
    }

    public void move() {
        x += cos * speed;
        y += sin * speed;

        if (x + radius >= rightEdge) {
            negativeCos();
        }

        if (x - radius <= 0) {
            positiveCos();
        }

        if ((y - radius <= 0)) {
            positiveSin();
        }
    }

    public void moveTo(float tox, float toy) {
        x = tox + radius;
        y = toy + radius;
    }

    public void resetAngleAndPosition() {
        cos = COS_PI_4;
        sin = SIN_PI_4;
        x = 0;
        y = 0;
    }

    public void increaseCos() {
        if (cos > 0.8) {
            return;
        }
        cos += 0.1;
        sin = (float) Math.sqrt(1 - Math.pow(cos, 2));
    }

    public void decreaseCos() {
        if (cos < -0.8) {
            return;
        }
        cos -= 0.1;
        sin = (float) Math.sqrt(1 - Math.pow(cos, 2));
    }

    public void negativeCos() {
        cos = -Math.abs(cos);
    }

    public void negativeSin() {
        sin = -Math.abs(sin);
    }

    public void positiveCos() {
        cos = Math.abs(cos);
    }

    public void positiveSin() {
        sin = Math.abs(sin);
    }

    public void zeroCos() {
        cos = 0;
        sin = 1;
    }

    public void setCos45() {
        cos = COS_PI_4;
        sin = SIN_PI_4;
    }

    public float getCos() {
        return cos;
    }

    public float getSin() {
        return sin;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    public void speedDown() {
        speed /= 1.5;
    }

    public void speedUp() {
        speed *= 1.5;
    }

    public void drawBall(Canvas canvas, float interpolation) {
        float interpolatedX = x + cos * speed * interpolation;
        float interpolatedY = y + sin * speed * interpolation;
        RadialGradient ballGrad = new RadialGradient(interpolatedX, interpolatedY, radius, color, blueColor, Shader.TileMode.MIRROR);
        paint.setShader(ballGrad);

        canvas.drawCircle(interpolatedX, interpolatedY, radius, paint);
    }

    public void setColor(int newColor) {
        color = newColor;
    }

    public void restoreColor() {
        color = Color.BLACK;
    }
}
