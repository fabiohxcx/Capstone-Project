package fabiohideki.com.megagenerator.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import fabiohideki.com.megagenerator.model.UltimoResultado;

/**
 * Created by hidek on 04/02/2018.
 */

public class UltimoResultadoAsyncTaskLoader extends AsyncTaskLoader<UltimoResultado> {

    private TaskCallBack listener;
    private Context context;
    private final String TAG = getClass().getSimpleName();

    private static final String URL_MEGASENA = "http://loterias.caixa.gov.br/wps/portal/loterias/landing/megasena/";

    private String mUrlAPIMegasena;

    public UltimoResultadoAsyncTaskLoader(Context context, TaskCallBack listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(TAG, "onStartLoading: ");

        //TODO Loading indicator visible
        listener.onStartTask();

        forceLoad();
    }

    @Override
    public UltimoResultado loadInBackground() {

        mUrlAPIMegasena = getUrlAPIMegasena();

        Log.d(TAG, "loadInBackground: " + mUrlAPIMegasena);

        if (mUrlAPIMegasena != null) {

            try {

                Document apiDoc = Jsoup.connect(mUrlAPIMegasena)
                        .header("Accept-Encoding", "gzip, deflate")
                        .userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Mobile Safari/537.36")
                        .maxBodySize(0)
                        .timeout(60000)
                        .get();

                Log.d(TAG, "loadInBackground: " + apiDoc.text());

                String jsonResult = apiDoc.text();

                Gson gson = new Gson();

                UltimoResultado ultimoResultado = gson.fromJson(jsonResult, UltimoResultado.class);

                Log.d(TAG, "loadInBackground: concurso " + ultimoResultado.getNroConcurso());


                return ultimoResultado;


            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    private String getUrlAPIMegasena() {

        try {
            Document docWebPageMegasena = Jsoup.connect(URL_MEGASENA).get();

            String baseURL = docWebPageMegasena.select("head > base").attr("href");
            String urlBuscarResultado = docWebPageMegasena.select("#urlBuscarResultado").attr("value");

            if (baseURL != null && urlBuscarResultado != null) {

                Log.d("Fabio", "getUrlAPIMegasena: " + baseURL + urlBuscarResultado + "?timestampAjax=" + System.currentTimeMillis());

                return baseURL + urlBuscarResultado + "?timestampAjax=" + System.currentTimeMillis();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return null;

    }

}
