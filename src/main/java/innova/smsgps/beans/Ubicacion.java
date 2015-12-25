package innova.smsgps.beans;

import innova.smsgps.application.Globals;
import innova.smsgps.enums.IDSP2;
import innova.smsgps.enums.IDUTILS;
import innova.smsgps.utils.ManagerUtils;

/**
 * Created by USUARIO on 14/11/2015.
 */
public class Ubicacion
{
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

    public String getIdRegistroAlertasMovil()
    {
        return Globals.getInfoMovil().getSPF2(IDSP2.IDREGISTROALERTASMOVIL);
    }
//    public String getIdFacebook()
//    {
//        return Globals.getInfoMovil().getSPF2(IDSP2.IDFACEBOOK);
//    }
    public String getNickUsuario()
    {
        return Globals.getInfoMovil().getSPF2(IDSP2.NICKUSUARIO);
    }



}
