package fabiohideki.com.megagenerator.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import fabiohideki.com.megagenerator.model.Bolas;
import fabiohideki.com.megagenerator.model.UltimoResultado;
import fabiohideki.com.megagenerator.utils.Utils;

/**
 * Created by hidek on 04/02/2018.
 */

public class UltimoResultadoAsyncTaskLoader extends AsyncTaskLoader<UltimoResultado> {

    private TaskCallBack listener;
    private Context context;
    private final String TAG = getClass().getSimpleName();

    public UltimoResultadoAsyncTaskLoader(Context context, TaskCallBack listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(TAG, "onStartLoading: ");

        //TODO Loading indicator visible
        listener.onStartTask();

        forceLoad();
    }

    @Override
    public UltimoResultado loadInBackground() {

        Log.d(TAG, "loadInBackground: ");

        Utils.waiting();

        Bolas bolas = new Bolas(10, 15, 5, 2, 36, 78);

        UltimoResultado ultimoResultado = new UltimoResultado(
                2010,
                bolas,
                "03/02/2018",
                true,
                "R$ 56.000.000,00",
                "06/02/2018",
                "Não houve acertador",
                "106 apostas ganhadoras, R$ 33.705,40",
                "7436 apostas ganhadoras, R$ 686,38"
        );

        ultimoResultado.setBolas(bolas);

        return ultimoResultado;
    }
}
