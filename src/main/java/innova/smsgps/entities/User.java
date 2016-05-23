package innova.smsgps.entities;

import innova.smsgps.application.Globals;
import innova.smsgps.enums.IDSP1;

/**
 * Created by USUARIO on 19/05/2016.
 * Entidad User - Login Facebook
 */
public class User
{
    // código de acciones para que la ws identifique ..
    protected String MANUAL     = "1";
    protected String FACEBOOK   = "2";

    private String ACTION_LOGIN = "";   // indicador de tipo de sesión (fb/manual)



    // datos obtenidos de la api facebook
    private String idFacebook   = "";
    private String firstName    = "";
    private String timeZone     = "";
    private String email        = "";
    private String verified     = "";
    private String name         = "";
    private String locale       = "";
    private String link         = "";
    private String lastName     = "";
    private String gender       = "";
    private String updatedTime  = "";

    // datos ingresados por el usuario o establecido de facebook
    private String password     = "";

    // _id sqlite para crud update/delete
    private String _id          = "";

    public User(){}

    /**
     * @param idFacebook idFacebook único de facebook
     * @param firstName primer nombre
     * @param timeZone zona horaria
     * @param email email
     * @param verified estado cuenta
     * @param name nombre completo
     * @param locale idioma
     * @param link link usuario
     * @param lastName apellidos
     * @param gender género
     * @param updatedTime tiempo de actualización
     */
    public User(String idFacebook, String firstName, String timeZone, String email, String verified, String name, String locale, String link, String lastName, String gender, String updatedTime)
    {
        this.idFacebook = idFacebook;
        this.firstName = firstName;
        this.timeZone = timeZone;
        this.email = email;
        this.verified = verified;
        this.name = name;
        this.locale = locale;
        this.link = link;
        this.lastName = lastName;
        this.gender = gender;
        this.updatedTime = updatedTime;
        this.ACTION_LOGIN = FACEBOOK;   // login por facebook
    }

    /**
     * @param email email
     * @param password contrasenia
     */
    public User(String email, String password)
    {
        this.email = email;
        this.password = password;
        this.ACTION_LOGIN = MANUAL; // login sin facebook
    }

    /**
     * @param firstName primer nombre
     * @param lastName apellidos
     * @param email email
     * @param password contrasenia
     */
    public User(String firstName,  String lastName, String email, String password)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.ACTION_LOGIN = MANUAL; // login sin facebook
    }

    public String getIdFacebook() {
        return idFacebook;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getEmail() {
        return email;
    }

    public String getVerified() {
        return verified;
    }

    public String getName() {
        return name;
    }

    public String getLocale() {
        return locale;
    }

    public String getLink() {
        return link;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }


    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }


    public String getACTION_LOGIN() {
        return ACTION_LOGIN;
    }
    public String getPassword()
    {
        if (this.password.length() == 0)
            return this.getIdFacebook();    // se envía idFacebook de facebook como contrasenia...
        return password;    // se envía psssword ingresado..
    }

    // obtenemos idioma seleccionado..
    public String getLanguaje()
    {
        return (Globals.getInfoMovil().getSPF1(IDSP1.IDIOMA) == Idioma.ESPANIOL) ? Idioma.SPANISH : Idioma.ENGLISH;
    }

}
