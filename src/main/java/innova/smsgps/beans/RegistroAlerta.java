package innova.smsgps.beans;

/**
 * Created by USUARIO on 14/11/2015.
 */
public class RegistroAlerta extends Ubicacion
{
    String IdFacebook;
    int IdTipoAlerta;

    public String getIdFacebook()
    {
        return IdFacebook;
    }

    public void setIdFacebook(String idFacebook)
    {
        IdFacebook = idFacebook;
    }

    public int getIdTipoAlerta()
    {
        return IdTipoAlerta;
    }

    public void setIdTipoAlerta(int idTipoAlerta)
    {
        IdTipoAlerta = idTipoAlerta;
    }

}
