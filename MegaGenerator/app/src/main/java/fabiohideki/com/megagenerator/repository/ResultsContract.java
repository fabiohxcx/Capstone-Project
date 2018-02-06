package fabiohideki.com.megagenerator.repository;

import android.net.Uri;
import android.provider.BaseColumns;

import fabiohideki.com.megagenerator.BuildConfig;

/**
 * Created by fabio.lagoa on 05/02/2018.
 */

public class ResultsContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID;

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_RESULTS = "results";

    public static final String PATH_LAST = "last";


    /* ResultEntry is an inner class that defines the contents of the task table */
    public static final class ResultEntry implements BaseColumns {

        // ResultEntry content URI = base content URI + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RESULTS).build();


        // Task table and column names
        public static final String TABLE_NAME = "resultados";

        // Since ResultEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_CONCURSO = "concurso";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_NUMBERS = "numbers";

    }

}
