package fabiohideki.com.megagenerator.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.model.Bolas;
import fabiohideki.com.megagenerator.model.UltimoResultado;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.tv_card_concurso)
    TextView mCardConcurso;

    @BindView(R.id.tv_card_concurso_acumulou)
    TextView mCardConcursoAcumulou;

    @BindView(R.id.bola1)
    TextView mBola1;

    @BindView(R.id.bola2)
    TextView mBola2;

    @BindView(R.id.bola3)
    TextView mBola3;

    @BindView(R.id.bola4)
    TextView mBola4;

    @BindView(R.id.bola5)
    TextView mBola5;

    @BindView(R.id.bola6)
    TextView mBola6;

    @BindView(R.id.tv_result_sena)
    TextView mResultSena;

    @BindView(R.id.tv_result_quina)
    TextView mResultaQuina;

    @BindView(R.id.tv_result_quadra)
    TextView mResultQuadra;

    @BindView(R.id.tv_data_prox_concurso)
    TextView mDataProxConcurso;

    @BindView(R.id.tv_valor_prox_concurso)
    TextView mValorProxConcurso;

    @BindString(R.string.card_title_concurso)
    String mTitleConcurso;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MobileAds.initialize(getContext(), getString(R.string.AdMobAppID));

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Bolas bolas = new Bolas(10, 15, 5, 2, 36, 78);

        UltimoResultado ultimoResultado = new UltimoResultado(
                2010,
                bolas,
                "03/02/2018",
                true,
                "R$ 56.000.000,00",
                "06/02/2018",
                "Não houve acertador",
                "106 apostas ganhadoras, R$ 33.705,40",
                "7436 apostas ganhadoras, R$ 686,38"
        );

        setupCard(ultimoResultado);
    }


    private void setupCard(UltimoResultado ultimoResultado) {

        Bolas bolasResult = ultimoResultado.getBolas();

        mCardConcurso.setText(mTitleConcurso + " " + Integer.toString(ultimoResultado.getNroConcurso()));
        mCardConcursoAcumulou.setVisibility(ultimoResultado.isAcumulou() ? View.VISIBLE : View.INVISIBLE);
        mBola1.setText(bolasResult.getBola1String());
        mBola2.setText(bolasResult.getBola2String());
        mBola3.setText(bolasResult.getBola3String());
        mBola4.setText(bolasResult.getBola4String());
        mBola5.setText(bolasResult.getBola5String());
        mBola6.setText(bolasResult.getBola6String());
        mResultSena.setText(ultimoResultado.getSena());
        mResultaQuina.setText(ultimoResultado.getQuina());
        mResultQuadra.setText(ultimoResultado.getQuadra());
        mDataProxConcurso.setText(ultimoResultado.getDataProxConc());
        mValorProxConcurso.setText(ultimoResultado.getEstPremioProxConc());
    }


    @OnClick(R.id.bt_card_share)
    public void submit(View view) {
        Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
    }


}
