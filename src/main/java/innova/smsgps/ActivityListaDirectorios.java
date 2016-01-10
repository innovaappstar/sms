package innova.smsgps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import innova.smsgps.beans.Directorios;
import innova.smsgps.enums.IDSP2;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityListaDirectorios extends BaseActivity{

    Intent intent ;
    ListView mListView;

    private File file;
    ArrayList<Directorios> listDirectorios = new ArrayList<Directorios>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.lista_directorios);


        mListView = (ListView) findViewById(R.id.list);
        ReadSDCard();
        //mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ReadSDCard("")));




    }

    private void ReadSDCard()
    {
        String path     = "/sdcard/";
        //It have to be matched with the directory in SDCard
        File f          = new File(path);

        File[] files    =   f.listFiles();
        listDirectorios.clear();

        for(int i=0; i<files.length; i++)
        {
            File file = files[i];
            /*It's assumed that all file in the path are in supported type*/
            String filePath = file.getPath();
            String[] vector = filePath.split("\\/");
            if (vector.length > 2)
            {
                //myList.add(vector[2]);
                String nombreDirectorio = vector[2];

                int cantidadArchivosMp3 = 0;
                try
                {
                    if (!nombreDirectorio.contains("."))
                        cantidadArchivosMp3 = getCountArchivosMp3(nombreDirectorio);
                }catch (Exception e )
                {
                    e.printStackTrace();
                }
                if ( cantidadArchivosMp3 > 0)
                {
                    Directorios directorios = new Directorios();
                    directorios.setNombreDirectorio(nombreDirectorio);
                    directorios.setCantidadArchivos(cantidadArchivosMp3);
                    listDirectorios.add(directorios);
                }

            }
        }
        mListView.setAdapter(new DirectorioAdapter(this, listDirectorios));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                String NombreDirectorio = ((TextView) view.findViewById(R.id.txtNombreDirectorio)).getText().toString();
                managerUtils.imprimirToast(getApplicationContext(), NombreDirectorio);
                managerInfoMovil.setSpf2(IDSP2.DIRECTORIOMUSIC, NombreDirectorio);
                Intent intent=new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();//finishing activity
            }
        });
    }


    public int getCountArchivosMp3(String directorio)
    {
        int count = 0;
        String path = "/sdcard/" + directorio;
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
                String nombreArchivo = vector[vector.length -1];
                if (nombreArchivo.endsWith(".mp3"))
                {
                    count ++;
                }
            }
        }
        return count;
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


    /**
     * Clase Adaptador para inflar los items
     * del listado de contactos..
     **/
    //region ADAPTADOR
    public class DirectorioAdapter extends BaseAdapter {

        ArrayList<Directorios> myList = new ArrayList<Directorios>();
        LayoutInflater inflater;
        Context context;


        public DirectorioAdapter(Context context, ArrayList<Directorios> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public Directorios getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.items_list_directorios, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }

            Directorios directorios = getItem(position);

            mViewHolder.txtNombreDirectorio.setText(directorios.getNombreDirectorio());
            mViewHolder.txtNumeroArchivos.setText( "Cantidad de Archivos mp3 :" + directorios.getCantidadArchivos() + "");

            return convertView;
        }

        private class MyViewHolder
        {
            TextView txtNombreDirectorio;
            TextView txtNumeroArchivos;
            //ImageView ivIcon;

            public MyViewHolder(View item)
            {
                txtNombreDirectorio   = (TextView) item.findViewById(R.id.txtNombreDirectorio);
                txtNumeroArchivos     = (TextView) item.findViewById(R.id.txtNumeroArchivos);
            }
        }
    }
    //endregion


}



