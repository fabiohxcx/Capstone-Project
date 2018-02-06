package fabiohideki.com.megagenerator.repository;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static fabiohideki.com.megagenerator.repository.ResultsContract.ResultEntry.COLUMN_CONCURSO;
import static fabiohideki.com.megagenerator.repository.ResultsContract.ResultEntry.CONTENT_URI;
import static fabiohideki.com.megagenerator.repository.ResultsContract.ResultEntry.TABLE_NAME;

public class ResultsContentProvider extends ContentProvider {

    public static final int RESULTS = 100;
    public static final int RESULTS_WITH_ID = 101;
    public static final int RESULTS_LAST = 102;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(ResultsContract.AUTHORITY, ResultsContract.PATH_RESULTS, RESULTS);

        uriMatcher.addURI(ResultsContract.AUTHORITY, ResultsContract.PATH_RESULTS + "/#", RESULTS_WITH_ID);

        uriMatcher.addURI(ResultsContract.AUTHORITY, ResultsContract.PATH_RESULTS + "/" + ResultsContract.PATH_LAST, RESULTS_LAST);

        return uriMatcher;
    }

    private ResultsDbHelper mResultsDbHelper;

    public ResultsContentProvider() {

    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mResultsDbHelper = new ResultsDbHelper(context);

        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mResultsDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case RESULTS:

                long id = db.insert(TABLE_NAME, null, values);

                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mResultsDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Log.d("Fabio", "query: match: " + match);

        Cursor retCursor;

        switch (match) {
            case RESULTS:
                Log.d("Fabio", "query: RESULTS");
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            case RESULTS_WITH_ID:

                Log.d("Fabio", "query: RESULTS_WITH_ID");

                String id = uri.getPathSegments().get(1);

                String mSelection = "concurso=?";
                String[] mSelectionArgs = new String[]{id};

                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            case RESULTS_LAST:

                String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_CONCURSO + " DESC LIMIT 1;";
                retCursor = db.rawQuery(sql, null);

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mResultsDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int resultDeleted;

        switch (match) {
            case RESULTS_WITH_ID:

                String id = uri.getPathSegments().get(1);

                resultDeleted = db.delete(TABLE_NAME, COLUMN_CONCURSO + "=?", new String[]{id});

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (resultDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return resultDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
