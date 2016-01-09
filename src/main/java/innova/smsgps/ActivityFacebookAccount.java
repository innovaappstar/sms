package innova.smsgps;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
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

import innova.smsgps.application.Globals;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityFacebookAccount extends BaseActivity{

    TextView lblProfileNameUser;
    ProfilePictureView profilePictureView;

    /**
     * Clase para guardar y restaurar las sesiones
     */
    private UiLifecycleHelper uiHelper;

    /**Class GraphUser para obtener sus datos.*/
    private GraphUser user;

    innova.smsgps.beans.Session sessionbeans = new innova.smsgps.beans.Session();


    //region guardado y lectura de instancias guardadas.



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_account_apolo);
        lblProfileNameUser = (TextView) findViewById(R.id.lblProfileNameUser);
        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePictureUser);

    }
    boolean isFetching = false;
    private void performFacebookLogin()
    {
        Log.d("FACEBOOK", "performFacebookLogin");
        final Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, Arrays.asList("email"));
        Session openActiveSession = Session.openActiveSession(this, true, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                Log.d("FACEBOOK", "call");
                if (session.isOpened() && !isFetching)
                {
                    Log.d("FACEBOOK", "if (session.isOpened() && !isFetching)");
                    isFetching = true;
                    session.requestNewReadPermissions(newPermissionsRequest);
                    Request getMe = Request.newMeRequest(session, new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            Log.d("FACEBOOK", "onCompleted");
                            if (user != null)
                            {
                                ActivityFacebookAccount.this.user = user;
                                updateUI();

                            }
                        }
                    });
                    getMe.executeAsync();
                } else {
                    if (!session.isOpened())
                        Log.d("FACEBOOK", "!session.isOpened()");
                    else
                        Log.d("FACEBOOK", "isFetching");

                }
            }
        });
    }


    //region FACEBOOK
    /**
     * Callback para las sesiones.
     */
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            sessionbeans.setSession(session);
            Globals.setSession(session);
//            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();

    }
    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void listenerTimer() {

    }

    /**Manejado para acciones pendientes.*/

    /**Actualizaci�n de detalles de vista (Nombres , etc)*/
    private void updateUI() {
        sessionbeans.getSession().getActiveSession();

        if (user != null) {
//            pictureProfile.setProfileId(user.getId());
//            Globals.getInfoMovil().setSpf2(IDSP2.IDFACEBOOK, String.valueOf(user.getId()));
//            managerUtils.imprimirToast(this, "Te damos la bienvenida \n" + user + "\n a AlertaSms");
            profilePictureView.setProfileId(user.getId());
            lblProfileNameUser.setText("Hola\n" + user.getName());
        } else {
            profilePictureView.setProfileId(null);
            lblProfileNameUser.setText("");
        }
    }

    //endregion


    /**

     {
     "id":"483620641820436",
     "first_name":"Kenny",
     "timezone":-5,
     "verified":true,
     "name":"Kenny Baltazar Alanoca"
     ,"locale":"es_ES",
     "link":"https:\/\/www.facebook.com\/app_scoped_user_id\/483620641820436\/",
     "last_name":"Baltazar Alanoca",
     "gender":"male",
     "updated_time":"2015-12-05T00:28:36+0000"
     }
     */


    /**
     * Evento que se ejecutará al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnAceptar:
//                imprimitToast("Aceptar..");
                return;
            case R.id.contenedorFacebookAccount:
                performFacebookLogin();
                break;

        }
//        startActivityForResult(intent, 1);
    }


}



