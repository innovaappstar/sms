package innova.smsgps.beans;

/**
 * Created by USUARIO on 11/11/2015.
 */
public class Directorios
{


    public String getNombreDirectorio() {
        return NombreDirectorio;
    }

    public void setNombreDirectorio(String nombreDirectorio) {
        NombreDirectorio = nombreDirectorio;
    }

    public int getCantidadArchivos() {
        return CantidadArchivos;
    }

    public void setCantidadArchivos(int cantidadArchivos) {
        CantidadArchivos = cantidadArchivos;
    }

    String NombreDirectorio = "";
    int CantidadArchivos    = 0;
}
