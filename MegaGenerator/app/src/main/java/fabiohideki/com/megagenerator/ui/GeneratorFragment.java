package fabiohideki.com.megagenerator.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.generator.BetGenerator;
import fabiohideki.com.megagenerator.generator.NumberGenerator;
import fabiohideki.com.megagenerator.generator.algorithms.CheckInHistory;
import fabiohideki.com.megagenerator.generator.model.BetCandidate;
import fabiohideki.com.megagenerator.model.Resultado;
import fabiohideki.com.megagenerator.repository.ResultsRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneratorFragment extends Fragment {

    @BindView(R.id.card_result)
    CardView cardViewResult;

    @BindView(R.id.tv_result_test)
    TextView tvResult;

    public GeneratorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_generator, container, false);

        ButterKnife.bind(this, rootView);
        // Inflate the layout for this fragment
        return rootView;
    }

    @OnClick(R.id.bt_generate)
    public void generate(View view) {


        int number = NumberGenerator.generate(null);

        List<Integer> lista = new ArrayList<>();

        TreeSet<Integer> balls = BetGenerator.generate(10, lista);

        BetCandidate candidate = new BetCandidate();
        candidate.setBalls(balls);

        ResultsRepository resultsRepository = new ResultsRepository();

        List<Resultado> resultados = resultsRepository.listAll(getContext());

        CheckInHistory checkInHistory = new CheckInHistory(resultados);

        checkInHistory.execute(candidate);

        tvResult.setText(candidate.getBalls().toString() + "\n" +
                candidate.isRefused() + "\n" +
                candidate.getRefusedReasons().toString());

        Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);

        if (cardViewResult.getVisibility() == View.GONE) {
            cardViewResult.setVisibility(View.VISIBLE);
            cardViewResult.startAnimation(slideUp);
        }

    }

}
