package innova.smsgps.constantes;

/**
 * Created by USUARIO on 13/12/2015.
 */
public class CONSTANT
{
    /**
     * Variables estAAticas para conexiOn remota con el servidor ...
     **/
    private static String NOMBRE_SERVIDOR           = "http://apolomultimedia-server1.info";
    private static String FOLDER_ANDROID            = "android/ws_sms_gps";
    public  static String PATH_WS                   =  NOMBRE_SERVIDOR + "/" + FOLDER_ANDROID + "/";
    public  static String WS_LOGIN_USUARIO          = "ws_login_usuario.php";
    public  static String WS_REGISTRO_USUARIO       = "ws_registro_usuario.php";
    public  static String WS_UPDATE_PROFILE_DATOS_USUARIO   = "ws_update_profile.php";
    public  static String WS_UPDATE_PROFILE_PHOTO_USUARIO   = "ws_upload_foto.php";

    public  static String WS_UPDATE_CONTACTO        = "ws_update_contacto.php";

    public  static String WS_REGISTRO_ALERTA        = "ws_registro_alerta.php";
    public  static String WS_LISTAR_ALERTAS         = "ws_listar_registros.php";
    public  static String WS_REGISTRO_DENUNCIA      = "ws_upload_img.php";


}
