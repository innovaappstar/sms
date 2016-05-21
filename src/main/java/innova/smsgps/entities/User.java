package innova.smsgps.entities;

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
    private String id           = "";
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


    public User(){}

    /**
     * @param id id único de facebook
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
    public User(String id, String firstName, String timeZone, String email, String verified, String name, String locale, String link, String lastName, String gender, String updatedTime)
    {
        this.id = id;
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
     * @param email email
     * @param password contrasenia
     * @param lastName apellidos
     */
    public User(String firstName, String email, String password, String lastName)
    {
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.ACTION_LOGIN = MANUAL; // login sin facebook
    }



    public String getId() {
        return id;
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


    public String getACTION_LOGIN() {
        return ACTION_LOGIN;
    }
    public String getPassword()
    {
        if (this.password.length() == 0)
            return this.getId();    // se envía id de facebook como contrasenia...
        return password;    // se envía psssword ingresado..
    }
}
