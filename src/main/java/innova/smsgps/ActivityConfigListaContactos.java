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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import innova.smsgps.beans.Contactos;
import innova.smsgps.infomovil.ManagerInfoMovil;

/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityConfigListaContactos extends Activity {

    // Info
    ManagerInfoMovil managerInfoMovil;

    ListView listView;
    ArrayList<Contactos> listContactos = new ArrayList<Contactos>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.lista_contactos);
        managerInfoMovil = new ManagerInfoMovil(getApplicationContext());
        listView = (ListView)findViewById(R.id.listContacts);

        fetchContacts();
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
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext())
            {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

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
                    contactos.setNumeroContacto(phoneNumber);
                    listContactos.add(contactos);
                    phoneCursor.close();
                }
            }
            //imprimirToast(output.toString());
            listView.setAdapter(new MyBaseAdapter(this, listContactos));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    TextView numero = (TextView) view.findViewById(R.id.txtNumeroContacto);
                    TextView nombre = (TextView) view.findViewById(R.id.txtNombreContacto);
                    setContacto(numero.getText().toString());
                    imprimitToast(nombre.getText().toString());
                }
            });
        }
    }




    public class MyBaseAdapter extends BaseAdapter {

        ArrayList<Contactos> myList = new ArrayList<Contactos>();
        LayoutInflater inflater;
        Context context;


        public MyBaseAdapter(Context context, ArrayList<Contactos> myList) {
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
                convertView = inflater.inflate(R.layout.items_config_contacts, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }

            Contactos currentListData = getItem(position);

            mViewHolder.txtNombre.setText(currentListData.getNombreContacto());
            mViewHolder.txtNumero.setText(currentListData.getNumeroContacto());


            //mViewHolder.ivIcon.setImageResource(currentListData.getImgResId());

            return convertView;
        }

        private class MyViewHolder {
            TextView txtNumero, txtNombre;
            ImageView ivIcon;

            public MyViewHolder(View item) {
                txtNumero       = (TextView) item.findViewById(R.id.txtNumeroContacto);
                txtNombre       = (TextView) item.findViewById(R.id.txtNombreContacto);
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
