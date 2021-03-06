package innova.smsgps.infomovil;

import android.content.Context;
import android.content.SharedPreferences;

import innova.smsgps.entities.Idioma;
import innova.smsgps.enums.IDSP1;
import innova.smsgps.enums.IDSP2;
import innova.smsgps.interfaces.IInfoMovil;

/**
 * Created by innovaapps on 29/10/2015.
 */
public class InfoMovil implements IInfoMovil
{
    static Context context;
    static SharedPreferences spf;
    static SharedPreferences.Editor editorspf;

    public InfoMovil(Context context)
    {
        this.context = context;
        instanciarPrefs();
    }
    // NOMBRES ESTÁTICOS -
    /**
     * INSTANCIAMOS LOS SPF EN UN CONTEXTO PRIVADO
     **/
    private void instanciarPrefs()
    {
        spf         = context.getSharedPreferences("innova.sib", Context.MODE_PRIVATE);
        editorspf   = spf.edit();
    }
    // NOMBRES ESTÁTICOS -

    /**
     * Setear los spf
     * @param idsp1 IDSP1 key del spf
     * @param i int valor que se seteará en el spf seleccionado.
     **/

    public void setSpf1(IDSP1 idsp1, int i)
    {
        switch (idsp1)
        {
            case BEEP1:
                editorspf.putInt("BEEP1", i);
                break;
            case BEEP2:
                editorspf.putInt("BEEP2", i);
                break;
            case BEEP3:
                editorspf.putInt("BEEP3", i);
                break;
            case IDIOMA:
                editorspf.putInt("IDIOMA", i);
                break;
            case IDUSUARIO:
                editorspf.putInt("IDUSUARIO", i);
                break;
            case TRACKING:
                editorspf.putInt("TRACKING", i);
                break;
            case LOGUEADO:
                editorspf.putInt("LOGUEADO", i);
                break;
//            case NUMERO01:
//                editorspf.putInt("NUMERO01", i);
//                break;
//            case NUMERO02:
//                editorspf.putInt("NUMERO02", i);
//                break;
//            case NUMERO03:
//                editorspf.putInt("NUMERO03", i);
//                break;
//            case NUMERO04:
//                editorspf.putInt("NUMERO04", i);
            case FLAGSPLASH:
                editorspf.putInt("FLAGSPLASH", i);
                break;
        }
        editorspf.commit();
    }

    // NOMBRES ESTÁTICOS -
    /**
     * Obtener int de los spf
     * @param idsp1 IDSP1 key del spf
     * @return int que contiene el spf
     * <ul>
     *     <li> -1 Se envió algún enumerable que no se encuentra en los casos </li>
     * </ul>
     **/

    public int getSPF1(IDSP1 idsp1)
    {
        switch (idsp1)
        {
            case BEEP1:
                return spf.getInt("BEEP1", 1);  // default : 1 = Asalto
            case BEEP2:
                return spf.getInt("BEEP2", 1);  // default : 1 = Asalto
            case BEEP3:
                return spf.getInt("BEEP3", 1);  // default : 1 = Asalto

            case IDIOMA:
                return spf.getInt("IDIOMA", Idioma.ESPANIOL_id);
            case IDUSUARIO:
                return spf.getInt("IDUSUARIO", 0);
            case TRACKING:
                return spf.getInt("TRACKING", 0);
            case LOGUEADO:
                return spf.getInt("LOGUEADO", 0);
//            case NUMERO01:
//                return spf.getInt("NUMERO01", 0);
//            case NUMERO02:
//                return spf.getInt("NUMERO02", 0);
//            case NUMERO03:
//                return spf.getInt("NUMERO03", 0);
//            case NUMERO04:
//                return spf.getInt("NUMERO04", 0);
            case FLAGSPLASH:
                return spf.getInt("FLAGSPLASH", 0);
            default:
                return -1;
        }
    }

    /**
     * Setear los spf
     * @param idsp2 IDSP1 key del spf
     * @param s String valor que se seteará en el spf seleccionado.
     **/

    public void setSpf2(IDSP2 idsp2, String s)
    {
        switch (idsp2)
        {
            case NUMERO01:
                editorspf.putString("NUMERO01", s);
                break;
            case NUMERO02:
                editorspf.putString("NUMERO02", s);
                break;
            case NUMERO03:
                editorspf.putString("NUMERO03", s);
                break;
            case NUMERO04:
                editorspf.putString("NUMERO04", s);
                break;
            case IDFACEBOOK:
                editorspf.putString("IDFACEBOOK", s);
                break;
            case IDREGISTROALERTASMOVIL:
                editorspf.putString("IDREGISTROALERTASMOVIL", s);
                break;
            case IDREGISTRODENUNCIAMOVIL:
                editorspf.putString("IDREGISTRODENUNCIAMOVIL", s);
                break;
            case NICKUSUARIO:
                editorspf.putString("NICKUSUARIO", s);
                break;
            case DIRECTORIOMUSIC:
                editorspf.putString("DIRECTORIOMUSIC", s);
            case MACADDRESS:
                editorspf.putString("MACADDRESS", s);
                break;
            case URIFOTOPROFILE:
                editorspf.putString("URIFOTOPROFILE", s);
                break;
            case EMAILUSUARIO:
                editorspf.putString("EMAILUSUARIO", s);
                break;




        }
        editorspf.commit();
    }

    // NOMBRES ESTÁTICOS -
    /**
     * Obtener int de los spf
     * @param idsp2 IDSP1 key del spf
     * @return int que contiene el spf
     * <ul>
     *     <li> -1 Se envió algún enumerable que no se encuentra en los casos </li>
     * </ul>
     **/

    public String getSPF2(IDSP2 idsp2)
    {
        switch (idsp2)
        {
            case NUMERO01:
                return spf.getString("NUMERO01", "");
            case NUMERO02:
                return spf.getString("NUMERO02", "");
            case NUMERO03:
                return spf.getString("NUMERO03", "");
            case NUMERO04:
                return spf.getString("NUMERO04", "");
            case IDFACEBOOK:
                return spf.getString("IDFACEBOOK", "");
            case IDREGISTROALERTASMOVIL:
                return spf.getString("IDREGISTROALERTASMOVIL", "");
            case IDREGISTRODENUNCIAMOVIL:
                return spf.getString("IDREGISTRODENUNCIAMOVIL", "");
            case NICKUSUARIO:
                return spf.getString("NICKUSUARIO", "");
            case DIRECTORIOMUSIC:
                return spf.getString("DIRECTORIOMUSIC", "");
            case MACADDRESS:
                return spf.getString("MACADDRESS", "");
            case URIFOTOPROFILE:
                return spf.getString("URIFOTOPROFILE", "");
            case EMAILUSUARIO:
                return spf.getString("EMAILUSUARIO", "");

            default:
                return "";
        }
    }

}
