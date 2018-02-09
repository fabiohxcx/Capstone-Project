package fabiohideki.com.megagenerator.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.victor.loading.rotate.RotateLoading;

import java.io.File;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.model.UltimoResultado;
import fabiohideki.com.megagenerator.network.FillMissingResultsTask;
import fabiohideki.com.megagenerator.network.TaskCallBack;
import fabiohideki.com.megagenerator.network.UltimoResultadoAsyncTaskLoader;
import fabiohideki.com.megagenerator.utils.Utils;

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

    @BindView(R.id.bt_retry)
    ImageButton mBtRetry;

    @BindView(R.id.cardview_homefragment)
    CardView cardView;

    @BindString(R.string.card_title_concurso)
    String mTitleConcurso;

    @BindString(R.string.network_error)
    String mNetworkErrorLabel;

    @BindString(R.string.retry)
    String mRetryLabel;

    @BindString(R.string.checkout_last_result)
    String mUltimoResultadoLabel;

    @BindString(R.string.share_via)
    String mCompartilharVia;

    @BindString(R.string.permission_denied)
    String mPermissionDenied;

    @BindString(R.string.no_winners)
    String mNoWinners;

    @BindString(R.string.winners_bets)
    String mWinnerBets;

    @BindString(R.string.today)
    String mToday;

    @BindString(R.string.tomorrow)
    String mTomorrow;

    @BindString(R.string.days)
    String mDays;

    private static final int MEGASEGA_ULTIMO_RESULTADO_LOADER = 22;

    private static final int REQUEST = 112;

    private Context context;

    private View rootView;

    private UltimoResultado mUltimoResultado = null;

    private static String TAG = "Fabio";

    private Loader<UltimoResultado> mAsyncTaskLoader;

    public HomeFragment() {
        Log.d(TAG, "HomeFragment: ");
        // Required empty public constructor
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

        Log.d(TAG, "HomeFragment: onCreateView");

        restartLoader();
        Log.d(TAG, "HomeFragment: onCreateView restartLoader()");

        mBtRetry.setVisibility(View.INVISIBLE);

        return rootView;
    }

    private void setupCard(UltimoResultado ultimoResultado) {

        String[] bolas = ultimoResultado.getResultado().split("-");

        mCardConcurso.setText(mTitleConcurso + " " + Integer.toString(ultimoResultado.getNroConcurso()) + " (" + Utils.convertMillitoDate(ultimoResultado.getDataConcurso()) + ")");
        mCardConcursoAcumulou.setVisibility(ultimoResultado.isAcumulou() == 1 ? View.VISIBLE : View.INVISIBLE);
        mBola1.setText(bolas[0]);
        mBola2.setText(bolas[1]);
        mBola3.setText(bolas[2]);
        mBola4.setText(bolas[3]);
        mBola5.setText(bolas[4]);
        mBola6.setText(bolas[5]);


        if (ultimoResultado.getGanhadoresSena() == 0) {
            mResultSena.setText(mNoWinners);
        } else {
            mResultSena.setText(ultimoResultado.getGanhadoresSena() + " " + mWinnerBets + " " + Utils.decimalFormat(Double.toString(ultimoResultado.getValorSena())));
        }

        if (ultimoResultado.getGanhadoresQuina() == 0) {
            mResultaQuina.setText(mNoWinners);
        } else {
            mResultaQuina.setText(ultimoResultado.getGanhadoresQuina() + " " + mWinnerBets + " " + Utils.decimalFormat(Double.toString(ultimoResultado.getValorQuina())));
        }

        if (ultimoResultado.getGanhadoresQuadra() == 0) {
            mResultQuadra.setText(mNoWinners);
        } else {
            mResultQuadra.setText(ultimoResultado.getGanhadoresQuadra() + " " + mWinnerBets + " " + Utils.decimalFormat(Double.toString(ultimoResultado.getValorQuadra())));
        }

        int diffDays = Utils.diffBetweenDayAndToday(Utils.convertMillitoDate(ultimoResultado.getDataProxConc()));

        if (diffDays != -1) {
            if (diffDays == 0) {
                mDataProxConcurso.setText(Utils.convertMillitoDate(ultimoResultado.getDataProxConc()) + " (" + mToday + ")");
            } else if (diffDays == 1) {
                mDataProxConcurso.setText(Utils.convertMillitoDate(ultimoResultado.getDataProxConc()) + " (" + mTomorrow + ")");
            } else {
                mDataProxConcurso.setText(Utils.convertMillitoDate(ultimoResultado.getDataProxConc()) + " (" + diffDays + " " + mDays + ")");
            }
        }

        mValorProxConcurso.setText(Utils.decimalFormat(Double.toString(ultimoResultado.getEstPremioProxConc())));

    }

    @OnClick(R.id.bt_card_share)
    public void submit(View view1) {

        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(context, PERMISSIONS)) {
                requestPermissions(PERMISSIONS, REQUEST);
            } else {
                shareLastResult();
            }
        } else {
            shareLastResult();
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    shareLastResult();
                } else {
                    Toast.makeText(context, mPermissionDenied, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void shareLastResult() {
        Bitmap bm = Utils.screenShot(getActivity().findViewById(android.R.id.content).getRootView());
        File file = Utils.saveBitmap(bm, "megasena.png");

        Log.i("Fabio", "filepath: " + file.getAbsolutePath());
        Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mUltimoResultadoLabel);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, mCompartilharVia));
    }

    @OnClick(R.id.bt_retry)
    public void retry(View view) {
        Log.d(TAG, "HomeFragment: retry");
        restartLoader();
    }


    @Override
    public Loader<UltimoResultado> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "HomeFragment: onCreateLoader");
        return new UltimoResultadoAsyncTaskLoader(context, this);
    }

    @Override
    public void onLoadFinished(Loader<UltimoResultado> loader, UltimoResultado
            ultimoResultado) {

        Log.d(TAG, "HomeFragment: onLoadFinished");

        if (ultimoResultado != null) {

            this.mUltimoResultado = ultimoResultado;
            setupCard(ultimoResultado);

            checkMissingResults(mUltimoResultado);

            this.onTaskCompleted();
        } else {
            onTaskError();
        }
    }

    private void checkMissingResults(UltimoResultado mUltimoResultado) {
        Log.d(TAG, "HomeFragment: checkMissingResults");
        FillMissingResultsTask missingResultsTask = new FillMissingResultsTask(context, mUltimoResultado.getNroConcurso());
        missingResultsTask.execute();

    }

    @Override
    public void onLoaderReset(Loader<UltimoResultado> loader) {
        Log.d(TAG, "HomeFragment: onLoaderReset");
    }


    @Override
    public void onStartTask() {
        Log.d(TAG, "HomeFragment: onStartTask");
        mBtRetry.setVisibility(View.INVISIBLE);

        if (mUltimoResultado == null) {
            rotateLoading.start();
            mAsyncTaskLoader.forceLoad();
        } else {
            mAsyncTaskLoader.deliverResult(mUltimoResultado);
        }

    }

    @Override
    public void onTaskCompleted() {
        Log.d(TAG, "HomeFragment: onTaskCompleted");

        rotateLoading.stop();
    }

    @Override
    public void onTaskError() {
        Log.d(TAG, "HomeFragment: onTaskError");
        rotateLoading.stop();

        Snackbar snackbar = Snackbar.make(rootView, mNetworkErrorLabel, Snackbar.LENGTH_LONG);

        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        TextView tvText = (TextView) (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
        TextView tvAction = (TextView) (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_action);

        Typeface font = Typer.set(context).getFont(Font.ROBOTO_MEDIUM);
        tvText.setTypeface(font);

        Typeface font2 = Typer.set(context).getFont(Font.ROBOTO_BOLD);
        tvAction.setTypeface(font2);

        snackbar.setAction(mRetryLabel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "HomeFragment: snackbar onClick");
                restartLoader();
            }
        });

        mBtRetry.setVisibility(View.VISIBLE);

        snackbar.show();

    }

    private void restartLoader() {
        Log.d(TAG, "HomeFragment: restartLoader");
        mAsyncTaskLoader = getLoaderManager().restartLoader(MEGASEGA_ULTIMO_RESULTADO_LOADER, null, HomeFragment.this);
    }

}
