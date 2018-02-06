package fabiohideki.com.megagenerator.network;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by hidek on 05/02/2018.
 */

public class DownloadZipAllResults extends AsyncTask<String, String, String> {

    private Context context;

    public DownloadZipAllResults(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {

     /*   ContentValues contentValues = new ContentValues();

        contentValues.put(ResultsContract.ResultEntry.COLUMN_CONCURSO, 1);
        contentValues.put(ResultsContract.ResultEntry.COLUMN_DATE, "05/02/02018");
        contentValues.put(ResultsContract.ResultEntry.COLUMN_NUMBERS, "01-02-03-04-05-06");

        Uri uri = context.getContentResolver().insert(ResultsContract.ResultEntry.CONTENT_URI, contentValues);
*/

        return null;
    }

}
