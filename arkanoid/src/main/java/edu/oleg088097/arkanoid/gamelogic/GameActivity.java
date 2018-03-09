
package edu.oleg088097.arkanoid.gamelogic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;

import edu.oleg088097.arkanoid.gamelogic.collision.GameCollision;
import edu.oleg088097.arkanoid.gameobjects.Ball;
import edu.oleg088097.arkanoid.gameobjects.Bonus;
import edu.oleg088097.arkanoid.gameobjects.Brick;
import edu.oleg088097.arkanoid.gameobjects.Paddle;
import edu.oleg088097.arkanoid.menu.GameTypeMenuActivity;
import edu.oleg088097.arkanoid.menu.PauseMenuActivity;
import edu.oleg088097.arkanoid.util.Utils;

public class GameActivity extends AppCompatActivity {
    static public final int BRICKS_LINES = 5;
    static public final int BRICKS_COLUMNS = 7;

    private TextView livesTextView;
    private TextView levelTextView;
    private GameView gameView;
    private Typeface typeface;
    private SurfaceHolder sHolder;
    private int livesAmount;
    private int level;
    private int BOTTOM_EDGE;
    private float bonusSpeed;
    private float brickWidth;
    private float bricksOffsetX;
    private float brickHeight;
    private float bricksOffsetY;
    private GameCycle gameCycle;

    private void drawPanel() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                livesTextView.setText(String.format(getString(edu.oleg088097.arkanoid.R.string.lives), livesAmount));
                levelTextView.setText(String.format(getString(edu.oleg088097.arkanoid.R.string.level), level + 1));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(edu.oleg088097.arkanoid.R.layout.activity_game);

