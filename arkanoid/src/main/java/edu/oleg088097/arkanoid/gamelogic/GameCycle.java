package edu.oleg088097.arkanoid.gamelogic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.view.SurfaceHolder;

class GameCycle extends Thread {
    private final GameActivity.GameView gameView;
    private final SurfaceHolder sHolder;

    GameCycle(GameActivity.GameView controlledView, SurfaceHolder controllerHolder){
        super();
        gameView = controlledView;
        sHolder = controllerHolder;
    }

    @Override
    public void run() {
        final int TICKS_PER_SECOND = 133;
        final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
        final int MAX_FRAMESKIP = 5;

        long next_game_tick = SystemClock.elapsedRealtime();
        int loops;
        float interpolation;

        while (!Thread.interrupted()) {
            loops = 0;
            while (SystemClock.elapsedRealtime() > next_game_tick && loops < MAX_FRAMESKIP) {
                gameView.updateGame();

                next_game_tick += SKIP_TICKS;
                loops++;
            }

            interpolation = (SystemClock.elapsedRealtime() + SKIP_TICKS - next_game_tick) / SKIP_TICKS;

            Canvas canvas = null;
            try {
                canvas = sHolder.lockCanvas(null);

                if (canvas == null)
                    continue;
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                gameView.drawing(canvas, interpolation);
            } finally {
                if (canvas != null) {
                    sHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}