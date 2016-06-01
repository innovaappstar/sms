package innova.smsgps.entities;

import innova.smsgps.application.Globals;
import innova.smsgps.enums.IDSP1;
import innova.smsgps.enums.IDSP2;

/**
 * Created by USUARIO on 29/05/2016.
 */
public class RegistroTrack
{
    public final static int TIPOALERTA = 1;
    public final static int TIPOSEGUIMIENTO = 2;

    public int ACTION = TIPOALERTA;

    // alerta
    public String latitud   = "";
    public String longitud  = "";
    public String fechaHora = "";

    // seguimiento
    public String velocidad = "";
    public String bateria   = "";


    public RegistroTrack() { }

    /**
     * @param latitud String
     * @param longitud String
     * @param fechaHora String
     */
    public RegistroTrack(String latitud, String longitud, String fechaHora) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaHora = fechaHora;
        this.ACTION = TIPOALERTA;
    }

    /**
     * @param latitud String
     * @param longitud String
     * @param fechaHora String
     * @param velocidad String
     * @param bateria String
     */
    public RegistroTrack(String latitud, String longitud, String fechaHora, String velocidad, String bateria)
    {
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaHora = fechaHora;
        this.velocidad = velocidad;
        this.bateria = bateria;
        this.ACTION = TIPOSEGUIMIENTO;
    }


    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public String getEmail()
    {
        return Globals.getInfoMovil().getSPF2(IDSP2.EMAILUSUARIO);
    }

    public String getVelocidad() {
        return velocidad;
    }

    public String getBateria() {
        return bateria;
    }
    public String getIdUsuario()
    {
        return String.valueOf(Globals.getInfoMovil().getSPF1(IDSP1.IDUSUARIO));
    }

    public int getACTION() {
        return ACTION;
    }



}
