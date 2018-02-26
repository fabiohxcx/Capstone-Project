package fabiohideki.com.megagenerator.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;

import fabiohideki.com.megagenerator.MyApplication;
import fabiohideki.com.megagenerator.model.UltimoResultado;
import fabiohideki.com.megagenerator.utils.Utils;

/**
 * Created by fabio.lagoa on 26/02/2018.
 */

public class UpdateLastResultService extends IntentService {

    private static String packageName = MyApplication.getContext().getPackageName();

    public static final String ACTION_UPDATE_WIDGET_LAST_RESULT = packageName + ".action.update_widget_last_result";

    public UpdateLastResultService() {
        super("UpdateLastResultService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d("Fabio", "UpdateLastResultService onHandleIntent: ");
        if (intent != null) {

            final String action = intent.getAction();

            if (ACTION_UPDATE_WIDGET_LAST_RESULT.equals(action)) {
                handleActionUpdateLastResult();
            }
        }

    }

    private void handleActionUpdateLastResult() {

        Log.d("Fabio", "UpdateLastResultService handleActionUpdateLastResult: ");

        String urlAPIMegasena = Utils.getUrlAPIMegasena();

        Log.d("Fabio", "handleActionUpdateLastResult: " + urlAPIMegasena);

        if (urlAPIMegasena != null) {

            try {
                Document apiDoc = Jsoup.connect(urlAPIMegasena)
                        .header("Accept-Encoding", "gzip, deflate")
                        .userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Mobile Safari/537.36")
                        .maxBodySize(0)
                        .timeout(60000)
                        .get();

                Log.d("Fabio", "handleActionUpdateLastResult: " + apiDoc.text());

                String jsonResult = apiDoc.text();

                Gson gson = new Gson();

                UltimoResultado ultimoResultado = gson.fromJson(jsonResult, UltimoResultado.class);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, LastResultWidgetProvider.class));

                LastResultWidgetProvider.updateLastResultWidgets(this, appWidgetManager, appWidgetIds, ultimoResultado);

                Log.d("Fabio", "handleActionUpdateLastResult: concurso " + ultimoResultado.getNroConcurso());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void startUpdateLastResult(Context context) {

        Intent intent = new Intent(context, UpdateLastResultService.class);
        intent.setAction(ACTION_UPDATE_WIDGET_LAST_RESULT);
        context.startService(intent);

    }


}
