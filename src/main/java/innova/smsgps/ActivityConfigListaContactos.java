package innova.smsgps;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import innova.smsgps.beans.Contactos;
import innova.smsgps.infomovil.ManagerInfoMovil;

/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityConfigListaContactos extends BaseActivity {

    // Info
    ManagerInfoMovil managerInfoMovil;
    RelativeLayout contenedorSinContactos;
    ListView listView;
    ArrayList<Contactos> listContactos = new ArrayList<Contactos>();
    String numeroContacto ;
    String nombreContacto ;
    String tipoContacto ;
    int TIPO_ACCION = 0;
    int CodContacto = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.lista_contactos);
        managerInfoMovil = new ManagerInfoMovil(getApplicationContext());
        listView                = (ListView)findViewById(R.id.list);
        contenedorSinContactos  = (RelativeLayout)findViewById(R.id.contenedorSinContactos);
        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            TIPO_ACCION = extras.getInt(FragmentContactos.ID_PETICION);
//            CodContacto = extras.getInt(FragmentContactos.COD_PETICION);
//        }

        fetchContacts();
    }

    @Override
    public void listenerTimer() {

    }

    public void fetchContacts() {

        String phoneNumber = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        ContentResolver contentResolver = getContentResolver();
        // limpiamos collecion
        listContactos.clear();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);
        if (cursor.getCount() > 0)
        {

            while (cursor.moveToNext())
            {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                String tipoContacto = "MOVIL";
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                if (hasPhoneNumber > 0)
                {
                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

                    while (phoneCursor.moveToNext())
                    {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        phoneNumber = phoneNumber.replace("-", "").replace(" ", "");
                    }
                    if (phoneNumber.length() < 9)
                        break;

                    Contactos contactos = new Contactos();
                    contactos.setNombreContacto(name);
                    contactos.setTipoContacto(tipoContacto);
                    contactos.setNumeroContacto(phoneNumber);
                    listContactos.add(contactos);
                    phoneCursor.close();
                }
            }
            //imprimirToast(output.toString());
            listView.setAdapter(new ContactosAdapter(this, listContactos));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    TextView numero = (TextView) view.findViewById(R.id.txtNumeroContacto);
                    TextView nombre = (TextView) view.findViewById(R.id.txtNombreContacto);
                    TextView tipo   = (TextView) view.findViewById(R.id.txtTipoContacto);
                    // INSERTAMOS
                    nombreContacto  = nombre.getText().toString();
                    numeroContacto  = numero.getText().toString();
                    tipoContacto    = tipo.getText().toString();
                    Contactos contactos = new Contactos();
                    contactos.setNombreContacto(nombreContacto);
                    contactos.setTipoContacto(tipoContacto);
                    contactos.setNumeroContacto(numeroContacto);

                    setContacto(numeroContacto);
                    /*
                    if (TIPO_ACCION == FragmentContactos.ID_INSERTAR_CONTACTO)
                    {
                        // INDICE 3 .. INSERTAR REGISTRO EN CONTACTOS
                        if (managerSqlite.ejecutarConsulta(3, null, null, contactos) == 1)
                        {
                            setContacto(numeroContacto);
                        }
                    }else if (TIPO_ACCION == FragmentContactos.ID_EDITAR_CONTACTO)
                    {
                        contactos.setCodContacto(CodContacto);
                        if (managerSqlite.ejecutarConsulta(22, null, null, contactos) == 1)
                        {
                            setContacto(numeroContacto);
                        }
                    }
                    */
                    //imprimitToast(nombre.getText().toString());
                }
            });
        }
        // se puede dar si no tiene, ningún número movil, talvez solo tuviese
        // teléfonos de casa o esten su google drive.
        if (listContactos.size() == 0)
        {
            managerUtils.imprimirToast(this, "No tiene ningún contacto en su SIM.");
            contenedorSinContactos.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else
        {
            contenedorSinContactos.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
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




    public void setContacto(String data)
    {
        Intent intent=new Intent();
        intent.putExtra("MESSAGE", data);
        setResult(Activity.RESULT_OK, intent);
        finish();//finishing activity
    }
    /**
     * Agrega cadena dependiendo de cual spf
     * se encuentra vacIo , haciendo un simple
     * orden de decendencia MENOR - MAYOR...
     **/
//    private void agregarContacto(String datoContacto)
//    {
//        String c1 = managerInfoMovil.getSPF2(IDSP2.NUMERO01);
//        String c2 = managerInfoMovil.getSPF2(IDSP2.NUMERO02);
//        String c3 = managerInfoMovil.getSPF2(IDSP2.NUMERO03);
//        String c4 = managerInfoMovil.getSPF2(IDSP2.NUMERO04);
//
//        if (c1.length() < 1)
//        {
//            // NOMBRE - TIPO - NUMERO
//            managerInfoMovil.setSpf2(IDSP2.NUMERO01, datoContacto);
//        }else if (c2.length() < 1)
//        {
//            // NOMBRE - TIPO - NUMERO
//            managerInfoMovil.setSpf2(IDSP2.NUMERO02, datoContacto);
//        }else if (c3.length() < 1)
//        {
//            // NOMBRE - TIPO - NUMERO
//            managerInfoMovil.setSpf2(IDSP2.NUMERO03, datoContacto);
//        }else if (c4.length() < 1)
//        {
//            // NOMBRE - TIPO - NUMERO
//            managerInfoMovil.setSpf2(IDSP2.NUMERO04, datoContacto);
//        }
//
//    }

}
