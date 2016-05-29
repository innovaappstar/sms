package innova.smsgps.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import innova.smsgps.R;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityFuncionesLocation extends BaseActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(getLayoutInflater().inflate(R.layout.abs_title, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        setContentView(R.layout.activity_funciones_location);
    }


    @Override
    public void listenerTimer()
    {
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
        }
    }



}



