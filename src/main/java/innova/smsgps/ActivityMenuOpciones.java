package innova.smsgps;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityMenuOpciones extends BaseActivity{

    Intent intent ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                managerUtils.imprimirToast(getApplicationContext(), "Se guardaron correctamente.");
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                //Write your code if there's no result

            }
        }
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
                return;
            case R.id.contenedor_contactos:
                intent = new Intent(this, ActivityListaContactos.class);
                break;
            case R.id.contenedor_add_facebook:
                intent = new Intent(this, ActivityFacebookAccount.class);
                break;
            case R.id.contenedor_mapa_alertas:
                intent = new Intent(this, ActivityMapaAlertas.class);
                break;
            case R.id.contenedor_denuncias:
                managerUtils.imprimirToast(this, "denuncias");
                break;

        }

        if (intent != null)
            startActivityForResult(intent, 1);
    }


}



