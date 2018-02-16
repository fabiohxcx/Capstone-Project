package fabiohideki.com.megagenerator.generator.algorithms;

import java.util.Iterator;

import fabiohideki.com.megagenerator.MyApplication;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.generator.model.BetCandidate;

/**
 * Created by hidek on 12/02/2018.
 */

public class CheckSequenceHorizontal implements ValidationAlgorithmsInterface {

    private String reason;

    public CheckSequenceHorizontal() {
        this.reason = MyApplication.getContext().getResources().getString(R.string.reasonSequenceHorizontal);
    }

    @Override
    public boolean execute(BetCandidate betCandidate) {

        Iterator<Integer> iterador = betCandidate.getBalls().iterator();

        while (iterador.hasNext()) {
            int number = iterador.next();
            if (betCandidate.getBalls().contains(number + 1) && betCandidate.getBalls().contains(number + 2)) {
                betCandidate.setRefused(true);
                betCandidate.addReason(reason);
            }

        }

        return betCandidate.isRefused();
    }
}
