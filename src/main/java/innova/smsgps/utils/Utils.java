package innova.smsgps.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import innova.smsgps.ActivityFacebookAccount;
import innova.smsgps.ActivityMediaOpciones;
import innova.smsgps.R;
import innova.smsgps.anim.AnimViews;
import innova.smsgps.application.Globals;
import innova.smsgps.enums.IDSP2;
import innova.smsgps.enums.IDUTILS;
import innova.smsgps.interfaces.IUtils;


/**
 * Created by innovaapps on 23/10/2015.
 */
public class Utils implements IUtils {
    public static String TAG = "smsgps";

    /**
     * Simple mEtodo que genera una notificación
     * en caso de haber perdido la sesión.
     * @param context
     */
    @Override
    public void showNotificacionSimple(Context context)
    {
        showNotificacion(context, R.drawable.img_notification_facebook_session, "Session perdida", "Restaura tu sesión", ActivityFacebookAccount.class);
    }

    @Override
    public void showNotificacionMusic(Context context)
    {
        showNotificacion(context, R.drawable.ic_small_music, "Music", "Selecciona un directorio de Música", ActivityMediaOpciones.class);
//        mostrarNotificacionCustomizada(context, "Perdido..");
    }

    @Override
    public void mostrarNotificacionCustomizada(Context context, String nombreCancion)
    {
        Notification notification = new Notification.Builder(context).
                setContentTitle("Música")
                .setContentText(nombreCancion)
                .setSmallIcon(R.drawable.ic_small_music)
                .setAutoCancel(true)
                .build();
//        notification.flags = Notification.FLAG_ONGOING_EVENT;
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }


    /**
     * Simple mEtodo que genera una notificación
     * en caso de haber perdido la sesión.
     * @param context
     */
    private void showNotificacion(Context context, int drawable,String Title, String Descripcion, Class classActivity)
    {
        int icon = R.drawable.ic_contacts;
        long when = System.currentTimeMillis();
        NotificationManager nm  = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(1);   // clear previous notification
        Intent intent   =   new Intent(context, classActivity);
        PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification;
        if (Build.VERSION.SDK_INT < 11) {
            notification = new Notification(icon, "Sesión Perdida", when);
            notification.setLatestEventInfo(
                    context,
                    "Sesión perdida",
                    "Restaura tu sesión",
                    pending);
        } else {
            notification = new Notification.Builder(context)
                    .setContentTitle(Title)
                    .setContentText(Descripcion)
                    .setSmallIcon(drawable)
//                    .setLargeIcon()   // SE PUEDE UTILIZAR UN SEGUNDO ICONO PARA MOSTRAR AL DESLIZAR..
                    .setContentIntent(pending).setWhen(when).setAutoCancel(true)
//                    .setVibrate(new long[] { 500, 500 })  //  { 1000, 1000, 1000, 1000, 1000 })
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setLights(Color.WHITE, 3000, 3000)
                    .build();
        }
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.ledOnMS = 1000;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.ledARGB = Color.YELLOW;
        nm.notify(1, notification);
//        RedFlashLight(context);
    }

    /**
     * Simple método que retornará un sparseArray
     * con los nombres de las canciones..
     * @return
     */
    @Override
    public SparseArray<String> getCanciones()
    {
        SparseArray<String> listaCanciones = new SparseArray<String>();
        int contadorCancion     = 0;
        String path     = "/sdcard/" + Globals.getInfoMovil().getSPF2(IDSP2.DIRECTORIOMUSIC);
        File file       = new File(path);
        File[] files    =   file.listFiles();
        for(File fileMusic : files)
        {
            String filePath = fileMusic.getPath();
            String[] vector = filePath.split("\\/");
            if (vector.length > 2)
            {
                String nombreArchivo = vector[vector.length -1];
                if (nombreArchivo.endsWith(".mp3"))
                {
                    listaCanciones.put(contadorCancion, nombreArchivo);
                    contadorCancion ++;
                }
            }
        }
        return listaCanciones;
    }

    public int getCountArchivosMp3(String directorio)
    {
        int count = 0;
        String path = "/sdcard/" + directorio;
        //It have to be matched with the directory in SDCard
        File f = new File(path);

        File[] files=f.listFiles();
        for(int i=0; i<files.length; i++)
        {
            File file = files[i];
            /*It's assumed that all file in the path are in supported type*/
            String filePath = file.getPath();

            String[] vector = filePath.split("\\/");
            if (vector.length > 2)
            {
                String nombreArchivo = vector[vector.length -1];
                if (nombreArchivo.endsWith(".mp3"))
                {
                    count ++;
                }
            }
        }
        return count;
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
            (idutils == IDUTILS.DDHHMMSS) ? "dd/MM/yyyy HH:mm:ss" : "dd/MM/yyyy HH:mm:ss.SSS";

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

    /**
     * Función que devuelve la uri de un archivo...
     **/
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    /**
     * Función que devuelve la ubicación de un archivo..
     **/
    public String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    /**
     * Función que devuelve objeto byte comprimido de una imágen...
     **/
    public byte[] getImagenComprimida(String path)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Bitmap bm = BitmapFactory.decodeFile(path);
        bm.compress(Bitmap.CompressFormat.JPEG, 75, bos); // Compresion
        byte[] data = bos.toByteArray();
        return data;
    }

    /**
     * Función que devuelve objeto bitmap a partir de su path
     **/
    public Bitmap getBitmap(String path)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Bitmap bm = BitmapFactory.decodeFile(path);
        bm.compress(Bitmap.CompressFormat.JPEG, 75, bos); // Compresion
        return bm;
    }


    /**
     * Simple función que devuelve el País del usuario , de acuerdo a la sim mobile.
     **/
    @Override
    public String getCountry(Context context)
    {
        TelephonyManager tlfManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String CountryID            = tlfManager.getSimCountryIso().toUpperCase();
        Locale l                    = new Locale("", CountryID);
        return l.getDisplayCountry();
    }

    /**
     * Deslizado Top-Bottom
     * @param view View
     * @param context Context
     * @param isMostrar boolean
     */
    @Override
    public void deslizadoVertical(View view,  Context context , boolean isMostrar)
    {
        AnimViews animViews = new AnimViews();
        animViews.deslizadoVertical(view, context, isMostrar);
    }

}
