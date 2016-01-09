package innova.smsgps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import innova.smsgps.enums.IDSP2;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityMediaOpciones extends BaseActivity{

    Intent intent ;
    TextView txtAvisoDirectorio, txtNombreDirectorio;
    ImageView imgBackground;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_sound_apolo);
        txtAvisoDirectorio  = (TextView)findViewById(R.id.lblAvisoDirectorio);
        txtNombreDirectorio = (TextView)findViewById(R.id.lblNombreDirectorio);
        imgBackground       = (ImageView)findViewById(R.id.imgBackgroundMusic);

        if (isDirectorioSeleccionado())
        {
            txtAvisoDirectorio.setText("Ya has seleccionado el siguiente directorio :");
            txtNombreDirectorio.setText(managerInfoMovil.getSPF2(IDSP2.DIRECTORIOMUSIC));
        }else
        {
            txtAvisoDirectorio.setText("Selecciona algún directorio de música.");
            txtNombreDirectorio.setText("");
        }
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
            case R.id.imgBackgroundMusic:
                intent = new Intent(this, ActivityListaDirectorios.class);
                break;
        }
        if (intent != null && !isDirectorioSeleccionado() )
            startActivity(intent);
    }

    public boolean isDirectorioSeleccionado()
    {
        return managerInfoMovil.getSPF2(IDSP2.DIRECTORIOMUSIC).length() > 0;
    }

}



