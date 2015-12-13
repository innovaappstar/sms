package innova.smsgps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityMenuOpciones extends BaseActivity{

    Intent intent ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_opciones_menu_apolo);
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
            case R.id.btnAceptar:
//                imprimitToast("Aceptar..");
                break;
        }

        startActivity(intent);
    }

    private void imprimitToast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }


}



