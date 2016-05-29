package innova.smsgps.entities;

import java.text.ParseException;

import innova.smsgps.application.Globals;
import innova.smsgps.enums.IDSP1;
import innova.smsgps.enums.IDSP2;

/**
 * Created by USUARIO on 28/05/2016.
 * Contactos agregados por el usuario..
 */
public class Tracker
{
    public static int SINCRONIZAR   = 1;
    public static int LISTAR        = 2;
    public static int UBICACION     = 3;

    private int ACTION = SINCRONIZAR;


    String _id = ""; // reservado para cruds SQLite

    String lat          = "";
    String lng          = "";
    String velocidad    = "";
    String bateria      = "";
    String fechaHora    = "";
    String fechaInicio  = "";
    String fechaFin     = "";

    int idFriend        = 0;    // mySQL

    public Tracker() {
    }
    // sincronizar
    public Tracker(int ACTION) {
        this.ACTION = ACTION;
    }

    // listar mySQL
    public Tracker(int ACTION, String fechaInicio, String fechaFin, int idFriend) {
        this.ACTION = ACTION;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idFriend = idFriend;
    }

    //  insertar SQLite
    public Tracker(String lat, String lng, String velocidad, String bateria, String fechaHora, int idFriend) {
        this.lat = lat;
        this.lng = lng;
        this.velocidad = velocidad;
        this.bateria = bateria;
        this.fechaHora = fechaHora;
        this.idFriend = idFriend;
    }




    public int getACTION() {
        return ACTION;
    }

    public String getLat() {
        return lat;
    }
    public Double getLatToDouble() throws ParseException
    {
        return Double.valueOf(getLat());
    }

    public String getLng() {
        return lng;
    }
    public Double getLngToDouble() throws ParseException
    {
        return Double.valueOf(getLng());
    }

    public String getVelocidad() {
        return velocidad;
    }

    public String getBateria() {
        return bateria;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public int getIdUsuario() {
        return Globals.getInfoMovil().getSPF1(IDSP1.IDUSUARIO);
    }
    public String getEmail()
    {
        return Globals.getInfoMovil().getSPF2(IDSP2.EMAILUSUARIO);
    }

    public String get_id() {
        return _id;
    }
    public int getIdFriend() {
        return idFriend;
    }
    public String getFechaFin() {
        return fechaFin;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }
}
