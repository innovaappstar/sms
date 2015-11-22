package innova.smsgps.beans;

/**
 * Created by USUARIO on 11/11/2015.
 */
public class Contactos
{


    public static String ListaNumeros = "";

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getNumeroContacto() {
        return numeroContacto;
    }

    public void setNumeroContacto(String numeroContacto) {
        this.numeroContacto = numeroContacto;
    }

    public String getTipoContacto() {
        return tipoContacto;
    }

    public void setTipoContacto(String tipoContacto) {
        this.tipoContacto = tipoContacto;
    }

    public static String getListaNumeros() {
        return ListaNumeros;
    }

    public static void setListaNumeros(String listaNumeros) {
        ListaNumeros = listaNumeros;
    }

    public int getCodContacto() {
        return codContacto;
    }

    public void setCodContacto(int codContacto) {
        this.codContacto = codContacto;
    }

    String nombreContacto;
    String numeroContacto;
    String tipoContacto;



    int codContacto ;
}
