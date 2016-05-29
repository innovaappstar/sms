package innova.smsgps.entities;

/**
 * Created by USUARIO on 28/05/2016.
 * Contactos agregados por el usuario..
 */
public class Contacto
{
    public static int COMPROBARNICK     = 1;
    public static int REGISTRARCONTACTO = 2;
    public static int DELETE            = 3;


    private int ACTION = COMPROBARNICK;


    String _id = ""; // reservado para cruds SQLite



    String telefono = "";
    String nombre   = "";
    String email    = "";



    String codContacto = "";


    public Contacto(String codContacto) {
        this.codContacto = codContacto;
    }

    public Contacto(int ACTION, String email) {
        this.ACTION = ACTION;
        this.email = email;
    }

    /**
     * @param ACTION int
     * @param telefono String
     * @param nombre String
     * @param email String
     * @param codContacto String
     */
    public Contacto(int ACTION, String telefono, String nombre, String email, String codContacto) {
        this.ACTION = ACTION;
        this.telefono = telefono;
        this.nombre = nombre;
        this.email = email;
        this.codContacto = codContacto;
    }

    /**
     * @param telefono String
     * @param nombre String
     * @param email String
     * @param codContacto String
     */
    public Contacto(String telefono, String nombre, String email, String codContacto) {
        this.telefono = telefono;
        this.nombre = nombre;
        this.email = email;
        this.codContacto = codContacto;
    }


    public Contacto() {
    }


    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodContacto() {
        return codContacto;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
    public int getACTION() {
        return ACTION;
    }

    public void setCodContacto(String codContacto) {
        this.codContacto = codContacto;
    }

}
