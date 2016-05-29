package innova.smsgps.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

import java.util.Arrays;

import innova.smsgps.ActivityAutenticationFacebook;
import innova.smsgps.R;
import innova.smsgps.application.Globals;
import innova.smsgps.entities.User;
import innova.smsgps.enums.IDSP2;
import innova.smsgps.utils.Utils;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityAddAccountFacebook extends BaseActivity
{

    ProfilePictureView ivPictureFacebook;
    TextView tvNombreUser;

    User user = new User();

    // instancias facebook
    private UiLifecycleHelper uiLifecycleHelper;
    private GraphUser graphUser;
    innova.smsgps.beans.Session sessionbeans = new innova.smsgps.beans.Session();
    boolean isRespondido = false;
    Session openActiveSession ;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(getLayoutInflater().inflate(R.layout.abs_title, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

        uiLifecycleHelper = new UiLifecycleHelper(this, callback);
        uiLifecycleHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account_facebook);

        tvNombreUser        = (TextView) findViewById(R.id.tvNombreUser);
        ivPictureFacebook   = (ProfilePictureView) findViewById(R.id.ivPictureFacebook);
        iniciarLogIn();
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

    private Session.StatusCallback callback = new Session.StatusCallback()
    {
        @Override
        public void call(Session session, SessionState state, Exception exception)
        {
            sessionbeans.setSession(session);
            Utils.LOG("se seteo la sesión...");
            Globals.setSession(session);
        }
    };


    /**
     * Simple login con facebook
     */
    private void iniciarLogIn()
    {
        final Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, Arrays.asList("email"));
        openActiveSession = Session.openActiveSession(this, true, new Session.StatusCallback()
        {
            @Override
            public void call(final Session session, SessionState state, Exception exception) {
                if (session.isOpened() && !isRespondido)
                {
                    isRespondido = true;
                    session.requestNewReadPermissions(newPermissionsRequest);
                    Request getMe = Request.newMeRequest(session, new Request.GraphUserCallback()
                    {
                        @Override
                        public void onCompleted(GraphUser graphUser, Response response)
                        {
                            if (user != null)
                            {
                                ActivityAddAccountFacebook.this.graphUser = graphUser;
                                sesionCallback(ActivityAddAccountFacebook.this.graphUser);
                                // guardamos sesion....
                                sessionbeans.setSession(session);
                                Globals.setSession(session);
                            }
                            managerUtils.imprimirLog("onCompleted : " + response.toString());
                        }
                    });
                    getMe.executeAsync();
                } else
                {
//                    managerUtils.imprimirToast(ActivityAddAccountFacebook.this, "CONEXIÓN MANUAL");
//                    cerrarIntentoConexion();
                }
            }
        });
    }

    private void cerrarIntentoConexion()
    {
        if (openActiveSession != null)
            openActiveSession.close();
        startActivity(new Intent(ActivityAddAccountFacebook.this, ActivityAutenticationFacebook.class));
        finish();
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
            String idFacebook   = (String) graphUser.getProperty("id");
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

            user    = new User(idFacebook, firstName, timeZone, email, verified, name, locale, link, lastName, gender, updatedTime);
            Globals.getInfoMovil().setSpf2(IDSP2.IDFACEBOOK, idFacebook);
            tvNombreUser.setText(name);
            ivPictureFacebook.setProfileId(user.getIdFacebook());
        }else
        {
            managerUtils.imprimirLog("El graphUser es null");
        }
    }



    @Override
    public void listenerTimer()
    {
    }


    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tvRegistrarse:
                break;
        }
    }


}



