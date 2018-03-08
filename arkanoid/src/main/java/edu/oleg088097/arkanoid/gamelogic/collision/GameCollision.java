package edu.oleg088097.arkanoid.gamelogic.collision;

import android.graphics.RectF;
import java.util.ArrayList;

import edu.oleg088097.arkanoid.gameobjects.Ball;
import edu.oleg088097.arkanoid.gameobjects.Bonus;
import edu.oleg088097.arkanoid.gameobjects.Brick;
import edu.oleg088097.arkanoid.gameobjects.Paddle;
import edu.oleg088097.arkanoid.util.Utils;

import static edu.oleg088097.arkanoid.gamelogic.GameActivity.BRICKS_COLUMNS;
import static edu.oleg088097.arkanoid.gamelogic.GameActivity.BRICKS_LINES;


public class GameCollision {
    public enum IntersectionResult {
        STOP,
        VICTORY,
        ACTIV_BONUS,
        RM_BONUS
    }

    private final int BOTTOM_EDGE;
    private final float brickWidth;
    private final float bricksOffsetX;
    private final float brickHeight;
    private final float bricksOffsetY;
    private final float bonusSpeed;
    private RectF bonusPlace;

    public GameCollision(int edge, float width, float offX, float height, float offY, float speed) {
        BOTTOM_EDGE = edge;
        brickWidth = width;
        bricksOffsetX = offX;
        brickHeight = height;
        bricksOffsetY = offY;
        bonusSpeed = speed;
    }

    public IntersectionResult checkIntersection(ArrayList<Brick> bricks, ArrayList<Ball> balls, Paddle paddle, Bonus currentBonus, boolean isFire, int activeBonusTime) {
        if (removeFallenBalls(balls) == 1) {
            return IntersectionResult.STOP;
        }

        boolean isAllBricksDestroyed = true;
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                isAllBricksDestroyed = false;
                break;
            }
        }
        if (isAllBricksDestroyed) {
            return IntersectionResult.VICTORY;
        }

        for (Ball ball : balls) {
            if (ObjectCollision.isIntersect(ball, paddle.getRect()) != null) {
                ObjectCollision.ballPaddleCollision(ball, paddle);
            }

            int column = (int) Math.floor((ball.getX() - bricksOffsetX) / brickWidth);
            int line = (int) Math.floor((ball.getY() - bricksOffsetY) / brickHeight);

            for (int i = Math.max(line - 1, 0); i < Math.min(line + 1, BRICKS_LINES); i++) {
                for (int j = Math.max(column - 1, 0); j < Math.min(column + 1, BRICKS_COLUMNS); j++) {
                    Brick brick = bricks.get(i * BRICKS_COLUMNS + j);
                    if (!brick.isDestroyed()) {
                        ObjectCollision.CollisionPoint collisionPoint = ObjectCollision.ballBricksCollision(ball, brick, isFire);
                        affectBall(ball, collisionPoint);

                        setBonusPlace(brick, activeBonusTime, currentBonus);
                    }
                }
            }
        }


        if (currentBonus != null && RectF.intersects(paddle.getRect(), currentBonus.getRect())) {
            return IntersectionResult.ACTIV_BONUS;
        }
        if (currentBonus != null && currentBonus.getRect().top >= BOTTOM_EDGE) {
            return IntersectionResult.RM_BONUS;
        }

        return null;
    }

    private void setBonusPlace(Brick brick, int activeBonusTime, Bonus currentBonus) {
        if (brick.isDestroyed()) {
            if (Utils.random(6) == 0 && activeBonusTime == 0 && currentBonus == null) {
                bonusPlace = brick.getRect();
            }
        }
    }

    public RectF getBonusPlace() {
        RectF place = bonusPlace;
        bonusPlace = null;
        return place;
    }

    private int removeFallenBalls(ArrayList<Ball> balls) {
        for (int num = 0; num < balls.size(); num++) {
            Ball ball = balls.get(num);
            if (ball.getY() + ball.getRadius() > BOTTOM_EDGE) {
                if (balls.size() == 1) {
                    return 1;
                }
                balls.remove(num);
                num--;
            }
        }
        return 0;
    }

    static private void affectBall(Ball ball, ObjectCollision.CollisionPoint intersectResult) {
        if (intersectResult == null) {
            return;
        }
        switch (intersectResult) {
            case BOT: {
                ball.positiveSin();
                break;
            }
            case TOP: {
                ball.negativeSin();
                break;
            }
            case LEFT: {
                ball.negativeCos();
                break;
            }
            case RIGHT: {
                ball.positiveCos();
                break;
            }
            case BOTRIGHT: {
                if (ball.getSin() < 0) {
                    ball.positiveSin();
                }
                if (ball.getCos() < 0) {
                    ball.positiveCos();
                }
                break;
            }
            case TOPRIGHT: {
                if (ball.getSin() > 0) {
                    ball.negativeCos();
                }
                if (ball.getCos() < 0) {
                    ball.positiveCos();
                }
                break;
            }
            case BOTLEFT: {
                if (ball.getSin() < 0) {
                    ball.positiveSin();
                }
                if (ball.getCos() > 0) {
                    ball.negativeCos();
                }
                break;
            }
            case TOPLEFT: {
                if (ball.getSin() > 0) {
                    ball.negativeCos();
                }
                if (ball.getCos() > 0) {
                    ball.negativeCos();
                }
            }
            default:
                break;
        }
    }
}
