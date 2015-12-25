package innova.smsgps;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import innova.smsgps.enums.IDSP2;
import innova.smsgps.task.UpRegistroUsuario;
import innova.smsgps.task.UpRegistroUsuario.RegistroUsuarioCallback;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityRegistroUsuario extends BaseActivity implements RegistroUsuarioCallback
{

    /**
     * Vistas editables
     **/
    EditText editNick;
    EditText editPassword;
    EditText editRepeatPassword;
    EditText editFirstName;
    EditText editLastName;


    /**
     * Instancias HTTP
     **/
    UpRegistroUsuario objUpRegistroUsuario = null;
    /**
     * Matriz de valores...
     **/
    String[] params = new String[4];


    Intent intent ;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_registro_apolo);

        // casteando las vistas
        editNick                = (EditText)findViewById(R.id.editNick);
        editPassword            = (EditText)findViewById(R.id.editPassword);
        editRepeatPassword      = (EditText)findViewById(R.id.editRepeatPassword);
        editFirstName           = (EditText)findViewById(R.id.editFirstName);
        editLastName            = (EditText)findViewById(R.id.editLastName);
        mostrarAdvertencia();
    }



    @Override
    public void onResume()
    {
        super.onResume();
        objUpRegistroUsuario   = null;
        objUpRegistroUsuario   = new UpRegistroUsuario(this);
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            case R.id.imgSplashFacebook:
                imprimitToast("Autenticando...");
                break;
            case R.id.btnAceptar:
                String nick         = editNick.getText().toString();
                String password     = editPassword.getText().toString();
                String firstName    = editFirstName.getText().toString();
                String lastName     = editLastName.getText().toString();

                if (coincidenPassword() && nick.length() > 5 && password.length() > 5 && firstName.length() > 5 && lastName.length() > 5)
                {
                    params[0] = nick;
                    params[1] = password;
                    params[2] = firstName;
                    params[3] = lastName;
                    runAsyntask(params);
                }else
                {
                    managerUtils.imprimirToast(getApplicationContext(), "Revise si todos los datos estan ingresados correctamente.");
                }
//                imprimitToast("Aceptar..");
                break;
        }

//        startActivity(intent);
    }

    /**
     * Simple método que iniciara el registro del usuario...
     **/
    private void runAsyntask(String[] params)
    {
        objUpRegistroUsuario.cancel(true);
        objUpRegistroUsuario = null;
        objUpRegistroUsuario = new UpRegistroUsuario(this);
        objUpRegistroUsuario.execute(params);
    }

    /**
     * Simple void para recorrer matriz y asignarle su listener..
     **/
    private void mostrarAdvertencia()
    {
        EditText[] editsRegistro = new EditText[]{editNick, editPassword, editRepeatPassword, editFirstName, editLastName};

        for (int i = 0; i < editsRegistro.length; i++)
        {
            String messageAdvertencia   = "";
            int isRepeatPassword        = 0;
            if (editsRegistro[i] == editRepeatPassword)
            {
                messageAdvertencia  = "No coinciden las contraseñas..";
                isRepeatPassword    = 1;
            }

            asignarListener(editsRegistro[i], messageAdvertencia, isRepeatPassword);
        }

    }
    /**
     * Void para asignar listener de TextWatcher...
     * a la matriz de edittext...
     **/
    private void asignarListener(final EditText inputsEdittext,final String messageAdvertencia, final int isRepeatPassword)
    {
        inputsEdittext.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                int textoCaracteres = inputsEdittext.getText().toString().length();
                if (textoCaracteres > 0 && textoCaracteres < 6) {
                    inputsEdittext.setError("Porfavor ingrese más de 5 carácteres");
                } else {
                    if (isRepeatPassword == 0) {
                        inputsEdittext.setError(null);
                    }
                }
                // caso repeat password..
                if (isRepeatPassword == 1) {
                    if (textoCaracteres > 5) {
                        if (!inputsEdittext.getText().toString().equals(editPassword.getText().toString())) {
                            inputsEdittext.setError(messageAdvertencia);
                        } else {
                            inputsEdittext.setError(null);
                        }
                    }
                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }


    private void imprimitToast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

    /**
     * Simple función que consultara si coinciden las contraseñas para poder registrar
     * el nuevo usuario...
     **/
    private boolean coincidenPassword()
    {
        String p    = editPassword.getText().toString();
        String rp   = editRepeatPassword.getText().toString();
        return (p.equals(rp));
    }

    /**
     * Valores de retorno
     * <ul>
     *     <li>  1 : Se registro correctamente el nuevo usuario </li>
     *     <li>  0 : ocurrio un error al insertar registro </li>
     *     <li> -1 : El usuario ya existe </li>
     * </ul>
     **/
    @Override
    public void RegistroUsuarioExecute(int resultado)
    {
        if ( resultado == 1)    // REGISTRO CORRECTO
        {
            // guardamos el nickusuario para mas adelante enviarlo en sus alertas
            managerInfoMovil.setSpf2(IDSP2.NICKUSUARIO, params[0]);
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }else if (resultado == -1)
        {
            managerUtils.imprimirToast(getApplicationContext(), "El nick de usuario ya existe");
            editNick.setError("Este usuario ya existe.");
        }else if (resultado == 0)
        {
            managerUtils.imprimirToast(getApplicationContext(), "Ocurrio un error inesperado.");
        }
    }
}



