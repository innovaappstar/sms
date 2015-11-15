package innova.smsgps.beans;

/**
 * Created by USUARIO on 14/11/2015.
 */
public class RegistroAlerta extends Ubicacion
{

    // SOLO SE USARÁ PARA LA SERIALIZACIÓN EN SQLITE
    String FechaHoraSqlite;
    String FlagServidorSqlite;

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
    public String _getIdTipoAlerta()
    {
        return String.valueOf(getIdTipoAlerta());
    }

    public void setIdTipoAlerta(int idTipoAlerta)
    {
        IdTipoAlerta = idTipoAlerta;
    }
    public String getFechaHoraSqlite() {
        return FechaHoraSqlite;
    }

    public void setFechaHoraSqlite(String fechaHoraSqlite) {
        FechaHoraSqlite = fechaHoraSqlite;
    }
    public String getFlagServidorSqlite() {
        return FlagServidorSqlite;
    }

    public void setFlagServidorSqlite(String flagServidorSqlite) {
        FlagServidorSqlite = flagServidorSqlite;
    }
}
