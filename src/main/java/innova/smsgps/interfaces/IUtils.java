package innova.smsgps.interfaces;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.SparseArray;
import android.view.View;

import innova.smsgps.enums.IDUTILS;


/**
 * Created by innovaapps on 23/10/2015.
 */
public interface IUtils {

     void imprimirLog(String texto);

     void imprimirToast(Context context, String texto);

     String getFechaHora(IDUTILS idutils);

     String getRecursos(Context context, int idRecurso);

     void showNotificacionSimple(Context context);

     Drawable getDrawable(Context context, int id);

     Bitmap getBitmapCanvas(Activity activity, View view);

     Uri getImageUri(Context inContext, Bitmap inImage);

     String getRealPathFromURI(Context context, Uri uri);

     byte[] getImagenComprimida(String path);

     Bitmap getBitmap(String path);

     String getCountry(Context context);

     SparseArray<String> getCanciones();

     void showNotificacionMusic(Context context);

     void mostrarNotificacionCustomizada(Context context, String nombreCancion);

     void deslizadoVertical(View view,  Context context , boolean isMostrar);
     boolean isExisteFile(Context context, String uri);

     int getBateria(Context context);
}