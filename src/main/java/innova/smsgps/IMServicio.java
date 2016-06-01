package innova.smsgps;

import android.os.Bundle;
import android.os.Message;

import com.facebook.Request;
import com.facebook.Response;

import java.sql.SQLException;

import innova.smsgps.application.Globals;
import innova.smsgps.beans.Coordenada;
import innova.smsgps.communication.BridgeIPC;
import innova.smsgps.dao.RegistroTrackDAO;
import innova.smsgps.entities.RegistroTrack;
import innova.smsgps.entities.RegistroTrackUser;
import innova.smsgps.enums.IDSP1;
import innova.smsgps.task.RegistroTrackAsyncTask;
import innova.smsgps.utils.Utils;

/**
 * Created by USUARIO on 29/05/2016.
 */
public class IMServicio  extends BaseServicio
{
    // object's access data
    RegistroTrackDAO registroTrackDAO = new RegistroTrackDAO();

    public static final String B1_01 = "BotonLocationClick01";
    public static final String B1_02 = "BotonLocationClick02";
    public static final String B1_03 = "BotonLocationClick03";

    private static final int ALERTA         = 1;
    private static final int SEGUIMIENTO    = 2;

    private static int TIPOLOCALIZACION = ALERTA;

    private int iContadorLocation = 0;
    private final int DELAYLOCATION = 8;
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
                if (message.arg1 == BridgeIPC.INDICE_LOCATION)
                {
                    Bundle bundle = message.getData();
                    if (bundle != null)
                    {
                        // detenemos localización...
                        super.DetenerLocalizacion();

                        String[] data = bundle.getStringArray(BridgeIPC.NOMBRE_BUNDLE);
                        if (data[0].equals(B1_01))
                        {
//                            managerUtils.imprimirToast(IMServicio.this, B1_01);
                            TIPOLOCALIZACION = ALERTA;
                            Globals.getInfoMovil().setSpf1(IDSP1.TRACKING, TIPOLOCALIZACION);
                            super.IniciarLocalizacion();
                        }else if (data[0].equals(B1_02))
                        {
//                            managerUtils.imprimirToast(IMServicio.this, B1_02);
                            TIPOLOCALIZACION = SEGUIMIENTO;
                            Globals.getInfoMovil().setSpf1(IDSP1.TRACKING, TIPOLOCALIZACION);
                            super.IniciarLocalizacion();
                        }else if (data[0].equals(B1_03))
                        {
//                            super.DetenerLocalizacion();
                            Globals.getInfoMovil().setSpf1(IDSP1.TRACKING, 0); // inactivo..
                        }

                    }
                }
                break;
        }
    }



    @Override
    public void onCreate()
    {
        super.onCreate();
        //region cargar gps location
        if (Globals.getInfoMovil().getSPF1(IDSP1.TRACKING) == RegistroTrack.TIPOALERTA  || Globals.getInfoMovil().getSPF1(IDSP1.TRACKING) == RegistroTrack.TIPOSEGUIMIENTO)
        {
            TIPOLOCALIZACION = Globals.getInfoMovil().getSPF1(IDSP1.TRACKING);
            super.IniciarLocalizacion();
        }
        //endregion
        managerUtils.imprimirToast(mContext, "sms");
//        mMessenger =  new Messenger(new IncomingIPC(this));
    }
    @Override
    public void onDestroy()
    {
        DetenerLocalizacion();
    }

    @Override
    public void getCoordenada(Coordenada coordenada)
    {
        //region TIPO-ALERTA
        if (TIPOLOCALIZACION == ALERTA)
        {
            // SQLite
            RegistroTrack registroTrack = new RegistroTrack(coordenada._getLatitud(), coordenada._getLongitud(), Utils.getFechaHora(0, true));
            try
            {
                registroTrackDAO.insertRegistroTrack(IMServicio.this, registroTrack);
            } catch (SQLException e) {}
            // POST
            String textoShareFacebook = "https://www.google.com/maps?daddr=" + coordenada.getLatitud() +"," + coordenada.getLongitud();
            this.postStatusUpdate(textoShareFacebook);
            // HTTP
            new RegistroTrackAsyncTask(new RegistroTrackAsyncTask.RegistroTrackCallback()
            {
                @Override
                public void onRegistroTrack(RegistroTrackUser registroTrackUser)
                {
                    managerUtils.imprimirToast(IMServicio.this, registroTrackUser.getDescription());
                }
            }, registroTrack).execute();

            try
            {
                registroTrackDAO.insertRegistroTrack(IMServicio.this, registroTrack);
            }catch (Exception e){}

            super.DetenerLocalizacion();
        }
        //endregion
        //region TIPO-SEGUIMIENTO
        else if (TIPOLOCALIZACION == SEGUIMIENTO && iContadorLocation >= DELAYLOCATION)
        {
            RegistroTrack registroTrack = new RegistroTrack(coordenada._getLatitud(), coordenada._getLongitud(), Utils.getFechaHora(0, true), coordenada._getVelocidad(), String.valueOf(managerUtils.getBateria(IMServicio.this)));
            try
            {
                registroTrackDAO.insertRegistroTrack(IMServicio.this, registroTrack);
            } catch (SQLException e) {}
            // HTTP
            new RegistroTrackAsyncTask(new RegistroTrackAsyncTask.RegistroTrackCallback()
            {
                @Override
                public void onRegistroTrack(RegistroTrackUser registroTrackUser)
                {
                    managerUtils.imprimirToast(IMServicio.this, registroTrackUser.getDescription());
                }
            }, registroTrack).execute();

            iContadorLocation = 0;
        }
        //endregion
        iContadorLocation++;
    }


    @Override
    public void listenerTimer()
    {
    }


    /**
     * @param message String post fb
     */
    private void postStatusUpdate(final String message)
    {
        Request request = Request.newStatusUpdateRequest(sessionbeans.getSession().getActiveSession(), message, new Request.Callback() {
                    @Override
                    public void onCompleted(Response response) {
                        if (response.getError() != null)
                        {
//                            managerUtils.imprimirToast(mContext, response.getGraphObject() + "|" + response.getError());
                            managerUtils.imprimirToast(mContext, "no se logró actualziar el post en fb.");
                            managerUtils.showNotificacionSimple(mContext);
                        }else
                        {
                            managerUtils.imprimirToast(mContext, "Estado de facebook actualizado.");
                        }

                    }
                });
        request.executeAsync();
    }


    //endregion
}