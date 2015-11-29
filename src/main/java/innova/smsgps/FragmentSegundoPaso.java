package innova.smsgps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by innovaapps on 30/10/2015.
 */
public class FragmentSegundoPaso extends BaseFragment
{
    ViewGroup rootView;
    List<String> listCategorias = new ArrayList<String>();
    Spinner mSpinner1, mSpinner2, mSpinner3;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = (ViewGroup)inflater.inflate(R.layout.activity_config_paso_2, container, false);
        super.IniciarInstancias();
        IniciandoVistas();
        return rootView;
    }

    private void IniciandoVistas()
    {
        ((Button)rootView.findViewById(R.id.btnAceptar)).setOnClickListener(this);
        mSpinner1     = (Spinner)rootView.findViewById(R.id.spinner1);
        mSpinner2     = (Spinner)rootView.findViewById(R.id.spinner2);
        mSpinner3     = (Spinner)rootView.findViewById(R.id.spinner3);
        cargarCategorias();
    }

    private void cargarCategorias()
    {
        listCategorias.add("Asalto");
        listCategorias.add("Accidente");
        listCategorias.add("Emergencia");
        ArrayAdapter<String> datosSpinner = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,listCategorias);
        datosSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner1.setAdapter(datosSpinner);
        mSpinner2.setAdapter(datosSpinner);
        mSpinner3.setAdapter(datosSpinner);
    }

    @Override
    public void listenerTimer() {

    }

    @Override
    public void onClick(View v) {

    }
}
