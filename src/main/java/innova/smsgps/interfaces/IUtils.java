package innova.smsgps.interfaces;

import android.content.Context;

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

}
