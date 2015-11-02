package innova.smsgps;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

/**
 * Created by USUARIO on 01/11/2015.
 */
public class SmsActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sms_gps);
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


    public void onClickSmsGps(View view)
    {
        switch (view.getId())
        {
            case R.id.btnEnviarSms:
                String[] strSms = new String[]{"951316806", "Hola como estas , enviado desde el equipo S5"};
                if (enviarSms(strSms[0], strSms[1]) == 1)
                {
                    Toast.makeText(this, "Se envió el sms correctamente", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(this, "Ocurrió un error al enviar el SMS", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



}
