package innova.smsgps;

import android.support.v4.app.Fragment;
import innova.smsgps.infomovil.ManagerInfoMovil;
import innova.smsgps.sqlite.ManagerSqlite;
import innova.smsgps.utils.ManagerUtils;


/**
 * Created by innovaapps on 21/09/2015.
 */
public abstract class BaseFragment extends Fragment implements TimerTarea.TimerTareaCallback
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


    public void IniciarInstancias()
    {
        // LLAMADA INTERNA
        objTimer = new TimerTarea(this);
        managerInfoMovil    = new ManagerInfoMovil(getActivity());
        managerUtils        = new ManagerUtils();
        managerSqlite       = new ManagerSqlite(getActivity());
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
