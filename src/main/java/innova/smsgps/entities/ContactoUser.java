package innova.smsgps.entities;

/**
 * Created by USUARIO on 20/05/2016.
 */
public class ContactoUser
{
    private int status  = 0;
    private String description = "";



    /**
     * @param status int
     * @param description String
     */
    public ContactoUser(int status, String description)
    {
        this.status = status;
        this.description = description;
    }

    /**
     * función que comprobará si el resultado es
     * uno de los dos valores..
     * <ul>
     *     <li>  1 : login correcto...</li>
     *     <li>  2 : registro de usuario correcto con facebook...</li>
     * </ul>
     * @return
     */
    public boolean isCorrecto()
    {
        if (this.status == 1 || this.status == 2)
            return true;

        return false;
    }
    public String getDescription() {
        return description;
    }

}
