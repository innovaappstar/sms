package innova.smsgps.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import innova.smsgps.R;
import innova.smsgps.base.adapters.SpinnerAdapter;
import innova.smsgps.base.adapters.SpinnerGenderAdapter;
import innova.smsgps.dao.UserDAO;
import innova.smsgps.datacontractenum.UserDataContract;
import innova.smsgps.dialogs.BirthDayDialog;
import innova.smsgps.entities.Gender;
import innova.smsgps.entities.Idioma;
import innova.smsgps.entities.LoginUser;
import innova.smsgps.entities.User;
import innova.smsgps.task.UpdateProfileUserAsyncTask;
import innova.smsgps.utils.Utils;
import innova.smsgps.views.EditTextListener;

import static android.app.DatePickerDialog.OnDateSetListener;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityProfile extends BaseActivity implements  OnDateSetListener, EditTextListener.EditTextListenerCallback, View.OnTouchListener , UpdateProfileUserAsyncTask.UpdateProfileUsuarioCallback
{
    UserDAO userDAO = new UserDAO();
    User user       = new User();

    LinearLayout llFuncionesActionBar;
    boolean isMostrarListaAbs = false;

    boolean isPermitirSeleccionSpiner = false;
    Spinner spIdiomas;
    Spinner spGender;

    TextView tvProfileNombre, tvProfileDetalle, tvActionRetroceder;
    EditTextListener etEmail, etPassword, etRepeatPassword, etFirstName, etLastName, etBirthDay, etCountry;
    String idFacebook = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(getLayoutInflater().inflate(R.layout.abs_title, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

        setContentView(R.layout.activity_profile);
        llFuncionesActionBar = (LinearLayout)findViewById(R.id.llFuncionesActionBar);

        tvProfileNombre     = (TextView)findViewById(R.id.tvProfileNombre);
        tvProfileDetalle    = (TextView)findViewById(R.id.tvProfileDetalle);
        tvActionRetroceder  = (TextView)findViewById(R.id.tvActionRetroceder);

        EditTextListener.setOnTextListener(this);
        etEmail             = (EditTextListener)findViewById(R.id.etEmail);
        etPassword          = (EditTextListener)findViewById(R.id.etPassword);
        etRepeatPassword    = (EditTextListener)findViewById(R.id.etRepeatPassword);
        etFirstName         = (EditTextListener)findViewById(R.id.etFirstName);
        etLastName          = (EditTextListener)findViewById(R.id.etLastName);
        etBirthDay          = (EditTextListener)findViewById(R.id.etBirthDay);
        etCountry           = (EditTextListener)findViewById(R.id.etCountry);

        etBirthDay.setOnTouchListener(this);
        spIdiomas           = (Spinner)findViewById(R.id.spIdiomas);
        spGender            = (Spinner)findViewById(R.id.spGender);


        //region cargar spinner
        ArrayList<Idioma> alIdiomas = new ArrayList<Idioma>();
        alIdiomas.add(new Idioma(getResources().getString(R.string.item_spanish), Idioma.ESPANIOL_id));
        alIdiomas.add(new Idioma(getResources().getString(R.string.item_english), Idioma.INGLES_id));

        SpinnerAdapter spinnerAdapter=new SpinnerAdapter(ActivityProfile.this, alIdiomas);
        spIdiomas.setAdapter(spinnerAdapter);
        spIdiomas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isPermitirSeleccionSpiner) {
                    Configuration configuration = new Configuration();
                    int codigo = ((Idioma) parent.getAdapter().getItem(position)).getCodigo();
                    if (codigo == Idioma.ESPANIOL_id)
                        configuration.locale = Locale.getDefault();
                    else if (codigo == Idioma.INGLES_id)
                        configuration.locale = Locale.ENGLISH;

                    getResources().updateConfiguration(configuration, null);
                } else {
                    isPermitirSeleccionSpiner = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //-----------------------------------------------------------------------------------------------------
        ArrayList<Gender> alGenders = new ArrayList<Gender>();
        alGenders.add(new Gender(getResources().getString(R.string.item_male), Gender.MALE_id));
        alGenders.add(new Gender(getResources().getString(R.string.item_female), Gender.FEMALE_id));

        SpinnerGenderAdapter spinnerGenderAdapter=new SpinnerGenderAdapter(ActivityProfile.this, alGenders);
        spGender.setAdapter(spinnerGenderAdapter);
        //endregion

        try
        {
            Cursor cursorUser = userDAO.getCursorObtenerUser(ActivityProfile.this);
            if (cursorUser != null && cursorUser.getCount() > 0)
            {

                tvProfileNombre.setText(cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserDataContract.FIRSTNAME.getValue())));
                this.idFacebook = cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserDataContract.ID_FACEBOOK.getValue()));  // mantendremos esta variable para su uso en la actualización...
                etEmail.setText(cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserDataContract.EMAIL.getValue())));
                etPassword.setText(cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserDataContract.PASSWORD.getValue())));
                etRepeatPassword.setText(cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserDataContract.PASSWORD.getValue())));
                etFirstName.setText(cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserDataContract.FIRSTNAME.getValue())));
                etLastName.setText(cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserDataContract.LASTNAME.getValue())));
                spIdiomas.setSelection(Idioma.GETITEMPOSITION(cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserDataContract.LANGUAJE.getValue()))));
                spGender.setSelection(Gender.GETITEMPOSITION(cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserDataContract.GENDER.getValue())) ));
