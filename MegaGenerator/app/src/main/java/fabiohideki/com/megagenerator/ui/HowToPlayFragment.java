package fabiohideki.com.megagenerator.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.megagenerator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HowToPlayFragment extends Fragment {

    @BindView(R.id.tv_how_to_play_content)
    TextView mTvHowToPlayContent;

    @BindString(R.string.how_to_play_content)
    String mHowToPlayTextContent;

    public HowToPlayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_how_to_play, container, false);
        ButterKnife.bind(this, rootView);

        mTvHowToPlayContent.setText(Html.fromHtml(mHowToPlayTextContent));

        return rootView;
    }

}
