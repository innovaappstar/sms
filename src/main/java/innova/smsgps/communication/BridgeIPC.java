package innova.smsgps.communication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import innova.smsgps.ServicioSms;
import innova.smsgps.interfaces.IBridgeIPC;

/**
 * Created by innovaapps on 12/11/2015.
 * IPC - Comunicación entre procesos...
 */
public class BridgeIPC implements IBridgeIPC
{
    /**
     * Índices referenciales
     */
    public static final int INDICE_WEBSOCKET    = 1;
    public static final int INDICE_LOCATION     = 2;

    public static final String SUB_INDICE_WEBSOCKET_TRANSACCION_BOLETO      = "1";
    public static final String SUB_INDICE_WEBSOCKET_TRANSACCION_ELECTRONICO = "2";

    public static final String NOMBRE_BUNDLE = "DATA";


    /**Objeto message*/
    Messenger mMessenger ;
    boolean isBound ;       // ESTÁ ATADO
    Context context;

    /**
     * Constructor para setear context e inicializar la conexión
     * con el servidor (Servicio)
     */
    public BridgeIPC(Context context)
    {
        this.context = context;
        ConectarnosAlServicio();
    }

    /**
     * Objeto conexión de servicio que contendra los eventos
     * para detectar si nos conectamos o perdimos la conexión.
     */
    public ServiceConnection mServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            isBound = true;
            mMessenger = new Messenger(service);
            try
            {
                Message msg = Message.obtain(null, ServicioSms.MSG_REGISTRAR_CLIENTE, 0, 0);
                mMessenger.send(msg);
            }catch (RemoteException e)
            {
                // Excepción ...
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            mMessenger  = null;
            isBound     = false;
        }
    };

    /**
     * Nos conectamos al servicio y volvemos true
     * nuestra booleano flag.
     */
    private void ConectarnosAlServicio()
    {
        context.bindService(new Intent(context, ServicioSms.class), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Método para enviar mensajes al servidor..
     */
    @Override
    public void enviarMensaje(int indice, String[] data)
    {
        if (isBound)
        {
            if (mMessenger != null)
            {
                try {
                    Message msg = Message.obtain(null, ServicioSms.MSG_SET_STRING_VALOR, indice, 0);
                    //msg.replyTo = mMessenger;
                    Bundle bundle = new Bundle();
                    bundle.putStringArray(NOMBRE_BUNDLE, data);
                    //bundle.putString("saludo", "hola");
                    // Seteamos los datos del bundle al message
                    msg.setData(bundle);
                    mMessenger.send(msg);
                } catch (RemoteException e) {
                }
            }
        }
    }

    /**
     * Enviamos al servicio message para que nos remueva
     * de su lista de clientes.
     */
    @Override
    public void DesconectarnosDelServicio()  throws Throwable
    {
        if(isBound)
        {
            if (mMessenger != null)
            {
                try
                {
                    Message message = Message.obtain(null, ServicioSms.MSG_ELIMINAR_CLIENTE);
                    message.replyTo = mMessenger;
                    mMessenger.send(message);
                }catch (RemoteException e)
                {

                }
            }
            context.unbindService(mServiceConnection);
            isBound = false;
        }
    }





}
