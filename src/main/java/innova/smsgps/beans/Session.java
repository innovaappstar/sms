package innova.smsgps.beans;

/**
 * Created by USUARIO on 14/11/2015.
 */
public class Session
{
    public com.facebook.Session getSession() {
        return session;
    }

    public void setSession(com.facebook.Session session) {
        Session.session = session;
    }

    static com.facebook.Session session;


}
