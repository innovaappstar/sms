package innova.smsgps.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import innova.smsgps.IMServicio;
import innova.smsgps.ServicioSms;
import innova.smsgps.beans.Coordenada;
import innova.smsgps.infomovil.ManagerInfoMovil;
import innova.smsgps.utils.ManagerUtils;

/**
 * Created by innovaapps on 24/09/2015.
 */
public class Globals extends Application {


    /**Contexto Global (m)*/
    private static Context mAppContext;
    public static com.facebook.Session session;
    /**IUtils*/
    private static ManagerUtils managerUtils;

    private static ManagerInfoMovil managerInfoMovil ;

    public static final ImageLoader imageLoader = ImageLoader.getInstance();
    @Override
    public void onCreate()
    {
        super.onCreate();
        mAppContext = getApplicationContext();
        managerUtils = new ManagerUtils();
        managerInfoMovil = new ManagerInfoMovil(mAppContext);
        // INICIAR SERVICIO
        iniciarServicio();

        //region inicializamos el imageLoader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mAppContext)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        imageLoader.init(config);
        //endregion
     }
    /**
     * Iniciar Servicio de Geolocalización en segundo plano...
     * #ServicioLocalizacion.java
     * Comprobar si una instancia del servicio esta ejecutandose..
     */
    private void iniciarServicio()
    {
        if(!ServicioSms.isRunning())
            startService(new Intent(this, IMServicio.class));
    }
    /**
     * get/set Session facebook
     **/
    public static com.facebook.Session getSession() {
        return session;
    }

    public static void setSession(com.facebook.Session session) {
        Globals.session = session;
    }

    /**
     * Post en Facebook
     */

    public static void PostearSegundoPlano(Coordenada coordenada)
    {
        postStatusInFacebook(coordenada);
        Toast.makeText(mAppContext, "POST STATUS", Toast.LENGTH_SHORT).show();
    }

    /**
     * Set parametros que se mostrarán en facebook
     */
    private static void postStatusInFacebook(Coordenada coordenada) {

        Bundle params = new Bundle();
        params.putString("caption", coordenada._getLatitud() + "|" + coordenada._getLongitud());
        params.putString("message", "Alerta SMS GPS.");
        params.putString("link", "https://www.youtube.com/watch?v=1uQG9yc-raQ");
        params.putString("picture", "http://www.nvtl.com/files/8713/6210/9612/map_gps_NVTL.jpg");

        Request request = new Request(getSession().getActiveSession(), "me/feed", params, HttpMethod.POST);
        request.setCallback(new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                if (response.getError() == null) {
                    // Tell the user success!
                } else if (response.getError().getErrorCode() == 2500)    // session null
                {
                    Toast.makeText(mAppContext, response.getError().toString(), Toast.LENGTH_SHORT).show();
                    managerUtils.showNotificacionSimple(mAppContext);
                } else
                {
                    // POSIBLES EXCEPCIONES DE PERMISOS DENEGADOS
                }
            }
        });
        request.executeAsync();
    }

    /**
     * Instancian Info Movil
     **/
    public static ManagerInfoMovil getInfoMovil()
    {
        return managerInfoMovil;
    }


//    // public static ManagerUtils getManagerUtils()
//    {
//        return null;
//    }



}
