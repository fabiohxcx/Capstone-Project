package fabiohideki.com.megagenerator.network;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import fabiohideki.com.megagenerator.model.Resultado;
import fabiohideki.com.megagenerator.repository.ResultsContract;
import fabiohideki.com.megagenerator.utils.Utils;

/**
 * Created by hidek on 06/02/2018.
 */

public class FillMissingResultsTask extends AsyncTask<Integer, String, Boolean> {

    private int mLastResultFromNetwork;
    private Context context;

    public FillMissingResultsTask(Context context, int mLastResultFromNetwork) {
        this.mLastResultFromNetwork = mLastResultFromNetwork;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Integer... strings) {

        Uri uri = ResultsContract.ResultEntry.CONTENT_URI.buildUpon().appendPath(ResultsContract.PATH_LAST).build();

        Cursor cursor = context.getContentResolver().query(uri,
                null,
                null,
                null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();

            int lastResultInDB = cursor.getInt(cursor.getColumnIndex(ResultsContract.ResultEntry.COLUMN_CONCURSO));

            int diff = mLastResultFromNetwork - lastResultInDB;

            if (diff > 0) {

                //http://loterias.caixa.gov.br/wps/portal/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOLNDH0MPAzcDbwMPI0sDBxNXAOMwrzCjA0sjIEKIoEKnN0dPUzMfQwMDEwsjAw8XZw8XMwtfQ0MPM2I02-AAzgaENIfrh-FqsQ9wNnUwNHfxcnSwBgIDUyhCvA5EawAjxsKckMjDDI9FQE-F4ca/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0KO6H80AU71KG7J0072/res/
                // id=buscaResultado/c=cacheLevelPage/=/?timestampAjax=1517964027922&concurso=2010
                String mBaseUrlAPIMegasena = Utils.getUrlAPIMegasena();

                if (mBaseUrlAPIMegasena != null) {

                    for (int i = 1; i <= diff; i++) {
                        Log.d("Fabio", "doInBackground: " + mBaseUrlAPIMegasena + "&concurso=" + (lastResultInDB + i));

                        try {
                            Document apiDoc = Jsoup.connect(mBaseUrlAPIMegasena + "&concurso=" + (lastResultInDB + i))
                                    .header("Accept-Encoding", "gzip, deflate")
                                    .userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Mobile Safari/537.36")
                                    .maxBodySize(0)
                                    .timeout(60000)
                                    .get();

                            String jsonResult = apiDoc.text();

                            Gson gson = new Gson();

                            Resultado resultado = gson.fromJson(jsonResult, Resultado.class);

                            Log.d("Fabio", "doInBackground: resultado " + resultado.getNumero() + " " + resultado.getDezenas());

                            ContentValues contentValues = new ContentValues();
                            contentValues.put(ResultsContract.ResultEntry.COLUMN_CONCURSO, resultado.getNumero());
                            contentValues.put(ResultsContract.ResultEntry.COLUMN_DATE, Utils.convertMillitoDate(resultado.getData()));
                            contentValues.put(ResultsContract.ResultEntry.COLUMN_NUMBERS, resultado.getDezenas());

                            Uri uriInserted = context.getContentResolver().insert(ResultsContract.ResultEntry.CONTENT_URI, contentValues);

                            Log.d("Fabio", "doInBackground: uri inserted " + uriInserted);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }
        }

        return false;
    }


}
