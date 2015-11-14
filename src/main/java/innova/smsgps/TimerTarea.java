package innova.smsgps;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by innovaapps on 23/10/2015.
 */
public class TimerTarea extends TimerTask{


    /**
     * CALLBACK QUE ACTUARÁ DE LISTENER
     **/
    TimerTareaCallback tareaCallback;
    /**
     * MANEJADOR DEL TIMER PARA PODER INTERACTUAR
     * CON LA UI. (Hilo manejador del reloj)
     **/
    Handler handlerTimer = new Handler();
    /**
     * VALORES DE RETARDO Y PERIOSO
     **/
    private int mDelay  = 0000;
    private int mPeriod = 1000;


    /**
     *   CONSTRUCTOR DE LA CLASE QUE INSTANCIA EL CALLBACK E INICIA LAS FUNCIONES DEL TIMER
     *   @param callback EL CALLBACK QUE ACTUARÁ DE LISTENER
     **/
    public TimerTarea(TimerTareaCallback callback)
    {
        tareaCallback = callback;
        new Timer().schedule(this, mDelay, mPeriod);
    }

    /**
     * INTERFAZ QUE CONTENDRÁ EL MÉTODO CALLBACK
     **/
    public interface TimerTareaCallback
    {
        void TimerTareaExecute();
    }


    /**
     * TAREA QUE SE EJECUTARÁ EN EL MÉTODO RUN()
     **/
    public void run()
    {
        //HILO DEL RELOJ QUE SE ENCARGARÁ DE PODER REALIZAR TAREAS EN UN HILO APARTE
        handlerTimer.post(new Runnable() {
            public void run() {
                tareaCallback.TimerTareaExecute();
                //Log.i("PrintReader", "TIMER-TAREA TASK");
            }
        });
    }
}
