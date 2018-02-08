package fabiohideki.com.megagenerator.generator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by hidek on 07/02/2018.
 */

public class BetCandidate {

    private TreeSet<Integer> balls;
    private boolean refused;
    private List<String> refusedReasons;

    public BetCandidate() {
        this.balls = new TreeSet<>();
        refused = false;
        refusedReasons = new ArrayList<>();
    }

    public void addReason(String reason) {
        refusedReasons.add(reason);
    }

    public TreeSet<Integer> getBalls() {
        return balls;
    }

    public void setBalls(TreeSet<Integer> balls) {
        this.balls = balls;
    }

    public boolean isRefused() {
        return refused;
    }

    public void setRefused(boolean refused) {
        this.refused = refused;
    }

    public List<String> getRefusedReasons() {
        return refusedReasons;
    }

    public void setRefusedReasons(List<String> refusedReasons) {
        this.refusedReasons = refusedReasons;
    }
}
