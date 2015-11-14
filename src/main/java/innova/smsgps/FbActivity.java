package innova.smsgps;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * Created by USUARIO on 01/11/2015.
 */
public class FbActivity extends Activity implements TimerTarea.TimerTareaCallback
{
    static int Contador ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.fragment_fb);
        TimerTarea timerTarea = new TimerTarea(this);

    }


    public void onClickSmsGps(View view)
    {
        switch (view.getId())
        {
            case R.id.btnEnviarSms:

                break;
            case R.id.btnBluetooth:
                ServicioSms.escuchar();
                break;
        }
    }


    @Override
    public void TimerTareaExecute()
    {
        Contador ++;
    }



}
