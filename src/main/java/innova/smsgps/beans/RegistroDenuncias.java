package innova.smsgps.beans;

/**
 * Created by USUARIO on 14/11/2015.
 */
public class RegistroDenuncias extends Ubicacion
{


    // SOLO SE USARAAA PARA LA SERIALIZACIoN EN SQLITE
    String FechaHoraSqlite;
    String FlagServidorSqlite;


    String IdFacebookSqlite;    // DENUNCIA

    String Descripcion;
    String IdTipoDenuncia;
    byte[] ImgDenuncia;

    public String getIdFacebookSqlite() {
        return IdFacebookSqlite;
    }

    public void setIdFacebookSqlite(String idFacebookSqlite) {
        IdFacebookSqlite = idFacebookSqlite;
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

    public byte[] getImgDenuncia() {
        return ImgDenuncia;
    }

    public void setImgDenuncia(byte[] imgDenuncia) {
        ImgDenuncia = imgDenuncia;
    }

    public String getIdTipoDenuncia() {
        return IdTipoDenuncia;
    }

    public void setIdTipoDenuncia(String idTipoDenuncia) {
        IdTipoDenuncia = idTipoDenuncia;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }


}
