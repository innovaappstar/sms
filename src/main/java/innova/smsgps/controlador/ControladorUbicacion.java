package innova.smsgps.controlador;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import innova.smsgps.beans.Coordenada;


public class ControladorUbicacion implements LocationListener{


	ControladorUbicacionCallback controladorUbicacionCallback;

	/**
	 * Interfaz que contendra el callback..
	 */
	public interface ControladorUbicacionCallback
	{
		void getCoordenada(Coordenada coordenada);
	}



	public ControladorUbicacion(ControladorUbicacionCallback controladorUbicacionCallback) {
		this.controladorUbicacionCallback = controladorUbicacionCallback;
	}

	@Override
	public void onLocationChanged(Location location) {

		if(location!=null)
		{
			Double d 	= location.getLatitude();
			int lat 	= d.intValue();
			if (lat != 0)
			{
				Coordenada coordenada = new Coordenada();
				coordenada.setLatitud(location.getLatitude());
				coordenada.setLongitud(location.getLongitude());
				coordenada.setVelocidad(location.getSpeed());
				coordenada.setPrecision(location.getAccuracy());
				controladorUbicacionCallback.getCoordenada(coordenada);
			}
		}
		
	}
	
	@Override
	public void onProviderDisabled(String provider) { }
	@Override
	public void onProviderEnabled(String provider) { }
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) { }


}
