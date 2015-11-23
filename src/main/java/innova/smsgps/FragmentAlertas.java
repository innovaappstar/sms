package innova.smsgps;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import innova.smsgps.beans.ListRegistrosAlertas;
import innova.smsgps.beans.RegistroAlerta;

/**
 * Created by innovaapps on 30/10/2015.
 */
public class FragmentAlertas extends BaseFragment
{
    ViewGroup rootView;
    ListView list;
    RelativeLayout contenedorAlertasVacias;
    RelativeLayout contenedorAlertasAgregadas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // SETEANDO ELEMENTOS
    private void IniciandoVistas()
    {
        ((TextView)rootView.findViewById(R.id.txtLeerMas)).setOnClickListener(this);
        list = (ListView)rootView.findViewById(R.id.list);
        contenedorAlertasVacias = (RelativeLayout)rootView.findViewById(R.id.contenedor_alertas_vacias);
        contenedorAlertasAgregadas = (RelativeLayout)rootView.findViewById(R.id.contenedor_alertas_agregadas);
        listarRegistros();

    }
    //endregion Fragments Fin

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = (ViewGroup)inflater.inflate(R.layout.activity_alertas, container, false);
        super.IniciarInstancias();
        IniciandoVistas();
        return rootView;
    }

    /**
     * Simple método para listar registros de alertas...
     **/
    public void listarRegistros()
    {
        if (managerSqlite.ejecutarConsulta(61, null, null, null) == 1)    // NULL = BAD PRACTICE
        {
            if (ListRegistrosAlertas.getListRegistrosAlertas().size() < 1)
            {
                contenedorAlertasAgregadas.setVisibility(View.GONE);
                contenedorAlertasVacias.setVisibility(View.VISIBLE);
                return;
            }
            //imprimirToast(output.toString());
            list.setAdapter(new MyBaseAdapter(getActivity(), ListRegistrosAlertas.getListRegistrosAlertas()));
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    TextView txtFechaHora = (TextView) view.findViewById(R.id.txtFechaHora);
                    managerUtils.imprimirToast(getActivity(), txtFechaHora.getText().toString());
                }
            });
            contenedorAlertasAgregadas.setVisibility(View.VISIBLE);
            contenedorAlertasVacias.setVisibility(View.GONE);
        }else
        {
            managerUtils.imprimirToast(getActivity(), "<<Excepcion Code 01>>");
        }

    }



    /**
     * ADAPTADOR DE ALERTAS
     **/
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
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.items_list_alertas, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }

            RegistroAlerta registroAlerta = getItem(position);
            String[] fecha = registroAlerta.getFechaHoraSqlite().split("\\/");



            mViewHolder.txtTipoAlerta.setText(getTipoAlerta(registroAlerta.getIdTipoAlerta()));
            mViewHolder.txtNombreMes.setText(getMes(Integer.valueOf(fecha[1])));
            mViewHolder.txtNumeroDia.setText(fecha[0]);
            mViewHolder.txtFechaHora.setText(registroAlerta.getFechaHoraSqlite());
            if (registroAlerta.getFlagServidorSqlite().equals("1"))
                mViewHolder.txtEnviado.setText("Entregado");
            else
                mViewHolder.txtEnviado.setText("No Entregado");


            //mViewHolder.ivIcon.setImageResource(currentListData.getImgResId());

            return convertView;
        }

        public String getMes(int month)
        {
            month = month - 1;  // -1 PORQUE INICIA DE 0
            String[] monthNames = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
            return monthNames[month];
        }

        public String getTipoAlerta(int idTipo)
        {
            return (idTipo == 1) ? "ASALTO" : (idTipo == 2) ? "EMERGENCIA" : (idTipo == 3) ? "ACCIDENTE" : "";
        }

        private class MyViewHolder {
            TextView txtNombreMes;
            TextView txtNumeroDia;
            TextView txtTipoAlerta;
            TextView txtFechaHora;
            TextView txtEnviado;


            public MyViewHolder(View item) {
                txtNombreMes        = (TextView) item.findViewById(R.id.txtNombremes);
                txtNumeroDia        = (TextView) item.findViewById(R.id.txtNumeroDia);
                txtTipoAlerta       = (TextView) item.findViewById(R.id.txtTipoAlerta);
                txtFechaHora        = (TextView) item.findViewById(R.id.txtFechaHora);
                txtEnviado          = (TextView) item.findViewById(R.id.txtEnviado);
            }
        }
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.txtLeerMas:
                managerUtils.imprimirToast(getActivity(), "Leer más...");
                break;
        }
    }

    @Override
    public void listenerTimer() {

    }

}
