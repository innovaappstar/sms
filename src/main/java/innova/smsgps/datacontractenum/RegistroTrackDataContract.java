package innova.smsgps.datacontractenum;

public enum RegistroTrackDataContract
{
	/**
	 * -IdTracking
	 -lat
	 -lng
	 -velocidad
	 -bateria
	 -fechaHora
	 -IdUsuario (se relacionará con la tabla contactos)
	 */
	TABLA		("TbTracking"),
	ID			("_id"),
	LATITUD		("LATITUD"),
	LONGITUD	("LONGITUD"),
	VELOCIDAD	("VELOCIDAD"),
	BATERIA		("BATERIA"),
	FECHAHORA	("FECHAHORA"),
	IDUSUARIO	("IDUSUARIO"),	// ALTERNATIVO - EMAIL USUARIO..
	TIPOTRACK	("TIPOTRACK");	// ALERTA - SEGUIMIENTO

	// CREAR TABLA SQLITE  DBHELPER
	// DAO
	// AGREGAR ATRIBUTOS EN REGISTRO TRACK

	private String s;

	RegistroTrackDataContract(String s)
	{
		this.s = s;
	}

	public String getValue() {
		return s;
	}
}