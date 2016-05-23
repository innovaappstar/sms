package innova.smsgps.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import innova.smsgps.R;
import innova.smsgps.base.adapters.SpinnerAdapter;
import innova.smsgps.entities.Idioma;

import static android.app.DatePickerDialog.OnDateSetListener;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityProfile extends BaseActivity implements  OnDateSetListener
{
    LinearLayout llFuncionesActionBar;
    boolean isMostrarListaAbs = false;

    boolean isPermitirSeleccionSpiner = false;
    Spinner spIdiomas;

    //-----------------------
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    //----------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(getLayoutInflater().inflate(R.layout.abs_title, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

        setContentView(R.layout.activity_profile);
        llFuncionesActionBar = (LinearLayout)findViewById(R.id.llFuncionesActionBar);
        spIdiomas           = (Spinner)findViewById(R.id.spIdiomas);

        //region cargar spinner
        ArrayList<Idioma> alIdiomas = new ArrayList<Idioma>();
        alIdiomas.add(new Idioma(Idioma.SPANISH, Idioma.ESPANIOL));
        alIdiomas.add(new Idioma(Idioma.ENGLISH, Idioma.INGLES));

        SpinnerAdapter spinnerAdapter=new SpinnerAdapter(ActivityProfile.this, alIdiomas);
        spIdiomas.setAdapter(spinnerAdapter);
        spIdiomas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isPermitirSeleccionSpiner) {
                    Configuration configuration = new Configuration();
                    int codigo = ((Idioma) parent.getAdapter().getItem(position)).getCodigo();
                    if (codigo == Idioma.ESPANIOL)
                        configuration.locale = Locale.getDefault();
                    else if (codigo == Idioma.INGLES)
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
        //endregion

        dateFormatter = new SimpleDateFormat("dd/MM", Locale.US);

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

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, monthOfYear, dayOfMonth);
        managerUtils.imprimirToast(ActivityProfile.this, dateFormatter.format(newDate.getTime()));
    }


    //--------------------------------------------
    // clase customizada para mostrar un datepickerDialog - derivarla..
    class CustomDatePickerDialog extends DatePickerDialog implements DatePicker.OnDateChangedListener {

        private DatePickerDialog mDatePicker;

        @SuppressLint("NewApi")
        public CustomDatePickerDialog(Context context,int theme, OnDateSetListener callBack,
                                      int year, int monthOfYear, int dayOfMonth) {
            super(context, theme,callBack, year, monthOfYear, dayOfMonth);
            mDatePicker = new DatePickerDialog(context,theme,callBack, year, monthOfYear, dayOfMonth);

            mDatePicker.getDatePicker().init(2013, 7, 16, this);

            updateTitle(dayOfMonth, monthOfYear);

        }
        public void onDateChanged(DatePicker view, int year,
                                  int month, int day) {
            updateTitle(day, month);
        }
        private void updateTitle(int day, int month) {
            Calendar mCalendar = Calendar.getInstance();
//            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, day);
            mDatePicker.setTitle(getFormat().format(mCalendar.getTime()));

        }

        public DatePickerDialog getPicker(){

            return this.mDatePicker;
        }
        /*
         * the format for dialog tile,and you can override this method
         */
        public SimpleDateFormat getFormat(){
            return new SimpleDateFormat("dd, MMM", Locale.getDefault());//"MMM, yyyy");
        }
    }



    //----------------------------------------------










    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ivActionAbs:
                managerUtils.deslizadoVertical(llFuncionesActionBar, this, isMostrarListaAbs);
                isMostrarListaAbs = !isMostrarListaAbs;
                break;
            case R.id.btProfileGuardar:
//                CustomDatePickerDialog dp = new CustomDatePickerDialog(this , android.R.style.Theme_Holo_Light_Dialog,  this, 1, 1, 22);
                CustomDatePickerDialog dp = new CustomDatePickerDialog(this , android.R.style.Theme_Holo_Light_Dialog_NoActionBar,  this, 1, 1, 22);
                // oculta la vista yeard
                DatePickerDialog obj = dp.getPicker();
                try{
                    Field[] datePickerDialogFields = obj.getClass().getDeclaredFields();
                    for (Field datePickerDialogField : datePickerDialogFields) {
                        if (datePickerDialogField.getName().equals("mDatePicker")) {
                            datePickerDialogField.setAccessible(true);
                            DatePicker datePicker = (DatePicker) datePickerDialogField.get(obj);
                            Field datePickerFields[] = datePickerDialogField.getType().getDeclaredFields();
                            for (Field datePickerField : datePickerFields)
                            {
                                if ("mYearPicker".equals(datePickerField.getName()) || "mYearSpinner".equals(datePickerField.getName()))    // mDayPicker-mDaySpinner
                                {
                                    datePickerField.setAccessible(true);
                                    Object dayPicker = new Object();
                                    dayPicker = datePickerField.get(datePicker);
                                    ((View) dayPicker).setVisibility(View.GONE);
                                }
                            }
                        }

                    }
                }catch(Exception ex){
                }
                obj.show();

                break;

            default:
                break;
        }
    }





}



