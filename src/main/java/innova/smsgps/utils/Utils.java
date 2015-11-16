package innova.smsgps.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import innova.smsgps.ActivityLoginFb;
import innova.smsgps.R;
import innova.smsgps.enums.IDUTILS;
import innova.smsgps.interfaces.IUtils;


/**
 * Created by innovaapps on 23/10/2015.
 */
public class Utils implements IUtils {
    public static String TAG = "PrintReader";

    /**
     * Simple mEtodo que genera una notificación
     * en caso de haber perdido la sesión.
     * @param context
     */
    public void showNotificacionSimple(Context context)
    {
        int icon = R.drawable.ic_contacts;
        long when = System.currentTimeMillis();
        NotificationManager nm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent=new Intent(context, ActivityLoginFb.class);
        PendingIntent pending=PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification;
        if (Build.VERSION.SDK_INT < 11) {
            notification = new Notification(icon, "Title", when);
            notification.setLatestEventInfo(
                    context,
                    "Session perdida",
                    "Restaura tu session",
                    pending);
        } else {
            notification = new Notification.Builder(context)
                    .setContentTitle("Session perdida")
                    .setContentText(
                            "Restaura tu session").setSmallIcon(R.drawable.ic_contacts)
                    .setContentIntent(pending).setWhen(when).setAutoCancel(true)
                    .build();
        }
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        nm.notify(0, notification);
    }

    /**
     * imprimirLog void IMPRIME LOG EN CONSOLA
     * @param texto String QUE SE IMPRIMIRA.
     **/
    public void imprimirLog(String texto)
    {
        Log.i(TAG, texto);
    }
    /**
     * imprimirToast void IMPRIME TOAST EN PANTALLA
     * @param context Context DONDE SE APLICARA.
     * @param texto String QUE SE IMPRIMIRA.
     **/
    public void imprimirToast(Context context, String texto)
    {
        Toast toast = Toast.makeText(context, texto, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
        //Toast.makeText(context, texto, Toast.LENGTH_SHORT).setGravity(Gravity.BOTTOM, 0, 0)
    }
    /**
     *  FUNCIÓN AUXILIAR QUE DEVOLVERÁ LA FECHA Y HORA EN ALGUNOS FORMATOS
     *  @param idutils IDUTILS indice para que evalue que Formato se requiere.
     *  @return String Fecha Formateada.
     **/
    public String getFechaHora(IDUTILS idutils)
    {
        String mFecha = (idutils == IDUTILS.HHMM) ? "HH:mm:ss" :
            (idutils == IDUTILS.HHMM.DDHHMMSS) ? "dd/MM/yyyy HH:mm:ss" : "dd/MM/yyyy HH:mm:ss.SSS";

        return new SimpleDateFormat(mFecha).format(Calendar.getInstance().getTime());
    }

    /**
     * getRecursos String OBTIENE CADENA QUE SE ENCUENTRE EN LOS RECURSOS.
     * @param context context que se usará para llamar a la funcion getResources.
     * @param idRecurso int es la key del recurso.
     **/
    public String getRecursos(Context context, int idRecurso)
    {
        return context.getResources().getString(idRecurso);
    }

    /**
     * Simple void que devolverA un objeto de tipo DRAWABLE de
     * acuerdo al SO del equipo para las vistas
     * marcadores , etc.
     * @param context Contexto de la actividad
     * @param id Int identificador del recurso.
     */
    public Drawable getDrawable(Context context, int id)
    {
        Drawable d;
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            d = context.getDrawable(id);
        } else
        {
            d = context.getResources().getDrawable(id);
        }
        return d;
    }

    /**
     * Simple void que devolverA un objeto de tipo BITMAP de
     * un layout.xml .
     * @param activity Actividad que la requiera.
     * @param view View que se convertira a bitmap.
     */
    public Bitmap getBitmapCanvas(Activity activity, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


}
