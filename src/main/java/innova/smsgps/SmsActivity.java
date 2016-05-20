package innova.smsgps;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import innova.smsgps.listener.TimerTarea;

/**
 * Created by USUARIO on 01/11/2015.
 */
public class SmsActivity extends Activity implements TimerTarea.TimerTareaCallback
{
    /**
     * Ordenes Bluetooth
     */
    private String Ordenes[]=new String[]{"A","B","C","D"};
    static TextView txtTimer ;
    static int Contador ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.fragment_sms_gps);
        TimerTarea timerTarea = new TimerTarea(this);

        iniciarServicio();
        txtTimer = (TextView)findViewById(R.id.txtSalida);
     }

    private int  enviarSms(String numero, String Data)
    {

        int result = 1;
        // Instanciamos el objeto smsManager
        SmsManager smsManager = SmsManager.getDefault();
        try {
            smsManager.sendTextMessage(numero, null, Data, null, null);
        }catch(Exception e)
        {
            result = -1;    // ERROR
        }finally {
            return result;
        }

    }
    public void iniciarServicio() {
        // Construct our Intent specifying the Service
        Intent i = new Intent(this, ServicioSms.class);
        // Add extras to the bundle
        i.putExtra("foo", "bar");
        // Start the service
        startService(i);
    }


    public void onClickSmsGps(View view)
    {
        switch (view.getId())
        {
            case R.id.btnEnviarSms:
                String[] strSms = new String[]{"951316806", "Hola como estas , enviado desde el equipo S5"};
                /*
                if (enviarSms(strSms[0], strSms[1]) == 1)
                {
                    Toast.makeText(this, "Se envi el sms correctamente", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(this, "Ocurri un error al enviar el SMS", Toast.LENGTH_SHORT).show();
                }
                */

                break;
            case R.id.btnBluetooth:
                //ServicioSms.AsynTaskOrden AsynkOrden = new ServicioSms.AsynTaskOrden();
                //AsynkOrden.execute(Ordenes[0]);
//                ServicioSms.escuchar();
                break;
        }
    }


    @Override
    public void TimerTareaExecute()
    {
        txtTimer.setText(Contador + "");
        Contador ++;
    }



}
