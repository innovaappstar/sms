package innova.smsgps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import innova.smsgps.communication.BridgeIPC;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityMenu extends BaseActivity{

    Intent intent ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_menu_apolo);

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
            case R.id.contenedor_device:
                intent = new Intent(this, ActivityCameraPhoto.class);
                break;
            case R.id.contenedor_gps:
                intent = new Intent(this, ActivityMenuOpciones.class);
                break;
            case R.id.contenedor_selfie:
                intent = new Intent(this, ActivitySelfieOpciones.class);
                break;
            case R.id.contenedor_other:
                intent = new Intent(this, ActivityOthers.class);
                break;
            case R.id.contenedor_media:
                intent = new Intent(this, ActivityMediaOpciones.class);
                break;
            case R.id.btnAceptar:
//                imprimitToast("Aceptar..");
                break;
        }

        startActivity(intent);
    }

    private void imprimitToast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }


    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.compartirUbicacion:
                enviarMensajeIPC(BridgeIPC.INDICE_LOCATION, new String[]{"1|1", "Bye Mundo"});
                //managerUtils.imprimirToast(this, "Locación");
//                finish();
                return true;
            case R.id.iniciarSound:
                enviarMensajeIPC(BridgeIPC.INDICE_LOST_ANDROID, new String[]{"2|1", "Iniciar Sonido"});
                return true;
            case R.id.detenerSound:
                enviarMensajeIPC(BridgeIPC.INDICE_LOST_ANDROID, new String[]{"2|2", "Detener Sonido"});
                return true;
            case R.id.toogleLED:
                enviarMensajeIPC(BridgeIPC.INDICE_LOST_ANDROID, new String[]{"2|3", "Switch Led"});
                return true;
            // CONTROLES MUSIC
            case R.id.toogleMusic:
                enviarMensajeIPC(BridgeIPC.INDICE_MUSIC_ANDROID, new String[]{"4|1", "Play Music"});
                return true;
            case R.id.nextMusic:
                enviarMensajeIPC(BridgeIPC.INDICE_MUSIC_ANDROID, new String[]{"4|2", "Next Song"});
                return true;
            case R.id.backMusic:
                enviarMensajeIPC(BridgeIPC.INDICE_MUSIC_ANDROID, new String[]{"4|3", "Back Song"});
                return true;
            case R.id.Selfie:
                return true;
            case R.id.PlayMusic:
                return true;
            case R.id.call_responder:
                enviarMensajeIPC(BridgeIPC.INDICE_CALL_ANDROID, new String[]{"5|1", "Responder LLamadas"});
                return true;
            case R.id.call_altavoz:
                enviarMensajeIPC(BridgeIPC.INDICE_CALL_ANDROID, new String[]{"5|2", "Altavoz"});
                return true;
            case R.id.call_colgar:
                enviarMensajeIPC(BridgeIPC.INDICE_CALL_ANDROID, new String[]{"5|3", "Colgar LLamada"});
                return true;
            case R.id.GoogleMaps:
                enviarMensajeIPC(BridgeIPC.INDICE_GOOGLE_MAP_ANDROID, new String[]{"6|1", "Open Google Map"});
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}



