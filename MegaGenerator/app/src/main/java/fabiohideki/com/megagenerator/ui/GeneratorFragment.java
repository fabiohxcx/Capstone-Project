package fabiohideki.com.megagenerator.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.generator.BetGenerator;
import fabiohideki.com.megagenerator.generator.algorithms.CheckInHistory;
import fabiohideki.com.megagenerator.generator.algorithms.CheckSequenceHorizontal;
import fabiohideki.com.megagenerator.generator.model.BetCandidate;
import fabiohideki.com.megagenerator.model.Resultado;
import fabiohideki.com.megagenerator.repository.ResultsRepository;

public class GeneratorFragment extends Fragment {

    @BindView(R.id.card_result)
    CardView mCardViewResult;

    @BindView(R.id.tv_result_test)
    TextView mTvResult;

    @BindView(R.id.spin_bets)
    Spinner mSpinnerBets;

    @BindView(R.id.spin_numbers_per_bet)
    Spinner mSpinnerNumbersPerBet;

    @BindView(R.id.cb_refuse_history)
    CheckBox mCheckBoxRefuseHistory;

    @BindView(R.id.cb_refuse_sequence)
    CheckBox mCheckBoxRefuseSequence;

    @BindView(R.id.pb_generator_screen)
    ProgressBar mProgressBar;

    private int mBets;
    private int mNumberPerBets;
    private boolean mCheckHistory;

    public GeneratorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_generator, container, false);
        ButterKnife.bind(this, rootView);

        List<String> list = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            list.add(Integer.toString(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerBets.setAdapter(dataAdapter);

        return rootView;
    }

    @OnClick(R.id.bt_generate)
    public void generate(View view) {

        mProgressBar.setVisibility(View.VISIBLE);

        mBets = Integer.parseInt((String) mSpinnerBets.getSelectedItem());
        mNumberPerBets = Integer.parseInt((String) mSpinnerNumbersPerBet.getSelectedItem());
        mCheckHistory = mCheckBoxRefuseHistory.isChecked();

        mTvResult.setText("");

        for (int i = 0; i < mBets; i++) {

            List<Integer> lista = new ArrayList<>();

            TreeSet<Integer> balls = BetGenerator.generate(mNumberPerBets, lista);

            BetCandidate candidate = new BetCandidate();
            candidate.setBalls(balls);

            ResultsRepository resultsRepository = new ResultsRepository();

            List<Resultado> resultados = resultsRepository.listAll(getContext());

            if (mCheckHistory) {
                CheckInHistory checkInHistory = new CheckInHistory(resultados);
                checkInHistory.execute(candidate);
            }

            if (mCheckBoxRefuseSequence.isChecked()) {
                CheckSequenceHorizontal checkSequenceHorizontal = new CheckSequenceHorizontal();
                checkSequenceHorizontal.execute(candidate);
            }

            mTvResult.append(candidate.getBalls().toString() + "\n" +
                    candidate.isRefused() + "\n" +
                    candidate.getRefusedReasons().toString() + "\n\n");

        }

        mCardViewResult.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

    }

}
