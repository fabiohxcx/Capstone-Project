package fabiohideki.com.megagenerator.generator;

import android.util.Log;

import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * Created by hidek on 07/02/2018.
 */

public class BetGenerator {

    public static TreeSet<Integer> generate(int numberOfBalls, List<Integer> numberToRefuse) {

        if (numberOfBalls > 0) {
            TreeSet<Integer> balls = new TreeSet<Integer>();
            Random random = new Random();

            while (balls.size() < numberOfBalls) {
                int ball = NumberGenerator.generate(random);
                Log.d("Fabio", "generate: " + ball);
                if (numberToRefuse != null) {
                    if (!numberToRefuse.contains(ball)) {
                        balls.add(ball);
                    }
                } else {
                    balls.add(ball);
                }
            }
            return balls;
        }

        return null;
    }

}
