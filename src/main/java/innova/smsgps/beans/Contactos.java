package innova.smsgps.beans;

/**
 * Created by USUARIO on 11/11/2015.
 */
public class Contactos
{
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

    String nombreContacto;
    String numeroContacto;

}
