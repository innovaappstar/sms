package innova.smsgps.beans;

import java.util.ArrayList;

/**
 * Created by USUARIO on 14/11/2015.
 */
public class ListRegistrosAlertas
{

    public static ArrayList<HistorialRegistros> listHistorial   = new ArrayList<HistorialRegistros>();

    public static ArrayList<RegistroAlerta> listRegistrosAlertas = new ArrayList<RegistroAlerta>();

    public static ArrayList<RegistroAlerta> getListRegistrosAlertas() {
        return listRegistrosAlertas;
    }

    public static void setListRegistrosAlertas(ArrayList<RegistroAlerta> listRegistrosAlertas) {
        ListRegistrosAlertas.listRegistrosAlertas = listRegistrosAlertas;
    }


    public static ArrayList<HistorialRegistros> getListHistorial() {
        return listHistorial;
    }

    public static void setListHistorial(ArrayList<HistorialRegistros> listHistorial) {
        ListRegistrosAlertas.listHistorial = listHistorial;
    }


}
