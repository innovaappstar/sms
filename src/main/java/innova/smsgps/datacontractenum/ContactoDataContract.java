package innova.smsgps.datacontractenum;

public enum ContactoDataContract
{
	TABLA		("TbContactos"),
	ID			("_id"),
	TELEFONO	("TELEFONO"),
	NOMBRE		("NOMBRE"),
	EMAIL		("EMAIL"),
	CODCONTACTO	("CODCONTACTO");

	private String s;

	ContactoDataContract(String s)
	{
		this.s = s;
	}

	public String getValue() {
		return s;
	}
}