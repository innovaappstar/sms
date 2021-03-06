package innova.smsgps.entities;

/**
 * Created by USUARIO on 21/05/2016.
 * Idioma seleccionado por el usuario...
 */
public class Idioma
{
    public static String ESPANIOL = "ESPAÑOL";  // User.java - Idioma.java
    public static int ESPANIOL_id  = 0;
    public static int INGLES_id    = 1;

    public String nombre    = "";
    public int codigo       = 0;

    /**
     * @param nombre String
     * @param codigo int
     */
    public Idioma(String nombre, int codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCodigo() {
        return codigo;
    }

    /**
     * Simple función para obtener posición de spinner
     * @param idioma String
     * @return
     * <ul>
     *     <li>  0 : Español..</li>
     *     <li> -1 : Ingles..</li>
     * </ul>
     */
    public static int GETITEMPOSITION(String idioma)
    {
        idioma = idioma.toUpperCase();
        return  (idioma.equals(ESPANIOL))? ESPANIOL_id : (idioma.equals("Spanish"))? ESPANIOL_id : INGLES_id;
    }

}
