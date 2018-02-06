package fabiohideki.com.megagenerator.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fabio.lagoa on 05/02/2018.
 */

public class ResultsDbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "megasenaDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;


    public ResultsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE " + ResultsContract.ResultEntry.TABLE_NAME + " (" +
                ResultsContract.ResultEntry.COLUMN_CONCURSO + " INTEGER PRIMARY KEY, " +
                ResultsContract.ResultEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                ResultsContract.ResultEntry.COLUMN_NUMBERS + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ResultsContract.ResultEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
