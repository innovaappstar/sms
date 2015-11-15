package innova.smsgps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import innova.smsgps.beans.ListRegistrosAlertas;
import innova.smsgps.beans.RegistroAlerta;
import innova.smsgps.sqlite.ManagerSqlite;

/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityListaRegistroAlertas extends Activity {

    // Info
    ManagerSqlite managerSqlite;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.lista_registro_alertas);

        managerSqlite = new ManagerSqlite(this);
        listView = (ListView)findViewById(R.id.listRegistroAlertas);
        listarRegistros();

    }

    public void listarRegistros()
    {
            if (managerSqlite.ejecutarConsulta(61, new RegistroAlerta()) == 1)
            {
                //imprimirToast(output.toString());
                listView.setAdapter(new MyBaseAdapter(this, ListRegistrosAlertas.getListRegistrosAlertas()));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
                    {
                        TextView txtFechaHora = (TextView) view.findViewById(R.id.txtFechaHora);
                        imprimitToast(txtFechaHora.getText().toString());
                    }
                });
            }else
            {
                imprimitToast("<<Excepción Code 01>>");
            }

    }


    public class MyBaseAdapter extends BaseAdapter {

        ArrayList<RegistroAlerta> myList = new ArrayList<RegistroAlerta>();
        LayoutInflater inflater;
        Context context;


        public MyBaseAdapter(Context context, ArrayList<RegistroAlerta> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public RegistroAlerta getItem(int position) {
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
                convertView = inflater.inflate(R.layout.items_registros_alertas, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }

            RegistroAlerta registroAlerta = getItem(position);

            mViewHolder.txtTipoAlerta.setText(registroAlerta._getIdTipoAlerta());
            mViewHolder.txtFechaHora.setText(registroAlerta.getFechaHoraSqlite());
            if (registroAlerta.getFlagServidorSqlite().equals("1"))
                mViewHolder.txtEntregado.setText("Entregado");
            else
                mViewHolder.txtEntregado.setText("No Entregado");


            //mViewHolder.ivIcon.setImageResource(currentListData.getImgResId());

            return convertView;
        }

        private class MyViewHolder {
            TextView txtTipoAlerta, txtFechaHora, txtEntregado;
            ImageView ivIcon;

            public MyViewHolder(View item) {
                txtTipoAlerta      = (TextView) item.findViewById(R.id.txtTipoAlerta);
                txtFechaHora       = (TextView) item.findViewById(R.id.txtFechaHora);
                txtEntregado       = (TextView) item.findViewById(R.id.txtEntregado);
                //iconContacto    = (ImageView) item.findViewById(R.id.ivIcon);
            }
        }
    }

    public void setContacto(String data)
    {
        Intent intent=new Intent();
        intent.putExtra("MESSAGE", data);
        setResult(Activity.RESULT_OK, intent);
        finish();//finishing activity
    }

    private void imprimitToast(String data)
    {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
}
