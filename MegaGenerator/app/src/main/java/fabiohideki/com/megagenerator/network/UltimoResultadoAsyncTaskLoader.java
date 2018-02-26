package fabiohideki.com.megagenerator.network;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;

import fabiohideki.com.megagenerator.model.UltimoResultado;
import fabiohideki.com.megagenerator.utils.Utils;
import fabiohideki.com.megagenerator.widget.LastResultWidgetProvider;

/**
 * Created by hidek on 04/02/2018.
 */

public class UltimoResultadoAsyncTaskLoader extends AsyncTaskLoader<UltimoResultado> {

    private TaskCallBack listener;

    public UltimoResultadoAsyncTaskLoader(Context context, TaskCallBack listener) {
        super(context);
        this.listener = listener;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d("Fabio", "onStartLoading: ");

        listener.onStartTask();

    }

    @Override
    public UltimoResultado loadInBackground() {

        String urlAPIMegasena = Utils.getUrlAPIMegasena();

        Log.d("Fabio", "loadInBackground: " + urlAPIMegasena);

        if (urlAPIMegasena != null) {

            try {

                Document apiDoc = Jsoup.connect(urlAPIMegasena)
                        .header("Accept-Encoding", "gzip, deflate")
                        .userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Mobile Safari/537.36")
                        .maxBodySize(0)
                        .timeout(60000)
                        .get();

                Log.d("Fabio", "loadInBackground: " + apiDoc.text());

                String jsonResult = apiDoc.text();

                Gson gson = new Gson();

                UltimoResultado ultimoResultado = gson.fromJson(jsonResult, UltimoResultado.class);

                Log.d("Fabio", "loadInBackground: concurso " + ultimoResultado.getNroConcurso());

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getContext(), LastResultWidgetProvider.class));

                LastResultWidgetProvider.updateLastResultWidgets(getContext(), appWidgetManager, appWidgetIds, ultimoResultado);

                return ultimoResultado;


            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

}
