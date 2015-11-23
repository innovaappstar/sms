package innova.smsgps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import innova.smsgps.beans.ListRegistrosDenuncias;
import innova.smsgps.beans.RegistroDenuncias;

/**
 * Created by innovaapps on 30/10/2015.
 */
public class FragmentDenuncias extends BaseFragment
{
    ViewGroup rootView;
    ListView list;
    RelativeLayout contenedorDenunciasVacias;
    RelativeLayout contenedorDenunciasAgregadas;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void IniciandoVistas()
    {
        list = (ListView)rootView.findViewById(R.id.list);
        contenedorDenunciasVacias       = (RelativeLayout)rootView.findViewById(R.id.contenedor_denuncias_vacias);
        contenedorDenunciasAgregadas    = (RelativeLayout)rootView.findViewById(R.id.contenedor_denuncias_agregadas);
        listarRegistros();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = (ViewGroup)inflater.inflate(R.layout.activity_denuncias, container, false);
        super.IniciarInstancias();
        IniciandoVistas();
        return rootView;
    }

    public void listarRegistros()
    {

        if (managerSqlite.ejecutarConsulta(63, null, null, null) == 1)    // NULL = BAD PRACTICE
        {
            if (ListRegistrosDenuncias.getListRegistrosDenuncias().size() < 1)
            {
                contenedorDenunciasAgregadas.setVisibility(View.GONE);
                contenedorDenunciasVacias.setVisibility(View.VISIBLE);
                return;
            }
            //imprimirToast(output.toString());
            list.setAdapter(new DenunciasAdapter(getActivity(), ListRegistrosDenuncias.getListRegistrosDenuncias()));
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    TextView txtFechaHora = (TextView) view.findViewById(R.id.txtFechaHora);
                    managerUtils.imprimirToast(getActivity(), txtFechaHora.getText().toString());
                }
            });
            contenedorDenunciasAgregadas.setVisibility(View.VISIBLE);
            contenedorDenunciasVacias.setVisibility(View.GONE);
        }else
        {
            managerUtils.imprimirToast(getActivity(), "<<Excepcion Code 01>>");
        }


    }



    public class DenunciasAdapter extends BaseAdapter {

        ArrayList<RegistroDenuncias> myList = new ArrayList<RegistroDenuncias>();
        LayoutInflater inflater;
        Context context;


        public DenunciasAdapter(Context context, ArrayList<RegistroDenuncias> myList) {
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
                convertView = inflater.inflate(R.layout.items_list_denuncias, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }

            RegistroDenuncias registroDenuncias = getItem(position);
            String[] fecha = registroDenuncias.getFechaHoraSqlite().split("\\/");
            String descripcion = registroDenuncias.getDescripcion();
            if (descripcion.length() > 80)
            {
                descripcion = descripcion.substring(0 , 80) + "...";
            }


            mViewHolder.txtTipoDenuncia.setText(getTipoDenuncia(Integer.parseInt(registroDenuncias.getIdTipoDenuncia())));
            mViewHolder.txtNombreMes.setText(getMes(Integer.valueOf(fecha[1])));
            mViewHolder.txtNumeroDia.setText(fecha[0]);
            mViewHolder.txtFechaHora.setText(registroDenuncias.getFechaHoraSqlite());
            mViewHolder.txtDescripcionDenuncia.setText(descripcion);
            if (registroDenuncias.getFlagServidorSqlite().equals("1"))
                mViewHolder.txtEnviado.setText("Entregado");
            else
                mViewHolder.txtEnviado.setText("No Entregado");

            BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
            options.inPurgeable = true; // inPurgeable is used to free up memory while required
            byte[] imgByte = registroDenuncias.getImgDenuncia();
            Bitmap songImage1 = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length, options);//Decode image, "thumbnail" is the object of image file
            Bitmap songImage = Bitmap.createScaledBitmap(songImage1, 50, 50, true);// convert decoded bitmap into well scalled Bitmap format.
            mViewHolder.imgDenuncia.setImageBitmap(songImage);

            //mViewHolder.ivIcon.setImageResource(currentListData.getImgResId());

            return convertView;
        }

        public String getMes(int month)
        {
            month = month - 1;  // -1 PORQUE INICIA DE 0
            String[] monthNames = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
            return monthNames[month];
        }

        public String getTipoDenuncia(int idTipo)
        {
            return (idTipo == 1) ? "ASALTO" : (idTipo == 2) ? "EMERGENCIA" : (idTipo == 3) ? "ACCIDENTE" : "";
        }
        private class MyViewHolder {
            TextView txtNombreMes;
            TextView txtNumeroDia;
            TextView txtTipoDenuncia;
            TextView txtDescripcionDenuncia;
            TextView txtFechaHora;
            TextView txtEnviado;
            ImageView imgDenuncia;

            public MyViewHolder(View item) {
                txtNombreMes        = (TextView) item.findViewById(R.id.txtNombremes);
                txtNumeroDia        = (TextView) item.findViewById(R.id.txtNumeroDia);
                txtTipoDenuncia     = (TextView) item.findViewById(R.id.txtTipoDenuncia);
                txtDescripcionDenuncia     = (TextView) item.findViewById(R.id.txtDescripcionDenuncia);
                txtFechaHora        = (TextView) item.findViewById(R.id.txtFechaHora);
                txtEnviado          = (TextView) item.findViewById(R.id.txtEnviado);
                imgDenuncia         = (ImageView) item.findViewById(R.id.imgDenuncia);
            }
        }
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnAceptar:
                managerUtils.imprimirToast(getActivity(), "registrando ..");
                break;
        }
    }

    @Override
    public void listenerTimer() {

    }


}