//                etGender.setText(cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserDataContract.GENDER.getValue())));
                etBirthDay.setText(cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserDataContract.BIRTHDAY.getValue())));
                etCountry.setText(cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserDataContract.COUNTRY.getValue())));
//                etEmail.setText(cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserDataContract.ID_FACEBOOK.getValue())));
            }
        } catch (SQLException e)
        {
            Utils.LOG("cursor profile : " + e.getMessage() );
        }

    }


    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void listenerTimer()
    {
    }



    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ivActionAbs:
                managerUtils.deslizadoVertical(llFuncionesActionBar, this, isMostrarListaAbs);
                isMostrarListaAbs = !isMostrarListaAbs;
                break;
            case R.id.btProfileGuardar:
//                public function ActualizarPerfilUsuario ($nickUsuario, $passwordUsuario, $nombreUsuario, $apellidosUsuario, $lenguajeUsuario, $generoUsuario, $ciudadUsuario)
                // brithday , country
                String firstName        = etFirstName.getText().toString();
                String email            = etEmail.getText().toString();
                String lastName         = etLastName.getText().toString();
                String gender           = ((Gender) spGender.getAdapter().getItem(spGender.getSelectedItemPosition())).getNombre();
                String password         = etPassword.getText().toString();
                String languaje         = ((Idioma) spIdiomas.getAdapter().getItem(spIdiomas.getSelectedItemPosition())).getNombre();
                String birthDay         = etBirthDay.getText().toString();
                String country          = etCountry.getText().toString();
                String repeatPassword   = etRepeatPassword.getText().toString();
                if (!(firstName.length() > 0 && email.length() > 0 && lastName.length() >0 && gender.length() > 0 && password.length() > 0 && languaje.length() > 0 && birthDay.length() > 0 && country.length() > 0 && repeatPassword.length() > 0))
                {
                    managerUtils.imprimirToast(this, "casillas incompletas");
                    return;
                }
                if (!password.equals(repeatPassword))
                {
                    etRepeatPassword.setError("no coinciden");
                    return;
                }

                user    = new User(this.idFacebook, firstName, email, lastName, gender, password, languaje, birthDay, country);
                new UpdateProfileUserAsyncTask(this, user).execute();

                break;
            case R.id.tvActionRetroceder:
                startActivity(new Intent(this, ActivityMenuPrincipal.class));
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public void onAfterTextChanged(EditText editText, String texto, int tamanio)
    {
        switch (editText.getId())
        {
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
            default:
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
        String formato = "dd/MM";
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, monthOfYear, dayOfMonth);
        String fechaSeleccionada = new SimpleDateFormat(formato, Locale.US).format(newDate.getTime());
        etBirthDay.setText(fechaSeleccionada);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            switch (v.getId())
            {
                case R.id.etBirthDay:
                    BirthDayDialog birthDayDialog = new BirthDayDialog(this, this);
                    birthDayDialog.getDatePickerBirthDay().show();
                    break;

            }
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return true;
    }

    @Override
    public void onUpdateProfileUser(LoginUser loginUser)
    {
        if (loginUser.isCorrecto())
        {
            try
            {
                if (userDAO.insertUser(this, user))
                    managerUtils.imprimirToast(this, loginUser.getDescription());
            } catch (SQLException e)
            {
                managerUtils.imprimirToast(this, "no se pudó actualizar el pérfil.");
            }
        }else
        {
            managerUtils.imprimirToast(this, loginUser.getDescription());
        }
    }
}



