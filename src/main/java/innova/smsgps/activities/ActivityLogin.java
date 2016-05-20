package innova.smsgps.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import innova.smsgps.R;
import innova.smsgps.entities.InfoProcesos;
import innova.smsgps.entities.User;
import innova.smsgps.task.LoginUserAsyncTask;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityLogin extends BaseActivity implements LoginUserAsyncTask.LoginUsuarioCallback
{

    User user = new User();

    // instancias facebook
    private UiLifecycleHelper uiLifecycleHelper;
    private GraphUser graphUser;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        uiLifecycleHelper = new UiLifecycleHelper(this, null);
        uiLifecycleHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((LoginButton) findViewById(R.id.lbIniciarSesionFacebook)).setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser graphUser) {
                ActivityLogin.this.graphUser = graphUser;
                sesionCallback(ActivityLogin.this.graphUser);
            }
        });

    }

    //region ciclos de vida (uiLifecycleHelper)
    @Override
    public void onPause() {
        super.onPause();
        uiLifecycleHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiLifecycleHelper.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        uiLifecycleHelper.onActivityResult(requestCode, resultCode, data, null);
    }
    //endregion



    /**
     * callback de sesión
     * @param graphUser GraphUser
     */
    private void sesionCallback(GraphUser graphUser)
    {
        if (graphUser != null)
        {
            String id           = (String) graphUser.getProperty("id");
            String firstName    = (String) graphUser.getProperty("first_name");
            String timeZone     = graphUser.getProperty("timezone").toString();
            String email        = (String) graphUser.getProperty("email");
            String verified     = graphUser.getProperty("verified").toString();
            String name         = (String) graphUser.getProperty("name");
            String locale       = (String) graphUser.getProperty("locale");
            String link         = (String) graphUser.getProperty("link");
            String lastName     = (String) graphUser.getProperty("last_name");
            String gender       = (String) graphUser.getProperty("gender");
            String updatedTime  = (String) graphUser.getProperty("updated_time");

            user    = new User(id, firstName, timeZone, email, verified, name, locale, link, lastName, gender, updatedTime);
            new LoginUserAsyncTask(this, user).execute();

            String sDatosUsuario = String.format("Se obtuvieron los siguientes datos %s\n%s\n%s\n%s" , user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
            managerUtils.imprimirLog(sDatosUsuario);
        }else
        {
            managerUtils.imprimirLog("El graphUser es null");
        }
    }

    @Override
    public void onLoginUser(InfoProcesos infoProcesos)
    {
        if (infoProcesos.isError())
        {
            managerUtils.imprimirToast(this, infoProcesos.getDetalleError());
        }else
        {
            managerUtils.imprimirToast(this, "login correcto.");
        }
    }

    @Override
    public void listenerTimer()
    {
    }


    public void onClick(View view)
    {

    }



}



