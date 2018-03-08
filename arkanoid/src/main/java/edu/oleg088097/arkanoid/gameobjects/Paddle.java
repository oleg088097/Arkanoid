package edu.oleg088097.arkanoid.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Paddle {
    private int initialX;
    private int initialY;
    private int rightEdge;
    private RectF rect;
    private RectF drawedRect;
    private float dx = 0;
    private final Paint paintGray;
    private final Paint paintBlue ;

    public Paddle() {
        rect = new RectF();
        drawedRect = new RectF();
        paintGray = new Paint();
        paintGray.setColor(Color.parseColor("#666666"));
        paintBlue = new Paint();
        paintBlue.setColor(Color.parseColor("#00ccff")); //text_blue in decimal form
    }

    public void set(float width, float heigth, int initX, int initY, int rEdge) {
        rect.set(initX, initY, initX + width, initY + heigth);
        rightEdge = rEdge;
        initialX = initX;
        initialY = initY;
    }

    public void setDx(float x) {
        dx = x;
    }

    public float getDx() {
        return dx;
    }

    public void move() {
        if (isEdge()) {
            return;
        }

        rect.offset(dx, 0);
    }

    public RectF getRect() {
        return rect;
    }

    public void resetState() {
        rect.offsetTo(initialX, initialY);
    }

    public void expand() {
        float x = rect.centerX();
        float width = rect.width();
        rect.set(x - width, rect.top, x + width, rect.bottom);
        if (rect.right > rightEdge) {
            rect.offset(rightEdge - rect.right, 0);
        } else if (rect.left < 0) {
            rect.offset(-rect.left, 0);
        }
    }

    public void compress() {
        float x = rect.centerX();
        float width_4 = rect.width() / 4;
        rect.set(x - width_4, rect.top, x + width_4, rect.bottom);
    }

    private boolean isEdge(){
        float x = rect.left + dx;
        return (x < 0) || (x > rightEdge - rect.width());
    }

    public void drawPaddle(Canvas canvas, float interpolation) {
        if (isEdge()) {
            interpolation = 0;
        }

        drawedRect.set(rect);
        drawedRect.offset(dx * interpolation, 0);

        paintGray.setStrokeWidth(2);
        paintGray.setStyle(Paint.Style.FILL_AND_STROKE);

        canvas.drawRoundRect(drawedRect, 10, 10, paintGray);

        paintBlue.setStrokeWidth(2);
        paintBlue.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(drawedRect.left+2, drawedRect.top+2, drawedRect.right-2, drawedRect.bottom-2, 10, 10, paintBlue);
        canvas.drawRect(drawedRect.left+drawedRect.width()/2.23f, drawedRect.top+2, drawedRect.right-drawedRect.width()/2.23f,
                drawedRect.bottom-2, paintBlue);

        paintGray.setStyle(Paint.Style.FILL);
        canvas.drawRect(drawedRect.left+drawedRect.width()/2.23f, drawedRect.top, drawedRect.right-drawedRect.width()/2.23f,
                drawedRect.bottom, paintGray);

        canvas.drawRect(drawedRect.left+drawedRect.width()/2.088f, drawedRect.top+1, drawedRect.right-drawedRect.width()/2.088f,
                drawedRect.bottom-1, paintBlue);
    }
}
