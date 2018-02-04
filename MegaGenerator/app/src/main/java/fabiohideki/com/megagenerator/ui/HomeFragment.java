package fabiohideki.com.megagenerator.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.victor.loading.rotate.RotateLoading;

import org.parceler.Parcels;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.model.Bolas;
import fabiohideki.com.megagenerator.model.UltimoResultado;
import fabiohideki.com.megagenerator.network.TaskCallBack;
import fabiohideki.com.megagenerator.network.UltimoResultadoAsyncTaskLoader;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<UltimoResultado>, TaskCallBack {

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

    @BindView(R.id.rotateloading)
    RotateLoading rotateLoading;

    @BindString(R.string.card_title_concurso)
    String mTitleConcurso;

    private static final int MEGASEGA_ULTIMO_RESULTADO_LOADER = 22;

    private static final String ON_SAVE_INSTANCE_STATE = "onSaveInstanceState";

    private Context context;

    private final String TAG = getClass().getSimpleName();

    private View rootView;

    private UltimoResultado mUltimoResultado = null;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (this.mUltimoResultado != null) {
            outState.putParcelable(ON_SAVE_INSTANCE_STATE, Parcels.wrap(mUltimoResultado));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);

        context = getContext();

        MobileAds.initialize(context, getString(R.string.AdMobAppID));

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            mUltimoResultado = Parcels.unwrap(savedInstanceState.getParcelable(ON_SAVE_INSTANCE_STATE));

            if (mUltimoResultado != null) {
                setupCard(mUltimoResultado);
            } else {
                getLoaderManager().restartLoader(MEGASEGA_ULTIMO_RESULTADO_LOADER, null, this);
            }

        } else {
            getLoaderManager().restartLoader(MEGASEGA_ULTIMO_RESULTADO_LOADER, null, this);
        }

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
        Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
    }


    @Override
    public Loader<UltimoResultado> onCreateLoader(int id, Bundle args) {

        return new UltimoResultadoAsyncTaskLoader(context, this);
    }

    @Override
    public void onLoadFinished(Loader<UltimoResultado> loader, UltimoResultado ultimoResultado) {
        Log.d(TAG, "onLoadFinished: ");

        if (ultimoResultado != null) {

            this.mUltimoResultado = ultimoResultado;
            setupCard(ultimoResultado);

            this.onTaskCompleted();
        } else {
            onTaskError();
        }
    }

    @Override
    public void onLoaderReset(Loader<UltimoResultado> loader) {

    }


    @Override
    public void onStartTask() {
        Log.d(TAG, "onStartTask: ");
        rotateLoading.start();

    }

    @Override
    public void onTaskCompleted() {
        Log.d(TAG, "onTaskCompleted: ");

        rotateLoading.stop();
    }

    @Override
    public void onTaskError() {

    }
}
