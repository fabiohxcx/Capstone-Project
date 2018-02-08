package fabiohideki.com.megagenerator;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * Created by hidek on 04/02/2018.
 */

public class MyApplication extends Application {

    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }

}
