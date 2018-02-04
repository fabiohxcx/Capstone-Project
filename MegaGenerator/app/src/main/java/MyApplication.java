import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by hidek on 04/02/2018.
 */

public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }

}
