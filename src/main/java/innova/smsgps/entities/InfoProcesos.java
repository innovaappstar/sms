package innova.smsgps.entities;

/**
 * Created by innovaapps on 10/03/2016.
 * Pequeño beans para poder setear
 * los posibles errores que ocurran en
 * el ServicioLocalización.java y otros..
 */
public class InfoProcesos
{
    public InfoProcesos() {
    }

    public boolean isError() {
        return isError;
    }

    public String getDetalleError() {
        return detalleError;
    }

    public void setDetalleError(String detalleError) {
        this.isError        = true;
        this.detalleError   = detalleError;
    }

    boolean isError = false;
    String detalleError = "";
}
