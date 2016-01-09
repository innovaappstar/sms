package innova.smsgps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityListaDirectorios extends BaseActivity{

    Intent intent ;
    ListView mListView;

    private File file;
    private List<String> myList = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.lista_directorios);


        mListView = (ListView) findViewById(R.id.list);

        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ReadSDCard("")));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long id) {
                String item = ((TextView) view).getText().toString();
                //mListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, ReadSDCard(item)));

            }
        });


    }

    private List<String> ReadSDCard(String directory)
    {
        String path = "/sdcard/" + directory;
        //It have to be matched with the directory in SDCard
        File f = new File(path);

        File[] files=f.listFiles();
        for(int i=0; i<files.length; i++)
        {
            File file = files[i];
            /*It's assumed that all file in the path are in supported type*/
            String filePath = file.getPath();
            String[] vector = filePath.split("\\/");
            if (vector.length > 2)
            {
                myList.add(vector[2]);
            }

//            if(filePath.endsWith(".mp3")) // Condition to check .txt file extension
        }
        return myList;
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
            case R.id.contenedorCamara:
//                enviarMensajeIPC(BridgeIPC.INDICE_SELFIE_ANDROID, new String[]{"3|1", "Camera"});

                intent = new Intent(this, ActivityCameraPhoto.class);
                break;
        }
        if (intent != null)
            startActivity(intent);
    }


}



