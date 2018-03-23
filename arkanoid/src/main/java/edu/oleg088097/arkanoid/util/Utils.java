package edu.oleg088097.arkanoid.util;

import android.graphics.Color;
import java.util.ArrayList;
import java.util.Random;

public class Utils {
    static private final Random rand = new Random();

    static public ArrayList<Integer> generateColors(){
        ArrayList<Integer> colors = new ArrayList<>(125);
        int baseR = 255;
        int baseG = 255;
        int baseB = 255;

        int divisor = (int)Math.cbrt(30) + 1;
        int stepR = (baseR) / divisor;
        int stepG = (baseG) / divisor;
        int stepB = (baseB) / divisor;

        for(int r = 0; r <= baseR ; r += stepR)
        {
            for(int g = 0; g <= baseG ; g += stepG)
            {
                for(int b = 0; b <= baseB ; b += stepB)
                {
                    colors.add(Color.argb(255,r,g,b));
                }
            }
        }

        return colors;
    }

    static public int random(int bound){
        return rand.nextInt(bound);
    }

    public interface ArithmeticInterface {
        float operation(float a, float b);
    }

    static public class Plus implements ArithmeticInterface {
        @Override
        public float operation(float a, float b) {
            return a+b;
        }
    }

    static public class Minus implements ArithmeticInterface {
        @Override
        public float operation(float a, float b) {
            return a-b;
        }
    }
}
