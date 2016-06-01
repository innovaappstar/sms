package innova.smsgps.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import innova.smsgps.R;
import innova.smsgps.base.adapters.RegistroTrackAdapter;
import innova.smsgps.dao.RegistroTrackDAO;
import innova.smsgps.entities.RegistroTrack;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityListaTrack extends BaseActivity implements ListView.OnItemClickListener
{
    RegistroTrackDAO registroTrackDAO = new RegistroTrackDAO();
    ListView lvTracker ;
    //-------
    LinearLayout llFuncionesActionBar;
    boolean isMostrarListaAbs = false;
    //-------
    public static final int REQUESTMAPA         = 10;
    public static final String EXTRALAT         = "EXTRALAT";
    public static final String EXTRALNG         = "EXTRALNG";
    public static final String EXTRAFECHAHORA   = "EXTRAFECHAHORA";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(getLayoutInflater().inflate(R.layout.abs_title, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

        setContentView(R.layout.activity_lista_track);
        //region casting vistas
        llFuncionesActionBar = (LinearLayout)findViewById(R.id.llFuncionesActionBar);
        lvTracker                  = (ListView)findViewById(R.id.lvTrack);
        lvTracker.setOnItemClickListener(this);
        try
        {
            lvTracker.setAdapter(new RegistroTrackAdapter(registroTrackDAO.getalRegistroTrack(ActivityListaTrack.this, new String[]{String.valueOf(RegistroTrack.TIPOSEGUIMIENTO)}) , ActivityListaTrack.this));
        }catch (Exception e)
        {}
        //endregion
    }


    @Override
    public void listenerTimer()
    {
    }


    public void onClick(View view)
    {{
        switch (view.getId())
        {
            case R.id.ivActionAbs:
                managerUtils.deslizadoVertical(llFuncionesActionBar, this, isMostrarListaAbs);
                isMostrarListaAbs = !isMostrarListaAbs;
                break;
            case R.id.tvActionBorrar:
                try
                {
                    registroTrackDAO.deleteRegistroTrack(ActivityListaTrack.this, new String[]{String.valueOf(RegistroTrack.TIPOSEGUIMIENTO)});
                    lvTracker.setAdapter(new RegistroTrackAdapter(registroTrackDAO.getalRegistroTrack(ActivityListaTrack.this, new String[]{String.valueOf(RegistroTrack.TIPOSEGUIMIENTO)}), ActivityListaTrack.this));
                    managerUtils.imprimirToast(ActivityListaTrack.this, "registros elimiandos!");
                }catch (Exception e)
                {}
                break;
            default:
                break;
        }
    }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        RegistroTrack registroTrack = (RegistroTrack)parent.getItemAtPosition(position);
        String fechaHora = registroTrack.getFechaHora();
//        managerUtils.imprimirToast(ActivityListaTrack.this, fechaHora);
        Intent intent = new Intent(this, ActivityMapTrackHistorial.class);
        intent.putExtra(EXTRALAT, registroTrack.getLatitud());
        intent.putExtra(EXTRALNG, registroTrack.getLongitud());
        intent.putExtra(EXTRAFECHAHORA, registroTrack.getFechaHora());
        startActivityForResult(intent, REQUESTMAPA);
//        Dispositivo dispositivo = (Dispositivo)parent.getItemAtPosition(position);
    }




}



