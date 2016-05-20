package innova.smsgps.entities;

/**
 * Created by USUARIO on 19/05/2016.
 * Entidad User - Login Facebook
 */
public class User
{
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
    public User(String id, String firstName, String timeZone, String email, String verified, String name, String locale, String link, String lastName, String gender, String updatedTime) {
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


}
