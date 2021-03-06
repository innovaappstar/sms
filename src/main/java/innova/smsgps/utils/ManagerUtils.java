package innova.smsgps.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.SparseArray;
import android.view.View;

import innova.smsgps.enums.IDUTILS;
import innova.smsgps.interfaces.IUtils;

/**
 * Created by innovaapps on 23/10/2015.
 */
public class ManagerUtils implements IUtils {

    IUtils iUtils;

    public ManagerUtils()
    {
        iUtils  = new Utils();
    }


    @Override
    public void imprimirLog(String texto) {
        iUtils.imprimirLog(texto);
    }

    @Override
    public void imprimirToast(Context context, String texto) {
        iUtils.imprimirToast(context, texto);
    }

    @Override
    public String getFechaHora(IDUTILS idutils) {
        return iUtils.getFechaHora(idutils);
    }



    @Override
    public String getRecursos(Context context, int idRecurso) {
        return iUtils.getRecursos(context, idRecurso);
    }

    @Override
    public void showNotificacionSimple(Context context) {
        iUtils.showNotificacionSimple(context);
    }

    @Override
    public Drawable getDrawable(Context context, int id) {
        return iUtils.getDrawable(context, id);
    }

    @Override
    public Bitmap getBitmapCanvas(Activity activity, View view) {
        return iUtils.getBitmapCanvas(activity, view);
    }

    @Override
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        return iUtils.getImageUri(inContext, inImage);
    }

    @Override
    public String getRealPathFromURI(Context context, Uri uri) {
        return iUtils.getRealPathFromURI(context, uri);
    }

    @Override
    public byte[] getImagenComprimida(String path) {
        return iUtils.getImagenComprimida(path);
    }

    @Override
    public Bitmap getBitmap(String path) {
        return iUtils.getBitmap(path);
    }

    public String getCountry(Context context)
    {
        return iUtils.getCountry(context);
    }
    public SparseArray<String> getCanciones()
    {
        return iUtils.getCanciones();
    }

    @Override
    public void showNotificacionMusic(Context context) {
        iUtils.showNotificacionMusic(context);
    }

    @Override
    public void mostrarNotificacionCustomizada(Context context, String nombreCancion) {
        iUtils.mostrarNotificacionCustomizada(context, nombreCancion);
    }

    @Override
    public void deslizadoVertical(View view, Context context, boolean isMostrar) {
        iUtils.deslizadoVertical(view, context, isMostrar);
    }

    @Override
    public boolean isExisteFile(Context context, String uri) {
        return iUtils.isExisteFile(context, uri);
    }

    @Override
    public int getBateria(Context context) {
        return iUtils.getBateria(context);
    }

}
