package innova.smsgps.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import innova.smsgps.R;
import innova.smsgps.utils.Utils;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityContactos extends BaseActivity
{
    public static final int REQUESTCONTACTO     = 10;
    public static final String EXTRACONTACTO    = "EXTRA_CONTACTO";


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(getLayoutInflater().inflate(R.layout.abs_title, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        setContentView(R.layout.activity_contactos);
    }


    @Override
    public void listenerTimer()
    {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUESTCONTACTO)
        {
            if (resultCode == RESULT_OK)
            {
                Utils.LOG("ok result");
            }
        }
    }

    public void onClick(View view)
    {
        Intent intent = new Intent(this, ActivityContacto.class);
        switch (view.getId())
        {
            case R.id.rlContacto01:
                intent.putExtra(EXTRACONTACTO, 1);
                startActivityForResult(intent, REQUESTCONTACTO);
                break;
            case R.id.rlContacto02:
                intent.putExtra(EXTRACONTACTO, 2);
                startActivityForResult(intent, REQUESTCONTACTO);
                break;
            case R.id.rlContacto03:
                intent.putExtra(EXTRACONTACTO, 3);
                startActivityForResult(intent, REQUESTCONTACTO);
                break;
            case R.id.rlContacto04:
                intent.putExtra(EXTRACONTACTO, 4);
                startActivityForResult(intent, REQUESTCONTACTO);
                break;
        }
    }



}



