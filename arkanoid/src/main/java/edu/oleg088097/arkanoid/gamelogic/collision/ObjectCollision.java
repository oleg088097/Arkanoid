package edu.oleg088097.arkanoid.gamelogic.collision;

import android.graphics.RectF;
import java.util.ArrayList;
import java.util.Collections;

import edu.oleg088097.arkanoid.gameobjects.Ball;
import edu.oleg088097.arkanoid.gameobjects.Brick;
import edu.oleg088097.arkanoid.gameobjects.Paddle;
import edu.oleg088097.arkanoid.util.Utils;

import static java.lang.Math.pow;

class ObjectCollision {
    enum CollisionPoint {
        LEFT,
        RIGHT,
        TOP,
        TOPLEFT,
        TOPRIGHT,
        BOT,
        BOTLEFT,
        BOTRIGHT,
    }

    static CollisionPoint isIntersect(final Ball ball, final RectF rect) {
        float ballRadius = ball.getRadius();

        if (pow(rect.left - ball.getX(), 2) + pow(rect.bottom - ball.getY(), 2) <= pow(ballRadius, 2)) {
            return CollisionPoint.BOTLEFT;
        }
        if (pow(rect.left - ball.getX(), 2) + pow(rect.top - ball.getY(), 2) <= pow(ballRadius, 2)) {
            return CollisionPoint.TOPLEFT;
        }
        if (pow(rect.right - ball.getX(), 2) + pow(rect.bottom - ball.getY(), 2) <= pow(ballRadius, 2)) {
            return CollisionPoint.BOTRIGHT;
        }
        if (pow(rect.right - ball.getX(), 2) + pow(rect.top - ball.getY(), 2) <= pow(ballRadius, 2)) {
            return CollisionPoint.TOPRIGHT;
        }

        Utils.ArithmeticInterface plus = new Utils.Plus();
        Utils.ArithmeticInterface minus = new Utils.Minus();

        ArrayList<Integer> sides = new ArrayList<>();//TOP, BOT, LEFT, RIGHT
        sides.add(containsYPoints(ball, rect, plus));
        sides.add(containsYPoints(ball, rect, minus));
        sides.add(containsXPoints(ball, rect, plus));
        sides.add(containsXPoints(ball, rect, minus));

        int max = Collections.max(sides);

        if (max > 0) {
            switch (sides.indexOf(max)) {
                case 0:
                    return CollisionPoint.TOP;
                case 1:
                    return CollisionPoint.BOT;
                case 2:
                    return CollisionPoint.LEFT;
                case 3:
                    return CollisionPoint.RIGHT;
                default:
                    break;
            }
        }

        return null;
    }

    static void ballPaddleCollision(Ball ball, Paddle paddle) {
        double paddleLPos = paddle.getRect().left;
        double paddleWidth = paddle.getRect().width();
        double ballPos = ball.getX();

        double first = paddleLPos + paddleWidth * 0.1;
        double second = paddleLPos + paddleWidth * 0.48;
        double third = paddleLPos + paddleWidth * 0.52;
        double fourth = paddleLPos + paddleWidth * 0.9;

        if (ballPos < first) {
            if (Math.abs(ball.getCos()) < 0.5) {
                ball.setCos45();
            }
            ball.negativeCos();
        } else if ((ballPos >= first && ballPos < second) || (ballPos > third && ballPos <= fourth)) {
            if (paddle.getDx() > 0) {
                ball.increaseCos();
            } else if (paddle.getDx() < 0) {
                ball.decreaseCos();
            }
        } else if (ballPos >= second && ballPos <= third) {
            ball.zeroCos();
        } else /*if (ballPos > fourth)*/ {
            if (ball.getCos() == 0) {
                ball.setCos45();
            }
            ball.positiveCos();
        }
        ball.negativeSin();
    }

    static CollisionPoint ballBricksCollision(Ball ball, Brick brick, boolean isFire) {
        CollisionPoint intersectResult = isIntersect(ball, brick.getRect());
        if (intersectResult == null) {
            return null;
        }

        brick.hit();

        if (isFire && brick.isDestroyed()) {
            return null;
        }

        return intersectResult;
    }


    private static int containsYPoints(final Ball ball, final RectF rect, Utils.ArithmeticInterface arithmetic) {
        float COS_PI_3 = (float) Math.cos(Math.PI / 3);
        float SIN_PI_3 = (float) Math.sin(Math.PI / 3);
        float ballRadius = ball.getRadius();
        int ret = 0;
        if (rect.contains(ball.getX(), arithmetic.operation(ball.getY(), ballRadius))) {
            ret = 1;
            if (rect.contains(ball.getX() + ballRadius * COS_PI_3, arithmetic.operation(ball.getY(), ballRadius * SIN_PI_3))) {
                ret = 2;
                if (rect.contains(ball.getX() - ballRadius * COS_PI_3, arithmetic.operation(ball.getY(), ballRadius * SIN_PI_3))) {
                    return 3;
                }
            }
        }
        return ret;
    }

    private static int containsXPoints(final Ball ball, final RectF rect, Utils.ArithmeticInterface arithmetic) {
        float COS_PI_6 = (float) Math.cos(Math.PI / 6);
        float SIN_PI_6 = (float) Math.sin(Math.PI / 6);
        float ballRadius = ball.getRadius();
        int ret = 0;
        if (rect.contains(arithmetic.operation(ball.getX(), ballRadius), ball.getY())) {
            ret = 1;
            if (rect.contains(arithmetic.operation(ball.getX(), ballRadius * COS_PI_6), ball.getY() - ballRadius * SIN_PI_6)) {
                ret = 2;
                if (rect.contains(arithmetic.operation(ball.getX(), ballRadius * COS_PI_6), ball.getY() + ballRadius * SIN_PI_6)) {
                    return 3;
                }
            }
        }
        return ret;
    }

    static void affectBall(Ball ball, ObjectCollision.CollisionPoint intersectResult) {
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
