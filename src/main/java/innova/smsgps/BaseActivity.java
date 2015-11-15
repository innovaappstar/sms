package innova.smsgps;

import android.app.Activity;
import android.os.Bundle;

import innova.smsgps.infomovil.ManagerInfoMovil;
import innova.smsgps.sqlite.ManagerSqlite;
import innova.smsgps.utils.ManagerUtils;


/**
 * Created by innovaapps on 21/09/2015.
 */
public abstract class BaseActivity extends Activity implements TimerTarea.TimerTareaCallback
{
    /**
     * Objetos
     */
    TimerTarea objTimer                         = null;     // LISTENER QUE ACTUALIZARA NUESTRO CALLBACK.
    ManagerInfoMovil managerInfoMovil           = null;     // MANEJADOR SE SPF's.
    ManagerUtils managerUtils                   = null;     // MANEJADOR DE FUNCIONES TIPO UTILS.
    ManagerSqlite managerSqlite                 = null;     // MANEJADOR DE FUNCIONES C.R.U.D. SQLITE.

    /**
     * Habilita sistema NFC en primer plano.
     */

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // LLAMADA INTERNA
        objTimer = new TimerTarea(this);
        managerInfoMovil    = new ManagerInfoMovil(this);
        managerUtils        = new ManagerUtils();
        managerSqlite       = new ManagerSqlite(this);
    }


    /**
     * Deshabilita sistema NFC en primer plano.
     */
    @Override
    public void onPause() {
        super.onPause();
        objTimer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public abstract void listenerTimer();

    /**
     *  MÉTODO LISTENER QUE SE EJECUTA CADA SEGUNDO
     **/
    @Override
    public void TimerTareaExecute()
    {
        listenerTimer();
    }





}
