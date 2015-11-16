package innova.smsgps.interfaces;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import innova.smsgps.enums.IDUTILS;


/**
 * Created by innovaapps on 23/10/2015.
 */
public interface IUtils
{

    public void imprimirLog(String texto);
    public void imprimirToast(Context context, String texto);
    public String getFechaHora(IDUTILS idutils);
    public String getRecursos(Context context, int idRecurso);
    public void showNotificacionSimple(Context context);
    public Drawable getDrawable(Context context, int id);
    public Bitmap getBitmapCanvas(Activity activity, View view);

}
