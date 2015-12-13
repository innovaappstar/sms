package innova.smsgps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import innova.smsgps.beans.Contactos;

/**
 * Created by innovaapps on 30/10/2015.
 */
public class FragmentContactos extends BaseFragment
{
    ViewGroup rootView;
    RelativeLayout contenedorContactosVacios;
    RelativeLayout contenedorContactosAgregados;
    ListView list;
    ArrayList<Contactos> listContactos = new ArrayList<Contactos>();
    public static final int REQUEST_CONTACTOS = 1;
    public static final String ID_PETICION = "PETICION";
    public static final String COD_PETICION = "COD_PETICION";
    public static final int ID_EDITAR_CONTACTO= 2;
    public static final int ID_INSERTAR_CONTACTO = 3;
    int CodContacto = 0;
    Dialog dialog;

    //region Iniciando Fragments
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    // SETEANDO ELEMENTOS
    private void IniciandoVistas()
    {
        ((Button)rootView.findViewById(R.id.btnAceptar)).setOnClickListener(this);
        contenedorContactosVacios       = (RelativeLayout)rootView.findViewById(R.id.contenedor_contactos_vacios);
        contenedorContactosAgregados    = (RelativeLayout)rootView.findViewById(R.id.contenedor_contactos_agregados);
        list                            = (ListView)rootView.findViewById(R.id.list);
        mostrarListaContactos();
    }
    //endregion Fragments Fin
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = (ViewGroup)inflater.inflate(R.layout.activity_contactos, container, false);
        super.IniciarInstancias();
        IniciandoVistas();
        return rootView;
    }

    /**
    * Simple m3todo para mostrar listado de contactos
    **/
    //region ListaContactos
    private void mostrarListaContactos()
    {
        if (managerSqlite.ejecutarConsulta(64 , null, null, null) == 1)
        {
            if (Contactos.getListaNumeros().length() > 0)
            {

                contenedorContactosVacios.setVisibility(View.GONE);
                contenedorContactosAgregados.setVisibility(View.VISIBLE);
                //        String contactosArray = c1 + "~" + c2 + "~" + c3 + "~" + c4;
                String[] fila = Contactos.getListaNumeros().split("~");
                listContactos.clear();
                for (int i = 0; i < fila.length; i++)
                {
                    if (fila[i].length() < 1)
                    {
                        continue;
                    }else
                    {
                        contenedorContactosVacios.setVisibility(View.GONE);
                        contenedorContactosAgregados.setVisibility(View.VISIBLE);
                    }
                    String[] Columna = fila[i].split("\\|");
                    //columna1 = ""
                    if (Columna.length > 0)
                    {
                        Contactos contactos = new Contactos();
                        contactos.setCodContacto(Integer.valueOf(Columna[0]));
                        contactos.setNombreContacto(Columna[1]);
                        contactos.setTipoContacto(Columna[2]);
                        contactos.setNumeroContacto(Columna[3]);
                        listContactos.add(contactos);
                    }
                }
                list.setAdapter(new ContactosAdapter(getActivity(), listContactos));
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
//                        TextView numeroContacto = (TextView) view.findViewById(R.id.txtNumeroContacto);
                        TextView nombreContacto = (TextView) view.findViewById(R.id.txtNombreContacto);
                        TextView tipoContacto = (TextView) view.findViewById(R.id.txtTipoContacto);
                        CodContacto = Integer.valueOf(tipoContacto.getTag().toString());
                        mostrarDialogo(nombreContacto.getText().toString());
                        //managerUtils.imprimirToast(getActivity(), position + " position");
                    }
                });

            }else
            {
                contenedorContactosVacios.setVisibility(View.VISIBLE);
                contenedorContactosAgregados.setVisibility(View.GONE);
                return;
            }
        }


    }
    //endregion

    private void mostrarDialogo(String NombreContacto)
    {
        //Create custom dialog object
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_pop_up_contacts_gestion);
        String datos = "Ha seleccionado al contacto \n" + NombreContacto + "\nQue acción desea realizar?.";
        ((TextView)dialog.findViewById(R.id.txtDetalleAplicativo)).setText(datos);
        ((Button)dialog.findViewById(R.id.btnSustituirContacto)).setOnClickListener(this);
        ((Button)dialog.findViewById(R.id.btnEliminarContacto)).setOnClickListener(this);

        dialog.show();
    }


    /**
     * Clase Adaptador para inflar los items
     * del listado de contactos..
     **/
    //region ADAPTADOR
    public class ContactosAdapter extends BaseAdapter {

        ArrayList<Contactos> myList = new ArrayList<Contactos>();
        LayoutInflater inflater;
        Context context;


        public ContactosAdapter(Context context, ArrayList<Contactos> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public Contactos getItem(int position) {
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
                convertView = inflater.inflate(R.layout.items_list_contacts, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }

            Contactos contactos = getItem(position);

            mViewHolder.txtNombreContacto.setText(contactos.getNombreContacto());
            mViewHolder.txtTipoContacto.setText(contactos.getTipoContacto());
            mViewHolder.txtTipoContacto.setTag(contactos.getCodContacto() + "");
            mViewHolder.txtNumeroContacto.setText(contactos.getNumeroContacto());


            //mViewHolder.ivIcon.setImageResource(currentListData.getImgResId());

            return convertView;
        }

        private class MyViewHolder
        {
            TextView txtNombreContacto;
            TextView txtTipoContacto;
            TextView txtNumeroContacto ;
            //ImageView ivIcon;

            public MyViewHolder(View item)
            {
                txtNombreContacto   = (TextView) item.findViewById(R.id.txtNombreContacto);
                txtTipoContacto     = (TextView) item.findViewById(R.id.txtTipoContacto);
                txtNumeroContacto   = (TextView) item.findViewById(R.id.txtNumeroContacto);
                //iconContacto    = (ImageView) item.findViewById(R.id.ivIcon);
            }
        }
    }
    //endregion

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnAceptar:
//                managerUtils.imprimirToast(getActivity(), "HOLA FRAGMENT HERENCIA");
                //mostrarListaContactos();
                if (listContactos.size() >= 4)
                {
                    managerUtils.imprimirToast(getActivity(), "S�lo se permite 4 contactos como m�ximo.");
                    return;
                }
                break;
            case R.id.btnSustituirContacto:
                if (dialog.isShowing())
                    dialog.cancel();
                Intent intent=new Intent(getActivity(), ActivityConfigListaContactos.class);
                intent.putExtra(ID_PETICION, ID_EDITAR_CONTACTO);
                intent.putExtra(COD_PETICION, CodContacto);
                startActivityForResult(intent, REQUEST_CONTACTOS);
                return;
            case R.id.btnEliminarContacto:
//                managerInfoMovil.setSpf2(idsp2, "");
                if (dialog.isShowing())
                    dialog.cancel();

                Contactos contactos = new Contactos();
                contactos.setCodContacto(CodContacto);
                if (managerSqlite.ejecutarConsulta(41, null ,null, contactos) == 1)
                {
                    Contactos.setListaNumeros("");
                    mostrarListaContactos();
                }

                return;
        }
        Intent intent=new Intent(getActivity(), ActivityConfigListaContactos.class);
        intent.putExtra(ID_PETICION, ID_INSERTAR_CONTACTO);
        intent.putExtra(COD_PETICION, 0);
        startActivityForResult(intent, REQUEST_CONTACTOS);
    }

    // Call Back method  to get the Message form other Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case REQUEST_CONTACTOS:
                if (resultCode != Activity.RESULT_OK)
                {
                    return;
                } else
                {
                    mostrarListaContactos();
                }
                break;
        }
    }



    @Override
    public void listenerTimer()
    {

    }

}
