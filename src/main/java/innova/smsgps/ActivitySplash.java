package innova.smsgps;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivitySplash extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_bienvenida_detalle);
    }
    /**
     * Evento que se ejecutará al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */

    public void onClickSplash(View view) {
        switch (view.getId()) {
            case R.id.imgSplashFacebook:
                imprimitToast("Autenticando...");
                break;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAceptar:
                imprimitToast("Aceptar..");
                break;
        }
    }
    private void imprimitToast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
}



