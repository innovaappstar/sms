package innova.smsgps;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import innova.smsgps.beans.Coordenada;
import innova.smsgps.communication.BridgeIPC;
import innova.smsgps.communication.IncomingIPC;
import innova.smsgps.enums.IDSP1;
import innova.smsgps.task.UpAlerta;

/**
 * Created by USUARIO on 02/11/2015.
 */

public class ServicioSms extends BaseServicio  {

    /**
     * Instancias bluetooth y objetos.
     */
    static InputStream mmInputStream;
    static Handler mHandler = new Handler();


    // INDICES IPC
    public static final int MSG_REGISTRAR_CLIENTE	= 1;
    public static final int MSG_ELIMINAR_CLIENTE	= 2;
    public static final int MSG_SET_INT_VALOR		= 3;
    public static final int MSG_SET_STRING_VALOR	= 4;

    @Override
    public void IncomingIPC(Message message)
    {
        switch (message.what)
        {
            case MSG_REGISTRAR_CLIENTE:
//                mClientes.add(message.replyTo);
                //Toast.makeText(getApplicationContext(), "Nuevo cliente conectado..", Toast.LENGTH_SHORT).show();
                break;
            case MSG_ELIMINAR_CLIENTE:
//                mClientes.remove(message.replyTo);
                break;
            case MSG_SET_STRING_VALOR:
                // COMUNICACIÓN DESDE BASE ACTIVITY - CLIENTE
                if (message.arg1 == BridgeIPC.INDICE_WEBSOCKET)
                {
                    Bundle bundle = message.getData();
                    if (bundle != null)
                    {
                        String[] data = bundle.getStringArray(BridgeIPC.NOMBRE_BUNDLE);
//                        managerUtils.imprimirToast(this, data[0]);

                        // SÓLO PARA PRUEBAS DE DESARROLLO REGISTRAREMOS ALERTAS DESDE AQUÍ
                        if (data[0].equals("1"))
                        {
                            new UpAlerta(mContext, 0);
                            postStatusUpdate("Prueba integrada ... " + (new Date().toString()));
                        }
                    }
                }else if (message.arg1 == BridgeIPC.INDICE_LOCATION)
                {
                    Bundle bundle = message.getData();
                    if (bundle != null)
                    {
                        String[] data = bundle.getStringArray(BridgeIPC.NOMBRE_BUNDLE);
                        int mTipoAlerta = 0;
                        if (data[0].equals("1|1"))  // Consultamos el BEEP 01
                        {
                            mTipoAlerta = managerInfoMovil.getSPF1(IDSP1.BEEP1);
                        }else if (data[0].equals("1|2"))
                        {
                            mTipoAlerta = managerInfoMovil.getSPF1(IDSP1.BEEP2);
                        }else if (data[0].equals("1|3"))
                        {
                            mTipoAlerta = managerInfoMovil.getSPF1(IDSP1.BEEP3);
                        }
                        new UpAlerta(mContext, mTipoAlerta);  // esto será estático porque quien lo enviará será el bluetooth
                        postStatusUpdate("Prueba integrada ... " + mTipoAlerta + " --- " + (new Date().toString()));
                    }
                }
                break;
            case MSG_SET_INT_VALOR:
                Toast.makeText(getApplicationContext(), message.arg2 + " <I>", Toast.LENGTH_SHORT).show();
                break;
            //default:
            //IncomingIPC(message);
        }
    }



    @Override
    public void onCreate() {
        super.onCreate();
        mMessenger =  new Messenger(new IncomingIPC(this));
    }

    @Override
    public void getCoordenada(Coordenada coordenada) {
//        Toast.makeText(mContext, coordenada._getLatitud() + "|" + coordenada._getLongitud(), Toast.LENGTH_SHORT).show();
    }



    static BufferedReader r = null;
    static String mSalida="";

    static void escuchar()
    {
        /**
         *  Correr funcin de lectura con las claves obtenidas
         **/
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Get key map from glob. variable. 'SparseArray<String[]>'
                /** Obtendremos el valor de los bloques*/
                while (true)
                {
                    //mmOutputStream.write(params[0].getBytes());
                    //Log.i("smsgps", "1");
//                    if (r != null)
//                    {
                        r = new BufferedReader(new InputStreamReader(mmInputStream));
                        Log.i("smsgps", "2");
                        try {
                            mSalida = r.readLine().toString();
                            mHandler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (mSalida.equals("1"))
                                    {
                                        new UpAlerta(mContext, 0);
                                        //postStatusUpdate("Prueba integrada ... " + (new Date().toString()));
                                    }
                                    Toast.makeText(mContext, mSalida, Toast.LENGTH_SHORT).show();
//                                    SmsActivity.Contador = 0;
                                }
                            });
                        }catch (Exception e)
                        {
                            Log.i("smsgps", e.getMessage());

                        }
//                    }


                }


            }
        }).start();
    }




    private void postStatusUpdate(final String message) {
        if (sessionbeans != null)
        {
            Request request = Request
                    .newStatusUpdateRequest(sessionbeans.getSession().getActiveSession(), message, new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            if (response.getError() != null)
                            {
                                managerUtils.imprimirToast(mContext, response.getGraphObject() + "|" + response.getError());
                                // INICIAMOS NOTIFICACIÓN
                                managerUtils.showNotificacionSimple(mContext);
                            }else
                            {
                                managerUtils.imprimirToast(mContext, "Se actualizo correctamente");
//                                managerUtils.imprimirToast(mContext, response.toString());
                            }

                        }
                    });
            request.executeAsync();
        }
    }


    //endregion
}
