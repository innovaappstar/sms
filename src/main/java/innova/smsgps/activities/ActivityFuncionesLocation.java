package innova.smsgps.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import innova.smsgps.IMServicio;
import innova.smsgps.R;
import innova.smsgps.application.Globals;
import innova.smsgps.communication.BridgeIPC;
import innova.smsgps.entities.RegistroTrack;
import innova.smsgps.enums.IDSP1;
import innova.smsgps.utils.Utils;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityFuncionesLocation extends BaseActivity
{

    boolean isPulso = false;
    int iContadorPulso = 2;
    int iNumClicks      = 0;
    RelativeLayout  rlLocation02 ;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(getLayoutInflater().inflate(R.layout.abs_title, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        setContentView(R.layout.activity_funciones_location);

        rlLocation02    = (RelativeLayout)findViewById(R.id.rlLocation02);

        rlLocation02.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    Utils.LOG("DOWN");
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    Utils.LOG("UP");
                    enviarMensajeIPC(BridgeIPC.INDICE_LOCATION, new String[]{IMServicio.B1_03, "LOCATION3"});
                    rlLocation02.setVisibility(View.GONE);
                    return true;
                }
                return true;
            }
        });


        //region cargar gps location
        if (Globals.getInfoMovil().getSPF1(IDSP1.TRACKING) == RegistroTrack.TIPOSEGUIMIENTO)
            rlLocation02.setVisibility(View.VISIBLE);
        //endregion

    }


    @Override
    public void listenerTimer()
    {
        if (iContadorPulso == 1)
        {
            if (iNumClicks == 1)
            {
                enviarMensajeIPC(BridgeIPC.INDICE_LOCATION, new String[]{IMServicio.B1_01, "LOCATION"});
            }else if (iNumClicks >= 2)
            {
                enviarMensajeIPC(BridgeIPC.INDICE_LOCATION, new String[]{IMServicio.B1_02, "LOCATION"});
                rlLocation02.setVisibility(View.VISIBLE);
                // grabar spf
            }

            iNumClicks = 0;
            isPulso = false;
        }

        iContadorPulso ++;
    }


    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.rlContactos:
                startActivity(new Intent(ActivityFuncionesLocation.this, ActivityContactos.class));
                break;
            case R.id.rlAddAccountFacebook:
                startActivity(new Intent(ActivityFuncionesLocation.this, ActivityAddAccountFacebook.class));
                break;
            case R.id.rlMapTrack:
                startActivity(new Intent(ActivityFuncionesLocation.this, ActivityMapTrack.class));
                break;
            case R.id.rlLocation:
                if (!isPulso)
                    iContadorPulso = 0;

                iNumClicks++;

                if (iNumClicks >= 2)
                    iContadorPulso = 1;
                isPulso = true;
                break;
            case R.id.rlHistorialTrack:
                startActivity(new Intent(ActivityFuncionesLocation.this, ActivityListaTrack.class));
                break;
        }
    }



}



