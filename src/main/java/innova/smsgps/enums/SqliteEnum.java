package innova.smsgps.enums;

public enum SqliteEnum
{
	DATABASE_NAME("SMS"),
	DATABASE_VERSION("1");

	private String sValue;

	SqliteEnum(String sValue) {
		this.sValue = sValue;
	}

	public String getValue() {
		return sValue;
	}
}
