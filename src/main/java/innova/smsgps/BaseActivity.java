package innova.smsgps;

import android.app.Activity;
import android.os.Bundle;

import innova.smsgps.communication.ManagerBridgeIPC;
import innova.smsgps.infomovil.ManagerInfoMovil;
import innova.smsgps.listener.TimerTarea;
import innova.smsgps.managerhttp.Httppostaux;
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
    private ManagerBridgeIPC managerBridgeIPC   = null;     // PUENTE DE COMUNICACIÓN ENTRE PROCEDOS.

    Httppostaux post;

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
        post                = new Httppostaux();
        managerBridgeIPC    = new ManagerBridgeIPC(this);

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
        try
        {
            managerBridgeIPC.DesconectarnosDelServicio();
        } catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }
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

    /**
     * Simple método para enviar mensajes al servidor IPC
     */
    public void enviarMensajeIPC(int indice, String[] data)
    {
        managerBridgeIPC.enviarMensaje(indice, data);
    }



}
