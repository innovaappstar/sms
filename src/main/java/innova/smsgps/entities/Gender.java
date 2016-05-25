package innova.smsgps.entities;

/**
 * Created by USUARIO on 21/05/2016.
 * Idioma seleccionado por el usuario...
 */
public class Gender
{
    public static int MALE_id   = 0;
    public static int FEMALE_id = 1;

    public String nombre    = "";
    public int codigo       = 0;

    /**
     * @param nombre String
     * @param codigo int
     */
    public Gender(String nombre, int codigo) {
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
     * @param gender String
     * @return
     * <ul>
     *     <li>  0 : masculino..</li>
     *     <li> -1 : femenino..</li>
     * </ul>
     */
    public static int GETITEMPOSITION(String gender)
    {
        gender = gender.toUpperCase();
        return  (gender.equals("MALE"))? MALE_id : (gender.equals("MASCULINO"))? MALE_id : (gender.length() == 0)? MALE_id : FEMALE_id;
    }
}
