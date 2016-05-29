package innova.smsgps.activities;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.SQLException;

import innova.smsgps.R;
import innova.smsgps.dao.ContactoDAO;
import innova.smsgps.entities.Contacto;
import innova.smsgps.entities.ContactoUser;
import innova.smsgps.task.UpdateContactoAsyncTask;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityContacto extends BaseActivity implements UpdateContactoAsyncTask.ContactoCallback
{
    ContactoDAO contactoDAO = new ContactoDAO();
    Contacto contacto = new Contacto();

    EditText etEmail, etTelefono;

    TextView tvNumeroContacto ;
    LinearLayout llFuncionesActionBar;
    boolean isMostrarListaAbs = false;
    private static int nC = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(getLayoutInflater().inflate(R.layout.abs_title, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        setContentView(R.layout.activity_contacto);
        tvNumeroContacto = (TextView)findViewById(R.id.tvNumeroContacto);
        etEmail         = (EditText)findViewById(R.id.etEmail);
        etTelefono      = (EditText)findViewById(R.id.etTelefono);
        nC = getIntent().getIntExtra(ActivityContactos.EXTRACONTACTO, 0);
        int idString = (nC == 1)? R.string.contacto01 : (nC == 2)? R.string.contacto02 : (nC == 3)? R.string.contacto03 :  R.string.contacto04;
        tvNumeroContacto.setText(idString);

        // recuperamos contacto
        contacto = contactoDAO.getContacto(this, new Contacto(String.valueOf(nC)));
        etEmail.setText(contacto.getEmail());
        etTelefono.setText(contacto.getTelefono());


        llFuncionesActionBar = (LinearLayout)findViewById(R.id.llFuncionesActionBar);

    }


    @Override
    public void listenerTimer()
    {
    }


    public void onClick(View view)
    {
        String email    = etEmail.getText().toString();
        String telefono = etTelefono.getText().toString();
        String nombre   = "";
        switch (view.getId())
        {
            case R.id.ivActionAbs:
                managerUtils.deslizadoVertical(llFuncionesActionBar, this, isMostrarListaAbs);
                isMostrarListaAbs = !isMostrarListaAbs;
                break;
            case R.id.btGuardarContacto:
                if (email.length() > 3 && telefono.length() > 5)
                {
                    contacto = new Contacto(Contacto.REGISTRARCONTACTO, telefono, nombre, email, String.valueOf(nC));
                    new UpdateContactoAsyncTask(this, contacto).execute();
                }else
                {
                    managerUtils.imprimirToast(ActivityContacto.this, "campos incompletos");
                }
//                Intent resultIntent = new Intent();
//                setResult(RESULT_OK, resultIntent);
//                finish();
                break;
            case R.id.tvComprobarEmail:
                if (email.length() > 0)
                {
                    contacto = new Contacto(Contacto.COMPROBARNICK, email);
                    new UpdateContactoAsyncTask(this, contacto).execute();
                }else
                {
                    managerUtils.imprimirToast(ActivityContacto.this, "Incompleto");
                }
                break;
            case R.id.tvDelete:
                contacto = new Contacto(Contacto.DELETE, email);
                contacto.setCodContacto(String.valueOf(nC));
                new UpdateContactoAsyncTask(this, contacto).execute();
                break;
        }
    }


    @Override
    public void onContacto(ContactoUser contactoUser, Contacto contacto)
    {
        if (!contactoUser.isCorrecto())
        {
            managerUtils.imprimirToast(this, contactoUser.getDescription());
        }else
        {
            if (contacto.getACTION() == Contacto.COMPROBARNICK)
            {
                managerUtils.imprimirToast(this, contactoUser.getDescription());
            }else if (contacto.getACTION() == Contacto.REGISTRARCONTACTO)
            {
                try
                {
                    contactoDAO.insertContacto(ActivityContacto.this, contacto);
                    managerUtils.imprimirToast(this, contactoUser.getDescription());
                } catch (SQLException e)
                {
                }
            }else if (contacto.getACTION() == Contacto.DELETE)
            {
                try
                {
                    if (contactoDAO.deleteContacto(ActivityContacto.this, contacto))
                    {
                        etEmail.setText("");
                        etTelefono.setText("");
                        managerUtils.imprimirToast(this, contactoUser.getDescription());
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }


            }
        }
    }
}



