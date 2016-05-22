package innova.smsgps.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import innova.smsgps.R;
import innova.smsgps.base.adapters.SpinnerAdapter;
import innova.smsgps.dao.UserDAO;
import innova.smsgps.entities.Idioma;
import innova.smsgps.entities.LoginUser;
import innova.smsgps.entities.User;
import innova.smsgps.enums.IDSP1;
import innova.smsgps.task.RegistroUserAsyncTask;
import innova.smsgps.views.EditTextListener;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityRegistro extends BaseActivity implements EditTextListener.EditTextListenerCallback, RegistroUserAsyncTask.RegistroUsuarioCallback
{
    boolean isPermitirSeleccionSpiner = false;
    EditTextListener etEmail, etPassword, etRepeatPassword, etFirstName, etLastName;
    Spinner spIdiomas;

    private boolean IS_RUNING_ASYNC_TASK = false;

    User user = new User();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_registro);

        //region casting vistas
        EditTextListener.setOnTextListener(this);
        etEmail             = (EditTextListener)findViewById(R.id.etEmail);
        etPassword          = (EditTextListener)findViewById(R.id.etPassword);
        etRepeatPassword    = (EditTextListener)findViewById(R.id.etRepeatPassword);
        etFirstName         = (EditTextListener)findViewById(R.id.etFirstName);
        etLastName          = (EditTextListener)findViewById(R.id.etLastName);
        spIdiomas           = (Spinner)findViewById(R.id.spIdiomas);
        //endregion
        //region cargar spinner
        ArrayList<Idioma> alIdiomas = new ArrayList<Idioma>();
        alIdiomas.add(new Idioma(Idioma.SPANISH, Idioma.ESPANIOL));
        alIdiomas.add(new Idioma(Idioma.ENGLISH, Idioma.INGLES));
        SpinnerAdapter spinnerAdapter=new SpinnerAdapter(ActivityRegistro.this,alIdiomas);
        spIdiomas.setAdapter(spinnerAdapter);
        spIdiomas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (isPermitirSeleccionSpiner)
                {
                    Configuration configuration = new Configuration();
                    String nmbre    = ((Idioma)parent.getAdapter().getItem(position)).getNombre();
                    int codigo      = ((Idioma)parent.getAdapter().getItem(position)).getCodigo();
                    if (codigo == Idioma.ESPANIOL)
                        configuration.locale = Locale.getDefault(); // new Locale("es") -- alternativo
                    else if (codigo == Idioma.INGLES)
                        configuration.locale = Locale.ENGLISH;

                    getResources().updateConfiguration(configuration, null);

//                    Toast.makeText(parent.getContext(), nmbre + "\n" + codigo, Toast.LENGTH_SHORT).show();
                } else
                {
                    isPermitirSeleccionSpiner = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //endregion


    }

    //region ciclos de vida (uiLifecycleHelper)
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }
    //endregion


    @Override
    public void listenerTimer()
    {
    }


    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tvRegistrarse:
                String email            = etEmail.getText().toString();
                String password         = etPassword.getText().toString();
                String repeatPassword   = etRepeatPassword.getText().toString();
                String firstName        = etFirstName.getText().toString();
                String lastname         = etLastName.getText().toString();

                if (IS_RUNING_ASYNC_TASK)
                {
                    managerUtils.imprimirToast(this, "registro en progreso aún.");
                    return;
                }
                if (!(email.length() > 0 && password.length() > 0 && repeatPassword.length() >0 && firstName.length() > 0 && lastname.length() > 0))
                {
                    managerUtils.imprimirToast(this, "casillas incompletas");
                    return;
                }
                if (!password.equals(repeatPassword))
                {
                    etRepeatPassword.setError("no coinciden");
                    return;
                }

                this.user = new User(firstName, lastname, email, password);
                new RegistroUserAsyncTask(this, this.user).execute();
                IS_RUNING_ASYNC_TASK = true;
                break;
        }
    }

    @Override
    public void onAfterTextChanged(EditText editText, String texto, int tamanio)
    {
        switch (editText.getId())
        {
            case R.id.etEmail:
                break;
            case R.id.etPassword:
                if (tamanio < 6)
                    etPassword.setError("mínimo 6 dígitos");
                else
                {
                    etPassword.setError(null);
                    if (etRepeatPassword.getText().toString().equals(etPassword.getText().toString()))
                        etRepeatPassword.setError(null);
                }
                break;
            case R.id.etRepeatPassword:
                // el password al ser modificado podra quitar la advertencia del repeatPassword... (ambos)
                if (etPassword.getText().toString().length() > 5 && etRepeatPassword.getText().toString().length() > 5)
                {
                    if (etRepeatPassword.getText().toString().equals(etPassword.getText().toString()))
                        etRepeatPassword.setError(null);
                    else
                        etRepeatPassword.setError("no coinciden las contraseñas.");
                }else
                    etRepeatPassword.setError(null);
                break;
            case R.id.etFirstName:
                break;
            case R.id.etLastName:
                break;
            default:
                break;
        }
    }


    @Override
    public void onLoginUser(LoginUser loginUser)
    {
        if (!loginUser.isCorrecto())
        {
            managerUtils.imprimirToast(this, loginUser.getDescription());
        }else
        {
            try
            {
                // registramos o actualizamos nuevo usuario...
                UserDAO userDAO = new UserDAO();
                userDAO.insertUser(this, this.user);
                // grabamos código idioma
                managerInfoMovil.setSpf1(IDSP1.IDIOMA, ((Idioma) spIdiomas.getAdapter().getItem(spIdiomas.getSelectedItemPosition())).getCodigo());
                managerUtils.imprimirToast(this, loginUser.getDescription());
                startActivity(new Intent(this, ActivityMenuPrincipal.class));
                finish();
            } catch (SQLException e)
            {
                managerUtils.imprimirToast(this, e.getMessage());
            }
        }
        IS_RUNING_ASYNC_TASK = false;
    }

}



