package innova.smsgps;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    /**
     * Tipo Alerta que se enviará al servidor..
     **/
    int mTipoAlerta     = 0;
    boolean isEnviado       = true;
    int mUsoLocalizacion    = 0;
    public static int ALERTA    = 1;
    public static int DENUNCIA  = 2;
    boolean isApagarLed     = true;
    private static ServicioCallback servicioCallback = null;
    boolean isPausarMusica  = false;

    boolean isAltavoz       = true;

    public interface ServicioCallback
    {
        void RecepcionMensaje(int activity, int tipo);
        void RecepcionCoordenadas(int activity, Coordenada coordenada);
    }


    /**
     * Simple set de base activity
     * @param servicioCallback
     */
    public static void setServicioCallback(ServicioCallback servicioCallback)
    {
        ServicioSms.servicioCallback   = servicioCallback;
    }

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
                        mTipoAlerta = 0;
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
                        isEnviado           = false;
                        mUsoLocalizacion    = ALERTA;
                        IniciarLocalizacion();
//                        new UpAlerta(mContext, mTipoAlerta);  // esto será estático porque quien lo enviará será el bluetooth
//                        postStatusUpdate("Prueba integrada ... " + mTipoAlerta + " --- " + (new Date().toString()));
                    }
                }else if (message.arg1 == BridgeIPC.INDICE_LOST_ANDROID)
                {
                    Bundle bundle = message.getData();
                    if (bundle != null)
                    {
                        String[] data = bundle.getStringArray(BridgeIPC.NOMBRE_BUNDLE);
                        mTipoAlerta = 0;
                        if (data[0].equals("2|1"))  // INICIAMOS RINGTONE
                        {
                            IniciarRingtone();
                        }else if (data[0].equals("2|2"))    // DETENEMOS RINGTONE
                        {
                            DetenerRingtone();
                        }else if (data[0].equals("2|3"))    // ENCENDEMOS LINTERNA
                        {
                            isApagarLed = !isApagarLed;
                            ToogleLed(isApagarLed);
                        }
                    }
                }else if (message.arg1 == BridgeIPC.INDICE_SELFIE_ANDROID)
                {
                    Bundle bundle = message.getData();
                    if (bundle != null)
                    {
                        String[] data = bundle.getStringArray(BridgeIPC.NOMBRE_BUNDLE);
                        if (data[0].equals("3|1"))  //  CAPTURAR FOTO
                        {
//                            servicioCallback.RecepcionMensaje(1, 1);
                            servicioCallback.RecepcionMensaje(2, 1);
                        }else if (data[0].equals("3|2"))    // GRABAR VIDEO
                        {
//                            servicioCallback.RecepcionMensaje(1, 2);
                            servicioCallback.RecepcionMensaje(2, 2);
                        }else if (data[0].equals("3|3"))    // DETENER VIDEO
                        {

                        }
                    }
                }else if (message.arg1 == BridgeIPC.INDICE_MUSIC_ANDROID)
                {
                    Bundle bundle = message.getData();
                    if (bundle != null)
                    {
                        String[] data = bundle.getStringArray(BridgeIPC.NOMBRE_BUNDLE);
//                        managerUtils.showNotificacionMusic(this);

                        if (data[0].equals("4|1"))      //  PLAY MUSIC / PAUSE
                        {
                            if (IniciarMusica(isPausarMusica) != 0)
                                managerUtils.showNotificacionMusic(this);
//                            IniciarMusica(isPausarMusica);
                            isPausarMusica = !isPausarMusica;
                        }else if (data[0].equals("4|2"))    // NEXT MUSIC
                        {
                            if (CambiarCancion(true) != 0)
                                managerUtils.showNotificacionMusic(this);
                        }else if (data[0].equals("4|3"))    // BACK MUSIC
                        {
                            if (CambiarCancion(false) != 0)
                                managerUtils.showNotificacionMusic(this);
                        }
                    }
                }else if (message.arg1 == BridgeIPC.INDICE_CALL_ANDROID)
                {
                    Bundle bundle = message.getData();
                    if (bundle != null)
                    {
                        String[] data = bundle.getStringArray(BridgeIPC.NOMBRE_BUNDLE);
                        if (data[0].equals("5|1"))          // ACEPTAR LLAMADA
                        {
                            aceptarLLamada();
                        }else if (data[0].equals("5|2"))    // ALTAVOZ
                        {
                            toogleAltavoz(isAltavoz);
                            isAltavoz = !isAltavoz;
                        }else if (data[0].equals("5|3"))    // COLGAR LLAMADA
                        {
                            colgarLLamada();
                        }
                    }
                }else if (message.arg1 == BridgeIPC.INDICE_GOOGLE_MAP_ANDROID)
                {
                    Bundle bundle = message.getData();
                    if (bundle != null)
                    {
                        String[] data = bundle.getStringArray(BridgeIPC.NOMBRE_BUNDLE);
                        if (data[0].equals("6|1"))          // GOOGLE MAPS
                        {
                            openApp(this);
//                            managerUtils.imprimirToast(this, "OPEN APP");
                        }
                    }
                }else if (message.arg1 == BridgeIPC.INDICE_DENUNCIA_ANDROID)
                {
                    Bundle bundle = message.getData();
                    if (bundle != null)
                    {
                        String[] data = bundle.getStringArray(BridgeIPC.NOMBRE_BUNDLE);
                        if (data[0].equals("7|1"))          // OBTENER COORDENADAS
                        {
                            IniciarLocalizacion();
                            mUsoLocalizacion    = DENUNCIA;
                        }
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
//        mMessenger =  new Messenger(new IncomingIPC(this));
    }

    @Override
    public void getCoordenada(Coordenada coordenada)
    {
        if (mUsoLocalizacion == ALERTA)
        {
            if (!isEnviado)
            {
                isEnviado   = true;
                DetenerLocalizacion();
                managerUtils.imprimirToast(this, "Se está enviando..");
                new UpAlerta(mContext, mTipoAlerta);  // esto será estático porque quien lo enviará será el bluetooth
                //postStatusUpdate("https://www.google.com/maps?ll=-12.005097,-77.048391" + "\n--- " + (new Date().toString()));
                //https://www.google.com/maps?daddr=-12.005097,-77.048391
                //postStatusUpdate("https://www.google.com/maps?ll=" + coordenada.getLatitud() +"," + coordenada.getLongitud() );
                postStatusUpdate("https://www.google.com/maps?daddr=" + coordenada.getLatitud() +"," + coordenada.getLongitud() );
                mUsoLocalizacion    = 0;
                // ENVIAR MENSAJE DE TEXTO .. FALTA

            }
        }else if (mUsoLocalizacion == DENUNCIA)
        {
            managerUtils.imprimirToast(this, "OBTENER COORDENADAS");
            servicioCallback.RecepcionCoordenadas(3, coordenada);   // ACTIVITYDENUNCIAREGISTROUPLOAD.JAVA
            DetenerLocalizacion();
            mUsoLocalizacion    = 0;
        }

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
