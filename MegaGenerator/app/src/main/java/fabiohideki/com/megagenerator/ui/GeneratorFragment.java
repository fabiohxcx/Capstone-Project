package fabiohideki.com.megagenerator.ui;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
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
    private View rootView;

    private List<BetCandidate> candidatesPassed = new ArrayList<>();
    private List<BetCandidate> candidatesRefused = new ArrayList<>();

    public GeneratorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_generator, container, false);
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

        GeneratorTask generatorTask = new GeneratorTask();

        generatorTask.execute();


    }

    private class GeneratorTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mCardViewResult.setVisibility(View.GONE);
            mTvResult.setText("");
        }

        @Override
        protected String doInBackground(String... strings) {

            mBets = Integer.parseInt((String) mSpinnerBets.getSelectedItem());
            mNumberPerBets = Integer.parseInt((String) mSpinnerNumbersPerBet.getSelectedItem());
            mCheckHistory = mCheckBoxRefuseHistory.isChecked();


            StringBuilder stringBuilder = new StringBuilder();

            candidatesPassed = new ArrayList<>();
            candidatesRefused = new ArrayList<>();

            int counter = 0;
            while (counter < mBets) {
                //create Bet Candidate with Random Balls
                TreeSet<Integer> balls = BetGenerator.generate(mNumberPerBets, null);
                BetCandidate candidate = new BetCandidate();
                candidate.setBalls(balls);

                //Fetch History Results
                ResultsRepository resultsRepository = new ResultsRepository();
                List<Resultado> resultados = resultsRepository.listAll(getContext());

                //Check Balls with Algorithms
                if (mCheckHistory) {
                    CheckInHistory checkInHistory = new CheckInHistory(resultados);
                    checkInHistory.execute(candidate);
                }

                if (mCheckBoxRefuseSequence.isChecked()) {
                    CheckSequenceHorizontal checkSequenceHorizontal = new CheckSequenceHorizontal();
                    checkSequenceHorizontal.execute(candidate);
                }

                if (!candidate.isRefused()) {
                    candidatesPassed.add(candidate);
                    counter++;

                    stringBuilder.append(candidate.getBalls().toString() + "\n" +
                            candidate.isRefused() + "\n" +
                            candidate.getRefusedReasons().toString() + "\n\n");

                    Log.d("Fabio", "passed: " + candidate.getBalls().toString());
                } else {
                    candidatesRefused.add(candidate);
                    Log.d("Fabio", "refused: " + candidate.getBalls().toString());
                }

            }

            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            LinearLayout generatedBetsList = (LinearLayout) rootView.findViewById(R.id.ll_bet_items_generated);
            generatedBetsList.removeAllViews();

            for (int i = 0; i < candidatesPassed.size(); i++) {
                BetCandidate betCandidate = candidatesPassed.get(i);

                View itemGenerated = getActivity().getLayoutInflater().inflate(R.layout.item_bet_generated, null);

                TextView textViewTitleItem = itemGenerated.findViewById(R.id.tv_title_number_bet_generated);
                textViewTitleItem.append("" + (i + 1));
                LinearLayout linearLayoutItemGenerated = itemGenerated.findViewById(R.id.ll_balls_generated);


                Iterator<Integer> iterador = betCandidate.getBalls().iterator();

                while (iterador.hasNext()) {
                    int number = iterador.next();

                    View ball = getActivity().getLayoutInflater().inflate(R.layout.ball, (ViewGroup) itemGenerated, false);
                    ((TextView) ball).setText("" + number);
                    linearLayoutItemGenerated.addView(ball);

                }

                generatedBetsList.addView(itemGenerated);
            }

            //mTvResult.setText(result);
            mCardViewResult.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }


}
