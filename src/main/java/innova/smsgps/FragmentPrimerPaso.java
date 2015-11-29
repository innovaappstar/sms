package innova.smsgps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

import innova.smsgps.application.Globals;
import innova.smsgps.enums.ACTIONS;
import innova.smsgps.enums.IDSP2;

/**
 * Created by innovaapps on 30/10/2015.
 */
public class FragmentPrimerPaso extends BaseFragment
{
    Dialog dialog;
    ProfilePictureView profilePictureView;
    LoginButton loginButton;
    TextView txtUserName;
    ViewGroup rootView;

    /**Permisos de la api de Facebook (Publicar)*/
    private static final String PERMISOS = "publish_actions";

    /**Nombre de la key para el guardado de instancias. SavedInstance- OnRestoreInstance.*/
    private final String PENDING_ACTION_BUNDLE_KEY = "innova.smsgps:PendingAction";

    /**Enumrable para las acciones*/
    private ACTIONS pendingAction = ACTIONS.NONE;

    /**
     * Clase para guardar y restaurar las sesiones
     */
    private UiLifecycleHelper uiHelper;

    /**Class GraphUser para obtener sus datos.*/
    private GraphUser user;

    innova.smsgps.beans.Session sessionbeans = new innova.smsgps.beans.Session();

    /**Cambios de estados o sesiones, muestra Toast de:  'no se le concedio permisos' */
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (pendingAction != ACTIONS.NONE &&
                (exception instanceof FacebookOperationCanceledException ||
                        exception instanceof FacebookAuthorizationException)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            pendingAction = ACTIONS.NONE;
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
//            handlePendingAction();
        }
        updateUI();
    }

    //region guardado y lectura de instancias guardadas.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    // SETEANDO ELEMENTOS
    private void IniciandoVistas()
    {
        ((Button)rootView.findViewById(R.id.btnAceptar)).setOnClickListener(this);
        // COMPROBAMOS SI TIENE UNA SESIÓN CON FACEBOOK
        if (!isSessionWithFacebook())
        {
//            mostrarDialogo();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = ACTIONS.valueOf(name);
        }
        rootView = (ViewGroup)inflater.inflate(R.layout.activity_config_paso_1, container, false);
        super.IniciarInstancias();
        IniciandoVistas();

        profilePictureView = (ProfilePictureView)rootView.findViewById(R.id.profilePicture);
        txtUserName = (TextView) rootView.findViewById(R.id.txtSaludo);
        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                FragmentPrimerPaso.this.user = user;
                updateUI();
            }
        });
        return rootView;
    }

    /**
     * Callback para las sesiones.
     */
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            sessionbeans.setSession(session);
            Globals.setSession(session);
            onSessionStateChange(session, state, exception);
        }
    };


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

    /**Actualizaci?n de detalles de vista (Nombres , etc)*/
    private void updateUI() {
        sessionbeans.getSession().getActiveSession();

        if (user != null) {
            profilePictureView.setProfileId(user.getId());
            Globals.getInfoMovil().setSpf2(IDSP2.IDFACEBOOK, String.valueOf(user.getId()));
            managerUtils.imprimirToast(getActivity(), "Te damos la bienvenida \n" + user.getFirstName() + "\n a AlertaSms");
            txtUserName.setText("Hola " + user.getFirstName());
            if (dialog == null)
                return;
            if (dialog.isShowing())
                dialog.cancel();
        } else {
            profilePictureView.setProfileId(null);
            txtUserName.setText("");
            managerUtils.imprimirToast(getActivity(), "Sesión no iniciada..");

//            pictureProfile.setProfileId(null);
//            mostrarDialogo();
        }
    }

    /**
     * POP UP SESSIÓN DE FACEBOOK
     **/
    private void mostrarDialogo()
    {
        //Create custom dialog object
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_pop_up_session_facebook);
        loginButton = (LoginButton) dialog.findViewById(R.id.login_button);
//        String datos = "Ha seleccionado al contacto \n" + NombreContacto + "\n?Que acci?n desea realizar?.";
//        ((TextView)dialog.findViewById(R.id.txtDetalleAplicativo)).setText(datos);
        // SETEAMOS VISTAS
//        LoginButton btnLogin = (LoginButton) dialog.findViewById(R.id.login_button);
//        btnLogin.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
//            @Override
//            public void onUserInfoFetched(GraphUser user) {
//                FragmentPrimerPaso.this.user = user;
//                updateUI();
//            }
//        });
        dialog.show();
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnAceptar:
                if (isSessionWithFacebook())
                {
                    managerUtils.imprimirToast(getActivity(), "Sesión iniciada correctamente..");
                }else
                {
                    managerUtils.imprimirToast(getActivity(), "Aún no inicia su sesión..");
                }
                break;
        }
    }

    /**
     * Simple función booleana que nos indicará
     * si tenemos guardada una sesión...
     **/

    private boolean isSessionWithFacebook()
    {
        if (sessionbeans.getSession() == null)
            return false;
        return  true;
    }

}
