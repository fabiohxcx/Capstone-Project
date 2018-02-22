package fabiohideki.com.megagenerator.repository;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fabiohideki.com.megagenerator.model.Resultado;

/**
 * Created by hidek on 08/02/2018.
 */

public class ResultsRepository {

    public List<Resultado> listAll(Context context) {

        List<Resultado> results = new ArrayList<>();

        Cursor cursor;

        if (context != null) {

            cursor = context.getContentResolver().query(ResultsContract.ResultEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    ResultsContract.ResultEntry.COLUMN_CONCURSO + " DESC");

            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    Resultado resultado = new Resultado();

                    resultado.setNumero(cursor.getInt(cursor.getColumnIndex(ResultsContract.ResultEntry.COLUMN_CONCURSO)));
                    resultado.setData(cursor.getString(cursor.getColumnIndex(ResultsContract.ResultEntry.COLUMN_DATE)));
                    resultado.setDezenas(cursor.getString(cursor.getColumnIndex(ResultsContract.ResultEntry.COLUMN_NUMBERS)));

                    results.add(resultado);

                }

                cursor.close();

                if (results.size() > 0) {
                    return results;
                }
            }
        }

        return null;
    }


    public Resultado getByNumber(int number, Context context) {

        Cursor cursor;

        Uri uri = ResultsContract.ResultEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(number)).build();

        Log.d("Fabio", "getByNumber: uri:" + uri);

        cursor = context.getContentResolver().query(uri,
                null,
                null,
                null,
                null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                Resultado resultado = new Resultado();
                resultado.setNumero(cursor.getInt(cursor.getColumnIndex(ResultsContract.ResultEntry.COLUMN_CONCURSO)));
                resultado.setData(cursor.getString(cursor.getColumnIndex(ResultsContract.ResultEntry.COLUMN_DATE)));
                resultado.setDezenas(cursor.getString(cursor.getColumnIndex(ResultsContract.ResultEntry.COLUMN_NUMBERS)));

                Log.d("Fabio", "getByNumber: " + resultado.getNumero());

                cursor.close();
                return resultado;

            }

        }

        if (cursor != null)
            cursor.close();
        return null;
    }


}
