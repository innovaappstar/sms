package innova.smsgps.beans;

import java.util.Date;

public class Coordenada {

	public static Date FechaHora;
	public static double Latitud;
	public static double Longitud;
	public static float Velocidad;
	public static float Rodamiento; 
	public static int	IdAlerta;
	public static float Precision;
	public static int	Bateria; 
	public static int FrecuenciaPosteo; 
	 
	public static int getFrecuenciaPosteo() {
		return FrecuenciaPosteo;
	}
	public static void setFrecuenciaPosteo(int frecuenciaPosteo) {
		FrecuenciaPosteo = frecuenciaPosteo;
	}  
	public Date getFechaHora() {
		return FechaHora;
	}
	public void setFechaHora(Date FechaHora) {
		this.FechaHora= FechaHora;
	}
	public double getLatitud() {
		return Latitud;
	}
	public void setLatitud(double Latitud) {
		this.Latitud = Latitud;
	}
	public double getLongitud() {
		return Longitud;
	}
	public void setLongitud(double Longitud) {
		this.Longitud = Longitud;
	}
	public float getVelocidad() {
		return Velocidad * 3.6f;
	}
	public void setVelocidad(float Velocidad) {
		this.Velocidad = Velocidad;
	}	
	public float getRodamiendo() {
		return Rodamiento;
	}
	public void setRodamiento(float Rodamiento) {
		this.Rodamiento = Rodamiento;
	}
	public int getIdAlerta() {
		return IdAlerta;
	}
	public void setIdAlerta(int IdAlerta) {
		this.IdAlerta = IdAlerta;
	}
	public float getPrecision() {
		return Precision;
	}
	public void setPrecision(float Precision) {
		this.Precision = Precision;
	}
	public int getBateria() {
		return Bateria;
	}
	public void setBateria(int Bateria) {
		this.Bateria = Bateria;
	}
	// Parseado a String
	public String _getLatitud()
	{
		return (String.format("%.6f", getLatitud())).replace(',', '.');
	}
	public String _getLongitud()
	{
		return (String.format("%.6f", getLongitud())).replace(',', '.');  
	}	
	public String _getVelocidad()
	{
		return (String.format("%.1f", getVelocidad())).replace(',', '.');  
	}		 
	
}
