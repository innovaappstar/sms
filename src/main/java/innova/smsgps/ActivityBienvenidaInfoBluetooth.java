package innova.smsgps;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import innova.smsgps.application.Globals;
import innova.smsgps.enums.IDSP1;

/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityBienvenidaInfoBluetooth extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_bienvenida_info_bluetooth);
        Globals.getInfoMovil().setSpf1(IDSP1.FLAGSPLASH, 1);

    }
    /**
     * Evento que se ejecutará al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnAceptar:
                startActivity(new Intent(this, ActivityMenu.class));
                finish();
                break;
        }
    }
    private void imprimitToast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
}



