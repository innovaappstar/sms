package innova.smsgps.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import innova.smsgps.R;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivitySplash extends BaseActivity
{

    // contador splash
    private int iContadorSplash = 0;
    private int iTiempoLimiteSplash = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);

    }


    @Override
    public void listenerTimer()
    {
        if (iContadorSplash == iTiempoLimiteSplash)
        {
            startActivity(new Intent(ActivitySplash.this, ActivityLogin.class));
            finish();
        }

        iContadorSplash ++;
    }


    /**
     * Evento que se ejecutará al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */
    public void onClick(View view)
    {
    }


}



