package fabiohideki.com.megagenerator.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*      Insert Code

        ContentValues contentValues = new ContentValues();

        contentValues.put(ResultsContract.ResultEntry.COLUMN_CONCURSO, 1);
        contentValues.put(ResultsContract.ResultEntry.COLUMN_DATE, "05/02/2018");
        contentValues.put(ResultsContract.ResultEntry.COLUMN_NUMBERS, "01-02-03-04-05-06");

        Uri uri = getContentResolver().insert(ResultsContract.ResultEntry.CONTENT_URI, contentValues);
*/

/*
            Fetch datas
            AsyncTask asyncTask = new AsyncTask<Object, Object, Cursor>() {

            @Override
            protected Cursor doInBackground(Object... objects) {

                try {
                 // to match with id Uri uri = ResultsContract.ResultEntry.CONTENT_URI.buildUpon().appendPath("1").build();

                    Cursor cursor = getContentResolver().query(ResultsContract.ResultEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                    return cursor;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);

                while (cursor.moveToNext()) {
                    Toast.makeText(SplashActivity.this, cursor.getString(cursor.getColumnIndex(ResultsContract.ResultEntry.COLUMN_NUMBERS)), Toast.LENGTH_SHORT).show();
                }

            }
        };

        asyncTask.execute();*/

/*        AsyncTask asyncTask = new AsyncTask<Object, Object, Integer>() {

            @Override
            protected Integer doInBackground(Object... objects) {

                try {

                    Uri uri = ResultsContract.ResultEntry.CONTENT_URI.buildUpon().appendPath("1").build();

                    Log.d("Fabio", "doInBackground: " + uri);

                    int delete = getContentResolver().delete(uri, null, null);

                    return delete;

                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }

            }

            @Override
            protected void onPostExecute(Integer delete) {
                super.onPostExecute(delete);

                Toast.makeText(SplashActivity.this, " " + delete, Toast.LENGTH_LONG).show();

            }


        };

        asyncTask.execute();*/

        //Utils.waiting();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
