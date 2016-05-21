package innova.smsgps.entities;

/**
 * Created by USUARIO on 21/05/2016.
 * Idioma seleccionado por el usuario...
 */
public class Idioma
{
    public static String SPANISH = "ESPAÑOL";
    public static String ENGLISH = "INGLES";

    public static int ESPANIOL  = 1;
    public static int INGLES    = 2;

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
}
