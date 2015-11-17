package innova.smsgps.beans;

import java.util.ArrayList;

/**
 * Created by USUARIO on 14/11/2015.
 */
public class ListRegistrosDenuncias
{

    public static ArrayList<HistorialRegistros> getListHistorial() {
        return listHistorial;
    }   // MAPA

    public static void setListHistorial(ArrayList<HistorialRegistros> listHistorial) {
        ListRegistrosDenuncias.listHistorial = listHistorial;
    }

    public static ArrayList<RegistroDenuncias> getListRegistrosDenuncias() {
        return listRegistrosDenuncias;
    }   // LISTADO

    public static void setListRegistrosDenuncias(ArrayList<RegistroDenuncias> listRegistrosDenuncias) {
        ListRegistrosDenuncias.listRegistrosDenuncias = listRegistrosDenuncias;
    }

    public static ArrayList<HistorialRegistros> listHistorial   = new ArrayList<HistorialRegistros>();  // MAPA

    public static ArrayList<RegistroDenuncias> listRegistrosDenuncias = new ArrayList<RegistroDenuncias>(); // LISTADO


}
