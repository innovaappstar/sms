package innova.smsgps;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
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
    public static final int REQUEST_MEDIA = 1;
    MediaPlayer mediaPlayer;
    boolean isMedia     = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_sound_apolo);
        txtAvisoDirectorio  = (TextView)findViewById(R.id.lblAvisoDirectorio);
        txtNombreDirectorio = (TextView)findViewById(R.id.lblNombreDirectorio);
        imgBackground       = (ImageView)findViewById(R.id.imgBackgroundMusic);
        refrescarVistas();
        instanciarMedia();
    }
    private void instanciarMedia()
    {
        mediaPlayer = new  MediaPlayer();
        isMedia = true;
    }

    public void refrescarVistas()
    {
        if (isDirectorioSeleccionado())
        {
            txtAvisoDirectorio.setText("Ya has seleccionado el siguiente directorio :");
            txtNombreDirectorio.setText(getNombreDirectorio());
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
//                if (intent != null ) //&& !isDirectorioSeleccionado() )
                startActivityForResult(intent, REQUEST_MEDIA);

                break;
            case R.id.contenedorMedia:
                if (isDirectorioSeleccionado())
                {
//                    mediaPlayer = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/" + getNombreDirectorio()
//                            + "/See You Again (spanish version) - Kevin Karla & La Banda (Lyric Video).mp3"));
                    String filePath = Environment.getExternalStorageDirectory()+"/" + getNombreDirectorio() + "/DePlasticoVerde  Adivinanzas  [ VideoClip Oficial ].mp3";


                    System.gc();
                    try
                    {
                        if (mediaPlayer != null && !isMedia)
                            instanciarMedia();

                        if (mediaPlayer.isPlaying())
                        {
                            mediaPlayer.reset();
                        }
                        mediaPlayer.setDataSource(filePath);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                                mp = null;
                                isMedia = false;
                            }
                        });
                    }
                    catch (Exception e) {}

                }

                break;
        }

//        startActivity(intent);
    }

    public boolean isDirectorioSeleccionado()
    {
        return managerInfoMovil.getSPF2(IDSP2.DIRECTORIOMUSIC).length() > 0;
    }
    public String getNombreDirectorio()
    {
        return managerInfoMovil.getSPF2(IDSP2.DIRECTORIOMUSIC);
    }

    // Call Back method  to get the Message form other Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case REQUEST_MEDIA:
                if (resultCode != Activity.RESULT_OK)
                {

                    return;
                } else
                {
                    refrescarVistas();
                }
                break;
        }
    }


}



