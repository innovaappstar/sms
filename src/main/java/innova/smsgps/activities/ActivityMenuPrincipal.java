package innova.smsgps.activities;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import innova.smsgps.R;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityMenuPrincipal extends BaseActivity
{
    LinearLayout llFuncionesActionBar;
    boolean isMostrarListaAbs = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(getLayoutInflater().inflate(R.layout.abs_title, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

        setContentView(R.layout.activity_menu_principal);
        llFuncionesActionBar = (LinearLayout)findViewById(R.id.llFuncionesActionBar);

    }


    @Override
    public void listenerTimer()
    {
    }


    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ivActionAbs:
                managerUtils.deslizadoVertical(llFuncionesActionBar, this, isMostrarListaAbs);
                isMostrarListaAbs = !isMostrarListaAbs;
                break;
        }
    }





}



