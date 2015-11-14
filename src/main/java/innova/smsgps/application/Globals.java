package innova.smsgps.application;

import android.app.Application;
import android.content.Context;
import android.nfc.NfcAdapter;

import innova.smsgps.utils.ManagerUtils;

/**
 * Created by innovaapps on 24/09/2015.
 */
public class Globals extends Application{

    /**Adaptador NFC Global (m)*/
    private static NfcAdapter mNfcAdapter;
    /**Contexto Clobal (m)*/
    private static Context mAppContext;
    public static com.facebook.Session session;
    /**IUtils*/
    ManagerUtils managerUtils;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mAppContext = getApplicationContext();
        //managerUtils = new
     }

    public static com.facebook.Session getSession() {
        return session;
    }

    public static void setSession(com.facebook.Session session) {
        Globals.session = session;
    }

    public static ManagerUtils getManagerUtils()
    {
        return null;
    }


}
