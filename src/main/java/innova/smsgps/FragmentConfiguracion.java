package innova.smsgps;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by innovaapps on 30/10/2015.
 */
public class FragmentConfiguracion extends BaseFragment
{
    ViewGroup rootView;
    //region Iniciando Fragments
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    // SETEANDO ELEMENTOS
    private void IniciandoVistas()
    {
        ((Button)rootView.findViewById(R.id.btnAceptar)).setOnClickListener(this);
    }
    //endregion Fragments Fin
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = (ViewGroup)inflater.inflate(R.layout.activity_info_configuracion, container, false);
        super.IniciarInstancias();
        IniciandoVistas();
        return rootView;
    }

    //endregion



    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnAceptar:
//                managerUtils.imprimirToast(getActivity(), "HOLA FRAGMENT HERENCIA");
                Intent intent=new Intent(getActivity(), ManagerFragmentsConfiguracion.class);
                startActivity(intent);
                break;
        }
    }




    @Override
    public void listenerTimer()
    {

    }

}
