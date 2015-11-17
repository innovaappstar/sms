package innova.smsgps;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import innova.smsgps.beans.ListRegistrosDenuncias;
import innova.smsgps.beans.RegistroDenuncias;
import innova.smsgps.sqlite.ManagerSqlite;

/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityListaRegistroDenuncias extends Activity {

    // Info
    ManagerSqlite managerSqlite;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.lista_registro_denuncias);

        managerSqlite = new ManagerSqlite(this);
        listView = (ListView)findViewById(R.id.listRegistroDenuncias);
        listarRegistros();

    }

    public void listarRegistros()
    {

        //imprimirToast(output.toString());
        listView.setAdapter(new MyBaseAdapter(this, ListRegistrosDenuncias.getListRegistrosDenuncias()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                TextView txtFechaHora = (TextView) view.findViewById(R.id.txtFechaHora);
                imprimitToast(txtFechaHora.getText().toString());
            }
        });

    }


    public class MyBaseAdapter extends BaseAdapter {

        ArrayList<RegistroDenuncias> myList = new ArrayList<RegistroDenuncias>();
        LayoutInflater inflater;
        Context context;


        public MyBaseAdapter(Context context, ArrayList<RegistroDenuncias> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public RegistroDenuncias getItem(int position) {
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
                convertView = inflater.inflate(R.layout.items_registros_denuncias, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }

            RegistroDenuncias registroDenuncias = getItem(position);

            mViewHolder.txtTipoDenuncia.setText(registroDenuncias.getIdTipoDenuncia());
            mViewHolder.txtFechaHora.setText(registroDenuncias.getFechaHoraSqlite());

            BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
            options.inPurgeable = true; // inPurgeable is used to free up memory while required
            byte[] imgByte = registroDenuncias.getImgDenuncia();
            Bitmap songImage1 = BitmapFactory.decodeByteArray(imgByte,0, imgByte.length,options);//Decode image, "thumbnail" is the object of image file
            Bitmap songImage = Bitmap.createScaledBitmap(songImage1, 50 , 50 , true);// convert decoded bitmap into well scalled Bitmap format.

            //imageview.SetImageDrawable(songImage);
            if (registroDenuncias.getFlagServidorSqlite().equals("1"))
                mViewHolder.txtEntregado.setText("Entregado");
            else
                mViewHolder.txtEntregado.setText("No Entregado");


            mViewHolder.iconDenuncia.setImageBitmap(songImage);



            //mViewHolder.ivIcon.setImageResource(currentListData.getImgResId());

            return convertView;
        }

        private class MyViewHolder {
            TextView txtTipoDenuncia, txtFechaHora, txtEntregado;
            ImageView iconDenuncia;

            public MyViewHolder(View item) {
                txtTipoDenuncia      = (TextView) item.findViewById(R.id.txtTipoDenuncia);
                txtFechaHora       = (TextView) item.findViewById(R.id.txtFechaHora);
                txtEntregado       = (TextView) item.findViewById(R.id.txtEntregado);
                iconDenuncia    = (ImageView) item.findViewById(R.id.iconDenuncia);
            }
        }
    }


    private void imprimitToast(String data)
    {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
}
