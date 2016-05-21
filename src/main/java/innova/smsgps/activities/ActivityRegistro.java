package innova.smsgps.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import innova.smsgps.R;
import innova.smsgps.base.adapters.SpinnerAdapter;
import innova.smsgps.entities.LoginUser;
import innova.smsgps.entities.User;
import innova.smsgps.task.LoginUserAsyncTask;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityRegistro extends BaseActivity implements LoginUserAsyncTask.LoginUsuarioCallback
{
    boolean isPermitirSeleccionSpiner = false;
    Spinner spIdiomas;
    User user = new User();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_registro);
        spIdiomas = (Spinner)findViewById(R.id.spIdiomas);
        //Android Custom Spinner Example Programmatically
        initCustomSpinner();

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

    private void initCustomSpinner()
    {

        // Spinner Drop down elements
        ArrayList<String> alIdiomas = new ArrayList<String>();
        alIdiomas.add("Español");
        alIdiomas.add("Ingles");
        SpinnerAdapter spinnerAdapter=new SpinnerAdapter(ActivityRegistro.this,alIdiomas);
        spIdiomas.setAdapter(spinnerAdapter);
        spIdiomas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isPermitirSeleccionSpiner)
                {
                    String item = parent.getItemAtPosition(position).toString();

                    Toast.makeText(parent.getContext(), item, Toast.LENGTH_SHORT).show();
                }else
                {
                    isPermitirSeleccionSpiner = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }







    @Override
    public void listenerTimer()
    {
    }


    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btRegistrarse:
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
            managerUtils.imprimirToast(this, loginUser.getDescription());
        }
    }

}



