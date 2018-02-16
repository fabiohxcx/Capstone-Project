package fabiohideki.com.megagenerator.generator.algorithms;

import android.util.Log;

import java.util.List;
import java.util.TreeSet;

import fabiohideki.com.megagenerator.MyApplication;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.generator.model.BetCandidate;
import fabiohideki.com.megagenerator.model.Resultado;

/**
 * Created by hidek on 07/02/2018.
 */

public class CheckInHistory implements ValidationAlgorithmsInterface {

    private List<Resultado> results;
    private String reason6;
    private String reason5;


    public CheckInHistory(List<Resultado> results) {
        this.results = results;

        reason6 = MyApplication.getContext().getResources().getString(R.string.reasonHistory6);
        reason5 = MyApplication.getContext().getResources().getString(R.string.reasonHistory5);
    }

    @Override
    public boolean execute(BetCandidate betCandidate) {

        Log.d("Fabio", "execute: CheckInHistory");

        for (int i = 0; i < results.size(); i++) {

            TreeSet<Integer> intercession = new TreeSet<Integer>(betCandidate.getBalls());
            intercession.retainAll(results.get(i).getDezenasSet());

            String reasonSuffix = ": " + results.get(i).getNumero() + " - " + results.get(i).getData() + " - " + results.get(i).getDezenas();

            if (intercession.size() == 6) {
                betCandidate.setRefused(true);
                betCandidate.addReason(reason6 + reasonSuffix);
            }

            if (betCandidate.getBalls().size() < 11) {
                if (intercession.size() == 5) {
                    betCandidate.setRefused(true);
                    betCandidate.addReason(reason5 + reasonSuffix);
                }
            }

        }

        return betCandidate.isRefused();
    }
}
