package innova.smsgps.datacontractenum;

public enum UserDataContract
{
	TABLA		("TbUser"),
	ID			("_id"),
	EMAIL		("EMAIL"),
	PASSWORD	("PASSWORD"),
	FIRSTNAME	("FIRSTNAME"),
	LASTNAME	("LASTNAME"),
	LANGUAJE	("LANGUAJE"),
	GENDER		("GENDER"),
	BIRTHDAY	("BIRTHDAY"),
	COUNTRY		("COUNTRY"),
	ID_FACEBOOK	("ID_FACEBOOK");

	private String s;

	UserDataContract(String s)
	{
		this.s = s;
	}

	public String getValue() {
		return s;
	}
}