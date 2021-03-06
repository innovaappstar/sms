package innova.smsgps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivitySelfieOpciones extends BaseActivity{

    Intent intent ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_selfie_apolo);

    }

    @Override
    public void onResume()
    {
        super.onResume();

    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void listenerTimer() {

    }


    /**
     * Evento que se ejecutará al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.contenedorCamara:
//                enviarMensajeIPC(BridgeIPC.INDICE_SELFIE_ANDROID, new String[]{"3|1", "Camera"});

                intent = new Intent(this, ActivityCameraPhoto.class);
                break;
        }
        startActivity(intent);
    }


    @Override
    public void RecepcionMensaje(int activity, int tipo) {
        if (activity == 1 && tipo == 1)
        {
            intent = new Intent(this, ActivityCameraPhoto.class);
        }else if (activity == 1 && tipo == 2)
        {
            intent = new Intent(this, ActivityGrabarVideo.class);
        }

        if (intent != null)
            startActivity(intent);

    }
}



