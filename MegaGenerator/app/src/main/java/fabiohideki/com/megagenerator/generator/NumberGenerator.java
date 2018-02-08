package fabiohideki.com.megagenerator.generator;

import java.util.Random;

/**
 * Created by hidek on 07/02/2018.
 */

public class NumberGenerator {

    public static int generate(Random random) {

        if (random == null) random = new Random();

        int numberCandidate = random.nextInt(60) + 1;

        return numberCandidate;
    }

}
