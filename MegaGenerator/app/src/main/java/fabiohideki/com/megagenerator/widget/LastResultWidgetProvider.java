package fabiohideki.com.megagenerator.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.model.UltimoResultado;
import fabiohideki.com.megagenerator.ui.MainActivity;
import fabiohideki.com.megagenerator.utils.Utils;

/**
 * Implementation of App Widget functionality.
 */
public class LastResultWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, UltimoResultado ultimoResultado) {

       /* CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object*/
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.last_result_widget);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.ll_widget_content, pendingIntent);

        if (ultimoResultado != null) {
            views.setTextViewText(R.id.widget_tv_item_result_date, context.getString(R.string.card_title,
                    context.getString(R.string.card_title_concurso),
                    ultimoResultado.getNroConcurso(),
                    Utils.convertMillitoDate(ultimoResultado.getDataConcurso())));

            String[] bolas = ultimoResultado.getResultado().split("-");
            views.setTextViewText(R.id.widget_bola1, bolas[0]);
            views.setTextViewText(R.id.widget_bola2, bolas[1]);
            views.setTextViewText(R.id.widget_bola3, bolas[2]);
            views.setTextViewText(R.id.widget_bola4, bolas[3]);
            views.setTextViewText(R.id.widget_bola5, bolas[4]);
            views.setTextViewText(R.id.widget_bola6, bolas[5]);

        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        UpdateLastResultService.startUpdateLastResult(context);

    }

    @Override
    public void onEnabled(Context context) {
        Log.d("Fabio", "LastResultWidgetProvider onEnabled: ");

        // Set the alarm to start at approximately 10:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 22);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, UpdateLastResultService.class);
        intent.setAction(UpdateLastResultService.ACTION_UPDATE_WIDGET_LAST_RESULT);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    @Override
    public void onDisabled(Context context) {
        Log.d("Fabio", "LastResultWidgetProvider onDisabled: ");
        Intent intent = new Intent(context, UpdateLastResultService.class);
        PendingIntent sender = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        super.onDisabled(context);
    }

    public static void updateLastResultWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, UltimoResultado ultimoResultado) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, ultimoResultado);
        }

    }

}

