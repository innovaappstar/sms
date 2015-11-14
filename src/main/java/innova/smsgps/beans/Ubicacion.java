package innova.smsgps.beans;

import innova.smsgps.enums.IDUTILS;
import innova.smsgps.utils.ManagerUtils;

/**
 * Created by USUARIO on 14/11/2015.
 */
public class Ubicacion
{
    String Latitud;
    String Longitud;
    String FechaHora;
    ManagerUtils managerUtils = new ManagerUtils();
    Coordenada coordenada = new Coordenada();

    public String getLatitud()
    {
        return coordenada._getLatitud();
    }

    public String getLongitud()
    {
        return coordenada._getLongitud();
    }

    public String getFechaHora()
    {
        return managerUtils.getFechaHora(IDUTILS.DDHHMMSS);
    }



}
