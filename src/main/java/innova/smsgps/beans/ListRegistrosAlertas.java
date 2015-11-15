package innova.smsgps.beans;

import java.util.ArrayList;

/**
 * Created by USUARIO on 14/11/2015.
 */
public class ListRegistrosAlertas
{


    public static ArrayList<RegistroAlerta> getListRegistrosAlertas() {
        return listRegistrosAlertas;
    }

    public static void setListRegistrosAlertas(ArrayList<RegistroAlerta> listRegistrosAlertas) {
        ListRegistrosAlertas.listRegistrosAlertas = listRegistrosAlertas;
    }

    public static ArrayList<RegistroAlerta> listRegistrosAlertas = new ArrayList<RegistroAlerta>();



}
