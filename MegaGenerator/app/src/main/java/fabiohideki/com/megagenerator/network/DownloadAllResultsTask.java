package fabiohideki.com.megagenerator.network;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.model.Resultado;
import fabiohideki.com.megagenerator.repository.ResultsContract;
import fabiohideki.com.megagenerator.utils.Utils;

/**
 * Created by hidek on 05/02/2018.
 */

public class DownloadAllResultsTask extends AsyncTask<String, String, String> {

    public static final String RESULT_OK = "ok";
    public static final String RESULT_FAIL = "fail";

    private static final String URL_ZIP_FILE = "http://www1.caixa.gov.br/loterias/_arquivos/loterias/D_mgsasc.zip";
    private static final String ZIP_FILE_NAME = "downloaded_results.zip";

    private Context context;
    private URL mURLZipfile;
    private File mDownloadedZip;

    private TextView percentage;

    private TaskCallBack listener;


    public DownloadAllResultsTask(Context context, TextView percentage, TaskCallBack listener) {
        this.context = context;
        this.percentage = percentage;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {

        publishProgress(context.getResources().getString(R.string.downloading));

        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

        try {

            mURLZipfile = new URL(URL_ZIP_FILE);

            String internalPath = context.getFilesDir().getPath().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator + ZIP_FILE_NAME;
            String destinationPath = context.getFilesDir().getPath().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS;

            Log.d("Fabio", internalPath);

            mDownloadedZip = new File(internalPath);

            FileUtils.copyURLToFile(mURLZipfile, mDownloadedZip);
            publishProgress(context.getResources().getString(R.string.downloaded));

            boolean result = Utils.unzipFile(context, destinationPath, mDownloadedZip);

            if (result) {
                publishProgress(context.getResources().getString(R.string.unzip));
                Log.d("Fabio", "unzip OK");

                File htmlFromZip = new File(destinationPath + "/d_megasc.htm");

                List<Resultado> results = readFromHtmlFile(htmlFromZip);

                if (results != null) {

                    publishProgress(" 0 / " + results.size());

                    for (int i = 0; i < results.size(); i++) {

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(ResultsContract.ResultEntry.COLUMN_CONCURSO, results.get(i).getNumero());
                        contentValues.put(ResultsContract.ResultEntry.COLUMN_DATE, results.get(i).getData());
                        contentValues.put(ResultsContract.ResultEntry.COLUMN_NUMBERS, results.get(i).getDezenas());

                        Uri uri = context.getContentResolver().insert(ResultsContract.ResultEntry.CONTENT_URI, contentValues);

                        Log.d("Fabio ", contentValues.toString());

                        publishProgress((i + 1) + " / " + results.size());
                    }

                }


            } else {
                return RESULT_FAIL;
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return RESULT_FAIL;
        } catch (IOException e) {
            e.printStackTrace();
            return RESULT_FAIL;
        }

        return RESULT_OK;
    }

    private List<Resultado> readFromHtmlFile(File htmlFromZip) {

        List<Resultado> results = new ArrayList<>();

        try {
            Document doc = Jsoup.parse(htmlFromZip, "UTF-8");
            Elements row = doc.select("body > table > tbody > tr");

            for (int i = 1; i < row.size(); i++) {
                try {
                    Elements columns = row.get(i).select("td");

                    int number = Integer.parseInt(columns.first().text());
                    String data = columns.get(1).text();
                    String result = columns.get(2).text() + "-" +
                            columns.get(3).text() + "-" +
                            columns.get(4).text() + "-" +
                            columns.get(5).text() + "-" +
                            columns.get(6).text() + "-" +
                            columns.get(7).text();

                    Resultado resultado = new Resultado();
                    resultado.setNumero(number);
                    resultado.setData(data);
                    resultado.setDezenas(result);

                    results.add(resultado);

                    Log.d("Fabio ", number + " - " + data + " - " + result);

                } catch (NumberFormatException e) {
                    //ignore lines without content
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return results;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        percentage.setText(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (s.equals(RESULT_OK)) {
            listener.onTaskCompleted();
        } else if (s.equals(RESULT_FAIL)) {
            listener.onTaskError();
        }
    }
}
