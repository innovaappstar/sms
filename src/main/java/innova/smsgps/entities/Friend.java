package innova.smsgps.entities;

/**
 * Created by USUARIO on 28/05/2016.
 * Contactos agregados por el usuario..
 */
public class Friend
{
    int idUsuario   = 0;
    String email    = "";
    String nombre   = "";
    String apellido = "";
    String fotoURL  = "";

    /**
     * @param idUsuario int
     * @param email String
     * @param nombre String
     * @param apellido String
     * @param fotoURL String
     */
    public Friend(int idUsuario, String email, String nombre, String apellido, String fotoURL) {
        this.idUsuario = idUsuario;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fotoURL = fotoURL;
    }
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getFotoURL() {
        return fotoURL;
    }


}
