package innova.smsgps.communication;

import android.os.Handler;
import android.os.Message;

/**
 * Created by innovaapps on 13/11/2015.
 */
public class IncomingIPC extends Handler
{
    /**
     * Callback que actuará de listener...
     **/
    IncomingIpcCallback incomingIpcCallback;

    /**
     * Interfaz que contendra el callback..
     */
    public interface IncomingIpcCallback
    {
        void IncomingIPC(Message message);
    }

    /**
     * Manejador entrante de mensajes que se recibiran
     * de los clientes (Activitys).
     */
    public IncomingIPC(IncomingIpcCallback incomingIpcCallback)
    {
        this.incomingIpcCallback = incomingIpcCallback;
    }

    /**
     * Manejador entrante de mensajes que se recibiran
     * de los clientes (Activitys).
     * Implementarlo por medio de un callback +Adelante.
     */
    @Override
    public void handleMessage(Message message)
    {
        incomingIpcCallback.IncomingIPC(message);
    }
}
