package innova.smsgps.dialogs;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by USUARIO on 24/05/2016.
 */
public class BirthDayDialog extends DatePickerDialog implements DatePicker.OnDateChangedListener
{

    int theme = android.R.style.Theme_Holo_Light_Dialog_NoActionBar;
    private String formato = "dd, MMM";
    int[] date = new int[]{2016, 0, 11};
    private DatePickerDialog mDatePicker;

    @SuppressLint("NewApi")
    public BirthDayDialog(Context context, OnDateSetListener callBack) //, int year, int month, int day)
    {
        super(context, callBack, 2016, 0, 11);
        mDatePicker = new DatePickerDialog(context, this.theme, callBack, date[0], date[1], date[2]);
        mDatePicker.getDatePicker().init(date[0], date[1], date[2], this);
        actualizarTitulo(date[1], date[2]);
    }
    // listener de selección
    public void onDateChanged(DatePicker view, int year, int month, int day)
    {
        actualizarTitulo(day, month);
    }

    private void actualizarTitulo(int day, int month)
    {
        Calendar mCalendar = Calendar.getInstance();
//            mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mDatePicker.setTitle(getFormat().format(mCalendar.getTime()));

    }

    private DatePickerDialog getPicker()
    {
        return this.mDatePicker;
    }

    // El formato para el título del dialog..
    public SimpleDateFormat getFormat()
    {
        return new SimpleDateFormat(formato, Locale.getDefault());//"MMM, yyyy");
    }

    /**
     * Simple objeto picker que ocultará vistas hijas
     * que correspondan al field de yeard...
     * @return DatePicker
     */
    public DatePickerDialog getDatePickerBirthDay( )
    {
        String pcYear    = "mYearPicker";
        String spYear    = "mYearSpinner";
        String dpPicker  = "mDatePicker";
        DatePickerDialog datePickerDialog = this.getPicker();
        try{
            Field[] datePickerDialogFields = datePickerDialog.getClass().getDeclaredFields();
            for (Field datePickerDialogField : datePickerDialogFields)
            {
                if (datePickerDialogField.getName().equals(dpPicker))
                {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(datePickerDialog);
                    Field datePickerFields[] = datePickerDialogField.getType().getDeclaredFields();
                    for (Field datePickerField : datePickerFields)
                    {
                        if (pcYear.equals(datePickerField.getName()) || spYear.equals(datePickerField.getName()))
                        {
                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }

            }
        }catch(Exception ex)
        {
        }
        return datePickerDialog;
    }
}

