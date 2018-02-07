package fabiohideki.com.megagenerator.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fabiohideki.com.megagenerator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneratorFragment extends Fragment {

    @BindView(R.id.card_result)
    CardView cardViewResult;

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

        Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);

        if (cardViewResult.getVisibility() == View.GONE) {
            cardViewResult.setVisibility(View.VISIBLE);
            cardViewResult.startAnimation(slideUp);
        }

    }

}
