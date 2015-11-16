package innova.smsgps.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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


}
