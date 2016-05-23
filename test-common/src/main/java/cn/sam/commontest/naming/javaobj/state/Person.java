package cn.sam.commontest.naming.javaobj.state;

public class Person {
	public String surname;
	public String commonName;
	public String passwd;
	public String phone;
	public String seeAlso;
	public String desc;

	public Person(String sn, String cn) {
		this(sn, cn, null, null, null, null);
	}

	public Person(String sn, String cn, String pw, String ph, String see,
			String d) {
		surname = sn;
		commonName = cn;
		passwd = pw;
		phone = ph;
		seeAlso = see;
		desc = d;
	}

	public String toString() {
		return "My name is " + surname + ", " + commonName + ".";
	}
}