        typeface = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                String.format(Locale.US, "fonts/%s", "angled_mont.ttf"));

        gameView = new GameView(getApplicationContext(),
                (GameTypeMenuActivity.GameType)getIntent().getExtras().get("gameType"));
        gameView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT));
        gameView.setZOrderOnTop(true);

        addContentView(gameView, gameView.getLayoutParams());

        livesTextView = findViewById(edu.oleg088097.arkanoid.R.id.livesTextView);
        livesTextView.setTypeface(typeface);

        levelTextView = findViewById(edu.oleg088097.arkanoid.R.id.levelTextView);
        levelTextView.setTypeface(typeface);

        Bonus.setFont(typeface);
    }

    class GameView extends SurfaceView implements SurfaceHolder.Callback {
        boolean gameOver;
        boolean gameWon;
        boolean gameStarted;
        float paddleSpeed;
        int width;
        int height;
        int [] touchIDs;

        ArrayList<Ball> balls = new ArrayList<>();
        final Paddle paddle;
        ArrayList<Brick> bricks = new ArrayList<>(BRICKS_LINES* BRICKS_COLUMNS);
        Paint paint;
        final ArrayList<Integer> colors;
        Bonus currentBonus;
        Bonus.BonusType activeBonusType;
        Integer activeBonusTime;
        final GameTypeMenuActivity.GameType gameType;
        GameCollision gameCollision;

        GameView(Context context, GameTypeMenuActivity.GameType gType ) {
            super(context);
            sHolder = getHolder();
            sHolder.addCallback(this);
            sHolder.setFormat(PixelFormat.TRANSPARENT);
            level = 0;
            gameOver = false;
            gameWon = false;
            gameStarted = false;
            paddle = new Paddle();
            livesAmount = 3;
            currentBonus = null;
            activeBonusType = null;
            activeBonusTime = 0;
            gameType = gType;
            paint = new Paint();
            paint.setTypeface(typeface);
            touchIDs = new int[]{-1,-1,-1};
            colors = Utils.generateColors();

            balls.add(new Ball());
            for (int i = 0; i < BRICKS_COLUMNS *BRICKS_LINES; i++) {
                bricks.add(new Brick(colors));
            }

            prepareGame();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            gameCollision = new GameCollision(BOTTOM_EDGE, brickWidth, bricksOffsetX, brickHeight, bricksOffsetY);

            gameCycle = new GameCycle(gameView, sHolder);
            gameCycle.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }

        @Override
        protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
            height = MeasureSpec.getSize(heightMeasureSpec);
            width = MeasureSpec.getSize(widthMeasureSpec);

            BOTTOM_EDGE = height;

            paddle.set(width/6.4f, height/36, width / 2, (int) (height * 0.9), width);

            int ballRadius = (int)Math.ceil(height/72);
            int ballSpeed = (int)Math.ceil(height/80);
            float initX = paddle.getRect().left + paddle.getRect().width() * 0.7f;
            float initY = paddle.getRect().top - 2*ballRadius;
            for (Ball ball : balls){
                ball.set(ballSpeed, ballRadius, width);
                ball.moveTo(initX, initY);
            }

            setGameType();

            brickWidth = width/(BRICKS_COLUMNS+1);
            bricksOffsetX = brickWidth/2;
            brickHeight = height/(4.8f*BRICKS_LINES);
            bricksOffsetY = brickHeight*1.5f;

            for (int i = 0; i < BRICKS_LINES; i++) {
                for (int j = 0; j < BRICKS_COLUMNS; j++) {
                    bricks.get(i* BRICKS_COLUMNS +j).set(brickWidth, brickHeight,
                            j * brickWidth + bricksOffsetX, i * brickHeight + bricksOffsetY);
                }
            }

            bonusSpeed = height/160;
            paint.setTextSize(width/40);
            livesTextView.setTextSize(height/70);
            livesTextView.setTextColor(Color.WHITE);
            levelTextView.setTextSize(height/70);
            levelTextView.setTextColor(Color.WHITE);

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    for (int i = 0; i < 3; i++) {
                        if (touchIDs[i] == -1) {
                            touchIDs[i] = event.getPointerId(event.getActionIndex());
                            break;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    int pIndex = event.getActionIndex();
                    if (pIndex < 0 || pIndex >= event.getPointerCount()){
                        return true;
                    }
                    float x = event.getX(pIndex);
                    if (x > width / 3 && x < 2 * width / 3) {
                        if (!gameStarted) {
                            gameView.startGame();
                        } else {
                            pauseGame();
                        }
                    }
                    for (int i = 0; i < 3; i++) {
                        if (touchIDs[i] == event.getPointerId(event.getActionIndex())) {
                            System.arraycopy(touchIDs, i+1, touchIDs, i, touchIDs.length-(i+1));
                            touchIDs[2] = -1;
                            break;
                        }
                    }
                    break;
            }

            for (int i = 2; i >= 0; i--) {
                if (touchIDs[i] != -1) {
                    float x = event.getX(event.findPointerIndex(touchIDs[i]));
                    if (x < width / 3) {
                        paddle.setDx(-paddleSpeed);
                    } else if (x > 2 * width / 3) {
                        paddle.setDx(paddleSpeed);
                    }

                    return true;
                }
            }

            paddle.setDx(0);
            return true;
        }

        void drawing(Canvas canvas, float interpolation) {
            if (gameOver) {
                finishGame(canvas, "Your luck is over. Start form the beginning.", -1);
            } else if (gameWon) {
                finishGame(canvas, "It was easy. Next one will be harder. ", level + 1);
            } else {
                for (Ball ball : balls) {
                    ball.drawBall(canvas, gameStarted? interpolation : 0);
                }

                paddle.drawPaddle(canvas, interpolation);

                for (Brick i : bricks) {
                    i.drawBrick(canvas);
                }

                if (currentBonus != null) {
                    currentBonus.drawBonus(canvas, interpolation);
                }
            }

            drawPanel();
        }

        void updateGame(){
            if (activeBonusTime != 0)
            {
                activeBonusTime--;
                if (activeBonusTime == 0)
                {
                    removeBonus();
                }
            }
            moveObjects();

            boolean isFire = activeBonusTime != 0 && activeBonusType == Bonus.BonusType.FIRE;
            GameCollision.IntersectionResult collisionResult = gameCollision.checkIntersection(bricks, balls, paddle, currentBonus, isFire, activeBonusTime);

            RectF bonusPlace = gameCollision.getBonusPlace();
            if (bonusPlace != null) {
                currentBonus = new Bonus(height / 24, bonusPlace.centerX(), bonusPlace.centerY(), bonusSpeed);
            }

            if (collisionResult == null){
                return;
            }

            switch (collisionResult) {
                case STOP:
                    stopGame();
                    break;
                case VICTORY:
                    victory();
                    break;
                case ACTIV_BONUS:
                    activateBonus();
                    break;
                case RM_BONUS:
                    currentBonus = null;
                    break;
            }
        }

        private void finishGame(Canvas canvas, String message, int level) {
            paint.setColor(Color.WHITE);

            canvas.translate(width / 2, height / 2);

            canvas.drawText(message, -paint.measureText(message) / 2, 0, paint);
            if (level != -1) {
                canvas.drawText("Level", -paint.measureText("Level") / 2, 15, paint);
                canvas.drawText(Integer.toString(level), paint.measureText("Level") / 2 + 10, 15, paint);
            }
        }

        void moveObjects() {
            paddle.move();
            if (gameStarted) {
                for (Ball ball : balls) {
                    ball.move();
                }
            } else {
                float initX = paddle.getRect().left + paddle.getRect().width() * 0.7f;
                float initY = paddle.getRect().top - (float)Math.ceil(height/36);
                for (Ball ball : balls) {
                    ball.moveTo(initX, initY);
                }
            }
            if (currentBonus != null) {
                currentBonus.move();
            }
        }

        void setGameType() {
            switch (gameType) {
                case AGGRESSIVE:
                case FAST: {
                    for (Ball ball : balls) {
                        ball.speedUp();
                    }
                    paddleSpeed = width/80;
                    break;
                }
                default:
                    paddleSpeed = width/160;
                    break;
            }
        }

        void prepareGame() {
            for (int num = 1; num < balls.size(); num++) {
                balls.remove(num);
            }
            balls.get(0).resetAngleAndPosition();
            paddle.resetState();

            for (Brick it : bricks) {
                it.resetState();
            }

            int difficulty = level * 5;
            while (difficulty != 0) {
                bricks.get(Utils.random(bricks.size() - 1)).defend();
                difficulty--;
            }

            if (gameType == GameTypeMenuActivity.GameType.AGGRESSIVE) {
                Bonus.divideBonus(balls);
                Bonus.divideBonus(balls);
            }
        }

        void startGame() {
            if (paddle.getDx() > 0) {
                balls.get(0).setCos45();
                balls.get(0).positiveCos();
            } else if (paddle.getDx() < 0) {
                balls.get(0).setCos45();
                balls.get(0).negativeCos();
            }
            gameOver = false;
            gameWon = false;
            gameStarted = true;
        }

        void pauseGame() {
            gameCycle.interrupt();
            Intent intent = new Intent(GameActivity.super.getApplicationContext(), PauseMenuActivity.class);
            startActivity(intent);
        }

        void stopGame() {
            currentBonus = null;
            gameStarted = false;
            if (activeBonusTime != 0) {
                activeBonusTime = 1;
            }
            if (livesAmount == 1) {
                gameOver = true;
                level = 0;
                livesAmount = 3;
                prepareGame();
            } else {
                livesAmount -= 1;
                balls.get(0).resetAngleAndPosition();
                paddle.resetState();
            }
        }

        void victory() {
            currentBonus = null;
            gameWon = true;
            level++;
            gameStarted = false;
            prepareGame();
        }

        void activateBonus() {
            switch (currentBonus.getEffect()) {
                case PLAYER:
                    livesAmount++;
                    break;
                case SLOW:
                    activeBonusType = Bonus.BonusType.SLOW;
                    activeBonusTime = 1000;
                    for (Ball ball : balls) {
                        ball.speedDown();
                    }
                    break;
                case EXPAND:
                    activeBonusType = Bonus.BonusType.EXPAND;
                    activeBonusTime = 1000;
                    paddle.expand();
                    break;
                case DIVIDE:
                    Bonus.divideBonus(balls);
                    break;
                case FIRE:
                    activeBonusType = Bonus.BonusType.FIRE;
                    activeBonusTime = 1000;
                    for (Ball ball : balls) {
                        ball.setColor(Color.RED);
                    }
                    break;
                default:
                    break;
            }
            currentBonus = null;
        }

        private void removeBonus() {
            switch (activeBonusType)
            {
                case SLOW:
                {
                    for (Ball ball : balls)
                    {
                        ball.speedUp();
                    }
                    break;
                }
                case EXPAND:
                {
                    paddle.compress();
                    break;
                }
                case FIRE:
                {
                    for (Ball ball : balls)
                    {
                        ball.restoreColor();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }
}



