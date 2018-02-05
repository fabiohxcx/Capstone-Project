package fabiohideki.com.megagenerator.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by hidek on 03/02/2018.
 */

public class Utils {

    public static void waiting() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {

        }
    }

    public static String convertMillitoDate(String milliseconds) {

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(Long.parseLong(milliseconds));

        int mYear1 = calendar2.get(Calendar.YEAR);
        int mMonth1 = (calendar2.get(Calendar.MONTH) + 1);
        int mDay1 = calendar2.get(Calendar.DAY_OF_MONTH);

        return mDay1 + "/" + mMonth1 + "/" + mYear1;
    }

    public static String decimalFormat(String number) {

        DecimalFormat df = new DecimalFormat("#,###,##0.00");

        return df.format(Double.valueOf(number));
    }

    public static int diffBetweenDayAndToday(String date) {

        try {

            SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
            String inputString1 = myFormat.format(new Date());
            String inputString2 = date;

            Date date1 = null;

            date1 = myFormat.parse(inputString1);

            Date date2 = myFormat.parse(inputString2);

            long diff = date2.getTime() - date1.getTime();

            int dias = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            return dias;

        } catch (ParseException e) {

            e.printStackTrace();
            return -1;
        }


    }

    public static Bitmap screenShot(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static File saveBitmap(Bitmap bm, String fileName) {

        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Screenshots";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

}
