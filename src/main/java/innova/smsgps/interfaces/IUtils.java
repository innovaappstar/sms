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

    public void imprimirLog(String texto);

    public void imprimirToast(Context context, String texto);

    public String getFechaHora(IDUTILS idutils);

    public String getRecursos(Context context, int idRecurso);

    public void showNotificacionSimple(Context context);

    public Drawable getDrawable(Context context, int id);

    public Bitmap getBitmapCanvas(Activity activity, View view);

    public Uri getImageUri(Context inContext, Bitmap inImage);

    public String getRealPathFromURI(Context context, Uri uri);

    public byte[] getImagenComprimida(String path);

    public Bitmap getBitmap(String path);

    public String getCountry(Context context);

    public SparseArray<String> getCanciones();

    public void showNotificacionMusic(Context context);

    public void mostrarNotificacionCustomizada(Context context, String nombreCancion);
}