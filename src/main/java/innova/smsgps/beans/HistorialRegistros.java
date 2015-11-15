package innova.smsgps.beans;

/**
 * Created by USUARIO on 15/11/2015.
 * Java bean sOlo para los get/set
 * del serializado que se harA en
 * Sqlite y Servidor Php/Mysql. *
 */
public class HistorialRegistros
{
    public String IdFacebook ;
    public String IdTipoAlerta ;
    public String Latitud ;
    public String Longitud ;
    public String FechaHora ;

    public String getIdFacebook() {
        return IdFacebook;
    }

    public void setIdFacebook(String idFacebook) {
        IdFacebook = idFacebook;
    }

    public String getIdTipoAlerta() {
        return IdTipoAlerta;
    }

    public void setIdTipoAlerta(String idTipoAlerta) {
        IdTipoAlerta = idTipoAlerta;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public String getFechaHora() {
        return FechaHora;
    }

    public void setFechaHora(String fechaHora) {
        FechaHora = fechaHora;
    }
}
